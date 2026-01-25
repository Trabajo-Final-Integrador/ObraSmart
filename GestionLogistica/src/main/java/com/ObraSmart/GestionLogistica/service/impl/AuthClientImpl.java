package com.ObraSmart.GestionLogistica.service.impl;

import com.ObraSmart.GestionLogistica.dto.AuthIdentityDto;
import com.ObraSmart.GestionLogistica.service.AuthClient;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Service
public class AuthClientImpl implements AuthClient {

    private final RestTemplate restTemplate;
    private final String authBaseUrl;

    public AuthClientImpl(RestTemplate restTemplate,
                          @Value("${auth.base-url}") String authBaseUrl) {
        this.restTemplate = restTemplate;
        this.authBaseUrl = authBaseUrl.endsWith("/") ? authBaseUrl.substring(0, authBaseUrl.length() - 1) : authBaseUrl;
    }

    @Override
    public AuthIdentityDto fetchIdentity(HttpServletRequest request) {
        HttpHeaders headers = new HttpHeaders();
        String cookie = request.getHeader(HttpHeaders.COOKIE);
        if (cookie != null) {
            headers.set(HttpHeaders.COOKIE, cookie);
        }

        HttpEntity<Void> entity = new HttpEntity<>(headers);
        try {
            ResponseEntity<AuthIdentityDto> response = restTemplate.exchange(
                    authBaseUrl + "/auth/me",
                    HttpMethod.GET,
                    entity,
                    new ParameterizedTypeReference<AuthIdentityDto>() {}
            );
            return response.getBody();
        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.UNAUTHORIZED) {
                throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "No autenticado");
            }
            if (e.getStatusCode() == HttpStatus.FORBIDDEN) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "Acceso denegado");
            }
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Error llamando a auth");
        } catch (RestClientException e) {
            throw new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Auth no disponible");
        }
    }
}
