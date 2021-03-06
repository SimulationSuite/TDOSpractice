package org.tdos.tdospractice.entity;

import lombok.*;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class UserEntity {

    private String id;

    private String name;

    private String password;

    private Integer roleID;

    private String classID;

    private Integer gender;

    private String identificationNumber;

    private String phoneNumber;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}
