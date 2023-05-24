package com.rm.mynotes.service.impl;

import com.rm.mynotes.model.Annotation;
import com.rm.mynotes.model.CollectionNotes;
import com.rm.mynotes.model.UserEntity;
import com.rm.mynotes.repository.AnnotationRepository;
import com.rm.mynotes.repository.CollectionRepository;
import com.rm.mynotes.repository.UserRepository;
import com.rm.mynotes.service.mold.AnnotationService;
import com.rm.mynotes.utils.constants.CategoryTypes;
import com.rm.mynotes.utils.constants.OrdinationTypes;
import com.rm.mynotes.utils.constants.RoutePaths;
import com.rm.mynotes.utils.dto.payloads.AnnotationSummaryDTO;
import com.rm.mynotes.utils.dto.payloads.CollectionSummaryDTO;
import com.rm.mynotes.utils.dto.payloads.ResponseDTO;
import com.rm.mynotes.utils.dto.requests.AnnotationDTO;
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
import org.springframework.scheduling.annotation.Scheduled;
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
public class AnnotationServiceImplementation implements AnnotationService {
    @Autowired
    public CommonFunctions commonFunctions;
    @Autowired
    public AnnotationMethods annotationMethods;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CollectionRepository collectionRepository;
    @Autowired
    public AnnotationRepository annotationRepository;

    private final Integer pageElementsSize = 16;

    @Override
    @Scheduled(cron = "0 0 0 * * *")
    public void excludeNotesThatHaveReachedDeadline() {

    }

    @Override
    @Transactional
    public ResponseEntity<ResponseDTO> deleteAnnotation(Authentication authentication, Long noteId, Boolean isPermanent) {
        try {
            ResponseDTO responseDTO = new ResponseDTO();
            UserEntity user = commonFunctions.getCurrentUser(authentication);

            if (userRepository.getAnnotationBelongsToUser(user.getId(), noteId) == 0) throw new Exception("A anotação informada não pertence ao usuário atual!");
            Annotation annotation = annotationRepository.findById(noteId).orElseThrow(() -> new Exception("A anotação informada não existe"));

            if (isPermanent) {
                annotationRepository.delete(annotation);
                responseDTO.setMessage("A anotação foi excluída permanentemente. Desse modo, não será possível o recuperar.");
            } else {
                annotation.setDeletionDate(CommonFunctions.getCurrentDatetime());
                annotation.setIsExcluded(true);
                responseDTO.setMessage("A anotação foi excluída e movida a sua lixeira.");
                annotationRepository.saveAndFlush(annotation);
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

            Annotation annotation = annotationRepository.getReferenceById(noteId);
            CollectionNotes collection = collectionRepository.getReferencedById(collectionId);

            if (collectionRepository.existsOnCollection(noteId, collectionId) > 0) throw new Exception("A anotação já foi adicionada a coleção.");
            handleCollectionAnnotationErrors(user.getId(), noteId, collectionId);

            List<Annotation> collectionAnnotations = collection.getAnnotations();
            collectionAnnotations.add(annotation);

            collection.setAnnotations(collectionAnnotations);

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
            Annotation annotation = annotationRepository.getReferenceById(noteId);
            CollectionNotes collection = collectionRepository.getReferencedById(collectionId);

            handleCollectionAnnotationErrors(user.getId(), noteId, collectionId);
            if (collectionRepository.existsOnCollection(noteId, collectionId) == 0) throw new Exception("A anotação não existe na coleção informada.");

            List<Annotation> collectionAnnotations = collection.getAnnotations();
            collectionAnnotations.remove(annotation);

            collection.setAnnotations(collectionAnnotations);

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
            List<AnnotationSummaryDTO> annotations = annotationMethods.sortAndFilterAnnotations(user, ordination, categories, orderBy, endDate, startDate);

            int startIndex = (int) pageRequest.getOffset();
            int endIndex = Math.min(startIndex + pageRequest.getPageSize(), annotations.size());
            List<AnnotationSummaryDTO> pageElements = annotations.subList(startIndex, endIndex);

            Page<AnnotationSummaryDTO> paginatedAnnotations = new PageImpl<>(pageElements, pageRequest, annotations.size());

            responseDTO.setSuccess(true);
            responseDTO.setData(paginatedAnnotations);

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception exception) {
            return CommonFunctions.errorHandling(exception);
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> getAnnotation(Authentication authentication, Long noteId) {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            UserEntity user = commonFunctions.getCurrentUser(authentication);
            boolean belongsToTheUser = user.getAnnotations().stream().anyMatch(annotation -> Objects.equals(annotation.getId(), noteId));
            if (!belongsToTheUser) throw new Error("A anotação informada não pertence ao seu usuário.");

            Annotation annotation = annotationRepository.getReferenceById(noteId);

            responseDTO.setSuccess(true);
            responseDTO.setData(annotation);

            return ResponseEntity.accepted().body(responseDTO);
        } catch (Exception exception) {
            return CommonFunctions.errorHandling(exception);
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> createAnnotation(Authentication authentication, AnnotationDTO annotationDTO) {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            UserEntity user = commonFunctions.getCurrentUser(authentication);
            List<Annotation> annotations = user.getAnnotations();

            Annotation annotationCreated = annotationMethods.createAnnotation(annotationDTO);

            annotations.add(annotationCreated);
            user.setAnnotations(annotations);

            List<Annotation> AnnotationCreated = userRepository.save(user).getAnnotations().stream().filter(annotation -> annotation.getId().equals(annotationCreated.getId())).toList();

            responseDTO.setSuccess(true);
            responseDTO.setMessage("Anotação criada com sucesso!");
            responseDTO.setData(AnnotationCreated.get(0));

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

    private void handleCollectionAnnotationErrors(Long userId, Long noteId, Long collectionId) throws Exception {
        if (userRepository.getAnnotationBelongsToUser(userId, noteId) == 0) throw new Exception("A anotação informada não pertence ao usuário atual!");
        if (userRepository.getCollectionBelongsToUser(userId, collectionId) == 0) throw new Exception("A coleção informada não pertence ao usuário atual ou não existe!");
    }
}
