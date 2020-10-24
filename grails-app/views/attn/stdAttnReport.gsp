<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleAttandanceLayout"/>
    <title> Student Attendance Reports</title>
</head>

<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Attendance Reports"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Daily Attendance Report
            </header>
            <div class="panel-body">
                <div class="row" id="studentAttenReport">
                    <div class="form-horizontal" role="form">

                        <div class="form-group">
                            <label for="stdAttnEndDay" class="col-md-2 control-label">Date Range</label>
                            <div class="col-md-6">
                                <div class="input-daterange input-group">
                                    <span class="input-group-addon">From</span>
                                    <g:textField value="${g.formatDate(date: new Date().minus(0), format: 'dd/MM/yyyy')}" class="input-sm form-control picKDate" id="stdAttnStartDay" name="stdAttnStartDay"
                                                 tabindex="1" placeholder="Start Date" required="required"/>
                                    <span class="input-group-addon">to</span>
                                    <g:textField value="${g.formatDate(date: new Date(), format: 'dd/MM/yyyy')}" class="input-sm form-control picKDate" id="stdAttnEndDay" name="stdAttnEndDay"
                                                 tabindex="2" placeholder="End Date" required="required"/>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="stdAttnClsName" class="col-md-2 control-label">Class Name</label>
                            <div class="col-md-6">
                                <g:select class=" form-control"  id="stdAttnClsName" name='stdAttnClsName' tabindex="2"
                                          noSelection="${['': 'All Class...']}"
                                          from='${classNameList}'
                                          optionKey="id" optionValue="name"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="stdAttnSecName" class="col-md-2 control-label">Section Name</label>
                            <div class="col-md-6">
                                <select name="stdAttnSecName" id="stdAttnSecName" class="form-control" tabindex="3">
                                    <option value="">All Section</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="stdAttnPresentStatus" class="col-md-2 control-label">Attendance Type</label>
                            <div class="col-md-6">
                                <select name="stdAttnPresentStatus" id="stdAttnPresentStatus" class="form-control" tabindex="4">
                                    <option value="all">All</option>
                                    <option value="present">Present</option>
                                    <option value="absent">Absent</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="stdAttnPrintOptionType" class="col-md-2 control-label">Report Type</label>
                            <div class="col-md-6">
                                <g:select class="form-control" id="stdAttnPrintOptionType" name='stdAttnPrintOptionType'
                                          from='${com.grailslab.enums.PrintOptionType.values()}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <button type="button" id="stdAttnReportBtn" class="btn btn-primary">Show Report</button>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </section>
    </div>

</div>

<g:render template='/attn/attnStdReport'/>
<g:render template='/attn/indivudualAttnReport'/>
<g:render template='/attn/attnCountReport'/>
<g:render template='/attn/attnStdBoardReport'/>
<g:render template='/attn/attnStdReligionVeiw'/>


<script>
    var reportParam, startDate,endDate,academicYear,className, studentStatus ,section,student,printOptionType,discount,reportSortType,reportType,stdAttnStartDay, reportType,stdAttnEndDay,stdAttnStartDay;

    jQuery(function ($) {

        $('#stdAttnClsName').on('change', function (e) {
            var reportEndDate = $("#stdAttnEndDay").val();
            var acdemicYear = reportEndDate.substring(reportEndDate.length-4);
            className = $('#stdAttnClsName').find("option:selected").val();
            loadSectionOnClassChange(className, 'stdAttnSecName', 'Y'+acdemicYear);
        });

        $('#stdAttnCountClsName').on('change', function (e) {
            var reportEndDate = $("#stdAttnCountEndDay").val();
            var acdemicYear = reportEndDate.substring(reportEndDate.length-4);
            className = $('#stdAttnCountClsName').find("option:selected").val();
            loadSectionOnClassChange(className, 'stdCountSecName', 'Y'+acdemicYear);
        });

        $('#stdAttnsummeryClsName').on('change', function (e) {
            var reportEndDate = $("#stdAttnsummeryEndDay").val();
            var acdemicYear = reportEndDate.substring(reportEndDate.length-4);
            className = $('#stdAttnsummeryClsName').find("option:selected").val();
            loadSectionOnClassChange(className, 'stdAttnsummerySecName','Y'+acdemicYear);
        });


     /*   function repoFormatResult(repo) {
            var markup = '<div class="row-fluid">' +
                    '<div class="span12">' + repo.name + '</div>' +
                    '</div>';
            return markup;
        }

        function repoFormatSelection(repo) {
            return repo.name;
        }*/

        $('#data_range .input-daterange').datepicker({
            keyboardNavigation: false,
            todayBtn:true,
            format: 'dd/mm/yyyy',
            forceParse: false,
            autoclose: true
        });
        $('.picKDate').datepicker({
            keyboardNavigation: false,
            todayBtn:true,
            format: 'dd/mm/yyyy',
            forceParse: false,
            autoclose: true
        });

        $('#stdAttnReportBtn').click(function (e) {
            e.preventDefault();
            stdAttnStartDay = $("#stdAttnStartDay").val();
            stdAttnEndDay = $("#stdAttnEndDay").val();
            className = $('#stdAttnClsName').val();
            section = $('#stdAttnSecName').val();
            studentStatus = $('#stdAttnPresentStatus').val();
            printOptionType = $('#stdAttnPrintOptionType').val();
            var isValidDate = stdAttnStartDay.replace('-','/') > stdAttnEndDay.replace('-','/');
            if (isValidDate == true) {
                bootbox.alert( {size: 'small', message: "Please select a valid date range"});
                return false;
            }
            if (className == undefined || className == "") {
                bootbox.alert( {size: 'small', message: "please select className"});
                return false;
            }
            reportParam ="${g.createLink(controller: 'attendanceReport',action: 'studentAttnReport','_blank')}?rStartDate="+stdAttnStartDay+"&rEndDate="+stdAttnEndDay +"&className="+className+"&sectionName="+section+"&attndanceStatus="+studentStatus+"&printOptionType="+printOptionType;
            window.open(reportParam);
            return false;
        });

    });

    function loadSectionOnChange(academicYear, className, secControlName) {
        var $select = $('#'+secControlName);
        if (className.length === 0) {
            $select.find('option').remove();
            $select.append('<option value="">All Section</option>');
            return;
        }
        jQuery.ajax({
            type: 'POST',
            dataType: 'JSON',
            url: "${g.createLink(controller: 'remote',action: 'listSection')}?academicYear="+academicYear+"&className="+className,
            success: function (data, textStatus) {
                if (data.isError == false) {
                    $select.find('option').remove();
                    $select.append('<option value="">All Section</option>');
                    $.each(data.sectionList, function (key, value) {
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
    function loadSectionOnClassChange(classNameOnchange, controlName, academicYear) {
        var $select = $('#'+controlName);
        if (classNameOnchange.length === 0) {
            $select.find('option').remove();
            $select.append('<option value="">All Section</option>');
            return;
        }
        jQuery.ajax({
            type: 'POST',
            dataType: 'JSON',
            url: "${g.createLink(controller: 'remote',action: 'listSection')}?className="+classNameOnchange+'&academicYear='+academicYear,
            success: function (data, textStatus) {
                if (data.isError == false) {
                    $select.find('option').remove();
                    $select.append('<option value="">All Section</option>');
                    $.each(data.sectionList, function (key, value) {
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
    function repoFormatResult(repo) {
        var markup = '<div class="row-fluid">' +
                '<div class="span12">' + repo.name + '</div>' +
                '</div>';
        return markup;
    }

    function repoFormatSelection(repo) {
        return repo.name;
    }

</script>


</body>
</html>