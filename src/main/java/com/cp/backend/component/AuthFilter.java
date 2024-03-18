package com.cp.backend.component;

import java.io.IOException;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.cp.backend.service.ApiKeyCache;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class AuthFilter extends OncePerRequestFilter {

    private ApiKeyCache apiKeyCache;

    public AuthFilter(ApiKeyCache apiKeyCache) {
        this.apiKeyCache = apiKeyCache;
    }

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain)
            throws IOException, ServletException {

        String path = request.getRequestURI();
        log.info("URL path is: " + path);
        if (!path.equals("/api/login")) {
            String apiKey = request.getHeader("X-Api-Key");
            log.info("api key exists. {}", apiKey);

            if (!StringUtils.equals(apiKey, apiKeyCache.get())) {
                response.setStatus(HttpStatus.FORBIDDEN.value());
                response.setContentType("text/plain");
                response.getWriter().write("Please provide proper API key!");
                return;
            }
        }

        filterChain.doFilter(request, response);

    }

}
