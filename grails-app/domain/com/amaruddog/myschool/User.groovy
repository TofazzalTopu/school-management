package com.amaruddog.myschool

import com.grailslab.enums.MainUserType
import groovy.transform.EqualsAndHashCode
import groovy.transform.ToString
import grails.compiler.GrailsCompileStatic

@GrailsCompileStatic
@EqualsAndHashCode(includes='username')
@ToString(includes='username', includeNames=true, includePackage=false)
class User implements Serializable {

    private static final long serialVersionUID = 1

    String username
    String password
    boolean enabled = true
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired

    Long schoolId
    String userRef
    String name
    MainUserType mainUserType
    Date lastLogin

    Set<Role> getAuthorities() {
        (UserRole.findAllByUser(this) as List<UserRole>)*.role as Set<Role>
    }

    static constraints = {
        password nullable: false, blank: false, password: true
        username nullable: false, blank: false, unique: true
        userRef nullable: true
        name nullable: true
        mainUserType nullable: true
        lastLogin nullable: true
    }

    static mapping = {
	    password column: '`password`'
    }
}
