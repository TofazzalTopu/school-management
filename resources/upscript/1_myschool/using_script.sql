-- salary increment
update sal_increment set house_rent = round((house_rent + net_increment *0.25)), new_basic = round(old_basic + net_increment *0.70), dps_amount_school= round(previousdps_amount + net_increment*0.05) where academic_year = 'Y2019' and year_months = 'JUNE' and active_status = 'ACTIVE' and employee_id in (82,89);
update sal_increment set total_salary = new_basic + house_rent + medical + others where academic_year = 'Y2019' and year_months = 'JUNE' and active_status = 'ACTIVE'  and employee_id in (82,89);
update sal_increment set gross_salary = new_basic + house_rent + medical where academic_year = 'Y2019' and year_months = 'JUNE' and active_status = 'ACTIVE';
update sal_increment set total_salary = gross_salary + others where academic_year = 'Y2019' and year_months = 'JUNE' and active_status = 'ACTIVE';

select concat("update sal_setup set basic = ",new_basic, ", house_rent = ", house_rent, " , dps_amount_school = ", dps_amount_school, " where employee_id = ", employee_id, " and active_status = 'ACTIVE';")
 from sal_increment where academic_year = 'Y2019' and year_months = 'JUNE' and active_status = 'ACTIVE' and employee_id in (82,89);
 update sal_setup set gross_salary = basic + house_rent + medical where active_status = 'ACTIVE'  and employee_id in (82,89);
 update sal_setup set total_salary = gross_salary + others where active_status = 'ACTIVE'  and employee_id in (82,89);


 update exam_mark set ct_mark = 0 where ct_mark is null;
update exam_mark set ct_obtain_mark = 0 where ct_obtain_mark is null;
update exam_mark set full_mark = 0 where full_mark is null;
update exam_mark set hall_mark = 0 where hall_mark is null;
update exam_mark set hall_obtain_mark = 0 where hall_obtain_mark is null;
update exam_mark set is_optional = false where is_optional is null;
update exam_mark set tabulation_mark = 0 where tabulation_mark is null;
update exam_mark set hall_objective_mark = 0 where hall_objective_mark is null;
update exam_mark set hall_practical_mark = 0 where hall_practical_mark is null;
update exam_mark set hall_written_mark = 0 where hall_written_mark is null;
update exam_mark set hall_sba_mark = 0 where hall_sba_mark is null;
update exam_mark set hall_input5 = 0 where hall_input5 is null;
update exam_mark set is_extra_curricular = false where is_extra_curricular is null;
update exam_mark set tabulation_nosba_mark = 0 where tabulation_nosba_mark is null;
update exam_mark set average_mark = 0 where average_mark is null;
update exam_mark set half_yearly_mark = 0 where half_yearly_mark is null;
update exam_mark set ct2obtain_mark = 0 where ct2obtain_mark is null;
update exam_mark set ct3obtain_mark = 0 where ct3obtain_mark is null;

INSERT INTO `grade_point` (`version`,`active_status`,`created_by`,`credentials`,`date_created`,`from_mark`,`g_point`,`l_grade`,`school_id`,`up_to_mark`,`later_grade`,`class_name_id`) VALUES
(0,'ACTIVE','system','Excellent',now(),80,5,'GRADE_A_PLUS',10000,100,'A+',4),
(0,'ACTIVE','system','Very Good',now(),70,4,'GRADE_A',10000,80,'A',4),
(0,'ACTIVE','system','Good',now(),60,3.5,'GRADE_A_MINUS',10000,70,'A-',4),
(0,'ACTIVE','system','Satisfactory',now(),50,3,'GRADE_B',10000,60,'B',4),
(0,'ACTIVE','system','Need Improvement',now(),40,2,'GRADE_C',10000,50,'C',4),
(0,'ACTIVE','system','Satisfactory',now(),33,1,'GRADE_D',10000,40,'D',4),
(0,'ACTIVE','system','Fail',now(),0,0,'GRADE_F',10000,33,'F(Fail)',4);

select st.studentid, st.name, inv.invoice_no, inv.description, inv.amount, inv.created_by,
DATE_FORMAT(inv.invoice_date, "%Y-%b-%d") invoiceDate, DATE_FORMAT(inv.date_created, "%Y-%b-%d") dateCreated, DATEDIFF(inv.date_created, inv.invoice_date) createDiff
from invoice inv
inner join student st on st.id = inv.student_id
where inv.academic_year = 'Y2019' and inv.active_status = 'ACTIVE' order by inv.id desc;