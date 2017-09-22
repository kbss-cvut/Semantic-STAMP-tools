package cz.cvut.kbss.reporting.servlet;

import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.model.Person;
import cz.cvut.kbss.reporting.security.model.AuthenticationToken;
import cz.cvut.kbss.reporting.security.model.UserDetails;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.slf4j.MDC;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.security.Principal;
import java.util.Collections;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class DiagnosticsContextFilterTest {

    @Mock
    private HttpServletRequest requestMock;

    @Mock
    private HttpServletResponse responseMock;

    @Mock
    private FilterChain chainMock;

    private Person person = Generator.getPerson();

    private DiagnosticsContextFilter filter = new DiagnosticsContextFilter();

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void setsDiagnosticsContextWhenProcessingChain() throws Exception {
        final Principal token = new AuthenticationToken(Collections.emptyList(), new UserDetails(person));
        when(requestMock.getUserPrincipal()).thenReturn(token);
        doAnswer((answer) -> {
            assertEquals(MDC.get(DiagnosticsContextFilter.DMC_KEY), Generator.USERNAME);
            return null;
        }).when(chainMock).doFilter(requestMock, responseMock);

        filter.doFilter(requestMock, responseMock, chainMock);
        verify(chainMock).doFilter(requestMock, responseMock);
    }

    @Test
    public void doesNotSetDiagnosticsContextForAnonymousPrincipal() throws Exception {
        when(requestMock.getUserPrincipal()).thenReturn(null);
        doAnswer((answer) -> {
            assertNull(MDC.get(DiagnosticsContextFilter.DMC_KEY));
            return null;
        }).when(chainMock).doFilter(requestMock, responseMock);

        filter.doFilter(requestMock, responseMock, chainMock);
        verify(chainMock).doFilter(requestMock, responseMock);
    }
}