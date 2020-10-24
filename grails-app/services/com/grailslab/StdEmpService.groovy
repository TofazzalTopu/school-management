package com.grailslab

import com.grailslab.viewz.LibraryMemberView
import grails.gorm.transactions.Transactional

@Transactional
class StdEmpService {

    def getStdEmpTypeAheadList(String sSearch) {
        List dataReturns = new ArrayList()
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        def c = LibraryMemberView.createCriteria()
        def results = c.list(max: 20, offset: 0) {
            if (sSearch) {
                or {
                    ilike('name', sSearch)
                    ilike('mobile', sSearch)
                    ilike('stdEmpNo', sSearch)
                }
            }
            order("objType", CommonUtils.SORT_ORDER_ASC)
            order("name", CommonUtils.SORT_ORDER_ASC)
        }
        for (obj in results) {
            dataReturns.add([id: obj.stdEmpNo, name: "${obj.stdEmpNo} - ${obj.name} - ${obj.mobile}"])
        }

        return dataReturns
    }

    def getLibraryMemberTypeAheadList(String sSearch) {
        List dataReturns = new ArrayList()
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        def c = LibraryMemberView.createCriteria()
        def results = c.list(max: 20, offset: 0) {
            if (sSearch) {
                or {
                    ilike('name', sSearch)
                    ilike('mobile', sSearch)
                    ilike('stdEmpNo', sSearch)
                }
            }
            order("objType", CommonUtils.SORT_ORDER_ASC)
            order("name", CommonUtils.SORT_ORDER_ASC)
        }
        for (obj in results) {
            dataReturns.add([id: obj.stdEmpNo, name: "${obj.stdEmpNo} - ${obj.name} - ${obj.allowedDays} - ${obj.mobile}", allowedDays: obj.allowedDays])
        }

        return dataReturns
    }
}
