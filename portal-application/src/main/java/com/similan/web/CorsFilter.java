package com.similan.web;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.google.common.net.HttpHeaders;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
@Component
public class CorsFilter extends OncePerRequestFilter {
  private static final String WILDCARD = "*";

  List<String> allowedOrigins = new ArrayList<String>(0);

  @Override
  protected void doFilterInternal(HttpServletRequest request,
      HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {
    if (allowedOrigins.isEmpty()) {
      filterChain.doFilter(request, response);
      return;
    }
    String origin = request.getHeader(HttpHeaders.ORIGIN);
    String allowOrigin;
    boolean allowAnything = allowedOrigins.contains(WILDCARD);
    if (allowAnything) {
      allowOrigin = origin == null ? WILDCARD : origin;
    } else if (origin != null && allowedOrigins.contains(origin)) {
      allowOrigin = origin;
    } else {
      filterChain.doFilter(request, response);
      return;
    }
    response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_ORIGIN, allowOrigin);

    response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_METHODS,
        "OPTIONS, GET, POST, PUT, DELETE, PATCH");
    response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_HEADERS,
        HttpHeaders.CONTENT_TYPE + ", " + HttpHeaders.AUTHORIZATION);
    response.addHeader(HttpHeaders.ACCESS_CONTROL_EXPOSE_HEADERS,
        HttpHeaders.WWW_AUTHENTICATE);
    response.addHeader(HttpHeaders.ACCESS_CONTROL_MAX_AGE, "1800");
    response.addHeader(HttpHeaders.ACCESS_CONTROL_ALLOW_CREDENTIALS, "true");

    filterChain.doFilter(request, response);
  }
}