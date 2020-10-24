/* #################  Student Management Module  start ############# */
/*1. Class Subject Mapping View*/
    DROP TABLE IF EXISTS v_std_class_subject;
		CREATE OR REPLACE VIEW v_std_class_subject AS
       select cs.id, cn.name class_name, cn.id class_name_id,
      cs.group_name, cs.weight_on_result, cs.pass_mark, cs.is_extracurricular, cs.is_other_activity,
      cs.subject_type, cs.alternative_sub_ids, (select GROUP_CONCAT(name) from subject_name where FIND_IN_SET(id, cs.alternative_sub_ids)) alternative_sub_names,
      cs.is_ct_exam, cs.ct_mark, cs.ct_mark2, cs.ct_mark3, cs.ct_eff_mark, cs.is_hall_exam, cs.hall_mark, cs.hall_eff_mark, cs.is_hall_practical, cs.hall_written_mark, cs.hall_practical_mark, cs.hall_objective_mark,cs.hall_sba_mark,cs.hall_input5,
      sn.name subject_name, sn.id subject_id, cs.sort_order sort_position, sn.code, cs.is_pass_separately, cs.written_pass_mark, cs.objective_pass_mark, cs.practical_pass_mark, cs.sba_pass_mark, cs.input5pass_mark
      from
      class_subjects cs
      inner join class_name cn on cs.class_name_id = cn.id
      inner join subject_name sn on cs.subject_id = sn.id
      where cs.active_status='ACTIVE' and sn.active_status='ACTIVE';


      /*2. Student Subject Mapping View*/
      DROP TABLE IF EXISTS v_std_student_subject;
      CREATE OR REPLACE VIEW v_std_student_subject AS
      select std.id std_id, cls.id class_name_id, cls.name class_name, sec.id section_id, sec.name section_name,
      std.name student_name, std.roll_no, std.studentid,
      null student_subject_id, cs.group_name, cs.subject_type, null is_optional, cs.subject_name, cs.subject_id
      from student std
      inner join section sec on sec.id = std.section_id
      inner join class_name cls on sec.class_name_id = cls.id
      left join v_std_class_subject cs on cs.class_name_id = cls.id and (cs.group_name is null or cs.group_name=sec.group_name)
      where sec.academic_year in ('Y2019','Y2020','Y2018_2019','Y2019_2020') and sec.active_status='ACTIVE' and cs.subject_type='COMPULSORY' and std.student_status='NEW'
      union
      select std.id std_id, cls.id class_name_id, cls.name class_name, sec.id section_id, sec.name section_name,
      std.name student_name, std.roll_no, std.studentid,
      sbs.id student_subject_id, sec.group_name, sbs.subject_type, sbs.is_optional, sn.name subject_name, sn.id subject_id
      from student std
      inner join section sec on sec.id = std.section_id
      inner join class_name cls on sec.class_name_id = cls.id
      inner join student_subjects sbs on std.id = sbs.student_id
      inner join subject_name sn on sbs.subject_id = sn.id
      where sec.academic_year in ('Y2019','Y2020','Y2018_2019','Y2019_2020') and sec.active_status='ACTIVE' and std.student_status ='NEW' and sbs.active_status='ACTIVE';

/* #################  Student Management Module End ############# */



/*#################  Attendance Module  Start ############# */

/*1. Student or Employee card view */
      DROP TABLE IF EXISTS v_attn_card_no;
      CREATE OR REPLACE VIEW v_attn_card_no AS
      select registration.device_id AS card_no, registration.studentid as std_emp_no, student.id as obj_id,
      student.section_id as section_id, 'student' as obj_type, cls.hr_period_id as hr_period_id
      from student
      inner join registration on student.registration_id=registration.id
      inner join class_name cls on student.class_name_id = cls.id
      where student.academic_year in ('Y2019','Y2020') and student.student_status = 'NEW' and registration.device_id is not null
      UNION ALL
      select emp.device_id AS card_no, emp.empid as std_emp_no, emp.id as obj_id, null as section_id,
      'employee' as obj_type, emp.hr_period_id as hr_period_id
      from employee emp
      where emp.device_id is not null and emp.active_status = 'ACTIVE';

