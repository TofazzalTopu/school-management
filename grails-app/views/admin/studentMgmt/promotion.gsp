<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleStdMgmtLayout"/>
    <title>Student Promotion</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Manage Promotion"/>
<div class="row" id="create-form-holder">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Student Promotion
            </header>
            <div class="panel-body">
                <div class="row">
                    <div class="col-md-8 col-md-offset-2">
                        <div class="row">
                            <div class="form-horizontal">
                                <div class="form-group">
                                    <label for="academicYearOld" class="col-md-3 control-label">Academic Year*</label>
                                    <div class="col-md-8">
                                        <g:select tabindex="1" class="form-control"
                                                  id="academicYearOld" name='academicYearOld'
                                                  from='${academicYearList}'
                                                  optionKey="key" optionValue="value"></g:select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="classNameOld" class="col-md-3 control-label">Class Name*</label>
                                    <div class="col-md-5">
                                        <g:select tabindex="2" class="form-control" id="classNameOld"
                                                  name='classNameOld'
                                                  from='${classNameList}'
                                                  optionKey="id" optionValue="name"></g:select>
                                    </div>
                                    <div class="col-md-3">
                                        <button class="btn btn-info" id="load-old-btn">Load Students</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-12 alert-info" id="readmission-student-holder" style="display: none;">
                    </br>
                        <div class="row">
                            <form class="cmxform form-horizontal" id="readmission-form">
                                <g:hiddenField name="academicYear"/>
                                <div class="form-group col-md-3">
                                    <label class="col-md-4 control-label">Year</label>
                                    <div class="col-md-8">
                                        <label class="control-label">: <span id="yearHolder"></span> </label>
                                    </div>
                                </div>

                                <div class="form-group col-md-5">
                                    <label for="section" class="col-md-4 control-label">Section *</label>
                                    <div class="col-md-8">
                                        <select id="section" name='section' class="form-control" required="required"></select>
                                    </div>
                                </div>
                                <div class="form-group col-md-4">
                                    <label for="admissionMonth" class="col-md-6 control-label">Month Eff.</label>
                                    <div class="col-md-6">
                                        <g:select tabindex="2" class="form-control" id="admissionMonth" name='admissionMonth'
                                                  from='${com.grailslab.enums.YearMonths.values()}'
                                                  optionKey="key" optionValue="value"></g:select>
                                    </div>
                                </div>
                                <div class="form-group col-md-6">
                                    <label for="section" class="col-md-3 control-label">Student *</label>
                                    <div class="col-md-9">
                                        <select id="student" name='student' class="form-control" required="required"></select>
                                    </div>
                                </div>
                                <div class="form-group col-md-6">
                                    <label for="rollNo" class="col-md-3 control-label">Roll No *</label>
                                    <div class="col-md-5">
                                        <g:textField class="form-control" id="rollNo" tabindex="4" name="rollNo"
                                                     placeholder="Enter roll" required="required"/>
                                    </div>
                                    <div class="col-md-4">
                                        <button class="btn btn-primary" id="submit-readmission-btn">Submit</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>
<div class="row" id="student-datalist-holder" style="display: none;">
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
    var className, academicYear,section;
    jQuery(function ($) {
        $('#load-old-btn').click(function (e) {
            className = $('#classNameOld').val();
            academicYear = $('#academicYearOld').val();
            showLoading("#create-form-holder");
            jQuery.ajax({
                type: 'POST',
                dataType:'JSON',
                url: "${g.createLink(controller: 'student',action: 'loadPromotion')}?academicYear="+academicYear+"&className="+className,
                success: function (data) {
                    hideLoading("#create-form-holder");
                    if(data.isError==true){
                        $("#student-datalist-holder").hide(500);
                        $("#readmission-student-holder").hide(500);
                        showErrorMsg(data.message);
                    }else {
                        $('#academicYear').val(data.academicYear);
                        $('#yearHolder').html(data.academicYearStr);

                        var $select1 = $('#section');
                        $select1.find('option').remove();
                        $select1.append('<option value="">Select Section</option>');
                        $.each(data.sectionList,function(key, value)
                        {
                            $select1.append('<option value=' + value.id + '>' + value.name + '</option>');
                        });

                        $('#student').select2("destroy");
                        var $select2 = $('#student');
                        $select2.find('option').remove();
                        $.each(data.studentList,function(key, value)
                        {
                            $select2.append('<option value=' + value.id + '>' + value.name + '</option>');
                        });
                        $('#student').select2().enable(true);

                        $("#readmission-student-holder").show(500);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });

            e.preventDefault();
        });
        $('form#readmission-form #section').change(function () {
            var optionSelected = $(this).find("option:selected");
            section  = optionSelected.val();
            var academicYear = $('#academicYear').val();
            if(section == undefined || section == ""){
                $('#rollNo').val("");
                $('#student-datalist-holder').hide(500);
            }else{
                showLoading("#readmission-student-holder");
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'remote',action: 'lastRoll')}?inputSection="+section+"&inputAcademicYear="+academicYear,
                    success: function (data) {
                        hideLoading("#readmission-student-holder");
                        if (data.isError == false) {
                            $('#list-table').DataTable().ajax.reload();
                            $('#student-datalist-holder').show(500);
                            $('#rollNo').val(data.rollNumber);
                        } else {
                            showErrorMsg(data.message);
                        }
//                        clearForm(form);
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            }
        });

        var validator = $('#readmission-form').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                student: {
                    required: true
                },
                academicYear: {
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
                    data: $("#readmission-form").serialize(),
                    url: "${g.createLink(controller: 'student',action: 'saveReAdmission')}",
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            $("#rollNo").val('');
                            $('#list-table').DataTable().ajax.reload();
                            $("#student option:selected").remove();
                            $('#student').select2("destroy");
                            $('#student').select2().enable(true);
                            $("#rollNo").val(data.rollNumber);
                            $("#rollNo").focus();
                        } else {
                            showErrorMsg(data.message);
                        }
                        hideLoading("#create-form-holder");
                    },
                    failure: function (data) {
                    }
                })
            }
        });
        var table = $('#list-table').dataTable({
            "bAutoWidth": false,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [2, 'desc'],
            "deferLoading": 0,
            "sAjaxSource": "${g.createLink(controller: 'student',action: 'list')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(7)', nRow).html(getActionButtons(nRow, aData));
                return nRow;
            },
            "fnServerParams": function (aoData) {
                aoData.push({ "name": "sectionName", "value": $('#section').val() }, { "name": "academicYear", "value": $('#academicYear').val() });
            },
            "aoColumns": [
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                { "bSortable": false }
            ]
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
                            $( "#load-old-btn" ).trigger( "click" );
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
    });
    function getActionButtons(nRow, aData) {
        var actionButtons = "";
        actionButtons += '<sec:access controller="student" action="delete"><span class="col-md-6 no-padding"><a href="#" referenceId="' + aData.DT_RowId + '" class="delete-reference" title="Delete">';
        actionButtons += '<span class="green glyphicon glyphicon-trash"></span>';
        actionButtons += '</a></span></sec:access>';
        return actionButtons;
    }

</script>
</body>
</html>
