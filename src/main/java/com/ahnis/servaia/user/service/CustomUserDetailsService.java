
package com.ahnis.servaia.user.service;


import com.ahnis.servaia.user.entity.Therapist;
import com.ahnis.servaia.user.entity.User;
import com.ahnis.servaia.user.repository.TherapistRepository;
import com.ahnis.servaia.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
//todo shift to have both therapists and users
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final TherapistRepository therapistRepository;


    @Override
    public UserDetails loadUserByUsername(String identifier) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsernameOrEmail(identifier);
        if (user.isPresent()) {
            return user.get(); // Safe after isPresent() check
        }
        Optional<Therapist> therapist = therapistRepository.findByUsernameOrEmail(identifier);
        if (therapist.isPresent()) {
            return therapist.get(); // Safe after isPresent() check
        }
        throw new UsernameNotFoundException("Not found: " + identifier);
    }


}


