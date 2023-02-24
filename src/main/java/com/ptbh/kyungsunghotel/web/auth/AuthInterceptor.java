package com.ptbh.kyungsunghotel.web.auth;

import com.ptbh.kyungsunghotel.web.SessionConstants;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute(SessionConstants.AUTH_INFO) == null) {
            response.sendRedirect("/login?redirectURL=" + request.getRequestURI());
            return false;
        }
        return true;
    }
}
