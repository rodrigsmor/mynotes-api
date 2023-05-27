package com.rm.mynotes.resource;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rm.mynotes.service.mold.NoteService;
import com.rm.mynotes.utils.constants.CategoryTypes;
import com.rm.mynotes.utils.constants.OrdinationTypes;
import com.rm.mynotes.utils.constants.RoutePaths;
import com.rm.mynotes.utils.dto.payloads.ResponseDTO;
import com.rm.mynotes.utils.dto.requests.NoteDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping
public class NoteResource {
    private final NoteService noteService;

    @GetMapping(RoutePaths.GET_DELETED_NOTES)
    public ResponseEntity<ResponseDTO> getDeletedNotes(Authentication authentication) {
        return noteService.getDeletedNotes(authentication);
    }

    @PatchMapping(RoutePaths.RECOVER_NOTE)
    public ResponseEntity<ResponseDTO> recoverDeletedNote(Authentication authentication, @PathVariable("noteId") Long noteId) {
        return noteService.recoverDeletedNote(authentication, noteId);
    }

    @PatchMapping(RoutePaths.NOTE_TO_COLLECTION)
    public ResponseEntity<ResponseDTO> addsNoteToCollection(Authentication authentication, @PathVariable("noteId") Long noteId, @PathVariable("collectionId") Long collectionId) {
        return noteService.addsNoteToCollection(authentication, noteId, collectionId);
    }

    @DeleteMapping(RoutePaths.NOTE_TO_COLLECTION)
    public  ResponseEntity<ResponseDTO> removeNoteFromCollection(Authentication authentication, @PathVariable("noteId") Long noteId, @PathVariable("collectionId") Long collectionId) {
        return noteService.removeNoteFromCollection(authentication, noteId, collectionId);
    }

    @GetMapping(RoutePaths.GET_ALL_NOTES)
    public ResponseEntity<ResponseDTO> getAllNotes(Authentication authentication,
                                                   @RequestParam(required = false, name = "current_page", defaultValue = "0") Integer currentPage,
                                                   @RequestParam(required = false, name = "ordination", defaultValue = "ASC") String ordination,
                                                   @RequestParam(required = false, name = "categories") List<CategoryTypes> categories,
                                                   @RequestParam(required = false, name = "orderBy", defaultValue = "createdAt") OrdinationTypes orderBy,
                                                   @RequestParam(required = false) String endDate, @RequestParam(required = false) String startDate
                                                   ) {
        return noteService.getAllNotes(authentication, ordination, categories, orderBy, currentPage, endDate, startDate);
    }

    @GetMapping(RoutePaths.GET_NOTE)
    public ResponseEntity<ResponseDTO> getNote(Authentication authentication, @PathVariable Long id) {
        return noteService.getNote(authentication, id);
    }

    @PostMapping(RoutePaths.CREATE_NOTE)
    public ResponseEntity<ResponseDTO> createNote(Authentication authentication, @RequestParam(required = false) MultipartFile cover, @RequestParam(required = false) MultipartFile icon, @RequestParam(value = "data") String data) {
        NoteDTO noteDTO = convertStringIntoObject(data, cover, icon);
        return noteService.createNote(authentication, noteDTO);
    }

    @DeleteMapping(RoutePaths.DELETE_NOTE)
    public ResponseEntity<ResponseDTO> deleteNote(Authentication authentication, @PathVariable Long noteId, @RequestParam(required = false, name = "isPermanent", defaultValue = "false") Boolean isPermanent) {
        return noteService.deleteNote(authentication, noteId, isPermanent);
    }

    @DeleteMapping(RoutePaths.EMPTY_TRASH)
    public ResponseEntity<ResponseDTO> emptyTrash(Authentication authentication) {
        return noteService.emptyTrash(authentication);
    }

    private NoteDTO convertStringIntoObject(String objectString, MultipartFile cover, MultipartFile icon) {
        ObjectMapper objectMapper = new ObjectMapper();
        NoteDTO noteDTO = null;

        try {
            noteDTO = objectMapper.readValue(objectString, NoteDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        noteDTO.setCover(cover);
        noteDTO.setIcon(icon);

        return noteDTO;
    }
}
