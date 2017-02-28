package cz.cvut.kbss.reporting.security.model;

import cz.cvut.kbss.reporting.model.Person;
import cz.cvut.kbss.reporting.security.SecurityConstants;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Collectors;

public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

    private Person person;

    protected final Set<GrantedAuthority> authorities;

    public UserDetails(Person person) {
        Objects.requireNonNull(person);
        this.person = person;
        this.authorities = new HashSet<>();
        resolveRoles();
    }

    public UserDetails(Person person, Collection<GrantedAuthority> authorities) {
        Objects.requireNonNull(person);
        Objects.requireNonNull(authorities);
        this.person = person;
        this.authorities = new HashSet<>();
        resolveRoles();
        this.authorities.addAll(authorities);
    }

    private void resolveRoles() {
        authorities.addAll(person.getTypes().stream().filter(SecurityConstants.Role::exists)
                                 .map(t -> new SimpleGrantedAuthority(SecurityConstants.Role
                                         .getRole(t).getRoleName())).collect(Collectors.toSet()));
    }

    public void eraseCredentials() {
        person.erasePassword();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Collections.unmodifiableCollection(authorities);
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
