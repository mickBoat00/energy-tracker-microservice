package com.meichel.user_service.seed;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import com.meichel.user_service.entity.User;
import com.meichel.user_service.repository.UserRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class UserSeeder implements CommandLineRunner {

    // private final UserRepository userRepository;

    // public UserSeeder(UserRepository userRepository){
    //     this.userRepository = userRepository;
    // }

    @Override
    public void run(String... args) {
        // if (userRepository.count() > 0) {
        //     log.info("Users already seeded, skipping.");
        //     return;
        // }

        // userRepository.save(User.builder()
        //         .sub("seed-kwame")
        //         .name("Kwame Mensah")
        //         .address("12 Ring Road, Accra")
        //         .email("kwame@example.com")
        //         .enableAlerting(true)
        //         .alertingThreshold(5)
        //         .build());

        // userRepository.save(User.builder()
        //         .sub("seed-ama")
        //         .name("Ama Owusu")
        //         .address("5 Oxford St, Osu")
        //         .email("ama@example.com")
        //         .enableAlerting(false)
        //         .alertingThreshold(0)
        //         .build());

        // userRepository.save(User.builder()
        //         .sub("seed-kofi")
        //         .name("Kofi Boateng")
        //         .address("22 Spintex Road, Accra")
        //         .email("kofi@example.com")
        //         .enableAlerting(true)
        //         .alertingThreshold(10)
        //         .build());

        // userRepository.save(User.builder()
        //         .sub("seed-abena")
        //         .name("Abena Asante")
        //         .address("9 Cantonments Rd, Accra")
        //         .email("abena@example.com")
        //         .enableAlerting(true)
        //         .alertingThreshold(3)
        //         .build());

        log.info("Might remove seeding users.");
    }
}
