<%@ page import="com.grailslab.gschoolcore.AcademicYear" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleParentsLayout"/>
    <title>Manage Student</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Manage Student" SHOW_PRINT_BTN="YES"/>
<div class="row" id="create-form-holder" style="display: none;">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading gschool-header-bg">
                Manage Student
            </header>
            <div class="panel-body">
                <div class="row">
                    <div class="col-md-10 col-md-offset-1">
                        <form class="cmxform form-horizontal" id="create-form">
                            <g:hiddenField name="id"/>
                            <g:hiddenField name="academicYear"/>
                           <div class="row">
                                <div class="form-group">
                                    <label class="col-md-2 control-label">Select Class</label>
                                    <div class="col-md-5">
                                        <select tabindex="2" class="form-control" id="formClassName" name="formClassName">
                                            <option value="">Select Class.....</option>
                                        </select>
                                    </div>

                                    <div class="col-md-5">
                                        <select tabindex="3" class="form-control" id="section" name="section">
                                            <option value="">All Section</option>
                                        </select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="registration" class="col-md-2 control-label">Student</label>
                                    <div class="col-md-7">
                                        <select tabindex="3" class="form-control" id="registration" name="registration"></select>
                                    </div>
                                    <div class="col-md-3">
                                        <g:select tabindex="2" class="form-control" id="admissionMonth"
                                                  name='admissionMonth'
                                                  from='${com.grailslab.enums.YearMonths.values()}'
                                                  optionKey="key" optionValue="value"></g:select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="rollNo" class="col-md-2 control-label">Roll</label>
                                    <div class="col-md-4">
                                        <g:textField class="form-control" id="rollNo" tabindex="6" name="rollNo"
                                                     placeholder="Enter roll"/>
                                    </div>

                                    <div class="col-md-4">
                                        <button class="btn btn-primary" tabindex="7" id="form-submit-btn" type="submit">Save</button>
                                        <button class="btn btn-default cancel-btn" tabindex="8"
                                                type="reset">Cancel</button>
                                    </div>

                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>

<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Student List
            </header>
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th>Serial</th>
                            <th>STD-ID</th>
                            <th>Name</th>
                            <th>Roll No</th>
                            <th>Father's Name</th>
                            <th>Birth Date</th>
                            <th>Mobile</th>
                            <th>Class - Section</th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </section>
    </div>
</div>


