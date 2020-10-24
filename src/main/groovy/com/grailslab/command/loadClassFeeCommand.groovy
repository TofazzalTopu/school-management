package com.grailslab.command

import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.settings.ClassName

/**
 * Created by Hasnat on 6/27/2015.
 */
class loadClassFeeCommand implements grails.validation.Validateable{
    AcademicYear academicYear
    ClassName className
    Boolean monthlyFees
    Boolean allFeeDue
    static constraints = {
        academicYear nullable: true
        monthlyFees nullable: true
        allFeeDue nullable: true
    }
}
