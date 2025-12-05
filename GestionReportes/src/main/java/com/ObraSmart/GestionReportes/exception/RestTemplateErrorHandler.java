package com.ObraSmart.GestionReportes.exception;



import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import java.io.IOException;

public class RestTemplateErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse response) throws IOException {
        return response.getStatusCode().isError();
    }


    @Override
    public void handleError(ClientHttpResponse response) throws IOException {
        throw new ExternalServiceException(
                "Error en microservicio externo. Código: " + response.getStatusCode().value()
        );
    }
}

