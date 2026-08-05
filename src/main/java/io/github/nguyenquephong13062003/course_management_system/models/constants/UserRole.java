package io.github.nguyenquephong13062003.course_management_system.models.constants;


/**
 * User Role
 */
public enum UserRole {

    /**
     * Admin role with full access to the system.
     */
    ADMIN, 

    /**
     * Teacher role with access to manage courses and students.
     */
    TEACHER, 

    /**
     * Student role with access to view courses and enroll.
     */
    STUDENT
}
