<%@ page import="com.grailslab.gschoolcore.AcademicYear" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleStdMgmtLayout"/>
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
                    <div class="col-md-8 col-md-offset-1">
                        <form class="cmxform form-horizontal" id="create-form">
                            <g:hiddenField name="id"/>
                            <g:hiddenField name="academicYear"/>
                            <div class="row">
                                <div class="form-group">
                                    <label class="col-md-4  control-label">Select Class</label>
                                    <div class="col-md-8">
                                        <g:select class="form-control" id="formClassName" name='formClassName'
                                                  noSelection="${['': 'Select Class.....']}"
                                                  from='${classList}'
                                                  optionKey="id" optionValue="name"></g:select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="section" class="col-md-4 control-label">Section Name *</label>
                                    <div class="col-md-8">
                                        <select tabindex="2" class="form-control" id="section" name="section"></select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="admissionMonth" class="col-md-4 control-label">Admission On *</label>
                                    <div class="col-md-8">
                                        <g:select tabindex="2" class="form-control" id="admissionMonth"
                                                  name='admissionMonth'
                                                  from='${com.grailslab.enums.YearMonths.values()}'
                                                  optionKey="key" optionValue="value"></g:select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="registration" class="col-md-4 control-label">Student</label>
                                    <div class="col-md-8">
                                        <select tabindex="3" class="form-control" id="registration" name="registration"></select>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="rollNo" class="col-md-4 control-label">Roll</label>
                                    <div class="col-md-4">
                                        <g:textField class="form-control" id="rollNo" tabindex="4" name="rollNo"
                                                     placeholder="Enter roll"/>
                                    </div>
                                </div>
                            </div>
                    </div>
                    <div class="row">
                        <div class="form-group">
                            <div class="col-md-offset-8 col-lg-4">
                                <button class="btn btn-primary" tabindex="2" id="form-submit-btn"
                                        type="submit">Save</button>
                                <button class="btn btn-default cancel-btn" tabindex="3"
                                        type="reset">Cancel</button>
                            </div>
                        </div>
                    </div>
                </form>
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
                            <th>Roll No</th>
                            <th>Name</th>
                            <th>Father's Name</th>
                            <th>Birth Date</th>
                            <th>Mobile</th>
                            <th>Class - Section</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        <g:each in="${dataReturn}" var="dataSet">
                            <tr>
                                <td>${dataSet[0]}</td>
                                <td>${dataSet[1]}</td>
                                <td>${dataSet[2]}</td>
                                <td>${dataSet[3]}</td>
                                <td>${dataSet[4]}</td>
                                <td>${dataSet[5]}</td>
                                <td>${dataSet[6]}</td>
                                <td>${dataSet[7]}</td>
                                <td>
                                    <span class="col-md-4 no-padding"><a href="#" referenceId="${dataSet.DT_RowId}"
                                                                         class="edit-reference" title="Edit"><span
                                                class="green glyphicon glyphicon-edit"></span></a></span>
                                    <span class="col-md-4 no-padding"><a href="#" referenceId="${dataSet.DT_RowId}"
                                                                         class="delete-reference" title="Delete"><span
                                                class="green glyphicon glyphicon-trash"></span></a></span>
                                    <span class="col-md-4 no-padding"><g:link controller="student" action="view"
                                                                              id="${dataSet.DT_RowId}"><span
                                                class="green glyphicon glyphicon-zoom-in"></span></g:link></span>
                                </td>
                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                </div>
            </div>
        </section>
    </div>
</div>


