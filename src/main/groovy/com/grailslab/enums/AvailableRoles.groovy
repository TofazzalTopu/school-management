package com.grailslab.enums

enum AvailableRoles {
    SUPER_ADMIN("ROLE_SUPER_ADMIN"),
    SCHOOL_HEAD("ROLE_SCHOOL_HEAD"),
    ADMIN("ROLE_ADMIN"),
    TEACHER("ROLE_TEACHER"),
    STUDENT("ROLE_STUDENT"),
    PARENT("ROLE_PARENT"),
    ACCOUNTS("ROLE_ACCOUNTS"),
    HR("ROLE_HR"),
    LIBRARY("ROLE_LIBRARY"),
    APPLICANT("ROLE_APPLICANT"),
    SWITCH_USER("ROLE_SWITCH_USER"),
    ORGANIZER("ROLE_ORGANIZER"),
    SHIFT_INCHARGE("ROLE_SHIFT_INCHARGE")

    String roleId

    private AvailableRoles(String id) {
        this.roleId = id
    }

    String value() {
        roleId
    }
}