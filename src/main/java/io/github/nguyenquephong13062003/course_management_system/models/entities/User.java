package io.github.nguyenquephong13062003.course_management_system.models.entities;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import io.github.nguyenquephong13062003.course_management_system.models.constants.UserRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


/**
 * User entity representing a user in the system.
 */
@Entity
@Table(name = "users")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class User {
    
    /**
     * The unique identifier for the user.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long id;

    /**
     * The username of the user.
     */
    @Column(
            nullable = false,
            unique = true,
            length = 50
    )
    private String username;

    /**
     * The hashed password of the user.
     */
    @Column(
            name = "password_hash",
            nullable = false,
            length = 255
    )
    private String passwordHash;

    /**
     * The email address of the user.
     */
    @Column(
            name = "email",
            nullable = false,
            unique = true,
            length = 100
    )
    private String email;

    /**
     * The full name of the user.
     */
    @Column(
            name = "full_name",
            nullable = false,
            length = 100
    )
    private String fullName;

    /**
     * The role of the user.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @Builder.Default
    private UserRole role = UserRole.STUDENT;

    /**
     * Whether the user is active.
     */
    @Column(
            name = "is_active",
            nullable = false
    )
    @Builder.Default
    private Boolean active = true;

    /**
     * Timestamp indicating when the user was created.
     */
    @CreationTimestamp
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    /**
     * Timestamp indicating when the user was last updated.
     */
    @UpdateTimestamp
    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;

    /**
     * The list of reviews made by the user.
     * This is a one-to-many relationship, where one user can have many reviews.
     */
    @OneToMany(
            mappedBy = "student",
            fetch = FetchType.LAZY
    )
    @Builder.Default
    private List<Review> reviews = new ArrayList<>();

}
