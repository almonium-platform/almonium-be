package com.linguatool.model.entity.lang;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linguatool.model.entity.user.Language;
import com.linguatool.model.entity.user.Tag;
import com.linguatool.model.entity.user.User;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.PreRemove;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;


@Entity
@Getter
@Table(name = "card")
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Card {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column
    String entry;

    @Column(columnDefinition = "TIMESTAMP")
    LocalDateTime created;

    @Column(columnDefinition = "TIMESTAMP")
    LocalDateTime modified;

    @Column(columnDefinition = "TIMESTAMP")
    LocalDateTime lastRepeat;

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id")
    User owner;

    @Type(type = "numeric_boolean")
    boolean irregularSpelling;

    @Type(type = "numeric_boolean")
    boolean activeLearning = true;

    @Type(type = "numeric_boolean")
    boolean falseFriend;

    @Type(type = "numeric_boolean")
    boolean irregularPlural;

    @Type(type = "numeric_boolean")
    boolean learnt;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "lang_id", referencedColumnName = "id")
    LanguageEntity language;

    @OneToMany(mappedBy = "card")
    List<Example> examples;

    @JsonIgnore
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "card_tag",
        joinColumns = {@JoinColumn(name = "card_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "tag_id", referencedColumnName = "id")}
    )
    private Set<Tag> tags;


    public void addExample(Example example) {
        if (example != null) {
            this.examples.add(example);
            example.setCard(this);
        }
    }

    public void removeExample(Example example) {
        if (example != null) {
            this.examples.remove(example);
            example.setCard(null);
        }
    }

    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    @OneToMany(mappedBy = "card")
    List<Translation> translations;

    private String notes;

    private String source;

    private int iterations = 0;

    private int priority = 2;

    private String ipa;

    private int frequency;

//    @ManyToMany
//    @JoinTable(name = "confused",
//        joinColumns = {@JoinColumn(name = "fst_word_id", referencedColumnName = "id")},
//        inverseJoinColumns = {@JoinColumn(name = "snd_word_id", referencedColumnName = "id")}
//    )
//    List<CardEntity> confusedWith;

    String wordFamily;

    String hardIndices;

}
