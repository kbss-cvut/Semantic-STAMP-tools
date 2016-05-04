package cz.cvut.kbss.inbas.reporting.rest;

import cz.cvut.kbss.inbas.reporting.model.arms.AccidentOutcome;
import cz.cvut.kbss.inbas.reporting.model.arms.BarrierEffectiveness;
import cz.cvut.kbss.inbas.reporting.rest.exception.BadRequestException;
import cz.cvut.kbss.inbas.reporting.service.arms.ArmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/arms")
public class ArmsController {

    @Autowired
    private ArmsService armsService;

    @RequestMapping(method = RequestMethod.GET)
    public Short getArmsIndex(@RequestParam("accidentOutcome") String accidentOutcome,
                              @RequestParam("barrierEffectiveness") String barrierEffectiveness) {
        try {
            final AccidentOutcome ao = AccidentOutcome.fromString(accidentOutcome);
            final BarrierEffectiveness be = BarrierEffectiveness.fromString(barrierEffectiveness);
            return armsService.calculateArmsIndex(ao, be);
        } catch (IllegalArgumentException e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}
