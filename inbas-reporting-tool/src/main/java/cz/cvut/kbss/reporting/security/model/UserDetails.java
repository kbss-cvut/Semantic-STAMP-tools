package cz.cvut.kbss.reporting.security.model;

import cz.cvut.kbss.reporting.model.Person;
import cz.cvut.kbss.reporting.security.SecurityConstants;
import cz.cvut.kbss.reporting.service.security.SecurityUtils;
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
        initRoles();
    }

    public UserDetails(Person person, Collection<GrantedAuthority> authorities) {
        Objects.requireNonNull(person);
        Objects.requireNonNull(authorities);
        this.person = person;
        this.authorities = new HashSet<>();
        initRoles();
        this.authorities.addAll(authorities);
    }

    private void initRoles() {
        addDefaultRole();
        this.authorities.addAll(person.getTypes().stream().filter(SecurityConstants.Role::exists)
                                      .map(t -> new SimpleGrantedAuthority(
                                              SecurityConstants.Role.fromType(t).getName()))
                                      .collect(Collectors.toSet()));
    }

    private void addDefaultRole() {
        authorities.add(new SimpleGrantedAuthority(SecurityConstants.Role.USER.getName()));
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
        return !SecurityUtils.isLocked(person);
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
