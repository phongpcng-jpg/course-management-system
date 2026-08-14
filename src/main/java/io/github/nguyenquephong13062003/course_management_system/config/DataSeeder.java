package io.github.nguyenquephong13062003.course_management_system.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import io.github.nguyenquephong13062003.course_management_system.models.constants.UserRole;
import io.github.nguyenquephong13062003.course_management_system.models.entities.User;
import io.github.nguyenquephong13062003.course_management_system.models.repositories.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Bean
    CommandLineRunner seedData() {
        return args -> {
            seedAdmin();
            seedTeacher();
            seedStudent();
        };
    }

    private void seedAdmin() {
        if (!userRepository.findByRole(UserRole.ADMIN).isEmpty()) {
            log.debug("Admin account already exists, skipping seed");
            return;
        }

        User admin = User.builder()
                .username("admin")
                .passwordHash(passwordEncoder.encode("Admin@123"))
                .email("admin@example.com")
                .fullName("System Administrator")
                .role(UserRole.ADMIN)
                .build();

        userRepository.save(admin);

        log.info("Seeded default ADMIN account: {}", admin.getUsername());
    }

    private void seedTeacher() {
        if (!userRepository.findByRole(UserRole.TEACHER).isEmpty()) {
            log.debug("Teacher account already exists, skipping seed");
            return;
        }

        User teacher = User.builder()
                .username("teacher")
                .passwordHash(passwordEncoder.encode("Teacher@123"))
                .email("teacher@example.com")
                .fullName("Default Teacher")
                .role(UserRole.TEACHER)
                .build();

        userRepository.save(teacher);

        log.info("Seeded default TEACHER account: {}", teacher.getUsername());
    }

    private void seedStudent() {
        if (!userRepository.findByRole(UserRole.STUDENT).isEmpty()) {
            log.debug("Student account already exists, skipping seed");
            return;
        }

        User student = User.builder()
                .username("student")
                .passwordHash(passwordEncoder.encode("Student@123"))
                .email("student@example.com")
                .fullName("Default Student")
                .role(UserRole.STUDENT)
                .build();

        userRepository.save(student);

        log.info("Seeded default STUDENT account: {}", student.getUsername());
    }
}
