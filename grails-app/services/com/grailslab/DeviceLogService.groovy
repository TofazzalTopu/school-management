package com.grailslab

import com.grailslab.attn.AttnEmpRecord
import com.grailslab.attn.AttnRecordDay
import com.grailslab.attn.AttnStdRecord
import com.grailslab.hr.HrPeriod
import com.grailslab.viewz.AttnCardNoView
import grails.gorm.transactions.Transactional


@Transactional
class DeviceLogService {
    def recordDayService
    def hrPeriodService
    public static final String STD_TYPE = "student"
    public static final String EMP_TYPE = "employee"

    Boolean logAttendance(Map attnMap) {
        AttnCardNoView cardNoView
        AttnRecordDay mRecord
        Date recordDate
        AttnStdRecord stdRecord
        AttnEmpRecord empRecord
        Date inDateTime
        Date outDateTime
        HrPeriod hrPeriod
        Boolean isLateEntry
        Boolean isEarlyLeave
        try {
            attnMap.each {k,v ->
                recordDate = Date.parse("yyyyMMdd", k)
                mRecord = recordDayService.recordDayForDevice(recordDate)
                v.each {key, value ->
                    cardNoView = AttnCardNoView.findByCardNo(value.empId)
                    if (cardNoView) {
                        inDateTime = CommonUtils.getDeviceLogDate(value.inTime)
                        if (value.outtime) {
                            outDateTime = CommonUtils.getDeviceLogDate(value.outTime)
                        } else {
                            outDateTime = null
                        }
                        hrPeriod = HrPeriod.read(cardNoView.hrPeriodId)
                        if (cardNoView.objType == STD_TYPE) {
                            stdRecord = AttnStdRecord.findByRecordDayAndStudentId(mRecord, cardNoView.objId)
                            if (stdRecord) {
                                if (outDateTime && stdRecord.inTime.before(outDateTime)) {
                                    stdRecord.outTime = outDateTime
                                }else {
                                    if (stdRecord.inTime.before(inDateTime)) {
                                        stdRecord.outTime = inDateTime
                                    }
                                }
                            } else {
                                stdRecord = new AttnStdRecord(recordDay: mRecord, recordDate: mRecord.recordDate, studentId: cardNoView.objId, sectionId: cardNoView.sectionId, stdNo: cardNoView.stdEmpNo, inTime:inDateTime, outTime: outDateTime)
                                stdRecord.isLateEntry = hrPeriodService.isLateEntry(inDateTime, hrPeriod?.startTime, hrPeriod?.lateTolerance)
                            }
                            stdRecord.save(flush: true)
                        } else if (cardNoView.objType == EMP_TYPE) {
                            empRecord = AttnEmpRecord.findByRecordDayAndEmployeeId(mRecord, cardNoView.objId)
                            if (empRecord) {
                                if (outDateTime && empRecord.inTime.before(outDateTime)) {
                                    empRecord.outTime = outDateTime
                                }else {
                                    if (empRecord.inTime.before(inDateTime)) {
                                        empRecord.outTime = inDateTime
                                    }
                                }
                            } else {
                                empRecord = new AttnEmpRecord(recordDay: mRecord, recordDate: mRecord.recordDate, employeeId: cardNoView.objId, empNo: cardNoView.stdEmpNo, inTime:inDateTime, outTime: outDateTime)
                                empRecord.isLateEntry = hrPeriodService.isLateEntry(inDateTime, hrPeriod?.startTime, hrPeriod?.lateTolerance)
                            }
                            empRecord.save(flush: true)
                        }
                    }
                }
            }
            return true
        } catch (Exception ex) {
            log.debug("ATTENDANCE: entry: "+attnMap+"; Reason: "+ex.toString())
        }
        return false
    }

//    http://stackoverflow.com/questions/7676149/compare-only-the-time-portion-of-two-dates-ignoring-the-date-part
}
