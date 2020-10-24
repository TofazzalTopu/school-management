<section class="toparea">
    <div class="container">
        <div class="row">
            <div class="col-md-3 top-text pull-left animated fadeInLeft">
                <i class="icon-phone"></i>${grailsApplication.config.getProperty("app.home.header.phone")}
            </div>

            <div class="col-md-7 top-text animated" id="recent-notice" style="overflow: hidden;font-size: 16px;">
                <a href="${g.createLink(uri: '/')}"><notice:scrollText/></a>
            </div>

            <div class="col-md-2 text-right animated fadeInRight">
                <div class="social-icons">
                    <a class="icon icon-facebook" href="#"></a>
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
            <li class="${params.controller == 'allContent' && params.action == 'index' ? 'active' : ''}">
                <a href="${g.createLink(controller: 'allContent', action: 'index')}">Home</a>
            </li>

            <li class="dropdown ${(params.action == 'ourSchool' || params.action == 'voices' || params.action == 'faq') ? 'active' : ''}">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Abou Us <i class="icon-angle-down"></i></a>
                <ul class="dropdown-menu">
                    <li><a href="${g.createLink(controller: 'allContent', action: 'ourSchool')}">${grailsApplication.config.getProperty("app.school.name")}</a></li>
                    <li><a href="${g.createLink(controller: 'allContent', action: 'voices')}">Voices</a></li>
                    <li><a href="${g.createLink(controller: 'allContent', action: 'faq')}">FAQ</a></li>

                </ul>
            </li>
            <li class="dropdown ${(params.controller == 'allContent' && params.action == 'impLinks') ? 'active' : ''}">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Gallery <i class="icon-angle-down"></i></a>
                <ul class="dropdown-menu">
                    <li><a href="${g.createLink(controller: 'allContent', action: 'impLinks')}">Important Links</a></li>
                </ul>
            </li>
            <li class="dropdown ${(params.controller == 'allContent' && params.action == 'timeline' ) ? 'active' : ''}">
                <a href="#" class="dropdown-toggle" data-toggle="dropdown">Results <i class="icon-angle-down"></i></a>
                <ul class="dropdown-menu">
                    <li><a href="${g.createLink(controller: 'allContent', action: 'timeline')}">Progress</a></li>
                </ul>
            </li>
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