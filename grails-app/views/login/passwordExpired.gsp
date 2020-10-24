<!DOCTYPE html>
<html lang="en">
<head>
    <meta name="layout" content="logintpl"/>
    <title>Reset Password</title>
</head>
<body>
<div class="container">
    <form class="form-signin" action='updatePassword' method='POST' id='loginForm'>
        <h2 class="form-signin-heading">Update Password</h2>
        <g:if test='${flash.message}'>
            <div class="errorHandler alert alert-danger">
                <i class="fa fa-remove-sign"></i>
                ${flash.message}
            </div>
        </g:if>
        <div class="login-wrap">
            <div class="user-login-info">
                <input type="password" name='password' id='password' class="form-control" placeholder="Current Password" autofocus>
                <input type="password" name='password_new' id='password_new' class="form-control" placeholder="New Password">
                <input type="password" name='password_confirm' id='password_confirm' class="form-control" placeholder="New Password (again)">
            </div>
            <button class="btn btn-lg btn-login btn-block" type="submit">Reset</button>

            <div class="registration">
                Don't have an account yet?
                <a class="" href="${g.createLink(action: 'registration')}">
                    Create an account
                </a>
            </div>

        </div>
    </form>
</div>
</body>
</html>
