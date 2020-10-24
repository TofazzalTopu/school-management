ALTER TABLE `fee_items`
DROP FOREIGN KEY `FK_k1913p8i5c4mlim2jud37plbw`,
DROP FOREIGN KEY `FK_em5m7e9dv4tsg43hnc55u3q58`;
ALTER TABLE `fee_items`
DROP COLUMN `stationary_item_id`,
DROP COLUMN `cafeteria_id`,
DROP INDEX `FK_k1913p8i5c4mlim2jud37plbw` ,
DROP INDEX `FK_em5m7e9dv4tsg43hnc55u3q58` ;

DROP TABLE `stationary_item`;
DROP TABLE `Cafeteria`;
DROP TABLE `assets`;

/*ALTER TABLE `exam`
drop COLUMN `tabulation_prepared_date`,
drop COLUMN `is_ct_confirmed`,
drop COLUMN `is_hall_confirmed`;

ALTER TABLE `exam_schedule`
drop COLUMN `is_ct_mark_input`;

delete from tabulation_ct;
UPDATE `exam` SET `ct_exam_status` = 'NEW' WHERE 1= 1;
UPDATE `exam` SET `ct2exam_status` = 'NEW' WHERE 1= 1;
UPDATE `exam` SET `ct3exam_status` = 'NEW' WHERE 1= 1;

delete from exam_mark where exam_id in (select id from exam where shift_exam_id in (select id from shift_exam where active_status = 'INACTIVE'));
delete from exam_schedule where exam_id in (select id from exam where shift_exam_id in (select id from shift_exam where active_status = 'INACTIVE'));
delete from exam where shift_exam_id in (select id from shift_exam where active_status = 'INACTIVE');
delete from shift_exam where active_status = 'INACTIVE';*/


/*ALTER TABLE `class_subjects`
drop COLUMN `academic_year`;

alter table sal_setup
drop column pc,
drop column ad_status,
drop column ads_amount;

UPDATE `shift_exam` SET `ct_exam` = 1 where is_ct_exam = 1;
update exam_schedule set is_ct_exam = true where ct_exam_date is not null;
update exam_schedule set is_hall_exam = true where hall_exam_date is not null;

select * from exam_schedule order by id desc;
update exam_schedule set active_status = 'ACTIVE' where active_status is null;
update exam_schedule set ct_exam_mark = 0 where ct_exam_mark is null;
update exam_schedule set ct_mark_eff_percentage = 0 where ct_mark_eff_percentage is null;
update exam_schedule set hall_exam_mark = 0 where hall_exam_mark is null;
update exam_schedule set hall_mark_eff_percentage = 0 where hall_mark_eff_percentage is null;
update exam_schedule set hall_sba_mark = 0 where hall_sba_mark is null;
update exam_schedule set full_mark = (hall_exam_mark * hall_mark_eff_percentage * 0.01) + (ct_exam_mark * ct_mark_eff_percentage * 0.01) where hall_sba_mark = 0;
update exam_schedule set full_mark = ((hall_exam_mark - hall_sba_mark) * hall_mark_eff_percentage * 0.01) + (ct_exam_mark * ct_mark_eff_percentage * 0.01) where hall_sba_mark > 0;

alter table exam_schedule
drop column ct_exam_date,
drop column ct_period_id,
drop column is_fourth_subject;

alter table shift_exam
drop column shift,
drop column is_ct_exam,
drop column is_hall_exam;

alter table exam
drop column is_number_sync,
drop column ct_schedule_published_date,
drop column hall_schedule_published_date,
drop column ct_schedule;

UPDATE `exam_mark` SET `ct_attend_status` = 'NO_INPUT' WHERE ct_attend_status is null;
UPDATE `exam_mark` SET `ct_attend_status` = 'NO_INPUT' WHERE ct_attend_status = "";
UPDATE `exam_mark` SET `ct2status` = 'NO_INPUT' WHERE ct2status is null;
UPDATE `exam_mark` SET `ct2status` = 'NO_INPUT' WHERE ct2status = "";
UPDATE `exam_mark` SET `ct3status` = 'NO_INPUT' WHERE ct3status is null;
UPDATE `exam_mark` SET `ct3status` = 'NO_INPUT' WHERE ct3status = "";


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

UPDATE `shift_exam` SET `ct_exam` = 0 WHERE ct_exam is null;*/


/*update sal_pc spc set spc.opening_amount = spc.payable_amount - (spc.out_standing_amount + (SELECT COALESCE(sum(paid_amount),0) FROM sal_pc_detail where sal_pc_id = spc.id)) where spc.active_status = 'ACTIVE';

INSERT INTO core_sequence_config (tenant_id, sequence_script, sequence_number, sequence_name) VALUES
(1,'regCode.toUpperCase() + String.format(\"%06d\", sequenceNumber)',1301,'Registration.studentId6DigitSequence'),
(1,'invCode.toUpperCase() + String.format(\"%06d\", sequenceNumber)',75445,'Invoice.invoiceNoSequence');

INSERT INTO core_sequence_config (tenant_id, sequence_script, sequence_number, sequence_name) VALUES
(2019,'slCode.toUpperCase() + String.format(\"%04d\", sequenceNumber)',849,'RegOnlineRegistration.serialNoSequence'),
(2019,'regCode.toUpperCase() + String.format(\"%04d\", sequenceNumber)',849,'RegOnlineRegistration.applyNoSequence');

update lesson set academic_year = 'Y2019' where academic_year is null;
update lesson_details ld set ld.academic_year = 'Y2019', ld.created_by = (select username from user where user_ref = (select empid from employee where id = ld.employee_id)) where ld.academic_year is null;

ALTER TABLE `bonus_master`
CHANGE COLUMN `bonus_percentage` `basic_percentage` DOUBLE NOT NULL ;

select id, active_status, group_name, subject_id, last_updated from class_subjects where class_name_id = 12 order by subject_id asc;
select id, active_status, group_name, subject_id, last_updated from class_subjects where class_name_id = 13 order by subject_id asc;

update student_discount sd set sd.amount = (select round(net_payable * sd.discount_percent * 0.01) from fee_items where id = sd.fee_items_id) where amount = 0;*/

