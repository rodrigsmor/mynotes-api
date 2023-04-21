package com.rm.mynotes.utils.functions;

import com.rm.mynotes.model.Annotation;
import com.rm.mynotes.repository.AnnotationRepository;
import com.rm.mynotes.repository.UserRepository;
import com.rm.mynotes.utils.CustomExceptions;
import com.rm.mynotes.utils.config.FirebaseConfig;
import com.rm.mynotes.utils.constants.FileTypes;
import com.rm.mynotes.utils.dto.requests.AnnotationDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnnotationMethods {
    private UserRepository userRepository;
    private AnnotationRepository annotationRepository;

    private CommonFunctions commonFunctions;

    @Autowired
    private FirebaseConfig firebaseConfig;

    public Annotation createAnnotation(AnnotationDTO annotationDTO) throws IOException, CustomExceptions {
        Annotation annotation = new Annotation(annotationDTO);

        String coverUrl = this.uploadAnnotationImage(annotationDTO.getCover(), FileTypes.NOTE_COVER);
        String thumbnailUrl = this.uploadAnnotationImage(annotationDTO.getThumbnail(), FileTypes.NOTE_THUMBNAIL);

        if (coverUrl == "error" || thumbnailUrl == "error") throw new CustomExceptions("Erro ao salvar imagem!");

        annotation.setCover(thumbnailUrl);
        annotation.setThumbnail(thumbnailUrl);

        return annotationRepository.save(annotation);
    }

    private String uploadAnnotationImage(MultipartFile file, FileTypes type) throws IOException {
        HashMap<String, Object> uploadedImage = firebaseConfig.uploadImage(file, type);
        return (Boolean) uploadedImage.get("success") ? (String) uploadedImage.get("imageUrl") : "error";
    }
}
