package cz.cvut.kbss.inbas.audit.rest;

import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by ledvima1 on 21.5.15.
 */
@RestController
@RequestMapping("/helloworld")
public class HelloWorldController {

    @RequestMapping(method = RequestMethod.GET, produces = {MediaType.APPLICATION_JSON_VALUE})
    public String hello() {
        return "Hello world";
    }
}
