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

/**
 * DataSeeder is responsible for seeding initial data into the database.
 * It creates default users with different roles (ADMIN, TEACHER, STUDENT) and an inactive user.
 */
@Configuration
@RequiredArgsConstructor
@Slf4j
public class DataSeeder {

    /**
     * The user repository for accessing user data.
     */
    private final IUserRepository userRepository;

    /**
     * The password encoder for encoding user passwords.
     */
    private final PasswordEncoder passwordEncoder;

    /**
     * Seeds initial data into the database when the application starts.
     *
     * @return a CommandLineRunner that seeds data
     */
    @Bean
    CommandLineRunner seedData() {
        return args -> {
            seedAdmin();
            seedTeacher();
            seedStudent();
            seedInactiveUser();
        };
    }

    /**
     * Seeds a default ADMIN user if one does not already exist.
     */
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

    /**
     * Seeds a default TEACHER user if one does not already exist.
     */
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

    /**
     * Seeds a default STUDENT user if one does not already exist.
     */
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

    /**
     * Seeds a default inactive user if one does not already exist.
     */
    private void seedInactiveUser() {
        if (!userRepository.findByActive(false).isEmpty()) {
            log.debug("Inactive user account already exists, skipping seed");
            return;
        }

        User inactiveUser = User.builder()
                .username("inactiveUser")
                .passwordHash(passwordEncoder.encode("Inactive@123"))
                .email("inactiveuser@example.com")
                .fullName("Default Inactive User")
                .role(UserRole.STUDENT)
                .active(false)
                .build();

        userRepository.save(inactiveUser);

        log.info("Seeded default INACTIVE account: {}", inactiveUser.getUsername());
    }
}