<script>
    var workingYear, viewHrefUrl, academicYear, className, sectionName, printParam;
    jQuery(function ($) {
        viewHrefUrl = "${g.createLink(controller: 'student',action: 'view')}/";
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
                    url: "${g.createLink(controller: 'student',action: 'saveAdmission')}",
                    success: function (data, textStatus) {
                        hideLoading("#create-form-holder");
                        if (data.isError == false) {
                            $("#id").val('');
                            $("#rollNo").val('');
                            $("#form-submit-btn").html("Submit");
                            var table = $('#list-table').DataTable().ajax.reload();
                            $("#registration option:selected").remove();
                            $("#rollNo").val("");
                            $("#rollNo").focus();
                        } else {
                            showErrorMsg(data.message);
                        }
//                        $( "#create-form-holder" ).isLoading( "hide" );
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
            "deferLoading": ${totalCount},
            "sAjaxSource": "${g.createLink(controller: 'student',action: 'list')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(8)', nRow).html(getActionButtons(nRow, aData));
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
                null,
                {"bSortable": false}
            ]
        });
        $('#list-table_wrapper div.academicYear-filter-holder').html('<select id="filterAcademicYear" class="form-control" name="filterAcademicYear"><g:each in="${AcademicYear.collegeYears()}" var="academicYear"><option value="${academicYear.key}" ${academicYear.key==workingYear.key?'selected':''}>${academicYear.value}</option> </g:each></select>');
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


        $(".cancel-btn").click(function () {
            var validator = $("#create-form").validate();
            validator.resetForm();
            $("#id").val('');
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
                        $('#registration').append('<option value="' + data.obj.registration.id + '" selected="selected">' + data.studentID + ' - ' + data.studentName + '</option>');
                        if (data.isFeePaid === true) {
                            var $clsName = $('#formClassName');
                            $clsName.find('option').remove();
                            $clsName.append('<option value="' + data.obj.className.id + '" selected="selected">'+ data.className + '</option>');

                            var $secName = $('#section');
                            $secName.find('option').remove();
                            $secName.append('<option value="' + data.obj.section.id + '" selected="selected">' + data.sectionName + '</option>');
                            showSuccessMsg(data.feePaidMsg);
                        } else {
                            $('#formClassName').val(data.obj.className.id);
                            loadSectionOnEditStudent(data.obj.className.id, data.obj.section.id);
                        }
                        $('#admissionMonth').val(data.obj.admissionMonth ? data.obj.admissionMonth.name : '');
                        $('#rollNo').val(data.obj.rollNo);
                        $("#form-submit-btn").html("Update");
                        $("#create-form-holder").show(1000);
                        $("#name").focus();
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
//            var dataTableParams = table.oApi._fnAjaxParameters(table.dataTable().fnSettings());
            academicYear = $('#filterAcademicYear').find("option:selected").val();
            className = $('#filterClassName').find("option:selected").val();
            sectionName =$('#filterSection').find("option:selected").val();
            printParam = "${g.createLink(controller: 'studentReport',action: 'studentList','_blank')}?section="+sectionName+"&academicYear="+academicYear+"&className="+className;
            window.open(printParam);
            return false;
        });


    });

    function getActionButtons(nRow, aData) {
        var actionButtons = "";
        actionButtons += '<span class="col-md-4 no-padding"><a href="#" referenceId="' + aData.DT_RowId + '" class="edit-reference" title="Edit">';
        actionButtons += '<span class="green glyphicon glyphicon-edit"></span>';
        actionButtons += '</a></span>';
        actionButtons += '<span class="col-md-4 no-padding"><a href="#" referenceId="' + aData.DT_RowId + '" class="delete-reference" title="Delete">';
        actionButtons += '<span class="green glyphicon glyphicon-trash"></span>';
        actionButtons += '</a></span>';
        actionButtons += '<span class="col-md-4 no-padding"><a href="' + viewHrefUrl + aData.DT_RowId + '" class="view-reference" title="View Details">';
        actionButtons += '<span class="green glyphicon glyphicon-zoom-in"></span>';
        actionButtons += '</a></span>';
        return actionButtons;
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



    function loadSectionOnEditStudent(classNameOnchange, sectionName) {
        jQuery.ajax({
            type: 'POST',
            dataType: 'JSON',
            url: "${g.createLink(controller: 'remote',action: 'classSectionList')}?className=" + classNameOnchange,
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
