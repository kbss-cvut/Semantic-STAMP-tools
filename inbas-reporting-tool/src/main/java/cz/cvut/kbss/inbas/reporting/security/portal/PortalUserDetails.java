package cz.cvut.kbss.inbas.reporting.security.portal;

import cz.cvut.kbss.inbas.reporting.model.Person;
import cz.cvut.kbss.inbas.reporting.rest.dto.model.PortalUser;
import cz.cvut.kbss.inbas.reporting.security.model.UserDetails;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;

public class PortalUserDetails extends UserDetails {

    private final String basicAuthentication;

    public PortalUserDetails(Person person, String basicAuthentication) {
        super(person);
        addPortalUserRole();
        this.basicAuthentication = basicAuthentication;
    }

    public PortalUserDetails(Person person, Collection<GrantedAuthority> authorities, String basicAuthentication) {
        super(person, authorities);
        addPortalUserRole();
        this.basicAuthentication = basicAuthentication;
    }

    private void addPortalUserRole() {
        this.authorities.add(new SimpleGrantedAuthority(PortalUser.PORTAL_USER_ROLE));
    }

    public String getBasicAuthentication() {
        return basicAuthentication;
    }
}
