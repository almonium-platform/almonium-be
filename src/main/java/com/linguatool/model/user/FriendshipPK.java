package com.linguatool.model.user;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class FriendshipPK implements Serializable {

    private Long requesterId;

    private Long requesteeId;

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        FriendshipPK that = (FriendshipPK) o;
        return requesterId.equals(that.requesterId) && requesteeId.equals(that.requesteeId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(requesterId, requesteeId);
    }
}
