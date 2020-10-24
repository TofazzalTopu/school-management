package com.grailslab.hr

class HrController {

    def index() {
        render (view: '/common/dashboard', layout:'moduleHRLayout')
    }

}
