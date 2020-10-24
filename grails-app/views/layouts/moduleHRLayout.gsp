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
                            <i class="fa fa-tasks"></i>
                            <span>Settings</span>
                        </a>
                        <ul class="sub">
                            <li class="${params.controller == 'hrSettings' && params.action == 'certification' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'hrSettings', action: 'certification')}">Add Certification</a>
                            </li>
                            <li class="${params.controller == 'hrSettings' && params.action == 'board' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'hrSettings', action: 'board')}">Add Board</a>
                            </li>
                            <li class="${params.controller == 'hrSettings' && params.action == 'institution' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'hrSettings', action: 'institution')}">Add Institution</a>
                            </li>

                            <li class="${params.controller == 'hrCategory' && params.action == 'index' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'hrCategory')}">Hr Category</a>
                            </li>
                            <li class="${params.controller == 'hrCategory' && (params.action == 'hrStaffCategory' || params.action == 'staffSorting') ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'hrCategory', action: 'hrStaffCategory')}">Hr Staff Category</a>
                            </li>
                            <li class="${params.controller == 'hrDesignation' && params.action == 'index' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'hrDesignation')}">Add Designation</a>
                            </li>
                            <li class="${params.controller == 'hrPeriod' && params.action == 'index' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'hrPeriod')}">Attendance Hour</a>
                            </li>

                        </ul>
                    </li>

                    <li>
                        <a href="${g.createLink(controller: 'employee')}"
                           class="${params.controller == 'employee' && params.action == 'index' ? 'active' : ''}">
                            <i class="fa fa-female"></i>
                            <span>Employee</span>
                        </a>
                    </li>
                    <li class="sub-menu">
                        <a href="javascript:;">
                            <i class="fa fa-cc-mastercard"></i>
                            <span>Salary</span>
                        </a>
                        <ul class="sub">
                            <li class="${params.controller == 'salarySetup' && params.action == 'salMasterSetup' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'salarySetup', action: 'salMasterSetup')}">Generate Salary</a>
                            </li>

                            <li class="${params.controller == 'salarySetup' && params.action == 'index' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'salarySetup', action: 'index')}">Employee Salary Setup</a>
                            </li>

                            <li class="${params.controller == 'salaryAttendance' && params.action == 'index' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'salaryAttendance', action: 'index')}"> Attendance</a>
                            </li>

                            <li class="${params.controller == 'extraClass' && params.action == 'index' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'extraClass', action: 'index')}">Extra Class</a>
                            </li>

                            <li class="${params.controller == 'areaSetup' && params.action == 'index' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'areaSetup', action: 'index')}">Area Setup</a>
                            </li>

                            <li class="${params.controller == 'salAdvance' && params.action == 'index' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'salAdvance', action: 'index')}">Loan & Advance</a>
                            </li>

                            <li class="${params.controller == 'salPc' && params.action == 'index' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'salPc', action: 'index')}">PC</a>
                            </li>

                            <li class="${params.controller == 'salDps' && params.action == 'index' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'salDps',action: 'index')}">DPS</a>
                            </li>

                            <li class="${params.controller == 'salIncrement' && params.action == 'index' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'salIncrement', action: 'index')}">Increment Salary</a>
                            </li>
                            <li class="${params.controller == 'salaryBonus' && params.action == 'index' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'salaryBonus', action: 'index')}">Bonus Sheet</a>
                            </li>
                        </ul>
                    </li>

                    <li>
                        <a href="${g.createLink(controller: 'salaryReport', action: 'index')}"
                           class="${params.controller == 'salaryReport' && params.action == 'index' ? 'active' : ''}">
                            <i class="fa fa-file"></i>
                            <span>Reports</span>
                        </a>
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
<div id="load_popup_modal_show_id" class="modal fade" id=""  tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true"></div>
</body>
</html>
<script>
    jQuery(function ($) {
        $('ul.sidebar-menu >li >ul.sub >li.active:first').parent().show();
        $('ul.sidebar-menu >li >ul.sub >li.active:first').parent().parent().children('a.pushActive').addClass('active');
    });
</script>