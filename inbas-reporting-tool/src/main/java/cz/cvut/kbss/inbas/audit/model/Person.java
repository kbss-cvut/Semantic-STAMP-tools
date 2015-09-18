package cz.cvut.kbss.inbas.audit.model;

import cz.cvut.kbss.inbas.audit.util.Constants;
import cz.cvut.kbss.inbas.audit.util.Vocabulary;
import cz.cvut.kbss.jopa.model.annotations.Id;
import cz.cvut.kbss.jopa.model.annotations.OWLClass;
import cz.cvut.kbss.jopa.model.annotations.OWLDataProperty;
import cz.cvut.kbss.jopa.model.annotations.Properties;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.io.Serializable;
import java.net.URI;
import java.util.Map;
import java.util.Set;

/**
 * @author ledvima1
 */
@OWLClass(iri = Vocabulary.Person)
public class Person implements HasDerivableUri, Serializable {

    @Id
    private URI uri;

    @OWLDataProperty(iri = Vocabulary.p_firstName)
    private String firstName;

    @OWLDataProperty(iri = Vocabulary.p_lastName)
    private String lastName;

    @OWLDataProperty(iri = Vocabulary.p_username)
    private String username;

    @OWLDataProperty(iri = Vocabulary.p_password)
    private String password;

    @Properties
    private Map<String, Set<String>> properties;

    public URI getUri() {
        return uri;
    }

    public void setUri(URI uri) {
        this.uri = uri;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * Encodes password of this person.
     *
     * @param encoder Encoder to user to encode the password
     */
    public void encodePassword(PasswordEncoder encoder) {
        if (password == null || password.isEmpty()) {
            throw new IllegalStateException("Cannot encode an empty password.");
        }
        this.password = encoder.encode(password);
    }

    /**
     * Erases the password.
     * <p>
     * Handy for example before sending the instance outside the application.
     */
    public void erasePassword() {
        this.password = null;
    }

    public Map<String, Set<String>> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Set<String>> properties) {
        this.properties = properties;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName + " <" + uri + ">";
    }

    /**
     * Generates URI using {@link Constants#PERSON_BASE_URI} and the person's first and last name.
     */
    @Override
    public void generateUri() {
        if (firstName == null || firstName.isEmpty()) {
            throw new IllegalStateException("Missing first name.");
        }
        if (lastName == null || lastName.isEmpty()) {
            throw new IllegalStateException("Missing last name.");
        }
        this.uri = URI.create(Constants.PERSON_BASE_URI + firstName + "+" + lastName);
    }

    /**
     * Returns true if attributes of this instance are equal to those of the other instance.
     * <p>
     * Instance uri and username are not compared, because they are assumed to be read-only.
     *
     * @param other The other instance to compare to this one
     * @return true if the selected attributes are equal, false otherwise
     */
    public boolean valueEquals(Person other) {
        return other != null && firstName.equals(other.firstName) && lastName.equals(other.lastName) &&
                password.equals(other.password);
    }
}
