<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Merit List for Board Reporting
            </header>
            <div class="panel-body">
                <div class="row" id="merit-list-holder">
                    <div class="form-horizontal" role="form">
                        <div class="form-group">
                            <label for="meritAcademicYear" class="col-md-2 control-label">Academic Year</label>
                            <div class="col-md-6">
                                <g:select class="form-control academic-year-step-merit" id="meritAcademicYear" name='meritAcademicYear' tabindex="4"
                                          noSelection="${['': 'Select Academic Year...']}"
                                          from='${academicYearList}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="meritExamName" class="col-md-2 control-label">Exam Name</label>
                            <div class="col-md-6">
                                <select name="meritExamName" id="meritExamName" class="form-control merit-exam-name-step" tabindex="3">
                                    <option value="">Select Exam</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="meritClassName" class="col-md-2 control-label">Class Name</label>
                            <div class="col-md-6">
                                <select name="meritClassName" id="meritClassName" class="form-control merit-class-name-step" tabindex="3">
                                    <option value="">All Class</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="meritGroupName" class="col-md-2 control-label">Group</label>
                            <div class="col-md-6">
                                <g:select class="form-control merit-group-name" id="meritGroupName" name='meritGroupName'
                                          noSelection="${['': 'General']}"
                                          from='${com.grailslab.enums.GroupName.values()}'
                                          optionKey="key" optionValue="value" required="required"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="meritSection" class="col-md-2 control-label">Section Name</label>
                            <div class="col-md-6">
                                <select name="meritSection" id="meritSection" class="form-control merit-section-step" tabindex="3">
                                    <option value="">All Section</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="meritSortBy" class="col-md-2 control-label">Sort By</label>
                            <div class="col-md-6">
                                <select name="meritSortBy" id="meritSortBy" class="form-control list-merit-step" tabindex="3">
                                    <option value="clsMeritPos">Class Merit Position</option>
                                    <option value="secMeritPos">Section Merit Position</option>
                                    <option value="rollNo">Roll No </option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="merittabulationPrintOptionType" class="col-md-2 control-label">Print For</label>
                            <div class="col-md-6">
                                <g:select class="form-control merit-exam-type-print" id="merittabulationPrintOptionType" name='merittabulationPrintOptionType'
                                          from='${com.grailslab.enums.PrintOptionType.values()}'
                                          optionKey="key" optionValue="value" required="required"></g:select>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <button type="button" id="merit-sheet-report-btn" class="btn btn-default">Dowmload Report</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>
<script>
    jQuery(function ($) {
        //Tabulation  Top sheet show
        $('#merit-list-holder').cascadingDropdown({
            selectBoxes: [
                {
                    selector: '.academic-year-step-merit',
                    onChange: function(value){
                        loadmeritExamNames();
                    }
                },
                {
                    selector: '.merit-exam-name-step',
                    requires: ['.academic-year-step-merit'],
                    onChange: function (value) {
                        loadmeritClassNames();
                    }
                },
                {
                    selector: '.merit-class-name-step',
                    requires: ['.merit-exam-name-step'],
                    onChange: function (value) {
                        loadmeritSection();
                    }
                },
                {
                    selector: '.merit-section-step',
                    requires: ['.merit-class-name-step']
                },
                {
                    selector: '.merit-group-name',
                    requires: ['.merit-class-name-step']
                },

                {
                    selector: '.list-merit-step',
                    requires: ['.merit-class-name-step']
                },

                {
                    selector: '.merit-exam-type-print',
                    requires: ['.merit-class-name-step']
                }
            ]
        });

        $('#merit-sheet-report-btn').click(function (e) {
            examName = $('#meritExamName').val();
            classname = $('#meritClassName').val();
            section = $('#meritSection').val();
            var groupName = $('#meritGroupName').val();
            sortBy = $('#meritSortBy').val();
            printOptionType = $('#merittabulationPrintOptionType').val();
            if(examName != "" && classname != ""){
                var sectionParam ="${g.createLink(controller: 'examReport',action: 'meritList','_blank')}/"+examName+"?examId="+section+"&classname="+classname+"&sortBy="+sortBy+"&groupName="+groupName+"&printOptionType="+printOptionType;
                window.open(sectionParam);
            }
            e.preventDefault();
        });
    });

    function loadmeritExamNames(){
        academicYear=$('#meritAcademicYear').val();
        if(academicYear!=""){
            yearNameUrl = "${g.createLink(controller: 'remote',action: 'yearExamNameList')}?academicYear="+academicYear;
            loadExamNames(yearNameUrl, $('#meritExamName'),"#merit-list-holder");
        }
    }
    function loadmeritClassNames(){
        examName =$('#meritExamName').val();
        if (examName) {
            examClassUrl = "${g.createLink(controller: 'remote',action: 'examClassList')}?examName="+examName;
            loadExamClass(examClassUrl, '#meritClassName', "#merit-list-holder")
        }
    }

    function loadmeritSection(){
        examName =$('#meritExamName').val();
        className =$('#meritClassName').val();
        if(examName!="" && className!=""){
            $('#merit-sheet-report-btn').removeClass('btn-default').addClass('btn-primary');
            examAsSectionListUrl = "${g.createLink(controller: 'remote',action: 'sectionExamList')}?examType=publishing&examName="+examName+"&className="+className;
            loadExamAsClassSectionList(examAsSectionListUrl, $('#meritSection'),"#merit-list-holder");

        } else {
            $('#merit-sheet-report-btn').removeClass('btn-primary').addClass('btn-default');
        }
    }
</script>