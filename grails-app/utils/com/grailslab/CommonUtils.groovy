package com.grailslab

import com.grailslab.enums.YearMonths
import grails.web.servlet.mvc.GrailsParameterMap
import groovy.time.TimeCategory
import org.joda.time.DateTime
import org.joda.time.LocalTime
import org.joda.time.format.DateTimeFormat
import org.joda.time.format.DateTimeFormatter

import java.text.DateFormat
import java.text.ParseException
import java.text.SimpleDateFormat

/**
 * Created by aminul on 1/25/2015.
 */
class CommonUtils {

    def springSecurityService

    public static final String MODULE_LIBRARY = 'library'
    public static final String MODULE_CT = 'cttabulation'
    public static final String MODULE_FEEDBACK = 'feedback'
    public static final String MODULE_SALARY = 'salary'
    public static final String MODULE_ONLINE = 'online'
    public static final String MODULE_ATTENDANCE = 'attn'
    public static final String MODULE_ACCOUNTS = 'accounts'
    public static final String MODULE_ACCOUNTING = 'accounting'
    public static final String MODULE_LEAVE = 'leave'
    //running school list
    public static final String BAILY_SCHOOL ="bailyschool"
    public static final String NARAYANGANJ_IDEAL_SCHOOL ="nideal"
    public static final String NARAYANGANJ_HIGH_SCHOOL ="nhs"
    public static final String NARAYANGANJ_ADARSHA_SCHOOL ="adarshaschool"
    public static final String NARAYANGANJ_BIDDA_NIKETON_SCHOOL ="biddaniketon"
    public static final String LIGHT_HOUSE_SCHOOL ="lighthouse"


    public static final String IS_ERROR ="isError"
    public static final String MESSAGE ="message"
    public static final String OBJ ="obj"
    public static final String COMMON_ERROR_MESSAGE ="Something wrong. Please try later or contact admin"
    public static final String COMMON_NOT_FOUND_MESSAGE ="Object not found. Please refresh browser and try again"
    public static final String COMMON_DELETE_SUCCESS_MESSAGE ="Data deleted Successfully."
    public static final String COMMON_DELETE_FAILED_MESSAGE ="Data delete failed."
    public static final String NOT_APPLICABLE ="N/A"

    //for calender and events
    public static final String CALENDER_EVENT_MONTH_VIEW_FORMAT="yyyy-MM-dd"
    public static final String CALENDER_EVENT_LIST_INPUT_FORMAT="yyyy-MM-dd'T'HH:mm:ss"
    public static final String CALENDER_TIME_FORMAT="yyyy-MM-dd'T'HH:mm:ss"
    public static final String UI_TIME_FORMAT_12HR ='hh:mm:ss a'

    public static final String UI_DATE_INPUT="dd/MM/yyyy"
    public static final String UI_TIME_INPUT="h:mm a"
    public static final String MYSQL_QUERY_FORMAT="yyyy-MM-dd"

    public static final String DAY_FORMAT ='EEEE'
    public static final String MONTH_FORMAT ='MMMMM'
    public static final String YEAR_FORMAT ='yyyy'

    //device attandance
    public static final String DEVICE_DATE_TIME_FORMAT="yyyyMMddHHmmss"
    public static final String DEVICE_FILTER_DATE_FORMAT ='yyyyMMdd'
    public static final String DEVICE_TIME_FORMAT ='HH:mm:ss'

    public static final int DEFAULT_PAGINATION_START =0
    public static final int DEFAULT_PAGINATION_LENGTH =25
    public static final int MAX_PAGINATION_LENGTH =100
    public static final String DEFAULT_PAGINATION_SORT_ORDER ='desc'
    public static final String DEFAULT_PAGINATION_SORT_COLUMN ='id'
    public static final int DEFAULT_PAGINATION_SORT_IDX =0
    public static final String PERCENTAGE_SIGN ='%'
    public static final Long DEFAULT_SCHOOL_ID =10000

    public static final String SORT_ORDER_ASC ='asc'
    public static final String SORT_ORDER_DESC ='desc'
    public static final String UI_DATE_FORMAT ='dd-MMM-yyyy'
    public static final String UI_DATE_WITH_TIME_FORMAT ='dd-MMM-yyyy hh:mm:ss a'
    public static final String UI_DATE_FORMAT_WITH_DAYS ='EEEE-dd-MMM-yyyy'
    public static final String UI_DATE_HOLIDAY_FORMAT ='EEE'
    public static final String UI_DATE_FORMAT_FOR_PEAKER ='dd/MM/yyyy'
    public static final String DATE_FORMAT= "MMMM dd, yyyy"
    public static final String REPORT_LOGO = File.separator+"images"+File.separator+"logo.jpg";
    public static final String REPORT_DIRECTORY = File.separator+"reports";

