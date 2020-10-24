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
                    %{--<sec:ifAnyGranted roles="ROLE_SUPER_ADMIN,ROLE_ADMIN,ROLE_SCHOOL_HEAD,ROLE_HR">
                        <li>
                            <a href="${g.createLink(controller: 'portal', action: 'studentList')}">
                                <i class="fa fa-dashboard"></i>
                                <span>Select Student</span>
                            </a>
                        </li>
                    </sec:ifAnyGranted>--}%
                    <sec:ifAnyGranted roles="ROLE_SUPER_ADMIN,ROLE_ADMIN,ROLE_SCHOOL_HEAD,ROLE_SHIFT_INCHARGE,ROLE_HR,ROLE_STUDENT">
                        <li class="${params.controller == 'portal' && params.action == 'attendance' ? 'active' : ''}">
                            <a href="${g.createLink(controller: 'portal', action: 'attendance')}">
                                <i class="fa fa-laptop"></i>
                                <span>Attendance</span>
                            </a>
                        </li>
                        <li class="${params.controller == 'portal' && params.action == 'lessonPlan' ? 'active' : ''}">
                            <a href="${g.createLink(controller: 'portal', action: 'lessonPlan')}">
                                <i class="fa fa-laptop"></i>
                                <span>Lesson Plan</span>
                            </a>
                        </li>
                        <li class="sub-menu">
                            <a href="javascript:;">
                                <i class="fa fa-envelope"></i>
                                <span>Result</span>
                            </a>
                            <ul class="sub">
                                <li class="${params.controller == 'portal' && params.action == 'chart' ? 'active' : ''}">
                                    <a href="${g.createLink(controller: 'portal', action: 'chart')}">Progress Report Card</a>
                                </li>
                                <li class="${params.controller == 'portal' && params.action == 'gradeChart' ? 'active' : ''}">
                                    <a href="${g.createLink(controller: 'portal', action: 'gradeChart')}">Grade Chart</a>
                                </li>
                                <li class="${params.controller == 'portal' && params.action == 'resultComparison' ? 'active' : ''}">
                                    <a href="${g.createLink(controller: 'portal', action: 'resultComparison')}"> Result Comparison Chart</a>
                                </li>
                            </ul>
                        </li>
                        <li class="sub-menu">
                            <a href="javascript:;">
                                <i class="fa fa-dollar"></i>
                                <span>Payment</span>
                            </a>
                            <ul class="sub">
                                <g:set var="paid" value="[paid: true]" />
                                <g:set var="unpaid" value="[paid: false]" />
                                <li class="${params.controller == 'portal' && params.action == 'paymentList' ? 'active' : ''}">
                                    <a href="${g.createLink(controller: 'portal', action: 'paymentList', params: paid)}">Payment History</a>
                                </li>
                                <li class="${params.controller == 'portal' && params.action == 'paymentList' ? 'active' : ''}">
                                    <a href="${g.createLink(controller: 'portal', action: 'paymentList', params: unpaid)}">Payment Due</a>
                                </li>
                                <li class="${params.controller == 'portal' && params.action == 'onlinePayment' ? 'active' : ''}">
                                    <a href="${g.createLink(controller: 'portal', action: 'onlinePayment')}">Online Payment</a>
                                </li>
                            </ul>
                        </li>
                        <li>
                            <a href="${g.createLink(controller: 'portal', action: 'profile')}">
                                <i class="fa fa-user"></i>
                                <span>Profile</span>
                            </a>
                        </li>
                    </sec:ifAnyGranted>
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

</body>
</html>
<script>
    jQuery(function ($) {
        $('ul.sidebar-menu >li >ul.sub >li.active:first').parent().show();
        $('ul.sidebar-menu >li >ul.sub >li.active:first').parent().parent().children('a.pushActive').addClass('active');
    });
</script>