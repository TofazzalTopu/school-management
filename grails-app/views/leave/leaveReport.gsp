<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleLeaveManagementLayout"/>
    <title>Leave Report</title>
    <style>
    .form-group {
        margin-bottom: 5px;
    }
    </style>
</head>

<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Leave Report"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
               All Employee Leave Report
            </header>
            <div class="panel-body">
                <div class="row">
                    <div class="form-horizontal" role="form">

                        <div class="form-group">
                            <label for="leaveAcademicYear" class="col-md-2 control-label">Year </label>
                            <div class="col-md-4">
                                <g:select class="form-control" id="leaveAcademicYear" name='leaveAcademicYear'
                                          from='${academicYearList}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="leaveHrCategory" class="col-md-2 control-label">Department Name</label>
                            <div class="col-md-4">
                                <g:select name="leaveHrCategory" id="leaveHrCategory" class="form-control"
                                          from="${hrCategoryList}"
                                          noSelection="${['': 'All Department']}" optionKey="id"
                                          optionValue="name"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="leaveHrCategory" class="col-md-2 control-label">Leave Name</label>
                            <div class="col-md-4">
                                <g:select name="leaveName" id="leaveName" class="form-control"
                                          from="${leaveList}"
                                          optionKey="id" optionValue="name"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="" class="col-md-2 control-label">Report Type</label>
                            <div class="col-md-4">
                                <g:select class="form-control" id="leavePrintOptionType" name='leavePrintOptionType'
                                          from='${com.grailslab.enums.PrintOptionType.values()}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <button type="button" id="leaveReportBtn" class="btn btn-primary">Show Report</button>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </section>
    </div>
</div>
<g:render template="/leave/individualLeaveReport"/>
<script>
    jQuery(function ($) {

        $('#leaveReportBtn').click(function (e) {
            e.preventDefault();
            var  leaveAcademicYear = $('#leaveAcademicYear').val();
            var  hrCategory = $('#leaveHrCategory').val();
            var  leaveName = $("#leaveName").val()
            var printOptionType = $('#leavePrintOptionType').val();
            var reportParam = "${g.createLink(controller: 'leaveReport',action: 'allEmpLeaveReport','_blank')}?year=" + leaveAcademicYear + "&hrCategory=" + hrCategory + "&leaveName=" + leaveName + "&printOptionType=" + printOptionType;
            window.open(reportParam);
            return false;
        });

    });
</script>

</body>
</html>