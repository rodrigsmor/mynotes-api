package com.rm.mynotes.service.mold;

import com.rm.mynotes.utils.constants.CategoryTypes;
import com.rm.mynotes.utils.constants.OrdinationTypes;
import com.rm.mynotes.utils.dto.payloads.ResponseDTO;
import com.rm.mynotes.utils.dto.requests.CollectionDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface CollectionService {
    ResponseEntity<ResponseDTO> getPinnedCollections(Authentication authentication);
    ResponseEntity<ResponseDTO> getCollection(Authentication authentication, Long collectionId);
    ResponseEntity<ResponseDTO> deleteCollection(Authentication authentication, Long collectionId);
    ResponseEntity<ResponseDTO> editCollection(Authentication authentication, Long collectionId, CollectionDTO collectionDTO);
    ResponseEntity<ResponseDTO> createCollection(Authentication authentication, MultipartFile cover, CollectionDTO collectionDTO);
    ResponseEntity<ResponseDTO> getCollections(Authentication authentication, String ordination, List<CategoryTypes> categories, OrdinationTypes orderBy, Integer currentPage);
}
