package cz.cvut.kbss.reporting.security;

import cz.cvut.kbss.reporting.model.Vocabulary;

import java.util.Objects;

public class SecurityConstants {

    private SecurityConstants() {
        throw new AssertionError();
    }

    public static final String SESSION_COOKIE_NAME = "INBAS_JSESSIONID";

    public static final String REMEMBER_ME_COOKIE_NAME = "remember-me";

    public static final String CSRF_COOKIE_NAME = "CSRF-TOKEN";

    public static final String USERNAME_PARAM = "username";

    public static final String PASSWORD_PARAM = "password";

    public static final String SECURITY_CHECK_URI = "/j_spring_security_check";

    public static final String LOGOUT_URI = "/j_spring_security_logout";

    public static final String COOKIE_URI = "/";

    /**
     * Session timeout in seconds.
     */
    public static final int SESSION_TIMEOUT = 30 * 60;

    public static final String ROLE_GUEST = "ROLE_GUEST";

    public static final String ROLE_USER = "ROLE_USER";

    public static final String ROLE_ADMIN = "ROLE_ADMIN";

    /**
     * Represents user role mapping - from ontological types to system user roles.
     */
    public enum Role {
        GUEST(Vocabulary.s_c_guest, ROLE_GUEST),
        USER(Vocabulary.s_c_regular_user, ROLE_USER),
        ADMIN(Vocabulary.s_c_admin, ROLE_ADMIN);

        private final String type;
        private final String roleName;

        Role(String type, String roleName) {
            this.type = type;
            this.roleName = roleName;
        }

        public String getRoleName() {
            return roleName;
        }

        /**
         * Checks whether a role exists for the specified type.
         *
         * @param type The type to check
         * @return {@code true} if a matching role exists, {@code false} otherwise
         */
        public static boolean exists(String type) {
            Objects.requireNonNull(type);
            for (Role r : values()) {
                if (r.type.equals(type)) {
                    return true;
                }
            }
            return false;
        }

        /**
         * Gets role for the specified type.
         *
         * @param type The type to get role for
         * @return Matching role
         */
        public static Role getRole(String type) {
            Objects.requireNonNull(type);
            for (Role r : values()) {
                if (r.type.equals(type)) {
                    return r;
                }
            }
            throw new IllegalArgumentException("No role found for type " + type + ".");
        }
    }
}
