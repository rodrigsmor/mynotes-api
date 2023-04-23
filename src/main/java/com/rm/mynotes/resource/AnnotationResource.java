package com.rm.mynotes.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rm.mynotes.service.mold.AnnotationService;
import com.rm.mynotes.utils.constants.CategoryTypes;
import com.rm.mynotes.utils.constants.OrdinationTypes;
import com.rm.mynotes.utils.constants.RoutePaths;
import com.rm.mynotes.utils.dto.payloads.ResponseDTO;
import com.rm.mynotes.utils.dto.requests.AnnotationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class AnnotationResource {
    private final AnnotationService annotationService;

    @PatchMapping(RoutePaths.ADD_NOTE_TO_COLLECTION)
    public ResponseEntity<ResponseDTO> addsNotesToCollection(Authentication authentication, @PathVariable Long noteId, @PathVariable Long collectionId) {
        log.info("chegou");
        return annotationService.addsAnnotationToCollection(authentication, noteId, collectionId);
    }

    @GetMapping(RoutePaths.GET_ALL_NOTES)
    public ResponseEntity<ResponseDTO> getAllNotes(Authentication authentication,
                                                   @RequestParam(required = false, name = "current_page", defaultValue = "0") Integer currentPage,
                                                   @RequestParam(required = false, name = "ordination", defaultValue = "ASC") String ordination,
                                                   @RequestParam(required = false, name = "categories") List<CategoryTypes> categories,
                                                   @RequestParam(required = false, name = "orderBy", defaultValue = "createdAt") OrdinationTypes orderBy,
                                                   @RequestParam(required = false) String endDate, @RequestParam(required = false) String startDate
                                                   ) {
        return annotationService.getAllAnnotations(authentication, ordination, categories, orderBy, currentPage, endDate, startDate);
    }

    @GetMapping(RoutePaths.GET_NOTE)
    public ResponseEntity<ResponseDTO> getAnnotation(Authentication authentication, @PathVariable Long id) {
        return annotationService.getAnnotation(authentication, id);
    }

    @PostMapping(RoutePaths.CREATE_NOTE)
    public ResponseEntity<ResponseDTO> createAnnotation(Authentication authentication, @RequestParam(required = false) MultipartFile cover, @RequestParam(required = false) MultipartFile icon, @RequestParam(value = "data") String data) {
        AnnotationDTO annotationDTO = convertStringIntoObject(data, cover, icon);
        return annotationService.createAnnotation(authentication, annotationDTO);
    }

    private AnnotationDTO convertStringIntoObject(String objectString, MultipartFile cover, MultipartFile icon) {
        ObjectMapper objectMapper = new ObjectMapper();
        AnnotationDTO annotationDTO = null;

        try {
            annotationDTO = objectMapper.readValue(objectString, AnnotationDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        annotationDTO.setCover(cover);
        annotationDTO.setIcon(icon);

        return annotationDTO;
    }
}
