package io.github.nguyenquephong13062003.course_management_system.models.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * Represents a review for a course made by a student.
 */
@Entity
@Table(
        name = "reviews",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_reviews_course_student",
                        columnNames = {"course_id", "student_id"}
                )
        },
        indexes = {
                @Index(
                        name = "idx_reviews_course_id",
                        columnList = "course_id"
                ),
                @Index(
                        name = "idx_reviews_student_id",
                        columnList = "student_id"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {

    /**
     * The unique identifier for the review.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;

    /**
     * The course associated with the review.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "course_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_reviews_course")
    )
    private Course course;

    /**
     * The student who made the review.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "student_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_reviews_student")
    )
    private User student;

    /**
     * The rating given by the student for the course.
     */
    @Column(
            name = "rating",
            nullable = false
    )
    private Integer rating;

    /**
     * The comment provided by the student for the course.
     */
    @Column(
            name = "comment",
            columnDefinition = "TEXT"
    )
    private String comment;

    /**
     * The timestamp when the review was created.
     */
    @CreationTimestamp
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    /**
     * The timestamp when the review was last updated.
     */
    @UpdateTimestamp
    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;

}
