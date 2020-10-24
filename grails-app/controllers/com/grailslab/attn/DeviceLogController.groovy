package com.grailslab.attn

class DeviceLogController {
    def deviceLogService

    def index() {
        switch (request.method) {
            case 'GET':
                /*String attnParams = "l1060,10002,538622815,1478364298gl1063,10004,538622815,1478364303gl1071,10002,538622815,1478364456gl1095,10002,538622815,1478364974gl1098,10004,538622815,1478364979gl1104,10001,538622815,1478365049gl1115,10001,538622815,1478365329gl1118,10001,538622815,1478365336gl1121,547165,538622815,1478365342gl1124,547165,538622815,1478365349gl1127,10002,538622815,1478365379gl1130,10004,538622815,1478365383gl1155,10002,538622815,1478365972gl1159,10004,538622815,1478365977gl"
                def attnList = attnParams.split("gl")
                def attnObjArray
                String userId
                String logRecordDay
                Map attnMap = new LinkedHashMap()
                Map recordAttnMap
                def userEntry
                def userAttn
                attnList.each {attnObj ->
                    attnObjArray = attnObj.split(",")
                    userId = attnObjArray[1]
                    logRecordDay = logRecordDate(attnObjArray[3])
                    if (attnMap.containsKey(logRecordDay)){
                        userEntry = attnMap.get(logRecordDay)
                        if(userEntry.containsKey(userId)){
                            userAttn = userEntry.get(userId)
                            userAttn.outTime = attnObjArray[3]
                        } else {
                            userEntry.put(userId, [empId:userId, inTime:attnObjArray[3], outTime:null])
                        }
                    } else {
                        recordAttnMap = new LinkedHashMap<>()
                        recordAttnMap.put(userId, [empId:userId, inTime:attnObjArray[3], outTime:null])
                        attnMap.put(logRecordDay, recordAttnMap)
                    }
                }
                Boolean entrySuccess = deviceLogService.logAttendance(attnMap)
                if (entrySuccess) {
                    render(status: 200)
                    return
                } else {
                    render(status: 403, text: 'Invalid or unauthorized Request')
                }*/
                render(status: 403, text: 'Invalid or unauthorized Request')
                break
            case 'POST':
                if (!params.userAttnData){
                    render(status: 403, text: 'Attendance Log file not found')
                    return
                }
                String attnParams = params.userAttnData
                def attnList = attnParams.split("gl")
                def attnObjArray
                String userId
                String logRecordDay
                Map attnMap = new LinkedHashMap()
                Map recordAttnMap
                def userEntry
                def userAttn
                attnList.each {attnObj ->
                    attnObjArray = attnObj.split(",")
                    userId = attnObjArray[1]
                    logRecordDay = logRecordDate(attnObjArray[3])
                    if (attnMap.containsKey(logRecordDay)){
                        userEntry = attnMap.get(logRecordDay)
                        if(userEntry.containsKey(userId)){
                            userAttn = userEntry.get(userId)
                            userAttn.outTime = attnObjArray[3]
                        } else {
                            userEntry.put(userId, [empId:userId, inTime:attnObjArray[3], outTime:null])
                        }
                    } else {
                        recordAttnMap = new LinkedHashMap<>()
                        recordAttnMap.put(userId, [empId:userId, inTime:attnObjArray[3], outTime:null])
                        attnMap.put(logRecordDay, recordAttnMap)
                    }
                }
                Boolean entrySuccess = deviceLogService.logAttendance(attnMap)
                if (entrySuccess) {
                    render(status: 200)
                    return
                } else {
                    render(status: 403, text: 'Invalid or unauthorized Request')
                }
                break
        }
    }
    private String logRecordDate(String logTime) {
        return new Date(Long.valueOf(logTime) * 1000).format("yyyyMMdd")
    }
}
