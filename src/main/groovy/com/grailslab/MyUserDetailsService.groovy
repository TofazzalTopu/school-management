package com.grailslab

import com.amaruddog.myschool.Role
import com.amaruddog.myschool.User
import com.amaruddog.myschool.UserRole
import grails.gorm.transactions.Transactional
import grails.plugin.springsecurity.userdetails.GrailsUserDetailsService
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UsernameNotFoundException

/**
 * Created by aminul on 1/25/2015.
 */
class MyUserDetailsService implements GrailsUserDetailsService {

    MyUserDetails loadUserByUsername(String username, boolean loadRoles)
            throws UsernameNotFoundException {
        return loadUserByUsername(username)
    }

    @Transactional(readOnly = true, noRollbackFor = [IllegalArgumentException, UsernameNotFoundException])
    MyUserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = User.findByUsername(username)
        if (!user) throw new UsernameNotFoundException("User not found" + username)

        def authorities = getAuthorities(user)?.collect {
            new SimpleGrantedAuthority(it.authority)
        }

        return new MyUserDetails(user.username, user.password, user.enabled,
                !user.accountExpired, !user.passwordExpired,
                !user.accountLocked, authorities, user.id,
                user.schoolId, user.userRef, user.name)
    }
    Set<Role> getAuthorities(User myUser) {
        return UserRole.findAllByUser(myUser, [readOnly: true])*.role
    }
}