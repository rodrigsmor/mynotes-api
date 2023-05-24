package com.rm.mynotes.service.mold;

import com.rm.mynotes.utils.constants.CategoryTypes;
import com.rm.mynotes.utils.constants.OrdinationTypes;
import com.rm.mynotes.utils.dto.payloads.ResponseDTO;
import com.rm.mynotes.utils.dto.requests.AnnotationDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface AnnotationService {
    void excludeNotesThatHaveReachedDeadline();
    ResponseEntity<ResponseDTO> getAnnotation(Authentication authentication, Long noteId);
    ResponseEntity<ResponseDTO> createAnnotation(Authentication authentication, AnnotationDTO annotationDTO);
    ResponseEntity<ResponseDTO> deleteAnnotation(Authentication authentication, Long noteId, Boolean isPermanent);
    ResponseEntity<ResponseDTO> addsAnnotationToCollection(Authentication authentication, Long noteId, Long collectionId);
    ResponseEntity<ResponseDTO> removeAnnotationFromCollection(Authentication authentication, Long noteId, Long collectionId);
    ResponseEntity<ResponseDTO> getAllAnnotations(Authentication authentication, String ordination, List<CategoryTypes> categories, OrdinationTypes orderBy, Integer currentPage, String endDate, String startDate);
}
