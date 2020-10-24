<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Top Sheet
            </header>
            <div class="panel-body">
                <div class="row" id="top-sheet-holder">
                    <div class="form-horizontal" role="form">

                        <div class="form-group">
                            <label for="topAcademicYear" class="col-md-2 control-label">Academic Year</label>
                            <div class="col-md-6">
                                <g:select class="form-control academic-year-step-4" id="topAcademicYear" name='topAcademicYear' tabindex="4"
                                          noSelection="${['': 'Select Academic Year...']}"
                                          from='${academicYearList}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="topExamName" class="col-md-2 control-label">Exam Name</label>
                            <div class="col-md-6">
                                <select name="topExamName" id="topExamName" class="form-control top-exam-name-step" tabindex="3">
                                    <option value="">Select Exam</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="topClassName" class="col-md-2 control-label">Class Name</label>
                            <div class="col-md-6">
                                <select name="topClassName" id="topClassName" class="form-control top-class-name-step" tabindex="3">
                                    <option value="">All Class</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="topSection" class="col-md-2 control-label">Section Name</label>
                            <div class="col-md-6">
                                <select name="topSection" id="topSection" class="form-control top-section-step" tabindex="3">
                                    <option value="">All Section</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="topGroupName" class="col-md-2 control-label">Group</label>
                            <div class="col-md-6">
                                <g:select class="form-control top-group-name" id="topGroupName" name='topGroupName'
                                          noSelection="${['': 'General']}"
                                          from='${com.grailslab.enums.GroupName.values()}'
                                          optionKey="key" optionValue="value" required="required"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="toptabulationPrintOptionType" class="col-md-2 control-label">Print For</label>
                            <div class="col-md-6">
                                <g:select class="form-control top-exam-type-print" id="toptabulationPrintOptionType" name='toptabulationPrintOptionType'
                                          from='${com.grailslab.enums.PrintOptionType.values()}'
                                          optionKey="key" optionValue="value" required="required"></g:select>
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <button type="button" id="top-sheet-report-btn" class="btn btn-default">Dowmload Report</button>
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
        $('#top-sheet-holder').cascadingDropdown({
            selectBoxes: [
                {
                    selector: '.academic-year-step-4',
                    onChange: function(value){
                        loadTopExamNames();
                    }
                },
                {
                    selector: '.top-exam-name-step',
                    requires: ['.academic-year-step-4'],
                    onChange: function (value) {
                        loadTopClassNames();
                    }
                },
                {
                    selector: '.top-class-name-step',
                    requires: ['.top-exam-name-step'],
                    onChange: function (value) {
                        loadTopSection();
                    }
                },
                {
                    selector: '.top-section-step',
                    requires: ['.top-class-name-step']
                },

                {
                    selector: '.top-group-name',
                    requires: ['.top-class-name-step']
                },
                {
                    selector: '.top-exam-type-print',
                    requires: ['.top-class-name-step']
                }
            ]
        });


        $('#top-sheet-report-btn').click(function (e) {
            examName = $('#topExamName').val();
            classname = $('#topClassName').val();
            examId = $('#topSection').val();
            var groupName = $('#topGroupName').val();
            printOptionType = $('#toptabulationPrintOptionType').val();
            if(classname != ""){
                var sectionParam ="${g.createLink(controller: 'examReport',action: 'topsheet','_blank')}?examId="+examId+"&printOptionType="+printOptionType+"&examName="+examName+"&classname="+classname+"&groupName="+groupName;
                window.open(sectionParam);
            }
            e.preventDefault();
        });
    });
    function loadTopExamNames(){
        academicYear=$('#topAcademicYear').val();
        if(academicYear!=""){
            yearNameUrl = "${g.createLink(controller: 'remote',action: 'yearExamNameList')}?academicYear="+academicYear;
            loadExamNames(yearNameUrl, $('#topExamName'),"#top-sheet-holder");
        }
    }
    function loadTopClassNames(){
        examName =$('#topExamName').val();
        if (examName) {
            examClassUrl = "${g.createLink(controller: 'remote',action: 'examClassList')}?examName="+examName;
            loadExamClass(examClassUrl, '#topClassName', "#top-sheet-holder")
            $('#top-sheet-report-btn').removeClass('btn-default').addClass('btn-primary');
        } else {
            $('#top-sheet-report-btn').removeClass('btn-primary').addClass('btn-default');
        }
    }

    function loadTopSection(){
        examName =$('#topExamName').val();
        className =$('#topClassName').val();
        if(examName!="" && className!=""){
            examAsSectionListUrl = "${g.createLink(controller: 'remote',action: 'sectionExamList')}?examType=publishing&examName="+examName+"&className="+className;
            loadExamAsClassSectionList(examAsSectionListUrl, $('#topSection'),"#top-sheet-holder");
            $('#top-sheet-report-btn').removeClass('btn-default').addClass('btn-primary');
        }
    }
</script>