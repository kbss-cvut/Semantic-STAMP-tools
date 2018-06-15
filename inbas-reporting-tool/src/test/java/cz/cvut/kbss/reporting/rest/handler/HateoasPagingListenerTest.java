package cz.cvut.kbss.reporting.rest.handler;

import cz.cvut.kbss.reporting.dto.reportlist.ReportDto;
import cz.cvut.kbss.reporting.environment.generator.OccurrenceReportGenerator;
import cz.cvut.kbss.reporting.model.OccurrenceReport;
import cz.cvut.kbss.reporting.rest.event.PaginatedResultRetrievedEvent;
import org.junit.Before;
import org.junit.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static cz.cvut.kbss.reporting.rest.util.HttpPaginationLink.*;
import static org.hamcrest.Matchers.containsString;
import static org.junit.Assert.*;

public class HateoasPagingListenerTest {

    private static final String BASE_URL = "http://localhost/rest/reports";

    private UriComponentsBuilder uriBuilder;
    private MockHttpServletResponse responseMock;

    private List<ReportDto> reports;

    private HateoasPagingListener listener;

    @Before
    public void setUp() {
        this.listener = new HateoasPagingListener();
        this.uriBuilder = UriComponentsBuilder.newInstance().scheme("http").host("localhost").path("rest/reports");
        this.responseMock = new MockHttpServletResponse();
        this.reports = IntStream.range(0, 10).mapToObj(i -> {
            final OccurrenceReport or = OccurrenceReportGenerator.generateOccurrenceReport(true);
            return or.toReportDto();
        }).collect(Collectors.toList());
    }

    @Test
    public void generatesNextRelativeLink() {
        final int size = 5;
        final Page<ReportDto> page = new PageImpl<>(reports.subList(0, size), PageRequest.of(0, size), reports.size());
        listener.onApplicationEvent(event(page));
        final String linkHeader = responseMock.getHeader(HttpHeaders.LINK);
        assertNotNull(linkHeader);
        final String nextLink = HttpLinkHeaderUtil.extractURIByRel(linkHeader, NEXT.getName());
        assertThat(nextLink, containsString(BASE_URL));
        assertThat(nextLink, containsString("page=1"));
        assertThat(nextLink, containsString("size=" + size));
    }

    private PaginatedResultRetrievedEvent event(Page<ReportDto> page) {
        return new PaginatedResultRetrievedEvent(this, uriBuilder, responseMock, page);
    }

    @Test
    public void generatesLastRelativeLink() {
        final int size = 5;
        final Page<ReportDto> page = new PageImpl<>(reports.subList(0, size), PageRequest.of(0, size), reports.size());
        listener.onApplicationEvent(event(page));
        final String linkHeader = responseMock.getHeader(HttpHeaders.LINK);
        assertNotNull(linkHeader);
        final String lastLink = HttpLinkHeaderUtil.extractURIByRel(linkHeader, LAST.getName());
        assertThat(lastLink, containsString(BASE_URL));
        assertThat(lastLink, containsString("page=1"));
        assertThat(lastLink, containsString("size=" + size));
    }

    @Test
    public void generatesPreviousRelativeLink() {
        final int size = 5;
        final Page<ReportDto> page = new PageImpl<>(reports.subList(size, reports.size()), PageRequest.of(1, size),
                reports.size());
        listener.onApplicationEvent(event(page));
        final String linkHeader = responseMock.getHeader(HttpHeaders.LINK);
        assertNotNull(linkHeader);
        final String lastLink = HttpLinkHeaderUtil.extractURIByRel(linkHeader, PREVIOUS.getName());
        assertThat(lastLink, containsString(BASE_URL));
        assertThat(lastLink, containsString("page=0"));
        assertThat(lastLink, containsString("size=" + size));
    }

    @Test
    public void generatesFirstRelativeLink() {
        final int size = 5;
        final Page<ReportDto> page = new PageImpl<>(reports.subList(size, reports.size()), PageRequest.of(1, size),
                reports.size());
        listener.onApplicationEvent(event(page));
        final String linkHeader = responseMock.getHeader(HttpHeaders.LINK);
        assertNotNull(linkHeader);
        final String lastLink = HttpLinkHeaderUtil.extractURIByRel(linkHeader, FIRST.getName());
        assertThat(lastLink, containsString(BASE_URL));
        assertThat(lastLink, containsString("page=0"));
        assertThat(lastLink, containsString("size=" + size));
    }

    @Test
    public void generatesAllRelativeLinks() {
        final int size = 3;
        final int pageNum = 2;
        final Page<ReportDto> page = new PageImpl<>(reports.subList(pageNum * size, pageNum * size + size),
                PageRequest.of(pageNum, size), reports.size());
        listener.onApplicationEvent(event(page));
        final String linkHeader = responseMock.getHeader(HttpHeaders.LINK);
        assertNotNull(linkHeader);
        final String nextLink = HttpLinkHeaderUtil.extractURIByRel(linkHeader, NEXT.getName());
        assertThat(nextLink, containsString("page=" + (pageNum + 1)));
        assertThat(nextLink, containsString("size=" + size));
        final String previousLink = HttpLinkHeaderUtil.extractURIByRel(linkHeader, PREVIOUS.getName());
        assertThat(previousLink, containsString("page=" + (pageNum - 1)));
        assertThat(previousLink, containsString("size=" + size));
        final String firstLink = HttpLinkHeaderUtil.extractURIByRel(linkHeader, FIRST.getName());
        assertThat(firstLink, containsString("page=0"));
        assertThat(firstLink, containsString("size=" + size));
        final String lastLink = HttpLinkHeaderUtil.extractURIByRel(linkHeader, LAST.getName());
        assertThat(lastLink, containsString("page=3"));
        assertThat(lastLink, containsString("size=" + size));
    }

    @Test
    public void generatesNoLinksForEmptyPage() {
        final int size = 5;
        final Page<ReportDto> page = new PageImpl<>(Collections.emptyList(), PageRequest.of(0, size), 0);
        listener.onApplicationEvent(event(page));
        final String linkHeader = responseMock.getHeader(HttpHeaders.LINK);
        assertNull(linkHeader);
    }

    @Test
    public void generatesPreviousAndFirstLinkForEmptyPageAfterEnd() {
        final int size = 5;
        final int pageNum = 4;
        final Page<ReportDto> page = new PageImpl<>(Collections.emptyList(), PageRequest.of(pageNum, size),
                reports.size());
        listener.onApplicationEvent(event(page));
        final String linkHeader = responseMock.getHeader(HttpHeaders.LINK);
        assertNotNull(linkHeader);
        final String previousLink = HttpLinkHeaderUtil.extractURIByRel(linkHeader, PREVIOUS.getName());
        assertThat(previousLink, containsString("page=" + (pageNum - 1)));
        assertThat(previousLink, containsString("size=" + size));
        final String firstLink = HttpLinkHeaderUtil.extractURIByRel(linkHeader, FIRST.getName());
        assertThat(firstLink, containsString("page=0"));
        assertThat(firstLink, containsString("size=" + size));
    }

    @Test
    public void generatesNoLinksForOnlyPage() {
        final Page<ReportDto> page = new PageImpl<>(reports, PageRequest.of(0, reports.size()), reports.size());
        listener.onApplicationEvent(event(page));
        final String linkHeader = responseMock.getHeader(HttpHeaders.LINK);
        assertNull(linkHeader);
    }
}