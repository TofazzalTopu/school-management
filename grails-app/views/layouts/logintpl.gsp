<!DOCTYPE html>
<html lang="en">
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title><g:layoutTitle/> ${grailsApplication.config.getProperty("app.header.title")}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="shortcut icon" href="${assetPath(src: grailsApplication.config.getProperty('app.school.image.folder') + '/favicon.ico')}"
          type="image/x-icon">
    <link rel="apple-touch-icon" href="${assetPath(src: 'apple-touch-icon.png')}">
    <link rel="apple-touch-icon" sizes="114x114" href="${assetPath(src: 'apple-touch-icon-retina.png')}">
    <asset:stylesheet src="login.css"/>
    <asset:javascript src="login.js"/>
    <g:layoutHead/>
</head>

<body class="login-body">
    <g:layoutBody/>
</body>
</html>