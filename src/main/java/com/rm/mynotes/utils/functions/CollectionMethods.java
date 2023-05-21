package com.rm.mynotes.utils.functions;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rm.mynotes.model.CollectionNotes;
import com.rm.mynotes.repository.CollectionRepository;
import com.rm.mynotes.utils.config.FirebaseConfig;
import com.rm.mynotes.utils.constants.FileTypes;
import com.rm.mynotes.utils.dto.requests.CollectionDTO;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;

@Slf4j
@Component
@RequiredArgsConstructor
public class CollectionMethods {
    @Autowired
    private FirebaseConfig firebaseConfig;

    @Autowired
    private CollectionRepository collectionRepository;

    @Value("${DEFAULT_COLLECTION_COVER}")
    private String DEFAULT_COLLECTION_COVER;

    public CollectionNotes createCollections(CollectionDTO collectionDTO, MultipartFile cover) throws IOException {
        CollectionNotes collection = new CollectionNotes(collectionDTO);

        if (cover == null) collection.setCoverUrl(DEFAULT_COLLECTION_COVER);
        else {
            HashMap<String, Object> response = firebaseConfig.uploadImage(cover, FileTypes.COLLECTION_COVER);
            if ((Boolean) response.get("success")) collection.setCoverUrl((String) response.get("imageUrl"));
            else throw new Error("Não foi possível salvar a imagem. Tente novamente mais tarde!");
        }

        return collectionRepository.save(collection);
    }

    public static CollectionDTO convertStringIntoObject(String objectString) {
        ObjectMapper objectMapper = new ObjectMapper();
        CollectionDTO collectionDTO = null;
        try {
            collectionDTO = objectMapper.readValue(objectString, CollectionDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return collectionDTO;
    }

    public Integer getAmountOfAnnotationsInCollection(Long collectionId) {
        log.info("aqui + " + collectionId);
        return collectionRepository.getAmountOfAnnotationsInCollection(collectionId);
    }
}