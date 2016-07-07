package cz.cvut.kbss.inbas.reporting.rest;

import cz.cvut.kbss.inbas.reporting.rest.exception.BadRequestException;
import cz.cvut.kbss.inbas.reporting.service.arms.ArmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
}
