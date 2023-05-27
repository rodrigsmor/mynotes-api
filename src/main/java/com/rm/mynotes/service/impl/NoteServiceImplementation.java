package com.rm.mynotes.service.impl;

import com.rm.mynotes.model.Note;
import com.rm.mynotes.model.CollectionNotes;
import com.rm.mynotes.model.UserEntity;
import com.rm.mynotes.repository.NoteRepository;
import com.rm.mynotes.repository.CollectionRepository;
import com.rm.mynotes.repository.UserRepository;
import com.rm.mynotes.service.mold.NoteService;
import com.rm.mynotes.utils.constants.CategoryTypes;
import com.rm.mynotes.utils.constants.OrdinationTypes;
import com.rm.mynotes.utils.constants.RoutePaths;
import com.rm.mynotes.utils.dto.payloads.*;
import com.rm.mynotes.utils.dto.requests.NoteDTO;
import com.rm.mynotes.utils.errors.CustomExceptions;
import com.rm.mynotes.utils.functions.AnnotationMethods;
import com.rm.mynotes.utils.functions.CommonFunctions;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

@Data
@Service
@Slf4j
@RequiredArgsConstructor
public class NoteServiceImplementation implements NoteService {
    @Autowired
    public CommonFunctions commonFunctions;
    @Autowired
    public AnnotationMethods annotationMethods;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CollectionRepository collectionRepository;
    @Autowired
    public NoteRepository noteRepository;

    private final Integer pageElementsSize = 16;

