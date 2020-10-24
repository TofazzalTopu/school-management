package com.grailslab.salary

import com.grailslab.enums.YearMonths
import com.grailslab.gschoolcore.AcademicYear

class SalAdvanceDetail {
    SalAdvance salAdvance
    YearMonths yearMonths
    AcademicYear academicYear
    Date paymentDate
    Double paidAmount

    static constraints = {
    }
}
