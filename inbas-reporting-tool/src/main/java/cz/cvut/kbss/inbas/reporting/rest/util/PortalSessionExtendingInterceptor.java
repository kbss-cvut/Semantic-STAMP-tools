package cz.cvut.kbss.inbas.reporting.rest.util;

import cz.cvut.kbss.inbas.audit.service.security.PortalSessionManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Intercepts REST service calls and calls {@link PortalSessionManager} to keep the portal session alive (if running on
 * portal).
 */
@Service
public class PortalSessionExtendingInterceptor implements HandlerInterceptor {

    @Autowired
    private PortalSessionManager portalSessionManager;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o)
            throws Exception {
        // Default
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                           ModelAndView modelAndView) throws Exception {
        // Do nothing
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception {
        portalSessionManager.keepPortalSessionAlive();
    }
}
