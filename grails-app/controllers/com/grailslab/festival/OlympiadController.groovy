package com.grailslab.festival

class OlympiadController {

    def messageSource


    def index() {
        render(view: '/festival/olympiad/index' )

    }

    /*def save(OlympaidListCommand command) {
        if (!request.method.equals('POST')) {
            flash.message="Request Method not allow here."
            redirect(action: 'index')
            return
        }
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            flash.message=errorList?.join('\n')
            redirect(action: 'index')
            return
        }

        FesRegistration  olympaidList
        if (command.id) {
            olympaidList = FesRegistration.get(command.id)
            if (!olympaidList) {
                flash.message=CommonUtils.COMMON_NOT_FOUND_MESSAGE
                redirect(action: 'index')
                return
            }
            olympaidList.properties = command.properties
            flash.message = 'Registration Updated Successfully.'
        } else {
            olympaidList = new FesProgram(command.properties)
            flash.message = 'Registration Added Successfully.'
        }


        redirect(action: 'index')
    }*/
}
