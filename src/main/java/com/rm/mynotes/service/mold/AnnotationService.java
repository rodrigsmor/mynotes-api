package com.rm.mynotes.service.mold;

import com.rm.mynotes.utils.dto.payloads.ResponseDTO;
import com.rm.mynotes.utils.dto.requests.AnnotationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

public interface AnnotationService {
    ResponseEntity<ResponseDTO> createAnnotation(Authentication authentication, AnnotationDTO annotationDTO);
}
