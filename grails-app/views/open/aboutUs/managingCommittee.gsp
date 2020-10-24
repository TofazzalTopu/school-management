<%@ page defaultCodec="none" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="open-ltpl"/>
    <title>Managing Committee</title>

    <style>
    .content {
        padding-top: 30px;
    }

    /* Testimonials */
    .testimonials blockquote {
        background: #f8f8f8 none repeat scroll 0 0;
        border: medium none;
        color: #0d6aad;
        display: block;
        font-size: 12px;
        line-height: 20px;
        padding: 15px;
        position: relative;
    }


    .testimonials .carousel-info img {
        border: 1px solid #f5f5f5;
        border-radius: 200px !important;
        height: 120px;
        padding: 3px;
        width: 120px;
    }
    .testimonials .carousel-info {
        overflow: hidden;
    }
    .testimonials .carousel-info img {
        margin-right: 15px;
    }
    .testimonials .carousel-info span {
        display: block;
    }
    .testimonials span.testimonials-name {
        color: #e6400c;
        font-size: 18px;
        font-weight: 300;
        margin: 23px 0 7px;
    }
    .testimonials span.testimonials-post {
        color: #2e6da4;
        font-size: 16px;
    }



    </style>
</head>
<body>

<div class="wrapbox">

    <section class="pageheader-default text-center">
        <div class="semitransparentbg">
            <h1 class="animated fadeInLeftBig notransition">Managing Committee</h1>
        </div>
    </section>

    <div class="wrapsemibox">
        <div class="animated fadeInUpNow notransition fadeInUp topspace10">
            %{--<g:each in="${managingCommittee}" var="openOvice" status="i">
                <section class="grayarea">
                    <div class="cbp-qtrotator animated fadeInLeftNow notransition fadeInLeft">
                        <blockquote>
                            <p class="bigquote text-left">
                                <img alt="${openOvice.name}" class="pull-right testavatar" width="150" height="150" src="${imgSrc.fromIdentifier(imagePath: openOvice.imagePath)}">
                                <i class="colortext quoteicon"></i>${openOvice.description}<i class="colortext quoteicon"></i>
                            </p>
                        </blockquote>
                    </div>
                </section>
            </g:each>--}%
<g:each in="${managingCommittee}" var="openOvice" status="i">
            <div class="row">
                <div class="col-md-6 col-md-offset-1 col-lg-10">
                    <div class="testimonials">
                        <div class="active item">

                            <div class="carousel-info" id="center-point" >
                                <img class="pull-left" src="${imgSrc.fromIdentifier(imagePath: openOvice.imagePath)}"/>
                                <div class="pull-left">
                                    <span class="testimonials-name">${openOvice.name}</span>
                                    <span class="testimonials-post">${openOvice.title}</span>
                                </div>
                            </div>
                            <blockquote><p>${openOvice.description}</p></blockquote>

                        </div>
                    </div>
                </div>
            </div>
                </g:each>

        </div>

    </div>
</div>
</body>
</html>
