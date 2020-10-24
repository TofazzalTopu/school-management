<!DOCTYPE html>
<head>
    <title><g:message code="login"/></title>
    <meta name="layout" content="logintpl"/>
    <!-- start: META -->
    <meta charset="utf-8">
    <!--[if IE]><meta http-equiv='X-UA-Compatible' content="IE=edge,IE=9,IE=8,chrome=1" /><![endif]-->
    <meta name="viewport"
          content="width=device-width, initial-scale=1.0, user-scalable=0, minimum-scale=1.0, maximum-scale=1.0">
    <meta name="apple-mobile-web-app-capable" content="yes">
    <meta name="apple-mobile-web-app-status-bar-style" content="black">
    <meta content="" name="description">
    <meta content="" name="author">
</head>
<!-- end: HEAD -->
<!-- start: BODY -->
<body class="gray-bg" >
<div class="main-login col-sm-4 col-sm-offset-4">
    <div class="logo">${grailsApplication.config.getProperty("app.school.name")}</div>
    <!-- start: LOGIN BOX -->
    <div class="middle-box text-center animated fadeInDown">
        <h1>Access Denied</h1>

        <h3 class="font-bold">You Are Not Authorize For This Page</h3>

        <div class="error-desc">
                <a href="${createLink(controller: 'login',action: 'auth')}" class="btn btn-primary m-t">Back</a>
        </div>
    </div>

    <!-- end: FORGOT BOX -->
    <!-- start: COPYRIGHT -->
    <div class="copyright">
        <g:message code="company.copy"/>
    </div>
    <!-- end: COPYRIGHT -->
</div>
</body>
</html>
