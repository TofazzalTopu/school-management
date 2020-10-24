<%@ page import="com.grailslab.enums.HrKeyType" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Edit all Schedule List</title>
    <meta name="layout" content="moduleExam&ResultLayout"/>
</head>
<body>
<grailslab:breadCrumbActions firstBreadCrumbUrl="${g.createLink(controller: 'exam',action: 'index')}" firstBreadCrumbText="Exam List" secondBreadCrumbUrl="${g.createLink(controller: 'exam',action: 'classExams', params: [id: shiftExam?.id])}" secondBreadCrumbText="${className?.name}" breadCrumbTitleText="All Schedule" />
<div class="row" id="create-form-holder">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                <span class="panel-header-title">Class Name:</span><span class="panel-header-info">${className.name}</span>
            </header>
            <div class="panel-body">
                <div class="table-responsive">
                    <g:form name="myForm" method="post" controller="examSchedule" action="saveAllSchedule">
                        <g:hiddenField name="shiftExamId" value="${shiftExam?.id}" />
                        <g:hiddenField name="classNameId" value="${className?.id}" />
                        <g:hiddenField name="scheduleManageType" value="${scheduleManageType}" />
                        <table class="table table-striped table-hover table-bordered" id="list-table">
                            <thead>
                                <tr>
                                    <th class="col-md-1">Sort Order</th>
                                    <th class="col-md-3">Name & Type</th>
                                    <th class="col-md-3">CT Mark</th>
                                    <th class="col-md-5">Hall Mark</th>
                                </tr>
                            </thead>
                            <tbody>
                                <g:each in="${allScheduleList}" var="schedule" status="i">
                                    <tr>
                                        <td>
                                            <g:hiddenField name="classSubObjId${i}" value="${schedule.subjectId}"/>
                                            <input type="text" value="${schedule.sortOrder}" name="sortOrder${i}" id="sortOrder${schedule.subjectId}" class="form-control textField-onlyAllow-number" required>
                                            <br/>
                                            <button type="button" class="btn btn-danger delete-exam-btn" title="Remove this subject from Exam">Remove</button>
                                        </td>
                                        <td>
                                            <span style="color: red; font-weight: bold;">${schedule.subjectName}</span>
                                            <div class="input-group">
                                                <div class="input-group-addon">Tabulation Mark</div>
                                                <input type="text" value="${schedule.tabulationEffPercentage}" name="weightOnResult${i}" id="weightOnResult${schedule.subjectId}" class="form-control textField-onlyAllow-number" required>
                                                <div class="input-group-addon">%</div>
                                            </div>
                                            <div class="input-group">
                                                <div class="input-group-addon">Passed Mark</div>
                                                <input type="text" value="${schedule.passMark}" name="passMark${i}" id="passedMark${schedule.subjectId}" class="form-control textField-onlyAllow-number" >
                                            </div>

                                            <div class="check-box">
                                                <g:checkBox name="isExtracurricular${i}" id="isExtracurricular${schedule.subjectId}" value="${schedule.isExtracurricular}"/>
                                                <label> Is Extra Curricular? </label>
                                            </div>
                                        </td>
                                        <td>
                                            <g:if test="${shiftExam.ctExam >= 1}">
                                                <div class="check-box">
                                                    <g:checkBox name="isCtExam${i}" id="isCtExam${schedule.subjectId}" value="${schedule.isCtExam}" scheduleObjId="${schedule.subjectId}" class="ct-exam-checkbox"/>
