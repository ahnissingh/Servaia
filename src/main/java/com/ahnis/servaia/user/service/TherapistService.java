package com.ahnis.servaia.user.service;


import com.ahnis.servaia.user.dto.request.TherapistUpdateRequest;
import com.ahnis.servaia.user.dto.response.TherapistClientResponse;
import com.ahnis.servaia.user.dto.response.TherapistPersonalResponse;
import com.ahnis.servaia.user.dto.response.TherapistResponse;
import com.ahnis.servaia.user.entity.Therapist;
import com.ahnis.servaia.user.entity.User;
import com.ahnis.servaia.user.exception.UserNotFoundException;
import com.ahnis.servaia.user.repository.TherapistRepository;
import com.ahnis.servaia.user.repository.UserRepository;
import com.github.dockerjava.api.exception.ConflictException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class TherapistService {
    private final TherapistRepository therapistRepository;
    private final MongoTemplate mongoTemplate;
    private final UserRepository userRepository;


    public List<TherapistResponse> search(
            String specialty,
            String firstName,
            String lastName
    ) {
        var criteria = new Criteria();
        if (specialty != null)
            criteria.and("specialties").regex(specialty, "i");
        //todo remove and replace with first name and last name
        if (firstName != null)
            criteria.and("firstName").regex(firstName, "i");
        if (lastName != null)
            criteria.and("firstName").regex(lastName, "i");
        var query = Query.query(criteria);
        List<Therapist> therapists = mongoTemplate.find(query, Therapist.class);
        return therapists.stream()
                .map(this::mapToResponse)
                .toList();
    }


    public Page<TherapistResponse> getAllTherapists(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        return therapistRepository.findAll(pageable)
                .map(this::mapToResponse);

    }

    @Transactional
    public void subscribe(User user, String therapistId) {
        var therapist = therapistRepository.findById(therapistId)
                .orElseThrow(() -> new UserNotFoundException("Therapist not found" + therapistId));


//        if (user.getTherapistId() != null) {
//            throw new ConflictException("Therapist is already subscribed");
//        }

        // Updating both sides of relationship ie one to many
        log.info("Subscribing from service {}", therapistId);
        user.setTherapistId(therapistId);
        user.setSubscribedAt(Instant.now());
        therapist.getClientUserId().add(user.getId());

        //Todo refactor to atomic update
        userRepository.save(user);
        therapistRepository.save(therapist);
        log.info("exiting subscribe user state : {}", user);

        //notificationService.sendSubscriptionNotification(therapist, user);
    }


    private TherapistResponse mapToResponse(Therapist therapist) {
        return new TherapistResponse(
                therapist.getId(),
                therapist.getUsername(),
                therapist.getFirstName(),
                therapist.getLastName(),
                therapist.getSpecialties(),
                therapist.getLanguages(),
                therapist.getYearsOfExperience(),
                therapist.getBio(),
                therapist.getProfilePictureUrl()
        );
    }

    public TherapistPersonalResponse getProfile(String id) {
        return therapistRepository.findById(id)
                .map(TherapistPersonalResponse::fromEntity)
                .orElseThrow(() -> new UserNotFoundException("Therapist not found" + id));
    }

    @Transactional
    public void updateProfile(String therapistId, TherapistUpdateRequest request) {
        Therapist therapist = therapistRepository.findById(therapistId)
                .orElseThrow(() -> new UserNotFoundException("Therapist not found" + therapistId));

        therapist.setBio(request.bio());
        therapist.setSpecialties(request.specialties());
        therapist.setLanguages(request.spokenLanguages());
        therapist.setYearsOfExperience(request.yearsOfExperience());
        therapist.setProfilePictureUrl(request.profilePictureUrl());

        therapistRepository.save(therapist);
    }

    public List<TherapistClientResponse> getClients(String therapistId) {
        Therapist therapist = therapistRepository.findById(therapistId)
                .orElseThrow(() -> new UserNotFoundException("Therapist not found" + therapistId));

        return userRepository.findAllById(therapist.getClientUserId()).stream()
                .map(TherapistClientResponse::fromUser)
                .toList();
    }
}
