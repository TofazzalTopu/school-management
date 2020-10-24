<section class="toparea">
    <div class="container">
        <div class="row">
            <div class="col-md-3 top-text pull-left animated fadeInLeft">
                <i class="icon-phone"></i>${grailsApplication.config.getProperty("app.home.header.phone")}
            </div>

            <div class="col-md-7 top-text animated" id="recent-notice" style="overflow: hidden;font-size: 16px;">
                <a href="${g.createLink(uri: '/')}"> <notice:scrollText/></a>
            </div>

            <div class="col-md-2 text-right animated fadeInRight">
                <div class="social-icons">
                    <a class="icon icon-facebook" href="https://www.facebook.com/grailslab" target="_blank"></a>
                    <sec:ifLoggedIn>
                        <a style="float: right;" class="icon" href="${g.createLink(controller: 'logout')}"><span
                                class="glyphicon glyphicon-off"></span></a>
                    </sec:ifLoggedIn>
                    <sec:ifNotLoggedIn>
                        <a style="float: right;" class="icon" href="${g.createLink(controller: 'login')}"><span
                                class="glyphicon glyphicon-user"></span></a>
                    </sec:ifNotLoggedIn>
                </div>

            </div>
        </div>
    </div>
</section>
<!-- /.toparea end-->
<!-- NAV
================================================== -->
<nav class="navbar navbar-fixed-top wowmenu" role="navigation">
    <div class="container">
        <div class="navbar-header">
            <sec:ifLoggedIn>
                <a class="navbar-brand logo-nav" href="${g.createLink(controller: 'login', action: 'loginSuccess')}">
                    <img src="${assetPath(src: grailsApplication.config.getProperty('app.school.image.folder'))}/school-logo.png" alt="${grailsApplication.config.getProperty("app.school.name")}">
                </a>
            </sec:ifLoggedIn>
            <sec:ifNotLoggedIn>
                <a class="navbar-brand logo-nav" href="${g.createLink(uri: '/')}">
                    <img src="${assetPath(src: grailsApplication.config.getProperty('app.school.image.folder'))}/school-logo.png" alt="${grailsApplication.config.getProperty("app.school.name")}">
                </a>
            </sec:ifNotLoggedIn>

        </div>
        <ul id="nav" class="nav navbar-nav pull-right">
            <li class="${params.controller == 'home' && params.action == 'index' ? 'active' : ''}">
                <a href="${g.createLink(uri: '/')}">Home</a>
            </li>

            <li class="dropdown ${params.controller == 'home' && (params.action == 'ourSchool' || params.action == 'managingCommittee' || params.action == 'resources') ? 'active' : ''}">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">About Us <i class="icon-angle-down"></i></a>
                <ul class="dropdown-menu">
                    <li><a href="${g.createLink(controller: 'home', action: 'ourSchool')}">${grailsApplication.config.getProperty("app.school.name")}</a></li>
                    <li><a href="${g.createLink(controller: 'home', action: 'managingCommittee')}">Managing Committee</a></li>
                    <li><a href="${g.createLink(controller: 'home', action: 'resources')}">Teachers And Stuffs</a></li>
                </ul>
            </li>

            <li class="dropdown ${(params.controller == 'calendar' && (params.action == 'index' || params.action == 'notice'))|| (params.controller == 'home' && (params.action == 'classRoutine' || params.action == 'lessonPlan')) ? 'active' : ''}">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Academic <i class="icon-angle-down"></i></a>
                <ul class="dropdown-menu">
                    <li><a href="${g.createLink(controller: 'calendar', action: 'index')}">Academic Calendar</a></li>
                    <li><a href="${g.createLink(controller: 'calendar', action: 'notice')}">Notice Board</a></li>
                    <li><a href="${g.createLink(controller: 'calendar', action: 'classRoutine')}">Class Routine</a></li>
                    <li><a href="${g.createLink(controller: 'calendar', action: 'examRoutine')}">Exam Routine</a></li>
                    <li><a href="${g.createLink(controller: 'home', action: 'lessonPlan')}">Lesson Plan</a></li>

                </ul>
            </li>
            <li class="${params.controller == 'online' && (params.action == 'apply') ? 'active' : ''}">
                <a href="${g.createLink(controller: 'online', action: 'apply')}">Apply</a>
            </li>

            <li class="dropdown ${params.controller == 'home' && ((params.action == 'image') || (params.action == 'video') || (params.action == 'link')) ? 'active' : ''}">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Gallery <i class="icon-angle-down"></i></a>
                <ul class="dropdown-menu">
                    <li><a href="${g.createLink(controller: 'home', action: 'image')}">Photo Gallery</a></li>
                    <li><a href="${g.createLink(controller: 'home', action: 'video')}">Video Gallery</a></li>
                    <li><a href="${g.createLink(controller: 'home', action: 'link')}">Important Links</a></li>
                </ul>
            </li>

            <li class="dropdown ${params.controller == 'home' && params.action == 'timeline' ? 'active' : ''}">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Results<i class="icon-angle-down"></i></a>
                <ul class="dropdown-menu">
                    <li><a href="${g.createLink(controller: 'home', action: 'timeline')}">Progress Time Line</a></li>
                </ul>
            </li>

            <li class="dropdown ${params.controller == 'home' && params.action == 'offlineLibrary' || params.action == 'onlineLibrary' ? 'active' : ''}">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Library<i class="icon-angle-down"></i></a>
                <ul class="dropdown-menu">
                    <li><a href="${g.createLink(controller: 'home', action: 'onlineLibrary')}">Virtual Library</a></li>
                    <li><a href="${g.createLink(controller: 'home', action: 'offlineLibrary')}">Offline Library</a></li>
                </ul>
            </li>

            %{--<li class="${params.controller == 'home' && params.action == 'onlineLibrary' ? 'active' : ''}">
                <a href="${g.createLink(controller: 'home', action: 'onlineLibrary')}">Virtual Library</a>
            </li>--}%
        </ul>
    </div>
</nav>
<script>
    $(document).ready(function () {
        $('#recent-notice').marquee({
            allowCss3Support: true,
            speed: 9000,
            gap: 50,
            delayBeforeStart: 100,
            direction: 'left',
            duplicated: true,
            pauseOnHover: true
        });
    });
</script>