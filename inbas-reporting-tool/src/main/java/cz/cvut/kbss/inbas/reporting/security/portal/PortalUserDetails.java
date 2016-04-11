package cz.cvut.kbss.inbas.reporting.security.portal;

import cz.cvut.kbss.inbas.reporting.model_new.Person;
import cz.cvut.kbss.inbas.reporting.security.model.UserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

public class PortalUserDetails extends UserDetails {

    private final String basicAuthentication;

    public PortalUserDetails(Person person, Collection<GrantedAuthority> authorities, String basicAuthentication) {
        super(person, authorities);
        this.basicAuthentication = basicAuthentication;
    }

    public String getBasicAuthentication() {
        return basicAuthentication;
    }
}
