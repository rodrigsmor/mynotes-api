package com.rm.mynotes.service.impl;

import com.rm.mynotes.model.CollectionNotes;
import com.rm.mynotes.model.UserEntity;
import com.rm.mynotes.repository.UserRepository;
import com.rm.mynotes.service.mold.CollectionService;
import com.rm.mynotes.utils.constants.RoutePaths;
import com.rm.mynotes.utils.dto.payloads.ResponseDTO;
import com.rm.mynotes.utils.dto.requests.CollectionDTO;
import com.rm.mynotes.utils.functions.CollectionMethods;
import com.rm.mynotes.utils.functions.CommonFunctions;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class CollectionServiceImplementation implements CollectionService {
    @Autowired
    private CommonFunctions commonFunctions;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CollectionMethods collectionMethods;

    @Override
    public ResponseEntity<ResponseDTO> getFavorites(Authentication authentication) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> getCollection(Authentication authentication, Long id) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> deleteCollection(Authentication authentication, Long id) {
        return null;
    }

    @Override
    public ResponseEntity<ResponseDTO> getCollections(Authentication authentication, Integer pageNumber) {
        return null;
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
