<div class="col-sm-12">
    <section class="panel">
        <header class="panel-heading">
            Student Feedback Report
        </header>
                <div class="panel-body">
                    <div class="row">
                        <div class="row" id="book-history-report-holder">
                            <g:form class="form-horizontal" role="form" controller="feedbackReport"
                                    action="studentsFeedback" target="_blank" onsubmit="return addStdValidation()">
                                <div class="form-group">
                                    <label for="stdClassName" class="col-md-2 control-label">Select Class</label>
                                    <div class="col-md-6">
                                        <g:select class="form-control" id="stdClassName" name='stdClassName'
                                                  noSelection="${['': 'Select Class']}"
                                                  from='${classNameList}'
                                                  optionKey="id" optionValue="name"></g:select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="stdSectionName" class="col-md-2 control-label">Section</label>

                                    <div class="col-md-6">
                                        <select name="stdSectionName" id="stdSectionName" class="form-control class-name-step" tabindex="3">
                                            <option value="">All Section</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="stdWeekNo" class="col-md-2 control-label">Week No</label>
                                    <div class="col-md-6">
                                        <select name="stdWeekNo" id="stdWeekNo" class="form-control class-name-step" tabindex="3">
                                            <option value="">All Weeks</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="stdRating" class="col-md-2 control-label">Select Feedback</label>
                                    <div class="col-md-6">
                                        <select name="stdRating" id="stdRating" class="form-control class-name-step" tabindex="3">
                                            <option value="2">Bad</option>
                                            <option value="4">Below Average</option>
                                            <option value="6">Average</option>
                                            <option value="8">Very Good</option>
                                            <option value="10">Excellent</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="stdSortedBy" class="col-md-2 control-label">Sort By</label>

                                    <div class="col-md-6">
                                        <select name="stdSortedBy" id="stdSortedBy" class="form-control class-name-step" tabindex="3">
                                            <option value="std.roll_no">Roll No</option>
                                            <option value="lfa.average">Rating</option>
                                        </select>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="stdPrintType" class="col-md-2 control-label">Print Type</label>

                                    <div class="col-md-6">
                                        <g:select class="form-control" id="stdPrintType" name='stdPrintType'
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
    </section>
</div>
<script>
    function addStdValidation(){
        var stdClsName=$('#stdClassName').val();
        var stdSectionName=$('#stdSectionName').val();
        var stdWeekName=$('#stdWeekNo').val();
        if(stdClsName && stdSectionName && stdWeekName){
            return true;
        }else{
            alert("Please Select Class Name, Section Name, Week Number");
            return false;
        }
    }
    $(function ($) {
        var className, sectionName, stdClassUrl, stdSectionListUrl, stdSectionWeekList;

    $('#stdClassName').on('change', function (e) {
        className =$('#stdClassName').val();
        if (className) {
            stdClassUrl = "${g.createLink(controller: 'remote',action: 'listSection')}?className="+className;
            loadClassSection(stdClassUrl, '#stdSectionName', "#stu-manage-report-holder")
        }
        $('#stdSectionName').val("").trigger("change");
    });
    $('#stdSectionName').on('change', function (e) {
         sectionName = $('#stdSectionName').val();
        if(className!="" && sectionName!=""){
            stdSectionListUrl = "${g.createLink(controller: 'remote',action: 'sectionSubjectList')}?id="+sectionName+"&className="+className;
            stdSectionWeekList = "${g.createLink(controller: 'remote',action: 'lessonWeekList')}?id="+sectionName;
//            loadSectionSubject(stdSectionListUrl,className,sectionName, $('#stdSubjectName'), "#stu-manage-report-holder");
            loadSubjectWeek(stdSectionWeekList, $('#stdWeekNo'), "#stu-manage-report-holder");
        }
        $('#stdSubjectName').val("").trigger("change");
        $('#stdWeekNo').val("").trigger("change");
    });
         });
</script>