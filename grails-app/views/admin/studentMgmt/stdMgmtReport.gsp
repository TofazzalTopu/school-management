<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleStdMgmtLayout"/>
    <title>Student Management Reports</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Student Reports"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Student Report
            </header>
            <div class="panel-body">
                <form class="form-inline" role="form">
                    <div class="form-group">
                        <g:select class="form-control" id="studentAcademicYear" name='studentAcademicYear' tabindex="1"
                                  from='${academicYearList}'
                                  optionKey="key" optionValue="value"/>
                    </div>
                    <div class="form-group">
                        <g:select class=" form-control" id="studentClassName" name='studentClassName' tabindex="2"
                                  noSelection="${['': 'All Class...']}"
                                  from='${classNameList}'
                                  optionKey="id" optionValue="name"/>
                    </div>
                    <div class="form-group">
                        <select name="studentSection" id="studentSection" class="form-control" tabindex="3">
                        <option value="">All Section</option>
                    </select>
                    </div>
                    <div class="form-group">
                        <select name="studentStatus" id="studentStatus" class="form-control" tabindex="4">
                            <option value="NEW">Students</option>
                            <option value="TC">TC or Dropout</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <g:select class="form-control" id="studentPrintOptionType" name='studentPrintOptionType'
                                  from='${com.grailslab.enums.PrintOptionType.values()}'
                                  optionKey="key" optionValue="value"/>
                    </div>
                    <button type="button" id="studentReportBtn" class="btn btn-primary">Show Report</button>
                </form>
            </div>
        </section>
    </div>
</div>

<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Student Single Page
            </header>
            <div class="panel-body">
                <form class="form-inline" role="form">
                    <div class="form-group">
                        <g:select class="form-control" id="studentSingleAcademicYear" name='studentSingleAcademicYear' tabindex="1"
                                  from='${academicYearList}'
                                  optionKey="key" optionValue="value"/>
                    </div>
                    <div class="form-group">
                        <g:select class=" form-control" id="singleClassName" name='singleClassName' tabindex="2"
                                  noSelection="${['': 'All Class...']}"
                                  from='${classNameList}'
                                  optionKey="id" optionValue="name"/>
                    </div>
                    <div class="form-group">
                        <select name="singleSection" id="singleSection" class="form-control" tabindex="3">
                            <option value="">All Section</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <select name="singleStatus" id="singleStatus" class="form-control" tabindex="4">
                            <option value="NEW">Students</option>
                            <option value="NameRoll">Students (Name/Roll)</option>
                            <option value="TC">TC or Dropout</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <g:select class="form-control" id="singlePrintOptionType" name='singlePrintOptionType'
                                  from='${com.grailslab.enums.PrintOptionType.values()}'
                                  optionKey="key" optionValue="value"/>
                    </div>
                    <button type="button" id="singleReportBtn" class="btn btn-primary">Show Report</button>
                </form>
            </div>
        </section>
    </div>
</div>

<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Student Gender / Relegion Report
            </header>
            <div class="panel-body">
                <form class="form-inline" role="form">
                    <div class="form-group">
                        <g:select class="form-control" id="genRelegionAcademicYear" name='genRelegionAcademicYear' tabindex="1"
                                  from='${academicYearList}'
                                  optionKey="key" optionValue="value"/>
                    </div>
                    <div class="form-group">
                        <g:select class=" form-control" id="genRelegionClassName" name='genRelegionClassName' tabindex="2"
                                  noSelection="${['': 'All Class...']}"
                                  from='${classNameList}'
                                  optionKey="id" optionValue="name"/>
                    </div>
                    <div class="form-group">
                        <select name="genRelegionSection" id="genRelegionSection" class="form-control" tabindex="3">
                            <option value="">All Section</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <g:select class="form-control" id="gender" name='gender' tabindex="8"
                                  noSelection="${['': 'All Gender']}"
                                  from='${com.grailslab.enums.Gender.values()}'
                                  optionKey="key" optionValue="value"/>
                    </div>
                    <div class="form-group">
                        <g:select class="form-control" id="religion" name='religion' tabindex="4"
                                  noSelection="${['': 'All Religion']}"
                                  from='${com.grailslab.enums.Religion.values()}'
                                  optionKey="key" optionValue="value"/>
                    </div>
                    <div class="form-group">
                        <g:select class="form-control" id="genderRelegionPrintOptionType" name='genderRelegionPrintOptionType'
                                  from='${com.grailslab.enums.PrintOptionType.values()}'
                                  optionKey="key" optionValue="value"/>
                    </div>
                    <button type="button" id="genRelegionReportBtn" class="btn btn-primary">Show Report</button>
                </form>
            </div>
        </section>
    </div>
