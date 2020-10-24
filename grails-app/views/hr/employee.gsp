<%@ page import="com.grailslab.enums.EmpCategoryType"%>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleHRLayout"/>
    <title>Employee Registration</title>
    <style>
    .table-responsive {
        overflow-x: scroll;
    }
    </style>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Teacher And Staff" SHOW_CREATE_BTN="YES" createButtonText="Add New Teacher/Staff"></grailslab:breadCrumbActions>
<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="gLabModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-mid">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
                        class="sr-only">Close</span></button>
                <h4 class="modal-title" id="gLabModalTitleLabel">Manage Teacher/Staff</h4>
            </div>
            <section class="panel">
                <div class="panel-body create-content">
                    <form class="form-horizontal" id="2ndFormmodalForm">
                        <g:hiddenField name="id"/>
                        <div class="modal-body">
                            <div class="row">
                                <div class="col-md-12">
                                    <div class="form-group">
                                        <div class="error-highlight col-md-6">
                                            <label>Category*</label>
                                            <g:select name="hrCategory" class="form-control"
                                                      from="${hrCategoryList}"
                                                      noSelection="${['': 'Select Category...']}" optionKey="id"
                                                      optionValue="name"/>
                                        </div>
                                        <div class="error-highlight col-md-6">
                                            <label>Designation*</label>
                                            <select name="hrDesignation" id="hrDesignation" class="form-control"><option
                                                    value="">Select Designation .....</option></select>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <div class="error-highlight col-md-6">
                                            <label>Staff Categories</label>
                                            <g:select multiple="multiple" name="hrStaffCategory" id="hrStaffCategory" class="form-control"
                                                      from="${hrStaffCategoryList}"
                                                      optionKey="id"
                                                      optionValue="name"/>
                                        </div>

                                        <div class="error-highlight col-md-6">
                                            <label>Employee ID*</label>
                                            <g:textField name="empID" class="form-control" placeholder="Employee Id"/>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <div class="error-highlight col-md-6">
                                            <label>Full Name*</label>
                                            <g:textField name="name" class="form-control" placeholder="Full Name"/>
                                        </div>

                                        <div class="error-highlight col-md-6">
                                            <label>Mobile No*</label>
                                            <g:textField name="mobile" class="form-control" placeholder="Mobile No"/>
                                        </div>

                                    </div>

                                    <div class="form-group">
                                        <div class="error-highlight col-md-6">
                                            <label>Father's Name*</label>
                                            <g:textField name="fathersName" class="form-control"
                                                         placeholder="Fathers Name"/>
                                        </div>

                                        <div class="error-highlight col-md-6">
                                            <label>Mother's Name*</label>
                                            <g:textField name="mothersName" class="form-control"
                                                         placeholder="Mothers Name"/>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <div class="error-highlight col-md-4">
                                            <label>Email</label>
                                            <g:textField name="emailId" class="form-control" placeholder="Email"/>
                                        </div>

                                        <div class="error-highlight col-md-4">
                                            <label>Card Number</label>
                                            <g:textField name="cardNo" class="form-control" placeholder="Card Number"/>
                                        </div>
                                        <div class="error-highlight col-md-4">
                                            <label>Device Enroll ID</label>
                                            <g:textField name="deviceId" class="form-control" placeholder="Device Enroll ID"/>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <div class="error-highlight col-md-6">
                                            <label>Birth Date</label>
                                            <g:textField name="birthDate" class="form-control"
                                                         placeholder="Birth Date"/>
                                        </div>

                                        <div class="error-highlight col-md-6">
                                            <label>Join Date</label>
                                            <g:textField name="joiningDate" class="form-control"
                                                         placeholder="Join Date"/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="error-highlight col-md-6">
                                            <label>Confirmation Date</label>
                                            <g:textField name="confirmationDate" class="form-control"
                                                         placeholder="Confirmation Date"/>
                                        </div>

                                        <div class="error-highlight col-md-6">
                                            <label>Salary Account</label>
                                            <g:textField name="salaryAccounts" class="form-control"
                                                         placeholder="Salary Account"/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="error-highlight col-md-6">
                                            <label>Show Order*</label>
                                            <input type="number" min="1" max="4000" step="1" placeholder="Show Order" name="sortOrder" id="sortOrder" class="form-control">
                                        </div>

                                        <div class="error-highlight col-md-6">
                                            <label>School Hour</label>
                                            <g:select name="hrPeriod" id="hrPeriod" placeholder="School Hour" class="form-control"
                                                      noSelection="${['': 'Select One...']}"
                                                      from="${officeHourList}"
                                                      optionKey="id"
                                                      optionValue="name"/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="error-highlight col-md-6">
                                            <label>DPS Accounts</label>
                                            <g:textField name="dpsAccounts" class="form-control"
                                                         placeholder="DPS Accounts"/>
                                        </div>

                                        <div class="error-highlight col-md-6">
                                            <label>Employee Type</label>
                                            <g:select name="employeeType" id="employeeType" placeholder="Employee Type"
                                                      noSelection="${['': 'Select One...']}"
                                                      from="${com.grailslab.enums.EmployeeType.values()}"
                                                      value="${name}" class="form-control"
                                                      optionKey="key"/>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <div class="error-highlight col-md-6">

                                        </div>
                                        <div class="error-highlight col-md-6">
                                            <label>Facebook Profile Link</label>
                                            <g:textField name="fbId" id="fbId" class="form-control"
                                                         placeholder="Facebook Profile Link"/>
                                        </div>

                                    </div>
                                    <div class="form-group">
                                        <div class="error-highlight col-md-6">
                                            <label>Short Description</label>
                                            <textarea name="aboutMe" id="aboutMe" class="form-control" rows="10"></textarea>
                                        </div>

                                        <div class="error-highlight col-md-6">
                                            <label>Profile Image</label>
                                            <input type="file" name="pImage" id="pImage"/>
                                            <br/>
                                            <asset:image src="no-image.jpg" alt="avatar" id="ImagePreview" width="190px" height="190px" class="thumbnail"/>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div class="modal-footer modal-footer-action-btn">
                            <button type="button" class="btn btn-default" data-dismiss="modal"
                                    aria-hidden="true">Cancel</button>
                            <button type="submit" id="modal-create-yes-btn"
                                    class="btn btn-large btn-primary">Submit</button>
                        </div>

                        <div class="modal-footer modal-refresh-processing" style="display: none;">
                            <i class="fa fa-refresh fa-spin text-center"></i>
                        </div>
                    </form>
                </div>
            </section>
            <div class="create-success" style="display: none;">
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-2">
                            <asset:image src="share-modal-icon.jpg" width="60" height="60"/>
                        </div>

                        <div class="col-md-10">
                            <p class="message-content"></p>
                        </div>
                    </div>
                </div>

                <div class="modal-footer modal-footer-action-btn">
                    <button type="button" class="btn btn-default" data-dismiss="modal" aria-hidden="true">Close</button>
                </div>
            </div>

        </div>
    </div>
