package com.rm.mynotes.service.impl;

import com.rm.mynotes.model.Annotation;
import com.rm.mynotes.model.UserEntity;
import com.rm.mynotes.repository.AnnotationRepository;
import com.rm.mynotes.repository.UserRepository;
import com.rm.mynotes.service.mold.AnnotationService;
import com.rm.mynotes.utils.CustomExceptions;
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
import java.util.HashMap;
import java.util.List;

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
    public ResponseEntity<ResponseDTO> createAnnotation(Authentication authentication, AnnotationDTO annotationDTO) {
        ResponseDTO responseDTO = new ResponseDTO();

        try {
            UserEntity user = commonFunctions.getCurrentUser(authentication);
            List<Annotation> annotations = user.getAnnotations();

            Annotation annotationCreated = annotationMethods.createAnnotation(annotationDTO);
            log.info("passou: " + annotationCreated.getId());

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
            responseDTO.setSuccess(false);
            responseDTO.setMessage(exception.getMessage());

            return ResponseEntity.badRequest().body(responseDTO);
        }
    }
}
