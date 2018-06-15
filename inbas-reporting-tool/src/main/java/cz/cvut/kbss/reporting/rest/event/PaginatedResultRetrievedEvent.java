package cz.cvut.kbss.reporting.rest.event;

import org.springframework.context.ApplicationEvent;
import org.springframework.data.domain.Page;
import org.springframework.web.util.UriComponentsBuilder;

import javax.servlet.http.HttpServletResponse;

/**
 * Fired when a paginated result is retrieved by a REST controller, so that HATEOAS headers can be added to response.
 */
public class PaginatedResultRetrievedEvent extends ApplicationEvent {

    private final UriComponentsBuilder uriBuilder;
    private final HttpServletResponse response;
    private final Page<?> page;

    public PaginatedResultRetrievedEvent(Object source, UriComponentsBuilder uriBuilder, HttpServletResponse response,
                                         Page<?> page) {
        super(source);
        this.uriBuilder = uriBuilder;
        this.response = response;
        this.page = page;
    }

    public UriComponentsBuilder getUriBuilder() {
        return uriBuilder;
    }

    public HttpServletResponse getResponse() {
        return response;
    }

    public Page<?> getPage() {
        return page;
    }
}
