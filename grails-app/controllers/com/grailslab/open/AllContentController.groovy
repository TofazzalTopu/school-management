package com.grailslab.open

import com.grailslab.enums.OpenCont
import com.grailslab.gschoolcore.ActiveStatus
import org.springframework.dao.DataIntegrityViolationException

class AllContentController {

    def springSecurityService
    def messageSource

    def faq() {
        String messages=params.messages
        def faqList = FaqCategory.findAllByOpenCont(OpenCont.HOME_FAQ, [sort: "sortPosition"])
        render(view: '/open/admin/faq', model: [faqList: faqList,messages:messages])
    }

    def saveFaqPage(Long id) {
        if (id) {
            FaqCategory category = FaqCategory.get(id)
            render(view: '/open/admin/saveUpdateContent', model: [category: category])
            return
        }
        render(view: '/open/admin/saveUpdateContent')
    }

    def saveFaqQusPage(Long id) {
        if (id) {
            FaqQuestion faqQuestion = FaqQuestion.get(id)
            render(view: '/open/admin/saveUpQsContent', model: [faqQuestion: faqQuestion])
            return
        }
        render(view: '/open/admin/saveUpQsContent')
    }

    def saveFaqContent(FaqCategoryCommand command) {
        String messages
        if (!request.method.equals('POST')) {
            messages = 'Request method not alowed.'
            redirect(controller: params.prevController, action: params.prevAction, params: [messages: messages])
            return
        }

        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            messages = errorList?.join('\n')
            redirect(controller: params.prevController, action: params.prevAction, params: [messages: messages])
            return
        }
        String createOrUpdateBy = springSecurityService.principal?.username
        FaqCategory category
        if (command.id) {
            category = FaqCategory.get(command.id)
            if (!category) {
                messages = 'Content Not Found.'
                redirect(controller: params.prevController, action: params.prevAction, params: [messages: messages])
                return
            }
            category.name = command.name
            category.updatedBy = createOrUpdateBy
            category.sortPosition = command.sortPosition
            messages = 'Content Updated Successfully.'
        } else {
            FaqCategory categoryTest = FaqCategory.findByName(command.name)
            if (categoryTest) {
                messages = 'Content Title Already Exist Pleas Try Another.'
                redirect(controller: params.prevController, action: params.prevAction, params: [messages: messages])
                return
            }
            category = new FaqCategory()
            category.properties = command.properties
            category.createdBy = createOrUpdateBy
            messages = 'Content Inserted Successfully.'
        }
        if (category.hasErrors() || !category.save(flush: true)) {
            def errorList = category?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            messages = errorList?.join('\n')
            redirect(controller: params.prevController, action: params.prevAction, params: [messages: messages])
            return
        }
        redirect(controller: params.prevController, action: params.prevAction, params: [messages: messages])
    }

    def saveFaqQs(FaqQuestionCommand command) {
        String messages
        if (!request.method.equals('POST')) {
            messages = 'Request method not alowed.'
            redirect(controller: params.prevController, action: params.prevAction, params: [messages: messages])
            return
        }

        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            messages = errorList?.join('\n')
            redirect(controller: params.prevController, action: params.prevAction, params: [messages: messages])
            return
        }

        FaqQuestion question
        FaqCategory faqCategory
        if (command.id) {
            question = FaqQuestion.get(command.id)
            if (!question) {
                messages = 'Content Not Found.'
                redirect(controller: params.prevController, action: params.prevAction, params: [messages: messages])
                return
            }
            question.name = command.name
            question.updatedBy = springSecurityService.principal?.username
            question.answers = command.answers
            question.sortPosition = command.sortPosition
            messages = 'Content Updated Successfully.'
            if (question.hasErrors() || !question.save(flush: true)) {
                def errorList = question?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
                messages = errorList?.join('\n')
                redirect(controller: params.prevController, action: params.prevAction, params: [messages: messages])
                return
            }
        } else {
            FaqQuestion questions = FaqQuestion.findByName(command.name)
            faqCategory = FaqCategory.get(Long.parseLong(params.catId))

            if (questions) {
                messages = 'Content Title Already Exist Pleas Try Another.'
                redirect(controller: params.prevController, action: params.prevAction, params: [messages: messages])
                return
            }

            if (!faqCategory) {
                messages = 'Category not found pls try again.'
                redirect(controller: params.prevController, action: params.prevAction, params: [messages: messages])
                return
            }
            question = new FaqQuestion()
            question.createdBy = springSecurityService.principal?.username
            question.properties = command.properties
            faqCategory.addToFaqQuestion(question)
            messages = 'Content Inserted Successfully.'
            if (question.hasErrors() || !question.save(flush: true) || !faqCategory.save(flush: true)) {
                def errorList = question?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
                messages = errorList?.join('\n')
                redirect(controller: params.prevController, action: params.prevAction, params: [messages: messages])
                return
            }
        }
        redirect(controller: params.prevController, action: params.prevAction, params: [messages: messages])
    }

    def inactiveFaq(Long id) {
        FaqCategory allContent
        String messages
        if (!id) {
            messages = 'Content not found pleas try again.'
            redirect(controller: params.prevController,action: params.prevAction, params: [messages: messages])
            return
        }
        allContent = FaqCategory.get(id)

        if (!allContent) {
            messages = 'Content not found pleas try again.'
            redirect(controller: params.prevController,action: params.prevAction, params: [messages: messages])
            return
        }

        if (allContent.activeStatus.key.equals(ActiveStatus.ACTIVE.key)) {
            allContent.activeStatus = ActiveStatus.INACTIVE.key
            messages = 'Inactivated Successfully.'
        } else {
            allContent.activeStatus = ActiveStatus.ACTIVE.key
            messages = 'Activated Successfully.'
        }
        allContent.updatedBy = springSecurityService.principal?.username
        if (allContent.hasErrors() || !allContent.save(flush: true)) {
            def errorList = allContent?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            messages = errorList?.join('\n')
            redirect(controller: params.prevController,action: params.prevAction, params: [messages: messages])
            return
        }
        redirect(controller: params.prevController,action: params.prevAction, params: [messages: messages])
    }

    def deleteQus(Long id) {
        FaqQuestion question
        String messages
        if (!id) {
            messages = 'Content not found pleas try again.'
            redirect(action: 'index', params: [messages: messages])
            return
        }
        question = FaqQuestion.get(id)

        if (!question) {
            messages = 'Content not found pleas try again.'
            redirect(action: 'index', params: [messages: messages])
            return
        }
        FaqCategory category=question.faqCategory
        if (!category) {
            messages = 'Content not found pleas try again.'
            redirect(action: 'index', params: [messages: messages])
            return
        }
        category.removeFromFaqQuestion(question)
        category.save(flush: true)
        try {
            question.delete(flush: true)
            messages = 'Content delete successfully.'
        }
        catch (DataIntegrityViolationException e) {
            messages = 'Content delete failed pleas try again.'
            redirect(action: 'index', params: [messages: messages])
            return
        }
        redirect(action: 'index', params: [messages: messages])
        return
    }

    def deleteFaq(Long id) {
        FaqCategory category
        String messages
        if (!id) {
            messages = 'Content not found pleas try again.'
            redirect(controller: params.prevController, action: params.prevAction, params: [messages: messages])
            return
        }
        category = FaqCategory.get(id)

        if (!category) {
            messages = 'Content not found pleas try again.'
            redirect(controller: params.prevController, action: params.prevAction, params: [messages: messages])
            return
        }
        try {
            category.delete(flush: true)
            messages = 'Content delete successfully.'
        }
        catch (DataIntegrityViolationException e) {
            messages = 'Content delete failed pleas try again.'
            redirect(controller: params.prevController, action: params.prevAction, params: [messages: messages])
            return
        }
        redirect(controller: params.prevController, action: params.prevAction, params: [messages: messages])
        return
    }

}


class FaqCategoryCommand {
    Long id
    String name
    OpenCont openCont
    Integer sortPosition
    String prevController
    String prevAction
    static constraints = {
        id nullable: true
        openCont nullable: true
        prevAction nullable: true
        prevController nullable: true
    }
}


class FaqQuestionCommand {
    Long id
    String name
    String answers
    Integer sortPosition=1
    static constraints = {
        id nullable: true
    }
}

