package com.rm.mynotes.service.mold;

import com.rm.mynotes.utils.constants.CategoryTypes;
import com.rm.mynotes.utils.constants.OrdinationTypes;
import com.rm.mynotes.utils.dto.payloads.ResponseDTO;
import com.rm.mynotes.utils.dto.requests.NoteDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;

import java.util.List;

public interface NoteService {
    ResponseEntity<ResponseDTO> emptyTrash(Authentication authentication);
    ResponseEntity<ResponseDTO> getDeletedNotes(Authentication authentication);
    ResponseEntity<ResponseDTO> getNote(Authentication authentication, Long noteId);
    ResponseEntity<ResponseDTO> recoverDeletedNote(Authentication authentication, Long noteId);
    ResponseEntity<ResponseDTO> createNote(Authentication authentication, NoteDTO noteDTO);
    ResponseEntity<ResponseDTO> deleteNote(Authentication authentication, Long noteId, Boolean isPermanent);
    ResponseEntity<ResponseDTO> addsNoteToCollection(Authentication authentication, Long noteId, Long collectionId, Boolean isFavorite);
    ResponseEntity<ResponseDTO> removeNoteFromCollection(Authentication authentication, Long noteId, Long collectionId, Boolean isFavorite);
    ResponseEntity<ResponseDTO> getAllNotes(Authentication authentication, String ordination, List<CategoryTypes> categories, OrdinationTypes orderBy, Integer currentPage, String endDate, String startDate);
}
