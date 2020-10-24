package com.grailslab.open

class OrganizerController {

    def index() {
        render (view: '/common/dashboard', layout:'moduleWebLayout')
    }
}
