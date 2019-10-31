package cz.cvut.kbss.reporting.rest.handler;

import cz.cvut.kbss.reporting.rest.event.PaginatedResultRetrievedEvent;
import cz.cvut.kbss.reporting.rest.util.HttpPaginationLink;
import org.springframework.context.ApplicationListener;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import static cz.cvut.kbss.reporting.util.Constants.PAGE;
import static cz.cvut.kbss.reporting.util.Constants.PAGE_SIZE;

/**
 * Generates HATEOAS paging headers based on the paginated result retrieved by a REST controller.
 */
@Component
public class HateoasPagingListener implements ApplicationListener<PaginatedResultRetrievedEvent> {

    @Override
    public void onApplicationEvent(PaginatedResultRetrievedEvent event) {
        final Page<?> page = event.getPage();
        final LinkHeader header = new LinkHeader();
        if (page.hasNext()) {
            header.addLink(generateNextPageLink(page, event.getUriBuilder()), HttpPaginationLink.NEXT);
            header.addLink(generateLastPageLink(page, event.getUriBuilder()), HttpPaginationLink.LAST);
        }
        if (page.hasPrevious()) {
            header.addLink(generatePreviousPageLink(page, event.getUriBuilder()), HttpPaginationLink.PREVIOUS);
            header.addLink(generateFirstPageLink(page, event.getUriBuilder()), HttpPaginationLink.FIRST);
        }
        if (header.hasLinks()) {
            event.getResponse().addHeader(HttpHeaders.LINK, header.toString());
        }
    }

    private String generateNextPageLink(Page<?> page, UriComponentsBuilder uriBuilder) {
        return uriBuilder.replaceQueryParam(PAGE, page.getNumber() + 1).replaceQueryParam(PAGE_SIZE, page.getSize())
                         .build().encode().toUriString();
    }

    private String generatePreviousPageLink(Page<?> page, UriComponentsBuilder uriBuilder) {
        return uriBuilder.replaceQueryParam(PAGE, page.getNumber() - 1).replaceQueryParam(PAGE_SIZE, page.getSize())
                         .build().encode().toUriString();
    }

    private String generateFirstPageLink(Page<?> page, UriComponentsBuilder uriBuilder) {
        return uriBuilder.replaceQueryParam(PAGE, 0).replaceQueryParam(PAGE_SIZE, page.getSize())
                         .build().encode().toUriString();
    }

    private String generateLastPageLink(Page<?> page, UriComponentsBuilder uriBuilder) {
        return uriBuilder.replaceQueryParam(PAGE, page.getTotalPages() - 1).replaceQueryParam(PAGE_SIZE, page.getSize())
                         .build().encode().toUriString();
    }

    private static class LinkHeader {

        private final StringBuilder linkBuilder = new StringBuilder();

        private void addLink(String url, HttpPaginationLink type) {
            if (linkBuilder.length() > 0) {
                linkBuilder.append(", ");
            }
            linkBuilder.append('<').append(url).append('>').append("; ").append("rel=\"").append(type.getName())
                       .append('\"');
        }

        private boolean hasLinks() {
            return linkBuilder.length() > 0;
        }

        @Override
        public String toString() {
            return linkBuilder.toString();
        }
    }
}
