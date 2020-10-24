package com.grailslab.admin

import com.grailslab.stmgmt.Registration

class AdminController {

    def index() {
        render (view: '/common/dashboard', layout:'adminLayout')
    }
}
