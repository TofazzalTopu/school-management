<%@ page import="com.grailslab.enums.OpenCont" defaultCodec="none" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="open-ltpl-admin"/>
    <title>FAQ</title>
</head>

<body>
<section class="pageheader-default text-center">
    <div class="semitransparentbg">
        <h1 class="animated fadeInLeftBig notransition">Knowledge Base</h1>
    </div>
</section>

<div class="wrapsemibox">

    <a class="btn btn-default addBtn" id="${OpenCont.HOME_FAQ.key}"
       href="#"><i class="fa fa fa-plus"></i> &nbsp;&nbsp; Add Item
    </a>

    <g:if test="${messages}">
        <h3 style="color: lightsalmon">${messages}</h3>
    </g:if>

    <section class="container animated fadeInUp notransition">
        <div class="row">
            <g:each in="${faqList}" var="dataSet">
                <div class="col-md-6">
                    <h1 class="faqstitle">${dataSet?.name} <a class="btn btn-default editBtn" id="${dataSet?.id}"
                                                              href="#"><i
                                class="fa fa fa-plus"></i> &nbsp;&nbsp; Edit Item
                    </a> &nbsp;&nbsp; <a class="btn btn-default addBtnQus" id="${dataSet?.id}"
                           href="#"><i
                                class="fa fa fa-plus"></i> &nbsp;&nbsp; Add Question
                    </a>
                    </h1>

                    <dl class="faqs">

                <g:each in="${dataSet.faqQuestion}" var="dataSetSub">
                    <dt>${dataSetSub?.name}</dt>
                    <dd>${dataSetSub?.answers}&nbsp;&nbsp;&nbsp;&nbsp; <a class="btn btn-default editQuestion"  id="${dataSetSub?.id}" href="#"><i class="icon-edit"></i></a></dd>
                </g:each>
                    </dl>
                </div>
            </g:each>
        </div>
    </section>
</div>
<script>
    $(document).ready(function () {
        $('.faqs dd').hide(); // Hide all DDs inside .faqs
        $('.faqs dt').hover(function () {
            $(this).addClass('hover')
        }, function () {
            $(this).removeClass('hover')
        }).click(function () { // Add class "hover" on dt when hover
            $(this).next().slideToggle('normal'); // Toggle dd when the respective dt is clicked
        });
    });
</script>
<script>
    jQuery(function ($) {
        $('.addBtn').click(function (e) {
            var value = $(this).attr('id');
            var url = "${g.createLink(controller: 'allContent',action: 'saveFaqPage')}?key=" + value+"&prevController=${params.controller}&prevAction=${params.action}";
            window.location.replace(url);
            e.preventDefault();
        });

        $('.addBtnQus').click(function (e) {
            var value = $(this).attr('id');
            var url = "${g.createLink(controller: 'allContent',action: 'saveFaqQusPage')}?catId=" + value+"&prevController=${params.controller}&prevAction=${params.action}";
            window.location.replace(url);
            e.preventDefault();
        });

        $('.editBtn').click(function (e) {
            var value = $(this).attr('id');
            var url = "${g.createLink(controller: 'allContent',action: 'saveFaqPage')}?id=" + value+"&prevController=${params.controller}&prevAction=${params.action}";
            window.location.replace(url);
            e.preventDefault();
        });
        $('.addQusBtn').click(function (e) {
            var value = $(this).attr('id');
            var url = "${g.createLink(controller: 'allContent',action: 'saveFaqPage')}?id=" + value+"&prevController=${params.controller}&prevAction=${params.action}";
            window.location.replace(url);
            e.preventDefault();
        });
        $('.editQuestion').click(function (e) {
            var value = $(this).attr('id');
            var url = "${g.createLink(controller: 'allContent',action: 'saveFaqQusPage')}?id=" + value+"&prevController=${params.controller}&prevAction=${params.action}";
            window.location.replace(url);
            e.preventDefault();
        });
    });
</script>
</body>
</html>