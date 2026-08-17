package io.github.nguyenquephong13062003.course_management_system.models.entities;

import io.github.nguyenquephong13062003.course_management_system.models.constants.EnrollmentStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Represents an enrollment of a student in a course.
 * This entity captures the relationship between a student and a course, along with the enrollment status, progress, and related lesson progresses.
 */
@Entity
@Table(
        name = "enrollments",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_enrollments_student_course",
                        columnNames = {"student_id", "course_id"}
                )
        },
        indexes = {
                @Index(
                        name = "idx_enrollments_student_id",
                        columnList = "student_id"
                ),
                @Index(
                        name = "idx_enrollments_course_id",
                        columnList = "course_id"
                ),
                @Index(
                        name = "idx_enrollments_status",
                        columnList = "status"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Enrollment {

    /**
     * The unique identifier for the enrollment.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "enrollment_id")
    private Long id;

    /**
     * The student associated with this enrollment.
     * This is a many-to-one relationship, where multiple enrollments can be associated with a single student.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "student_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_enrollments_student")
    )
    private User student;

    /**
     * The course associated with this enrollment.
     * This is a many-to-one relationship, where multiple enrollments can be associated with a single course.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "course_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_enrollments_course")
    )
    private Course course;

    /**
     * The date and time when the enrollment was created.
     * This field is automatically set to the current date and time when a new enrollment is created.
     */
    @Column(
            name = "enrollment_date",
            nullable = false,
            updatable = false
    )
    @Builder.Default
    private LocalDateTime enrollmentDate = LocalDateTime.now();

    /**
     * The current status of the enrollment.
     * This field uses the EnrollmentStatus enum to represent the different states of the enrollment.
     */
    @Enumerated(EnumType.STRING)
    @Column(
            name = "status",
            nullable = false,
            length = 20
    )
    @Builder.Default
    private EnrollmentStatus status = EnrollmentStatus.ENROLLED;

    /**
     * The date and time when the enrollment was completed.
     * This field is set when the enrollment status is changed to COMPLETED.
     */
    @Column(
            name = "completion_date"
    )
    private LocalDateTime completionDate;

    /**
     * The progress percentage of the enrollment.
     * This field represents the completion progress of the course for the enrolled student, ranging from 0.00 to 100.00.
     */
    @Column(
            name = "progress_percentage",
            nullable = false,
            precision = 5,
            scale = 2
    )
    @Builder.Default
    private BigDecimal progressPercentage = BigDecimal.ZERO;

    /**
     * The list of lesson progresses associated with this enrollment.
     * This is a one-to-many relationship, where an enrollment can have multiple lesson progresses.
     * The lesson progresses are automatically managed and removed when the enrollment is deleted.
     */
    @OneToMany(
            mappedBy = "enrollment",
            cascade = CascadeType.ALL,
            orphanRemoval = true
    )
    @Builder.Default
    private List<LessonProgress> lessonProgresses = new ArrayList<>();

}