/*2. Employee Present view */
      DROP TABLE IF EXISTS v_attn_emp_present;
      CREATE OR REPLACE VIEW v_attn_emp_present AS
      SELECT er.id as obj_id, rd.day_type, rd.holiday_name, rd.record_date, rd.working_day_type,
      er.in_time, er.is_late_entry as is_late, er.is_early_leave as is_early_leave,
      er.out_time, er.record_day_id,
      emp.id employee_id, emp.card_no, emp.empid, emp.mobile, emp.name, emp.employee_type, hrd.name as designation, hrd.hr_category_id as hr_category_id
      FROM attn_record_day rd
      inner join attn_emp_record er on rd.id = er.record_day_id
      inner join employee emp on emp.id = er.employee_id
      inner join hr_designation hrd on emp.hr_designation_id = hrd.id order by rd.id, hrd.hr_category_id, hrd.sort_order, emp.sort_order;

/*3. Student Present View */
      DROP TABLE IF EXISTS v_attn_std_present;
      CREATE OR REPLACE VIEW v_attn_std_present AS
      SELECT sr.id as obj_id, rd.day_type, rd.holiday_name, rd.record_date, rd.working_day_type,
      sr.in_time, sr.is_late_entry as is_late, sr.is_early_leave as is_early_leave, sr.out_time, sr.record_day_id,
      st.id student_id, st.name, st.roll_no, st.studentid stdid, reg.gender, reg.religion, sec.name section_name, cls.name as class_name
      FROM attn_record_day rd
      inner join attn_std_record sr on rd.id = sr.record_day_id
      inner join student st on st.id = sr.student_id
      inner join registration reg on st.registration_id = reg.id
      inner join section sec on st.section_id = sec.id
      inner join class_name cls on st.class_name_id = cls.id
      order by rd.id, cls.sort_position, sec.id, st.roll_no;

/*#################  Attendance Module  end ############# */

DROP TABLE IF EXISTS v_std_emp;
      CREATE OR REPLACE VIEW v_std_emp AS
 select registration.studentid as std_emp_no, registration.name as name, registration.mobile, registration.id as obj_id, 'student' as obj_type
      from registration where registration.student_status = 'ADMISSION'
      UNION ALL
      select emp.empid as std_emp_no, emp.name as name, emp.mobile, emp.id as obj_id, 'employee' as obj_type
      from employee emp
      where emp.active_status = 'ACTIVE';


DROP TABLE IF EXISTS v_library_member;
      CREATE OR REPLACE VIEW v_library_member AS
      select registration.studentid as std_emp_no, registration.name as name, registration.mobile, registration.id as obj_id, libCon.allowed_days, 'student' as obj_type
      from registration
      inner join library_config libCon
      where registration.student_status = 'ADMISSION' and libCon.member_type = 'Student' and libCon.active_status = 'ACTIVE'
      UNION ALL
      select emp.empid as std_emp_no, emp.name as name, emp.mobile, emp.id as obj_id, libCon.allowed_days, 'employee' as obj_type
      from employee emp
      inner join library_config libCon
      where emp.active_status = 'ACTIVE' and libCon.member_type = 'Teacher' and libCon.active_status = 'ACTIVE'
      UNION ALL
      SELECT lim.member_id as std_emp_no,
	  lim.name as name, lim.mobile, lim.id as obj_id, libCon.allowed_days, 'Guardian' as obj_type
	  FROM library_member lim
      inner join library_config libCon
      where lim.active_status = 'ACTIVE' and libCon.member_type = 'Guardian' and libCon.active_status = 'ACTIVE';


