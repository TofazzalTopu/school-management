<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleSmsLayout"/>
    <title>Select Section to send result sms</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="SMS Result"/>
<div class="row" id="create-form-holder">
    <div class="col-sm-12">
        <div class="panel">
            <div class="panel-body">
                <form class="form-horizontal" role="form">
                    <div class="col-md-12" id="stu-manage-report-holder">
                        <div class="form-group">
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
            <header class="panel-heading">
                Select Exam to send SMS
            </header>

            <div class="panel-body">
                <g:if test="${sectionExamList}">
                    <div class="table-responsive">
                        <g:form class="form-horizontal" method="POST" controller="messaging" action="students">
                        %{--<input type="hidden" name="selectionType" value="${selectionType}">--}%
                            <table class="table table-striped table-hover table-bordered" id="list-table">
                                <thead>
                                <tr>
                                    <th>Serial</th>
                                    <th>ClassName</th>
                                    <th>Section</th>
                                    <th>Status</th>
                                    <th><input type="checkbox" name="checkAll" id="checkAll" checked> Select All</th>
                                </tr>
                                </thead>
                                <tbody>
                                <g:each in="${sectionExamList}" var="dataSet" status="i">
                                    <tr>
                                        <td>${i+1}</td>
                                        <td>${dataSet.className?.name}</td>
                                        <td>${dataSet.section?.name}</td>
                                        <td>${dataSet.examStatus?.value}</td>
                                        <td>
                                            <input type="checkbox" name="checkedId" class="checkSingle" value="${dataSet?.id}"
                                                   checked>
                                        </td>
                                    </tr>
                                </g:each>
                                </tbody>
                            </table>
                            <div class="row">
                                <div class="form-group">
                                    <div class="col-md-offset-10 col-md-2">
                                        <button class="btn btn-primary btn-submit" type="submit">Next</button>
                                    </div>
                                </div>
                            </div>
                        </g:form>
                    </div>
                </g:if>
                <g:else>
                    <p>Exam Result not publish yet for selected term</p>
                </g:else>
            </div>
        </section>
    </div>
</div>

<script>
    var shiftExam, className, section, examName, printOptionType, printParam;
    jQuery(function ($) {
        $('#examName').on('change', function (e) {
            examName =$('#examName').val();
            if (examName) {
                loadExamClass(examName, '#className', "#stu-manage-report-holder")
            }
            $('#className').val("").trigger("change");
        });
        $('#className').on('change', function (e) {
            examName =$('#examName').val();
            className = $('#className').val();
            if (examName && className) {
                window.location.href = '${g.createLink(controller: 'messaging',action: 'result')}/' + examName+"?class="+className;
            }
        });

        $("#checkAll").change(function () {
            if (this.checked) {
                $(".checkSingle").each(function () {
                    this.checked = true;
                })
            } else {
                $(".checkSingle").each(function () {
                    this.checked = false;
                })
            }
        });

        $(".checkSingle").click(function () {
            if ($(this).is(":checked")) {
                var isAllChecked = 0;
                $(".checkSingle").each(function () {
                    if (!this.checked)
                        isAllChecked = 1;
                })
                if (isAllChecked == 0) {
                    $("#checkAll").prop("checked", true);
                }
            }
            else {
                $("#checkAll").prop("checked", false);
            }
        });

        $('.create-new-btn').click(function (e) {
            $('#prepareResultModal').modal('show');
            e.preventDefault();
        });

    });
    function loadExamClass(examName, classCtrl, loadingCtrl){
        showLoading(loadingCtrl);
        jQuery.ajax({
            type: 'POST',
            dataType: 'JSON',
            url: "${g.createLink(controller: 'remote',action: 'examClassList')}?examName="+examName,
            success: function (data, textStatus) {
                hideLoading(loadingCtrl);
                if (data.isError == false) {
                    var $select = $(classCtrl);
                    $select.find("option:gt(0)").remove();
                    $.each(data.classNameList,function(key, value)
                    {
                        $select.append('<option value=' + value.id + '>' + value.name + '</option>');
                    });
                } else {
                    showErrorMsg(data.message);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            }
        });
    }
</script>
</body>
</html>