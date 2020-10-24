<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="open-ltpl"/>
    <title>Teacher And Stuff</title>
    <style>
    .teacher-profile-img{
        width:238px !important;
        height:222px !important;
    }
    </style>
</head>
<body>

<div class="wrapbox">

    <section class="pageheader-default text-center">
        <div class="semitransparentbg">
            <h1 class="animated fadeInLeftBig notransition">${sname}</h1>
        </div>
    </section>
    <div class="wrapsemibox">
        <section class="container">
            <div class="row">
                <div class="col-md-4 animated anim-slide notransition">
                    <div id="accordion">
                        <g:each in="${staffCategories}" var="staffCategory">
                            <div>
                                <h4 id="${staffCategory.keyName}">${staffCategory.name}</h4>
                                <p>
                                    ${staffCategory.description}
                                </p>
                            </div>
                        </g:each>
                    </div>
                </div>
                <g:if test="${headMaster}">
                    <div class="col-md-4">
                        <div class="thumbnail">
                            <g:if test="${headMaster.imagePath}">
                                <img class="teacher-profile-img" src="${imgSrc.fromIdentifier(imagePath: headMaster.imagePath)}"/>
                            </g:if>
                            <g:else>
                                <asset:image class="teacher-profile-img" src="no-image.jpg" alt="avatar"/>
                            </g:else>
                            <div class="caption">
                                <h4>${headMaster.name}</h4>
                                <span class="primarycol"> ${headMaster.hrDesignation?.name} </span>
                                <p>
                                    ${headMaster?.aboutMe}
                                    <br>
                                </p>
                                <ul class="social-icons">
                                    <li><a href="#"><i class="icon-facebook"></i></a></li>
                                    <li><a href="#"><i class="icon-twitter"></i></a></li>
                                    <li><a href="#"><i class="icon-linkedin"></i></a></li>
                                    <li><a href="#"><i class="icon-google-plus"></i></a></li>
                                    <li><a href="#"><i class="icon-pinterest"></i></a></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </g:if>
                <g:if test="${assistantHeadMaster}">
                    <div class="col-md-4">
                        <div class="thumbnail">
                            <g:if test="${assistantHeadMaster?.imagePath}">
                                <img class="teacher-profile-img" src="${imgSrc.fromIdentifier(imagePath: assistantHeadMaster?.imagePath)}"/>
                            </g:if>
                            <g:else>
                                <asset:image class="teacher-profile-img" src="no-image.jpg" alt="avatar"/>
                            </g:else>
                            <div class="caption">
                                <h4>${assistantHeadMaster.name}</h4>
                                <span class="primarycol"> ${assistantHeadMaster.hrDesignation?.name} </span>
                                <p>
                                    ${assistantHeadMaster?.aboutMe}
                                    <br>
                                </p>
                                <ul class="social-icons">
                                    <li><a href="#"><i class="icon-facebook"></i></a></li>
                                    <li><a href="#"><i class="icon-twitter"></i></a></li>
                                    <li><a href="#"><i class="icon-linkedin"></i></a></li>
                                    <li><a href="#"><i class="icon-google-plus"></i></a></li>
                                    <li><a href="#"><i class="icon-pinterest"></i></a></li>
                                </ul>
                            </div>
                        </div>
                    </div>
                </g:if>
            </div>
        </section>
        <g:if test="${teacherList}">
            <section class="container animated notransition topspace10">
                <div class="row">
                    <h1 class="text-center smalltitle">
                        <span>${teacherType?.name}</span>
                    </h1>
                    <g:each in="${teacherList}" var="dataSet" status="i" >
                        <g:if test="${i % 3==0}">
                            <div class="clearfix"></div>
                        </g:if>
                        <div class="col-md-4">
                            <div class="thumbnail">
                                <g:if test="${dataSet.imagePath}">
                                    <img class="img-thumbnail teacher-profile-img" src="${imgSrc.fromIdentifier(imagePath: dataSet?.imagePath)}" alt="Image">
                                </g:if>
                                <g:else>
                                    <img class="img-thumbnail teacher-profile-img" src="${assetPath(src: 'no-image.jpg')}" alt="">
                                </g:else>
                                <div class="caption">
                                    <h4><a href="${createLink(controller: 'home',action: 'userDetail',params: [id :"${dataSet?.id}"])}" target="_blank">${dataSet?.name}</a></h4>
                                    <span class="primarycol"> ${dataSet?.hrDesignation?.name}</span>
                                    <p>
                                        ${dataSet?.aboutMe}
                                        <br>
                                    </p>
                                    <ul class="social-icons">
                                        <li><a href="#"><i class="icon-facebook"></i></a></li>
                                        <li><a href="#"><i class="icon-twitter"></i></a></li>
                                        <li><a href="#"><i class="icon-linkedin"></i></a></li>
                                        <li><a href="#"><i class="icon-google-plus"></i></a></li>
                                        <li><a href="#"><i class="icon-pinterest"></i></a></li>
                                    </ul>
                                </div>
                            </div>
                        </div>
                    </g:each>
                </div>
            </section>
        </g:if>
    </div>
</div>
</div>
<script>
    var activeCategory, accorElem, accorPdiv, accordItems;
    jQuery(function ($) {
        activeCategory = "${stype}";
        accorPdiv = $('#'+activeCategory).parent('div');
        if( !accorPdiv.hasClass('active')) {
            accordItems = $('#accordion div');
            accordItems.removeClass('active');
            accorPdiv.addClass('active');
        }
    });
    /* ---------------------------------------------------------------------- */
    /*	Accordion
     /* ---------------------------------------------------------------------- */
    var clickElem = $('#accordion div h4');
    clickElem.on('click', function(e){
        e.preventDefault();
        var stype = $(this).attr('id');
        var url = "${g.createLink(controller: 'home',action: 'resources')}?stype=" + stype;
        window.location.replace(url);
    });
</script>
</body>
</html>