DROP TABLE IF EXISTS v_lesson_feedback;
CREATE OR REPLACE VIEW v_lesson_feedback AS
select cln.name as class_name, cln.id as class_name_id,  sc.id as section_name_id,
sc.name as section_name, lw.week_number, lw.id as week_number_id,
sub.name as subject_name, sub.id as subject_name_id, lf.academic_year
from lesson_feedback_detail as ld
inner join lesson_feedback lf on lf.id=ld.feedback_id
inner join section sc on sc.id = lf.section_id
inner join class_name cln on cln.id = lf.class_name_id
inner join lesson_week lw on lw.id = lf.lesson_week_id
inner join subject_name sub on sub.id = ld.subject_name_id
group by cln.id, sc.id, lf.academic_year, lw.id, sub.id
order by cln.sort_position, sc.id, lw.week_number;

DROP TABLE IF EXISTS view_book_transaction;
CREATE OR REPLACE VIEW view_book_transaction AS
SELECT
    trx.id,
    trx.book_id,
    trx.school_id,
    trx.reference_id,
    trx.academic_year,
    trx.reference_type,
    trx.issue_date,
    trx.fine,
    trx.pay_type,
    trx.return_date,
    trx.return_type,
    trx.submission_date,
	  stu.name as ref_name,
    stu.studentid as ref_id,
    b.book_id as b_id,
    bdetails.name as book_detail,
    ath.name as author_name
FROM book_transaction trx
LEFT JOIN student stu ON stu.id = trx.reference_id
LEFT JOIN book b ON b.id = trx.book_id
LEFT JOIN book_details bdetails ON bdetails.id = b.book_details_id
LEFT JOIN author ath ON ath.id = bdetails.author_id
WHERE trx.active_status = 'ACTIVE'
AND trx.reference_type = 'student'

UNION ALL

SELECT
    trx.id,
    trx.book_id,
    trx.school_id,
    trx.reference_id,
    trx.academic_year,
    trx.reference_type,
    trx.issue_date,
    trx.fine,
    trx.pay_type,
    trx.return_date,
    trx.return_type,
    trx.submission_date,
    member.name as ref_name,
    member.member_id as ref_id,
	  b.book_id as b_id,
    bdetails.name as book_detail,
    ath.name as author_name
FROM book_transaction trx
LEFT JOIN library_member member ON member.id = trx.reference_id
LEFT JOIN book b ON b.id = trx.book_id
LEFT JOIN book_details bdetails ON bdetails.id = b.book_details_id
LEFT JOIN author ath ON ath.id = bdetails.author_id
WHERE trx.active_status = 'ACTIVE'
AND trx.reference_type = 'member'

UNION ALL

SELECT
    trx.id,
    trx.book_id,
    trx.school_id,
    trx.reference_id,
    trx.academic_year,
    trx.reference_type,
    trx.issue_date,
    trx.fine,
    trx.pay_type,
    trx.return_date,
    trx.return_type,
    trx.submission_date,
    emp.name as ref_name,
    emp.empid as ref_id,
	  b.book_id as b_id,
    bdetails.name as book_detail,
    ath.name as author_name
FROM book_transaction trx
LEFT JOIN employee emp ON emp.id = trx.reference_id
LEFT JOIN book b ON b.id = trx.book_id
LEFT JOIN book_details bdetails ON bdetails.id = b.book_details_id
LEFT JOIN author ath ON ath.id = bdetails.author_id
WHERE trx.active_status = 'ACTIVE'
AND trx.reference_type = 'employee';

DROP TABLE IF EXISTS v_grade_teacher;
CREATE OR REPLACE VIEW v_grade_teacher AS
SELECT ght.id, ght.active_status, ght.academic_year, ght.student_ids, emp.empid, emp.name, emp.id as employee_id
FROM guide_teacher ght
inner join employee emp on ght.employee_id = emp.id;
