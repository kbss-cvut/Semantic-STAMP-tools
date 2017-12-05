package cz.cvut.kbss.reporting.environment.config;

import cz.cvut.kbss.reporting.security.HttpAuthenticationEntryPoint;
import cz.cvut.kbss.reporting.security.SecurityConstants;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * This configuration class is necessary when testing security of REST controllers (e.g. PersonController).
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class RestSecurityConfig extends WebSecurityConfigurerAdapter {

    private AuthenticationEntryPoint authenticationEntryPoint = new HttpAuthenticationEntryPoint();

    @Mock
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Mock
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    protected RestSecurityConfig() {
        MockitoAnnotations.initMocks(this);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().permitAll().and()
            .exceptionHandling().authenticationEntryPoint(authenticationEntryPoint)
            .and().headers().frameOptions().sameOrigin()
            .and().csrf().disable()
            .formLogin().successHandler(authenticationSuccessHandler)
            .failureHandler(authenticationFailureHandler)
            .loginProcessingUrl(SecurityConstants.SECURITY_CHECK_URI)
            .usernameParameter(SecurityConstants.USERNAME_PARAM).passwordParameter(SecurityConstants.PASSWORD_PARAM)
            .and().sessionManagement().maximumSessions(1);
    }
}
