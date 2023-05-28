package com.rm.mynotes.service.impl;

import com.rm.mynotes.model.CollectionNotes;
import com.rm.mynotes.model.UserEntity;
import com.rm.mynotes.repository.CollectionRepository;
import com.rm.mynotes.repository.UserRepository;
import com.rm.mynotes.service.mold.CollectionService;
import com.rm.mynotes.utils.constants.CategoryTypes;
import com.rm.mynotes.utils.constants.OrdinationTypes;
import com.rm.mynotes.utils.constants.RoutePaths;
import com.rm.mynotes.utils.dto.payloads.NoteSummaryDTO;
import com.rm.mynotes.utils.dto.payloads.CollectionSummaryDTO;
import com.rm.mynotes.utils.dto.payloads.ResponseDTO;
import com.rm.mynotes.utils.dto.requests.CollectionDTO;
import com.rm.mynotes.utils.functions.CollectionMethods;
import com.rm.mynotes.utils.functions.CommonFunctions;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class CollectionServiceImplementation implements CollectionService {
    @Autowired
    private CommonFunctions commonFunctions;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CollectionMethods collectionMethods;
    private final CollectionRepository collectionRepository;

    @Override
    public ResponseEntity<ResponseDTO> pinCollection(Authentication authentication, Long collectionId) {
        try {
            UserEntity user = commonFunctions.getCurrentUser(authentication);
            CollectionNotes collection = collectionRepository.getReferencedById(collectionId);

            if (userRepository.getCollectionBelongsToUser(user.getId(), collectionId) == 0) throw new Exception("A coleção informada não pertence ao usuário atual ou não existe!");
            if (collection.getIsPinned()) throw new Exception("A coleção já está fixada.");
            collection.setIsPinned(true);

            CollectionSummaryDTO collectionUpdated = new CollectionSummaryDTO(collectionRepository.save(collection));
            collectionUpdated.setNumberOfNotes(collectionRepository.getAmountOfNotesInCollection(collection.getId()));

            return ResponseEntity.accepted().body(new ResponseDTO("Coleção fixada", true, collectionUpdated));
        } catch (Exception exception) {
            return CommonFunctions.errorHandling(exception);
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> unpinCollection(Authentication authentication, Long collectionId) {
        try {
            UserEntity user = commonFunctions.getCurrentUser(authentication);
            CollectionNotes collection = collectionRepository.getReferencedById(collectionId);

            if (userRepository.getCollectionBelongsToUser(user.getId(), collectionId) == 0) throw new Exception("A coleção informada não pertence ao usuário atual ou não existe!");
            if (!collection.getIsPinned()) throw new Exception("A coleção não está fixada.");
            collection.setIsPinned(false);

            CollectionSummaryDTO collectionUpdated = new CollectionSummaryDTO(collectionRepository.save(collection));
            collectionUpdated.setNumberOfNotes(collectionRepository.getAmountOfNotesInCollection(collection.getId()));

            return ResponseEntity.accepted().body(new ResponseDTO("Coleção não está mais fixada.", true, collectionUpdated));
        } catch (Exception exception) {
            return CommonFunctions.errorHandling(exception);
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> getPinnedCollections(Authentication authentication) {
        try {
            UserEntity user = commonFunctions.getCurrentUser(authentication);

            Set<CollectionSummaryDTO> collections = user.getCollections().stream()
                    .filter(CollectionNotes::getIsPinned)
                    .map(collection -> {
                        CollectionSummaryDTO collectionSummary = new CollectionSummaryDTO(collection);
                        collectionSummary.setNumberOfNotes(collectionRepository.getAmountOfNotesInCollection(collection.getId()));
                        return collectionSummary;
                    })
                    .collect(Collectors.toSet());

            ResponseDTO responseDTO = new ResponseDTO("", true, collections);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception exception) {
            return CommonFunctions.errorHandling(exception);
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> getCollections(Authentication authentication, String ordination, List<CategoryTypes> categories, OrdinationTypes orderBy, Integer currentPage) {
        try {
            UserEntity user = commonFunctions.getCurrentUser(authentication);

            int pageElementsSize = 16;
            PageRequest pageRequest = PageRequest.of(currentPage, pageElementsSize);
            List<CollectionSummaryDTO> collections = collectionMethods.sortAndFilterCollections(user, ordination, categories, orderBy);

            int startIndex = (int) pageRequest.getOffset();
            int endIndex = Math.min(startIndex + pageRequest.getPageSize(), collections.size());
            List<CollectionSummaryDTO> pageElements = collections.subList(startIndex, endIndex);

            Page<CollectionSummaryDTO> paginatedCollections = new PageImpl<>(pageElements, pageRequest, collections.size());

            ResponseDTO responseDTO = new ResponseDTO("", true, paginatedCollections);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception exception) {
            return CommonFunctions.errorHandling(exception);
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> getCollection(Authentication authentication, Long id) {
        try {
            UserEntity user = commonFunctions.getCurrentUser(authentication);
            if (userRepository.getCollectionBelongsToUser(user.getId(), id) == 0) throw new Exception("A coleção informada não pertence ao usuário atual ou não existe!");

            CollectionNotes collection = collectionRepository.getReferencedById(id);
            CollectionSummaryDTO collectionSummaryDTO = new CollectionSummaryDTO(collection);
            collectionSummaryDTO.setNumberOfNotes(collectionRepository.getAmountOfNotesInCollection(collection.getId()));

            Set<NoteSummaryDTO> notes = collection.getNotes()
                    .stream().filter(note -> !note.getIsExcluded())
                    .map(NoteSummaryDTO::new).collect(Collectors.toSet());

            Map<String, Object> data = new HashMap<>();
            data.put("notes", notes);
            data.put("collection", collectionSummaryDTO);

            ResponseDTO responseDTO = new ResponseDTO("", true, data);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception exception) {
            return CommonFunctions.errorHandling(exception);
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> editCollection(Authentication authentication, Long collectionId, CollectionDTO collectionDTO) {
        try {
            UserEntity user = commonFunctions.getCurrentUser(authentication);
            if (userRepository.getCollectionBelongsToUser(user.getId(), collectionId) == 0) throw new Exception("A coleção informada não pertence ao usuário atual ou não existe!");

            CollectionNotes existingCollection = collectionRepository.findById(collectionId)
                    .orElseThrow(() -> new Exception("A coleção informada não existe!"));

            existingCollection.setName(Objects.nonNull(collectionDTO.getName()) ? collectionDTO.getName() : existingCollection.getName());
            existingCollection.setIsPinned(Objects.nonNull(collectionDTO.getIsPinned()) ? collectionDTO.getIsPinned() : existingCollection.getIsPinned());
            existingCollection.setCategory(Objects.nonNull(collectionDTO.getCategory()) ? collectionDTO.getCategory() : existingCollection.getCategory());

            CollectionSummaryDTO collectionUpdated = new CollectionSummaryDTO(collectionRepository.save(existingCollection));
            collectionUpdated.setNumberOfNotes(collectionRepository.getAmountOfNotesInCollection(collectionUpdated.getId()));

            ResponseDTO responseDTO = new ResponseDTO("Coleção atualizada com sucesso.", true, collectionUpdated);
            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception exception) {
            return CommonFunctions.errorHandling(exception);
        }
    }

    @Override
    @Transactional
    public ResponseEntity<ResponseDTO> deleteCollection(Authentication authentication, Long collectionId) {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            UserEntity user = commonFunctions.getCurrentUser(authentication);
            if (userRepository.getCollectionBelongsToUser(user.getId(), collectionId) == 0) throw new Exception("A coleção informada não pertence ao usuário atual ou não existe!");

            CollectionNotes collection = collectionRepository.getReferencedById(collectionId);

            if (collection.getIsFavorite()) throw new Exception("Não é possível excluir seus favoritos.");

            collectionRepository.deleteRelatedUserCollections(collectionId);

            collection.setNotes(new ArrayList<>());
            collectionRepository.delete(collection);

            responseDTO.setSuccess(true);
            responseDTO.setMessage("Coleção excluída com êxito.");

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception exception) {
            return CommonFunctions.errorHandling(exception);
        }
    }

    @Override
    public ResponseEntity<ResponseDTO> createCollection(Authentication authentication, MultipartFile cover, CollectionDTO collectionDTO) {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            UserEntity user = commonFunctions.getCurrentUser(authentication);
            Set<CollectionNotes> userCollections = user.getCollections();
            CollectionNotes collection = collectionMethods.createCollections(collectionDTO, cover);

            userCollections.add(collection);
            user.setCollections(userCollections);

            user = userRepository.save(user);

            responseDTO.setSuccess(true);
            responseDTO.setData(collection);
            responseDTO.setMessage("Sua coleção foi criada!");

            URI uri = URI.create(ServletUriComponentsBuilder.fromCurrentContextPath().path(RoutePaths.CREATE_NEW_COLLECTION).toUriString());

            return ResponseEntity.created(uri).body(responseDTO);
        } catch (Exception exception) {
            return CommonFunctions.errorHandling(exception);
        }
    }
}
