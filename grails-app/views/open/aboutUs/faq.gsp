<%@ page import="com.grailslab.enums.OpenCont" defaultCodec="none" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="open-ltpl"/>
    <title>FAQ</title>
</head>
<body>
<section class="pageheader-default text-center">
    <div class="semitransparentbg">
        <h1 class="animated fadeInLeftBig notransition">Knowledge Base</h1>
    </div>
</section>
<div class="wrapsemibox">
    <section class="container animated fadeInUp notransition">
        <div class="row">
            <g:each in="${faqList}" var="dataSet">
                <div class="col-md-6">
                    <h1 class="faqstitle">${dataSet?.name}</h1>

                    <dl class="faqs">

                        <g:each in="${dataSet.faqQuestion}" var="dataSetSub">
                            <dt>${dataSetSub?.name}</dt>
                            <dd>${dataSetSub?.answers}</dd>
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
</body>
</html>