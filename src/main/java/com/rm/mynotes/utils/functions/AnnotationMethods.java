package com.rm.mynotes.utils.functions;

import com.rm.mynotes.model.Annotation;
import com.rm.mynotes.model.CollectionNotes;
import com.rm.mynotes.model.UserEntity;
import com.rm.mynotes.repository.AnnotationRepository;
import com.rm.mynotes.repository.CollectionRepository;
import com.rm.mynotes.repository.UserRepository;
import com.rm.mynotes.utils.constants.CategoryTypes;
import com.rm.mynotes.utils.constants.OrdinationTypes;
import com.rm.mynotes.utils.dto.payloads.AnnotationSummaryDTO;
import com.rm.mynotes.utils.dto.payloads.CollectionSummaryDTO;
import com.rm.mynotes.utils.errors.CustomExceptions;
import com.rm.mynotes.utils.config.FirebaseConfig;
import com.rm.mynotes.utils.constants.FileTypes;
import com.rm.mynotes.utils.dto.requests.AnnotationDTO;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.time.ZoneOffset;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AnnotationMethods {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AnnotationRepository annotationRepository;

    @Autowired
    private CollectionRepository collectionRepository;

    @Autowired
    private CommonFunctions commonFunctions;

    @Autowired
    private FirebaseConfig firebaseConfig;

    @Value("${DEFAULT_NOTES_ICON}")
    private String DEFAULT_NOTES_ICON;

    @Value("${DEFAULT_NOTES_COVER}")
    private String DEFAULT_NOTES_COVER;

    public Annotation createAnnotation(AnnotationDTO annotationDTO) throws IOException, CustomExceptions {
        Annotation annotation = new Annotation(annotationDTO);

        String coverUrl = (annotationDTO.getCover() == null) ? DEFAULT_NOTES_COVER : this.uploadAnnotationImage(annotationDTO.getCover(), FileTypes.NOTE_COVER);
        String iconUrl =  (annotationDTO.getIcon() == null) ? DEFAULT_NOTES_ICON : this.uploadAnnotationImage(annotationDTO.getCover(), FileTypes.NOTE_ICON);

        if (coverUrl.equals("error") || iconUrl.equals("error")) throw new CustomExceptions("Erro ao salvar imagem!");

        annotation.setCover(coverUrl);
        annotation.setIcon(iconUrl);

        return annotationRepository.save(annotation);
    }

    public List<AnnotationSummaryDTO> sortAndFilterAnnotations(UserEntity user, String ordination, List<CategoryTypes> categories, OrdinationTypes orderBy, String endDateString, String startDateString) throws ClassCastException, ParseException {
        List<AnnotationSummaryDTO> userAnnotations = new java.util.ArrayList<>(user.getAnnotations().stream().map(AnnotationSummaryDTO::new).toList());

        if(ordination.equals("DESC")) Collections.reverse(userAnnotations);

        switch (orderBy) {
            case title -> userAnnotations.sort(Comparator.comparing(AnnotationSummaryDTO::getTitle));
            case lastUpdate -> userAnnotations.sort(Comparator.comparing(AnnotationSummaryDTO::getLastUpdate));
            case createdAt -> userAnnotations.sort(Comparator.comparing(AnnotationSummaryDTO::getCreatedAt));
        }

        if(categories != null) userAnnotations = userAnnotations.stream().filter(noteSummary -> categories.stream().anyMatch(category -> category.name().equals(noteSummary.getCategory().name()))).toList();

        if(endDateString != null && !endDateString.isEmpty()) {
            Date endDate = CommonFunctions.convertStringToDate(endDateString);
            userAnnotations = userAnnotations.stream().filter(annotation -> endDate.toInstant().isAfter(annotation.getLastUpdate().toInstant())).toList();
        }

        if(startDateString != null && !startDateString.isEmpty()) {
            Date startDate = CommonFunctions.convertStringToDate(startDateString);
            userAnnotations = userAnnotations.stream().filter(annotation -> startDate.toInstant().isBefore(annotation.getLastUpdate().toInstant())).toList();
        }

        return userAnnotations;
    }

    public List<CollectionSummaryDTO> getCollectionsThatHaveTheAnnotation(Long annotationId) {
        List<CollectionNotes> collections = collectionRepository.getCollectionsByAnnotation(annotationId);;

        return collections.stream().map(CollectionSummaryDTO::new).toList();
    }

    private String uploadAnnotationImage(MultipartFile file, FileTypes type) throws IOException {
        HashMap<String, Object> uploadedImage = firebaseConfig.uploadImage(file, type);
        return (Boolean) uploadedImage.get("success") ? (String) uploadedImage.get("imageUrl") : "error";
    }
}