    public static final String PDF_EXTENSION = ".pdf"
    public static final String XLSX_EXTENSION = ".xlsx"
    public static final String XLS_EXTENSION = ".xls"
    public static final String DOCX_EXTENSION = ".docx"
    public static final String TEXT_EXTENSION = ".txt"
    public static final String REPORT_FILE_FORMAT_PDF = 'pdf'
    public static final String REPORT_FILE_FORMAT_XLSX = 'xlsx'
    public static final String REPORT_FILE_FORMAT_XLS = 'xls'
    public static final String REPORT_FILE_FORMAT_DOCX = 'docx'
    public static final String REPORT_FILE_FORMAT_TEXT = 'text'
    public static final String LEAVE_APPLICATION_JASPER_FILE = 'text'

    public static String getSortColumn(String [] sortColumns, int idx){
        if(!sortColumns || sortColumns.length<1)
            return DEFAULT_PAGINATION_SORT_COLUMN
        int columnCounts = sortColumns.length
        if(idx>0 && idx<columnCounts){
            return sortColumns[idx]
        }
        return sortColumns[DEFAULT_PAGINATION_SORT_IDX]
    }
    public static String getUiDateStr(Date oldDate){
        if(!oldDate) return ''
        return oldDate.format(UI_DATE_FORMAT)
    }

    public static String getHolidayStr(Date oldDate){
        if(!oldDate) return ''
        return oldDate.format(UI_DATE_HOLIDAY_FORMAT)
    }

    public static String getDateRangeStr(Date fromDate, Date toDate){
        if(!fromDate) return ''
        if (toDate) {
            return fromDate.format(UI_DATE_FORMAT)+" - "+toDate.format(UI_DATE_FORMAT)
        } else {
            return fromDate.format(UI_DATE_FORMAT)
        }
    }
    public static String getDateRange(Date fromDate, Date toDate){
        if(!fromDate) return ''
        if (toDate) {
            return fromDate.format(UI_DATE_FORMAT)+" to "+toDate.format(UI_DATE_FORMAT)
        } else {
            return fromDate.format(UI_DATE_FORMAT)
        }
    }

    public static String getUiDateTimeStr(Date oldDate){
        if(!oldDate) return ''
        use( TimeCategory ) {
            oldDate = oldDate + 6.hours
        }
        return oldDate.format(UI_DATE_WITH_TIME_FORMAT)
    }

    public static Date getDeviceLogDate(String logTime){
        if(!logTime) return ''
        return new Date(Long.valueOf(logTime) * 1000)
    }


    public static String getUiDateStrWithDays(Date oldDate){
        if(!oldDate) return ''
        return oldDate.format(UI_DATE_FORMAT_WITH_DAYS)
    }

    public static String getUiDateStrForPicker(Date oldDate){
        if(!oldDate) return ''
        return oldDate.format(UI_DATE_FORMAT_FOR_PEAKER)
    }

    public static Date getUiPickerDate(String pickerDate, Boolean startOfDay = true){
        if(!pickerDate) return null
        DateTimeFormatter formatter = DateTimeFormat.forPattern(UI_DATE_FORMAT_FOR_PEAKER);
        DateTime startDateTime
        Date returnDate
        try{
            startDateTime = formatter.parseDateTime(pickerDate)
            if(startOfDay){
                returnDate = startDateTime.withTimeAtStartOfDay().toDate()
            }else {
                returnDate = startDateTime.plusDays(1).withTimeAtStartOfDay().toDate()
            }
        }catch (Exception ex){
            returnDate = null
        }
        return returnDate
    }

    public static Date getStringToDate(String pickerDate){
        if(!pickerDate) return null
        DateFormat formatter = new SimpleDateFormat(UI_DATE_FORMAT_FOR_PEAKER);
        Date returnDate
        try{
            returnDate = formatter.parse(pickerDate)
        }catch (Exception ex){
            returnDate = null
        }
        return returnDate
    }


