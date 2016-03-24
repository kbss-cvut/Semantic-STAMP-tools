package cz.cvut.kbss.inbas.reporting.rest.dto.mapper;

import cz.cvut.kbss.inbas.reporting.config.RestConfig;
import cz.cvut.kbss.inbas.reporting.config.SecurityConfig;
import cz.cvut.kbss.inbas.reporting.config.ServiceConfig;
import cz.cvut.kbss.inbas.reporting.dto.*;
import cz.cvut.kbss.inbas.reporting.dto.incursion.AircraftIntruderDto;
import cz.cvut.kbss.inbas.reporting.dto.incursion.PersonIntruderDto;
import cz.cvut.kbss.inbas.reporting.dto.incursion.RunwayIncursionDto;
import cz.cvut.kbss.inbas.reporting.environment.config.MockSesamePersistence;
import cz.cvut.kbss.inbas.reporting.environment.util.Environment;
import cz.cvut.kbss.inbas.reporting.environment.util.Generator;
import cz.cvut.kbss.inbas.reporting.model.Aircraft;
import cz.cvut.kbss.inbas.reporting.model.Location;
import cz.cvut.kbss.inbas.reporting.model.Organization;
import cz.cvut.kbss.inbas.reporting.model.reports.*;
import cz.cvut.kbss.inbas.reporting.model.reports.incursions.Intruder;
import cz.cvut.kbss.inbas.reporting.model.reports.incursions.LowVisibilityProcedure;
import cz.cvut.kbss.inbas.reporting.model.reports.incursions.PersonIntruder;
import cz.cvut.kbss.inbas.reporting.model.reports.incursions.RunwayIncursion;
import cz.cvut.kbss.inbas.reporting.persistence.dao.OrganizationDao;
import cz.cvut.kbss.inbas.reporting.service.data.FileDataLoader;
import cz.cvut.kbss.inbas.reporting.test.config.TestPersistenceConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.net.URI;
import java.util.*;

import static org.junit.Assert.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {RestConfig.class,
        ServiceConfig.class,
        TestPersistenceConfig.class,
        MockSesamePersistence.class,
        SecurityConfig.class})
public class MappersTest {

    @Autowired
    private ReportMapper reportMapper;

    @Autowired
    private OrganizationDao organizationDao;

    @Test
    public void testMapAircraftToAircraftIntruder() {
        final Aircraft aircraft = initAircraft();
        final AircraftIntruderDto result = reportMapper.aircraftToAircraftIntruder(aircraft);
        assertNotNull(result);
        assertEquals(aircraft.getUri(), result.getUri());
        assertEquals(aircraft.getCallSign(), result.getCallSign());
        assertEquals(aircraft.getOperationType(), result.getOperationType());
        assertEquals(aircraft.getOperator(), result.getOperator());
        assertEquals(aircraft.getStateOfRegistry(), result.getStateOfRegistry());
        assertEquals(aircraft.getRegistration(), result.getRegistration());
    }

    private Aircraft initAircraft() {
        final Aircraft aircraft = new Aircraft();
        aircraft.setUri(URI.create("http://krizik.felk.cvut.cz/ontologies/inbas#aircraftTest"));
        aircraft.setCallSign("OK12345");
        aircraft.setOperationType("passenger flight");
        aircraft.setOperator(new Organization("CSA"));
        aircraft.setStateOfRegistry("CZ");
        aircraft.setRegistration("OK-123");
        return aircraft;
    }

    @Test
    public void eventTypeAssessmentWithRunwayIncursionIsMappedToRunwayIncursionDto() {
        final EventTypeAssessment assessment = initEventTypeAssessment(true);
        final EventTypeAssessmentDto result = reportMapper
                .eventTypeAssessmentToEventTypeAssessmentDto(assessment);
        assertNotNull(result);
        assertEquals(assessment.getUri(), result.getUri());
        assertTrue(result instanceof RunwayIncursionDto);
        assertEquals(assessment.getRunwayIncursion().getUri(), ((RunwayIncursionDto) result).getIncursionUri());
        final RunwayIncursionDto ri =
                (RunwayIncursionDto) result;
        assertEquals(assessment.getEventType().getId(), ri.getEventType().getId());
        assertTrue(ri.getIntruder() instanceof AircraftIntruderDto);
        assertEquals(assessment.getRunwayIncursion().getLocation(), ri.getLocation());
        assertEquals(assessment.getRunwayIncursion().getLowVisibilityProcedure(), ri.getLvp());
    }

