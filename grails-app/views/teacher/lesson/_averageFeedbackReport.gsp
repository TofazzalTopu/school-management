<div class="col-sm-12">
    <section class="panel">
        <header class="panel-heading">
            Average Feedback Report
        </header>
        <div class="panel-body">
            <div class="row">
                <div class="row" id="user-history-report-holder">
                    <g:form class="form-horizontal" role="form" controller="feedbackReport"
                            action="averageFeedback" target="_blank" onsubmit="return addValidation()">
                        <div class="form-group">
                            <label for="avgClassName" class="col-md-2 control-label">Select Class</label>
                            <div class="col-md-6">
                                <g:select class="form-control" id="avgClassName" name='avgClassName'
                                          noSelection="${['': 'Select Class']}"
                                          from='${classNameList}'
                                          optionKey="id" optionValue="name"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="avgSectionName" class="col-md-2 control-label">Section</label>

                            <div class="col-md-6">
                                <select name="avgSectionName" id="avgSectionName" class="form-control class-name-step" tabindex="3">
                                    <option value="">All Section</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="avgWeekNo" class="col-md-2 control-label">Week No</label>

                            <div class="col-md-6">
                                <select name="avgWeekNo" id="avgWeekNo" class="form-control class-name-step" tabindex="3">
                                    <option value="">All Weeks</option>
                                </select>
                            </div>
                        </div>
                        %{--<div class="form-group">
                            <label for="avgSortedBy" class="col-md-2 control-label">Sort By</label>

                            <div class="col-md-6">
                                <select name="avgSortedBy" id="avgSortedBy" class="form-control class-name-step" tabindex="3">
                                    <option value="std.roll_no">Roll No</option>
                                    <option value="lfa.average">Average</option>
                                </select>
                            </div>
                        </div>--}%

                        <div class="form-group">
                            <label for="avgPrintType" class="col-md-2 control-label">Print Type</label>

                            <div class="col-md-6">
                                <g:select class="form-control" id="avgPrintType" name='avgPrintType'
                                          from='${com.grailslab.enums.PrintOptionType.values()}'
                                          optionKey="key" optionValue="value" required="required"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <button type="submit"
                                        class="btn btn-primary">Show Report</button>
                            </div>
                        </div>
                    </g:form>
                </div>
            </div>
        </div>
    </section>
</div>
<script>
    function addValidation(){
        var avgClsName=$('#avgClassName').val();
        if(avgClsName){
            return true;
        }else{
            alert("Please Select Class Name");
            return false;
        }
    }
    $(function ($) {
        var avgClassName, avgSectionName, avgClassUrl, avgSectionListUrl, avgSectionWeekList;

    $('#avgClassName').on('change', function (e) {
        avgClassName =$('#avgClassName').val();
        if (avgClassName) {
            avgClassUrl = "${g.createLink(controller: 'remote',action: 'listSection')}?className="+avgClassName;
            loadClassSection(avgClassUrl, '#avgSectionName', "#stu-manage-report-holder")
        }
        $('#avgSectionName').val("").trigger("change");
    });
    $('#avgSectionName').on('change', function (e) {
         avgSectionName = $('#avgSectionName').val();
        if(avgClassName!="" && avgSectionName!=""){
            avgSectionListUrl = "${g.createLink(controller: 'remote',action: 'sectionSubjectList')}?id="+avgSectionName+"&className="+avgClassName;
            avgSectionWeekList = "${g.createLink(controller: 'remote',action: 'lessonWeekList')}?id="+avgSectionName;
            loadSubjectWeek(avgSectionWeekList, $('#avgWeekNo'), "#stu-manage-report-holder");
        }
        $('#avgSubjectName').val("").trigger("change");
        $('#avgWeekNo').val("").trigger("change");
    });
         });
</script>