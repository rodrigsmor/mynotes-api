package com.rm.mynotes.resource;

import com.rm.mynotes.service.mold.CollectionService;
import com.rm.mynotes.utils.constants.CategoryTypes;
import com.rm.mynotes.utils.constants.OrdinationTypes;
import com.rm.mynotes.utils.constants.RoutePaths;
import com.rm.mynotes.utils.dto.payloads.ResponseDTO;
import com.rm.mynotes.utils.dto.requests.CollectionDTO;
import com.rm.mynotes.utils.functions.CollectionMethods;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class CollectionResource {
    private final CollectionService collectionService;

    @GetMapping(RoutePaths.COLLECTION)
    public ResponseEntity<ResponseDTO> getCollection(Authentication authentication, @PathVariable("collectionId") Long collectionId) {
        return collectionService.getCollection(authentication, collectionId);
    }

    @GetMapping(RoutePaths.GET_PINNED_COLLECTIONS)
    public ResponseEntity<ResponseDTO> getPinnedCollection(Authentication authentication) {
        return collectionService.getPinnedCollections(authentication);
    }

    @GetMapping(RoutePaths.GET_ALL_COLLECTIONS)
    public ResponseEntity<ResponseDTO> getAllCollections(Authentication authentication,
                                                      @RequestParam(required = false, name = "current_page", defaultValue = "0") Integer currentPage,
                                                      @RequestParam(required = false, name = "ordination", defaultValue = "ASC") String ordination,
                                                      @RequestParam(required = false, name = "categories") List<CategoryTypes> categories,
                                                      @RequestParam(required = false, name = "orderBy", defaultValue = "createdAt") OrdinationTypes orderBy
                                                      ) {
        return collectionService.getCollections(authentication, ordination, categories, orderBy, currentPage);
    }

    @PostMapping(RoutePaths.CREATE_NEW_COLLECTION)
    public ResponseEntity<ResponseDTO> createCollection(Authentication authentication, @RequestParam(required = false) MultipartFile cover, @RequestParam(name = "data") String data) {
        CollectionDTO collectionDTO = CollectionMethods.convertStringIntoObject(data);
        return collectionService.createCollection(authentication, cover, collectionDTO);
    }

    @DeleteMapping(RoutePaths.COLLECTION)
    public ResponseEntity<ResponseDTO> deleteCollection(Authentication authentication, @PathVariable("collectionId") Long collectionId) {
        return collectionService.deleteCollection(authentication, collectionId);
    }

    @PutMapping(RoutePaths.COLLECTION)
    public ResponseEntity<ResponseDTO> editCollection(Authentication authentication, @PathVariable("collectionId") Long collectionId, @RequestBody CollectionDTO collectionDTO) {
        return collectionService.editCollection(authentication, collectionId, collectionDTO);
    }
}
