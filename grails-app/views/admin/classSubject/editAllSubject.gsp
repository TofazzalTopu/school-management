<%@ page import="com.grailslab.enums.HrKeyType" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Edit all Subject List</title>
    <meta name="layout" content="moduleStdMgmtLayout"/>
</head>
<body>
<grailslab:breadCrumbActions firstBreadCrumbUrl="${g.createLink(controller: 'classSubjects',action: 'index')}" firstBreadCrumbText="Class List" secondBreadCrumbUrl="${g.createLink(controller: 'classSubjects',action: 'subjects', params: [id: className?.id])}" secondBreadCrumbText="${className?.name}" breadCrumbTitleText="All Subjects" />
<div class="row" id="create-form-holder">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                <span class="panel-header-title">Class Name:</span><span class="panel-header-info">${className.name}</span>
            </header>
            <div class="panel-body">
                <div class="table-responsive">
                    <g:form name="myForm" method="post" controller="classSubjects" action="saveAllSubject">
                        <g:hiddenField name="id" value="${className?.id}" />
                        <table class="table table-striped table-hover table-bordered" id="list-table">
                            <thead>
                                <tr>
                                    <th class="col-md-1">Sort Order</th>
                                    <th class="col-md-3">Name & Type</th>
                                    <th class="col-md-3">CT Mark Entry?</th>
                                    <th class="col-md-5">Hall Mark Entry?</th>
                                </tr>
                            </thead>
                            <tbody>
                                <g:each in="${classSubjectList}" var="classSubjectObj" status="i">
                                    <g:hiddenField name="classSubObjIds" value="${classSubjectObj.id}"/>
                                    <tr>
                                        <td>
                                            <input type="text" value="${classSubjectObj.sortPosition}" name="sortOrder${classSubjectObj.id}" id="sortOrder${classSubjectObj.id}" class="form-control textField-onlyAllow-number" required>
                                        </td>
                                        <td>
                                            ${classSubjectObj?.subjectName} <span style="color: red;">(${classSubjectObj.groupName?:'All'})</span>
                                            <div class="input-group">
                                                <div class="input-group-addon">Tabulation Mark</div>
                                                <input type="text" value="${classSubjectObj.weightOnResult}" name="weightOnResult${classSubjectObj.id}" id="weightOnResult${classSubjectObj.id}" class="form-control textField-onlyAllow-number" required>
                                                <div class="input-group-addon">%</div>
                                            </div>
                                            <div class="input-group">
                                                <div class="input-group-addon">Passed Mark</div>
                                                <input type="text" value="${classSubjectObj.passMark}" name="passMark${classSubjectObj.id}" id="passedMark${classSubjectObj.id}" class="form-control textField-onlyAllow-number" >
                                            </div>

                                            <div class="check-box">
                                                <g:checkBox name="isExtracurricular${classSubjectObj.id}" id="isExtracurricular${classSubjectObj.id}" value="${classSubjectObj.isExtracurricular}"/>
                                                <label> Is Extra Curricular? </label>
                                            </div>
                                            <div class="check-box">
                                                <g:checkBox name="isOtherActivity${classSubjectObj.id}" id="isOtherActivity${classSubjectObj.id}" value="${classSubjectObj.isOtherActivity}"/>
                                                <label>Only Final mark input? </label>
                                            </div>

                                        </td>
                                        <td>
                                            <g:if test="${classSubjectObj.isCtExam}">
                                                <div class="check-box">
                                                    <input type="checkbox" class="ct-exam-checkbox" classSubjectObjId="${classSubjectObj.id}" value="${classSubjectObj.isCtExam}"  id="isCtExam${classSubjectObj.id}" name="isCtExam${classSubjectObj.id}" checked>
                                                    <label>Ct Exam ?</label>
                                                </div>
                                                <div id="ct-mark-entry${classSubjectObj.id}">
                                                    <div class="input-group">
                                                        <div class="input-group-addon">CT 1 Mark:</div>
                                                        <input type="text" value="${classSubjectObj.ctMark}"   name="ctMark${classSubjectObj.id}"  id="ctMark${classSubjectObj.id}" class="form-control textField-onlyAllow-number">
                                                    </div>
                                                    <div class="input-group">
                                                        <div class="input-group-addon">CT 2 Mark:</div>
                                                        <input type="text" value="${classSubjectObj.ctMark2}"   name="ctMark2${classSubjectObj.id}"  id="ctMark2${classSubjectObj.id}" class="form-control textField-onlyAllow-number">
                                                    </div>
                                                    <div class="input-group">
                                                        <div class="input-group-addon">CT 3 Mark:</div>
                                                        <input type="text" value="${classSubjectObj.ctMark3}"   name="ctMark3${classSubjectObj.id}"  id="ctMark3${classSubjectObj.id}" class="form-control textField-onlyAllow-number">
                                                    </div>
                                                    <div class="input-group">
                                                        <div class="input-group-addon">Eff:</div>
                                                        <input type="text" value="${classSubjectObj.ctEffMark}"  name="ctEffMark${classSubjectObj.id}" id="ctEffMark${classSubjectObj.id}" class="form-control textField-onlyAllow-number">
                                                        <div class="input-group-addon">%</div>
                                                    </div>
                                                </div>
                                            </g:if>
                                            <g:else>
                                                <div class="check-box">
                                                    <input type="checkbox"  value="${classSubjectObj.isCtExam}" name="isCtExam${classSubjectObj.id}"  id="isCtExam${classSubjectObj.id}" class="ct-exam-checkbox" classSubjectObjId="${classSubjectObj.id}">
                                                    <label>Ct Exam ?</label>
                                                </div>
                                                <div id="ct-mark-entry${classSubjectObj.id}" style="display: none;">
                                                    <div class="input-group">
                                                        <div class="input-group-addon">CT 1 Mark:</div>
                                                        <input type="text" value="${classSubjectObj.ctMark}"   name="ctMark${classSubjectObj.id}" placeholder="CT 1 Mark" id="ctMark${classSubjectObj.id}" class="form-control textField-onlyAllow-number">
                                                    </div>
                                                    <div class="input-group">
                                                        <div class="input-group-addon">CT 2 Mark:</div>
                                                        <input type="text" value="${classSubjectObj.ctMark2}"   name="ctMark2${classSubjectObj.id}" placeholder="CT 2 Mark" id="ctMark2${classSubjectObj.id}" class="form-control textField-onlyAllow-number">
                                                    </div>
                                                    <div class="input-group">
                                                        <div class="input-group-addon">CT 3 Mark:</div>
                                                        <input type="text" value="${classSubjectObj.ctMark3}"   name="ctMark3${classSubjectObj.id}" placeholder="CT 3 Mark" id="ctMark3${classSubjectObj.id}" class="form-control textField-onlyAllow-number">
                                                    </div>

                                                    <div class="input-group">
                                                        <div class="input-group-addon">Eff:</div>
                                                        <input type="text" value="${classSubjectObj.ctEffMark}"  name="ctEffMark${classSubjectObj.id}" id="ctEffMark${classSubjectObj.id}" class="form-control textField-onlyAllow-number">
                                                        <div class="input-group-addon">%</div>
                                                    </div>
                                                </div>
                                            </g:else>
                                        </td>
                                        <td>
                                            <div class="check-box">
                                                <g:hiddenField id="isHallExam${classSubjectObj.id}" name="isHallExam${classSubjectObj.id}" classSubjectObjId="${classSubjectObj.id}" class="is-hall-exam"/>
                                                <label for="isHallExam${classSubjectObj.id}" > Hall Exam </label>
                                            </div>
                                            <div id="hallExamBlock${classSubjectObj.id}" style="display: none;" class="hallExamBlock">

                                                <div class="input-group">
                                                    <div class="input-group-addon">Mark:</div>
                                                    <input type="text"  value="${classSubjectObj.hallMark}"   name="hallMark${classSubjectObj.id}" id="hallMark${classSubjectObj.id}" class="form-control textField-onlyAllow-number" style="background-color: white;">
                                                </div>
                                                <div class="input-group">
                                                    <div class="input-group-addon">Eff:</div>
                                                    <input type="text" value="${classSubjectObj.hallEffMark}" name="hallEffMark${classSubjectObj.id}" id="hallEffMark${classSubjectObj.id}" class="form-control textField-onlyAllow-number">
                                                    <div class="input-group-addon">%</div>
                                                </div>
                                                <div class="check-box">
                                                    <g:checkBox class="isDetailsCheckBos" value="${classSubjectObj.isHallPractical}"  name="isHallPractical${classSubjectObj.id}"  id="isHallPractical${classSubjectObj.id}" classSubjectObjId="${classSubjectObj.id}" />
                                                    <label> Is Details? </label>
                                                </div>
                                                <div id="hall-mark-distribution${classSubjectObj.id}" style="display: none;">
                                                <table class="table table-bordered table-condensed">
                                                    <tr>
                                                        <td>
                                                            <span> Hall Exam Mark Distribution</span>
                                                            <span class="pull-right"> <label class="checkbox-inline">
                                                                <g:checkBox value="${classSubjectObj.isPassSeparately}" name="isPassSeparately${classSubjectObj.id}" id="isPassSeparately${classSubjectObj.id}" classSubjectObjId="${classSubjectObj.id}" />
                                                                Is Pass separately?
                                                            </label></span>
                                                        </td>
                                                    </tr>
                                                    <tr>
                                                        <td>
                                                            <div class="input-group">
                                                                <div class="input-group-addon">${grailsApplication.config.getProperty("app.markentry.hall.input1")}</div>
                                                                <input type="text" value="${classSubjectObj.hallWrittenMark}" name="hallWrittenMark${classSubjectObj.id}" id="distExamMarkWritten${classSubjectObj.id}" class="form-control textField-onlyAllow-number hallExmMarkDistField${classSubjectObj.id}" >
                                                                <div class="input-group-addon">Passed Mark</div>
                                                                <input type="text" value="${classSubjectObj.writtenPassMark}" name="writtenPassMark${classSubjectObj.id}" id="writtenPassMark${classSubjectObj.id}" class="form-control separately-pass-mark${classSubjectObj.id} textField-onlyAllow-number" disabled>
                                                            </div>
                                                            <div class="input-group">
                                                                <div class="input-group-addon">${grailsApplication.config.getProperty("app.markentry.hall.input2")}</div>
                                                                <input type="text" value="${classSubjectObj.hallObjectiveMark}" name="hallObjectiveMark${classSubjectObj.id}" id="distExamMarkObject${classSubjectObj.id}" class="form-control textField-onlyAllow-number hallExmMarkDistField${classSubjectObj.id}" >
                                                                <div class="input-group-addon">Passed Mark</div>
                                                                <input type="text" value="${classSubjectObj.objectivePassMark}" name="objectivePassMark${classSubjectObj.id}" id="objectivePassMark${classSubjectObj.id}" class="form-control separately-pass-mark${classSubjectObj.id} textField-onlyAllow-number" disabled>
                                                            </div>
                                                            <div class="input-group">
                                                                <div class="input-group-addon">${grailsApplication.config.getProperty("app.markentry.hall.input3")}</div>
                                                                <input type="text" value="${classSubjectObj.hallPracticalMark}" name="hallPracticalMark${classSubjectObj.id}" id="distExamMarkPractical${classSubjectObj.id}" class="form-control textField-onlyAllow-number hallExmMarkDistField${classSubjectObj.id}" >
                                                                <div class="input-group-addon">Passed Mark</div>
                                                                <input type="text" value="${classSubjectObj.practicalPassMark}" name="practicalPassMark${classSubjectObj.id}" id="distPassedMarkPractical${classSubjectObj.id}" class="form-control separately-pass-mark${classSubjectObj.id} textField-onlyAllow-number" disabled>
                                                            </div>
                                                            <div class="input-group">
                                                                <div class="input-group-addon">${grailsApplication.config.getProperty("app.markentry.hall.input4")}</div>
                                                                <input type="text" value="${classSubjectObj.hallSbaMark}" name="hallSbaMark${classSubjectObj.id}" id="distExamMarkSba${classSubjectObj.id}" class="form-control textField-onlyAllow-number hallExmMarkDistField${classSubjectObj.id}">
                                                                <div class="input-group-addon">Passed Mark</div>
                                                                <input type="text" value="${classSubjectObj.sbaPassMark}" name="sbaPassMark${classSubjectObj.id}" id="sbaPassMark${classSubjectObj.id}" class="form-control separately-pass-mark${classSubjectObj.id} textField-onlyAllow-number" disabled>
                                                            </div>
                                                            <div class="input-group">
                                                                <div class="input-group-addon">${grailsApplication.config.getProperty("app.markentry.hall.input5")}</div>
                                                                <input type="text" value="${classSubjectObj.hallInput5}" name="hallInput5${classSubjectObj.id}" id="distExamMarkInput5${classSubjectObj.id}" class="form-control textField-onlyAllow-number hallExmMarkDistField${classSubjectObj.id}" >
                                                                <div class="input-group-addon">Passed Mark</div>
                                                                <input type="text" value="${classSubjectObj.input5PassMark}" name="input5PassMark${classSubjectObj.id}" id="input5PassMark${classSubjectObj.id}" class="form-control separately-pass-mark${classSubjectObj.id} textField-onlyAllow-number" disabled>
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
                            <button class="btn btn-default" aria-hidden="true" data-dismiss="modal"
                                    type="button">Cancel</button>
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
        $("#list-table input.is-hall-exam").each(function() {
            var referenceId = $(this).attr('classSubjectObjId');
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
            var referenceId = $(control).attr('classSubjectObjId');
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
            var referenceId = $(control).attr('classSubjectObjId');
            if(this.checked){
               $('#ct-mark-entry'+referenceId).show();
            }else{
               $('#ct-mark-entry'+referenceId).hide();
            }
        });

    });
</script>

</body>
</html>