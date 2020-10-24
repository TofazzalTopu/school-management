%{--Left Menu for Admin login start--}%

<!-- sidebar menu start-->
<sec:ifAnyGranted roles="ROLE_SUPER_ADMIN,ROLE_ADMIN,ROLE_SHIFT_INCHARGE">
    <div class="leftside-navigation">
        <ul class="sidebar-menu" id="nav-accordion">
            <li>
                <a href="${g.createLink(controller: 'admin')}"
                   class="${params.controller == 'admin' && params.action == 'index' ? 'active' : ''}">
                    <i class="fa fa-dashboard"></i>
                    <span>Dashboard</span>
                </a>
            </li>
            <li class="sub-menu">
                <a href="javascript:;">
                    <i class="fa fa-book"></i>
                    <span>Academic</span>
                </a>
                <ul class="sub">
                    <li class="${params.controller == 'noticeBoard' && params.action == 'index' ? 'active' : ''}">
                        <a href="${g.createLink(controller: 'noticeBoard', action: 'index')}">Notice Board</a>
                    </li>
                    <li class="${params.controller == 'lessonWeek' && params.action == 'week' ? 'active' : ''}">
                        <a href="${g.createLink(controller: 'lessonWeek', action: 'week')}">Lesson Week</a>
                    </li>
                </ul>
            </li>
            <sec:ifAnyGranted roles="ROLE_SUPER_ADMIN,ROLE_ADMIN">
                <li>
                    <a href="${g.createLink(controller: 'user')}"
                       class="${params.controller == 'user' && params.action == 'index' ? 'active' : ''}">
                        <i class="fa fa-users"></i>
                        <span>Manage User</span>
                    </a>
                </li>
            </sec:ifAnyGranted>
            <sec:ifAnyGranted roles="ROLE_SUPER_ADMIN">
                <li>
                    <a href="${g.createLink(controller: 'school', action: 'index')}"
                       class="${params.controller == 'school' && params.action == 'index' ? 'active' : ''}">
                        <i class="fa fa-users"></i>
                        <span>School</span>
                    </a>
                </li>
                <li>
                    <a href="${g.createLink(controller: 'acaYear', action: 'index')}"
                       class="${params.controller == 'acaYear' && params.action == 'index' ? 'active' : ''}">
                        <i class="fa fa-users"></i>
                        <span>Academic Year</span>
                    </a>
                </li>
            </sec:ifAnyGranted>
        </ul>
    </div>
</sec:ifAnyGranted>


%{--##################################################################  TEACHER LOGIN START    ############################################--}%
%{--Left Menu for TEACHER login START--}%
<sec:ifAnyGranted roles="ROLE_TEACHER">
    <!-- sidebar menu start-->
    <div class="leftside-navigation">
        <ul class="sidebar-menu" id="nav-accordion">
            <li>
                <a href="${g.createLink(controller: 'teacher')}"
                   class="${params.controller == 'teacher' && params.action == 'index' ? 'active' : ''}">
                    <i class="fa fa-calendar"></i>
                    <span>Dashboard</span>
                </a>
            </li>
            <li>
                <a href="${g.createLink(controller: 'markEntry', action: 'index')}"
                   class="${params.controller == 'markEntry' && (params.action == 'index' || params.action == 'addCtMark' || params.action == 'addHallMark') ? 'active' : ''}">
                    <i class="fa fa-users"></i>
                    <span>Mark Entry</span>
                </a>
            </li>
            <li>
                <a href="${g.createLink(controller: 'previousTerm', action: 'attendance')}"
                   class="${params.controller == 'previousTerm' && (params.action == 'attendance'|| params.action == 'workDays') ? 'active' : ''}">
                    <i class="fa fa-users"></i>
                    <span>Attendance Entry (Exam)</span>
                </a>
            </li>
            <li>
                <a href="${g.createLink(controller: 'previousTerm', action: 'ctMark')}"
                   class="${params.controller == 'previousTerm' && (params.action == 'ctMark'|| params.action == 'ctWorkDays') ? 'active' : ''}">
                    <i class="fa fa-users"></i>
                    <span>CT Mark (Previous)</span>
                </a>
            </li>
            <li>
                <a href="${g.createLink(controller: 'eas', action: 'index')}"
                   class="${params.controller == 'eas' && (params.action == 'index' || params.action == 'easEntry') ? 'active' : ''}">
                    <i class="fa fa-users"></i>
                    <span>EAS Entry</span>
                </a>
            </li>
            <li>
                <a href="${g.createLink(controller: 'teacher', action: 'myAttendance')}"
                   class="${params.controller == 'teacher' && params.action == 'myAttendance' ? 'active' : ''}">
                    <i class="fa fa-users"></i>
                    <span>My Attendance</span>
                </a>
            </li>
            <li>
                <a href="${g.createLink(controller: 'leave', action: 'index')}"
                   class="${params.controller == 'leave' && params.action == 'index' ? 'active' : ''}">
                    <i class="fa fa-laptop"></i>
                    <span>Leave</span>
                </a>
            </li>
        </ul>
    </div>
    <!-- sidebar menu end-->

</sec:ifAnyGranted>
%{--Left Menu for TEACHER login END--}%
