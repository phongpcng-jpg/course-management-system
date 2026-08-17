package io.github.nguyenquephong13062003.course_management_system.models.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

/**
 * Represents the progress of a student in a specific lesson within a course.
 * This entity captures the relationship between an enrollment and a lesson, along with the completion status and timestamps.
 */
@Entity
@Table(
        name = "lesson_progress",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_lesson_progress_enrollment_lesson",
                        columnNames = {"enrollment_id", "lesson_id"}
                )
        },
        indexes = {
                @Index(
                        name = "idx_lesson_progress_enrollment_id",
                        columnList = "enrollment_id"
                ),
                @Index(
                        name = "idx_lesson_progress_lesson_id",
                        columnList = "lesson_id"
                ),
                @Index(
                        name = "idx_lesson_progress_is_completed",
                        columnList = "is_completed"
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LessonProgress {

    /**
     * The unique identifier for the lesson progress.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "progress_id")
    private Long id;

    /**
     * The enrollment associated with this lesson progress.
     * This is a many-to-one relationship, where multiple lesson progresses can be associated with a single enrollment.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "enrollment_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_lesson_progress_enrollment"
            )
    )
    private Enrollment enrollment;

    /**
     * The lesson associated with this lesson progress.
     * This is a many-to-one relationship, where multiple lesson progresses can be associated with a single lesson.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "lesson_id",
            nullable = false,
            foreignKey = @ForeignKey(
                    name = "fk_lesson_progress_lesson"
            )
    )
    private Lesson lesson;

    /**
     * Indicates whether the lesson has been completed by the student.
     */
    @Column(
            name = "is_completed",
            nullable = false
    )
    @Builder.Default
    private boolean completed = false;

    /**
     * The date and time when the lesson was completed.
     * This field is set when the lesson progress is marked as completed.
     */
    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    /**
     * The date and time when the lesson was last accessed by the student.
     * This field is updated whenever the student accesses the lesson.
     */
    @Column(name = "last_accessed_at")
    private LocalDateTime lastAccessedAt;

}
