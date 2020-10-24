package com.grailslab

import com.grailslab.enums.NoticeCategory
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.settings.NoticeBoard
import grails.gorm.transactions.Transactional
import grails.web.servlet.mvc.GrailsParameterMap
import org.springframework.dao.DataIntegrityViolationException

@Transactional
class NoticeBoardService {
    def grailsApplication
    def springSecurityService

    static final String[] sortColumns = ['title','details','scrollText']

    LinkedHashMap noticeBoardPaginateList(GrailsParameterMap params) {
        int iDisplayStart = params.iDisplayStart ? params.getInt('iDisplayStart') : CommonUtils.DEFAULT_PAGINATION_START
        int iDisplayLength = params.iDisplayLength ? params.getInt('iDisplayLength') : CommonUtils.DEFAULT_PAGINATION_LENGTH
        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : CommonUtils.DEFAULT_PAGINATION_SORT_ORDER
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : CommonUtils.DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns, iSortingCol)
        List dataReturns = new ArrayList()
        def c = NoticeBoard.createCriteria()
        def results = c.list(max: iDisplayLength, offset: iDisplayStart) {
            and {
            }
            if (sSearch) {
                or {
                    ilike('title', sSearch)
                    ilike('details', sSearch)
                    ilike('scrollText', sSearch)
                }
            }
            order(sortColumn, sSortDir)
        }
        int totalCount = results.totalCount
        int serial = iDisplayStart;
        String title
        String scroll
        String isScroll
        String isBoard
        String publishDate
        String expireDate
        if (totalCount > 0) {
            if (sSortDir.equals(CommonUtils.SORT_ORDER_DESC)) {
                serial = (totalCount + 1) - iDisplayStart
            }
            results.each { NoticeBoard noticeBoard ->
                if (sSortDir.equals(CommonUtils.SORT_ORDER_ASC)) {
                    serial++
                } else {
                    serial--
                }
                title = noticeBoard.title ? noticeBoard.title.substring(0,noticeBoard.title.length()>100 ? 100: noticeBoard.title.length()):''
                scroll = noticeBoard.scrollText?noticeBoard.scrollText.substring(0,noticeBoard.scrollText.length()>100 ?100:noticeBoard.scrollText.length()):''
                isBoard = noticeBoard.keepBoard.equals(Boolean.TRUE) ? 'Published':'Not Published'
                isScroll = noticeBoard.keepScroll.equals(Boolean.TRUE) ? 'Published':'Not Published'
                publishDate=noticeBoard?.publish? CommonUtils.getUiDateStr(noticeBoard.publish):''
                expireDate=noticeBoard?.expire? CommonUtils.getUiDateStr(noticeBoard.expire):''
                dataReturns.add([DT_RowId: noticeBoard.id, 0: title, 1: scroll, 2: isBoard,3:isScroll,4: publishDate,5:expireDate,6:''])
            }
        }
        return [totalCount: totalCount, results: dataReturns]
    }

    def noticeBoard() {
        List dataReturns = new ArrayList()
        def c = NoticeBoard.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                eq("keepBoard", Boolean.TRUE)
            }
            order("id", CommonUtils.SORT_ORDER_DESC)
        }
        int scrollTextLength = 80
        String publishDate
        String expireDate
        String shortText
        results.each { NoticeBoard board ->
            publishDate=board?.publish? CommonUtils.getUiDateStr(board.publish):''
            expireDate=board?.expire? CommonUtils.getUiDateStr(board.expire):''
            if(board.scrollText.length() < scrollTextLength) {
                shortText = board.scrollText.toString()
            } else {
                shortText = board.scrollText.toString().substring(0, scrollTextLength)
            }

            dataReturns.add([
                    id: board.id,
                    title: board.title,
                    publishDate:publishDate,
                    expireDate:expireDate,
                    body:board.details,
                    fileLink: board.fileLink,
                    scrollText: shortText + '...'
            ])
        }
        return dataReturns
    }

    def noticeBoardList(NoticeCategory noticeCategory = null) {
        def g = grailsApplication.mainContext.getBean('org.grails.plugins.web.taglib.ApplicationTagLib')
        List dataReturns = new ArrayList()
        def c = NoticeBoard.createCriteria()
        def results = c.list() {
            and {
                eq("activeStatus", ActiveStatus.ACTIVE)
                if(noticeCategory) {
                    eq("category", noticeCategory.key)
                }
                eq("keepBoard", Boolean.TRUE)
            }
            order("id", CommonUtils.SORT_ORDER_DESC)
        }
        int scrollTextLength = 80
        String publishDate
        String expireDate
        String shortText
        results.each { NoticeBoard board ->
            publishDate=board.publish? CommonUtils.getUiDateStr(board.publish):''
            expireDate=board.expire? CommonUtils.getUiDateStr(board.expire):''
            if(board.scrollText.length() < scrollTextLength) {
                shortText = board.scrollText.toString()
            } else {
                shortText = board.scrollText.toString().substring(0, scrollTextLength)
            }

            dataReturns.add([
                    id: board.id,
                    title: board.title,
                    publishDate:publishDate,
                    expireDate:expireDate,
                    body:board.details,
                    fileLink: board.fileLink,
                    scrollText: shortText + '...',
                    link: board.fileLink? "${g.createLink(controller:'calendar', action: 'downloadFile', params: [identifier: board.fileLink])}" : '#'
            ])
        }
        return dataReturns
    }

    def inactive(Long id) {
        String createOrUpdateBy = springSecurityService.principal.username
        LinkedHashMap result = new LinkedHashMap()
        NoticeBoard noticeBoard = NoticeBoard.get(id)
        if (!noticeBoard) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
            return result
        }
        noticeBoard.activeStatus = ActiveStatus.INACTIVE
        noticeBoard.updatedBy = createOrUpdateBy
        noticeBoard.save(flush: true)
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put(CommonUtils.MESSAGE, 'Inactivated Successfully')
        return result
    }

    def delete(Long id) {
        LinkedHashMap result = new LinkedHashMap()
        String createOrUpdateBy = springSecurityService.principal.username
        NoticeBoard noticeBoard = NoticeBoard.get(id)
        if (!noticeBoard) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, CommonUtils.COMMON_NOT_FOUND_MESSAGE)
        }
        try {
            noticeBoard.updatedBy = createOrUpdateBy
            noticeBoard.delete(flush: true)
            result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
            result.put(CommonUtils.MESSAGE, "Data Deleted Successfully.")

        }
        catch (DataIntegrityViolationException e) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "Data already in use. You Can Inactive Reference")
        }
        return result
    }
}