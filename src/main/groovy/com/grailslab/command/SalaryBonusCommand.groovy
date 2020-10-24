package com.grailslab.command

import com.grailslab.enums.SalaryStatus
import com.grailslab.enums.YearMonths
import com.grailslab.gschoolcore.AcademicYear
import com.grailslab.hr.Employee

/**
 * Created by USER on 31-May-17.
 */
class SalaryBonusCommand implements grails.validation.Validateable{
    Long id
    String festivalName
    Date joinBefore
    AcademicYear academicYear
    String basedOn
    Double basicPercentage
    Double grossPercentage
    static constraints = {
        id nullable: true
    }
}
