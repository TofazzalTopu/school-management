package com.grailslab

class ScrollNoticeTagLib {
    static namespace = "notice"
    def noticeScrollService
    Closure scrollText = { attrs, body ->
        String scrollStr= noticeScrollService.scrollText()
        if(!scrollStr){
            scrollStr="<span style='color: #cccccc;'>No recent notice available</span>"
        }
        out << scrollStr
    }
    Closure scrollTextNew = { attrs, body ->
        String scrollStr= noticeScrollService.scrollTextNew(attrs)
        if(!scrollStr){
            scrollStr="<marquee direction=\"left\" onmouseover=\"this.stop();\"  " +
                    "onmouseout=\"this.start();\" scrollamount=\"6\">" +
                    "<a href=\"/\">No recent notice available</a>"
        }
        out << scrollStr
    }
    Closure scrollTextSideNotice = { attrs, body ->
        String scrollStr= noticeScrollService.scrollTextSideNotice(attrs)
        if(!scrollStr){
            scrollStr="<marquee direction=\"left\" onmouseover=\"this.stop();\"  " +
                    "onmouseout=\"this.start();\" scrollamount=\"6\">" +
                    "<a href=\"/\">No recent notice available</a>"
        }
        out << scrollStr
    }
}
