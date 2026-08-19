package io.github.nguyenquephong13062003.course_management_system.models.services.impl;

import io.github.nguyenquephong13062003.course_management_system.exceptions.AuthException;
import io.github.nguyenquephong13062003.course_management_system.exceptions.NotFoundException;
import io.github.nguyenquephong13062003.course_management_system.models.constants.UserRole;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.NotificationCreateRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.NotificationResponse;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.wrappers.PageResponse;
import io.github.nguyenquephong13062003.course_management_system.models.entities.Course;
import io.github.nguyenquephong13062003.course_management_system.models.entities.Lesson;
import io.github.nguyenquephong13062003.course_management_system.models.entities.Notification;
import io.github.nguyenquephong13062003.course_management_system.models.entities.User;
import io.github.nguyenquephong13062003.course_management_system.models.repositories.IEnrollmentRepository;
import io.github.nguyenquephong13062003.course_management_system.models.repositories.INotificationRepository;
import io.github.nguyenquephong13062003.course_management_system.models.repositories.IUserRepository;
import io.github.nguyenquephong13062003.course_management_system.models.services.INotificationService;
import io.github.nguyenquephong13062003.course_management_system.security.principal.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Implementation of notification-related business operations.
 */
@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class NotificationServiceImpl implements INotificationService {

    private final INotificationRepository notificationRepository;
    private final IUserRepository userRepository;
    private final IEnrollmentRepository enrollmentRepository;

    /**
     * Retrieves notifications belonging to the current authenticated user.
     *
     * @param isRead   optional read-status filter.
     * @param pageable pagination and sorting information.
     * @return paginated notification responses.
     */
    @Override
    public PageResponse<NotificationResponse> getMyNotifications(
            Boolean isRead,
            Pageable pageable
    ) {

        User authenticatedUser = getAuthenticatedUser();

        log.debug(
                "Fetching notifications for userId={}, isRead={}, page={}, size={}",
                authenticatedUser.getId(),
                isRead,
                pageable.getPageNumber(),
                pageable.getPageSize()
        );

        Page<Notification> notificationPage;

        if (isRead == null) {
            notificationPage = notificationRepository.findByUser_Id(
                    authenticatedUser.getId(),
                    pageable
            );
        } else {
            notificationPage = notificationRepository.findByUser_IdAndIsRead(
                    authenticatedUser.getId(),
                    isRead,
                    pageable
            );
        }

        Page<NotificationResponse> responsePage =
                notificationPage.map(this::toNotificationResponse);

        return PageResponse.<NotificationResponse>builder()
                .items(responsePage.getContent())
                .page(responsePage.getNumber())
                .size(responsePage.getSize())
                .totalItems(responsePage.getTotalElements())
                .totalPages(responsePage.getTotalPages())
                .isLast(responsePage.isLast())
                .build();
    }

    /**
     * Marks a notification as read.
     *
     * @param notificationId the ID of the notification.
     * @return the updated notification response.
     */
    @Override
    @Transactional
    public NotificationResponse markAsRead(
            Long notificationId
    ) {

        User authenticatedUser = getAuthenticatedUser();

        log.debug(
                "Marking notification as read, notificationId={}, userId={}",
                notificationId,
                authenticatedUser.getId()
        );

        Notification notification = notificationRepository
                .findByNotificationIdAndUser_Id(notificationId, authenticatedUser.getId())
                .orElseThrow(() -> {
                    log.warn(
                            "Notification not found or access denied, " +
                                    "notificationId={}, userId={}",
                            notificationId,
                            authenticatedUser.getId()
                    );

                    return new NotFoundException(
                            "Notification not found"
                    );
                });

        if (!Boolean.TRUE.equals(notification.getIsRead())) {
            notification.setIsRead(true);
            notification = notificationRepository.save(notification);

            log.debug(
                    "Notification marked as read successfully, " +
                            "notificationId={}, userId={}",
                    notificationId,
                    authenticatedUser.getId()
            );
        }

        return toNotificationResponse(notification);
    }

    /**
     * Creates a new notification for a specific user.
     *
     * @param request the notification creation request.
     * @return the created notification response.
     */
    @Override
    @Transactional
    public NotificationResponse createNotification(
            NotificationCreateRequest request
    ) {
        log.debug(
                "Creating notification for userId={}, type={}",
                request.getUserId(),
                request.getType()
        );

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> {
                    log.warn(
                            "Cannot create notification because userId={} " +
                                    "does not exist",
                            request.getUserId()
                    );

                    return new NotFoundException(
                            "User not found"
                    );
                });

        Notification notification = Notification.builder()
                .user(user)
                .message(request.getMessage())
                .type(request.getType())
                .targetUrl(request.getTargetUrl())
                .isRead(false)
                .build();

        Notification savedNotification =
                notificationRepository.save(notification);

        log.debug(
                "Notification created successfully, notificationId={}, " +
                        "userId={}",
                savedNotification.getNotificationId(),
                request.getUserId()
        );

        return toNotificationResponse(savedNotification);
    }

    /**
     * Deletes a notification.
     *
     * @param notificationId the ID of the notification to delete.
     */
    @Override
    @Transactional
    public void deleteNotification(Long notificationId) {
        log.debug(
                "Deleting notification, notificationId={}",
                notificationId
        );

        Notification notification = notificationRepository.findById(
                notificationId
        ).orElseThrow(() -> {
            log.warn(
                    "Notification not found, notificationId={}",
                    notificationId
            );

            return new NotFoundException(
                    "Notification not found"
            );
        });

        notificationRepository.delete(notification);

        log.debug(
                "Notification deleted successfully, notificationId={}",
                notificationId
        );
    }

    @Override
    @Transactional
    public void notifyNewCourse(Course course) {

        List<User> students =
                userRepository.findAllByRoleAndActiveTrue(UserRole.STUDENT);

        List<Notification> notifications = students.stream()
                .map(student -> Notification.builder()
                        .user(student)
                        .message("The new course has been published.: " + course.getTitle())
                        .type("NEW_COURSE")
                        .targetUrl("/api/courses/" + course.getId())
                        .isRead(false)
                        .build())
                .toList();

        notificationRepository.saveAll(notifications);

        log.info(
                "Created NEW_COURSE notifications for courseId={}, recipientCount={}",
                course.getId(),
                notifications.size()
        );
    }

    @Override
    @Transactional
    public void notifyLessonUpdated(Lesson lesson) {

        List<User> students =
                enrollmentRepository.findStudentsByCourseId(lesson.getCourse().getId());

        List<Notification> notifications = students.stream()
                .map(student -> Notification.builder()
                        .user(student)
                        .message("The lesson has been updated: " + lesson.getTitle())
                        .type("LESSON_UPDATED")
                        .targetUrl("/api/lessons/" + lesson.getLessonId())
                        .isRead(false)
                        .build())
                .toList();

        notificationRepository.saveAll(notifications);

        log.info(
                "Created LESSON_UPDATED notifications for lessonId={}, recipientCount={}",
                lesson.getLessonId(),
                notifications.size()
        );
    }

    @Override
    @Transactional
    public void notifyEnrollmentConfirmed(User student, Course course) {

        Notification notification = Notification.builder()
                .user(student)
                .message("Đăng ký khóa học thành công: " + course.getTitle())
                .type("ENROLLMENT_CONFIRMED")
                .targetUrl("/api/courses/" + course.getId())
                .isRead(false)
                .build();

        notificationRepository.save(notification);

        log.info(
                "Created ENROLLMENT_CONFIRMED notification for studentId={}, courseId={}",
                student.getId(),
                course.getId()
        );
    }

    /**
     * Maps a Notification entity to its response DTO.
     *
     * @param notification the notification entity.
     * @return notification response DTO.
     */
    private NotificationResponse toNotificationResponse(
            Notification notification
    ) {
        return NotificationResponse.builder()
                .notificationId(notification.getNotificationId())
                .message(notification.getMessage())
                .type(notification.getType())
                .targetUrl(notification.getTargetUrl())
                .isRead(notification.getIsRead())
                .createdAt(notification.getCreatedAt())
                .build();
    }

    /**
     * Retrieves the currently authenticated user from the security context.
     *
     * @return the authenticated User entity
     * @throws AuthException if authentication is missing or invalid
     */
    private User getAuthenticatedUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || !authentication.isAuthenticated()) {
            throw new AuthException("JWT verification failed: authentication is missing or unauthenticated");
        }

        if (!(authentication.getPrincipal() instanceof CustomUserDetails userDetails)) {
            throw new AuthException(
                    "JWT verification failed: invalid authentication principal"
            );
        }

        return userDetails.getUser();

    }
}
