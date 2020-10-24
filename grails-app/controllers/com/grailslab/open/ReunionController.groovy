package com.grailslab.open

class ReunionController {

    def index() {
        render (view: '/open/reunion/index')
    }
    def members() {
        render (view: '/open/reunion/members')
    }
    def organizer() {
        render (view: '/open/reunion/organizer')
    }
    def faq() {
        render (view: '/open/reunion/reunionFaq')
    }
}
