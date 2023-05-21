package com.rm.mynotes.service.mold;

import com.rm.mynotes.utils.dto.payloads.ResponseDTO;
import com.rm.mynotes.utils.dto.requests.CollectionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

public interface CollectionService {
    ResponseEntity<ResponseDTO> getFavorites(Authentication authentication);
    ResponseEntity<ResponseDTO> getCollection(Authentication authentication, Long collectionId);
    ResponseEntity<ResponseDTO> deleteCollection(Authentication authentication, Long collectionId);
    ResponseEntity<ResponseDTO> getCollections(Authentication authentication, Integer pageNumber);
    ResponseEntity<ResponseDTO> createCollection(Authentication authentication, MultipartFile cover, CollectionDTO collectionDTO);
}
