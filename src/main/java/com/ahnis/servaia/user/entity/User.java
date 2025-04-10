package com.ahnis.servaia.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.FieldType;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import com.ahnis.servaia.user.enums.Role;

import java.util.stream.Collectors;


@Getter
@Setter
@ToString
@EqualsAndHashCode
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User implements UserDetails {
    @Id
    private String id;
    //Both of these can be used to log in will implement logic in impl of user details service :)
    @Indexed(unique = true)
    private String username;
    @Indexed(unique = true)
    private String email;

    private String firstName;
    private String lastName;

    @JsonIgnore
    @ToString.Exclude //prevent password from logging stuff and json serialization/deserialization
    private String password;

    private String timezone;

    @Builder.Default
    private Set<Role> roles = new HashSet<>();


    @Builder.Default
    private boolean enabled = true; // Default to true

    @Builder.Default
    private boolean accountNonLocked = true; // Default to true

    @Builder.Default
    private boolean accountNonExpired = true; // Default to true

    @Builder.Default
    private boolean credentialsNonExpired = true; // Default to true

    @CreatedDate
    private Instant createdAt;

    @LastModifiedDate
    private Instant updatedAt;

    private Preferences preferences;
    @Indexed
    private Instant nextReportOn;
    @Indexed
    private Instant lastReportAt;

    //New properties as per discussion
    private int currentStreak; // Current consecutive days of journal writing
    private int longestStreak; // Longest streak achieved
    private Instant lastJournalEntryDate; // Date of the last journal entry
    @Indexed
    @Field(targetType = FieldType.OBJECT_ID)
    private String therapistId;
    private Instant subscribedAt; // Track when subscription occurred
    //added these will only be viewed by therapists
    //For spring security
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.name()))
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

}
