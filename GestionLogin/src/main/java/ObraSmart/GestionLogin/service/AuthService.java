package ObraSmart.GestionLogin.service;

import ObraSmart.GestionLogin.dto.ForgotPasswordRequest;
import ObraSmart.GestionLogin.dto.LoginRequestDto;
import ObraSmart.GestionLogin.dto.LoginResponse;
import ObraSmart.GestionLogin.dto.ResetPasswordRequest;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    LoginResponse login(LoginRequestDto req, HttpServletRequest http);
    void logout(HttpServletRequest http);
    String forgot(ForgotPasswordRequest req);
    void reset(ResetPasswordRequest req);
}