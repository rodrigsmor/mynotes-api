package com.rm.mynotes.model;

import com.rm.mynotes.utils.constants.StatusTypes;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;
import java.time.OffsetDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Reminder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String title;

    @NotNull
    private OffsetDateTime createdAt;

    @NotNull
    private OffsetDateTime lastUpdate;

    @NotNull
    private OffsetDateTime reminderDate;

    @Enumerated(EnumType.STRING)
    private StatusTypes status;
}
