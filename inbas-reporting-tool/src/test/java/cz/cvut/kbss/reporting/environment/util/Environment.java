package cz.cvut.kbss.reporting.environment.util;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import cz.cvut.kbss.jsonld.JsonLd;
import cz.cvut.kbss.jsonld.jackson.JsonLdModule;
import cz.cvut.kbss.reporting.environment.generator.Generator;
import cz.cvut.kbss.reporting.model.Person;
import cz.cvut.kbss.reporting.model.util.HasUri;
import cz.cvut.kbss.reporting.security.model.AuthenticationToken;
import cz.cvut.kbss.reporting.security.model.UserDetails;
import cz.cvut.kbss.reporting.util.Constants;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.context.SecurityContextImpl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.security.Principal;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;

public class Environment {

    private static Person currentUser;

    private static ObjectMapper objectMapper;

    private Environment() {
        throw new AssertionError();
    }

    /**
     * Initializes security context with the specified user.
     *
     * @param user User to set as currently authenticated
     */
    public static void setCurrentUser(Person user) {
        currentUser = user;
        final UserDetails userDetails = new UserDetails(user, new HashSet<>());
        SecurityContext context = new SecurityContextImpl();
        context.setAuthentication(new AuthenticationToken(userDetails.getAuthorities(), userDetails));
        SecurityContextHolder.setContext(context);
    }

    /**
     * Gets current user as security principal.
     *
     * @return Current user authentication as principal or {@code null} if there is no current user
     */
    public static Principal getCurrentUserPrincipal() {
        return SecurityContextHolder.getContext() != null ? SecurityContextHolder.getContext().getAuthentication() :
                null;
    }

    public static Person getCurrentUser() {
        return currentUser;
    }

    /**
     * Gets a Jackson object mapper for mapping JSON to Java and vice versa.
     *
     * @return Object mapper
     */
    public static ObjectMapper getObjectMapper() {
        if (objectMapper == null) {
            objectMapper = new ObjectMapper();
            objectMapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
            objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
            objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        return objectMapper;
    }

    /**
     * Creates a Jackson JSON-LD message converter.
     *
     * @return JSON-LD message converter
     */
    public static HttpMessageConverter<?> createJsonLdMessageConverter() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(JsonParser.Feature.AUTO_CLOSE_SOURCE, true);
        mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JsonLdModule());
        final MappingJackson2HttpMessageConverter converter = new MappingJackson2HttpMessageConverter(mapper);
        converter.setSupportedMediaTypes(Collections.singletonList(MediaType.valueOf(JsonLd.MEDIA_TYPE)));
        return converter;
    }

    public static HttpMessageConverter<?> createDefaultMessageConverter() {
        return new MappingJackson2HttpMessageConverter(getObjectMapper());
    }

    public static HttpMessageConverter<?> createStringEncodingMessageConverter() {
        return new StringHttpMessageConverter(Charset.forName(Constants.UTF_8_ENCODING));
    }

    /**
     * Returns true if the two collections contain elements with the same URIs.
     *
     * @param one First collection
     * @param two Second collection
     * @return True if the collections match, false otherwise
     */
    public static boolean areEqual(Collection<? extends HasUri> one, Collection<? extends HasUri> two) {
        assert one != null;
        assert two != null;
        if (one.size() != two.size()) {
            return false;
        }
        boolean found;
        for (HasUri a : one) {
            found = false;
            for (HasUri b : two) {
                if (a.getUri().equals(b.getUri())) {
                    found = true;
                    break;
                }
            }
            if (!found) {
                return false;
            }
        }
        return true;
    }

    /**
     * Gets random element from the specified collection.
     *
     * @param col The collection
     * @param <T> Element type
     * @return Randomly selected element from the collection
     */
    public static <T> T randomElement(Collection<T> col) {
        final int index = Generator.randomInt(col.size() + 1) - 1;  // Some arithmetic to get the bounds right
        final Iterator<T> it = col.iterator();
        int i = 0;
        T item = null;
        while (it.hasNext() && i <= index) {
            item = it.next();
            i++;
        }
        return item;
    }

    /**
     * Loads JSON data from the specified file and maps them to the specified result type.
     *
     * @param fileName   Name of the file to load data from (including path). It is expected to be on the classpath and
     *                   Classloader is used to get input stream for the file.
     * @param resultType Java type to which the JSON should be mapped
     * @return Object loaded from the file and mapped to the result type
     * @throws Exception If file reading or object unmarshalling fails
     */
    public static <T> T loadData(String fileName, Class<T> resultType) throws Exception {
        try (final BufferedReader in = new BufferedReader(
                new InputStreamReader(Environment.class.getClassLoader().getResourceAsStream(fileName)))) {
            final StringBuilder builder = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null) {
                builder.append(line).append('\n');
            }
            if (String.class.isAssignableFrom(resultType)) {
                return resultType.cast(builder.toString());
            } else {
                return getObjectMapper().readValue(builder.toString(), resultType);
            }
        }
    }
}
