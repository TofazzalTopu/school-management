
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleStdMgmtLayout"/>
    <title>Teacher Routine</title>
</head>

<body>
<grailslab:breadCrumbActions firstBreadCrumbUrl="${g.createLink(controller: 'classRoutine', action: 'teacherRoutine')}" firstBreadCrumbText="Teacher Routine" breadCrumbTitleText="Add/Edit Teacher Routine"/>
<div class="row" >
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Teacher Routine
            </header>
            <div class="panel-body">
                <div class="row" id="class-routine-holder">
                    <form class="form-horizontal" role="form" id="create-form">
                        <div class="col-md-12">
                            <div class="form-group">
                                <label for="employeeId" class="col-md-1 control-label">Teacher  <span class="required"></span></label>
                                <div class="col-md-3">
                                    <input type="hidden" class="form-control"  id="employeeId" name="employee" tabindex="2" />
                                </div>
                                <label for="academicYear" class="col-md-1 control-label">Year</label>
                                <div class="col-md-3">
                                    <g:select tabindex="1" class="form-control academic-year-step"
                                              id="academicYear" name='academicYear'
                                              noSelection="${['': 'Select Year...']}"
                                              from='${academicYearList}'
                                              optionKey="key" optionValue="value"></g:select>
                                </div>
                                <label for="className" class="col-md-1 control-label">Class</label>
                                <div class="col-md-3">
                                    <g:select class=" form-control class-name-step" id="className" name='className'
                                              tabindex="2"
                                              noSelection="${['': 'Select Class...']}"
                                              from='${classNameList}'
                                              optionKey="id" optionValue="name"></g:select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="subjectName" class="col-md-1 control-label">Period</label>
                                <div class="col-md-2">
                                    <select tabindex="1" class="form-control academic-year-step" id="classPeriod" name="classPeriod">
                                        <option value="1st Period">1st Period</option>
                                        <option value="2nd Period">2nd Period</option>
                                        <option value="3rd Period">3rd Period</option>
                                        <option value="4th Period">4th Period</option>
                                        <option value="5th Period">5th Period</option>
                                        <option value="6th Period">6th Period</option>
                                        <option value="7th Period">7th Period</option>
                                        <option value="8th Period">8th Period</option>
                                        <option value="9th Period">9th Period</option>
                                        <option value="10th Period">10th Period</option>
                                        <option value="Tiffin">Tiffin Period</option>
                                    </select>
                                </div>
                                <label for="startOn" class="col-md-1 control-label">From</label>
                                <div class="col-md-2">
                                    <g:textField id="startOn" class="form-control timepicker" name="startOn" required="required"/>
                                </div>

                                <label for="duration" class="col-md-1 control-label">Duration</label>
                                <div class="col-md-1">
                                    <input type="number" value="40" min="5" max="180" step="1" name="duration" id="duration" class="form-control" required>
                                </div>
                                <label for="section" class="col-md-1 control-label">Section</label>
                                <div class="col-md-3">
                                    <select class="form-control section-name-step" id="section" name='section'>
                                        <option  value="">Select Section</option>
                                    </select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="subjectName" class="col-md-1 control-label">Subject</label>
                                <div class="col-md-2">
                                    <select class="form-control subject-name-step" id="subjectName" name='subjectName'>
                                        <option  value="">Select Subject</option>
                                    </select>
                                </div>
                                <label for="days" class="col-md-1 control-label">Days</label>
                                <div class="col-md-6">
                                    <span><label class="checkbox-inline">
                                        <g:checkBox checked="false" value="SAT" name="days" id="saturdayBox" />
                                        Saturday
                                    </label></span>
                                    <span><label class="checkbox-inline">
                                        <g:checkBox checked="false" value="SUN" name="days" id="sundayBox" />
                                        Sunday
                                    </label></span>
                                    <span><label class="checkbox-inline">
                                        <g:checkBox checked="false" value="MON" name="days" id="mondayBox" />
                                        Monday
                                    </label></span>
                                    <span><label class="checkbox-inline">
                                        <g:checkBox checked="false" value="TUE" name="days" id="tuesdayBox" />
                                        Tuesday
                                    </label></span>
                                    <span><label class="checkbox-inline">
                                        <g:checkBox checked="false" value="WED" name="days" id="wednesdayBox" />
                                        Wednesday
                                    </label></span>
                                    <span><label class="checkbox-inline">
                                        <g:checkBox checked="false" value="THU" name="days" id="thursdayBox" />
                                        Thursday
                                    </label></span>
                                    <span><label class="checkbox-inline">
                                        <g:checkBox checked="false" value="FRI" name="days" id="fridayBox" />
                                        Friday
                                    </label></span>
                                </div>
                                <div class="col-md-2">
                                    <button class="btn btn-primary" tabindex="7" id="form-submit-btn" type="submit">Save</button>
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
                Teacher Routine List
            </header>
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                            <tr>
                                <th>Period</th>
                                <th>Time</th>
                                <th>Subject</th>
                                <th>Week Days</th>
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
    var teacherRoutineTable,academicYear, className, subject, remoteUrl, section;
    jQuery(function ($) {
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

        $('#employeeId').select2({
            placeholder: "Search for [employeeId/name]",
            allowClear: true,
            minimumInputLength:3,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: "${g.createLink(controller: 'remote',action: 'employeeList')}",
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
        $("#create-form").submit(function(e) {
            e.preventDefault();
            $.ajax({
                url: "${createLink(controller: 'classRoutine', action: 'save')}",
                type: 'post',
                dataType: "json",
                data: $("#create-form").serialize(),
                success: function (data) {
                    if (data.isError == false) {
                        teacherRoutineTable.draw(false);
                        $("#section").val("");
                        $("#className").val("");
                        $("#classPeriod").val("");
                        showSuccessMsg(data.message);
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
            "sAjaxSource": "${g.createLink(controller: 'classRoutine',action: 'list')}",
            "fnServerParams": function (aoData) {
                aoData.push({"name": "routineType", "value": "teacherRoutine"});
                aoData.push({"name": "employeeId", "value": $('#employeeId').val()});
                aoData.push({"name": "academicYear", "value": $('#academicYear').val()});
            },
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(7)', nRow).html(getGridActionBtns(nRow, aData, 'delete'));
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

        $('#employeeId').change(function () {
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
                    url: "${g.createLink(controller: 'classRoutine',action: 'delete')}?id=" + referenceId,
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
    });
</script>
</body>
</html>
