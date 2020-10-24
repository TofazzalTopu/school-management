<header class="header fixed-top clearfix">
    <!--logo start-->
    <div class="brand">

        <a href="${g.createLink(controller: 'login', action: 'loginSuccess')}" class="logo">
            <span style="font-size: 20px; color: #FFFFFF" class="nav-label">${grailsApplication.config.getProperty("home.brand")}</span>
        </a>

        <div class="sidebar-toggle-box">
            <div class="fa fa-bars"></div>
        </div>
    </div>
    <!--logo end-->

    <div class="nav notify-row" id="top_menu">
        <!--  notification start -->
        <ul class="nav top-menu">
            <!-- notification dropdown start-->
            <li id="header_notification_bar" class="dropdown">
                <a title="Notification" data-toggle="dropdown" class="dropdown-toggle" href="#">

                    <i class="fa fa-bell-o"></i>
                    <span class="badge bg-warning">3</span>
                </a>
                <ul class="dropdown-menu extended notification">
                    <li>
                        <p>Notifications</p>
                    </li>
                    <li>
                        <div class="alert alert-info clearfix">
                            <span class="alert-icon"><i class="fa fa-bolt"></i></span>

                            <div class="noti-info">
                                <a href="#">Server #1 overloaded.</a>
                            </div>
                        </div>
                    </li>
                    <li>
                        <div class="alert alert-danger clearfix">
                            <span class="alert-icon"><i class="fa fa-bolt"></i></span>
                            <div class="noti-info">
                                <a href="#">Server #2 overloaded.</a>
                            </div>
                        </div>
                    </li>
                    <li>
                        <div class="alert alert-success clearfix">
                            <span class="alert-icon"><i class="fa fa-bolt"></i></span>
                            <div class="noti-info">
                                <a href="#">Server #3 overloaded.</a>
                            </div>
                        </div>
                    </li>

                </ul>
            </li>
            <sec:access url="/stmgst/**">
                <li class="dropdown">
                    <a title="Student Mgmt Module" href="${g.createLink(controller: 'registration', action: 'index')}">
                        %{--<span class="badge bg-warning">Stu MGMT</span>--}%
                        <i><asset:image src="modules/modules_studentmgmt_icon.png"/></i>
                    </a>
                </li>
            </sec:access>
            <sec:access url="/collection/**">
                <li class="dropdown">
                    <a title="Collections" href="${g.createLink(controller: 'collections', action: 'index')}">
                        %{--<span class="badge bg-warning">Stu MGMT</span>--}%
                        <i><asset:image src="modules/module_accounts.png"/></i>
                    </a>
                </li>
            </sec:access>
            <sec:access url="/exam/**">
                <li class="dropdown">
                    <a title="Exam & Result" href="${g.createLink(controller: 'exam', action: 'index')}">
                        %{--<span class="badge bg-warning">Stu MGMT</span>--}%
                        <i><asset:image src="modules/modules_result_icon.png"/></i>
                    </a>
                </li>
            </sec:access>
            <sec:access url="/sms/**">
                <li class="dropdown">
                    <a title="Send SMS" href="${g.createLink(controller: 'messaging', action: 'index')}">
                        %{--<span class="badge bg-warning">Stu MGMT</span>--}%
                        <i><asset:image src="modules/modules_sms_icon.png"/></i>
                    </a>
                </li>
            </sec:access>


            <sec:access url="/web/**">
                <li class="dropdown">
                    <a title="Public Web Content" href="${g.createLink(controller: 'sliderImage', action: 'index')}">
                        %{--<span class="badge bg-warning">Stu MGMT</span>--}%
                        <i><asset:image src="modules/modules_web_icon.png"/></i>
                    </a>
                </li>
            </sec:access>

            <sec:access url="/attn/**">
                <li class="dropdown">
                    <a title="Attendance Management" href="${g.createLink(controller: 'recordDay', action: 'index')}">
                        %{--<span class="badge bg-warning">Stu MGMT</span>--}%
                        <i><asset:image src="modules/module_leave_icon.png"/></i>
                    </a>
                </li>
            </sec:access>
            <sec:access url="/hrm/**">
                <li class="dropdown">
                    <a title="HR Management" href="${g.createLink(controller: 'hr', action: 'index')}">
                        %{--<span class="badge bg-warning">Stu MGMT</span>--}%
                        <i><asset:image src="modules/modules_hr_icon.png"/></i>
                    </a>
                </li>
            </sec:access>

            <sec:access url="/LeaveMgmt/**">
                <li class="dropdown">
                    <a title="Leave Management" href="${g.createLink(controller: 'leaveApproval', action: 'index')}">
                        %{--<span class="badge bg-warning">Stu MGMT</span>--}%
                        <i><asset:image src="modules/module_leave.png"/></i>
                    </a>
                </li>
            </sec:access>
            %{--<sec:access url="/portalSwitch/**">
                <li class="dropdown">
                    <a title="Student/Parent Portal" href="${g.createLink(controller: 'portal', action: 'index')}">
                        --}%%{--<span class="badge bg-warning">Stu MGMT</span>--}%%{--
                        <i><asset:image src="modules/stu_module.png"/></i>
                    </a>
                </li>
            </sec:access>--}%
            <sec:access url="/applicant/**">
                <li class="dropdown">
                    <a title="Online Application Management" href="${g.createLink(controller: 'onlineRegistration', action: 'index')}">
                        %{--<span class="badge bg-warning">Stu MGMT</span>--}%
                        <i><asset:image src="modules/module_online_addmission.png"/></i>
                    </a>
                </li>
            </sec:access>
            <sec:access url="/library/**">
                <li class="dropdown">
                    <a title="Library Management" href="${g.createLink(controller: 'library', action: 'index')}">
                        %{--<span class="badge bg-warning">Stu MGMT</span>--}%
                        <i><asset:image src="modules/module_library.png"/></i>
                    </a>
                </li>
            </sec:access>
            <sec:access url="/lessonPlan/**">
                <li class="dropdown">
                    <a title="Lesson Plan & Feedback Module" href="${g.createLink(controller: 'lesson', action: 'index')}">
                        %{--<span class="badge bg-warning">Stu MGMT</span>--}%
                        <i><asset:image src="modules/lesson_plan_feedbck_icon.png"/></i>
                    </a>
                </li>
            </sec:access>

                <!-- notification dropdown end -->
        </ul>
        <!--  notification end -->
    </div>

    <div class="top-nav clearfix">
        <!--search & user info start-->
        <ul class="nav pull-right top-menu">
            <li>
                <input type="text" class="form-control search" placeholder=" Search">
            </li>
            <!-- user login dropdown start-->
            <li class="dropdown">
                <a data-toggle="dropdown" class="dropdown-toggle" href="#">
                    <asset:image src="avatar1_small.jpg" alt="avatar"/>
                    <span class="username"><sec:loggedInUserInfo field="username"/></span>
                    <b class="caret"></b>
                </a>
                <ul class="dropdown-menu extended logout">
                    <sec:ifSwitched>
                        <li>
                            <a href="${g.createLink(controller: 'logout', action: 'impersonate')}">
                                <i class="fa fa-level-up"></i>Back As <sec:switchedUserOriginalUsername/></a>
                        </li>
                    </sec:ifSwitched>
                    <li><a title="Manage own profile" href="${g.createLink(controller: 'profile', action: 'index')}"><i class=" fa fa-suitcase"></i>Profile</a></li>
                    <li><a title="Reset Password" href="${g.createLink(controller: 'profile', action: 'resetPassword')}"><i class=" fa fa-wrench"></i>Reset Password</a></li>
                    <li><a href="${g.createLink(controller: 'logout')}"><i class="fa fa-key"></i> Log Out</a></li>
                </ul>
            </li>
            <!-- user login dropdown end -->
        </ul>
        <!--search & user info end-->
    </div>
</header>