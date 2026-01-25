package com.ObraSmart.GestionLogistica.service;

import com.ObraSmart.GestionLogistica.dto.AuthIdentityDto;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthClient {

    AuthIdentityDto fetchIdentity(HttpServletRequest request);
}
