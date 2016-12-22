package cz.cvut.kbss.reporting.rest;

import cz.cvut.kbss.reporting.model.safetyissue.SafetyIssueRiskAssessment;
import cz.cvut.kbss.reporting.rest.exception.BadRequestException;
import cz.cvut.kbss.reporting.service.arms.ArmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequestMapping("/arms")
public class ArmsController {

    @Autowired
    private ArmsService armsService;

    @RequestMapping(method = RequestMethod.GET)
    public Integer getArmsIndex(@RequestParam("accidentOutcome") String accidentOutcome,
                                @RequestParam("barrierEffectiveness") String barrierEffectiveness) {
        try {
            return armsService.calculateArmsIndex(URI.create(accidentOutcome), URI.create(barrierEffectiveness));
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    @RequestMapping(value = "/sira", method = RequestMethod.POST, consumes = MediaType.APPLICATION_JSON_VALUE)
    public URI getSiraValue(@RequestBody SafetyIssueRiskAssessment sira) {
        final URI result;
        try {
            result = armsService.calculateSafetyIssueRiskAssessment(sira);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        }
        if (result == null) {
            throw new IllegalArgumentException("Missing some of the values required for SIRA calculation.");
        }
        return result;
    }
}
