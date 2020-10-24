<%@ page import="com.grailslab.enums.ScheduleStatus;com.grailslab.enums.ExamStatus" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleExam&ResultLayout"/>
    <title>Manage Exam Tabulation</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Manage Tabulation" />
<div class="row" id="create-form-holder">
    <div class="col-sm-12">
        <div class="panel">
            <div class="panel-body">
                <form class="form-horizontal" role="form">
                    <div class="col-md-12" id="stu-manage-report-holder">
                        <div class="form-group">
                            <div class="col-md-2">
                                <g:select class="form-control academic-year-step" id="academicYear" name='academicYear' tabindex="4"
                                          noSelection="${['': 'Select Academic Year...']}"
                                          from='${academicYearList}' value="${shiftExam?.academicYear?.key}"
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                            <div class="col-md-3">
                                <g:select class=" form-control " id="examName" name='examName'
                                          tabindex="1"
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

                            <div class="col-md-2">
                                <g:select class="form-control" id="groupName" name='groupName' tabindex="3"
                                          noSelection="${['': ' General...']}"
                                          from='${com.grailslab.enums.GroupName.values()}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>

                            <div class="col-md-2">
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
            <section class="panel">
                <div class="panel-body">
                    <g:if test="${sectionExamList}">
                        <div class="table-responsive">
                        <table class="table table-striped table-hover table-bordered" id="list-table">
                            <thead>
                            <tr>
                                <th class="col-md-1">SL</th>
                                <th class="col-md-2">Class</th>
                                <th class="col-md-2">Section</th>
                                <th class="col-md-1">Status</th>
                                <th class="col-md-6 center">Action</th>
                            </tr>
                            </thead>
                            <tbody>
                            <g:each in="${sectionExamList}" var="dataSet" status="i">
                                <tr>
                                    <td>${i+1}</td>
                                    <td class="no-padding">${dataSet.className}</td>
                                    <td class="no-padding">${dataSet.sectionName}</td>
                                    <td class="no-padding">${dataSet.examStatus}</td>

                                    <td class="no-padding center">
                                        <g:if test="${dataSet.examStatus == ExamStatus.NEW.value}">
                                            <a class="btn btn-sm btn-warning markProcess-btn" referenceId="${dataSet.id}" title="Process CT Mark" href="#">Process Result</a>
                                            <a class="btn btn-sm btn-success print-report-btn" referenceId="${dataSet.id}" title="Print mark entry status" href="#">Entry Status</a>
                                        </g:if>
                                        <g:elseif test="${dataSet.examStatus == ExamStatus.PENDING.value}">
                                            Entry Processing, Please wait!
                                        </g:elseif>
                                        <g:else>
                                            <a class="btn btn-sm btn-primary masterReset-btn" referenceId="${dataSet.id}" title="Master Reset" href="#">Reset Result</a>
                                            <g:if test="${dataSet.examStatus == ExamStatus.PROCESSED.value}">
                                                <a class="btn btn-sm btn-primary create-btn" referenceId="${dataSet.id}" title="Create Tabulation" href="#">Create Tabulation</a>
                                                <a class="btn btn-sm btn-success print-report-btn" referenceId="${dataSet.id}" title="Print mark entry status" href="#">Entry Status</a>
                                            </g:if>
                                            <g:else>
                                                <a class="btn btn-sm btn-success print-btn" referenceId="${dataSet.id}" title="Print Tabulation" href="#">Print</a>
                                                <a class="btn btn-sm btn-success print-report-btn" referenceId="${dataSet.id}" title="Print mark entry status" href="#">Entry Status</a>
                                            </g:else>
                                        </g:else>
                                    </td>
                                </tr>
                            </g:each>
                            </tbody>
                        </table>
                    </div>
                    </g:if>
                    <g:else>
                        <h4> No Tabulation found</h4>
                    </g:else>
                </div>
            </section>
        </div>
    </div>

<script>
    var shiftExam, className, section, examName, groupName ,printOptionType, printParam;
    jQuery(function ($) {
        $('#merit-pos-cal-btn').click(function (e) {
            examName = $('#examName').val();
            className = $('#className').val();
            groupName = $('#groupName').val();
            if (className === undefined || className === '') {
                showErrorMsg("Please select a class to Calculate merit position");
                return false;
            }
            var confirmDel = confirm("Are you sure all section tabulation prepared? Merit Position will wrong if all section tabulation not prepared.");
            if (confirmDel == true) {
                showLoading("#create-form-holder");
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'result', action: 'calculateMeritPosition')}?shiftExam="+examName+"&className="+className+"&groupName="+groupName,
                    success: function (data, textStatus) {
                        hideLoading("#create-form-holder");
                        if (data.isError == false) {
                            showSuccessMsg(data.message);
                            window.location.href = '${g.createLink(controller: 'result',action: 'tabulation')}?shiftExam='+ examName+"&class="+className;
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
            $('#className').val("").trigger("change");
        });

        $('#className').on('change', function (e) {
            examName =$('#examName').val();
            className = $('#className').val();
            if (examName && className) {
                window.location.href = '${g.createLink(controller: 'result',action: 'tabulation')}?shiftExam='+ examName+"&class="+className;
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
                    url: "${g.createLink(controller: 'tabulation',action: 'createTabulation')}?id="+referenceId,
                    success: function (data, textStatus) {
                        hideLoading("#list-table");
                        if (data.isError == false) {
                            showSuccessMsg(data.message);
                            window.location.href = '${g.createLink(controller: 'result',action: 'tabulation')}?shiftExam=${shiftExam?.id}&class=${className?.id}';
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
            printParam ="${g.createLink(controller: 'examReport',action: 'tabulation','_blank')}/${shiftExam?.id}?examId="+referenceId;
            window.open(printParam);
            e.preventDefault();
        });

        $('#list-table').on('click', 'a.markProcess-btn', function (e) {
            var confirmDel = confirm("Are you sure to process Hall mark?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                showLoading("#list-table");
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'tabulation',action: 'markAllHallComplete')}?id="+referenceId,
                    success: function (data, textStatus) {
                        hideLoading("#list-table");
                        if (data.isError == false) {
                            showSuccessMsg(data.message);
                            window.location.href = "${createLink(controller: 'result', action: 'tabulation')}?shiftExam=${shiftExam?.id}&class=${className?.id}";
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
                    url: "${g.createLink(controller: 'tabulation',action: 'masterResetHallResult')}?id="+referenceId,
                    success: function (data, textStatus) {
                        hideLoading("#list-table");
                        if (data.isError == false) {
                            showSuccessMsg(data.message);
                            window.location.href = '${g.createLink(controller: 'result',action: 'tabulation')}?shiftExam=${shiftExam?.id}&class=${className?.id}';
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