<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="layout" content="logintpl"/>
    <title>Student Registration</title>
</head>

<body class="login-body">

<div class="container">

    <form class="form-signin" role="form" id="form-signin" action="${g.createLink(action: 'registration')}" method="post">
        <h2 class="form-signin-heading">registration now</h2>
        <div class="login-wrap">
            <g:if test='${flash.message}'>
                <div class="errorHandler alert alert-danger">
                    <i class="fa fa-remove-sign"></i>
                    ${flash.message}
                </div>
            </g:if>
            <p>Enter your personal details below</p>
            <input type="text" class="form-control" value="${command?.userId}" name="userId" id="userId" placeholder="User ID" autofocus>
            <input type="text" class="form-control" value="${command?.mobileNo}" name="mobileNo" id="mobileNo" placeholder="Mobile Number" autofocus>
            <div>
                <input class="form-control" value="<g:formatDate format="dd/MM/yyyy" date="${command?.birthDate}"/>" type="text" name="birthDate" id="birthDate" placeholder="Date of Birth"/>
            </div>
            <p> Enter your Access details</p>
                <input type="password" id="password" name="password" class="form-control" placeholder="Password">
                <input type="password"  id="re_password" name="re_password" class="form-control" placeholder="Re-type Password">
            <button class="btn btn-lg btn-login btn-block" type="submit">Submit</button>

            <div class="registration">
                Already Registered.
                <a class="" href="${g.createLink(controller: 'login')}">
                    Login
                </a>
            </div>

        </div>
    </form>
</div>

<script>
    jQuery(function ($) {
        $('#birthDate').datepicker({
            format: 'dd/mm/yyyy',
            autoclose: true
        });
        jQuery.validator.setDefaults({
            success: "valid"
        });
        $("#form-signin").validate({
            rules: {
                studentId: {
                    required: true,
                    maxlength: 12
                },
                mobileNo: {
                    required: true,
                    maxlength: 12
                },
                birthDate: "required",
                password: "required",
                re_password: {
                    equalTo: "#password"
                }
            }
        });
    });
</script>
</body>
</html>
