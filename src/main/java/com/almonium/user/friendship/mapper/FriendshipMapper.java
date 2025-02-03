package com.almonium.user.friendship.mapper;

import com.almonium.user.friendship.dto.response.FriendshipDto;
import com.almonium.user.friendship.model.entity.Friendship;
import java.util.List;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface FriendshipMapper {
    @Mapping(target = "requesterId", source = "requester.id")
    @Mapping(target = "requesteeId", source = "requestee.id")
    FriendshipDto toDto(Friendship friendship);

    List<FriendshipDto> toDto(List<Friendship> friendships);
}