%{--                                                    <input type="checkbox" class="ct-exam-checkbox" scheduleObjId="${schedule.subjectId}" value="${schedule.isCtExam}"  id="isCtExam${schedule.subjectId}" name="isCtExam${i}">--}%
                                                    <label>Ct Exam ?</label>
                                                </div>
                                                <div id="ct-mark-entry${schedule.subjectId}">
                                                    <div class="input-group">
                                                        <div class="input-group-addon">CT 1 Mark:</div>
                                                        <input type="text" value="${schedule.ctExamMark}"   name="ct1Mark${i}"  id="ct1Mark${schedule.subjectId}" class="form-control textField-onlyAllow-number">
                                                    </div>
                                                    <g:if test="${shiftExam.ctExam >= 2}">
                                                        <div class="input-group">
                                                            <div class="input-group-addon">CT 2 Mark:</div>
                                                            <input type="text" value="${schedule.ctExamMark2}"   name="ct2Mark${i}"  id="ct2Mark${schedule.subjectId}" class="form-control textField-onlyAllow-number">
                                                        </div>
                                                    </g:if>
                                                    <g:if test="${shiftExam.ctExam == 3}">
                                                        <div class="input-group">
                                                            <div class="input-group-addon">CT 3 Mark:</div>
                                                            <input type="text" value="${schedule.ctExamMark3}"   name="ct3Mark${i}"  id="ct3Mark${schedule.subjectId}" class="form-control textField-onlyAllow-number">
                                                        </div>
                                                    </g:if>
                                                    <div class="input-group">
                                                        <div class="input-group-addon">Eff:</div>
                                                        <input type="text" value="${schedule.ctMarkEffPercentage}"  name="ctEffMark${i}" id="ctEffMark${schedule.subjectId}" class="form-control textField-onlyAllow-number">
                                                        <div class="input-group-addon">%</div>
                                                    </div>
                                                </div>
                                            </g:if>
                                            <g:else>
                                                <div class="check-box">
                                                    <input type="checkbox"  value="${schedule.isCtExam}" name="isCtExam${i}"  id="isCtExam${schedule.subjectId}" class="ct-exam-checkbox" scheduleObjId="${schedule.subjectId}">
                                                    <label>Ct Exam ?</label>
                                                </div>
                                                <div id="ct-mark-entry${schedule.subjectId}" style="display: none;">
                                                    <div class="input-group">
                                                        <div class="input-group-addon">CT 1 Mark:</div>
                                                        <input type="text" value="${schedule.ctExamMark}"   name="ct1Mark${i}" placeholder="Exam Mark" id="ct1Mark${schedule.subjectId}" class="form-control textField-onlyAllow-number">
                                                    </div>
                                                    <g:if test="${shiftExam.ctExam >= 2}">
                                                        <div class="input-group">
                                                            <div class="input-group-addon">CT 2 Mark:</div>
                                                            <input type="text" value="${schedule.ctExamMark2}"   name="ct2Mark${i}"  id="ct2Mark${schedule.subjectId}" class="form-control textField-onlyAllow-number">
                                                        </div>
                                                    </g:if>
                                                    <g:if test="${shiftExam.ctExam == 3}">
                                                        <div class="input-group">
                                                            <div class="input-group-addon">Mark:</div>
                                                            <input type="text" value="${schedule.ctExamMark3}"   name="ct3Mark${i}"  id="ct3Mark${schedule.subjectId}" class="form-control textField-onlyAllow-number">
                                                        </div>
                                                    </g:if>
                                                    <div class="input-group">
                                                        <div class="input-group-addon">Eff:</div>
                                                        <input type="text" value="${schedule.ctMarkEffPercentage}"  name="ctEffMark${i}" id="ctEffMark${schedule.subjectId}" class="form-control textField-onlyAllow-number">
                                                        <div class="input-group-addon">%</div>
                                                    </div>
                                                </div>
                                            </g:else>
                                        </td>
                                        <td>
                                            <div class="check-box">
                                                <g:checkBox value="${schedule.isHallExam}"  id="isHallExam${schedule.subjectId}" name="isHallExam${i}" scheduleObjId="${schedule.subjectId}" class="is-hall-exam" disabled="disabled"/>
                                                <label for="isHallExam${schedule.subjectId}" > Hall Exam? </label>
                                            </div>
                                            <div id="hallExamBlock${schedule.subjectId}" style="display: none;" class="hallExamBlock">

                                                <div class="input-group">
                                                    <div class="input-group-addon">Mark:</div>
                                                    <input type="text"  value="${schedule.hallExamMark}"   name="hallMark${i}" id="hallMark${schedule.subjectId}" class="form-control textField-onlyAllow-number" style="background-color: white;">
                                                </div>
                                                <div class="input-group">
                                                    <div class="input-group-addon">Eff:</div>
                                                    <input type="text" value="${schedule.hallMarkEffPercentage}" name="hallEffMark${i}" id="hallEffMark${schedule.subjectId}" class="form-control textField-onlyAllow-number">
                                                    <div class="input-group-addon">%</div>
                                                </div>
                                                <div class="check-box">
                                                    <g:checkBox class="isDetailsCheckBos" value="${schedule.isHallPractical}"  name="isHallPractical${i}"  id="isHallPractical${schedule.subjectId}" scheduleObjId="${schedule.subjectId}" />
                                                    <label> Is Details? </label>
                                                </div>
                                                <div id="hall-mark-distribution${schedule.subjectId}" style="display: none;">
                                                <table class="table table-bordered table-condensed">
                                                    <tr>
                                                        <td>
                                                            <span> Hall Exam Mark Distribution</span>
                                                            <span class="pull-right"> <label class="checkbox-inline">
                                                                <g:checkBox value="${schedule.isPassSeparately}" name="isPassSeparately${i}" id="isPassSeparately${schedule.subjectId}" scheduleObjId="${schedule.subjectId}" />
                                                                Is Pass separately?
                                                            </label></span>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <div class="input-group">
                                                                <div class="input-group-addon">${grailsApplication.config.getProperty("app.markentry.hall.input1")}</div>
                                                                <input type="text" value="${schedule.hallWrittenMark}" name="hallWrittenMark${i}" id="distExamMarkWritten${schedule.subjectId}" class="form-control textField-onlyAllow-number hallExmMarkDistField${schedule.subjectId}" >
                                                                <div class="input-group-addon">Passed Mark</div>
                                                                <input type="text" value="${schedule.writtenPassMark}" name="writtenPassMark${i}" id="writtenPassMark${schedule.subjectId}" class="form-control separately-pass-mark${schedule.subjectId} textField-onlyAllow-number" disabled>
                                                            </div>
                                                            <div class="input-group">
                                                                <div class="input-group-addon">${grailsApplication.config.getProperty("app.markentry.hall.input2")}</div>
                                                                <input type="text" value="${schedule.hallObjectiveMark}" name="hallObjectiveMark${i}" id="distExamMarkObject${schedule.subjectId}" class="form-control textField-onlyAllow-number hallExmMarkDistField${schedule.subjectId}" >
                                                                <div class="input-group-addon">Passed Mark</div>
                                                                <input type="text" value="${schedule.objectivePassMark}" name="objectivePassMark${i}" id="objectivePassMark${schedule.subjectId}" class="form-control separately-pass-mark${schedule.subjectId} textField-onlyAllow-number" disabled>
                                                            </div>
                                                            <div class="input-group">
                                                                <div class="input-group-addon">${grailsApplication.config.getProperty("app.markentry.hall.input3")}</div>
                                                                <input type="text" value="${schedule.hallPracticalMark}" name="hallPracticalMark${i}" id="distExamMarkPractical${schedule.subjectId}" class="form-control textField-onlyAllow-number hallExmMarkDistField${schedule.subjectId}" >
                                                                <div class="input-group-addon">Passed Mark</div>
                                                                <input type="text" value="${schedule.practicalPassMark}" name="practicalPassMark${i}" id="distPassedMarkPractical${schedule.subjectId}" class="form-control separately-pass-mark${schedule.subjectId} textField-onlyAllow-number" disabled>
                                                            </div>
                                                            <div class="input-group">
                                                                <div class="input-group-addon">${grailsApplication.config.getProperty("app.markentry.hall.input4")}</div>
                                                                <input type="text" value="${schedule.hallSbaMark}" name="hallSbaMark${i}" id="distExamMarkSba${schedule.subjectId}" class="form-control textField-onlyAllow-number hallExmMarkDistField${schedule.subjectId}">
                                                                <div class="input-group-addon">Passed Mark</div>
                                                                <input type="text" value="${schedule.sbaPassMark}" name="sbaPassMark${i}" id="sbaPassMark${schedule.subjectId}" class="form-control separately-pass-mark${schedule.subjectId} textField-onlyAllow-number" disabled>
                                                            </div>
                                                            <div class="input-group">
                                                                <div class="input-group-addon">${grailsApplication.config.getProperty("app.markentry.hall.input5")}</div>
                                                                <input type="text" value="${schedule.hallInput5}" name="hallInput5${i}" id="distExamMarkInput5${schedule.subjectId}" class="form-control textField-onlyAllow-number hallExmMarkDistField${schedule.subjectId}" >
                                                                <div class="input-group-addon">Passed Mark</div>
                                                                <input type="text" value="${schedule.input5PassMark}" name="input5PassMark${i}" id="input5PassMark${schedule.subjectId}" class="form-control separately-pass-mark${schedule.subjectId} textField-onlyAllow-number" disabled>
                                                            </div>
                                                        </td>
                                                    </tr>
                                                </table>
                                            </div>
                                            </div>
                                        </td>
                                    </tr>
                                </g:each>
                            </tbody>
                        </table>
                        <div class="col-md-12">
                            <button id="create-yes-btn" class="btn btn-large btn-primary" type="submit">Submit</button>
                        </div>
                    </g:form>
                </div>
            </div>
        </section>
    </div>
