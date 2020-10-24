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
                    <a href="${g.createLink(controller: 'lesson')}"
                       class="${params.controller == 'lesson' && params.action == 'index' ? 'active' : ''}">
                        <i class="fa fa-calendar"></i>
                        <span>Dashboard</span>
                    </a>
                </li>

                <li class="sub-menu">
                    <a class="pushActive" href="javascript:;">
                        <i class="fa fa-laptop"></i>
                        <span>Lesson Plan</span>
                    </a>
                    <ul class="sub">
                        <li class="${params.controller == 'lesson' && (params.action == 'lessonDetail' || params.action == 'lessonPlan') ? 'active' : ''}">
                            <a href="${g.createLink(controller: 'lesson', action: 'lessonDetail')}">Lesson Plan</a>
                        </li>
                        <li class="${params.controller == 'lesson' && (params.action == 'add' || params.action == 'edit' || params.action == 'create') ? 'active' : ''}">
                            <a href="${g.createLink(controller: 'lesson', action: 'add')}">Manage Lesson</a>
                        </li>
                        <li class="${params.controller == 'lesson' && (params.action == 'report') ? 'active' : ''}">
                            <a href="${g.createLink(controller: 'lesson', action: 'report')}">Report</a>
                        </li>
                    </ul>
                </li>
                <li class="sub-menu">
                    <a class="pushActive" href="javascript:;">
                        <i class="fa fa-laptop"></i>
                        <span>Feedback</span>
                    </a>
                    <ul class="sub">
                        <li class="${params.controller == 'feedback' && params.action == 'index' ? 'active' : ''}">
                            <a href="${g.createLink(controller: 'feedback', action: 'index')}">Student Feedback List</a>
                        </li>
                        <li class="${params.controller == 'feedback' && params.action == 'addFeedback' ? 'active' : ''}">
                            <a href="${g.createLink(controller: 'feedback', action: 'addFeedback')}">Add Feedback</a>
                        </li>
                        <li class="${params.controller == 'feedbackReport' ? 'active' : ''}">
                            <a href="${g.createLink(controller: 'feedbackReport', action: 'report')}">Feedback Reports</a>
                        </li>
                    </ul>
                </li>
     %{--           <li class="sub-menu">
                    <a href="javascript:;">
                        <i class="fa fa-laptop"></i>
                        <span>Exam & Mark Entry</span>
                    </a>
                    <ul class="sub">
                        <li class="${params.controller == 'markEntry' && (params.action == 'index' || params.action == 'addCtMark' || params.action == 'addHallMark') ? 'active' : ''}">
                            <a href="${g.createLink(controller: 'markEntry', action: 'index')}">Mark Entry</a>
                        </li>
                    </ul>
                </li>


                <li class="sub-menu">
                    <a href="javascript:;">
                        <i class="fa fa-reply-all"></i>
                        <span>Attendance & Previous Term </span>
                    </a>
                    <ul class="sub">

                        <li class="${params.controller == 'previousTerm' && (params.action == 'attendance'|| params.action == 'workDays') ? 'active' : ''}">
                            <a href="${g.createLink(controller: 'previousTerm', action: 'attendance')}">Attendance Entry </a>
                        </li>

                        <li class="${params.controller == 'previousTerm' && (params.action == 'ctMark'|| params.action == 'ctWorkDays') ? 'active' : ''}">
                            <a href="${g.createLink(controller: 'previousTerm', action: 'ctMark')}">Previous CT Mark Entry </a>
                        </li>


                    </ul>
                </li>--}%

            </ul>
        </div>
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

</body>
</html>
<script>
    jQuery(function ($) {
        $('ul.sidebar-menu >li >ul.sub >li.active:first').parent().show();
        $('ul.sidebar-menu >li >ul.sub >li.active:first').parent().parent().children('a.pushActive').addClass('active');
    });
</script>