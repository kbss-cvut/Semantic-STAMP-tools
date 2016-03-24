package cz.cvut.kbss.inbas.reporting.security.model;

import cz.cvut.kbss.inbas.reporting.model.Person;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;

public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

    private static final String DEFAULT_ROLE = "ROLE_USER";

    private Person person;

    private final Set<GrantedAuthority> authorities;

    public UserDetails(Person person) {
        Objects.requireNonNull(person);
        this.person = person;
        this.authorities = Collections.unmodifiableSet(Collections.singleton(new SimpleGrantedAuthority(DEFAULT_ROLE)));
    }

    public UserDetails(Person person, Collection<GrantedAuthority> authorities) {
        Objects.requireNonNull(person);
        Objects.requireNonNull(authorities);
        this.person = person;
        final Set<GrantedAuthority> userAuthorities = new HashSet<>();
        userAuthorities.add(new SimpleGrantedAuthority(DEFAULT_ROLE));
        userAuthorities.addAll(authorities);
        this.authorities = userAuthorities;
    }

    public void eraseCredentials() {
        person.erasePassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return person.getPassword();
    }

    @Override
    public String getUsername() {
        return person.getUsername();
    }

    @Override
    public boolean isAccountNonExpired() {
        return false;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    public Person getUser() {
        return person;
    }
}