</div>

<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Student Contact Info
            </header>
            <div class="panel-body">
                <form class="form-inline" role="form">
                    <div class="form-group">
                        <g:select class="form-control" id="contactAcademicYear" name='contactAcademicYear' tabindex="1"
                                  from='${academicYearList}'
                                  optionKey="key" optionValue="value"/>
                    </div>
                    <div class="form-group">
                        <g:select class=" form-control" id="contactClassName" name='contactClassName' tabindex="2"
                                  noSelection="${['': 'All Class...']}"
                                  from='${classNameList}'
                                  optionKey="id" optionValue="name"/>
                    </div>
                    <div class="form-group">
                        <select name="contactSection" id="contactSection" class="form-control" tabindex="3">
                            <option value="">All Section</option>
                        </select>
                    </div>
                    <div class="form-group">
                        <select name="cReportType" id="cReportType" class="form-control" tabindex="4">
                            <option value="ADDRESS">Address</option>
                            <option value="VALID">Valid Mobile </option>
                            <option value="INVAILD">Invalid Mobile No</option>

                        </select>
                    </div>
                    <div class="form-group">
                        <g:select class="form-control" id="contactPrintOptionType" name='contactPrintOptionType'
                                  from='${com.grailslab.enums.PrintOptionType.values()}'
                                  optionKey="key" optionValue="value"/>
                    </div>
                    <button type="button" id="contactReportBtn" class="btn btn-primary">Show Report</button>
                </form>
            </div>
        </section>
    </div>
</div>

<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Birth date report
            </header>
            <div class="panel-body">
                <form class="form-inline" role="form">
                    <div class="form-group">
                        <g:select class="form-control" id="birthdateAcademicYear" name='birthdateAcademicYear' tabindex="1"
                                  from='${academicYearList}'
                                  optionKey="key" optionValue="value"/>
                    </div>
                    <div class="form-group">
                        <g:select class=" form-control" id="birthdateClassName" name='birthdateClassName' tabindex="2"
                                  noSelection="${['': 'All Class...']}"
                                  from='${classNameList}'
                                  optionKey="id" optionValue="name"/>
                    </div>
                    <div class="form-group">
                        <select name="birthdateSection" id="birthdateSection" class="form-control" tabindex="3">
                            <option value="">All Section</option>
                        </select>
                    </div>
                    <div class="form-group" id="data_range">
                            <div class="input-daterange input-group" id="datepicker">
                                <span class="input-group-addon">From</span>
                                <g:textField class="input-sm form-control" id="startDate" name="startDate"
                                             tabindex="2" placeholder="Start Date" required="required"/>
                                <span class="input-group-addon">to</span>
                                <g:textField class="input-sm form-control" id="endDate" name="endDate"
                                             tabindex="3" placeholder="End Date" required="required"/>
                            </div>
                    </div>
                    <div class="form-group">
                        <g:select class="form-control" id="birthdatePrintOptionType" name='birthdatePrintOptionType'
                                  from='${com.grailslab.enums.PrintOptionType.values()}'
                                  optionKey="key" optionValue="value"/>
                    </div>
                    <button type="button" id="birthdateReportBtn" class="btn btn-primary">Show Report</button>
                </form>
            </div>
        </section>
    </div>
</div>

