<%@ page import="com.grailslab.enums.MainUserType" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="adminLayout"/>
    <title>User List</title>
</head>
<body>

<grailslab:breadCrumbActions breadCrumbTitleText="User List" SHOW_CREATE_LINK="YES" createLinkText="Add New User" createLinkUrl="${g.createLink(controller:'user', action: 'create')}"/>
<g:render template="/layouts/successMsg"/>
<grailslab:fullModal formId="resetPassForm" labelId="resetPassId" modalId="resetPassModal" hiddenId="hiddenIdResPass" label="Reset Password">
    <grailslab:input type="password" label="Password" name="passwordReset"/>
    <grailslab:input type="password" label="Retype Password" name="rePasswordReset"/>
</grailslab:fullModal>

<grailslab:fullModal formId="userLockForm" labelId="userLockId" modalId="userLockModal" hiddenId="hiddenUserLock" label="Mannage User">
    <grailslab:input type="checkbox" label="Enabled" name="enabled"/>
    <grailslab:input type="checkbox" label="Account Expired" name="accountExpired"/>
    <grailslab:input type="checkbox" label="Account Locked" name="accountLocked"/>
    <grailslab:input type="checkbox" label="Password Expired" name="passwordExpired"/>
</grailslab:fullModal>

<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th>Serial</th>
                            <th>Employee</th>
                            <th>Username</th>
                            <th>Status</th>
                            <th>Expired</th>
                            <th>Locked</th>
                            <th>Last Login</th>
                            <th>Switch</th>
                            <th>Manage</th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </section>
    </div>
</div>

