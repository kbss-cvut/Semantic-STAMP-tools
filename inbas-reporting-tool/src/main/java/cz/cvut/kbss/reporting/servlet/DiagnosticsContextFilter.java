package cz.cvut.kbss.reporting.servlet;

import org.slf4j.MDC;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.security.Principal;

public class DiagnosticsContextFilter extends GenericFilterBean {

    static final String DMC_KEY = "username";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        final HttpServletRequest req = (HttpServletRequest) servletRequest;
        final Principal principal = req.getUserPrincipal();
        boolean mdcSet = false;
        if (principal != null) {
            final String username = req.getUserPrincipal().getName();
            MDC.put(DMC_KEY, username);
            mdcSet = true;
        }

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            if (mdcSet) {
                MDC.remove(DMC_KEY);
            }
        }
    }
}
