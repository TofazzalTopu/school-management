<%@ page defaultCodec="none" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="open-ltpl"/>
    <title>Home</title>
</head>
<body>
<div class="wrapbox">
    <section class="carousel carousel-fade slide home-slider" id="c-slide" data-ride="carousel" data-interval="4500"
             data-pause="false">
        <ol class="carousel-indicators">

            <g:each in="${homeCarousel}" var="dataSet" status="i">
                <li data-target="#c-slide" data-slide-to="${i}" class="${i == 0 ? 'active' : ''}"></li>
            </g:each>

        </ol>

        <div class="carousel-inner">

            <g:each in="${homeCarousel}" var="dataSetImg" status="j">
                <div class="item ${j == 0 ? 'active' : ''}"
                     style="background: url('${imgSrc.fromIdentifier(imagePath: dataSetImg?.imagePath)}')"></div>
            </g:each>

        </div>
        <a class="left carousel-control animated fadeInLeft" href="#c-slide" data-slide="prev"><i
                class="icon-angle-left"></i></a>
        <a class="right carousel-control animated fadeInRight" href="#c-slide" data-slide="next"><i
                class="icon-angle-right"></i></a>
    </section>

    <div class="wrapsemibox">
        <section class="container">
            <div class="row">
                <div class="col-md-8 animated fadeInLeft notransition">
                    <g:each in="${schoolContent}" var="dataSet">

                        <h1 class="smalltitle">
                            <span>${dataSet?.title}</span>
                        </h1>
                        ${dataSet?.description}
                    </g:each>

                </div>

                <div class="col-md-4 animated fadeInRight notransition">
                    <g:each in="${facilityContent}" var="dataSet">
                        <h1 class="smalltitle">
                            <span>${dataSet?.title}</span>
                        </h1>
                        <blockquote>
                            ${dataSet?.description}
                        </blockquote>
                    </g:each>
                </div>

            </div>
        </section>
    </div>
</div>

</body>
</html>
