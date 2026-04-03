// Central role mapping file
package com.aditi_final.bridgeskill_api.security;

public final class RoleConstants {

    private RoleConstants() {
    }

    public static final Long ADMIN_ID = 1L;
    public static final Long STUDENT_ID = 2L;
    public static final Long CLIENT_ID = 3L;

    public static final String ADMIN = "ADMIN";
    public static final String STUDENT = "STUDENT";
    public static final String CLIENT = "CLIENT";

    public static String getRoleName(Long roleId) {
        if (roleId == null) {
            throw new IllegalArgumentException("Role ID cannot be null");
        }

        if (roleId.equals(ADMIN_ID)) return ADMIN;
        if (roleId.equals(STUDENT_ID)) return STUDENT;
        if (roleId.equals(CLIENT_ID)) return CLIENT;

        throw new IllegalArgumentException("Invalid role ID: " + roleId);
    }
}
