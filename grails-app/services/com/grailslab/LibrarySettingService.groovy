package com.grailslab

import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.library.LibraryConfig
import grails.gorm.transactions.Transactional

@Transactional
class LibrarySettingService {

    def confList() {
        LibraryConfig.findAllByActiveStatus(ActiveStatus.ACTIVE)
    }
}
