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
                        <a href="${g.createLink(controller: 'library')}"
                           class="${params.controller == 'library' && params.action == 'index' ? 'active' : ''}">
                            <i class="fa fa-dashboard"></i>
                            <span>Dashboard</span>
                        </a>
                    </li>
                    <li class="sub-menu">
                        <a class="pushActive" href="javascript:;">
                            <i class="fa fa-tasks"></i>
                            <span>Book Settings</span>
                        </a>
                        <ul class="sub">
                            <li class="${params.controller == 'libraryMember' && params.action == 'index' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'libraryMember', action: 'index')}">Library Membership</a>
                            </li>
                            <li class="${params.controller == 'librarySetting' && params.action == 'index' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'librarySetting', action: 'index')}">Configuration</a>
                            </li>
                            <li class="${params.controller == 'bookCategory' && params.action == 'index' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'bookCategory', action: 'index')}">Book Category</a>
                            </li>
                            <li class="${params.controller == 'bookAuthor' && params.action == 'index' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'bookAuthor', action: 'index')}">Author</a>
                            </li>
                            <li class="${params.controller == 'bookPublisher' && params.action == 'index' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'bookPublisher', action: 'index')}">Publisher</a>
                            </li>
                            <li class="${params.controller == 'library' && params.action == 'bookDetail' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'library', action: 'bookDetail')}">Book Details</a>
                            </li>
                            <li class="${params.controller == 'bookStock' && params.action == 'index' ? 'active' : ''}">
                                <a href="${g.createLink(controller: 'bookStock', action: 'index')}">Book Stock</a>
                            </li>
                        </ul>
                    </li>
                    <li>
                        <a href="${g.createLink(controller: 'bookTransaction', action: 'bookIssue')}"
                           class="${params.controller == 'bookTransaction' && params.action == 'bookIssue' ? 'active' : ''}">
                            <i class="fa fa-dashboard"></i>
                            <span>Book Issue</span>
                        </a>
                    </li>
                    <li>
                        <a href="${g.createLink(controller: 'bookTransaction', action: 'bookIssueReturn')}"
                           class="${params.controller == 'bookTransaction' && params.action == 'bookIssueReturn' ? 'active' : ''}">
                            <i class="fa fa-dashboard"></i>
                            <span>Issue and Return (Barcode)</span>
                        </a>
                    </li>
                    <li>
                        <a href="${g.createLink(controller: 'libraryReport', action: 'index')}"
                           class="${params.controller == 'libraryReport' && params.action == 'index' ? 'active' : ''}">
                            <i class="fa fa-file-text"></i>
                            <span>Book Report</span>
                        </a>
                    </li>
                    <li>
                        <a href="${g.createLink(controller: 'onlineLibrary', action: 'bookCategory')}"
                           class="${params.controller == 'onlineLibrary' && params.action == 'bookCategoryList' ? 'active' : ''}">
                            <i class="fa fa-file-text"></i>
                            <span>Virtual Library</span>
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

</body>
</html>
<script>
    jQuery(function ($) {
        $('ul.sidebar-menu >li >ul.sub >li.active:first').parent().show();
        $('ul.sidebar-menu >li >ul.sub >li.active:first').parent().parent().children('a.pushActive').addClass('active');
    });
</script>