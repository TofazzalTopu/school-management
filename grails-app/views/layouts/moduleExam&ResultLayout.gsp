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
    <script src="https://oss.maxcdn.com/libs/html5shiv/3.7.0/html5shiv.js"></script>
    <script src="https://oss.maxcdn.com/libs/respond.js/1.3.0/respond.min.js"></script>
</head>
<body>
<section id="container">
    <g:render template='/layouts/adminHeaderMenu'/>
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
                    <li class="${params.controller == 'examPeriod' && params.action == 'index' ? 'active' : ''}">
                        <a href="${g.createLink(controller: 'examPeriod', action: 'index')}"><i class="fa fa-pencil-square-o" aria-hidden="true"></i>Exam Period</a>
                    </li>
                    <li class="sub-menu">
                        <a href="javascript:;">
                            <i class="fa fa-file-text-o"></i>
                            <span>Exam & Result</span>
                        </a>
                        <ul class="sub">
                            <li class="${(params.controller == 'exam' && params.action == 'index') || (params.controller == 'exam' && params.action == 'classExams') || (params.controller == 'examSchedule' && params.action == 'classSchedule')|| (params.controller == 'examSchedule' && params.action == 'editAllSchedule') ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'exam', action: 'index')}">Exam & Schedule</a>
                            </li>
                            <li class="${(params.controller == 'exam' && params.action == 'college') ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'exam', action: 'college')}">Exam & Schedule - College</a>
                            </li>
                            <li class="${params.controller == 'markEntry' && (params.action == 'index' || params.action == 'addCtMark' || params.action == 'addHallMark') ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'markEntry', action: 'index')}">Mark Entry</a>
                            </li>
                            <li class="${(params.controller == 'exam' && params.action == 'entry') || (params.controller == 'examMark' && params.action == 'addCtMark') || (params.controller == 'examMark' && params.action == 'addHallMark') ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'exam', action: 'entry')}">Subject Student List</a>
                            </li>

                            <li class="${(params.controller == 'examMark' && params.action == 'markEntry')? 'active' : ''}">
                                <a href="${g.createLink(controller: 'examMark', action: 'markEntry')}">Mark Entry Status</a>
                            </li>
                            <li class="${params.controller == 'result' && params.action == 'ctTabulation' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'result', action: 'ctTabulation')}">Prapare CT Result</a>
                            </li>
                            <li class="${params.controller == 'result' && params.action == 'tabulation' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'result', action: 'tabulation')}">Prapare Hall Result</a>
                            </li>
                            <li class="${params.controller == 'result' && params.action == 'autoPromotion' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'result', action: 'autoPromotion')}">Prepare Auto Promotion</a>
                            </li>
                            <li class="${params.controller == 'result' && params.action == 'index' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'result')}">Results</a>
                            </li>
                            <li class="${params.controller == 'result' && params.action == 'college' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'result', action: 'college')}">Results (College)</a>
                            </li>
                            <li class="${params.controller == 'result' && params.action == 'analysis' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'result', action: 'analysis')}">Result Analysis</a>
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
                            <li class="${params.controller == 'eas' && (params.action == 'index' || params.action == 'easEntry') ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'eas', action: 'index')}">EAS Entry </a>
                            </li>
                        </ul>
                    </li>
                    <li class="sub-menu">
                        <a href="javascript:;">
                            <i class="fa fa-reply-all"></i>
                            <span>Evaluation</span>
                        </a>
                        <ul class="sub">
                            <li class="${params.controller == 'evaluation' && params.action == 'schedule' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'evaluation', action: 'schedule')}">Schedule</a>
                            </li>
                            <li class="${params.controller == 'evaluation' && params.action == 'entry' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'evaluation', action: 'entry')}">Entry</a>
                            </li>
                            <li class="${params.controller == 'evaluation' && params.action == 'report' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'evaluation', action: 'report')}">Report Print</a>
                            </li>
                        </ul>
                    </li>
                </ul>
            </div>
            <g:render template='/appVersion'/>
        </div>
    </aside>
    <section id="main-content">
        <section class="wrapper">
            <g:layoutBody/>
        </section>
    </section>
</section>
</body>
</html>
<script>
    jQuery(function ($) {
        $('ul.sidebar-menu >li >ul.sub >li.active:first').parent().show();
        $('ul.sidebar-menu >li >ul.sub >li.active:first').parent().parent().children('a.pushActive').addClass('active');
    });
</script>