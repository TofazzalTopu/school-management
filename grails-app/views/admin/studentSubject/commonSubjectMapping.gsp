<html>
<head>
    <title>Map Student Subject</title>
    <meta name="layout" content="moduleStdMgmtLayout"/>
</head>
<body>
<grailslab:breadCrumbActions firstBreadCrumbUrl="${g.createLink(controller: 'studentSubjects',action: 'index')}" firstBreadCrumbText="Class List" breadCrumbTitleText="Common Subject Mapping" SHOW_LINK_BTN="YES" linkBtnText="Back"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading" id="selectionFilterHolder">
                <div class="form-horizontal">
                    <div class="form-group">
                        <div class="col-md-3">
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
                                        <th>Name</th>
                                        <th>STD ID</th>
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
                        <div class="col-md-4">
                            <select id="subjectName" name='subjectName' class="form-control" ></select>
                        </div>
                        <div class="col-md-3" id="selectOptionalHolder">
                            <select name="isOptional" id="isOptional" class="form-control" tabindex="1">
                                <option value="0">Compulsory</option>
                                <option value="1">Optional</option>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <select name="added" id="added" class="form-control" tabindex="1">
                                <option value="1">Add</option>
                                <option value="0">Remove</option>
                            </select>
                        </div>
                        <div class="col-md-1">
                            <button class="btn btn-primary" type="submit" id="submit-fees-btn">Add Subject</button>
                        </div>
                    </div>
                </form>
            </div>
        </section>
    </div>
</div>
<script>
    var className, sectionName, gender, religion, subjectName, subjectVal, added;
    jQuery(function ($) {
        $("#subjectMappingForm").submit(function(e) {
            subjectVal = $('#subjectName').val();
            subjectName = $('#subjectName').find("option:selected").text();
            added = $('#added').val() === '1'? 'Add':'Remove';
            var confirmStr = "You are attempting to "+added+" "+subjectName+" to all selected Student." +
                    "  \n\nClick 'OK to confirm, or click 'Cancel' to stop this action.";
            if (subjectVal && confirm(confirmStr)) {
                showLoading("#student-list-holder");
                $.ajax({
                    type: "POST",
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'studentSubjects',action: 'saveCommonMapping')}",
                    data: $("#subjectMappingForm").serialize(), // serializes the form's elements.
                    success: function(data)
                    {
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
            className = $('#className').val();
            sectionName = $('#sectionName').val();
            gender = $('#gender').val();
            religion = $('#religion').val();
            $("#student-list-holder").hide(500);
            if(className != ""){
                showLoading("#selectionFilterHolder");
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'studentSubjects',action: 'loadStudentCommonMapping')}?className=" + className+"&sectionName=" + sectionName+"&gender=" + gender+"&religion=" + religion,
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
                             if (data.allowOptionalSubject === true) {
                                 $('#selectOptionalHolder').show();
                             } else {
                                 $('#selectOptionalHolder').hide();
                             }
                            $("#list-table").find("tr:gt(0)").empty();
                            $.each(data.studentList, function (key, value) {
                                $('#list-table').append('<tr><td>' + value.serial +'</td><td>' + value.studentName +'</td><td>' + value.studentid +'</td><td>' + value.rollNo +'</td><td>' + value.selectedSubjects +'</td><td><input name="selectedStdId" value="'+ value.id+'" type="checkbox" checked></td></tr>');
                            });
                            $("#student-list-holder").show(500);
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            }
            e.preventDefault();
        });
        $('#className').on('change', function (e) {
            className = $('#className').find("option:selected").val();
            loadSectionOnClassChange(className, 'sectionName');
        });
        $('.link-url-btn').click(function (e) {
            window.location.href = "${createLink(controller: 'studentSubjects', action: 'index')}";
            e.preventDefault();
        });
    });
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
</body>
</html>


