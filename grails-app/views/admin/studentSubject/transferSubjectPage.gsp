<html>
<head>
    <title>Map Student Subject</title>
    <meta name="layout" content="moduleStdMgmtLayout"/>
</head>
<body>
<grailslab:breadCrumbActions firstBreadCrumbUrl="${g.createLink(controller: 'studentSubjects',action: 'index')}" firstBreadCrumbText="Class List" breadCrumbTitleText="Students Subject Transfer" SHOW_LINK_BTN="NO" linkBtnText="Back"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading" id="selectionFilterHolder">
                <div class="form-horizontal">
                    <div class="form-group">
                        <div class="col-md-2">
                            <g:select tabindex="1" class="form-control"
                                      id="academicYear" name='academicYear'
                                      noSelection="${['': 'Select Year...']}"
                                      from='${academicYearList}'
                                      optionKey="key" optionValue="value"></g:select>
                        </div>
                        <div class="col-md-2">
                            <g:select class=" form-control" id="className" name='className' tabindex="1"
                                      noSelection="${['': 'Select Class']}"
                                      from='${classNameList}' value="${className?.id}"
                                      optionKey="id" optionValue="name"></g:select>
                        </div>
                        <div class="col-md-2">
                            <g:select class=" form-control" id="sectionName" name='sectionName' tabindex="1"
                                      noSelection="${['': 'All Section']}"
                                      from='${sectionList}'
                                      optionKey="id" optionValue="name"></g:select>
                        </div>
                        <div class="col-md-2">
                            <g:select class="form-control" id="gender" name='gender' tabindex="8"
                                      noSelection="${['': 'All Gender']}"
                                      from='${com.grailslab.enums.Gender.values()}'
                                      optionKey="key" optionValue="value"></g:select>
                        </div>
                        <div class="col-md-2">
                            <g:select class="form-control" id="religion" name='religion' tabindex="4"
                                      noSelection="${['': 'All Religion']}"
                                      from='${com.grailslab.enums.Religion.values()}'
                                      optionKey="key" optionValue="value"></g:select>
                        </div>

                        <div class="col-md-2">
                            <button class="btn btn-info" id="load-btn">Load Student</button>
                        </div>
                    </div>
                </div>
            </header>
            <div class="panel-body" id="student-list-holder" style="display: none;">
                <form class="cmxform form-horizontal" id="subjectMappingForm">
                    <div class="col-md-12">
                        <div class="table-responsive">
                            <table class="table table-striped table-hover table-bordered" id="list-table">
                                <thead>
                                    <tr>
                                        <th>SL</th>
                                        <th>STD ID</th>
                                        <th>Name</th>
                                        <th>Roll No</th>
                                        <th>Selected Subject(s)</th>
                                        <th>Action</th>
                                    </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="form-group col-md-12 pull-right">
                        <div class="col-md-1">
                            <button class="btn btn-primary" type="submit" id="submit-fees-btn">Transfer Subject</button>
                        </div>
                    </div>
                </form>
            </div>
        </section>
    </div>
</div>
<script>
    var className, sectionName, gender, religion, subjectName, academicYear, added;
    jQuery(function ($) {
        $("#subjectMappingForm").submit(function(e) {
            academicYear = $('#academicYear').val();
           // subjectName = $('#subjectName').find("option:selected").text();
            var confirmStr = "You are attempting to transfer Subject to all selected Student." +
                    "  \n\nClick 'OK to confirm, or click 'Cancel' to stop this action.";
            if (academicYear && confirm(confirmStr)) {
                showLoading("#student-list-holder");
                $.ajax({
                    type: "POST",
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'transferSubjects',action: 'saveStuTransferedSubject')}?academicYear=" + academicYear,
                    data: $("#subjectMappingForm").serialize(), // serializes the form's elements.
                    success: function (data) {
                        hideLoading("#student-list-holder");
                        if (data.isError === true) {
                            showErrorMsg(data.message);
                        } else {
                            showSuccessMsg(data.message);
                            $("#student-list-holder").hide(500);
                        }
                    }
                });
            }
            e.preventDefault(); // avoid to execute the actual submit of the form.
        });

        $('#load-btn').click(function (e) {
            academicYr = $('#academicYear').val();
            className = $('#className').val();
            sectionName = $('#sectionName').val();
            gender = $('#gender').val();
            religion = $('#religion').val();
            $("#student-list-holder").hide(500);
            if(academicYr && className && sectionName){
                showLoading("#selectionFilterHolder");
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'transferSubjects',action: 'loadSubjects')}?className=" + className+"&academicYear=" + academicYr+"&sectionName=" + sectionName+"&gender=" + gender+"&religion=" + religion,
                    success: function (data) {
                        hideLoading("#selectionFilterHolder");
                        if (data.isError === true) {
                            showErrorMsg(data.message);
                        } else {
                            var $selectSub = $('#subjectName');
                            $selectSub.find('option').remove();
                            $selectSub.append('<option value="">Select Subject</option>');
                            $.each(data.optComOptions, function (key, value) {
                                $selectSub.append('<option value=' + value.id + '>' + value.name + '</option>');
                            });
                            $("#list-table").find("tr:gt(0)").empty();
                            $.each(data.studentList, function (key, value) {
                                $('#list-table').append('<tr><td>' + value.serial +'</td><td>' +value.studentId +'</td><td>' + value.name  +'</td><td>' + value.rollNo +'</td><td>' + value.subjects +'</td><td><input name="selectedStdId" value="'+ value.id+'" type="checkbox" checked></td></tr>');
                            });
                            $("#student-list-holder").show(500);
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            } else {
                alert("Please select Year, Class name and Section to transfer subjects.")
            }
            e.preventDefault();
        });
        $('#academicYear').on('change', function (e) {
            academicYr = $('#academicYear').val();
            className =$('#className').val();
            sectionName =$('#sectionName').val();
            if (className && sectionName) {
                $('#className').val("").trigger("change");
                $('#sectionName').val("").trigger("change");
            }
        });
        $('#className').on('change', function (e) {
            academicYr = $('#academicYear').val();
            className =$('#className').val();
            if (academicYr && className) {
                classUrl = "${g.createLink(controller: 'remote',action: 'listSection')}?className="+className+"&academicYear="+academicYr;
                loadClassSection(classUrl, '#sectionName', "#stu-manage-report-holder")
            }
            $('#sectionName').val("").trigger("change");
        });
        $('.link-url-btn').click(function (e) {
            window.location.href = "${createLink(controller: 'studentSubjects', action: 'index')}";
            e.preventDefault();
        });
    });
</script>
</body>
</html>


