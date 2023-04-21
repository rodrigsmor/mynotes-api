package com.rm.mynotes.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rm.mynotes.service.mold.AnnotationService;
import com.rm.mynotes.utils.constants.RoutePaths;
import com.rm.mynotes.utils.dto.payloads.ResponseDTO;
import com.rm.mynotes.utils.dto.requests.AnnotationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class AnnotationResource {
    private final AnnotationService annotationService;

    @PostMapping(RoutePaths.CREATE_NOTE)
    public ResponseEntity<ResponseDTO> createAnnotation(Authentication authentication, @RequestParam(required = false) MultipartFile cover, @RequestParam(required = false) MultipartFile thumbnail, @RequestParam(value = "data") String data) {
        ObjectMapper objectMapper = new ObjectMapper();
        AnnotationDTO annotationDTO = null;
        try {
            annotationDTO = objectMapper.readValue(data, AnnotationDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        annotationDTO.setCover(cover);
        annotationDTO.setThumbnail(thumbnail);

        return annotationService.createAnnotation(authentication, annotationDTO);
    }
}