<script>
    var workingYear, viewHrefUrl, academicYear, className, sectionName, printParam;
    jQuery(function ($) {
        viewHrefUrl = "${g.createLink(controller: 'registration',action: 'addDetails')}/";
        var validator = $('#create-form').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                registration: {
                    required: true
                },
                section: {
                    required: true
                },
                rollNo: {
                    required: true,
                    digits: true
                }
            },
            /*errorPlacement: function (error, element) {
             },*/

            highlight: function (e) {
                $(e).closest('.form-group').removeClass('has-info').addClass('has-error');
            },
            success: function (e) {
                $(e).closest('.form-group').removeClass('has-error').addClass('has-info');
                $(e).remove();
            },
            submitHandler: function (form) {
                showLoading("#create-form-holder");
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    data: $("#create-form").serialize(),
                    url: "${g.createLink(controller: 'student',action: 'saveStudent')}",
                    success: function (data, textStatus) {
                        hideLoading("#create-form-holder");
                        if (data.isError == false) {
                            var table = $('#list-table').DataTable().ajax.reload();
                            $("#create-form-holder").hide(500);
                        } else {
                            showErrorMsg(data.message);
                        }
                    },
                    failure: function (data) {
                    }
                })
            }
        });

        var table = $('#list-table').dataTable({
            "sDom": "<'row'<'col-md-3 academicYear-filter-holder'><'col-md-3 className-filter-holder'><'col-md-3 section-filter-holder'><'col-md-3'f>r>t<'row'<'col-md-3'l><'col-md-3'i><'col-md-6'p>>",
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [2, 'desc'],
            "sAjaxSource": "${g.createLink(controller: 'student',action: 'list')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                return nRow;
            },
            "fnServerParams": function (aoData) {
                aoData.push({"name": "sectionName", "value": $('#filterSection').val()},
                        {"name": "className", "value": $('#filterClassName').val()},
                        {"name": "academicYear", "value": $('#filterAcademicYear').val()});
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
        $('#list-table_wrapper div.academicYear-filter-holder').html('<select id="filterAcademicYear" class="form-control" name="filterAcademicYear"><g:each in="${academicYearList}" var="academicYear"><option value="${academicYear.key}" ${academicYear.key==workingYear.key?'selected':''}>${academicYear.value}</option> </g:each></select>');
        $('#list-table_wrapper div.className-filter-holder').html('<select id="filterClassName" class="form-control" name="filterClassName"><option value="">All Class</option><g:each in="${classList}" var="className"><option value="${className.id}">${className.name}</option> </g:each></select>');
        $('#list-table_wrapper div.section-filter-holder').html('<select id="filterSection" class="form-control" name="filterSection"><option value="">All Section</option></select>');

        $('#filterAcademicYear').on('change', function (e) {
            $('#filterClassName').val("").trigger("change");
        });
        $('#filterClassName').on('change', function (e) {
            academicYear = $('#filterAcademicYear').val();
            className = $('#filterClassName').val();
            loadSectionOnClassChange(academicYear, className);
            $('#filterSection').val("").trigger("change");
        });
        $('#formClassName').on('change', function (e) {
            className = $('#formClassName').val();
            loadSectionOnClassChange(className);
        });
        $('#filterSection').on('change', function (e) {
            showLoading("#data-table-holder");
            $("#list-table").DataTable().draw(false);
            hideLoading("#data-table-holder");
        });

        $('#section').on('change', function (e) {
            sectionName = $('#section').val();
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
            className = $('#formClassName').val();
            $('#filterClassName').val(className);
            loadListSectionOnSectionChange(className, sectionName);
        });


        $(".cancel-btn").click(function () {
            $("#create-form-holder").hide(500);
        });

        $('#list-table').on('click', 'a.edit-reference', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            showLoading("#data-table-holder");
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'student',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    hideLoading("#data-table-holder");
                    if (data.isError == false) {
                        clearForm('#create-form');
                        $('#id').val('');
                        $('#id').val(data.obj.id);
                        $('#academicYear').val(data.obj.academicYear ? data.obj.academicYear.name : '');
                        var $registration = $('#registration');
                        $registration.find('option').remove();
                        $registration.append('<option value="' + data.obj.registration.id + '" selected="selected">' + data.studentID + ' - ' + data.studentName + '</option>');
                        var $clsName = $('#formClassName');
                        $clsName.find('option').remove();
                        $clsName.append('<option value="' + data.obj.className.id + '" selected="selected">'+ data.className + '</option>');
                        loadSectionOnEditStudent(data.obj.className.id, data.obj.section.id, data.obj.academicYear.name);
                        $('#admissionMonth').val(data.obj.admissionMonth ? data.obj.admissionMonth.name : '');
                        $('#rollNo').val(data.obj.rollNo);
                        $("#form-submit-btn").html("Update");
                        $("#create-form-holder").show(1000);
                        $("#name").focus();
                        showSuccessMsg(data.feePaidMsg);
                    } else {
                        showErrorMsg(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            e.preventDefault();
        });
        $('#list-table').on('click', 'a.delete-reference', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                showLoading("#data-table-holder");
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'student',action: 'delete')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        hideLoading("#data-table-holder");
                        if (data.isError == false) {
                            $("#list-table").DataTable().row(selectRow).remove().draw(false);
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
        $('.print-btn').click(function (e) {
            e.preventDefault();
            academicYear = $('#filterAcademicYear').find("option:selected").val();
            className = $('#filterClassName').find("option:selected").val();
            sectionName =$('#filterSection').find("option:selected").val();
            printParam = "${g.createLink(controller: 'studentReport',action: 'studentList','_blank')}?section="+sectionName+"&academicYear="+academicYear+"&className="+className;
            window.open(printParam);
            return false;
        });


    });

    function loadListSectionOnSectionChange(className, sectionName) {
        var $filterSection = $('#filterSection');
        $filterSection.find('option').remove();
        $filterSection.append('<option value="">All Section</option>');
        jQuery.ajax({
            type: 'POST',
            dataType: 'JSON',
            url: "${g.createLink(controller: 'remote',action: 'classSectionList')}?className=" + className,
            success: function (data, textStatus) {
                if (data.isError == false) {
                    $.each(data.sectionList, function (key, value) {
                        $filterSection.append('<option value=' + value.id + '>' + value.name + '</option>');
                    });
                    $filterSection.val(sectionName).trigger("change");
                } else {
                    showErrorMsg(data.message);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            }
        });
    }
    function loadSectionOnClassChange(academicYear, classNameOnchange) {
        var $filterSection = $('#filterSection');
            $filterSection.find('option').remove();
            $filterSection.append('<option value="">All Section</option>');
            if (classNameOnchange.length === 0) {
                return false
            }
        jQuery.ajax({
            type: 'POST',
            dataType: 'JSON',
            url: "${g.createLink(controller: 'remote',action: 'classSectionList')}?academicYear=" + academicYear+"&className="+classNameOnchange,
            success: function (data, textStatus) {
                if (data.isError == false) {
                    $.each(data.sectionList, function (key, value) {
                        $filterSection.append('<option value=' + value.id + '>' + value.name + '</option>');
                    });
                } else {
                    showErrorMsg(data.message);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            }
        });
    }



    function loadSectionOnEditStudent(classNameOnchange, sectionName, academicYear) {
        jQuery.ajax({
            type: 'POST',
            dataType: 'JSON',
            url: "${g.createLink(controller: 'remote',action: 'classSectionList')}?className=" + classNameOnchange+"&academicYear="+academicYear,
            success: function (data, textStatus) {
                if (data.isError == false) {
                    var $select = $('#section');
                    $select.find('option').remove();
                    $.each(data.sectionList, function (key, value) {
                        $select.append('<option value=' + value.id + '>' + value.name + '</option>');
                    });
                    $select.val(sectionName);
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