<script>

    var reportParam, startDate,endDate,academicYear,className, studentStatus ,contact,gender,religion,section,student,printOptionType,discount,reportSortType,reportType;
    jQuery(function ($) {
//        Student Report Start
        $('#studentAcademicYear').on('change', function (e) {
            $('#studentClassName').val("").trigger("change");
        });
        $('#studentClassName').on('change', function (e) {
            academicYear = $('#studentAcademicYear').find("option:selected").val();
            className = $('#studentClassName').find("option:selected").val();
            loadSectionOnChange(academicYear, className, 'studentSection');
        });
        $('#studentReportBtn').click(function (e) {
            e.preventDefault();
            academicYear=$('#studentAcademicYear').find("option:selected").val();
            className = $('#studentClassName').find("option:selected").val();
            section = $('#studentSection').find("option:selected").val();
            studentStatus = $('#studentStatus').find("option:selected").val();
            printOptionType = $('#studentPrintOptionType').find("option:selected").val();
            reportParam ="${g.createLink(controller: 'studentReport',action: 'studentList','_blank')}?academicYear="+academicYear+"&className="+className+"&section="+section+"&studentStatus="+studentStatus+"&printOptionType="+printOptionType;
            window.open(reportParam);
        });

        $('#studentSingleAcademicYear').on('change', function (e) {
            $('#singleClassName').val("").trigger("change");
        });
        $('#singleClassName').on('change', function (e) {
            academicYear = $('#studentSingleAcademicYear').find("option:selected").val();
            className = $('#singleClassName').find("option:selected").val();
            loadSectionOnChange(academicYear, className, 'singleSection');
        });
        $('#singleReportBtn').click(function (e) {
            e.preventDefault();
            academicYear=$('#studentSingleAcademicYear').find("option:selected").val();
            className = $('#singleClassName').find("option:selected").val();
            section = $('#singleSection').find("option:selected").val();
            studentStatus = $('#singleStatus').find("option:selected").val();
            printOptionType = $('#singlePrintOptionType').find("option:selected").val();
            reportParam ="${g.createLink(controller: 'studentReport',action: 'studentSingleList','_blank')}?academicYear="+academicYear+"&className="+className+"&section="+section+"&studentStatus="+studentStatus+"&printOptionType="+printOptionType;
            window.open(reportParam);
        });

//        Student report end
//        Student Contact Info Start
        $('#contactAcademicYear').on('change', function (e) {
            $('#contactClassName').val("").trigger("change");
        });
        $('#contactClassName').on('change', function (e) {
            academicYear = $('#contactAcademicYear').val();
            className = $('#contactClassName').val();
            loadSectionOnChange(academicYear, className, 'contactSection');
        });
        $('#contactReportBtn').click(function (e) {
            e.preventDefault();
            academicYear=$('#contactAcademicYear').val();
            className = $('#contactClassName').val();
            section = $('#contactSection').val();
            reportType = $('#cReportType').val();
            printOptionType = $('#contactPrintOptionType').val();
            reportParam ="${g.createLink(controller: 'studentReport',action: 'studentContactList','_blank')}?academicYear="+academicYear+"&className="+className+"&section="+section+"&reportType="+reportType+"&printOptionType="+printOptionType;
            window.open(reportParam);
        });

        $('#genRelegionAcademicYear').on('change', function (e) {
            $('#genRelegionClassName').val("").trigger("change");
        });

        $('#genRelegionClassName').on('change', function (e) {
            academicYear = $('#genRelegionAcademicYear').find("option:selected").val();
            className = $('#genRelegionClassName').find("option:selected").val();
            loadSectionOnChange(academicYear, className, 'genRelegionSection');
        });
        $('#genRelegionReportBtn').click(function (e) {
            e.preventDefault();
            academicYear=$('#genRelegionAcademicYear').find("option:selected").val();
            className = $('#genRelegionClassName').find("option:selected").val();
            section = $('#genRelegionSection').find("option:selected").val();
            gender = $('#gender').find("option:selected").val();
            religion = $('#religion').find("option:selected").val();
            printOptionType = $('#genderRelegionPrintOptionType').find("option:selected").val();
            reportParam ="${g.createLink(controller: 'studentReport',action: 'studentDynamicList','_blank')}?academicYear="+academicYear+"&className="+className+"&section="+section+"&gender="+gender+"&religion="+religion+"&printOptionType="+printOptionType;
            window.open(reportParam);
        });
        $('#birthdateAcademicYear').on('change', function (e) {
            $('#birthdateClassName').val("").trigger("change");
        });
        $('#birthdateClassName').on('change', function (e) {
            academicYear = $('#birthdateAcademicYear').find("option:selected").val();
            className = $('#birthdateClassName').find("option:selected").val();
            loadSectionOnChange(academicYear, className, 'birthdateSection');
        });
        $('#birthdateReportBtn').click(function (e) {
            e.preventDefault();
            academicYear=$('#birthdateAcademicYear').find("option:selected").val();
            className = $('#birthdateClassName').find("option:selected").val();
            section = $('#birthdateSection').find("option:selected").val();
            startDate = $('#startDate').val();
            endDate = $('#endDate').val();
            printOptionType = $('#birthdatePrintOptionType').find("option:selected").val();
            reportParam ="${g.createLink(controller: 'studentReport',action: 'studentBirthDayList','_blank')}?academicYear="+academicYear+"&className="+className+"&section="+section+"&printOptionType="+printOptionType+"&startDate="+startDate+"&endDate="+endDate;
            window.open(reportParam);
        });
        // Student birthdate end
        $('#registrationClassName').on('change', function (e) {
            var classNameOnchange = $('#registrationClassName').val();
            loadSectionOnClassChange(classNameOnchange, 'registrationSection');
        });

        $('#data_range .input-daterange').datepicker({
            keyboardNavigation: false,
            todayBtn:true,
            format: 'mm-dd',
            forceParse: false,
            autoclose: true
        });

        viewHrefUrl = "${g.createLink(controller: 'student',action: 'view')}/";
        $('#registration').select2({
            placeholder: "Search for a Student [studentId/name/Father Name/mobile",
            allowClear: true,
            minimumInputLength:3,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: "${g.createLink(controller: 'student',action: 'registrationTypeAheadList')}",
                dataType: 'json',
                quietMillis: 250,
                data: function (term, page) {
                    return {
                        q: term, // search term
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
        function repoFormatResult(repo) {
            var markup = '<div class="row-fluid">' +
                    '<div class="span12">' + repo.name + '</div>' +
                    '</div>';
            return markup;
        }

        function repoFormatSelection(repo) {
            return repo.name;
        }
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
    function loadSectionOnClassChange(classNameOnchange, controlName) {
        jQuery.ajax({
            type: 'POST',
            dataType: 'JSON',
            url: "${g.createLink(controller: 'remote',action: 'listSection')}?className="+classNameOnchange,
            success: function (data, textStatus) {
                if (data.isError == false) {
                    var $select = $('#'+controlName);
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
</script>

<script>

    var academicYear,examTerm,section,student,printOptionType;
    jQuery(function ($) {

        function loadSprSection(){
            academicYear=$('#academicYear').find("option:selected").val();
            examTerm =$('#examTerm').find("option:selected").val();
            if(academicYear!="" && examTerm!=""){
                loadSection(academicYear,$('#section'),"#section-progress-report-holder");
            }
            $('#section-progress-report-btn').removeClass('btn-primary').addClass('btn-default');
        }

        $('#section').change(function () {
            var optionSelected = $(this).find("option:selected");
            section  = optionSelected.val();
            if(section == undefined || section == ""){
                $('#section-progress-report-btn').removeClass('btn-primary').addClass('btn-default');
            }else{
                $('#section-progress-report-btn').removeClass('btn-default').addClass('btn-primary');
            }
        });
        $('#section-progress-report-btn').click(function (e) {
            /*academicYear=$('#academicYear').find("option:selected").val();
            examTerm =$('#examTerm').find("option:selected").val();
            section = $('#section').find("option:selected").val();
            printOptionType = $('#sectionProPrintOptionType').find("option:selected").val();
            if((academicYear != "") && (examTerm != "") && (section != "")){
                %{--var sectionParam ="${g.createLink(controller: 'examReport',action: 'sectionReportCard','_blank')}?academicYear="+academicYear+"&examTerm="+examTerm+"&section="+section+"&printOptionType="+printOptionType;--}%
                window.open(sectionParam);
            }*/
            e.preventDefault();
        });

    });

</script>

</body>
</html>