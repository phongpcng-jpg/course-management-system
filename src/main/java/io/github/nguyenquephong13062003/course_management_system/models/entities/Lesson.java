package io.github.nguyenquephong13062003.course_management_system.models.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

/**
 * The Lesson class represents a lesson entity in the course management system.
 * It is mapped to the "lessons" table in the database and contains information about individual lessons within a course.
 */
@Entity
@Table(
        name = "lessons",
        indexes = {
                @Index(
                        name = "idx_lessons_course_id",
                        columnList = "course_id"
                ),
                @Index(
                        name = "idx_lessons_course_order",
                        columnList = "course_id, order_index"
                )
        },
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_lessons_course_order",
                        columnNames = {"course_id", "order_index"}
                )
        }
)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Lesson {

    /**
     * The unique identifier for the lesson.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "lesson_id")
    private Long lessonId;

    /**
     * The course to which this lesson belongs.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "course_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_lessons_course")
    )
    private Course course;

    /**
     * The title of the lesson.
     */
    @Column(
            name = "title",
            nullable = false,
            length = 255
    )
    private String title;

    /**
     * The URL of the lesson's content (e.g., video, document).
     */
    @Column(
            name = "content_url",
            length = 1000
    )
    private String contentUrl;

    /**
     * The public ID of the lesson's content in Cloudinary.
     * This field is used to identify and manage the content resource in Cloudinary.
     */
    @Column(name = "content_public_id")
    private String contentPublicId;

    /**
     * The text content of the lesson.
     */
    @Column(
            name = "text_content",
            columnDefinition = "TEXT"
    )
    private String textContent;

    /**
     * The order index of the lesson within the course.
     * This field is used to determine the sequence of lessons in a course.
     */
    @Column(
            name = "order_index",
            nullable = false
    )
    private Integer orderIndex;

    /**
     * Indicates whether the lesson is published or not.
     * A published lesson is visible to students, while an unpublished lesson is hidden.
     */
    @Column(
            name = "is_published",
            nullable = false
    )
    @Builder.Default
    private Boolean isPublished = false;
    
    /**
     * The timestamp when the lesson was created.
     * This field is automatically populated with the current timestamp when the lesson is created.
     */
    @CreationTimestamp
    @Column(
            name = "created_at",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;

    /**
     * The timestamp when the lesson was last updated.
     * This field is automatically updated with the current timestamp whenever the lesson is modified.
     */
    @UpdateTimestamp
    @Column(
            name = "updated_at",
            nullable = false
    )
    private LocalDateTime updatedAt;

}
