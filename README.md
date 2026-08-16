<div align="center">

# ☕ Java Assignment

## **IOC-Final: Course Management System**

</div>

## 📚📚📚 Course Information

|                 |                                                                    |
|-----------------|--------------------------------------------------------------------|
| **Institution** | RIKKEI EDUCATION                                                   |
| **Course**      | IOC - Java Web Services And Security                               |
| **Class**       | PTHB260316 - Java Backend                                          |
| **Session**     | Final Project                                                      |
| **Instructor**  | Pham Tuan Binh - Github: [tuan-binh](https://github.com/tuan-binh) |

---

## ⚙️ System Information

|                   |                                                                    |
|-------------------|--------------------------------------------------------------------|
| **System Name**   | Course Management System (BE IOC Java Web Services)                |
| **Root Project**  | TSUBASA PLUS Java Web Services                                     |
| **Documentaries** | [Java Web Services - Quản lý khóa học](https://docs.google.com/spreadsheets/d/1OL2egdwCUbUqwEQIndMS1vI0c2kZ3CxK/edit?usp=sharing&ouid=115827278237663380944&rtpof=true&sd=true) - [Local File](./docs/Java%20Web%20Services%20-%20Quản%20lý%20khóa%20học.xlsx)                             |
|                   | [SOFTWARE REQUIREMENTS SPECIFICATION (SRS)](https://docs.google.com/document/d/16L1xkjIU88pezh9Vic1j6se-x5Faj_VkmS6Kph4IHss/edit?usp=sharing) - [Local File](./docs/SRS%20-%20Quản%20lý%20khóa%20học.docx)                      |

---

## 🌐 REST API

| No. | API Endpoint                                                  | Method | Required Role | Description                                                                          | Mandatory | Mandatory Points | Optional Points | Implemented |
|:---:|---------------------------------------------------------------|:------:|---------------|--------------------------------------------------------------------------------------|:---------:|:----------------:|:---------------:|:-----------:|
| 1   | `/api/auth/login`                                             | POST   | PUBLIC         | Authenticate a user and issue a JWT access token                                     | ☑ | 2 |   | ☑ |
| 2   | `/api/auth/verify`                                            | POST   | AUTH           | Verify the user's authentication token                                               | ☑ | 2 |   | ☑ |
| 3   | `/api/auth/me`                                                | GET    | AUTH           | Retrieve the current user's profile information                                      | ☑ | 2 |   | ☑ |
| 4   | `/api/users`                                                  | GET    | ADMIN          | Retrieve all users (supports filtering by role and account status)                   | ☑ | 2 |   | ☑ |
| 5   | `/api/users/{user_id}`                                        | GET    | ADMIN          | Retrieve detailed information for a specific user                                    | ☑ | 2 |   | ☑ |
| 6   | `/api/users`                                                  | POST   | ADMIN          | Create a new user account                                                            | ☑ | 2 |   | ☑ |
| 7   | `/api/users/{user_id}/role`                                   | PUT    | ADMIN          | Update a user's role (an ADMIN cannot update another ADMIN's role)                   | ☑ | 2 |   | ☐ |
| 8   | `/api/users/{user_id}/status`                                 | PUT    | ADMIN          | Activate or deactivate a user account (`is_active`)                                  | ☑ | 3 |   | ☐ |
| 9   | `/api/users/{user_id}`                                        | DELETE | ADMIN          | Delete a user from the system                                                        | ☑ | 2 |   | ☐ |
| 10  | `/api/courses`                                                | GET    | AUTH           | Retrieve all courses (supports filtering by `PUBLISHED` status)                      | ☑ | 2 |   | ☐ |
| 11  | `/api/courses/{course_id}`                                    | GET    | AUTH           | Retrieve detailed course information, including published lessons                    | ☑ | 2 |   | ☐ |
| 12  | `/api/courses`                                                | POST   | ADMIN          | Create a new course, assign an instructor, initial status is `DRAFT`                 | ☑ | 3 |   | ☐ |
| 13  | `/api/courses/{course_id}`                                    | PUT    | ADMIN          | Update course information                                                            | ☑ | 2 |   | ☐ |
| 14  | `/api/courses/{course_id}/status`                             | PUT    | ADMIN          | Update the course status (`DRAFT`, `PUBLISHED`, `ARCHIVED`)                          | ☑ | 2 |   | ☐ |
| 15  | `/api/courses/{course_id}`                                    | DELETE | ADMIN          | Delete a course                                                                      | ☑ | 2 |   | ☐ |
| 16  | `/api/courses/{course_id}/lessons`                            | GET    | AUTH           | Retrieve all lessons in a course (published lessons only)                            | ☑ | 2 |   | ☐ |
| 17  | `/api/lessons/{lesson_id}`                                    | GET    | AUTH           | Retrieve detailed information for a published lesson                                 | ☑ | 2 |   | ☐ |
| 18  | `/api/courses/{course_id}/lessons`                            | POST   | TEACHER, ADMIN | Add a new lesson to a course (teacher must be assigned to the course)                | ☑ | 2 |   | ☐ |
| 19  | `/api/lessons/{lesson_id}`                                    | PUT    | TEACHER, ADMIN | Update lesson information                                                            | ☑ | 2 |   | ☐ |
| 20  | `/api/lessons/{lesson_id}/publish`                            | PUT    | TEACHER, ADMIN | Update a lesson's publication status (`is_published`)                                | ☑ | 2 |   | ☐ |
| 21  | `/api/lessons/{lesson_id}`                                    | DELETE | TEACHER, ADMIN | Delete a lesson                                                                      | ☑ | 2 |   | ☐ |
| 22  | `/api/enrollments`                                            | GET    | STUDENT        | Retrieve all courses in which the student is enrolled                                | ☑ | 2 |   | ☐ |
| 23  | `/api/enrollments`                                            | POST   | STUDENT        | Enroll in a course                                                                   | ☑ | 2 |   | ☐ |
| 24  | `/api/enrollments/{enrollment_id}`                            | GET    | STUDENT        | Retrieve enrollment details, including learning progress                             | ☑ | 2 |   | ☐ |
| 25  | `/api/enrollments/{enrollment_id}/complete_lesson/{lesson_id}`| PUT    | STUDENT        | Mark a lesson as completed and update learning progress                              | ☑ | 2 |   | ☐ |
| 26  | `/api/users/{user_id}`                                        | PUT    | OWNER, ADMIN   | Update a user's profile information                                                  | ☑ | 2 |   | ☐ |
| 27  | `/api/users/{user_id}/password`                               | PUT    | OWNER, ADMIN   | Change a user's password                                                             | ☑ | 2 |   | ☐ |
| 28  | `/api/courses?search={keyword}`                               | GET    | AUTH           | Search courses by keyword in the title or description                                | ☑ | 2 |   | ☐ |
| 29  | `/api/courses?teacher_id={teacher_id}`                        | GET    | AUTH           | Filter courses by instructor                                                         | ☑ | 2 |   | ☐ |
| 30  | `/api/auth/logout`                                            | POST   | AUTH           | Log out and invalidate the authentication token                                      | ☐ |   | 2 | ☐ |
| 31  | `/api/users?status={status}`                                  | GET    | ADMIN          | Filter users by account status (`active` / `inactive`)                               | ☐ |   | 3 | ☑ |
| 32  | `/api/courses?status={status}`                                | GET    | AUTH           | Filter courses by status (ADMIN sees all; others see only `PUBLISHED`)               | ☐ |   | 3 | ☐ |
| 33  | `/api/notifications`                                          | GET    | AUTH           | Retrieve the current user's notifications                                            | ☐ |   | 3 | ☐ |
| 34  | `/api/notifications/{notification_id}/read`                   | PUT    | AUTH           | Mark a notification as read                                                          | ☐ |   | 3 | ☐ |
| 35  | `/api/notifications`                                          | POST   | ADMIN          | Create a notification for users                                                      | ☐ |   | 3 | ☐ |
| 36  | `/api/notifications/{notification_id}`                        | DELETE | ADMIN          | Delete a notification                                                                | ☐ |   | 3 | ☐ |
| 37  | `/api/reports/top_courses`                                    | GET    | ADMIN          | Retrieve the most popular courses based on enrollment count                          | ☐ |   | 2 | ☐ |
| 38  | `/api/reports/student_progress/{student_id}`                  | GET    | ADMIN          | Retrieve a learning progress report for a specific student                           | ☐ |   | 3 | ☐ |
| 39  | `/api/reports/teacher_courses_overview/{teacher_id}`          | GET    | ADMIN          | Retrieve an overview report of a teacher's courses                                   | ☐ |   | 3 | ☐ |
| 40  | `/api/courses/{course_id}/reviews`                            | GET    | AUTH           | Retrieve reviews and comments for a course                                           | ☐ |   | 2 | ☐ |
| 41  | `/api/courses/{course_id}/reviews`                            | POST   | STUDENT        | Submit a review or comment for a completed course                                    | ☐ |   | 3 | ☐ |
| 42  | `/api/reviews/{review_id}`                                    | PUT    | OWNER, ADMIN   | Update a review or comment                                                           | ☐ |   | 2 | ☐ |
| 43  | `/api/reviews/{review_id}`                                    | DELETE | OWNER, ADMIN   | Delete a review or comment                                                           | ☐ |   | 3 | ☐ |
| 44  | `/api/lessons/{lesson_id}/content_preview`                    | GET    | AUTH           | Retrieve a lesson preview (e.g., a short excerpt of the lesson content)              | ☐ |   | 2 | ☐ |

---

### 🪪 User Classes and Characteristics (RBAC)

| No. | Role | Description |
|:---:|------|-------------|
| 1 | **PUBLIC** | Unauthenticated visitor. Can only access the login endpoint. |
| 2 | **AUTH** | Any authenticated user, regardless of role. Can view their own profile, search, and browse published courses and lessons. |
| 3 | **STUDENT** | A student who can enroll in courses, study lessons, mark lesson completion, and submit course reviews. |
| 4 | **TEACHER** | An instructor responsible for assigned courses. Can create, manage, edit, and publish lessons within courses they are assigned to. |
| 5 | **ADMIN** | System administrator with full privileges, including user management, role assignment, account activation/deactivation, course management, and report access. |
| 6 | **OWNER_OR_ADMIN** | Only the resource owner or an administrator is authorized to update or delete the resource (e.g., user profile or review). |

---

## 🗄️ Database

The relational database is designed in compliance with the **Third Normal Form (3NF)** to optimize data storage, eliminate redundancy, and maintain strict referential integrity. The following sections describe the core entities that make up the Learning Management System (LMS).

---

### 👤 Users

The **Users** table stores account information for every person using the system and provides Role-Based Access Control (RBAC). Each user belongs to one of the supported roles: **ADMIN**, **TEACHER**, or **STUDENT**.

| No. | Field | Data Type | Constraints | Description |
|:---:|-------|-----------|-------------|-------------|
| 1 | `user_id` | BIGINT | Primary Key, Auto Increment | Unique identifier of the user. |
| 2 | `username` | VARCHAR | NOT NULL, UNIQUE | Username used for authentication. |
| 3 | `password_hash` | VARCHAR | NOT NULL | Password securely encrypted using the BCrypt hashing algorithm. |
| 4 | `email` | VARCHAR | NOT NULL, UNIQUE | User's email address. |
| 5 | `full_name` | VARCHAR | NOT NULL | Full name of the user. |
| 6 | `role` | ENUM | NOT NULL, DEFAULT `STUDENT` | User role (`ADMIN`, `TEACHER`, or `STUDENT`). |
| 7 | `is_active` | BOOLEAN | NOT NULL, DEFAULT `TRUE` | Indicates whether the account is active. |
| 8 | `created_at` | DATETIME | DEFAULT CURRENT_TIMESTAMP | Timestamp when the account was created. |
| 9 | `updated_at` | DATETIME | Auto Updated | Timestamp of the most recent account update. |

---

### 📚 Courses

The **Courses** table stores general information about courses offered on the platform and associates each course with its responsible instructor.

| No. | Field | Data Type | Constraints | Description |
|:---:|-------|-----------|-------------|-------------|
| 1 | `course_id` | BIGINT | Primary Key, Auto Increment | Unique identifier of the course. |
| 2 | `title` | VARCHAR | NOT NULL | Course title displayed to users. |
| 3 | `description` | TEXT | Nullable | Detailed description, objectives, and learning outcomes of the course. |
| 4 | `teacher_id` | BIGINT | NOT NULL, Foreign Key → `Users.user_id` | Assigned instructor. The referenced user must have the `TEACHER` role. |
| 5 | `price` | DECIMAL | DEFAULT `0.00` | Course price. |
| 6 | `duration_hours` | INT | Nullable | Estimated course duration in hours. |
| 7 | `status` | ENUM | NOT NULL, DEFAULT `DRAFT` | Course lifecycle status (`DRAFT`, `PUBLISHED`, or `ARCHIVED`). |
| 8 | `created_at` | DATETIME | - | Timestamp when the course was created. |
| 9 | `updated_at` | DATETIME | - | Timestamp of the most recent course update. |

---

### 📖 Lessons

The **Lessons** table stores lesson information, learning materials, and display order for each lesson belonging to a specific course.

| No. | Field | Data Type | Constraints | Description |
|:---:|-------|-----------|-------------|-------------|
| 1 | `lesson_id` | BIGINT | Primary Key, Auto Increment | Unique identifier of the lesson. |
| 2 | `course_id` | BIGINT | NOT NULL, Foreign Key → `Courses.course_id` | Course containing this lesson. |
| 3 | `title` | VARCHAR | NOT NULL | Lesson title. |
| 4 | `content_url` | VARCHAR | Nullable | URL to learning resources such as videos, slides, or documents. |
| 5 | `text_content` | TEXT | Nullable | Lesson content in text format. |
| 6 | `order_index` | INT | NOT NULL | Display order of the lesson within the course. |
| 7 | `is_published` | BOOLEAN | NOT NULL, DEFAULT `FALSE` | Indicates whether the lesson is visible to students. |
| 8 | `created_at` | DATETIME | - | Timestamp when the lesson was created. |
| 9 | `updated_at` | DATETIME | - | Timestamp of the most recent lesson update. |

---

### 🎓 Enrollments

The **Enrollments** table records course registrations made by students and tracks their overall learning progress throughout each enrolled course.

| No. | Field | Data Type | Constraints | Description |
|:---:|-------|-----------|-------------|-------------|
| 1 | `enrollment_id` | BIGINT | Primary Key, Auto Increment | Unique identifier of the enrollment record. |
| 2 | `student_id` | BIGINT | NOT NULL, Foreign Key → `Users.user_id` | Student who enrolled in the course. The referenced user must have the `STUDENT` role. |
| 3 | `course_id` | BIGINT | NOT NULL, Foreign Key → `Courses.course_id` | Enrolled course. |
| 4 | `enrollment_date` | DATETIME | DEFAULT CURRENT_TIMESTAMP | Date and time when the student enrolled. |
| 5 | `status` | ENUM | NOT NULL, DEFAULT `ENROLLED` | Enrollment status (`ENROLLED`, `COMPLETED`, or `DROPPED`). |
| 6 | `completion_date` | DATETIME | Nullable | Date and time when the student completed the course. |
| 7 | `progress_percentage` | DECIMAL | DEFAULT `0.00`, CHECK (`0.00`–`100.00`) | Overall course completion percentage. |

**Special Constraints**

- `UNIQUE(student_id, course_id)`
  - Ensures that a student can enroll in the same course only once.

---

### 📈 LessonProgress

The **LessonProgress** table tracks each student's progress at the lesson level within a specific course enrollment. It records lesson completion status and the student's most recent access time.

| No. | Field | Data Type | Constraints | Description |
|:---:|-------|-----------|-------------|-------------|
| 1 | `progress_id` | BIGINT | Primary Key, Auto Increment | Unique identifier of the lesson progress record. |
| 2 | `enrollment_id` | BIGINT | NOT NULL, Foreign Key → `Enrollments.enrollment_id` | Associated course enrollment. |
| 3 | `lesson_id` | BIGINT | NOT NULL, Foreign Key → `Lessons.lesson_id` | Lesson being tracked. |
| 4 | `is_completed` | BOOLEAN | NOT NULL, DEFAULT `FALSE` | Indicates whether the student has completed the lesson. |
| 5 | `completed_at` | DATETIME | Nullable | Date and time when the lesson was marked as completed. |
| 6 | `last_accessed_at` | DATETIME | Auto Updated | Timestamp of the student's most recent access to the lesson. |

**Special Constraints**

- `UNIQUE(enrollment_id, lesson_id)`
  - Ensures that each lesson within a specific enrollment has only one progress record.

---

### 🔔 Notifications

The **Notifications** table stores system-generated or administrator-created notifications sent to individual users. Notifications may contain navigation links to related resources within the application.

| No. | Field | Data Type | Constraints | Description |
|:---:|-------|-----------|-------------|-------------|
| 1 | `notification_id` | BIGINT | Primary Key, Auto Increment | Unique identifier of the notification. |
| 2 | `user_id` | BIGINT | NOT NULL, Foreign Key → `Users.user_id` | Recipient of the notification. |
| 3 | `message` | TEXT | NOT NULL | Notification content displayed to the user. |
| 4 | `type` | VARCHAR | Nullable | Notification category (e.g., `NEW_COURSE`, `LESSON_UPDATED`, `ENROLLMENT_CONFIRMED`). |
| 5 | `target_url` | VARCHAR | Nullable | URL to navigate when the notification is clicked. |
| 6 | `is_read` | BOOLEAN | NOT NULL, DEFAULT `FALSE` | Indicates whether the notification has been read. |
| 7 | `created_at` | DATETIME | DEFAULT CURRENT_TIMESTAMP | Timestamp when the notification was created. |

---

### ⭐ Reviews

The **Reviews** table stores students' ratings and written feedback for completed courses. Each student may submit only one review per course.

| No. | Field | Data Type | Constraints | Description |
|:---:|-------|-----------|-------------|-------------|
| 1 | `review_id` | BIGINT | Primary Key, Auto Increment | Unique identifier of the review. |
| 2 | `course_id` | BIGINT | NOT NULL, Foreign Key → `Courses.course_id` | Reviewed course. |
| 3 | `student_id` | BIGINT | NOT NULL, Foreign Key → `Users.user_id` | Student who submitted the review. |
| 4 | `rating` | INT | NOT NULL, CHECK (`1`–`5`) | Star rating given to the course (1 to 5). |
| 5 | `comment` | TEXT | Nullable | Written review or feedback about the course or instructor. |
| 6 | `created_at` | DATETIME | DEFAULT CURRENT_TIMESTAMP | Timestamp when the review was created. |
| 7 | `updated_at` | DATETIME | Auto Updated | Timestamp of the most recent review update. |

**Special Constraints**

- `UNIQUE(course_id, student_id)`
  - Ensures that each student can submit only one review for a specific course.

---

### 📌 Entity Relationships Summary

| Parent Entity | Relationship | Child Entity |
|----------------|:------------:|--------------|
| Users | 1 → N | Courses (Teacher) |
| Users | 1 → N | Enrollments (Student) |
| Users | 1 → N | Notifications |
| Users | 1 → N | Reviews |
| Courses | 1 → N | Lessons |
| Courses | 1 → N | Enrollments |
| Courses | 1 → N | Reviews |
| Enrollments | 1 → N | LessonProgress |
| Lessons | 1 → N | LessonProgress |

---

> **Database Design Notes**
>
> - The schema complies with the **Third Normal Form (3NF)** to eliminate redundancy and improve maintainability.
> - All relationships are enforced using **foreign key constraints** to preserve referential integrity.
> - Business rules such as **one enrollment per student per course**, **one review per student per course**, and **one lesson progress record per enrolled lesson** are enforced through **unique constraints**.
> - Frequently queried columns (e.g., foreign keys, usernames, email addresses, and status fields) should be indexed to improve query performance.
> - Audit fields (`created_at`, `updated_at`) are included to support data tracking and future auditing requirements.

---

## 🛡️ Non-Functional Requirements

### 🛡️ Security

| No. | Requirement | Description |
|:---:|-------------|-------------|
| 1 | Password Encryption | All user passwords must be securely hashed using the BCrypt algorithm before being stored in the database. |
| 2 | JWT Authentication | All protected API endpoints require a valid JWT access token in the `Authorization: Bearer <token>` header. |
| 3 | Role-Based Access Control (RBAC) | Access permissions must be strictly enforced. An ADMIN cannot modify another ADMIN's role, and a TEACHER may manage only lessons belonging to courses they are assigned to. |

---

### ⚡ Performance

| No. | Requirement | Description |
|:---:|-------------|-------------|
| 1 | API Response Time | Standard CRUD operations should have an average response time below **200 ms**. |
| 2 | SQL Optimization | Database queries should be optimized using appropriate indexing strategies. |
| 3 | Database Indexes | Indexes should be created on foreign keys and frequently filtered columns such as `user_id`, `course_id`, and `status`. |

---

### 🗄️ Data Integrity

| No. | Requirement | Description |
|:---:|-------------|-------------|
| 1 | Automatic Progress Calculation | `Enrollments.progress_percentage` must be automatically calculated using the formula: `(Completed Published Lessons / Total Published Lessons) × 100`. |
| 2 | Unique Enrollment | A student may enroll in the same course only once. |
| 3 | Unique Review | A student may submit only one review for each course. |
| 4 | Database Constraints | Data integrity must be enforced using foreign keys, unique constraints, and validation rules. |

---

## 📦 API Response Standard

To ensure consistency across all client applications (React, Mobile, Postman, etc.), every REST API endpoint must return a standardized JSON response envelope, regardless of whether the request succeeds or fails.

---

### ✅ Success Response

```json
{
  "success": true,
  "status_code": 200,
  "message": "Operation completed successfully.",
  "data": { },
  "timestamp": "2026-08-02T18:15:30Z"
}
```

| No. | Field | Type | Description |
|:---:|-------|------|-------------|
| 1 | success | Boolean | Indicates whether the request was successfully processed. |
| 2 | status_code | Integer | Standard HTTP status code. |
| 3 | message | String | Human-readable response message. |
| 4 | data | Object / Array / Null | Returned resource, collection, or `null` if no data is available. |
| 5 | timestamp | String | Response timestamp in ISO-8601 UTC format. |

--- 

### ❌ Error Response

```json
{
  "success": false,
  "status_code": 400,
  "error_code": "INVALID_INPUT_DATA",
  "message": "Validation failed.",
  "errors": [
    {
      "field": "email",
      "message": "Invalid email format."
    }
  ],
  "timestamp": "2026-08-02T18:15:30Z"
}
```

| No. | Field | Type | Description |
|:---:|-------|------|-------------|
| 1 | success | Boolean | Indicates that the request failed. |
| 2 | status_code | Integer | Standard HTTP status code. |
| 3 | error_code | String | Machine-readable error identifier used by client applications. |
| 4 | message | String | Human-readable error description. |
| 5 | errors | Array / Null | Validation error details. May be `null` for non-validation errors. |
| 6 | timestamp | String | Response timestamp in ISO-8601 UTC format. |
---

## 🚨 Global Exception Handling

The application must implement centralized exception handling using @ControllerAdvice or @RestControllerAdvice to ensure all exceptions are converted into standardized API responses.

---

### 🚨 HTTP Status & Error Code Mapping

| No. | HTTP Status | Error Code | Description |
|:---:|-------------|------------|-------------|
| 1 | 400 Bad Request | INVALID_INPUT_DATA | Request validation failed or invalid input data. |
| 2 | 400 Bad Request | DUPLICATE_RESOURCE | Duplicate resource or unique constraint violation. |
| 3 | 401 Unauthorized | EXPIRED_JWT_TOKEN | JWT token has expired. |
| 4 | 401 Unauthorized | INVALID_JWT_TOKEN | JWT token is invalid or has been tampered with. |
| 5 | 401 Unauthorized | BAD_CREDENTIALS | Invalid username or password. |
| 6 | 403 Forbidden | ACCESS_DENIED | User is not authorized to perform the requested operation. |
| 7 | 404 Not Found | RESOURCE_NOT_FOUND | Requested resource does not exist. |
| 8 | 409 Conflict | INVALID_STATE_TRANSITION | Invalid resource state transition. |
| 9 | 500 Internal Server Error | INTERNAL_SERVER_ERROR | Unexpected server-side exception. |

---

### 🔒 Exception Handling Requirements

| No. | Requirement | Description |
|:---:|-------------|-------------|
| 1 | Information Hiding | Never expose stack traces, SQL statements, or internal implementation details in production responses. |
| 2 | Error Logging | All critical exceptions (`500 Internal Server Error`) must be logged using SLF4J / Logback with timestamp, user identifier (if available), and full stack trace. |
| 3 | Automatic Validation | Validate incoming requests using Jakarta Bean Validation annotations (`@NotNull`, `@NotBlank`, `@Size`, `@Email`, `@Min`, `@Max`) before business logic execution. |
| 4 | Centralized Exception Handling | Handle all exceptions through `@ControllerAdvice` / `@RestControllerAdvice` to ensure consistent API responses. |

---

## 👨‍💻 Author

|               |                                                                             |
|---------------|-----------------------------------------------------------------------------|
| **Full Name** | Nguyen Que Phong                                                            |
| **Email**     | [nguyenquephong13062003@gmail.com](mailto:nguyenquephong13062003@gmail.com) |
| **Phone**     | [0908130603](tel:+84908130603)                                              |

---

<div align="center">

**Made with ❤️ using Java**

</div>