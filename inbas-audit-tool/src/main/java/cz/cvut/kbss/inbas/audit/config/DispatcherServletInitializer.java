package cz.cvut.kbss.inbas.audit.config;

import com.sun.faces.config.ConfigureListener;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import javax.faces.webapp.FacesServlet;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

/**
 * Created by ledvima1 on 8.4.15.
 */
public class DispatcherServletInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {

    @Override
    protected Class<?>[] getRootConfigClasses() {
        return new Class<?>[]{AppConfig.class};
    }

    @Override
    protected Class<?>[] getServletConfigClasses() {
        return null;
    }

    @Override
    protected String[] getServletMappings() {
        return new String[]{"/inbas/*"};
    }

    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        System.out.println("****** Application Context Initialization ******");

        // Use JSF view templates saved as *.xhtml, for use with Facelets
        servletContext.setInitParameter("javax.faces.DEFAULT_SUFFIX", ".xhtml");
        // Enable special Facelets debug output during development
        servletContext.setInitParameter("javax.faces.PROJECT_STAGE", "Development");
        // Causes Facelets to refresh templates during development
        servletContext.setInitParameter("javax.faces.FACELETS_REFRESH_PERIOD", "1");

        ServletRegistration.Dynamic facesServlet = servletContext.addServlet("Faces Servlet", FacesServlet.class);
        facesServlet.setLoadOnStartup(1);
        facesServlet.addMapping("*.xhtml");
        servletContext.setInitParameter("com.sun.faces.forceLoadConfiguration", "true");

        servletContext.addListener(ConfigureListener.class);

        super.onStartup(servletContext);
    }
}
