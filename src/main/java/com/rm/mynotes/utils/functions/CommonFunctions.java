package com.rm.mynotes.utils.functions;

import com.rm.mynotes.model.UserEntity;
import com.rm.mynotes.repository.UserRepository;
import com.rm.mynotes.utils.constants.FileTypes;
import com.rm.mynotes.utils.dto.payloads.ResponseDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CommonFunctions {
    private final UserRepository userRepository;

    public static Date convertStringToDate(String dateString) throws ParseException {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        return dateFormat.parse(dateString);
    }

    public static OffsetDateTime getCurrentDatetime() {
        return OffsetDateTime.now().atZoneSameInstant(ZoneId.of("Z")).toOffsetDateTime();
    }

    public UserEntity getCurrentUser(Authentication authentication) {
        return userRepository.getReferenceByEmail(authentication.getName());
    }

    public static ResponseEntity<ResponseDTO> errorHandling(Exception exception) {
        ResponseDTO responseDTO = new ResponseDTO();

        responseDTO.setSuccess(false);
        responseDTO.setMessage(exception.getMessage());
        responseDTO.setData(exception.getLocalizedMessage());

        return ResponseEntity.badRequest().body(responseDTO);
    }

    public static String formatFilename(MultipartFile file, FileTypes type) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
        String random = UUID.randomUUID().toString().replaceAll("_", "");

        int separator = Objects.requireNonNull(file.getOriginalFilename()).lastIndexOf(".");

        String filename = file.getOriginalFilename().substring(0, separator);
        String filetype = file.getOriginalFilename().substring(separator++);

        return String.format("%s_%s_%s_%s.%s", filename, type.toString(), random, dateFormat.format(new Date()), filetype);
    }
}
