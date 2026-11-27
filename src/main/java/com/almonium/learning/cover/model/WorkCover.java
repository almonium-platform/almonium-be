package com.almonium.learning.cover.model;

import static lombok.AccessLevel.PRIVATE;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * The cover art of one work, shared by all its editions: a period illustration from Wikimedia Commons, kept as our own
 * copy with the record of where it came from and why it was free to take. A work that was looked at and got nothing
 * has a row too, so it is not paid for twice.
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode(of = {"workSlug"})
@Table(name = "work_cover")
public class WorkCover {
    @Id
    @Column(length = 180)
    String workSlug;

    @Enumerated(EnumType.STRING)
    CoverStatus status;

    /** Our copy of the file, at the width the shelf needs; null unless approved. */
    byte[] image;

    String contentType;

    /** Where the subject sits in the image, in percent from the left and the top, for the square crop. */
    @Column(name = "focus_x")
    int focusX;

    @Column(name = "focus_y")
    int focusY;

    /** The file's page on Commons, which carries its licence and author. */
    String sourcePageUrl;

    String artist;

    String licence;

    /** The Commons category the licence rule accepted the file on, such as {@code PD-old-100-expired}. */
    String licenceBasis;

    /** The model's one line on why it chose this file, or why it chose none. */
    String reason;

    String model;

    Instant checkedAt;
}
