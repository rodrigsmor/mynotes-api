package com.rm.mynotes.utils.dto.requests;

import com.rm.mynotes.utils.constants.Category;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AnnotationDTO {
    private String title = "";
    private String description = "";
    private Category category = Category.OTHER;
    private MultipartFile cover;
    private MultipartFile thumbnail;

    public AnnotationDTO(String title, String description, Category category) {
        this.title = title;
        this.description = description;
        this.category = category;
    }
}
