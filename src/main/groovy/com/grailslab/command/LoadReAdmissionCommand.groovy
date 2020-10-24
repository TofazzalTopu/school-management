package com.grailslab.command

import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.settings.ClassName

/**
 * Created by aminul on 3/19/2015.
 */
class LoadReAdmissionCommand implements grails.validation.Validateable{
    AcademicYear academicYear
    ClassName className
}
