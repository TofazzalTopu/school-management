<div class="head_section" style="text-align: center">
    <div class="logo">
        <sec:ifLoggedIn>
            <a href="${g.createLink(controller: 'login', action: 'loginSuccess')}">
                <img src="${assetPath(src: grailsApplication.config.getProperty('app.school.image.folder'))}/school-logo_2.png"
                     alt="${grailsApplication.config.getProperty("app.school.name")}">
            </a>
        </sec:ifLoggedIn>
        <sec:ifNotLoggedIn>
            <a href="${g.createLink(uri: '/')}">
                <img src="${assetPath(src: grailsApplication.config.getProperty('app.school.image.folder'))}/school-logo_2.png"
                     alt="${grailsApplication.config.getProperty("app.school.name")}">
            </a>
        </sec:ifNotLoggedIn>
    </div>
    <div class="title" style="padding-bottom: 33px;">
        <div class="main_title">${grailsApplication.config.getProperty("app.home.school.title")}</div>
        <div class="sub_title">(${grailsApplication.config.getProperty("app.home.school.recognition")}</div>
        <div class="sub_sub_title">${grailsApplication.config.getProperty("app.home.school.estd")}</div>
        <div class="sub_sub_title">${grailsApplication.config.getProperty("app.home.school.mainAddress")} ${grailsApplication.config.getProperty("app.home.header.phone")}</div>
        <div class="sub_sub_title">${grailsApplication.config.getProperty("app.home.school.details")}</div>
    </div>
    <div class="cb"></div>
</div>

<div class="news_section">
    <section class="content" style="min-height: 0px;">
        <div class="row">
            <div class="col-lg-1">News:</div>
            <div class="col-lg-10 top-scroll">
                <notice:scrollTextNew scrollamount="6"/>
            </div>
            <div class="col-lg-1">
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
    </section>
</div>

<div class="menu_section">
    <div class="menu">
        <ul>
            <li class="${params.controller == 'home' && params.action == 'index' ? 'active' : ''}">
                <a href="${g.createLink(uri: '/')}"><div class="menu_item">Home</div></a>
            </li>
            <li class="${params.controller == 'home' && (params.action == 'ourSchool' || params.action == 'managingCommittee' || params.action == 'resources') ? 'active' : ''}">
                <a href="#"><div class="menu_item">About Us <i class="icon-angle-down"></i></div></a>
                <ul>
                    <li><a href="${g.createLink(controller: 'home', action: 'ourSchool')}"><div class="menu_item">${grailsApplication.config.getProperty("app.school.name")}</div></a></li>
                    <li><a href="${g.createLink(controller: 'home', action: 'managingCommittee')}"><div class="menu_item">Managing Committee</div></a></li>
                    <li><a href="${g.createLink(controller: 'home', action: 'resources')}"><div class="menu_item">Teachers And Stuffs</div></a></li>
                </ul>
            </li>
            <li class="${(params.controller == 'calendar' && (params.action == 'index' || params.action == 'notice'))|| (params.controller == 'home' && (params.action == 'classRoutine' || params.action == 'lessonPlan')) ? 'active' : ''}">
                <a href="#"><div class="menu_item">Academic <i class="icon-angle-down"></i></div></a>
                <ul>
                    <li><a href="${g.createLink(controller: 'calendar', action: 'index')}"><div class="menu_item">Academic Calendar</div></a></li>
                    <li><a href="${g.createLink(controller: 'calendar', action: 'notice')}"><div class="menu_item">Notice Board</div></a></li>
                    <li><a href="${g.createLink(controller: 'calendar', action: 'classRoutine')}"><div class="menu_item">Class Routine</div></a></li>
                    <li><a href="${g.createLink(controller: 'calendar', action: 'examRoutine')}"><div class="menu_item">Exam Routine</div></a></li>
                    <li><a href="${g.createLink(controller: 'home', action: 'lessonPlan')}"><div class="menu_item">Lesson Plan</div></a></li>
                </ul>
            </li>
            <li class="${params.controller == 'online' && (params.action == 'apply') ? 'active' : ''}">
                <a href="${g.createLink(controller: 'online', action: 'apply')}"><div class="menu_item">Apply</div></a>
            </li>
            <li class="${params.controller == 'home' && ((params.action == 'image') || (params.action == 'video') || (params.action == 'link')) ? 'active' : ''}">
                <a href="#"><div class="menu_item">Gallery <i class="icon-angle-down"></i></div></a>
                <ul>
                    <li><a href="${g.createLink(controller: 'home', action: 'image')}"><div class="menu_item">Photo Gallery</div></a></li>
                    <li><a href="${g.createLink(controller: 'home', action: 'video')}"><div class="menu_item">Video Gallery</div></a></li>
                    <li><a href="${g.createLink(controller: 'home', action: 'link')}"><div class="menu_item">Important Links</div></a></li>
                </ul>
            </li>
            <li class="${params.controller == 'home' && params.action == 'timeline' ? 'active' : ''}">
                <a href="#"><div class="menu_item">Results<i class="icon-angle-down"></i></div></a>
                <ul>
                    <li><a href="${g.createLink(controller: 'home', action: 'timeline')}"><div class="menu_item">Progress Time Line</div></a></li>
                </ul>
            </li>
            <li class="${params.controller == 'home' && params.action == 'onlineLibrary' ? 'active' : ''}">
                <a href="${g.createLink(controller: 'home', action: 'onlineLibrary')}"><div class="menu_item">Virtual Library</div></a>
            </li>
        </ul>
    </div>
</div>