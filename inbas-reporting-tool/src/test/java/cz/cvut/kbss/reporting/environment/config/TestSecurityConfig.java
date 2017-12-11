package cz.cvut.kbss.reporting.environment.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpSession;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Configuration
@ComponentScan(basePackages = "cz.cvut.kbss.reporting.security")
public class TestSecurityConfig {

    @Bean
    @Primary
    public HttpSession getSession() {
        return new MockHttpSession();
    }

    @Bean
    public HttpServletRequest request() {
        return new MockHttpServletRequest();
    }
}
