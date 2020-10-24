
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="adminLayout"/>
    <title>Class Routine</title>
</head>

<body>
<grailslab:breadCrumbActions firstBreadCrumbUrl="${g.createLink(controller: 'login', action: 'loginSuccess')}" firstBreadCrumbText="Dashboard" breadCrumbTitleText="${classRoutine ? 'Edit Routine': 'Create Routine'}"/>
<div class="row" >
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Manage Routine
            </header>
            <div class="panel-body">
                <div class="row" id="class-routine-holder">
                    <form class="form-horizontal" role="form" id="create-form">
                        <g:hiddenField name="employee" id="employee" value="${employeeId}"/>
                        <g:hiddenField name="id" id="id" value="${classRoutine?.id}"/>
                        <div class="col-md-12">
                            <div class="form-group">
                                <label for="academicYear" class="col-md-1 control-label">Year</label>
                                <div class="col-md-3">
                                    <g:select tabindex="1" class="form-control academic-year-step"
                                              id="academicYear" name='academicYear'
                                              noSelection="${['': 'Select Year...']}"
                                              from='${academicYearList}'
                                              optionKey="key" optionValue="value" value="${classRoutine?.academicYear}"></g:select>
                                </div>
                                <label for="className" class="col-md-1 control-label">Class</label>
                                <div class="col-md-3">
                                    <g:select class=" form-control class-name-step" id="className" name='className'
                                              tabindex="2"
                                              noSelection="${['': 'Select Class...']}"
                                              from='${classNameList}'
                                              optionKey="id" optionValue="name" value="${classRoutine?.className?.id}"></g:select>
                                </div>
                                <label for="section" class="col-md-1 control-label">Section</label>
                                <div class="col-md-3">
                                    <select class="form-control section-name-step" id="section" name='section'>
                                        <g:if test="${sectionList}">
                                            <g:each in="${sectionList}" var="oldSection">
                                                <option  value="${oldSection.id}" ${classRoutine.section.id == oldSection.id ? 'selected': ''}>${oldSection.name}</option>
                                            </g:each>
                                        </g:if>
                                        <g:else>
                                            <option  value="">Select Section</option>
                                        </g:else>
                                    </select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="subjectName" class="col-md-1 control-label">Period</label>
                                <div class="col-md-2">
                                    <select tabindex="1" class="form-control academic-year-step" id="classPeriod" name="classPeriod">
                                        <option value="1st Period" ${classRoutine && classRoutine.classPeriod == '1st Period' ? 'selected': ''}>1st Period</option>
                                        <option value="2nd Period" ${classRoutine && classRoutine.classPeriod == '2nd Period' ? 'selected': ''}>2nd Period</option>
                                        <option value="3rd Period" ${classRoutine && classRoutine.classPeriod == '3rd Period' ? 'selected': ''}>3rd Period</option>
                                        <option value="4th Period" ${classRoutine && classRoutine.classPeriod == '4th Period' ? 'selected': ''}>4th Period</option>
                                        <option value="5th Period" ${classRoutine && classRoutine.classPeriod == '5th Period' ? 'selected': ''}>5th Period</option>
                                        <option value="6th Period" ${classRoutine && classRoutine.classPeriod == '6th Period' ? 'selected': ''}>6th Period</option>
                                        <option value="7th Period" ${classRoutine && classRoutine.classPeriod == '7th Period' ? 'selected': ''}>7th Period</option>
                                        <option value="8th Period" ${classRoutine && classRoutine.classPeriod == '8th Period' ? 'selected': ''}>8th Period</option>
                                        <option value="9th Period" ${classRoutine && classRoutine.classPeriod == '9th Period' ? 'selected': ''}>9th Period</option>
                                        <option value="10th Period" ${classRoutine && classRoutine.classPeriod == '10th Period' ? 'selected': ''}>10th Period</option>
                                        <option value="Tiffin" ${classRoutine && classRoutine.classPeriod == 'Tiffin' ? 'selected': ''}>Tiffin Period</option>
                                    </select>
                                </div>
                                <label for="startOn" class="col-md-1 control-label">From</label>
                                <div class="col-md-2">
                                    <g:textField id="startOn" class="form-control timepicker" value="${classRoutine?.startOn}" name="startOn" required="required"/>
                                </div>

                                <label for="duration" class="col-md-1 control-label">Duration</label>
                                <div class="col-md-1">
                                    <input type="number" value="${classRoutine? classRoutine.duration : '40'}" min="5" max="180" step="1" name="duration" id="duration" class="form-control" required>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="subjectName" class="col-md-1 control-label">Subject</label>
                                <div class="col-md-2">
                                    <select class="form-control subject-name-step" id="subjectName" name='subjectName'>
                                        <g:if test="${subjectList}">
                                            <g:each in="${subjectList}" var="oldSubject">
                                                <option value="${oldSubject.id}" ${classRoutine.subjectName.id == oldSubject.id ? 'selected': ''}>${oldSubject.name}</option>
                                            </g:each>
                                        </g:if>
                                        <g:else>
                                            <option  value="">Select Subject</option>
                                        </g:else>
                                    </select>
                                </div>
                                <label for="days" class="col-md-1 control-label">Days</label>
                                <div class="col-md-6">
                                    <span><label class="checkbox-inline">
                                        <g:checkBox checked="${classRoutine ? classRoutine.days?.contains('SAT'): 'false'}" value="SAT" name="days" id="saturdayBox" />
                                        Saturday
                                    </label></span>
                                    <span><label class="checkbox-inline">
                                        <g:checkBox checked="${classRoutine ? classRoutine.days?.contains('SUN'): 'false'}" value="SUN" name="days" id="sundayBox" />
                                        Sunday
                                    </label></span>
                                    <span><label class="checkbox-inline">
                                        <g:checkBox checked="${classRoutine ? classRoutine.days?.contains('MON'): 'false'}" value="MON" name="days" id="mondayBox" />
                                        Monday
                                    </label></span>
                                    <span><label class="checkbox-inline">
                                        <g:checkBox checked="${classRoutine ? classRoutine.days?.contains('TUE'): 'false'}" value="TUE" name="days" id="tuesdayBox" />
                                        Tuesday
                                    </label></span>
                                    <span><label class="checkbox-inline">
                                        <g:checkBox checked="${classRoutine ? classRoutine.days?.contains('WED'): 'false'}" value="WED" name="days" id="wednesdayBox" />
                                        Wednesday
                                    </label></span>
                                    <span><label class="checkbox-inline">
                                        <g:checkBox checked="${classRoutine ? classRoutine.days?.contains('THU'): 'false'}" value="THU" name="days" id="thursdayBox" />
                                        Thursday
                                    </label></span>
                                    <span><label class="checkbox-inline">
                                        <g:checkBox checked="${classRoutine ? classRoutine.days?.contains('FRI'): 'false'}" value="FRI" name="days" id="fridayBox" />
                                        Friday
                                    </label></span>
                                </div>
                                <div class="col-md-2">
                                    <button class="btn btn-primary" tabindex="7" id="form-submit-btn" type="submit">${classRoutine ? 'Update' : 'Save'}</button>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </section>
    </div>
