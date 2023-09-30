package com.linguarium.friendship.model;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class Friend {
    private Long id;
    private String username;
    private String email;
}