</div>

<grailslab:dataTable dataSet="${dataReturn}" tableHead="SL,EmpID,Name,Card No,Mobile,Email,Designation,Show Catagory,Show Order"
                     actions="fa fa-pencil-square-o, fa fa-retweet"></grailslab:dataTable>

<script>
    $('#birthDate').datepicker({
        format: 'dd/mm/yyyy',
        endDate: new Date(),
        autoclose: true
    });
    $('#joiningDate').datepicker({
        format: 'dd/mm/yyyy',
        endDate: new Date(),
        autoclose: true
    });
    $('#confirmationDate').datepicker({
        format: 'dd/mm/yyyy',
        autoclose: true
    });
    var hrCategory;
    jQuery(function ($) {
        var employeeList = $('#list-table').DataTable({
            "sDom": "<'row'<'col-md-3 user-filter-holder'><'col-md-3 status-filter-holder'><'col-md-6'f>r>t<'row'<'col-md-4'l><'col-md-4'i><'col-md-4'p>>",
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'asc'],
            "deferLoading": ${totalCount?:0},
            "sAjaxSource": "${g.createLink(controller: 'employee',action: 'list')}",
            "fnServerParams": function (aoData) {
                aoData.push({"name": "hrCategoryType", "value": $('#hrCategoryType').val()});
                aoData.push({"name": "activeStatus", "value": $('#activeStatus').val()});
            },
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(9)', nRow).html(getActionButtons(nRow, aData));
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
                null,
                null,
                {"bSortable": false}
            ]
        });
        $('#list-table_wrapper div.user-filter-holder').html('<select id="hrCategoryType" class="form-control" name="hrCategoryType"><option value="">All</option><g:each in="${hrCategoryList}" var="empType"><option value="${empType.id}">${empType.name}</option> </g:each></select>');
        $('#list-table_wrapper div.status-filter-holder').html('<select id="activeStatus" class="form-control" name="activeStatus"><option value="ACTIVE">Active</option><option value="INACTIVE">Deleted Employees</option></select>');

        $("#hrStaffCategory").select2({
            placeholder: "Select One or More Category",
            allowClear: true
        });
        $("#pImage").change(function () {
            readImageURL(this);
        });
        var validator = $('#2ndFormmodalForm').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                name: {
                    required: true,
                    maxlength: 200
                },
                empID: {
                    required: true
                },
                emailId: {
                    email: true
                },
                mobile: {
                    required: true
                },
                fathersName: {
                    required: true
                },
                mothersName: {
                    required: true
                },
                hrCategory: {
                    required: true
                },
                sortOrder: {
                    required: true,
                    number: true
                },
                hrDesignation: {
                    required: true
                }
            },
            highlight: function (e) {
                $(e).closest('.error-highlight').removeClass('has-info').addClass('has-error');
            },
            success: function (e) {
                $(e).closest('.error-highlight').removeClass('has-error').addClass('has-info');
                $(e).remove();

            },
            submitHandler: function (form) {
                $('#myModal .create-content .modal-footer-action-btn').hide();
                $('#myModal .create-content .modal-refresh-processing').show();
                hrCategory = $('#hrCategory').val();
                var formData = new FormData(form);
                $.ajax({
                    url: "${createLink(controller: 'employee', action: 'save')}",
                    type: 'post',
                    dataType: "json",
                    data: formData,
                    mimeType: 'multipart/form-data',
                    contentType: false,
                    cache: false,
                    processData: false,
                    success: function (data) {
                        if (data.isError == false) {
                            showSuccessMsg(data.message);
                            window.location.href = "${g.createLink(controller: 'employee',action: 'index')}?hrCategoryType="+hrCategory;
                        } else {
                            showErrorMsg(data.message);
                        }
                        formSuccess(data);
                    },
                    failure: function (data) {
                    }
                })
            }
        });


        hrCategory = "${hrCategoryType}";
        if(hrCategory){
            $('#hrCategoryType').val(hrCategory);
        }
        $('#hrCategoryType').on('change', function (e) {
            showLoading("#data-table-holder");
            employeeList.draw(false);
            hideLoading("#data-table-holder");
        });
        $('#activeStatus').on('change', function (e) {
            showLoading("#data-table-holder");
            employeeList.draw(false);
            hideLoading("#data-table-holder");
        });


        $('.create-new-btn').click(function (e) {
            $('#myModal .create-success').hide();
            $('#myModal .create-content').show();
            var validator = $("#2ndFormmodalForm").validate();
            validator.resetForm();
            $("#id").val('');
            $('#myModal').modal('show');
            e.preventDefault();
        });

        $(".cancel-btn").click(function () {
            var validator = $("#2ndFormmodalForm").validate();
            validator.resetForm();
            $("#id").val('');
            $('#myModal').modal('hide');
        });

        $('#hrCategory').on('change', function () {
            var hrCategory = $('#hrCategory').val();
            if (hrCategory != "") {
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'employee',action: 'changeCategory')}?hrCategory=" + hrCategory,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            createCombo(data.designation, 'hrDesignation', 'id', 'name');
                        } else {
                            showErrorMsg(data.message);
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            } else {
                createCombo(null, 'hrDesignation', 'id', 'name');
            }
            $('#hrCategoryType').val(hrCategory);
            $('#hrCategoryType').trigger("change");
        });

        $('#list-table').on('click', 'a.reference-1', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'employee',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    validator.resetForm();
                    if (data.isError == false) {
                        var dateBirth = data.obj.birthDate;
                        var joiningDate = data.obj.joiningDate;
                        var confirmationDate = data.obj.confirmationDate;
                        createCombo(data.designation, 'hrDesignation', 'id', 'name');
                        $('#id').val(data.obj.id);
                        $('#name').val(data.obj.name);
                        $('#empID').val(data.obj.empID);
                        $('#hrDesignation').val(data.obj.hrDesignation ? data.obj.hrDesignation.id : '');
                        $('#hrCategory').val(data.obj.hrCategory ? data.obj.hrCategory.id : '');
                        $('#hrPeriod').val(data.obj.hrPeriod ? data.obj.hrPeriod.id : '');
                        $('#sortOrder').val(data.obj.sortOrder);
                        $('#fathersName').val(data.obj.fathersName);
                        $('#mothersName').val(data.obj.mothersName);
                        $('#employeeType').val(data.obj.employeeType ? data.obj.employeeType.name : '');
                        $('#emailId').val(data.obj.emailId);
                        $('#bloodGroup').val(data.obj.bloodGroup);
                        $('#cardNo').val(data.obj.cardNo);
                        $('#deviceId').val(data.obj.deviceId);
                        $('#mobile').val(data.obj.mobile);
                        $('#birthDate').datepicker('setDate', new Date(dateBirth));
                        $('#joiningDate').datepicker('setDate', new Date(joiningDate));
                        $('#confirmationDate').datepicker('setDate', new Date(confirmationDate));
                        $('#salaryAccounts').val(data.obj.salaryAccounts);
                        $('#dpsAccounts').val(data.obj.dpsAccounts);
                        $('#aboutMe').val(data.obj.aboutMe);
                        $('#fbId').val(data.obj.fbId);

                        var hrStaffCat =data.categoryIdList;
                        if(hrStaffCat){
                            var $hrStaffCategory = $("#hrStaffCategory");
                            $hrStaffCategory.val(hrStaffCat).trigger("change");
                        }

                        $('#myModal .create-success').hide();
                        $('#myModal .create-content').show();
                        $('#myModal').modal('show');
                    } else {
                        alert(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            e.preventDefault();
        });

        $('#list-table').on('click', 'a.reference-2', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure inactive/active this employee?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'employee',action: 'inactive')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            employeeList.draw(false);
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
        return getActionBtnCustom(nRow, aData, 'fa fa-pencil-square-o, fa fa-retweet');
    }

    function readImageURL(input) {
        if (input.files && input.files[0]) {
            var reader = new FileReader();
            reader.onload = function (e) {
                $('#ImagePreview').attr('src', e.target.result);
            };
            reader.readAsDataURL(input.files[0]);
        }
    }
</script>
</body>
</html>