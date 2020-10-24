<%@ page import="com.grailslab.enums.ScheduleStatus" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleExam&ResultLayout"/>
    <title>Manage Auto Tabulation</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Calculate Auto Promotion" SHOW_PRINT_BTN="YES"/>
<div class="row" id="create-form-holder">
    <div class="col-sm-12">
        <div class="panel">
            <div class="panel-body">
                <form class="form-horizontal" role="form">
                    <div class="col-md-12" id="stu-manage-report-holder">
                        <div class="form-group">
                            <div class="col-md-2">
                                <g:select class="form-control" id="academicYear" name='academicYear' tabindex="4"
                                          noSelection="${['': 'Select Academic Year...']}"
                                          from='${academicYearList}' value="${shiftExam?.academicYear?.key}"
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                            <div class="col-md-3">
                                <g:select class="form-control " id="examName" name='examName'
                                          tabindex="1"
                                          noSelection="${['': 'Select Exam...']}"
                                          from='${examNameList}' value="${shiftExam?.id}"
                                          optionKey="id" optionValue="name"></g:select>
                            </div>
                            <div class="col-md-2">
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

                            <div class="col-md-3">
                                <button class="btn btn-info" id="merit-pos-cal-btn">Calculate Auto Promotion</button>
                            </div>

                        </div>
                    </div>
                </form>
            </div>
        </div>
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
                showErrorMsg("Please select a class to Calculate auto promotion");
                return false;
            }
            var confirmDel = confirm("Are you sure all section metiit position prepared? Auto promotion roll no will wrong if all section tabulation not prepared.");
            if (confirmDel == true) {
                showLoading("#create-form-holder");
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'result', action: 'calculateAutoPromotion')}?shiftExam="+examName+"&className="+className+"&groupName="+groupName,
                    success: function (data, textStatus) {
                        hideLoading("#create-form-holder");
                        if (data.isError == false) {
                            showSuccessMsg(data.message);
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
            var academicYear =$('#academicYear').val();
            if (academicYear) {
                var examNameUrl = "${g.createLink(controller: 'remote',action: 'yearExamNameList')}?academicYear="+academicYear;
                loadExamName(examNameUrl, academicYear, '#examName', "#stu-manage-report-holder")
            }
            $('#examName').val("").trigger("change");
        });
        $('#examName').on('change', function (e) {
            examName =$('#examName').val();
            if (examName) {
                loadExamClass(examName, '#className', "#stu-manage-report-holder")
            }
            $('#className').val("").trigger("change");
        });
        $('.print-btn').click(function (e) {
            e.preventDefault();
            examName = $('#examName').val();
            className = $('#className').val();
            groupName = $('#groupName').val();
            var printOptionType = 'PDF';
            var sectionParam ="${g.createLink(controller: 'examReport',action: 'promotionList','_blank')}/"+examName+"?printOptionType="+printOptionType+"&classname="+ className;
            window.open(sectionParam);
            return false; // avoid to execute the actual submit of the form.
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