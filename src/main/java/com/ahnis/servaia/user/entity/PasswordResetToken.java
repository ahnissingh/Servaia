package com.ahnis.servaia.user.entity;


import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;

import java.time.Instant;

@Getter
@Setter
@ToString
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "password_reset_tokens")
public class PasswordResetToken {

    @Id
    private String id;
    private String token;
    @Indexed
    @Field(targetType = FieldType.OBJECT_ID)
    private String userId;
    private Instant expiryDate;
}