    //device attendance
    public static Date deviceLogDateStrToDate(String dateStr, String timeStr){
        String dateTimeStr = dateStr+timeStr
        DateTimeFormatter formatter = DateTimeFormat.forPattern(DEVICE_DATE_TIME_FORMAT);
        DateTime startDateTime
        Date returnDate = null
        try{
            startDateTime = formatter.parseDateTime(dateTimeStr)
            returnDate= startDateTime.toDate()
        }catch (Exception ex){
            returnDate = null
        }
        return returnDate
    }

    //manual attendance
    public static Date manualAttnDateStrToDate(Date recordDate, String timeStr){
        if(!recordDate || !timeStr) return null
        DateTimeFormatter timeFormatter = DateTimeFormat.forPattern(UI_TIME_INPUT);
        LocalTime startTime =timeFormatter.parseLocalTime(timeStr)
        DateTime dateTime = new DateTime(recordDate).withTime(startTime.hourOfDay, startTime.minuteOfHour, 0, 0);
        return dateTime.toDate()
    }

    public static String getDeviceDateStr(Date oldDate){
        if(!oldDate) return ''
        return oldDate.format(DEVICE_FILTER_DATE_FORMAT)
    }
    public static String getMonthName(Date oldDate){
        if(!oldDate) return YearMonths.JANUARY.value
        return oldDate.format(MONTH_FORMAT)
    }



   /* public static String getDeviceTimeStr(Date oldDate){
        if(!oldDate) return ''
        use( TimeCategory ) {
            oldDate = oldDate + 6.hours
        }
        return oldDate.format(DEVICE_TIME_FORMAT)
    }*/
    //device attendance


    public static String getUiTimeStr12Hr(Date oldDate){
        if(!oldDate) return ''
        use( TimeCategory ) {
            oldDate = oldDate + 6.hours
        }
        return oldDate.format(UI_TIME_FORMAT_12HR)
    }

    public static String getUiTimeStr12HrLocal(Date oldDate){
        if(!oldDate) return ''
        return oldDate.format(UI_TIME_FORMAT_12HR)
    }
    public static String getUiDoubleStr(Double amount){
        if(!amount) return '0.00'
        return String.format("%.2f", amount)
    }
    public static String getUiDoubleStrRound(Double amount){
        if(!amount) return '-'
        return Math.round(amount).toString()
    }
    public static String getMysqlQueryDateStr(Date oldDate){
        if(!oldDate) return ''
        return oldDate.format(MYSQL_QUERY_FORMAT)
    }

    public static String getUiDateStrForCalenderDateEdit(Date oldDate){
        if(!oldDate) return ''
        return oldDate.format(UI_DATE_INPUT)
    }

    public static String getUiTimeForEdit(Date oldDate){
        if(!oldDate) return ''
        return oldDate.format(UI_TIME_INPUT)
    }

    public static String getTimeRangeStr(String startOn, Integer duration){
        String name
        try{
            DateTimeFormatter timeFormatter = DateTimeFormat.forPattern(UI_TIME_INPUT);
            LocalTime startTime =timeFormatter.parseLocalTime(startOn)
            LocalTime endTime = startTime.plusMinutes(duration)
            name = "${startTime.toString(timeFormatter)} - ${endTime.toString(timeFormatter)}"
        }catch (Exception ex){
            name = null
        }
        return name
    }

    public static Date calendarEventDateWeekViewFormat(String dateStr, boolean isStartOfDay = true){
        DateTimeFormatter formatter = DateTimeFormat.forPattern(CALENDER_EVENT_LIST_INPUT_FORMAT);
        DateTime startDateTime
        Date returnDate
        try{
            startDateTime = formatter.parseDateTime(dateStr)
            if(isStartOfDay){
                returnDate = startDateTime.withTimeAtStartOfDay().toDate()
            }else {
                returnDate = startDateTime.plusDays(1).withTimeAtStartOfDay().toDate()
            }
        }catch (Exception ex){
            returnDate = null
        }
        return returnDate
    }
    public static Date calendarEventDateMonthViewFormat(String dateStr, boolean isStartOfDay = true){
        DateTimeFormatter formatter = DateTimeFormat.forPattern(CALENDER_EVENT_MONTH_VIEW_FORMAT);
        DateTime startDateTime
        Date returnDate
        try{
            startDateTime = formatter.parseDateTime(dateStr)
            if(isStartOfDay){
                returnDate = startDateTime.withTimeAtStartOfDay().toDate()
            }else {
                returnDate = startDateTime.plusDays(1).withTimeAtStartOfDay().toDate()
            }
        }catch (Exception ex){
            returnDate = null
        }
        return returnDate
    }
    public static Date searchDateFormatDMY(String dateStr, boolean isStartOfDay = true){
        DateTimeFormatter formatter = DateTimeFormat.forPattern(UI_DATE_INPUT);
        DateTime startDateTime
        Date returnDate
        try{
            startDateTime = formatter.parseDateTime(dateStr)
            if(isStartOfDay){
                returnDate = startDateTime.withTimeAtStartOfDay().toDate()
            }else {
                returnDate = startDateTime.plusDays(1).withTimeAtStartOfDay().toDate()
            }
        }catch (Exception ex){
            returnDate = null
        }
        return returnDate
    }
    public static DateTime getDateTime(int year, int month){
       return  new DateTime().withYear(year).withMonthOfYear(month);
    }
   public static def getFirstAndLastDate(int year, int month){
        DateTime aDate = new DateTime().withYear(year).withMonthOfYear(month);
        return [firstDay: aDate.dayOfMonth().withMinimumValue().toDate(), lastDate: aDate.dayOfMonth().withMaximumValue().toDate()]
    }


