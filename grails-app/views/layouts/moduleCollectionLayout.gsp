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
                <a href="${g.createLink(controller: 'collections')}"
                   class="${params.controller == 'collections' && params.action == 'index' ? 'active' : ''}">
                    <i class="fa fa-tachometer"></i>
                    <span>Dashboard</span>
                </a>
            </li>
            <li class="sub-menu">
                <a class="pushActive" href="javascript:;">
                    <i class="fa fa-money"></i>
                    <span>Collection</span>
                </a>
                <ul class="sub">
                    <li class="${params.controller == 'collections' && params.action == 'manageQuickFee' ? 'active' : ''}">
                        <a href="${g.createLink(controller: 'collections', action: 'manageQuickFee')}">Manage Quick Fee</a>
                    </li>
                    <li class="${params.controller == 'collections' && (params.action == 'byClass' || params.action == 'byClassStep') ? 'active' : ''}">
                        <a href="${g.createLink(controller: 'collections', action: 'byClass')}">By Class</a>
                    </li>
                    <li class="${params.controller == 'collections' && (params.action == 'byStudent'  || params.action == 'byStudentStep2') ? 'active' : ''}">
                        <a href="${g.createLink(controller: 'collections', action: 'byStudent')}">By Student</a>
                    </li>
                    %{--<li class="${params.controller == 'invoiceDay' && params.action == 'index' ? 'active' : ''}">
                        <a href="${g.createLink(controller: 'invoiceDay', action: 'index')}">Collection Day</a>
                    </li>--}%
                    <li class="${params.controller == 'collections' && params.action == 'manage' ? 'active' : ''}">
                        <a href="${g.createLink(controller: 'collections', action: 'manage')}">Manage Collection</a>
                    </li>
                    <li class="${params.controller == 'collections' && params.action == 'studentPay' ? 'active' : ''}">
                        <a href="${g.createLink(controller: 'collections', action: 'studentPay')}">Student Pay</a>
                    </li>
                </ul>
            </li>
            <li class="sub-menu">
                <a class="pushActive" href="javascript:;">
                    <i class="fa fa-cc-paypal"></i>
                    <span>Report</span>
                </a>
                <ul class="sub">
                    <li class="${params.controller == 'accountsReport' && params.action == 'report' ? 'active' : ''}">
                        <a href="${g.createLink(controller: 'accountsReport', action: 'report')}">Collection Report</a>
                    </li>
                    <li class="${params.controller == 'accountsReport' && params.action == 'stdPayReports' ? 'active' : ''}">
                        <a href="${g.createLink(controller: 'accountsReport', action: 'stdPayReports')}">Student Pay Report</a>
                    </li>
                    <li class="${params.controller == 'collections' && params.action == 'feePay' ? 'active' : ''}">
                        <a href="${g.createLink(controller: 'collections', action: 'feePay')}">Fee Pay Status</a>
                    </li>
                    <li class="${params.controller == 'collections' && params.action == 'monthlyFeeDues' ? 'active' : ''}">
                        <a href="${g.createLink(controller: 'collections', action: 'monthlyFeeDues')}">Monthly Fee Pay/Due</a>
                    </li>
                    <li class="${params.controller == 'collections' && params.action == 'monthWise' ? 'active' : ''}">
                        <a href="${g.createLink(controller: 'collections', action: 'monthWise')}">Fee Collection</a>
                    </li>
                </ul>
            </li>
            <li class="sub-menu">
                <a class="pushActive" href="javascript:;">
                    <i class="fa fa-cc-paypal"></i>
                    <span>Fee Items</span>
                </a>
                <ul class="sub">
                    <li class="${params.controller == 'chart' && params.action == 'fees' ? 'active' : ''}">
                        <a href="${g.createLink(controller: 'chart', action: 'fees')}">Chart of Accounts</a>
                    </li>
                    <li class="${params.controller == 'chart' && (params.action == 'classFees'  || params.action == 'manageActivationFees') ? 'active' : ''}">
                        <a href="${g.createLink(controller: 'chart', action: 'classFees')}">Class Fees</a>
                    </li>
                    <li class="${params.controller == 'chart' && params.action == 'commonFees'? 'active' : ''}">
                        <a href="${g.createLink(controller: 'chart', action: 'commonFees')}">Common Fees</a>
                    </li>
                </ul>
            </li>
            %{--<li class="sub-menu">
                <a class="pushActive" href="javascript:;">
                    <i class="fa fa-coffee"></i>
                    <span>Cafeteria</span>
                </a>
                <ul class="sub">
                    <li class="${params.controller == 'chart' && params.action == 'cafeteria' ? 'active' : ''}">
                        <a href="${g.createLink(controller: 'chart', action: 'cafeteria')}">Cafeteria Items</a>
                    </li>
                </ul>
            </li>--}%
            %{--<li class="sub-menu">
                <a class="pushActive" href="javascript:;">
                    <i class="fa fa-book"></i>
                    <span>Books & Stationary</span>
                </a>
                <ul class="sub">
                    <li class="${params.controller == 'chart' && params.action == 'classStationary' ? 'active' : ''}">
                        <a href="${g.createLink(controller: 'chart', action: 'classStationary')}">Class Items</a>
                    </li>
                    <li class="${params.controller == 'chart' && params.action == 'commonStationary' ? 'active' : ''}">
                        <a href="${g.createLink(controller: 'chart', action: 'commonStationary')}">Common Items</a>
                    </li>
                </ul>
            </li>--}%

            <li class="sub-menu">
                <a class="pushActive" href="javascript:;">
                    <i class="fa fa-credit-card"></i>
                    <span>Discount</span>
                </a>
                <ul class="sub">
                    <li class="${params.controller == 'discount' && params.action == 'index' ? 'active' : ''}">
                        <a href="${g.createLink(controller: 'discount', action: 'index')}">Student Discount</a>
                    </li>
                    <li class="${params.controller == 'discount' && params.action == 'scholarship' ? 'active' : ''}">
                        <a href="${g.createLink(controller: 'discount', action: 'scholarship')}">Scholarship</a>
                    </li>
                </ul>
            </li>
            <li class="sub-menu">
                <a class="pushActive" href="javascript:;">
                    <i class="fa fa-money"></i>
                    <span>Bkash Payment</span>
                </a>
                <ul class="sub">

                    <li class="${params.controller == 'bkash' && params.action == 'index' ? 'active' : ''}">
                        <a href="${g.createLink(controller: 'bkash', action: 'index')}">Manage Collection</a>
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