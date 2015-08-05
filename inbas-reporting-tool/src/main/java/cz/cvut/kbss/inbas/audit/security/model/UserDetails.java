package cz.cvut.kbss.inbas.audit.security.model;

import org.springframework.security.core.GrantedAuthority;

import java.net.URI;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author ledvima1
 */
public class UserDetails implements org.springframework.security.core.userdetails.UserDetails {

    private URI userUri;
    private String username;
    private String password;

    private final Set<GrantedAuthority> authorities;

    public UserDetails(URI userUri, String username, String password, Collection<GrantedAuthority> authorities) {
        this.userUri = userUri;
        this.username = username;
        this.password = password;
        this.authorities = Collections.unmodifiableSet(new HashSet<>(authorities));
    }

    public void eraseCredentials() {
        this.password = null;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserDetails that = (UserDetails) o;

        if (userUri != null ? !userUri.equals(that.userUri) : that.userUri != null) return false;
        if (username != null ? !username.equals(that.username) : that.username != null) return false;
        return authorities.equals(that.authorities);

    }

    @Override
    public int hashCode() {
        int result = userUri != null ? userUri.hashCode() : 0;
        result = 31 * result + (username != null ? username.hashCode() : 0);
        result = 31 * result + (authorities.hashCode());
        return result;
    }

    @Override
    public String toString() {
        return username;
    }
}
