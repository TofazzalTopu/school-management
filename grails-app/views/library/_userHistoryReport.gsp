<div class="col-sm-12">
    <section class="panel">
        <header class="panel-heading">
            User History
        </header>
        <div class="panel-body">
            <div class="row">
                <div class="row" id="user-history-report-holder">
                    <g:form class="form-horizontal" role="form" controller="libraryReport"
                            action="userHistory" target="_blank">
                        <div class="form-group">
                            <label for="stdEmpAcademicYr" class="col-md-2 control-label">Academic Year</label>

                            <div class="col-md-6">
                                <g:select class="form-control" id="stdEmpAcademicYr" name='stdEmpAcademicYr'
                                          from='${workingYearList}'
                                          optionKey="key" optionValue="value" required="required"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="stuEmpID" class="col-md-2 control-label">Student/Employee/Member</label>
                            <div class="col-md-6">
                                <input type="hidden" class="form-control" id="stuEmpID" name="stuEmpID" tabindex="1" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="userFromDate" class="col-md-2 control-label">From Date</label>

                            <div class="col-md-6">
                                <input  class="form-control" type="text"  name="userFromDate" id="userFromDate" placeholder="<g:formatDate date="${new java.util.Date().minus(180)}" format="dd/MM/yyyy" />"
                                        tabindex="2"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="userToDate" class="col-md-2 control-label">To Date</label>

                            <div class="col-md-6">
                                <input  class="form-control" type="text"  name="userToDate" id="userToDate" placeholder="<g:formatDate date="${new java.util.Date()}" format="dd/MM/yyyy" />"
                                        tabindex="2"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="userHistoryPrintType" class="col-md-2 control-label">Print Type</label>

                            <div class="col-md-6">
                                <g:select class="form-control" id="userHistoryPrintType" name='userHistoryPrintType'
                                          from='${com.grailslab.enums.PrintOptionType.values()}'
                                          optionKey="key" optionValue="value" required="required"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <button type="submit" class="btn btn-primary">Show Report</button>
                            </div>
                        </div>
                    </g:form>
                </div>
            </div>
        </div>
    </section>
</div>
<script>
    jQuery(function ($) {

        $('#userFromDate').datepicker({
            format: 'dd/mm/yyyy',
            default: new Date(),
            setData: new Date(),
            autoclose: true
        });
        $('#userToDate').datepicker({
            format: 'dd/mm/yyyy',
            default: new Date(),
            setData: new Date(),
            autoclose: true
        });
    });

</script>