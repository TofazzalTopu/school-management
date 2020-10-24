<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="adminLayout"/>
    <title>User List</title>
</head>
<body>
<grailslab:breadCrumbActions firstBreadCrumbUrl="${g.createLink(controller: 'user', action: 'index')}" firstBreadCrumbText="User List" breadCrumbTitleText="Create" SHOW_LINK_BTN="YES" linkBtnText="Back"/>
<div class="row">
    <div class="col-sm-12">
        <section>
            <div class="panel">
                <header class="panel-heading">
                    ADD NEW USER
                </header>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-md-10">
                            <form class="form-horizontal" id="add_new_user" action="${g.createLink(controller: 'user', action: 'saveEmployee')}" method="post">
                                <g:hiddenField name="userId" value="${user?.id}"/>
                                <g:hiddenField name="mainUserType" value="Employee"/>
                                <g:render template="/layouts/successMsg"/>
                                <g:render template="/layouts/errors" bean="${command?.errors}"/>
                                <div class="form-group ${hasErrors(bean: command, field: 'objId', 'has-error')}">
                                    <label for="objId" class="col-md-4 control-label">Employee List</label>
                                    <div class="col-md-6">
                                        <g:if test="${user}">
                                            <input type="text" class="form-control" name="objId" value="${employeeName}" tabindex="2" readonly />
                                        </g:if>
                                        <g:else>
                                            <input type="hidden" class="form-control" id="objId"  name="objId"  tabindex="2" />
                                        </g:else>
                                    </div>
                                </div>
                                <div class="form-group ${hasErrors(bean: command, field: 'username', 'has-error')}">
                                    <label for="username" class="col-md-4 control-label">User name</label>
                                    <div class="col-md-6">
                                        <g:if test="${user}">
                                            <input type="text" class="form-control" id="username" name="username" value="${user?.username}"  tabindex="2" readonly />
                                        </g:if>
                                        <g:else>
                                            <input type="text" class="form-control" id="username" name="username" value="${command.username? command.username : user?.username}"  tabindex="2" />
                                        </g:else>
                                    </div>
                                </div>
                                <g:if test="${!user}">
                                    <div class="form-group">
                                        <label class="col-md-4 control-label">Password</label>
                                        <div class="col-md-6">
                                            <span class="">${grailsApplication.config.getProperty("app.school.default.password")}</span>
                                        </div>
                                    </div>
                                </g:if>

                                <div class="form-group ${hasErrors(bean: command, field: 'empRole', 'has-error')}">
                                    <label  class="col-md-4 control-label" style="color: #70b212;">Select Main User Role</label>
                                </div>
                                <sec:ifAnyGranted roles="ROLE_SUPER_ADMIN,ROLE_SCHOOL_HEAD">
                                    <div class="form-group">
                                    <label  class="col-md-4 control-label">Administrator</label>
                                        <div class="col-md-6">
                                            <sec:ifAnyGranted roles="ROLE_SUPER_ADMIN">
                                                <g:if test="${authorityStr?.contains("ROLE_SCHOOL_HEAD")}">
                                                    <input class="form-check-input" type="checkbox" name="empRole" checked value="ROLE_SCHOOL_HEAD"> ROLE_SCHOOL_HEAD
                                                </g:if>
                                                <g:else>
                                                    <input class="form-check-input" type="checkbox" name="empRole" value="ROLE_SCHOOL_HEAD"> ROLE_SCHOOL_HEAD
                                                </g:else>
                                                <g:if test="${authorityStr?.contains("ROLE_ADMIN")}">
                                                    <input class="form-check-input" type="checkbox" name="empRole" checked value="ROLE_ADMIN"> ROLE_ADMIN
                                                </g:if>
                                                <g:else>
                                                    <input class="form-check-input" type="checkbox" name="empRole" value="ROLE_ADMIN"> ROLE_ADMIN
                                                </g:else>
                                                <g:if test="${authorityStr?.contains("ROLE_SWITCH_USER")}">
                                                    <input class="form-check-input" type="checkbox" name="empRole" checked value="ROLE_SWITCH_USER"> ROLE_SWITCH_USER
                                                </g:if>
                                                <g:else>
                                                    <input class="form-check-input" type="checkbox" name="empRole" value="ROLE_SWITCH_USER"> ROLE_SWITCH_USER
                                                </g:else>
                                            </sec:ifAnyGranted>
                                            <sec:ifAnyGranted roles="ROLE_SCHOOL_HEAD">
                                                <g:if test="${authorityStr?.contains("ROLE_ADMIN")}">
                                                    <input class="form-check-input" type="checkbox" name="empRole" checked value="ROLE_ADMIN"> ROLE_ADMIN
                                                </g:if>
                                                <g:else>
                                                    <input class="form-check-input" type="checkbox" name="empRole" value="ROLE_ADMIN"> ROLE_ADMIN
                                                </g:else>
                                            </sec:ifAnyGranted>
                                        </div>
                                    </div>
                                </sec:ifAnyGranted>
                                <div class="form-group">
                                    <label  class="col-md-4 control-label">Hr</label>
                                    <div class="col-md-6">
                                        <g:if test="${authorityStr?.contains("ROLE_HR")}">
                                            <input class="form-check-input" type="checkbox" name="empRole" checked value="ROLE_HR"> ROLE_HR
                                        </g:if>
                                        <g:else>
                                            <input class="form-check-input" type="checkbox" name="empRole" value="ROLE_HR"> ROLE_HR
                                        </g:else>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label  class="col-md-4 control-label">Accounts & Collection</label>
                                    <div class="col-md-6">
                                        <g:if test="${authorityStr?.contains("ROLE_ACCOUNTS")}">
                                            <input class="form-check-input" type="checkbox" name="empRole" checked value="ROLE_ACCOUNTS"> ROLE_ACCOUNTS
                                        </g:if>
                                        <g:else>
                                            <input class="form-check-input" type="checkbox" name="empRole" value="ROLE_ACCOUNTS"> ROLE_ACCOUNTS
                                        </g:else>

                                    </div>
                                </div>
                                <div class="form-group">
                                    <label  class="col-md-4 control-label">Lirbaby</label>
                                    <div class="col-md-6">
                                        <g:if test="${authorityStr?.contains("ROLE_LIBRARY")}">
                                            <input class="form-check-input" type="checkbox" name="empRole" checked value="ROLE_LIBRARY"> ROLE_LIBRARY
                                        </g:if>
                                        <g:else>
                                            <input class="form-check-input" type="checkbox" name="empRole" value="ROLE_LIBRARY"> ROLE_LIBRARY
                                        </g:else>

                                    </div>
                                </div>
                                <div class="form-group">
                                    <label  class="col-md-4 control-label">Teacher</label>
                                    <div class="col-md-6">
                                        <g:if test="${authorityStr?.contains("ROLE_TEACHER")}">
                                            <input class="form-check-input" type="checkbox" name="empRole" checked value="ROLE_TEACHER"> ROLE_TEACHER
                                        </g:if>
                                        <g:else>
                                            <input class="form-check-input" type="checkbox" name="empRole" value="ROLE_TEACHER"> ROLE_TEACHER
                                        </g:else>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label  class="col-md-4 control-label">Website</label>
                                    <div class="col-md-6">
                                        <g:if test="${authorityStr?.contains("ROLE_ORGANIZER")}">
                                            <input class="form-check-input" type="checkbox" name="empRole" checked value="ROLE_ORGANIZER"> ROLE_ORGANIZER
                                        </g:if>
                                        <g:else>
                                            <input class="form-check-input" type="checkbox" name="empRole" value="ROLE_ORGANIZER"> ROLE_ORGANIZER
                                        </g:else>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label  class="col-md-4 control-label">Shift Incharge</label>
                                    <div class="col-md-6">
                                        <g:if test="${authorityStr?.contains("ROLE_SHIFT_INCHARGE")}">
                                            <input class="form-check-input" type="checkbox" name="empRole" checked value="ROLE_SHIFT_INCHARGE"> SHIFT_INCHARGE
                                        </g:if>
                                        <g:else>
                                            <input class="form-check-input" type="checkbox" name="empRole" value="ROLE_SHIFT_INCHARGE"> SHIFT_INCHARGE
                                        </g:else>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label  class="col-md-4 control-label" style="color: #b24b18;">Allow Access to other modules</label>
                                </div>
                                <div class="form-group">
                                    <div class="col-md-offset-4 col-md-6">
                                        Send SMS
                                    </div>
                                    <div class="col-md-offset-4 col-md-6">
                                        Mark Entry
                                    </div>
                                    <div class="col-md-offset-4 col-md-6">
                                        Student Feedback
                                    </div>
                                </div>
                                <div class="form-group">
                                    <div class="col-sm-offset-4 col-sm-8">
                                        <g:if test="${user}">
                                            <button type="submit" id="submitBtn" class="btn btn-primary">Update</button>
                                        </g:if>
                                        <g:else>
                                            <button type="submit" id="submitBtn" class="btn btn-primary">Submit</button>
                                        </g:else>
                                        <button type="reset" id="cancelBtn" class="btn btn-default">Cancel</button>
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
<script>
    jQuery(function ($) {
        var $add_new_user = $('#add_new_user').validate({
            "rules": {
                "username": {
                    "required": true
                }
            },
            messages: {
                "username": "Please enter username"
            },
            errorElement: 'span',
            highlight: function (e) {
                $(e).closest('.form-group').removeClass('has-info').addClass('has-error');
            },
            success: function (e) {
                $(e).closest('.form-group').removeClass('has-error').addClass('has-info');
                $(e).remove();
            },
            submitHandler: function(add_new_user) {
                var stdEmpId = $("#objId").val();
                if(stdEmpId.length === 0){
                    alert('Please select Employee to create user');
                    return false;
                }
                $add_new_user.submit();
            }
        });

        $('.link-url-btn').click(function (e) {
            e.preventDefault();
            window.location.href = "${g.createLink(controller: 'user',action: 'index')}";
        });
        $('#cancelBtn').click(function (e) {
            e.preventDefault();
            window.location.href = "${g.createLink(controller: 'user',action: 'index')}";
        });

        $('#objId').select2({
            placeholder: "Search for a employee [name or id]",
            allowClear: true,
            minimumInputLength:3,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: "${g.createLink(controller: 'remote',action: 'employeeWithDesignationList')}",
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
    });
</script>
</body>
</html>
