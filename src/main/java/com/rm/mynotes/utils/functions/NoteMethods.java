package com.rm.mynotes.utils.functions;

import com.rm.mynotes.model.Note;
import com.rm.mynotes.model.CollectionNotes;
import com.rm.mynotes.model.UserEntity;
import com.rm.mynotes.repository.NoteRepository;
import com.rm.mynotes.repository.CollectionRepository;
import com.rm.mynotes.repository.UserRepository;
import com.rm.mynotes.utils.config.FirebaseConfig;
import com.rm.mynotes.utils.constants.CategoryTypes;
import com.rm.mynotes.utils.constants.FileTypes;
import com.rm.mynotes.utils.constants.OrdinationTypes;
import com.rm.mynotes.utils.dto.payloads.NoteSummaryDTO;
import com.rm.mynotes.utils.dto.payloads.CollectionSummaryDTO;
import com.rm.mynotes.utils.dto.requests.NoteDTO;
import com.rm.mynotes.utils.errors.CustomExceptions;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.ParseException;
import java.time.OffsetDateTime;
import java.util.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class NoteMethods {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private NoteRepository noteRepository;

    private CollectionRepository collectionRepository;

    @Autowired
    public NoteMethods(CollectionRepository collectionRepository) {
        this.collectionRepository = collectionRepository;
    }

    @Autowired
    private CommonFunctions commonFunctions;

    @Autowired
    private FirebaseConfig firebaseConfig;

    @Value("${DEFAULT_NOTES_ICON}")
    private String DEFAULT_NOTES_ICON;

    @Value("${DEFAULT_NOTES_COVER}")
    private String DEFAULT_NOTES_COVER;

    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void excludeNotesThatHaveReachedDeadline() {
        OffsetDateTime deadline = OffsetDateTime.now().minusDays(30);
        List<Note> notesToDelete = noteRepository.findByDeletionDateBefore(deadline);

        deletePermanentlyNotes(notesToDelete);
    }

    @Transactional
    public void deletePermanentlyNotes(List<Note> notesToDelete) {
        if (notesToDelete.size() > 0) {
            for(Note note : notesToDelete) {
                log.info(note.getId().toString());
                noteRepository.removeAllRelationsFromNote(note.getId());
            }

            noteRepository.deleteAll(notesToDelete);
        }
    }

    public Note createNote(NoteDTO noteDTO) throws IOException, CustomExceptions {
        Note note = new Note(noteDTO);

        String coverUrl = (noteDTO.getCover() == null) ? DEFAULT_NOTES_COVER : this.uploadNoteImage(noteDTO.getCover(), FileTypes.NOTE_COVER);
        String iconUrl =  (noteDTO.getIcon() == null) ? DEFAULT_NOTES_ICON : this.uploadNoteImage(noteDTO.getCover(), FileTypes.NOTE_ICON);

        if (coverUrl.equals("error") || iconUrl.equals("error")) throw new CustomExceptions("Erro ao salvar imagem!");

        note.setCover(coverUrl);
        note.setIcon(iconUrl);

        return noteRepository.save(note);
    }

    public List<NoteSummaryDTO> sortAndFilterNotes(UserEntity user, String ordination, List<CategoryTypes> categories, OrdinationTypes orderBy, String endDateString, String startDateString) throws ClassCastException, ParseException {
        List<NoteSummaryDTO> userNotes = new java.util.ArrayList<>(user.getNotes().stream().filter(note -> !note.getIsExcluded()).map(NoteSummaryDTO::new).toList());

        if(ordination.equals("DESC")) Collections.reverse(userNotes);

        switch (orderBy) {
            case title -> userNotes.sort(Comparator.comparing(NoteSummaryDTO::getTitle));
            case lastUpdate -> userNotes.sort(Comparator.comparing(NoteSummaryDTO::getLastUpdate));
            case createdAt -> userNotes.sort(Comparator.comparing(NoteSummaryDTO::getCreatedAt));
        }

        if(categories != null) userNotes = userNotes.stream().filter(noteSummary -> categories.stream().anyMatch(category -> category.name().equals(noteSummary.getCategory().name()))).toList();

        if(endDateString != null && !endDateString.isEmpty()) {
            Date endDate = CommonFunctions.convertStringToDate(endDateString);
            userNotes = userNotes.stream().filter(note -> endDate.toInstant().isAfter(note.getLastUpdate().toInstant())).toList();
        }

        if(startDateString != null && !startDateString.isEmpty()) {
            Date startDate = CommonFunctions.convertStringToDate(startDateString);
            userNotes = userNotes.stream().filter(note -> startDate.toInstant().isBefore(note.getLastUpdate().toInstant())).toList();
        }

        return userNotes;
    }

    public List<CollectionSummaryDTO> getCollectionsThatHaveTheNote(Long noteId) {
        List<CollectionNotes> collections = collectionRepository.getCollectionsByNotes(noteId);

        return collections.stream().map(CollectionSummaryDTO::new).toList();
    }

    private String uploadNoteImage(MultipartFile file, FileTypes type) throws IOException {
        HashMap<String, Object> uploadedImage = firebaseConfig.uploadImage(file, type);
        return (Boolean) uploadedImage.get("success") ? (String) uploadedImage.get("imageUrl") : "error";
    }
}
