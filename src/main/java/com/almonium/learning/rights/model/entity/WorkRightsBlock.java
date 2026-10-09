package com.almonium.learning.rights.model.entity;

import static lombok.AccessLevel.PRIVATE;

import com.almonium.learning.rights.model.enums.BlockSource;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.Table;
import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/** One country a shelf work is hidden in; {@code *} hides it everywhere. Derived, never edited by hand. */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = PRIVATE)
@EqualsAndHashCode(of = {"workSlug", "country"})
@IdClass(WorkRightsBlock.Key.class)
@Table(name = "work_rights_block")
public class WorkRightsBlock {
    public static final String EVERYWHERE = "*";

    @Id
    String workSlug;

    @Id
    String country;

    @Enumerated(EnumType.STRING)
    BlockSource source;

    public record Key(String workSlug, String country) implements Serializable {}
}
