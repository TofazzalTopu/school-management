<%@ page import="com.grailslab.enums.ExamType" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleExam&ResultLayout"/>
    <title>Class Exam & Schedule </title>
</head>
<body>
<grailslab:breadCrumbActions   firstBreadCrumbUrl="${g.createLink(controller: 'exam', action: 'index')}" firstBreadCrumbText="Exam"   breadCrumbTitleText="Manage Class Schedule"/>
<div class="row" id="tc-datalist-holder">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                <span class="panel-header-title">Exam: </span> <span class="panel-header-info">${shiftExam.examName} </span>
            </header>
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                            <tr>
                                <th>SL</th>
                                <th>Class</th>
                                <th>Exam Type</th>
                                <th >Manage Exam Mark</th>
                            </tr>
                        </thead>
                        <tbody>
                        <g:each in="${classExams}" var="dataSet" status="i">
                            <tr>
                                <td>${i+1}</td>
                                <td>${dataSet.name}</td>
                                <td>${shiftExam.examTerm?.value}</td>
                                <td>
                                    <g:if test="${dataSet.isHtScheduleAdded}">
                                        <a class="btn btn-sm btn-info " title="View Exam Mark Settings" href="${g.createLink(controller: 'examSchedule', action: 'classSchedule', params: [shiftExam: shiftExam.id, className:dataSet.clasId])}">View Exam Mark Settings</a>
                                        <a class="btn btn-sm btn-warning" title="Edit Exam Mark Settings" href="${g.createLink(controller: 'examSchedule', action: 'editAllSchedule', id: shiftExam.id, params: [className: dataSet.clasId])}" >Edit Exam Mark Settings</a>
                                    </g:if>
                                    <g:else>
                                        <a class="btn btn-sm btn-primary" title="Add Exam Mark Settings" href="${g.createLink(controller: 'examSchedule', action: 'addAllSchedule', id: shiftExam.id, params: [className: dataSet.clasId])}" >Add Exam Mark Settings</a>
                                    </g:else>
                                </td>
                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                </div>
            </div>
        </section>
    </div>
</div>
<script>
    jQuery(function ($) {
        $('#list-table').dataTable({
            "sDom": "<'row'<'col-md-6'><'col-md-6'>r>t<'row'<'col-md-4'l><'col-md-4'i><'col-md-4'p>>",
            "bAutoWidth": true,
            "iDisplayLength": 25,
            "aaSorting": [0,'asc'],
            "aoColumns": [
                null,
                null,
                null,
                { "bSortable": false }
            ]
        });
    });

</script>
</body>
</html>