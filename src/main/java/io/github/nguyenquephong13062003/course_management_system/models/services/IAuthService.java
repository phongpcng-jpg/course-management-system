package io.github.nguyenquephong13062003.course_management_system.models.services;

import io.github.nguyenquephong13062003.course_management_system.models.dtos.requests.LoginRequests;
import io.github.nguyenquephong13062003.course_management_system.models.dtos.responses.LoginResponse;

public interface IAuthService {

    LoginResponse login(LoginRequests loginRequests);

}