    private EventTypeAssessment initEventTypeAssessment(boolean addRunwayIncursion) {
        final EventTypeAssessment eta = new EventTypeAssessment();
        eta.setUri(URI.create("http://krizik.felk.cvut.cz/ontologies/inbas#eta"));
        eta.setEventType(new EventType(URI.create(
                "http://onto.fel.cvut.cz/ontologies/2014/ECCAIRS_Aviation_1.3.0.12/eccairs-events-390#category_2200000")));
        if (addRunwayIncursion) {
            eta.setDescription("Runway incursion assessment.");
            final RunwayIncursion ri = new RunwayIncursion();
            ri.setUri(URI.create("http://krizik.felk.cvut.cz/ontologies/inbas#ri"));
            ri.setLocation(new Location());
            ri.setLowVisibilityProcedure(LowVisibilityProcedure.CAT_III);
            ri.setIntruder(new Intruder(initAircraft()));
            eta.setRunwayIncursion(ri);
        }
        return eta;
    }

    @Test
    public void eventTypeAssessmentWithoutRunwayIncursionIsMappedToGeneralEvent() {
        final EventTypeAssessment assessment = initEventTypeAssessment(false);
        final EventTypeAssessmentDto result = reportMapper
                .eventTypeAssessmentToEventTypeAssessmentDto(
                        assessment);
        assertNotNull(result);
        assertTrue(result instanceof GeneralEventDto);
        final GeneralEventDto ge = (GeneralEventDto) result;
        assertEquals(assessment.getUri(), ge.getUri());
        assertEquals(assessment.getEventType(), ge.getEventType());
        assertEquals(assessment.getDescription(), ge.getDescription());
    }

    @Test
    public void testMapOccurrenceReportToOccurrenceReportDto() {
        final PreliminaryReport report = initOccurrenceReport();
        final PreliminaryReportDto result =
                reportMapper.preliminaryReportToPreliminaryReportDto(
                        report);
        assertNotNull(result);
        assertEquals(report.getSummary(), result.getSummary());
        assertEquals(report.getSeverityAssessment(), result.getSeverityAssessment());
        assertEquals(report.getCreated(), result.getCreated());
        boolean riFound = false, geFound = false;
        for (EventTypeAssessmentDto et : result.getTypeAssessments()) {
            if (et instanceof RunwayIncursionDto) {
                riFound = true;
                assertTrue(((RunwayIncursionDto) et)
                        .getIntruder() instanceof AircraftIntruderDto);
            } else if (et instanceof GeneralEventDto) {
                geFound = true;
            }
        }
        assertTrue(riFound);
        assertTrue(geFound);
    }

    private PreliminaryReport initOccurrenceReport() {
        final PreliminaryReport or = new PreliminaryReport();
        or.setTypeAssessments(
                new HashSet<>(Arrays.asList(initEventTypeAssessment(true), initEventTypeAssessment(false))));
        or.setSummary("Something happened.");
        or.setCreated(new Date());
        or.setSeverityAssessment(OccurrenceSeverity.MAJOR_INCIDENT);
        or.setCorrectiveMeasures(Collections.singleton(new CorrectiveMeasure("Pilot was fired.")));
        return or;
    }

    @Test
    public void personIntruderDtoToEntityLoadsExistingOrganization() {
        final Organization org = new Organization("CSA");
        organizationDao.persist(org);
        final PersonIntruderDto intruderDto = initPersonIntruderDto(org);

        final PersonIntruder result =
                reportMapper.personIntruderDtoToPersonIntruder(intruderDto);
        assertNotNull(result);
        assertEquals(intruderDto.getCategory(), result.getCategory());
        assertEquals(intruderDto.getWasDoing(), result.getWhatWasDoing());
        assertNotNull(result.getOrganization());
        assertEquals(org, result.getOrganization());
    }

    private PersonIntruderDto initPersonIntruderDto(Organization organization) {
        final PersonIntruderDto intruder = new PersonIntruderDto();
        intruder.setOrganization(organization.getName());
        intruder.setCategory("Staff");
        intruder.setWasDoing("Walking");
        intruder.setIntruderType(PersonIntruder.INTRUDER_TYPE);
        return intruder;
    }

