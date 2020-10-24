package com.grailslab.salary

import com.grailslab.enums.YearMonths
import com.grailslab.gschoolcore.AcademicYear

class SalPcDetail {
    SalPc salPc
    Date paymentDate
    Double paidAmount
    YearMonths yearMonths
    AcademicYear academicYear

    static constraints = {
    }
}