</div>
<div class="row" id="routine-list-holder">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Routine List
            </header>
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                            <tr>
                                <th>Week Days</th>
                                <th>Period</th>
                                <th>Time</th>
                                <th>Subject</th>
                                <th>Section</th>
                                <th>Class</th>
                                <th>Teacher</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                        </tbody>
                    </table>
                </div>
            </div>
        </section>
    </div>
</div>

<script>
    var teacherRoutineTable,academicYear, className, subject, remoteUrl, section, routineCreateUrl;
    jQuery(function ($) {
        routineCreateUrl = "${g.createLink(controller: 'teacher',action: 'createRoutine')}?id=";
        $('.timepicker').timepicker({
            defaultTime: '08:00 AM',
            template: false,
            showInputs: false,
            minuteStep: 5
        });
        $('#class-routine-holder').cascadingDropdown({
            selectBoxes: [
                {
                    selector: '.academic-year-step',
                    onChange: function(value){
                        loadSectionNames();
                    }
                },
                {
                    selector: '.class-name-step',
                    requires: ['.academic-year-step'],
                    onChange: function (value) {
                        loadSectionNames();
                    }
                },
                {
                    selector: '.section-name-step',
                    requires: ['.class-name-step'],
                    onChange: function (value) {
                        loadSubjectNames();
                    }
                },
                {
                    selector: '.subject-name-step',
                    requires: ['.section-name-step'],
                }
            ]
        });
        $("#create-form").submit(function(e) {
            e.preventDefault();
            showLoading("#create-form");
            $.ajax({
                url: "${createLink(controller: 'teacher', action: 'saveRoutine')}",
                type: 'post',
                dataType: "json",
                data: $("#create-form").serialize(),
                success: function (data) {
                    // hideLoading("#create-form");
                    if (data.isError == false) {
                        if (data.isEdit == true) {
                            window.location.href = "${g.createLink(controller: 'teacher',action: 'createRoutine')}";
                        } else {
                            $("#className").val("");
                            showSuccessMsg(data.message);
                            teacherRoutineTable.draw(false);
                        }
                    } else {
                        showErrorMsg(data.message);
                    }
                },
                failure: function (data) {
                }
            })
            return false;
        });

        teacherRoutineTable = $('#list-table').DataTable({
            "sDom": "<'row'<'col-md-3'><'col-md-3'><'col-md-6'f>r>t<'row'<'col-md-3'l><'col-md-3'i><'col-md-6'p>>",
            "bAutoWidth": true,
            "scrollX": false,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'asc'],
            "sAjaxSource": "${g.createLink(controller: 'teacher',action: 'listRoutine')}",
            "fnServerParams": function (aoData) {
                aoData.push({"name": "academicYear", "value": $('#academicYear').val()});
            },
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(7)', nRow).html(getActionButtons(nRow, aData));
                return nRow;
            },

            "aoColumns": [
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                {"bSortable": false}
            ]
        });
        $('#className').change(function () {
            teacherRoutineTable.draw(false);
        });
        $('#section').change(function () {
            teacherRoutineTable.draw(false);
        });

        $('#list-table').on('click', 'a.delete-reference', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'teacher',action: 'deleteRoutine')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            showSuccessMsg(data.message);
                            teacherRoutineTable.draw(false);
                        } else {
                            showErrorMsg(data.message);
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            }
            e.preventDefault();
        });


        function loadSectionNames(){
            academicYear =$('#academicYear').val();
            className =$('#className').val();
            if(academicYear && className){
                remoteUrl = "${g.createLink(controller: 'remote',action: 'classSectionList')}?className="+className+"&academicYear="+academicYear;
                loadClassSection(remoteUrl, $('#section'),"#class-routine-holder");
            }
        }
        function loadSubjectNames(){
            className =$('#className').val();
            section =$('#section').val();
            if(className && section){
                remoteUrl = "${g.createLink(controller: 'remote',action: 'sectionSubjectList')}?id="+section;
                loadSectionSubject(remoteUrl, className, section, $('#subjectName'),"#class-routine-holder");
            }
        }
        function getActionButtons(nRow, aData) {
            var actionButtons = "";
            actionButtons += '<span class="col-md-3 no-padding"><a target="_self" href="' + routineCreateUrl + aData.DT_RowId + '" class="routine-edit-reference" title="Edit Routine">';
            actionButtons += '<span class="fa fa-edit"></span>';
            actionButtons += '</a></span>';
            actionButtons += '<span class="col-md-3 no-padding"><a href="javascript:void(0)" referenceId="' + aData.DT_RowId + '" class="delete-reference" title="Delete">';
            actionButtons += '<span class="fa fa-trash"></span></a></span>';
            return actionButtons;
        }
    });
</script>
</body>
</html>
