package com.grailslab

import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.HrStaffCategory
import grails.gorm.transactions.Transactional

@Transactional
class HrResourceService {

    def getStaffCategories() {
        def result = HrStaffCategory.findAllByActiveStatus(ActiveStatus.ACTIVE,[sort:'sortOrder'])
        return result
    }
}
