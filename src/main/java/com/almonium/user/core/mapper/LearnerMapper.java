package com.almonium.user.core.mapper;

import com.almonium.user.core.dto.response.LearnerDto;
import com.almonium.user.core.model.entity.Learner;
import java.util.List;
import java.util.Set;
import org.mapstruct.Mapper;

@Mapper
public interface LearnerMapper {
    LearnerDto toDto(Learner learner);

    List<LearnerDto> toDto(Set<Learner> learners);
}
