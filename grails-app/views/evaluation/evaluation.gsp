<%@ page import="com.grailslab.enums.AttendStatus; com.grailslab.enums.PrintOptionType; com.grailslab.enums.ScheduleStatus; com.grailslab.enums.EvaluationDataType;" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleExam&ResultLayout"/>
    <title>Evaluation  Entry</title>
</head>

<body>
<grailslab:breadCrumbActions firstBreadCrumbUrl="${g.createLink(controller: 'evaluation',action: 'entry')}"
                             firstBreadCrumbText="Evaluation Entry" breadCrumbTitleText="Evaluation"
                             SHOW_PRINT_BTN="NO" printBtnText="Print"/>
<g:render template='evaluationEntry'/>
<div class="row" id="create-form-holder">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                <span class="panel-header-title">Exam Name: </span><span class="panel-header-info">${schedule?.scheduleName} </span>
                <span class="panel-header-title">Class : </span> <span class="panel-header-info">${className?.name},</span>
                <span class="panel-header-title">Section : </span> <span class="panel-header-info">${section?.name},</span>

            </header>
            <div class="panel-body">
            <form class="form-horizontal" id="create-evaluation-form">
                <g:hiddenField name="scheduleId" id="scheduleId" value="${schedule?.id}" />
                <g:hiddenField name="classNameId" id="classNameId" value="${className?.id}" />
                <g:hiddenField name="sectionId" id="sectionId" value="${section?.id}" />
                <div class="form-group col-md-5">
                    <div class="col-md-12">
                        <g:select class="form-control" id="student" name='student' tabindex="1"
                                  from='${studentList}'
                                  optionKey="id" optionValue="name" required=""></g:select>
                    </div>
                </div>
                <div class="form-group col-md-2">
                    <div class="col-md-12">
                        <button id="initiateEvaluationBtn" class="btn btn-primary" tabindex="4">Evaluation</button>
                    </div>
                </div>
                <div class="row" id="create-evaluation-form-holder" style="display: none;">
                    <div class="col-sm-12">
                        <csection class="panel">
                            <div class="panel-body">
                                <table class="table table-bordered">
                                    <thead>
                                        <tr>
                                            <th class="text-center">সামাজিক</th>
                                            <th class="text-center">শারীরিক</th>
                                        </tr>
                                    </thead>
                                <tbody>
                                    <tr>
                                        <td>
                                            <div class="form-group">
                                                <label class="col-md-3 control-label">বন্ধুত্ব</label>
                                                <div class="col-md-9">
                                                    <g:each in="${EvaluationDataType.values()}" var="eval">
                                                        <label class="radio-inline">
                                                            <input name="socFriendly" type="radio" value="${eval?.value}">
                                                            ${eval?.value}
                                                        </label>
                                                    </g:each>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-md-3 control-label">শেয়ারিং</label>
                                                <div class="col-md-9">
                                                    <g:each in="${EvaluationDataType.values()}" var="eval">
                                                        <label class="radio-inline">
                                                            <input name="socSharing" type="radio" value="${eval?.value}">
                                                            ${eval?.value}
                                                        </label>
                                                    </g:each>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-md-3 control-label">সহনশীলতা</label>
                                                <div class="col-md-9">
                                                    <g:each in="${EvaluationDataType.values()}" var="eval">
                                                        <label class="radio-inline">
                                                            <input name="socTolerance" type="radio" value="${eval?.value}">
                                                            ${eval?.value}
                                                        </label>
                                                    </g:each>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-md-3 control-label">ভাষার ব্যবহার</label>
                                                <div class="col-md-9">
                                                    <g:each in="${EvaluationDataType.values()}" var="eval">
                                                        <label class="radio-inline">
                                                            <input name="socLanguageUsages" type="radio" value="${eval?.value}">
                                                            ${eval?.value}
                                                        </label>
                                                    </g:each>
                                                </div>
                                            </div>
                                        </td>
                                        <td>
                                            <div class="form-group">
                                                <label class="col-md-3 control-label">বৃদ্ধি</label>
                                                <div class="col-md-9">
                                                    <g:each in="${EvaluationDataType.values()}" var="eval">
                                                        <label class="radio-inline">
                                                            <input name="phyIncrease" type="radio" value="${eval?.value}">
                                                            ${eval?.value}
                                                        </label>
                                                    </g:each>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-md-3 control-label">সুস্থতা</label>
                                                <div class="col-md-9">
                                                    <g:each in="${EvaluationDataType.values()}" var="eval">
                                                        <label class="radio-inline">
                                                            <input name="phyWellness" type="radio" value="${eval?.value}">
                                                            ${eval?.value}
                                                        </label>
                                                    </g:each>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-md-3 control-label">খাওয়ার আগ্রহ</label>
                                                <div class="col-md-9">
                                                    <g:each in="${EvaluationDataType.values()}" var="eval">
                                                        <label class="radio-inline">
                                                            <input name="phyEatInterest" type="radio" value="${eval?.value}">
                                                            ${eval?.value}
                                                        </label>
                                                    </g:each>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                </table>
                                <table class="table table-bordered">
                                    <thead>
                                    <tr>
                                        <th class="text-center">মানসিক</th>
                                        <th class="text-center">শিক্ষা</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td>
                                            <div class="form-group">
                                                <label class="col-md-3 control-label">মনোযোগ</label>
                                                <div class="col-md-9">
                                                    <g:each in="${EvaluationDataType.values()}" var="eval">
                                                        <label class="radio-inline">
                                                            <input name="menAttention" type="radio" value="${eval?.value}">
                                                            ${eval?.value}
                                                        </label>
                                                    </g:each>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-md-3 control-label">অস্থিরতা</label>
                                                <div class="col-md-9">
                                                    <g:each in="${EvaluationDataType.values()}" var="eval">
                                                        <label class="radio-inline">
                                                            <input name="menUnrest" type="radio" value="${eval?.value}">
                                                            ${eval?.value}
                                                        </label>
                                                    </g:each>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-md-3 control-label">সঠিকতা</label>
                                                <div class="col-md-9">
                                                    <g:each in="${EvaluationDataType.values()}" var="eval">
                                                        <label class="radio-inline">
                                                            <input name="menAccuracy" type="radio" value="${eval?.value}">
                                                            ${eval?.value}
                                                        </label>
                                                    </g:each>
                                                </div>
                                            </div>
                                        </td>
                                        <td>
                                            <div class="form-group">
                                                <label class="col-md-3 control-label">গতি</label>
                                                <div class="col-md-9">
                                                    <g:each in="${EvaluationDataType.values()}" var="eval">
                                                        <label class="radio-inline">
                                                            <input name="eduMotion" type="radio" value="${eval?.value}">
                                                            ${eval?.value}
                                                        </label>
                                                    </g:each>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-md-3 control-label">আগ্রহ</label>
                                                <div class="col-md-9">
                                                    <g:each in="${EvaluationDataType.values()}" var="eval">
                                                        <label class="radio-inline">
                                                            <input name="eduInterest" type="radio" value="${eval?.value}">
                                                            ${eval?.value}
                                                        </label>
                                                    </g:each>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-md-3 control-label">বাড়ির কাজ</label>
                                                <div class="col-md-9">
                                                    <g:each in="${EvaluationDataType.values()}" var="eval">
                                                        <label class="radio-inline">
                                                            <input name="eduHomeWork" type="radio" value="${eval?.value}">
                                                            ${eval?.value}
                                                        </label>
                                                    </g:each>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-md-3 control-label">শ্রেণির কাজ</label>
                                                <div class="col-md-9">
                                                    <g:each in="${EvaluationDataType.values()}" var="eval">
                                                        <label class="radio-inline">
                                                            <input name="eduClassWork" type="radio" value="${eval?.value}">
                                                            ${eval?.value}
                                                        </label>
                                                    </g:each>
                                                </div>
                                            </div>
                                        </td>
                                    </tr>
                                </table>
                                <table class="table table-bordered">
                                    <thead>
                                    <tr>
                                        <th class="text-center">সামগ্রিক অগ্রগতি</th>
                                        <th class="text-center">মন্তব্য</th>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td>
                                            <div class="form-group">
                                                <label class="col-md-3 control-label">বাংলা</label>
                                                <div class="col-md-9">
                                                    <g:each in="${EvaluationDataType.values()}" var="eval">
                                                        <label class="radio-inline">
                                                            <input name="totBangla" type="radio" value="${eval?.value}">
                                                            ${eval?.value}
                                                        </label>
                                                    </g:each>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-md-3 control-label">ইংরেজি</label>
                                                <div class="col-md-9">
                                                    <g:each in="${EvaluationDataType.values()}" var="eval">
                                                        <label class="radio-inline">
                                                            <input name="totEnglish" type="radio" value="${eval?.value}">
                                                            ${eval?.value}
                                                        </label>
                                                    </g:each>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-md-3 control-label">গণিত</label>
                                                <div class="col-md-9">
                                                    <g:each in="${EvaluationDataType.values()}" var="eval">
                                                        <label class="radio-inline">
                                                            <input name="totMathematics" type="radio" value="${eval?.value}">
                                                            ${eval?.value}
                                                        </label>
                                                    </g:each>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-md-3 control-label">পরিবেশ প্রতিবেশ</label>
                                                <div class="col-md-9">
                                                    <g:each in="${EvaluationDataType.values()}" var="eval">
                                                        <label class="radio-inline">
                                                            <input name="totEnvEcology" type="radio" value="${eval?.value}">
                                                            ${eval?.value}
                                                        </label>
                                                    </g:each>
                                                </div>
                                            </div>
                                            <div class="form-group">
                                                <label class="col-md-3 control-label">পরিষ্কার পরিচ্ছন্নতা</label>
                                                <div class="col-md-9">
                                                    <g:each in="${EvaluationDataType.values()}" var="eval">
                                                        <label class="radio-inline">
                                                            <input name="totCleanness" type="radio" value="${eval?.value}">
                                                            ${eval?.value}
                                                        </label>
                                                    </g:each>
                                                </div>
                                            </div>
                                        </td>
                                        <td>
                                            <textarea class="form-control" rows="5" name="comment"></textarea>
                                        </td>
                                    </tr>
                                </table>
                                <div class="row">
                                    <div class="form-group">
                                        <div class="col-md-offset-8 col-lg-4">
                                            <button class="btn btn-primary btn-submit" tabindex="11" id="evaluationSaveBtn">Save</button>
                                            <button class="btn btn-default cancel-btn" tabindex="12" type="reset">Cancel</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </csection>
                    </div>
                </div>
            </form>
            </div>
        </section>
    </div>
    <div class="row">
        <div class="col-sm-12">
            <section class="panel">
                <div class="panel-body">
                    <div class="table-responsive">
                        <table class="table table-striped table-hover table-bordered" id="list-table">
                            <thead>
                            <tr>
                                %{--<th class="col-md-1">Serial</th>--}%
                                <th class="col-md-1">STD-ID</th>
                                <th class="col-md-2">Name</th>
                                <th class="col-md-1">ROll</th>
                                <th class="col-md-1">Schedul Id</th>
                                <th class="col-md-1">SectionId</th>

                            </tr>
                            </thead>
                            <tbody>
                            <g:each in="${dataReturn}" var="dataSet">
                                <tr>
                                    <td>${dataSet[0]}</td>
                                    <td>${dataSet[1]}</td>
                                    <td>${dataSet[2]}</td>
                                    <td>${dataSet[3]}</td>
                                    <td>${dataSet[4]}</td>

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
        $('#initiateEvaluationBtn').on('click', function (e) {
            $("#create-evaluation-form-holder").toggle(500);
            e.preventDefault();
        });
        var oTable1 = $('#list-table').dataTable({
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'desc'],
            "deferLoading": ${totalCount?:0},
            "sAjaxSource": "${g.createLink(controller: 'evaluation',action: 'listEvaluation')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(3)', nRow).html(getActionButtons(nRow, aData));
                return nRow;
            },
            "aoColumns": [
                null,
                null,
                null,
                { "bSortable": false }
            ]
        });
        $("#create-evaluation-form").submit(function () {
            var data = $("#create-evaluation-form").serialize();
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                data: data,
                url: "${g.createLink(controller: 'evaluation', action: 'saveEvaluation')}",
                success: function (data) {
                    if (data.isError == false) {
                        $("#create-evaluation-form").trigger("reset");
                        $("#create-evaluation-form-holder").toggle(500);
                        showSuccessMsg(data.message);
                    } else {
                        showErrorMsg(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            return false; // avoid to execute the actual submit of the form.


        });

    </script>
</div>
</body>
</html>