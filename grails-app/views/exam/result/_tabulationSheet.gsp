<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Tabulation Sheet
            </header>
            <div class="panel-body">
                <div class="row" id="tabulation-sheet-holder">
                    <div class="form-horizontal" role="form">

                        <div class="form-group">
                            <label for="taAcademicYear" class="col-md-2 control-label">Academic Year</label>
                            <div class="col-md-6">
                                <g:select class="form-control academic-year-step-3" id="taAcademicYear" name='taAcademicYear' tabindex="4"
                                          noSelection="${['': 'Select Academic Year...']}"
                                          from='${academicYearList}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="taExamName" class="col-md-2 control-label">Exam Name</label>
                            <div class="col-md-6">
                                <select name="taExamName" id="taExamName" class="form-control ta-exam-name-step" tabindex="3">
                                    <option value="">Select Exam</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="taClassName" class="col-md-2 control-label">Class Name</label>
                            <div class="col-md-6">
                                <select name="taClassName" id="taClassName" class="form-control ta-class-name-step" tabindex="3">
                                    <option value="">All Class</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="taSection" class="col-md-2 control-label">Section Name</label>
                            <div class="col-md-6">
                                <select name="taSection" id="taSection" class="form-control ta-section-step" tabindex="3">
                                    <option value="">All Section</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="taSortBy" class="col-md-2 control-label">Sort By</label>
                            <div class="col-md-6">
                                <select name="taSortBy" id="taSortBy" class="form-control ta-merit-step" tabindex="3">
                                    <option value="rollNo">Roll No </option>
                                    <option value="secMeritPos">Section Merit Position</option>
                                    <option value="clsMeritPos">Class Merit Position</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="tabulationPrintOptionType" class="col-md-2 control-label">Print For</label>
                            <div class="col-md-6">
                                <g:select class="form-control ta-exam-type-print" id="tabulationPrintOptionType" name='tabulationPrintOptionType'
                                          from='${com.grailslab.enums.PrintOptionType.values()}'
                                          optionKey="key" optionValue="value" required="required"></g:select>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <button type="button" id="tabulation-sheet-report-btn" class="btn btn-default">Dowmload Report</button>
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
        //Tabulation sheet show
        $('#tabulation-sheet-holder').cascadingDropdown({
            selectBoxes: [
                {
                    selector: '.academic-year-step-3',
                    onChange: function(value){
                        loadTaExamNames();
                    }
                },
                {
                    selector: '.ta-exam-name-step',
                    requires: ['.academic-year-step-3'],
                    onChange: function (value) {
                        loadTaClassNames();
                    }
                },
                {
                    selector: '.ta-class-name-step',
                    requires: ['.ta-exam-name-step'],
                    onChange: function (value) {
                        loadTaSection();
                    }
                },
                {
                    selector: '.ta-section-step',
                    requires: ['.ta-class-name-step']
                },

                {
                    selector: '.ta-merit-step',
                    requires: ['.ta-section-step']
                },

                {
                    selector: '.ta-exam-type-print',
                    requires: ['.ta-section-step']
                }
            ]
        });
        $('#taSection').change(function () {
            var optionSelected = $(this).find("option:selected");
            section  = optionSelected.val();
            if(section == undefined || section == ""){
                $('#tabulation-sheet-report-btn').removeClass('btn-primary').addClass('btn-default');
            }else{
                $('#tabulation-sheet-report-btn').removeClass('btn-default').addClass('btn-primary');
            }
        });
        $('#tabulation-sheet-report-btn').click(function (e) {

            examName =$('#taExamName').val();
            section = $('#taSection').val();
            sortBy = $('#taSortBy').val();
            printOptionType = $('#tabulationPrintOptionType').val();
            if((examName != "") && (section != "")){
                var sectionParam ="${g.createLink(controller: 'examReport',action: 'tabulation','_blank')}/"+examName+"?examId="+section+"&sortBy="+sortBy+"&printOptionType="+printOptionType;
                window.open(sectionParam);
            }
            e.preventDefault();
        });
    });
    function loadTaExamNames(){
        academicYear=$('#taAcademicYear').val();
        if(academicYear!=""){
            yearNameUrl = "${g.createLink(controller: 'remote',action: 'yearExamNameList')}?academicYear="+academicYear;
            loadExamNames(yearNameUrl, $('#taExamName'), "#tabulation-sheet-holder");
        }
    }
    function loadTaClassNames(){
        examName =$('#taExamName').val();
        if (examName) {
            examClassUrl = "${g.createLink(controller: 'remote',action: 'examClassList')}?examName="+examName;
            loadExamClass(examClassUrl, '#taClassName', "#tabulation-sheet-holder")
        }
    }
    function loadTaSection(){
        examName =$('#taExamName').val();
        className =$('#taClassName').val();
        if(examName!="" && className!=""){
            examAsSectionListUrl = "${g.createLink(controller: 'remote',action: 'sectionExamList')}?examType=publishing&examName="+examName+"&className="+className;
            loadExamAsClassSectionList(examAsSectionListUrl, $('#taSection'),"#tabulation-sheet-holderr");

        }
        $('#tabulation-sheet-report-btn').removeClass('btn-primary').addClass('btn-default');
    }
</script>