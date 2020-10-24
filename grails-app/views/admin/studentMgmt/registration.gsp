<%@ page import="com.grailslab.enums.Religion; com.grailslab.settings.Profession" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleStdMgmtLayout"/>
    <title>Student Registration</title>
</head>

<body>
<sec:ifAnyGranted roles="ROLE_SUPER_ADMIN,ROLE_ADMIN">
    <grailslab:breadCrumbActions breadCrumbTitleText="Student Registration" SHOW_CREATE_BTN="YES"
                                 createButtonText="New Registration"/>
</sec:ifAnyGranted>
<sec:ifNotGranted roles="ROLE_SUPER_ADMIN,ROLE_ADMIN">
    <grailslab:breadCrumbActions breadCrumbTitleText="Student Registration"/>
</sec:ifNotGranted>
<div class="row" id="create-form-holder" style="display: none;">
    <div class="col-sm-12">
        <section class="panel">
            <div class="panel-body">
                <form class="cmxform form-horizontal" id="create-form">
                    <g:hiddenField name="id"/>
                    <div class="row">
                        <div class="col-lg-6">
                            <div class="form-group">
                                <label for="studentID" class="col-md-4 control-label">Student ID</label>

                                <div class="col-md-8">
                                    %{--<input class="form-control" type="text" name="studentID" id="studentID"
                                           placeholder="Auto Generate"/>--}%
                                    <input readonly class="form-control" type="text" name="studentID" id="studentID"
                                           placeholder="Auto Generate"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="name" class="col-md-4 control-label">Student Name *</label>

                                <div class="col-md-8">
                                    <input class="form-control" type="text" name="name" id="name" tabindex="1"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="fathersName" class="col-md-4 control-label">Fathers Name *</label>

                                <div class="col-md-8">
                                    <input class="form-control" type="text" name="fathersName" id="fathersName"
                                           tabindex="2"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="fathersProfession" class="col-md-4 control-label">Fathers Profession</label>

                                <div class="col-md-8">
                                    <g:select class="form-control" id="fathersProfession" name='fathersProfession'
                                              tabindex="3"
                                              noSelection="${['': 'Select One...']}"
                                              from='${Profession.list()}'
                                              optionKey="id" optionValue="name"></g:select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="mothersName" class="col-md-4 control-label">Mothers Name *</label>

                                <div class="col-md-8">
                                    <input class="form-control" type="text" name="mothersName" id="mothersName"
                                           tabindex="4"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="mothersProfession" class="col-md-4 control-label">Mothers Profession</label>

                                <div class="col-md-8">
                                    <g:select class="form-control" id="mothersProfession" name='mothersProfession'
                                              tabindex="5"
                                              noSelection="${['': 'Select One...']}"
                                              from='${com.grailslab.settings.Profession.list()}'
                                              optionKey="id" optionValue="name"></g:select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="birthDate" class="col-md-4 control-label">Birth Date *</label>

                                <div class="col-md-8">
                                    <input class="form-control" type="text" name="birthDate" id="birthDate"
                                           placeholder="dd/mm/yyyy"
                                           tabindex="6"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="bloodGroup" class="col-md-4 control-label">Blood Group</label>

                                <div class="col-md-8">
                                    <input class="form-control" type="text" name="bloodGroup" id="bloodGroup"
                                           tabindex="7"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="religion" class="col-md-4 control-label">Religion</label>

                                <div class="col-md-8">
                                    <g:select class="form-control" id="religion" name='religion' tabindex="8"
                                              from='${com.grailslab.enums.Religion.values()}'
                                              optionKey="key" optionValue="value"></g:select>
                                </div>
                            </div>

                        </div>

                        <div class="col-lg-6">
                            <div class="form-group">
                                <label for="Gender" class="col-md-4 control-label">Gender</label>

                                <div class="col-md-8">
                                    <g:select class="form-control" id="gender" name='gender' tabindex="8"
                                              from='${com.grailslab.enums.Gender.values()}'
                                              optionKey="key" optionValue="value"></g:select>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="mobile" class="col-md-4 control-label">Mobile Number *</label>

                                <div class="col-md-8">
                                    <input class="form-control" type="text" name="mobile" id="mobile" tabindex="10"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="fathersName" class="col-md-4 control-label">Card Number</label>

                                <div class="col-md-8">
                                    <input class="form-control" type="text" name="cardNo" id="cardNo" tabindex="9"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="deviceId" class="col-md-4 control-label">Device Enrol ID</label>

                                <div class="col-md-8">
                                    <input class="form-control" type="text" name="deviceId" id="deviceId" tabindex="9"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="email" class="col-md-4 control-label">Email</label>

                                <div class="col-md-8">
                                    <input class="form-control" type="email" name="email" id="email" tabindex="12"/>
                                </div>
                            </div>

                            <div class="form-group">

                                <label for="presentAddress" class="col-md-4 control-label">Present Address *</label>

                                <div class="col-md-8">
                                    <textarea id="presentAddress" name="presentAddress" class="form-control"
                                              tabindex="13"></textarea>
                                </div>
                            </div>

                            <div class="form-group">

                                <label for="permanentAddress" class="col-md-4 control-label">Permanent Address</label>

                                <div class="col-md-8">
                                    <textarea id="permanentAddress" name="permanentAddress" class="form-control"
                                              tabindex="14"></textarea>
                                </div>
                            </div>

                            <div class="form-group">

                                <label for="pImage" class="col-md-4 control-label">Profile Image</label>

                                <div class="col-md-8">
                                    <input type="file" name="pImage" id="pImage"/>
                                </div>
                            </div>

                        </div>

                        <div class="col-sm-12" id="studentAdmissionDiv">
                            <header class="panel-heading" >
                                Student Admission (Optional)
                            </header>
                            <div class="col-lg-6" style="padding-top: 10px;">
                                <div class="form-group">
                                    <label for="academicYear" class="col-md-4 control-label">Academic Year</label>

                                    <div class="col-md-8">
                                        <g:select tabindex="1" class="form-control"
                                                  id="academicYear" name='academicYear'
                                                  noSelection="${['': 'Select Year...']}"
                                                  from='${academicYearList}'
                                                  optionKey="key" optionValue="value"></g:select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="sectionName" class="col-md-4 control-label">Section Name</label>

                                    <div class="col-md-8">
                                        <g:select class=" form-control" id="sectionName" name='sectionName' tabindex="1"
                                                  noSelection="${['': 'All Section']}"
                                                  from='${sectionList}'
                                                  optionKey="id" optionValue="name"></g:select>
                                    </div>
                                </div>
                            </div>
                            <div class="col-lg-6" style="padding-top: 10px;">
                                <div class="form-group">
                                    <label for="className" class="col-md-4 control-label">Class Name</label>

                                    <div class="col-md-8">
                                        <g:select class=" form-control" id="className" name='className' tabindex="1"
                                                  noSelection="${['': 'Select Class']}"
                                                  from='${classNameList}' value="${className?.id}"
                                                  optionKey="id" optionValue="name"></g:select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="rollNo" class="col-md-4 control-label">Roll No</label>

                                    <div class="col-md-8">
                                        <input class="form-control" type="text" name="rollNo" id="rollNo"
                                               tabindex="7"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div class="form-group">
                        <div class="col-md-offset-5 col-lg-4">
                            <button class="btn btn-primary" style="width: 150px;" tabindex="15" type="submit">Save</button>
                            <button class="btn btn-default cancel-btn" tabindex="16" type="reset">Cancel</button>
                        </div>
                    </div>
                </form>
            </div>
        </section>
    </div>
