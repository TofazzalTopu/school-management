package com.grailslab.open

class BlogController {

    def index() {
        render (view: '/open/blog/blog')
    }
    def story(Long id){
        render(view: '/open/blog/story')
    }
}
