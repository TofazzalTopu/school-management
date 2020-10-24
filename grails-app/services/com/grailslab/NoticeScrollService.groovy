package com.grailslab

import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.NoticeBoard
import grails.gorm.transactions.Transactional

@Transactional
class NoticeScrollService {
    def noticeBoardService
    def grailsApplication

    String scrollText() {
        Date toDay = new Date().clearTime()
        def c = NoticeBoard.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("keepScroll", Boolean.TRUE)
                le("publish", toDay)
                ge("expire", toDay)
            }
            order("id", CommonUtils.SORT_ORDER_DESC)
        }
        String scrollText = ""
        results.each {NoticeBoard notice ->
            scrollText+="<span style='color: ${notice.scrollColor};'>${notice.scrollText}</span>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;"
        }
        return scrollText
    }

    String scrollTextNew(Map attrs) {
        def g = grailsApplication.mainContext.getBean('org.grails.plugins.web.taglib.ApplicationTagLib')
        String scrollAmount = attrs.remove("scrollamount")
        String direction = attrs.remove("direction")
        Date toDay = new Date().clearTime()
        def c = NoticeBoard.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("keepScroll", Boolean.TRUE)
                le("publish", toDay)
                ge("expire", toDay)
                order("publish", "desc")
            }
            order("id", CommonUtils.SORT_ORDER_DESC)
        }
        String scrollText = "<marquee "
        if(!direction) {
            direction = 'left'
        }
        scrollText +="direction=\"${direction}\" "
        if(scrollAmount) {
            scrollText +="onmouseover=\"this.stop();\"  onmouseout=\"this.start();\" scrollamount=\"6\">"
        } else {
            scrollText +="onmouseover=\"this.stop();\"  onmouseout=\"this.start();\" scrollamount=\"6\">"
        }
        results.each {NoticeBoard notice ->
            if(notice.fileLink) {
                scrollText+="<li class=\"new-target-link\"><a href=\"${g.createLink(controller:'calendar', action: 'downloadFile', params: [identifier: notice.fileLink])}\">${notice.scrollText}</a></li>"
            } else {
                scrollText+="<li><a href=\"#\">${notice.scrollText}</a></li>"
            }
        }
        scrollText += "</marquee>"
        return scrollText
    }

    String scrollTextSideNotice(Map attrs) {
        def g = grailsApplication.mainContext.getBean('org.grails.plugins.web.taglib.ApplicationTagLib')
        String scrollAmount = attrs.remove("scrollamount")
        String direction = attrs.remove("direction")
        String classAttr = attrs.remove("class")
        String behavior = attrs.remove("behavior")
        String ulClass = attrs.remove("ulClass")
        String ulStyle = attrs.remove("style")
        Date toDay = new Date().clearTime()
        def noticeList = noticeBoardService.noticeBoard()
        String scrollText = "<marquee "
        if(!direction) {
            direction = 'left'
        }
        scrollText +="direction=\"${direction}\" "

        if(scrollAmount) {
            scrollText +="onmouseover=\"this.stop();\"  onmouseout=\"this.start();\" scrollamount=\"${scrollAmount}\""
        } else {
            scrollText +="onmouseover=\"this.stop();\"  onmouseout=\"this.start();\" scrollamount=\"6\""
        }
        if(ulStyle) {
            scrollText +=" style='${ulStyle}'"
        }
        if(classAttr) {
            scrollText +=" class='${classAttr}'"
        }
        if(behavior) {
            scrollText +=" behavior='${behavior}'"
        }
        scrollText += "><ul class=\"${ulClass}\">"
        for(def notice: noticeList) {
            if(notice.fileLink) {
                scrollText+="<li class=\"new-target-link\"><a href=\"${g.createLink(controller:'calendar', action: 'downloadFile', params: [identifier: notice.fileLink])}\">${notice.scrollText}</a></li>"
            } else {
                scrollText+="<li><a href=\"#\">${notice.scrollText}</a></li>"
            }
        }
        scrollText += "</ul></marquee>"
        return scrollText
    }
}
