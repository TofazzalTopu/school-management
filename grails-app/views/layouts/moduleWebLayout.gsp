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
                            <i class="fa fa-envelope-o"></i>
                            <span>Web Content</span>
                        </a>
                        <ul class="sub">
                            <li class="${params.controller == 'sliderImage' && (params.action == 'index' || params.action == 'edit' || params.action == 'create') ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'sliderImage', action: 'index')}">Slider Image</a>
                            </li>
                            <li class="${params.controller == 'classRoutine' && (params.action == 'index' || params.action == 'edit' || params.action == 'create') ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'classRoutine', action: 'index')}">Class Routine Image</a>
                            </li>
                            <li class="${params.controller == 'feature' &&(params.action == 'index' || params.action == 'edit') ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'feature', action: 'index')}">Feature Content</a>
                            </li>
                            <li class="${params.controller == 'mgmtVoice' && (params.action == 'index' || params.action == 'edit') ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'mgmtVoice', action: 'index')}">Voice And Committee</a>
                            </li>
                            <li class="${params.controller == 'successStory' && (params.action == 'index' || params.action == 'create') ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'successStory', action: 'index')}">Success Story</a>
                            </li>
                            <li class="${params.controller == 'quickLink' && params.action == 'index' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'quickLink', action: 'index')}">Quick Link</a>
                            </li>
                            <li class="${params.controller == 'timeLine' &&  (params.action == 'index' || params.action == 'create') ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'timeLine', action: 'index')}">Time Line</a>
                            </li>
                            <li class="${params.controller == 'gallery' && ( params.action == 'picture'|| params.action == 'pictureItem') ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'gallery', action: 'picture')}">Image Gallery</a>
                            </li>
                            <li class="${params.controller == 'gallery' && params.action == 'video' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'gallery', action: 'video')}">Video Gallery</a>
                            </li>
                        </ul>
                    </li>
                    <li class="sub-menu">
                        <a href="javascript:;">
                            <i class="fa fa-envelope-o"></i>
                            <span>Science & Cultural Festival</span>
                        </a>
                        <ul class="sub">
                            <li class="${params.controller == 'festival' && (params.action == 'index' || params.action == 'edit' || params.action == 'create') ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'festival', action: 'index')}">Festival</a>
                            </li>
                            <li class="${params.controller == 'festival' &&(params.action == 'registration') ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'festival', action: 'registration')}">Registration</a>
                            </li>
                            <li class="${params.controller == 'festival' &&(params.action == 'report') ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'festival', action: 'report')}">Report</a>
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

</body>
</html>
<script>
    jQuery(function ($) {
        $('ul.sidebar-menu >li >ul.sub >li.active:first').parent().show();
        $('ul.sidebar-menu >li >ul.sub >li.active:first').parent().parent().children('a.pushActive').addClass('active');
    });
</script>