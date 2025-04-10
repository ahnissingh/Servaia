package com.ahnis.servaia.user.entity;


import com.ahnis.servaia.user.enums.Language;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Instant;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "therapists")
//For faster searching
//never add >1 array index
@CompoundIndex(name = "search_idx", def = """
            {
                specialties: 1,
                firstname: 1,
                lastname: 1,
            }
        """)
public class Therapist implements UserDetails {
    @Id
    private String id;
    @Indexed(unique = true)
    private String username;
    @Indexed(unique = true)
    private String email;

    @JsonIgnore
    private String password;

    @Indexed(unique = true)
    private String licenseNumber;

    private String firstName;
    private String lastName;
    private int yearsOfExperience;
    private String bio;
    private Set<Language> languages = new HashSet<>();
    private String profilePictureUrl;

    private Set<String> specialties = new HashSet<>();

    private Set<String> clientUserId = new HashSet<>();
    @CreatedDate
    private Instant createdAt;
    @LastModifiedDate
    private Instant updatedAt;


    //Spring security things
    @Builder.Default
    private boolean accountNonExpired = true;
    @Builder.Default
    private boolean accountNonLocked = true;
    @Builder.Default
    private boolean credentialsNonExpired = true;
    @Builder.Default
    private boolean enabled = true;


    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_THERAPIST"));
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
