<!DOCTYPE html>
<html lang="en">
<head>
    <title><g:message code="login"/></title>
    <meta name="layout" content="logintpl"/>
    <title>Login</title>
</head>
<body>
<div class="container">
    <form class="form-signin" action='${postUrl}' method='POST' id='loginForm'>
        <h2 class="form-signin-heading">sign in now</h2>
        <g:if test='${flash.message}'>
            <div class="errorHandler alert alert-danger">
                <i class="fa fa-remove-sign"></i>
                ${flash.message}
            </div>
        </g:if>
        <div class="login-wrap">
            <div class="user-login-info">
                <input type="text" name='j_username' id='username' class="form-control" placeholder="User ID" autofocus>
                <input type="password" placeholder="Password" name='j_password' id='password' class="form-control" placeholder="Password">
            </div>

            <label class="checkbox" style="padding-left: 20px;">
                <input type="checkbox" name='${rememberMeParameter}' id='remember_me'
                       <g:if test='${hasCookie}'>checked='checked'</g:if>/> Remember me
                <span class="pull-right">
                    <a data-toggle="modal" href="#myModal"> Forgot Password?</a>
                </span>
            </label>


            <button class="btn btn-lg btn-login btn-block" type="submit">Sign in</button>

            <div class="registration">
                Don't have an account yet?
                <a class="" href="${g.createLink(action: 'registration')}">
                    Create an account
                </a>
            </div>

        </div>

        <!-- Modal -->
        <div aria-hidden="true" aria-labelledby="myModalLabel" role="dialog" tabindex="-1" id="myModal" class="modal fade">
            <div class="modal-dialog">
                <div class="modal-content">
                    <div class="modal-header">
                        <button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>
                        <h4 class="modal-title">Forgot Password ?</h4>
                    </div>
                    <div class="modal-body">
                        <p>Enter user ID & mobile number to reset your password.</p>



                        <input type="text" class="form-control" name="username" placeholder="User ID">


                        <input type="text" class="form-control" name="mobileNumber" placeholder="Mobile Number">




                    </div>
                    <div class="modal-footer">
                        <button data-dismiss="modal" class="btn btn-default" type="button">Cancel</button>
                        <button class="btn btn-success" type="button">Submit</button>
                    </div>
                </div>
            </div>
        </div>
        <!-- modal -->

    </form>
</div>
</body>
</html>
