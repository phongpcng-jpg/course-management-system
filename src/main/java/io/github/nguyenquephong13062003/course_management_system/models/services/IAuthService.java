package io.github.nguyenquephong13062003.course_management_system.models.services;

import io.github.nguyenquephong13062003.course_management_system.exceptions.AuthException;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.LoginRequest;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.LoginResponse;

/**
 * IAuthService
 * Interface for authentication services.
 */
public interface IAuthService {


    /**
     * Authenticates a user based on the provided login request.
     *
     * @param loginRequest The login request containing username and password.
     * @return A LoginResponse containing the access token and user role.
     * @throws AuthException If authentication fails due to incorrect credentials.
     */
    LoginResponse login(LoginRequest loginRequest) throws AuthException;

}
