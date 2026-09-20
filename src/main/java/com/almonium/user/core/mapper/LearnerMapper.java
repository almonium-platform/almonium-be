package com.almonium.user.core.mapper;

import com.almonium.analyzer.translator.model.enums.LanguageVariety;
import com.almonium.user.core.dto.response.LearnerDto;
import com.almonium.user.core.model.entity.Learner;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper
public interface LearnerMapper {
    LearnerDto toDto(Learner learner);

    List<LearnerDto> toDto(Set<Learner> learners);

    /** The entity resolves the default itself, so the client always sees a variety where one can be chosen. */
    default LanguageVariety unwrap(Optional<LanguageVariety> variety) {
        return variety.orElse(null);
    }
}
