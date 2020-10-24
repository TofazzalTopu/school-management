package com.grailslab

import grails.gorm.transactions.Transactional

@Transactional
class SqlStatementService {

    def present(String from ,String to){
        def sqlParam = new StringBuffer()
        sqlParam.append(" SELECT rd.id as record_day_id, rd.record_date, rd.day_type,  rd.working_day_type,")
        sqlParam.append(" cls.id as classId, cls.name as class_name, cls.sort_position,")
        sqlParam.append(" sec.id as sectionId, sec.name as section_name,")
        sqlParam.append(" std.id as studentId, std.studentid as stdid, std.name, std.roll_no,")
        sqlParam.append(" 'Present' as  attnType, sr.in_time, sr.out_time")
        sqlParam.append(" FROM attn_record_day rd")
        sqlParam.append(" inner join attn_std_record sr on rd.id = sr.record_day_id")
        sqlParam.append(" inner join student std on std.id = sr.student_id")
        sqlParam.append(" inner join section sec on std.section_id = sec.id")
        sqlParam.append(" inner join class_name cls on std.class_name_id = cls.id")
        sqlParam.append(" where rd.id in(Select id FROM attn_record_day where record_date between'"+from+"' and '"+to+"')")

        return sqlParam;

    }

    def absence(String from ,String to){
        def sqlParam = new StringBuffer()
        sqlParam.append(" SELECT rd.id as record_day_id, rd.record_date, rd.day_type, rd.working_day_type,")
        sqlParam.append(" cls.id as classId, cls.name as class_name, cls.sort_position,")
        sqlParam.append(" sec.id sectionId, sec.name as section_name,")
        sqlParam.append(" std.id as studentId, std.studentid as stdid, std.name, std.roll_no,")
        sqlParam.append(" 'Absent' as  attnType, null in_time, null out_time")
        sqlParam.append(" from attn_record_day rd join student std")
        sqlParam.append(" inner join section sec on std.section_id = sec.id")
        sqlParam.append(" inner join class_name cls on std.class_name_id = cls.id")
        sqlParam.append(" where std.active_status='ACTIVE' and std.student_status='NEW' and std.academic_year='Y2016'")
        sqlParam.append(" and rd.id in (Select id FROM attn_record_day where record_date between'"+from+"' and '"+to+"') and std.id not in (select student_id from attn_std_record where record_day_id in  (Select id FROM attn_record_day where record_date between'"+from+"' and '"+to+"'))")
        return sqlParam;

    }
    def bothPresentAbsence(String from ,String to){
        def sqlParam = new StringBuffer()
        sqlParam.append(" SELECT rd.id as record_day_id, rd.record_date, rd.day_type,  rd.working_day_type,")
        sqlParam.append(" cls.id as classId, cls.name as class_name, cls.sort_position,")
        sqlParam.append(" sec.id as sectionId, sec.name as section_name,")
        sqlParam.append(" std.id as studentId, std.studentid as stdid, std.name, std.roll_no,")
        sqlParam.append(" 'Present' as  attnType, sr.in_time, sr.out_time")
        sqlParam.append(" FROM attn_record_day rd")
        sqlParam.append(" inner join attn_std_record sr on rd.id = sr.record_day_id")
        sqlParam.append(" inner join student std on std.id = sr.student_id")
        sqlParam.append(" inner join section sec on std.section_id = sec.id")
        sqlParam.append(" inner join class_name cls on std.class_name_id = cls.id")
        sqlParam.append(" where rd.id in(Select id FROM attn_record_day where record_date between'"+from+"' and '"+to+"')")
        sqlParam.append(" UNION ALL")
        sqlParam.append(" SELECT rd.id as record_day_id, rd.record_date, rd.day_type, rd.working_day_type,")
        sqlParam.append(" cls.id as classId, cls.name as class_name, cls.sort_position,")
        sqlParam.append(" sec.id sectionId, sec.name section_name,")
        sqlParam.append(" std.id as studentId, std.studentid as stdid, std.name, std.roll_no,")
        sqlParam.append(" 'Absent' as  attnType, null in_time, null out_time")
        sqlParam.append(" from attn_record_day rd join student std")
        sqlParam.append(" inner join section sec on std.section_id = sec.id")
        sqlParam.append(" inner join class_name cls on std.class_name_id = cls.id")
        sqlParam.append(" where std.active_status='ACTIVE' and std.student_status='NEW' and std.academic_year='Y2016'")
        sqlParam.append(" and rd.id in (Select id FROM attn_record_day where record_date between'"+from+"' and '"+to+"') and std.id not in (select student_id from attn_std_record where record_day_id in  (Select id FROM attn_record_day where record_date between'"+from+"' and '"+to+"'))")
        return sqlParam;

    }
}
