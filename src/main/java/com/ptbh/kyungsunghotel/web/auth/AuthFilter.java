package com.ptbh.kyungsunghotel.web.auth;

import com.ptbh.kyungsunghotel.web.SessionConstants;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.Arrays;

public class AuthFilter extends OncePerRequestFilter {

    private static final String[] WHITE_LIST = {"/", "/boards", "/boards/{id:\\d+}", "/members/{id:\\d+}",
            "/login", "/logout", "/join", "/css/*", "/img/*", "/*.ico", "/error"};

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        AntPathMatcher matcher = new AntPathMatcher();
        return Arrays.stream(WHITE_LIST)
                .anyMatch(pattern -> matcher.match(pattern, request.getServletPath()));
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String requestURI = request.getRequestURI();
        try {
            HttpSession session = request.getSession(false);
            if (session == null || session.getAttribute(SessionConstants.AUTH_INFO) == null) {
                response.sendRedirect("/login?redirectURL=" + requestURI);
                return;
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            throw e;
        }
    }
}
