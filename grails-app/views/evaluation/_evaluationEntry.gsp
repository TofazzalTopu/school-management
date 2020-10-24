<div class="row" id="markEntrySubjectSelectHolder" style="display: none">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                <span class="panel-header-info">Select Subject to Evaluation Entry</span>
            </header>
            <div class="panel-body">
                <div class="row" id="markEntrySelectionHolder">
                    <div class="form-horizontal" role="form">
                        <div class="form-group">
                            <label for="mesExamName" class="col-md-2 control-label">Academic Year</label>
                            <div class="col-md-6">
                                <g:select class=" form-control mesAcademicYearStep" id="mesAcademicYearName" name='mesAcademicYearName'
                                          noSelection="${['': 'Select Year...']}"
                                          from='${academicYearList}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="mesExamName" class="col-md-2 control-label">Exam Name</label>
                            <div class="col-md-6">
                                <select name="mesExamName"  id="mesExamName" class="form-control mesExamNameStep">
                                    <option value="">Select Exam</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="mesClassName" class="col-md-2 control-label">Class Name</label>
                            <div class="col-md-6">
                                <select name="mesClassName"  id="mesClassName" class="form-control mesClassNameStep">
                                    <option value="">All Class</option>
                                </select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="mesSection" class="col-md-2 control-label">Section Name</label>
                            <div class="col-md-6">
                                <select name="mesSection" id="mesSection" class="form-control mesSectionStep">
                                    <option value="">All Section</option>
                                </select>
                            </div>
                        </div>


                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <button type="button" id="student-mark-entry-btn"
                                        class="btn btn-default">Entry Evaluation</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>
<script type="application/javascript">
    var section, examTerm, examName, examType, subject, className, examClassUrl,examAsSectionListUrl;
    jQuery(function ($) {
        $('#markEntrySelectionHolder').cascadingDropdown({
            selectBoxes: [
                {
                    selector: '.mesAcademicYearStep',
                    onChange: function (value) {
                        mesLoadAcademicYearExams();
                    }
                },
                {
                    selector: '.mesExamNameStep',
                    requires: ['.mesAcademicYearStep'],
                    onChange: function (value) {
                        mesLoadExamClasses();
                    }
                },
                {
                    selector: '.mesClassNameStep',
                    requires: ['.mesExamNameStep'],
                    onChange: function (value) {
                        mesLoadSection();
                    }
                }
            ]
        });
        $('#mesExamName').on('change', function (e) {
            $('#mesClassName').val("").trigger("change");
        });
        $('#mesClassName').on('change', function (e) {
            $('#mesSection').val("").trigger("change");
        });
        $('#mesSection').on('change', function (e) {
            $('#mesExamType').val("").trigger("change");
        });
        function mesLoadAcademicYearExams(){
            var academicYearName =$('#mesAcademicYearName').val();
            if (academicYearName) {
                var examNameUrl = "${g.createLink(controller: 'evaluation',action: 'yearExamNameList')}?academicYear="+academicYearName;
                loadExamName(examNameUrl, academicYearName, '#mesExamName', "#markEntrySelectionHolder");
            } else {
                $('#mesSubjectHolder').hide(500);
                $('#student-mark-entry-btn').removeClass('btn-primary').addClass('btn-default');
            }
        }
        function mesLoadExamClasses(){
            examName =$('#mesExamName').val();
            if (examName) {
                examClassUrl = "${g.createLink(controller: 'evaluation',action: 'examClassList')}?scheduleName="+examName;
                loadExamClass(examClassUrl, '#mesClassName', "#markEntrySelectionHolder")
            } else {
                $('#mesSubjectHolder').hide(500);
                $('#student-mark-entry-btn').removeClass('btn-primary').addClass('btn-default');
            }
        }
        function mesLoadSection(){
            examName =$('#mesExamName').val();
            className =$('#mesClassName').val();
            if(examName!="" && className!=""){
                examAsSectionListUrl = "${g.createLink(controller: 'evaluation',action: 'sectionExamList')}?scheduleName="+examName+"&className="+className;
                loadExamAsClassSectionList(examAsSectionListUrl, $('#mesSection'),"#markEntrySelectionHolder");
            } else {
                $('#mesSubjectHolder').hide(500);
                $('#student-mark-entry-btn').removeClass('btn-primary').addClass('btn-default');
            }
        }

        $('#student-mark-entry-btn').click(function (e) {
            var mesAcademicYearName = $('#mesAcademicYearName').val();
            var mesExamName = $('#mesExamName').val();
            var mesClassName = $('#mesClassName').val();
            var mesSection = $('#mesSection').val();
            if ((section != "") && (examTerm != "") && (examType != "") && (subject != "")) {
                window.location.href = '${g.createLink(controller: 'evaluation',action: 'evaluation')}?academicYear=' + mesAcademicYearName + '&examName='
                            + mesExamName + '&className=' + mesClassName + '&section=' + mesSection;
            }
            e.preventDefault();
        });
    });
</script>