    public static String getEventDateTimeStr2(Date dateObj){
        String returnStr=''
        if(!dateObj) return returnStr
        try {
            returnStr = dateObj.format(CALENDER_TIME_FORMAT)
        } catch (ParseException e) {
            e.printStackTrace()
        }
        return returnStr
    }

    public static String classStartTime(Date recordDate, String timeStr){
        if(!recordDate || !timeStr) return null
        DateTimeFormatter timeFormatter = DateTimeFormat.forPattern(UI_TIME_INPUT);
        LocalTime startTime =timeFormatter.parseLocalTime(timeStr)
        DateTime dateTime = new DateTime(recordDate).withTime(startTime.hourOfDay, startTime.minuteOfHour, 0, 0);
        return getEventDateTimeStr2(dateTime.toDate())
    }

    public static String classEndTime(Date recordDate, String timeStr, int duration){
        if(!recordDate || !timeStr) return null
        DateTimeFormatter timeFormatter = DateTimeFormat.forPattern(UI_TIME_INPUT);
        LocalTime startTime =timeFormatter.parseLocalTime(timeStr).plusMinutes(duration)
        DateTime dateTime = new DateTime(recordDate).withTime(startTime.hourOfDay, startTime.minuteOfHour, 0, 0);
        return getEventDateTimeStr2(dateTime.toDate())
    }

    public static String ordinal(int i) {
        String[] sufixes = ["th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"]
        switch (i % 100) {
            case 11:
            case 12:
            case 13:
                return i + "th";
            default:
                return i + sufixes[i % 10];

        }
    }

    public static Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }
    public static Integer monthOrder(YearMonths yearMonths) {
        return yearMonths.getSerial()
    }

    public static String createSelect(def dataObj,String idName,String labelName){

        String selectStr = '<div class="form-group" id="nweSelectDive">\n' +
                '<label class="col-md-4 control-label">\n' +
                labelName+' \n' +
                '</label>\n' +
                '<div class="col-md-8">\n' +
                '<select id="'+idName+'" class="form-control" name="'+idName+'">\n' +
                '<option value="">All Item</option>\n'

        dataObj.each {objects ->
            selectStr = selectStr + "<option value=\"${objects.id}\">${objects.name}</option>\n"
        }
        selectStr = selectStr + '</select>\n' +
                '</div>\n' +
                '</div>'
        return selectStr
    }

    public static Map getPaginationParams(GrailsParameterMap params, String[] sortColumns) {
        String iDisplayLength = params.iDisplayLength ? params.iDisplayLength : DEFAULT_PAGINATION_LENGTH
        String iDisplayStart = params.iDisplayStart ? params.iDisplayStart : null

        String sSortDir = params.sSortDir_0 ? params.sSortDir_0 : DEFAULT_PAGINATION_SORT_ORDER
        int iSortingCol = params.iSortCol_0 ? params.getInt('iSortCol_0') : DEFAULT_PAGINATION_SORT_IDX
        String sSearch = params.sSearch ? params.sSearch : null
        if (sSearch) {
            sSearch = CommonUtils.PERCENTAGE_SIGN + sSearch + CommonUtils.PERCENTAGE_SIGN
        }
        String sortColumn = CommonUtils.getSortColumn(sortColumns, iSortingCol)
        return [iDisplayStart:iDisplayStart, iDisplayLength:iDisplayLength, sSearch:sSearch, sortColumn: sortColumn, sSortDir: sSortDir]
    }


}