<script>
    jQuery(function ($) {
        var userList = $('#list-table').DataTable({
            "sDom": "<'row'<'col-md-6 user-filter-holder dataTables_length'><'col-md-6'f>r>t<'row'<'col-md-4'l><'col-md-4'i><'col-md-4'p>>",
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0,'desc'],
            "sAjaxSource": "${g.createLink(controller: 'user',action: 'list')}",
            "fnServerParams": function (aoData) {
                aoData.push({"name": "userType", "value": $('#userTypeFilter').val()});
            },
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(7)', nRow).html(getswitchButtons(nRow, aData));
                $('td:eq(8)', nRow).html(getActionButtons(nRow, aData));
                return nRow;
            },
            "aoColumns": [
                null,
                { "bSortable": false },
                null,
                { "bSortable": false },
                { "bSortable": false },
                { "bSortable": false },
                { "bSortable": false },
                { "bSortable": false },
                { "bSortable": false }
            ]
        });
        var validatorResetPass = $('#resetPassForm').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                passwordReset: {
                    required: true,
                    maxlength: 200
                },
                rePasswordReset: {
                    required: true,
                    equalTo: "#passwordReset"
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
                var password=$("#passwordReset").val();
                var rePassword=$("#rePasswordReset").val();
                if(machPassWord(password,rePassword)){
                    $('#resetPassModal .create-content .modal-footer-action-btn').hide();
                    $('#resetPassModal .create-content .modal-refresh-processing').show();
                    $.ajax({
                        url: "${createLink(controller: 'user', action: 'resetPass')}",
                        type: 'post',
                        dataType: "json",
                        data: $("#resetPassForm").serialize(),
                        success: function (data) {
                            $('#resetPassModal .create-content .modal-refresh-processing').hide();
                            $('#resetPassModal .create-success .modal-footer-action-btn').show();
                            if (data.isError == true) {
                                $('#resetPassModal .create-success p.message-content').html(data.message);
                            } else {
                                $('#resetPassModal .create-success .message-content').html(data.message);
                            }
                            userList.draw(false);
                            $('#resetPassModal .create-content').hide(1000);
                            $('#resetPassModal .create-success').show(1000);
                            $('#resetPassModal .create-content .modal-footer-action-btn').show();
                        },
                        failure: function (data) {
                        }
                    })
                }else{
                    alert("Password Mismatch please try again")
                }
            }
        });

        var validatorUserLockForm = $('#userLockForm').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
            },
            errorPlacement: function (error, element) {
            },
            highlight: function (e) {
                $(e).closest('.form-group').removeClass('has-info').addClass('has-error');
            },
            success: function (e) {
                $(e).closest('.form-group').removeClass('has-error').addClass('has-info');
                $(e).remove();

            },
            submitHandler: function (form) {
                    $('#userLockModal .create-content .modal-footer-action-btn').hide();
                    $('#userLockModal .create-content .modal-refresh-processing').show();
                    $.ajax({
                        url: "${createLink(controller: 'user', action: 'userManageSave')}",
                        type: 'post',
                        dataType: "json",
                        data: $("#userLockForm").serialize(),
                        success: function (data) {
                            $('#userLockModal .create-content .modal-refresh-processing').hide();
                            $('#userLockModal .create-success .modal-footer-action-btn').show();
                            if (data.isError == true) {
                                $('#userLockModal .create-success p.message-content').html(data.message);
                            } else {
                                $('#userLockModal .create-success .message-content').html(data.message);
                            }
                            userList.draw(false);
                            $('#userLockModal .create-content').hide(1000);
                            $('#userLockModal .create-success').show(1000);
                            $('#userLockModal .create-content .modal-footer-action-btn').show();
                        },
                        failure: function (data) {
                        }
                    })
            }
        });



        $('#list-table_wrapper div.user-filter-holder').html('<select id="userTypeFilter" class="form-control" name="userTypeFilter"><option value="">All</option><g:each in="${workingUserTypes}" var="empType"><option value="${empType.key}">${empType.value}</option> </g:each></select>');
        $('#userTypeFilter').on('change', function (e) {
            showLoading("#data-table-holder");
            userList.draw(false);
            hideLoading("#data-table-holder");
        });

        $('#list-table').on('click', 'a.resetPass-reference', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            $('#resetPassModal .create-success').hide();
            $('#resetPassModal .create-content').show();
            $("#passwordReset").val('');
            $("#rePasswordReset").val('');
            $("#hiddenIdResPass").val(referenceId);
            $('#resetPassModal').modal('show');
            e.preventDefault();
        });

        $('#list-table').on('click', 'a.user-reference', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'user',action: 'userLock')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        $('#hiddenUserLock').val(data.obj.id);
                        $("#enabled").prop("checked", data.obj.enabled);
                        $("#accountExpired").prop("checked", data.obj.accountExpired);
                        $("#accountLocked").prop("checked", data.obj.accountLocked);
                        $("#passwordExpired").prop("checked", data.obj.passwordExpired);
                        $('#userLockModal .create-success').hide();
                        $('#userLockModal .create-content').show();
                        $('#userLockModal').modal('show');
                    } else {
                        alert(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            e.preventDefault();
        });

        $('#list-table').on('click', 'a.edit-reference', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            window.location.href = "${g.createLink(controller: 'user',action: 'edit')}/"+referenceId;
            e.preventDefault();
        });
    });

    function getActionButtons(nRow, aData) {
        var actionButtons = "";
        actionButtons += '<span class="col-md-4 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="edit-reference" title="Edit">';
        actionButtons += '<span class="green glyphicon glyphicon-edit"></span>';
        actionButtons += '</a></span>';
        actionButtons += '<span class="col-md-4 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="user-reference" title="Edit">';
        actionButtons += '<span class="green glyphicon glyphicon-user"></span>';
        actionButtons += '</a></span>';
        actionButtons += '<span class="col-md-4 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="resetPass-reference" title="Reset Password">';
        actionButtons += '<span class="red glyphicon glyphicon-refresh"></span>';
        actionButtons += '</a></span>';
        return actionButtons;
    }

    function getswitchButtons(nRow, aData) {
        var actionButtons = "";
        actionButtons += '<sec:ifAllGranted roles="ROLE_SWITCH_USER"><form style="display: inline-block; margin-right: 15px; " action="${g.createLink(controller: 'login', action: 'impersonate')}" method="POST">';
        actionButtons += '<input type="hidden" name="username" value="'+aData.username+'"/>';
        actionButtons += '<input type="submit" value="Go To User"/></form></sec:ifAllGranted>';
        return actionButtons;
    }

    function machPassWord(password,rePassword) {
        if(password==rePassword){
            return true
        }else{
            return false;
        }
    }

</script>
</body>
</html>
