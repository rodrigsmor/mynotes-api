package com.rm.mynotes.service.impl;

import com.rm.mynotes.model.Annotation;
import com.rm.mynotes.model.UserEntity;
import com.rm.mynotes.repository.AnnotationRepository;
import com.rm.mynotes.repository.UserRepository;
import com.rm.mynotes.service.mold.AnnotationService;
import com.rm.mynotes.utils.dto.payloads.AnnotationSummaryDTO;
import com.rm.mynotes.utils.errors.CustomExceptions;
import com.rm.mynotes.utils.constants.RoutePaths;
import com.rm.mynotes.utils.dto.payloads.ResponseDTO;
import com.rm.mynotes.utils.dto.requests.AnnotationDTO;
import com.rm.mynotes.utils.functions.AnnotationMethods;
import com.rm.mynotes.utils.functions.CommonFunctions;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
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
    public AnnotationRepository annotationRepository;

    @Autowired
    private final UserRepository userRepository;

    @Override
    public ResponseEntity<ResponseDTO> getAllAnnotations(Authentication authentication) {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            UserEntity user = commonFunctions.getCurrentUser(authentication);
            AnnotationSummaryDTO annotations = (AnnotationSummaryDTO) user.getAnnotations().stream().map(AnnotationSummaryDTO::new);

            responseDTO.setSuccess(true);
            responseDTO.setData(annotations);

            return ResponseEntity.ok().body(responseDTO);
        } catch (Exception exception) {
            return commonFunctions.errorHandling(exception);
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
            return commonFunctions.errorHandling(exception);
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
            return commonFunctions.errorHandling(exception);
        }
    }
}
