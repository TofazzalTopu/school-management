<%@ page import="com.grailslab.gschoolcore.AcademicYear; com.grailslab.enums.ScheduleStatus;com.grailslab.enums.ExamStatus" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleExam&ResultLayout"/>
    <title>Manage CT Tabulation</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Manage CT  Tabulation"/>
<div class="row" id="create-form-holder">
    <div class="col-sm-12">
        <div class="panel">
            <div class="panel-body">
                <form class="form-horizontal" role="form">
                    <div class="col-md-12" id="stu-manage-report-holder">
                        <div class="form-group">
                            <div class="col-md-3">
                                <g:select class="form-control academic-year-step" id="academicYear" name='academicYear' tabindex="4"
                                          noSelection="${['': 'Select Academic Year...']}"
                                          from='${academicYearList}' value="${shiftExam?.academicYear?.key}"
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                            <div class="col-md-3">
                                <g:select class=" form-control " id="examName" name='examName'
                                          tabindex="2"
                                          noSelection="${['': 'Select Exam...']}"
                                          from='${examNameList}' value="${shiftExam?.id}"
                                          optionKey="id" optionValue="name"></g:select>
                            </div>
                            <div class="col-md-3">
                                <g:select class=" form-control " id="className" name='className'
                                          tabindex="2"
                                          noSelection="${['': 'Select Class...']}"
                                          from='${classNameList}' value="${className?.id}"
                                          optionKey="id" optionValue="name"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-md-3">
                                <select class="form-control" id="ctExamNo" name='ctExamNo'>
                                    <option value="">Select CT Exam</option>
                                    <option value="CLASS_TEST1" ${ctExamNo == 'CLASS_TEST1'? 'selected': ''}>CT 1 Exam</option>
                                    <option value="CLASS_TEST2" ${ctExamNo == 'CLASS_TEST2'? 'selected': ''}>CT 2 Exam</option>
                                    <option value="CLASS_TEST3" ${ctExamNo == 'CLASS_TEST3'? 'selected': ''}>CT 3 Exam</option>
                                </select>
                            </div>
                            <div class="col-md-3">
                                <g:select class="form-control" id="groupName" name='groupName' tabindex="3"
                                          noSelection="${['': ' General...']}"
                                          from='${com.grailslab.enums.GroupName.values()}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>

                            <div class="col-md-4">
                                <button class="btn btn-info" id="merit-pos-cal-btn">Calculate Merit Position</button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<div class="row">
    <div class="col-sm-12">
        <g:if test="${sectionExamList}">
            <section class="panel">
                <div class="panel-body">
                    <div class="table-responsive">
                        <table class="table table-striped table-hover table-bordered" id="list-table">
                            <thead>
                            <tr>
                                <th class="col-md-1">SL</th>
                                <th class="col-md-1">Class</th>
                                <th class="col-md-2">Section</th>
                                <th class="col-md-2">Status</th>
                                <th class="col-md-6 center">Action</th>
                            </tr>
                            </thead>
                            <tbody>
                            <g:each in="${sectionExamList}" var="dataSet" status="i">
                                <tr>
                                    <td>${i+1}</td>
                                    <td class="no-padding">${dataSet.className}</td>
                                    <td class="no-padding">${dataSet.sectionName}</td>
                                    <td class="no-padding">${dataSet.examStatusStr}</td>
                                    <td class="no-padding center">
                                        <g:if test="${dataSet.examStatusStr == "New"}">
                                            <a class="btn btn-sm btn-warning markProcess-btn" referenceId="${dataSet.id}" title="Process CT Mark" href="#">Process CT Mark</a>
                                            <a class="btn btn-sm btn-success print-report-btn" referenceId="${dataSet.id}" title="Print mark entry status" href="#">Entry Status</a>
                                        </g:if>
                                        <g:else>
                                            <a class="btn btn-sm btn-danger masterReset-btn" referenceId="${dataSet.id}" title="Master Reset" href="#">Master Reset</a>
                                            <g:if test="${dataSet.examStatusStr == "Processed"}">
                                                <a class="btn btn-sm btn-primary create-btn" referenceId="${dataSet.id}" title="Create Tabulation" href="#">Create Tabulation</a>
                                                <a class="btn btn-sm btn-success print-report-btn" referenceId="${dataSet.id}" title="Print mark entry status" href="#">Entry Status</a>
                                            </g:if>
                                            <g:else>
                                                <a class="btn btn-sm btn-success print-btn" referenceId="${dataSet.id}" title="Print Tabulation" href="#">Print Tabulation</a>
                                                <a class="btn btn-sm btn-success print-report-btn" referenceId="${dataSet.id}" title="Print mark entry status" href="#">Entry Status</a>
                                            </g:else>
                                        </g:else>
                                    </td>

                                </tr>
                            </g:each>
                            </tbody>
                        </table>
                    </div>
                </div>
            </section>
        </g:if>
        <g:else>
            <h4> No Tabulation found</h4>
        </g:else>
    </div>
</div>

