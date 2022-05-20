package com.linguatool.model.dto.lang;

import com.linguatool.model.user.User;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Type;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.time.LocalDateTime;
import java.util.List;


@Entity
@Getter
@Setter
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CardEntity {

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

    @ManyToOne
    @JoinColumn(name = "owner_id", referencedColumnName = "id", insertable = false, updatable = false)
    User owner;

    @Type(type = "numeric_boolean")
    boolean irregularSpelling;

    @Type(type = "numeric_boolean")
    boolean activeLearning = true;
    @Type(type = "numeric_boolean")
    boolean forThePronunciation;
    @Type(type = "numeric_boolean")
    boolean forThePlural;

    @Type(type = "numeric_boolean")
    boolean learnt = false;

    @OneToMany(mappedBy = "cardEntity")
    List<Example> examples;


    private String note;
    private String source;

    private int iterations = 0;

    private int priority = 3;

    private String ipa;

    private int frequency;

    @ManyToMany
    @JoinTable(name = "confused",
        joinColumns = {@JoinColumn(name = "fst_word_id", referencedColumnName = "id")},
        inverseJoinColumns = {@JoinColumn(name = "snd_word_id", referencedColumnName = "id")}
    )
    List<CardEntity> confusedWith;

    String wordFamily;

    String hardIndices;

}
