
package com.july.gestion_polizas;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

@Component
public class ApiKeyFilter extends OncePerRequestFilter {

    @Value("${app.security.api-key}")
    private String apiKeyEsperada;

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain)
            throws ServletException, IOException {

        String apiKeyRecibida = request.getHeader("x-api-key");

        if (apiKeyRecibida == null ||
                !MessageDigest.isEqual(
                        apiKeyEsperada.getBytes(StandardCharsets.UTF_8),
                        apiKeyRecibida.getBytes(StandardCharsets.UTF_8))) {

            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.setCharacterEncoding("UTF-8");
            response.getWriter().write(
                    "{\"error\":\"API key ausente o inválida\"}");
            return;
        }

        filterChain.doFilter(request, response);
    }
}