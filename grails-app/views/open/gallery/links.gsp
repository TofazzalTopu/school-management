<%@ page import="com.grailslab.enums.OpenCont" defaultCodec="none" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="open-ltpl"/>
    <title>Important Link</title>
</head>

<body>
<section class="pageheader-default text-center">
    <div class="semitransparentbg">
        <h1 class="animated fadeInLeftBig notransition">Knowledge Base</h1>
    </div>
</section>
    <div class="wrapsemibox">
        <div class="container notransition">
            <div class="row">
                <section class="container animated notransition topspace10 fadeInDown">
                    <div class="container animated notransition">
                        <h2 class="text-center colortext">Quick Links</h2>
                        <div class="br-hr type_short">
                            <span class="br-hr-h">
                                <i class="icon-pencil"></i>
                            </span>
                        </div>
                        <div class="col-md-12 inline-block" style="padding-left: 25px; padding-right: 10px;">
                            <g:each in="${galleryQuickLinks}" var="galleryLinks">
                            <div class="col-md-3">
                                <div class="singledown clearfix">
                                    <img src="${assetPath(src: 'archive2.jpg')}">
                                    <h2><a href="${galleryLinks?.linkUrl}" target="_blank">${galleryLinks?.displayName}</a></h2>
                                </div>
                            </div>
                            </g:each>
                        </div>
                    </div>
                </section>
            </div>
        </div>
    </div>
</body>
</html>