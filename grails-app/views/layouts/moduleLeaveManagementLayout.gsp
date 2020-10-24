<!DOCTYPE html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <title><g:layoutTitle/> ${grailsApplication.config.getProperty("app.header.title")}</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="shortcut icon" href="${assetPath(src: grailsApplication.config.getProperty('app.school.image.folder') + '/favicon.ico')}"
          type="image/x-icon">
    <link rel="apple-touch-icon" href="${assetPath(src: 'apple-touch-icon.png')}">
    <link rel="apple-touch-icon" sizes="114x114" href="${assetPath(src: 'apple-touch-icon-retina.png')}">
    <asset:stylesheet src="application.css"/>
    <asset:javascript src="application.js"/>
    <g:layoutHead/>
    <!-- HTML5 shim and Respond.js IE8 support of HTML5 elements and media queries -->
    <!--[if lt IE 9]>
            <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
            <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
        <![endif]-->
</head>
<body>
<section id="container">
    <!--header start-->
    <g:render template='/layouts/adminHeaderMenu'/>
    <!--header end-->

    <aside>
        <div id="sidebar" class="nav-collapse">
            <div class="leftside-navigation">
                <ul class="sidebar-menu" id="nav-accordion">
                    <li>
                        <a href="${g.createLink(controller: 'login', action: 'loginSuccess')}">
                            <i class="fa fa-dashboard"></i>
                            <span>Dashboard</span>
                        </a>
                    </li>

                    <li class="sub-menu">
                        <a href="javascript:;">
                            <i class="fa fa-linux"></i>
                            <span>Leave</span>
                        </a>
                        <ul class="sub">
                            <li class="${params.controller == 'leaveName' && (params.action == 'index' || params.action == 'create' || params.action == 'edit' )  ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'leaveName')}">Leave Name</a>
                            </li>
                            <li class="${params.controller == 'leaveTemplate' && (params.action == 'index' || params.action == 'create' || params.action == 'edit' )  ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'leaveTemplate')}">Application Template</a>
                            </li>
                            <li class="${params.controller == 'leaveApproval' && (params.action == 'index' || params.action == 'addLeaveApplication') ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'leaveApproval', action: 'index')}">Leave Application</a>
                            </li>
                            <li class="${params.controller == 'leaveReport' && (params.action == 'index' || params.action == 'leaveReport') ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'leaveReport', action: 'index')}">Report</a>
                            </li>

                        </ul>
                    </li>
                </ul>
            </div>
            <g:render template='/appVersion'/>
        </div>
    </aside>
    <!--sidebar end-->
    <!--main content start-->
    <section id="main-content">
        <section class="wrapper">
            <!-- page start-->
            <g:layoutBody/>
            <!-- page end-->
        </section>
    </section>
    <!--main content end-->
</section>
<script>
    jQuery(function ($) {
        $('ul.sidebar-menu >li >ul.sub >li.active:first').parent().show();
        $('ul.sidebar-menu >li >ul.sub >li.active:first').parent().parent().children('a.pushActive').addClass('active');
    });
</script>
</body>
</html>