</div>
<script>
    jQuery(function ($) {
        $('#list-table .textField-onlyAllow-number').numeric();
        $("#list-table input.is-hall-exam:checkbox:checked").each(function() {
            var referenceId = $(this).attr('scheduleObjId');
            $('#hallExamBlock'+referenceId).show();
            if ($('#isHallPractical' + referenceId).is(":checked")){
                $('#hall-mark-distribution'+referenceId).show();
                $('#hallMark'+referenceId).css({cursor:"not-allowed"}).prop('disabled', true);
                $('.hallExmMarkDistField'+referenceId).focusout(function(){
                    addHallMark();
                });
                function addHallMark(){
                    var distExamMarkWritten = ($('#distExamMarkWritten'+referenceId).val() != "" && !isNaN($('#distExamMarkWritten'+referenceId).val())) ? Math.round($('#distExamMarkWritten'+referenceId).val()) : 0;
                    var distExamMarkObject = ($('#distExamMarkObject'+referenceId).val() != "" && !isNaN($('#distExamMarkObject'+referenceId).val())) ? Math.round($('#distExamMarkObject'+referenceId).val()) : 0;
                    var distExamMarkPractical = ($('#distExamMarkPractical'+referenceId).val() != "" && !isNaN($('#distExamMarkPractical'+referenceId).val())) ? Math.round($('#distExamMarkPractical'+referenceId).val()) : 0;
                    var distExamMarkSba = ($('#distExamMarkSba'+referenceId).val() != "" && !isNaN($('#distExamMarkSba'+referenceId).val())) ? Math.round($('#distExamMarkSba'+referenceId).val()) : 0;
                    var distExamMarkInput5 = ($('#distExamMarkInput5'+referenceId).val() != "" && !isNaN($('#distExamMarkInput5'+referenceId).val())) ? Math.round($('#distExamMarkInput5'+referenceId).val()) : 0;
                      $('#hallMark'+referenceId).val(distExamMarkWritten+distExamMarkObject+distExamMarkPractical+distExamMarkSba+distExamMarkInput5);
                }
                if ($('#isPassSeparately' + referenceId).is(":checked")){
                        $(".separately-pass-mark"+referenceId).prop('disabled', false).css({cursor:"text"});
                    }else{
                        $(".separately-pass-mark"+referenceId).prop('disabled', true).css({cursor:"not-allowed"});
                }
                $('#isPassSeparately'+referenceId).change(function(){
                    if(this.checked){
                        $(".separately-pass-mark"+referenceId).prop('disabled', false).css({cursor:"text"});
                    }else{
                        $(".separately-pass-mark"+referenceId).prop('disabled', true).css({cursor:"not-allowed"});
                    }
                });
            }
        });

        $('#list-table').on('click', ':checkbox.isDetailsCheckBos', function () {
            var control = this;
            var referenceId = $(control).attr('scheduleObjId');
            if(this.checked){
                $('#hallMark'+referenceId).css({cursor:"not-allowed"}).prop('disabled', true);
                $("#hall-mark-distribution"+referenceId).show();
                $('.hallExmMarkDistField'+referenceId).focusout(function(){
                    addHallMark();
                });
                function addHallMark(){
                        var distExamMarkWritten = ($('#distExamMarkWritten'+referenceId).val() != "" && !isNaN($('#distExamMarkWritten'+referenceId).val())) ? Math.round($('#distExamMarkWritten'+referenceId).val()) : 0;
                        var distExamMarkObject = ($('#distExamMarkObject'+referenceId).val() != "" && !isNaN($('#distExamMarkObject'+referenceId).val())) ? Math.round($('#distExamMarkObject'+referenceId).val()) : 0;
                        var distExamMarkPractical = ($('#distExamMarkPractical'+referenceId).val() != "" && !isNaN($('#distExamMarkPractical'+referenceId).val())) ? Math.round($('#distExamMarkPractical'+referenceId).val()) : 0;
                        var distExamMarkSba = ($('#distExamMarkSba'+referenceId).val() != "" && !isNaN($('#distExamMarkSba'+referenceId).val())) ? Math.round($('#distExamMarkSba'+referenceId).val()) : 0;
                        var distExamMarkInput5 = ($('#distExamMarkInput5'+referenceId).val() != "" && !isNaN($('#distExamMarkInput5'+referenceId).val())) ? Math.round($('#distExamMarkInput5'+referenceId).val()) : 0;

                    $('#hallMark'+referenceId).val(distExamMarkWritten+distExamMarkObject+distExamMarkPractical+distExamMarkSba+distExamMarkInput5);
                }
            }else{
                $('#hallMark'+referenceId).prop('disabled', false).css({cursor:"text"});
                $("#hall-mark-distribution"+referenceId).hide();
            }
            $(".separately-pass-mark"+referenceId).css({cursor:"not-allowed"});
            $('#isPassSeparately'+referenceId).change(function(){
                if(this.checked){
                    $(".separately-pass-mark"+referenceId).prop('disabled', false).css({cursor:"text"});
                }else{
                    $(".separately-pass-mark"+referenceId).prop('disabled', true).css({cursor:"not-allowed"});
                }
            });
        });

        $('#list-table').on('click', ':checkbox.ct-exam-checkbox', function () {
            var control = this;
            var referenceId = $(control).attr('scheduleObjId');
            if(this.checked){
               $('#ct-mark-entry'+referenceId).show();
            }else{
               $('#ct-mark-entry'+referenceId).hide();
            }
        });
        $('#list-table').on('click', ':button.delete-exam-btn', function () {
            if(confirm("Are you sure no exam for this subject?")){
                $(this).closest('tr').remove();
                return false;
            }
        });
    });
</script>

</body>
</html>