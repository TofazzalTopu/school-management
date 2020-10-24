<%@ page import="com.grailslab.enums.ExamType; com.grailslab.enums.ScheduleStatus" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleExam&ResultLayout"/>
    <title>Exam Schedule</title>
</head>
<body>
<grailslab:breadCrumbActions firstBreadCrumbText="Exam" firstBreadCrumbUrl="${g.createLink(controller: 'exam',action: 'index')}" secondBreadCrumbUrl="${g.createLink(controller: 'exam', action: 'classExams', id: shiftExamId)}" secondBreadCrumbText="Class List" breadCrumbTitleText="Hall Schedules" SHOW_LINK_BTN="YES"  linkBtnText="Edit Schedule"/>

<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                <span class="panel-header-title">Exam: </span> <span class="panel-header-info">${examName}</span><span class="panel-header-title">Class: </span> <span class="panel-header-info">${className?.name}</span>
            </header>
        </section>
    </div>
</div>

<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <div class="panel-body">
                <g:if test="${examScheduleList}">
                    <div class="table-responsive">
                        <table class="table table-striped table-hover table-bordered" id="list-table">
                            <thead>
                            <tr>
                                <th class="center">SL</th>
                                <th class="center">Subject</th>
                                <th class="center">CT MARK</th>
                                <th class="center">CT Cal. Mark</th>
                                <th class="center">HALL MARK</th>
                                <th class="center">HALL Cal. Mark</th>
                                <th class="center">Full Mark</th>
                                <th class="center">Tabulation Mark</th>
                            </tr>
                            </thead>
                            <tbody>
                            <g:each in="${examScheduleList}" var="dataSet" status="i">
                                <tr>
                                    <td class="center">${i+1}</td>
                                    <td class="center">${dataSet.subjectName}</td>
                                    <td class="center">
                                        <g:if test="${dataSet.isCtExam}">
                                            ${dataSet.ctExamMark}
                                            <g:if test="${dataSet.ctExamMark2 > 0}">
                                                , CT 2: ${dataSet.ctExamMark2}
                                            </g:if>
                                            <g:if test="${dataSet.ctExamMark3 > 0}">
                                                , CT 3: ${dataSet.ctExamMark3}
                                            </g:if>
                                        </g:if>
                                        <g:else>
                                            -
                                        </g:else>
                                    </td>
                                    <td class="center">
                                        <g:if test="${dataSet.isCtExam}">
                                            ${dataSet.ctMarkEffPercentage}%
                                        </g:if>
                                        <g:else>
                                            -
                                        </g:else>
                                    </td>
                                    <td class="center">${dataSet.hallExamMark}</td>
                                    <td class="center">${dataSet.hallMarkEffPercentage}%</td>
                                    <td class="center">${dataSet.fullMark}</td>
                                    <td class="center">${dataSet.tabulationEffPercentage}%</td>
                                </tr>
                            </g:each>
                            </tbody>
                        </table>
                    </div>
                </g:if>
                <g:else>
                    <g:if test='${flash.message}'>
                        <div class="errorHandler alert alert-danger">
                            <i class="fa fa-remove-sign"></i>
                            ${flash.message}
                        </div>

                    </g:if>
                </g:else>
            </div>
        </section>
    </div>
</div>

<script>
    jQuery(function ($) {
        $('#list-table').dataTable({"iDisplayLength": 25});
        $('.link-url-btn').click(function (e) {
            var confirmDel = confirm("Are you sure Edit HT Schedule?");
            if (confirmDel == true) {
                var shiftExam = "${shiftExamId}";
                var className = "${className.id}";
                window.location.href = "${createLink(controller: 'examSchedule', action: 'addAllSchedule')}?id="+shiftExam+"&className="+className+"&scheduleManageType=UPDATE";
            }
            e.preventDefault();
        });
    });
</script>
</body>
</html>