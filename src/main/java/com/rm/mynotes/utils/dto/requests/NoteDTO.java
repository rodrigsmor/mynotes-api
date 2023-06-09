package com.rm.mynotes.utils.dto.requests;

import com.rm.mynotes.utils.constants.CategoryTypes;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class NoteDTO {
    private String title = "";
    private String description = "";
    private CategoryTypes category = CategoryTypes.OTHER;
    private MultipartFile cover;
    private MultipartFile icon;

    public NoteDTO(String title, String description, CategoryTypes category) {
        this.title = title;
        this.description = description;
        this.category = category;
    }
}