<script>
    var shiftExam, academicYear, className, section, examName, ctExamNo, groupName ,printOptionType, printParam;
    jQuery(function ($) {
        $('#merit-pos-cal-btn').click(function (e) {
            examName = $('#examName').val();
            className = $('#className').val();
            groupName = $('#groupName').val();
            ctExamNo = $('#ctExamNo').val();
            if (className === undefined || className === '' || ctExamNo === '') {
                showErrorMsg("Please select a class and ct exam no to Calculate merit position");
                return false;
            }
            var confirmDel = confirm("Are you sure all section tabulation prepared? Merit Position will wrong if all section tabulation not prepared.");
            if (confirmDel == true) {
                showLoading("#create-form-holder");
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'result', action: 'calculateCtMeritPosition')}?shiftExam="+examName+"&className="+className+"&groupName="+groupName+"&ctExamNo="+ctExamNo,
                    success: function (data, textStatus) {
                        hideLoading("#create-form-holder");
                        if (data.isError == false) {
                            showSuccessMsg(data.message);
                            window.location.href = '${g.createLink(controller: 'result',action: 'ctTabulation')}?shiftExam='+ examName+"&class="+className+"&ctExamNo="+ctExamNo;
                        } else {
                            showErrorMsg(data.message);
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            }
            e.preventDefault();
        });
        $('#academicYear').on('change', function (e) {
            academicYear =$('#academicYear').val();
            if (academicYear) {
                var examNameUrl = "${g.createLink(controller: 'remote',action: 'yearExamNameList')}?academicYear="+academicYear;
                loadExamName(examNameUrl, academicYear, '#examName', "#stu-manage-report-holder")
            }
            $('#examName').val("").trigger("change");
        });
        $('#examName').on('change', function (e) {
            examName =$('#examName').val();
            if (examName) {
                var examClassNameUrl = "${g.createLink(controller: 'remote',action: 'examClassList')}?examName="+examName;
                loadExamClass(examClassNameUrl, '#className', "#stu-manage-report-holder")
            }
            $('#ctExamNo').val("");
        });
        $('#className').on('change', function (e) {
            $('#ctExamNo').val("").trigger("change");
        });

        $('#ctExamNo').on('change', function (e) {
            examName =$('#examName').val();
            className = $('#className').val();
            ctExamNo = $('#ctExamNo').val();
            if (examName && className && ctExamNo) {
                window.location.href = '${g.createLink(controller: 'result',action: 'ctTabulation')}?shiftExam='+ examName+"&class="+className+"&ctExamNo="+ctExamNo;
            }
        });

        $('#list-table').dataTable({
            "iDisplayLength": 25,
            "aoColumns": [
                { "bSortable": false },
                { "bSortable": false },
                { "bSortable": false },
                { "bSortable": false },
                { "bSortable": false }
            ]
        });

        $('#list-table').on('click', 'a.create-btn', function (e) {
            var confirmDel = confirm("Are you sure to create tabulation?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                showLoading("#list-table");
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'tabulation',action: 'createCtTabulation')}?id="+referenceId+"&ctExamNo=${ctExamNo}",
                    success: function (data, textStatus) {
                        hideLoading("#list-table");
                        if (data.isError == false) {
                            showSuccessMsg(data.message);
                            window.location.href = "${g.createLink(controller: 'result',action: 'ctTabulation')}?shiftExam=${shiftExam?.id}&class=${className?.id}&ctExamNo=${ctExamNo}";
                        } else {
                            showErrorMsg(data.message);
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            }
            e.preventDefault();
        });

        $('#list-table').on('click', 'a.print-report-btn', function (e) {
            e.preventDefault();
            var control = this;
            var referenceId = $(control).attr('referenceId');
            printOptionType = 'PDF';
            printParam = "${g.createLink(controller: 'examReport',action: 'markEntryStatus','_blank')}/${shiftExam?.id}?exam="+referenceId+"&printOptionType="+printOptionType;
            window.open(printParam);
            return false;
        });

        $('#list-table').on('click', 'a.print-btn', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            printParam ="${g.createLink(controller: 'examReport',action: 'ctTabulation','_blank')}/${shiftExam?.id}?examId="+referenceId+"&ctExamNo=${ctExamNo}";
            window.open(printParam);
            e.preventDefault();
        });





        $('#list-table').on('click', 'a.markProcess-btn', function (e) {
            var confirmDel = confirm("Are you sure to process CT mark?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                showLoading("#list-table");
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'tabulation',action: 'markAllCtComplete')}?id="+referenceId+"&ctExamNo=${ctExamNo}",
                    success: function (data, textStatus) {
                        hideLoading("#list-table");
                        if (data.isError == false) {
                            showSuccessMsg(data.message);
                            window.location.href = "${g.createLink(controller: 'result',action: 'ctTabulation')}?shiftExam=${shiftExam?.id}&class=${className?.id}&ctExamNo=${ctExamNo}";
                        } else {
                            showErrorMsg(data.message);
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            }
            e.preventDefault();
        });

        $('#list-table').on('click', 'a.masterReset-btn', function (e) {
            var confirmReset = confirm("Are you sure to master reset?");
            if (confirmReset == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                showLoading("#list-table");
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'tabulation',action: 'masterResetCtResult')}?id="+referenceId+"&ctExamNo=${ctExamNo}",
                    success: function (data, textStatus) {
                        hideLoading("#list-table");
                        if (data.isError == false) {
                            showSuccessMsg(data.message);
                            window.location.href = "${g.createLink(controller: 'result',action: 'ctTabulation')}?shiftExam=${shiftExam?.id}&class=${className?.id}&ctExamNo=${ctExamNo}";
                        } else {
                            showErrorMsg(data.message);
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            }
            e.preventDefault();
        });
    });
</script>
</body>
</html>