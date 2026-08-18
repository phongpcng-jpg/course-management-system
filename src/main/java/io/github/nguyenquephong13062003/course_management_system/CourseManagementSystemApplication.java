package io.github.nguyenquephong13062003.course_management_system;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * The main entry point for the Course Management System application.
 * This class is annotated with @SpringBootApplication, indicating that it is a Spring Boot application.
 */
@SpringBootApplication
public class CourseManagementSystemApplication {

	/**
	 * The main method that starts the Spring Boot application.
	 * @param args command-line arguments passed to the application
	 */
	public static void main(String[] args) {
		SpringApplication.run(CourseManagementSystemApplication.class, args);
	}

}