    @Override
    @Transactional
    public ResponseEntity<ResponseDTO> deleteAnnotation(Authentication authentication, Long noteId, Boolean isPermanent) {
        try {
            ResponseDTO responseDTO = new ResponseDTO();
            UserEntity user = commonFunctions.getCurrentUser(authentication);

            if (userRepository.getAnnotationBelongsToUser(user.getId(), noteId) == 0) throw new Exception("A anotação informada não pertence ao usuário atual!");
            Note note = noteRepository.findById(noteId).orElseThrow(() -> new Exception("A anotação informada não existe"));

            if (isPermanent) {
                user.getNotes().remove(note);
                userRepository.saveAndFlush(user);
                noteRepository.delete(note);
                responseDTO.setMessage("A anotação foi excluída permanentemente. Desse modo, não será possível o recuperar.");
            } else {
                note.setDeletionDate(CommonFunctions.getCurrentDatetime());
                note.setIsExcluded(true);
                responseDTO.setMessage("A anotação foi excluída e movida a sua lixeira.");
                noteRepository.saveAndFlush(note);
            }

            responseDTO.setSuccess(true);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception exception) {
            return CommonFunctions.errorHandling(exception);
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> addsAnnotationToCollection(Authentication authentication, Long noteId, Long collectionId) {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            UserEntity user = commonFunctions.getCurrentUser(authentication);

            Note note = noteRepository.getReferenceById(noteId);
            CollectionNotes collection = collectionRepository.getReferencedById(collectionId);

            if(note.getIsExcluded()) throw new Exception("A anotação não existe.");
            if (collectionRepository.existsOnCollection(noteId, collectionId) > 0) throw new Exception("A anotação já foi adicionada a coleção.");
            handleCollectionAnnotationErrors(user.getId(), noteId, collectionId);

            List<Note> collectionNotes = collection.getNotes();
            collectionNotes.add(note);

            collection.setNotes(collectionNotes);

            CollectionSummaryDTO collectionUpdated = new CollectionSummaryDTO(collectionRepository.save(collection));
            collectionUpdated.setNumberOfNotes(collectionRepository.getAmountOfAnnotationsInCollection(collectionUpdated.getId()));

            HashMap<String, Object> data = new HashMap<>();

            data.put("collection", collectionUpdated);

            responseDTO.setData(data);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("A adição foi realizada com êxito!");

            return ResponseEntity.accepted().body(responseDTO);
        } catch (Exception exception) {
            return CommonFunctions.errorHandling(exception);
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> removeAnnotationFromCollection(Authentication authentication, Long noteId, Long collectionId) {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            UserEntity user = commonFunctions.getCurrentUser(authentication);
            Note note = noteRepository.getReferenceById(noteId);
            CollectionNotes collection = collectionRepository.getReferencedById(collectionId);

            handleCollectionAnnotationErrors(user.getId(), noteId, collectionId);
            if (collectionRepository.existsOnCollection(noteId, collectionId) == 0) throw new Exception("A anotação não existe na coleção informada.");

            List<Note> collectionNotes = collection.getNotes();
            collectionNotes.remove(note);

            collection.setNotes(collectionNotes);

            CollectionSummaryDTO collectionUpdated = new CollectionSummaryDTO(collectionRepository.save(collection));
            collectionUpdated.setNumberOfNotes(collectionRepository.getAmountOfAnnotationsInCollection(collectionUpdated.getId()));

            HashMap<String, Object> data = new HashMap<>();
            data.put("collection", collectionUpdated);

            responseDTO.setData(data);
            responseDTO.setSuccess(true);
            responseDTO.setMessage("A anotação foi removida da coleção.");

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception exception) {
            return CommonFunctions.errorHandling(exception);
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> getAllAnnotations(Authentication authentication, String ordination, List<CategoryTypes> categories, OrdinationTypes orderBy, Integer currentPage, String endDate, String startDate) {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            UserEntity user = commonFunctions.getCurrentUser(authentication);

            PageRequest pageRequest = PageRequest.of(currentPage, pageElementsSize);
            List<NoteSummaryDTO> annotations = annotationMethods.sortAndFilterAnnotations(user, ordination, categories, orderBy, endDate, startDate);

            int startIndex = (int) pageRequest.getOffset();
            int endIndex = Math.min(startIndex + pageRequest.getPageSize(), annotations.size());
            List<NoteSummaryDTO> pageElements = annotations.subList(startIndex, endIndex);

            Page<NoteSummaryDTO> paginatedAnnotations = new PageImpl<>(pageElements, pageRequest, annotations.size());

            responseDTO.setSuccess(true);
            responseDTO.setData(paginatedAnnotations);

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception exception) {
            return CommonFunctions.errorHandling(exception);
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> getAnnotation(Authentication authentication, Long noteId) {
        try {
            UserEntity user = commonFunctions.getCurrentUser(authentication);
            boolean belongsToUser = user.getNotes().stream().anyMatch(annotation -> Objects.equals(annotation.getId(), noteId));
            if (!belongsToUser) throw new CustomExceptions("A anotação informada não existe ou não pertence ao seu usuário.");

            Note note = noteRepository.getReferenceById(noteId);
            List<CollectionSummaryDTO> collectionNotes = annotationMethods.getCollectionsThatHaveTheAnnotation(noteId);

            NoteDetailedDTO noteDetailedDTO = new NoteDetailedDTO(note);
            noteDetailedDTO.setAnnotationCollections(collectionNotes);
            ResponseDTO responseDTO = new ResponseDTO("", true, noteDetailedDTO);

            if (note.getIsExcluded()) {
                DeletedNoteDetailedDTO deletedNote = new DeletedNoteDetailedDTO(note);
                deletedNote.setAnnotationCollections(collectionNotes);
                responseDTO.setData(deletedNote);
            }

            return ResponseEntity.accepted().body(responseDTO);
        } catch (Exception exception) {
            return CommonFunctions.errorHandling(exception);
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> createAnnotation(Authentication authentication, NoteDTO noteDTO) {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            UserEntity user = commonFunctions.getCurrentUser(authentication);
            List<Note> notes = user.getNotes();

            Note noteCreated = annotationMethods.createAnnotation(noteDTO);

            notes.add(noteCreated);
            user.setNotes(notes);

            List<Note> notesUpdated = userRepository.save(user).getNotes().stream().filter(note -> note.getId().equals(noteCreated.getId())).toList();

            responseDTO.setSuccess(true);
            responseDTO.setMessage("Anotação criada com sucesso!");
            responseDTO.setData(notesUpdated.get(0));

            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(RoutePaths.CREATE_NOTE).toUriString());
            return ResponseEntity.created(uri).body(responseDTO);
        } catch (CustomExceptions customExceptions) {
            responseDTO.setSuccess(false);
            responseDTO.setMessage(customExceptions.getMessage());
            
            return ResponseEntity.internalServerError().body(responseDTO);
        } catch (Exception exception) {
            return CommonFunctions.errorHandling(exception);
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> getDeletedNotes(Authentication authentication) {
        try {
            UserEntity user = commonFunctions.getCurrentUser(authentication);
            List<SummaryDeletedNoteDTO> annotationsDeleted = user.getNotes().stream().filter(Note::getIsExcluded).map(SummaryDeletedNoteDTO::new).toList();

            return ResponseEntity.accepted().body(new ResponseDTO("", true, annotationsDeleted));
        } catch (Exception exception) {
            return CommonFunctions.errorHandling(exception);
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> recoverDeletedNote(Authentication authentication, Long noteId) {
        try {
            UserEntity user = commonFunctions.getCurrentUser(authentication);

            Note note = user.getNotes()
                    .stream().filter(noteItem -> Objects.equals(noteItem.getId(), noteId))
                    .findFirst().orElseThrow(() -> new CustomExceptions("Não existe ou não pertence ao seu usuário."));

            if (!note.getIsExcluded()) throw new CustomExceptions("Não há necessidade de recuperar, pois a anotação informada não foi sequer excluída.");

            note.setIsExcluded(false);
            note.setDeletionDate(null);
            note.setLastUpdate(CommonFunctions.getCurrentDatetime());

            Note noteUpdated = noteRepository.save(note);

            return ResponseEntity.accepted().body(new ResponseDTO("", true, noteUpdated));
        } catch (Exception exception) {
            return CommonFunctions.errorHandling(exception);
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> emptyTrash(Authentication authentication) {
        try {
            UserEntity user = commonFunctions.getCurrentUser(authentication);

            List<Note> annotationsToDelete = user.getNotes()
                    .stream().filter(Note::getIsExcluded).toList();

            if(annotationsToDelete.size() == 0) throw new CustomExceptions("A lixeira já esta vazia.");
            annotationMethods.deletePermanentlyNotes(annotationsToDelete);

            return ResponseEntity.ok().body(new ResponseDTO("A sua lixeira foi limpa completamente.", true, null));
        } catch (Exception exception) {
            return CommonFunctions.errorHandling(exception);
        }
    }

    private void handleCollectionAnnotationErrors(Long userId, Long noteId, Long collectionId) throws Exception {
        if (userRepository.getAnnotationBelongsToUser(userId, noteId) == 0) throw new Exception("A anotação informada não pertence ao usuário atual!");
        if (userRepository.getCollectionBelongsToUser(userId, collectionId) == 0) throw new Exception("A coleção informada não pertence ao usuário atual ou não existe!");
    }
}