    @Test
    public void testRunwayIncursionDtoToRunwayIncursion() {
        final RunwayIncursionDto dto = initRunwayIncursionDto();

        final RunwayIncursion result = reportMapper.runwayIncursionDtoToRunwayIncursion(dto);
        assertNotNull(result);
        assertEquals(dto.getLvp(), result.getLowVisibilityProcedure());
        assertEquals(dto.getLocation(), result.getLocation());
        assertNotNull(result.getIntruder());
        assertNotNull(result.getIntruder().getPerson());
    }

    private RunwayIncursionDto initRunwayIncursionDto() {
        final RunwayIncursionDto dto = new RunwayIncursionDto();
        dto.setIntruder(initPersonIntruderDto(new Organization("CSA")));
        dto.setLvp(LowVisibilityProcedure.CAT_I);
        dto.setLocation(new Location());
        return dto;
    }

    @Test
    public void testOccurrenceReportDtoWithRunwayIncursionAndGeneralEventToEntity() {
        final PreliminaryReportDto dto = initOccurrenceReportDto();

        final PreliminaryReport result = reportMapper.preliminaryReportDtoToPreliminaryReport(dto);
        assertNotNull(result);
        assertEquals(dto.getSummary(), result.getSummary());
        boolean riFound = false, geFound = false;
        for (EventTypeAssessment eta : result.getTypeAssessments()) {
            if (eta.getRunwayIncursion() != null) {
                riFound = true;
                assertNotNull(eta.getRunwayIncursion().getIntruder().getPerson());
            } else {
                geFound = true;
                assertFalse(eta.getDescription().isEmpty());
            }
        }
        assertTrue(riFound);
        assertTrue(geFound);
    }

    private PreliminaryReportDto initOccurrenceReportDto() {
        final PreliminaryReportDto dto = new PreliminaryReportDto();
        final Set<EventTypeAssessmentDto> eTypes = new HashSet<>();
        eTypes.add(initRunwayIncursionDto());
        final GeneralEventDto ge = new GeneralEventDto();
        ge.setEventType(new EventType(URI.create(
                "http://onto.fel.cvut.cz/ontologies/2014/ECCAIRS_Aviation_1.3.0.12/eccairs-events-390#category_2200000")));
        ge.setDescription("Some general event.");
        eTypes.add(ge);
        dto.setTypeAssessments(eTypes);
        dto.setSummary("Does not really matter, its a simple string.");
        return dto;
    }

    @Test
    public void assignsRandomReferenceIdsToFactorDtos() throws Exception {
        final String json = new FileDataLoader().loadData("test_data/reportWithFactorHierarchy.json", null);
        final InvestigationReport report = Environment.getObjectMapper().readValue(json, InvestigationReport.class);

        final FactorDto dto = reportMapper.factorToFactorDto(report.getRootFactor());
        verifyFactorsHaveReferenceIds(dto);
    }

    private void verifyFactorsHaveReferenceIds(FactorDto root) {
        assertNotNull(root.getReferenceId());
        if (root.getChildren() != null) {
            root.getChildren().forEach(this::verifyFactorsHaveReferenceIds);
        }
    }

    @Test
    public void testInvestigationReportDtoWithLinksToInvestigationReport() throws Exception {
        final String json = new FileDataLoader().loadData("test_data/reportDtoFactorsCauseMitigate.json", null);
        final InvestigationReportDto dto = Environment.getObjectMapper().readValue(json, InvestigationReportDto.class);

        final InvestigationReport result = reportMapper.investigationReportDtoToInvestigationReport(dto);
        assertSame(dto.getOccurrence(), result.getOccurrence());
        assertNotNull(result.getRootFactor());
        verifyLinks(dto.getLinks().getCauses(), result, "cause");
        verifyLinks(dto.getLinks().getMitigates(), result, "mitigatingFactor");
    }

    private void verifyLinks(Set<Link> links, InvestigationReport result, String linkType) {
        for (Link link : links) {
            final Factor linkFrom = findFactor(link.getFrom().getUri(), result.getRootFactor());
            assertNotNull(linkFrom);
            final Factor linkTo = findFactor(link.getTo().getUri(), result.getRootFactor());
            assertNotNull(linkTo);
            boolean found = false;
            final Set<Factor> factors = linkType.equals("cause") ? linkTo.getCauses() : linkTo.getMitigatingFactors();
            for (Factor c : factors) {
                if (c.getUri().equals(linkFrom.getUri())) {
                    found = true;
                    break;
                }
            }
            assertTrue(found);
        }
    }

