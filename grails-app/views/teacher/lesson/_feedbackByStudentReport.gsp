<div class="col-sm-12">
    <section class="panel">
        <header class="panel-heading">
            Feedback By Student Report
        </header>
        <div class="panel-body">
            <div class="row">
                <div class="row" id="user-history-report-holder">
                    <g:form class="form-horizontal" role="form" controller="feedbackReport"
                            action="feedbackByStudent" target="_blank" onsubmit="return addStuValidation()">
                        <div class="form-group">
                            <label for="studentName" class="col-md-2 control-label">Student Name</label>
                            <div class="col-md-6">
                                <input type="hidden" class="form-control" id="studentName" name="studentName" tabindex="2" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="stuSubjectName" class="col-md-2 control-label">Subject</label>
                            <div class="col-md-6">
                                <select name="stuSubjectName" id="stuSubjectName" class="form-control class-name-step" tabindex="3">
                                    <option value="">All Subjects</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="stuWeekNo" class="col-md-2 control-label">Week No</label>

                            <div class="col-md-6">
                                <select name="stuWeekNo" id="stuWeekNo" class="form-control class-name-step" tabindex="3">
                                    <option value="">All Weeks</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="stuPrintType" class="col-md-2 control-label">Print Type</label>

                            <div class="col-md-6">
                                <g:select class="form-control" id="stuPrintType" name='stuPrintType'
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
    function addStuValidation(){
       var stuName=$('#studentName').val();
       if(stuName){
        return true;
       }else{
           alert("Please Select Student Name");
           return false;
       }
    }
    $(function ($) {
        var avgClassName, avgSectionName, studentUrl, avgSectionListUrl, avgSectionWeekList, studentWeekNoUrl,studentName;



        $('#studentName').select2({
            placeholder: "Search for a student [name]",
            allowClear: true,
            minimumInputLength:3,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: "${g.createLink(controller: 'remote',action: 'studentTypeAheadList')}",
                dataType: 'json',
                quietMillis: 250,
                data: function (term, page) {
                    return {
                        q: term
                    };
                },
                results: function (data, page) { // parse the results into the format expected by Select2.
                    // since we are using custom formatting functions we do not need to alter the remote JSON data
                    return { results: data.items };
                },
                cache: true
            },
            formatResult: repoFormatResult, // omitted for brevity, see the source of this page
            formatSelection: repoFormatSelection,  // omitted for brevity, see the source of this page
            dropdownCssClass: "bigdrop", // apply css that makes the dropdown taller
            escapeMarkup: function (m) { return m; } // we do not want to escape markup since we are displaying html in results
        });

        $('#studentName').on("change", function(e) {
            // mostly used event, fired to the original element when the value changes
            studentName =$('#studentName').val();
            if (studentName) {
                studentUrl = "${g.createLink(controller: 'remote',action: 'studentSubjectList')}?id="+studentName;
                studentWeekNoUrl = "${g.createLink(controller: 'remote',action: 'lessonWeekList')}?studentId="+studentName;
                loadStudentSubject(studentUrl, $('#stuSubjectName'), "#stu-manage-report-holder");
                loadSubjectWeek(studentWeekNoUrl, $('#stuWeekNo'), "#stu-manage-report-holder");
            }
            $('#stuWeekNo').val("").trigger("change");
        })
         });
</script>