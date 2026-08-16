package io.github.nguyenquephong13062003.course_management_system.models.entities;

import io.github.nguyenquephong13062003.course_management_system.models.constants.CourseStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Represents a course in the course management system.
 * This entity is mapped to the "courses" table in the database.
 */
@Entity
@Table(
        name = "courses",
        indexes = {
                @Index(name = "idx_courses_teacher_id", columnList = "teacher_id"),
                @Index(name = "idx_courses_status", columnList = "status")
        }
)
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    /**
     * The unique identifier for the course.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "course_id")
    private Long id;

    /**
     * The title of the course.
     */
    @Column(
            name = "title",
            nullable = false,
            length = 255
    )
    private String title;

    /**
     * The description of the course.
     */
    @Column(
            name = "description",
            columnDefinition = "TEXT"
    )
    private String description;

    /**
     * The teacher associated with the course.
     * This is a many-to-one relationship, where many courses can be taught by one teacher.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "teacher_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_courses_teacher"
            )
    )
    private User teacher;

    /**
     * The price of the course.
     */
    @Column(
            name = "price",
            nullable = false,
            precision = 12,
            scale = 2
    )
    @Builder.Default
    private BigDecimal price = BigDecimal.ZERO;

    /**
     * The duration of the course in hours.
     */
    @Column(name = "duration_hours")
    private Integer durationHours;

    /**
     * The status of the course, indicating whether it is in draft, published, or archived state.
     */
    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 20
    )
    @Builder.Default
    private CourseStatus status = CourseStatus.DRAFT;

    /**
     * The timestamp when the course was created.
     */
    @CreationTimestamp
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    /**
     * The timestamp when the course was last updated.
     */
    @UpdateTimestamp
    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;

}