    private Factor findFactor(URI uri, Factor root) {
        if (root.getUri().equals(uri)) {
            return root;
        }
        if (root.getChildren() != null) {
            Factor factor;
            for (Factor f : root.getChildren()) {
                factor = findFactor(uri, f);
                if (factor != null) {
                    return factor;
                }
            }
        }
        return null;
    }

    @Test
    public void testInvestigationReportWithCausesAndMitigatesToInvestigationReportDtoWithLinks() throws Exception {
        final InvestigationReport report = Generator.generateInvestigationWithCausesAndMitigatingFactors();

        final InvestigationReportDto result = reportMapper.investigationReportToInvestigationReportDto(report);
        assertNotNull(result);
        assertEquals(report.getUri(), result.getUri());
        final Map<URI, FactorDto> factorDtos = new HashMap<>();
        mapFactorDtoTree(result.getRootFactor(), factorDtos);
        verifyLinksExistence(report.getRootFactor(), result.getLinks(), factorDtos);
    }

    private void mapFactorDtoTree(FactorDto dto, Map<URI, FactorDto> map) {
        map.put(dto.getUri(), dto);
        if (dto.getChildren() != null && !dto.getChildren().isEmpty()) {
            dto.getChildren().forEach(child -> mapFactorDtoTree(child, map));
        }
    }

    private void verifyLinksExistence(Factor root, Links links, Map<URI, FactorDto> dtoTree) {
        final FactorDto rootDto = dtoTree.get(root.getUri());
        assertNotNull(rootDto);
        if (!root.getCauses().isEmpty()) {
            for (Factor cause : root.getCauses()) {
                final FactorDto causeDto = dtoTree.get(cause.getUri());
                assertNotNull(causeDto);
                assertTrue(linkExists(links.getCauses(), causeDto, rootDto));
            }
        }
        if (!root.getMitigatingFactors().isEmpty()) {
            for (Factor mitigation : root.getMitigatingFactors()) {
                final FactorDto mitigationDto = dtoTree.get(mitigation.getUri());
                assertNotNull(mitigationDto);
                assertTrue(linkExists(links.getMitigates(), mitigationDto, rootDto));
            }
        }
        if (!root.getChildren().isEmpty()) {
            root.getChildren().forEach(child -> verifyLinksExistence(child, links, dtoTree));
        }
    }

    private boolean linkExists(Set<Link> links, FactorDto from, FactorDto to) {
        for (Link link : links) {
            if (link.getFrom().equals(from) && link.getTo().equals(to)) {
                return true;
            }
        }
        return false;
    }

    @Test
    public void reportToDtoWithPreliminaryReportInstanceReturnsPreliminaryReportDto() {
        final Report report = Generator.generatePreliminaryReport(Generator.ReportType.WITHOUT_TYPE_ASSESSMENTS);
        final AbstractReportDto result = reportMapper.reportToReportDto(report);
        assertNotNull(result);
        assertTrue(result instanceof PreliminaryReportDto);
    }

    @Test
    public void reportToDtoWithInvestigationInstanceReturnsPInvestigationDto() {
        final Report report = Generator.generateMinimalInvestigation();
        final AbstractReportDto result = reportMapper.reportToReportDto(report);
        assertNotNull(result);
        assertTrue(result instanceof InvestigationReportDto);
    }

    @Test
    public void reportDtoToReportWithPreliminaryDtoReturnsPreliminaryReport() {
        final PreliminaryReportDto dto = reportMapper
                .preliminaryReportToPreliminaryReportDto(Generator.generatePreliminaryReport(
                        Generator.ReportType.WITHOUT_TYPE_ASSESSMENTS));
        final Report result = reportMapper.reportDtoToReport(dto);
        assertTrue(result instanceof PreliminaryReport);
    }

    @Test
    public void reportDtoToReportWithInvestigationDtoReturnsInvestigation() {
        final InvestigationReportDto dto = reportMapper
                .investigationReportToInvestigationReportDto(Generator.generateMinimalInvestigation());
        final Report result = reportMapper.reportDtoToReport(dto);
        assertTrue(result instanceof InvestigationReport);
    }
}