</div>

<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Student Registration List
            </header>

            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th>SL NO</th>
                            <th>STD-ID</th>
                            <th>Name</th>
                            <th>Fathers Name</th>
                            <th>Mobile</th>
                            <th>Religion-Gender</th>
                            <th>Class - Section</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </section>
    </div>
</div>
<script>

    $('#birthDate').datepicker({
        format: 'dd/mm/yyyy',
        endDate: new Date(),
        autoclose: true
    });
    var academicYr, academicYear, className, sectionName;
    jQuery(function ($) {
        var registrationList = $('#list-table').DataTable({
            "sDom": "<'row'<'col-md-4 section-filter-holder'><'col-md-4'><'col-md-4'f>r>t<'row'<'col-md-3'l><'col-md-3'i><'col-md-6'p>>",
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'desc'],
            "fnServerParams": function (aoData) {
                aoData.push({"name": "rStatus", "value": $('#status').val()});
            },
            "sAjaxSource": "${g.createLink(controller: 'registration',action: 'list')}",
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
                {"bSortable": false},
                null,
                {"bSortable": false}
            ]
        });
        $('#list-table_wrapper div.section-filter-holder').html('<select id="status" class="form-control" name="section-filter"><option value="NEW">New Registration</option><option value="ADMISSION">Admitted Students</option><option value="TC">TC or Dropout</option><option value="DELETED">Deleted Entries</option></select>');

        var validator = $('#create-form').validate({
            errorElement: 'span',
//            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                name: {
                    required: true
                },
                fathersName: {
                    required: true
                },
                email: {
                    email: true
                },
                birthDate: {
                    required: true
                },
                religion: {
                    required: true
                },
                gender: {
                    required: true
                },
                mobile: {
                    required: true
                },
                presentAddress: {
                    required: true
                },
                mothersName: {
                    required: true
                }
            },
            highlight: function (e) {
                $(e).closest('.form-group').removeClass('has-info').addClass('has-error');
            },
            success: function (e) {
                $(e).closest('.form-group').removeClass('has-error').addClass('has-info');
                $(e).remove();
            },
            submitHandler: function (form) {
                showLoading("#create-form-holder");
                var formData = new FormData(form);
                $.ajax({
                    url: "${createLink(controller: 'registration', action: 'save')}",
                    type: 'post',
                    dataType: "json",
                    data: formData,
                    mimeType: 'multipart/form-data',
                    contentType: false,
                    cache: false,
                    processData: false,
                    success: function (data) {
                        hideLoading("#create-form-holder");
                        if (data.isError == false) {
                            clearForm(form);
                            registrationList.draw(false);
                            showSuccessMsg(data.message);
                        } else {
                            showErrorMsg(data.message);
                        }
                    },
                    failure: function (data) {
                    }
                })
            }
        });

        $("#presentAddress").blur(function (e) {
            var presentAdd = $.trim($(this).val());
            var permanentAdd = $("#permanentAddress").val();
            if (presentAdd.length != 0) {
                if ($.trim(permanentAdd).length === 0) {
                    $('#permanentAddress').val(presentAdd);
                }
            }
        });

        $('.create-new-btn').click(function (e) {
            $("#create-form-holder").toggle(500);
            $("#name").focus();
            e.preventDefault();
            $("#studentAdmissionDiv").show();
        });

        $(".cancel-btn").click(function () {
            var validator = $("#create-form").validate();
            validator.resetForm();
            $("#id").val('');
            $("#create-form-holder").hide(500);
        });

        $('#list-table').on('click', 'a.edit-reference', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'registration',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        clearForm('#create-form');
                        var dateBirth = data.obj.birthDate;
                        $('#id').val('');
                        $('#id').val(data.obj.id);
                        $('#studentID').val(data.obj.studentID);
                        $('#name').val(data.obj.name);
                        $('#fathersName').val(data.obj.fathersName);
                        $('#mothersName').val(data.obj.mothersName);
                        $('#bloodGroup').val(data.obj.bloodGroup);
                        $('#religion').val(data.obj.religion ? data.obj.religion.name : '');
                        $('#gender').val(data.obj.gender ? data.obj.gender.name : '');
                        $('#presentAddress').val(data.obj.presentAddress);
                        $('#permanentAddress').val(data.obj.permanentAddress);
                        $('#deviceId').val(data.obj.deviceId);
                        $('#email').val(data.obj.email);
                        $('#birthDate').datepicker('setDate', new Date(dateBirth));
                        $('#mobile').val(data.obj.mobile);
                        $('#fathersProfession').val(data.obj.fathersProfession);
                        $('#mothersProfession').val(data.obj.mothersProfession);
                        $('#cardNo').val(data.obj.cardNo);
                        $('#imagePath').val(data.obj.imagePath);
                        $("#create-form-holder").show(1000);
                        $("#name").focus();
                        $("#studentAdmissionDiv").hide(500);
                    } else {
                        showErrorMsg(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            e.preventDefault();
        });

        $('#list-table').on('click', 'a.do-active-reference', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'registration',action: 'doActive')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            registrationList.draw(false);
                            showSuccessMsg(data.message);
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

        $('#list-table').on('click', 'a.delete-reference', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'registration',action: 'delete')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            registrationList.draw(false);
                            showSuccessMsg(data.message);
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

        $('#list-table').on('click', 'a.add-reference', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            var url = "${g.createLink(controller: 'registration',action: 'addDetails')}/" + referenceId;
            window.open(url);
            e.preventDefault();
        });

        $('#status').on('change', function (e) {
            registrationList.draw(false);
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

        $('#sectionName').on('change', function (e) {
            sectionName = $('#sectionName').val();
            academicYear = $('#academicYear').val();
            showLoading("#create-form-holder");
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                data: 'inputSection=' + sectionName + '&inputAcademicYear=' + academicYear,
                url: "${g.createLink(controller: 'remote',action: 'lastRoll')}",
                success: function (data, textStatus) {
                    hideLoading("#create-form-holder");
                    if (data.isError == false) {
                        if (data.rollNumber == null) {
                            $('#rollNo').val(1);
                        } else {
                            $('#rollNo').val(data.rollNumber);
                        }
                    } else {
                        $('#rollNo').val("");
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            $('#filterSection').val(sectionName).trigger("change");
        });

    });

    function getActionButtons(nRow, aData) {
        var actionButtons = "";
        if (aData.studentStatus == 'NEW') {
            <sec:ifAnyGranted roles="ROLE_SUPER_ADMIN,ROLE_ADMIN">
            actionButtons += '<span class="col-md-3 no-padding"><a href="#" referenceId="' + aData.DT_RowId + '" class="edit-reference" title="Edit">';
            actionButtons += '<span class="green glyphicon glyphicon-edit"></span>';
            actionButtons += '</a></span>';
            </sec:ifAnyGranted>

            actionButtons += '<span class="col-md-3 no-padding"><a href="#" referenceId="' + aData.DT_RowId + '" class="delete-reference" title="Delete">';
            actionButtons += '<span class="red glyphicon glyphicon-trash"></span>';
            actionButtons += '</a></span>';
            <sec:ifAnyGranted roles="ROLE_SUPER_ADMIN,ROLE_ADMIN">
            actionButtons += '<span class="col-md-3 no-padding"><a href="#" referenceId="' + aData.DT_RowId + '" class="add-reference" title="Add Details">';
            actionButtons += '<span class="fa fa-plus-square-o"></span>';
            actionButtons += '</a></span>';
            </sec:ifAnyGranted>

        } else if (aData.studentStatus == 'ADMISSION') {
            <sec:ifAnyGranted roles="ROLE_SUPER_ADMIN,ROLE_ADMIN">
            actionButtons += '<span class="col-md-4 no-padding"><a href="#" referenceId="' + aData.DT_RowId + '" class="edit-reference" title="Edit">';
            actionButtons += '<span class="green glyphicon glyphicon-edit"></span>';
            actionButtons += '</a></span>';
            actionButtons += '<span class="col-md-4 no-padding"><a href="#" referenceId="' + aData.DT_RowId + '" class="add-reference" title="Add Details">';
            actionButtons += '<span class="fa fa-plus-square-o"></span>';
            actionButtons += '</a></span>';
            </sec:ifAnyGranted>

        } else if (aData.studentStatus == 'DELETED') {
            actionButtons += '<span class="col-md-6 no-padding"><a href="#" referenceId="' + aData.DT_RowId + '" class="do-active-reference" title="Active">';
            actionButtons += '<span class="green glyphicon glyphicon-ok-circle"></span>';
            actionButtons += '</a></span>';
        }
        return actionButtons;
    }


</script>
</body>
</html>
