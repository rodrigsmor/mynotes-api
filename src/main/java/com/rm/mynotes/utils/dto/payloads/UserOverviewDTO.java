package com.rm.mynotes.utils.dto.payloads;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOverviewDTO {
    private int lastMonth;
    private int totalNotes;
    private int notesDeleted;
}
