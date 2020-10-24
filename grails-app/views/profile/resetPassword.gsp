<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="adminLayout"/>
    <title>Reset Password</title>
</head>
<body>
<grailslab:breadCrumbActions firstBreadCrumbText="${sec.loggedInUserInfo(field: 'name')}" firstBreadCrumbUrl="${g.createLink(controller: 'login', action: 'loginSuccess')}"  breadCrumbTitleText="Reset Password"/>
<div class="row" id="create-form-holder">
    <div>
        <div class="col-sm-12">
            <div class="panel">
                <header class="panel-heading">
                    RESET PASSWORD
                </header>
                <div class="panel-body">
                    <div class="row">
                        <div class="col-md-6 col-md-offset-2">
                        <form class="form-horizontal" id="reset_password_form" action="${g.createLink(action: 'saveResetPassword')}" method="post">
                            <g:if test="${flash.message}">
                                <h4 class="text-center" style="color: sienna">${flash.message}</h4>
                            </g:if>
                            <g:render template="/layouts/errors" bean="${command?.errors}"/>
                            <div class="form-group">
                                <label for="oldPassword" class="col-sm-4 control-label">Current password</label>
                                <div class="col-sm-8">
                                    <input type="password" class="form-control left phone-group " name="oldPassword" id="oldPassword" placeholder="Old password">
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="newPassword" class="col-sm-4 control-label">New password</label>
                                <div class="col-sm-8">
                                    <input type="password" class="form-control" name="newPassword" id="newPassword" placeholder=" New password">
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="confirmPassword" class="col-sm-4 control-label">Re type password</label>
                                <div class="col-sm-8">
                                    <input type="password" class="form-control" name="confirmPassword" id="confirmPassword" placeholder="Re type password">
                                </div>
                            </div>

                            <div class="form-group">
                                <div class="col-sm-offset-4 col-sm-8">
                                    <button type="submit" class="btn btn-primary">Submit</button>
                                    <button type="reset" id="cancelBtn" class="btn btn-default">Cancel</button>
                                </div>
                            </div>
                        </form>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    jQuery(function ($) {
    $('#reset_password_form').validate({
        "rules": {
            "oldPassword": {
                "required": true
            },
            "newPassword": {
                "required": true,
                minlength: 5,
                maxlength: 12

            },
            "confirmPassword": {
                "required": true,
                 equalTo: "#newPassword"
            }
        },
        messages: {
            "oldPassword": "Please enter your current password"
        },
        errorElement: 'span',
        highlight: function (e) {
            $(e).closest('.form-group').removeClass('has-info').addClass('has-error');
        },
        success: function (e) {
            $(e).closest('.form-group').removeClass('has-error').addClass('has-info');
            $(e).remove();
        },
        submitHandler: function(reset_password_form) {
            reset_password_form.submit();
        }
    });
    });

</script>


</body>
</html>
