<%@ page import="com.grailslab.enums.OpenCont" defaultCodec="none"%>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="open-ltpl"/>
    <title>Home</title>
</head>

<body>
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
    <div class="semiboxshadow text-center">
        <img src="${assetPath(src: 'open/shp.png')}" alt="" class="img-responsive">
    </div>

    <section class="intro-note topspace10">
        <div class="container">
            <div class="row">
                <div class="col-md-12 text-center">
                    <h1><span class="colortext">${grailsApplication.config.getProperty("app.home.school.title")}</span></h1>
                    <p>
                        ${grailsApplication.config.getProperty("app.home.school.subtitle")}
                    </p>
                </div>
            </div>
        </div>
    </section>


    <section class="service-box topspace10">
        <div class="container">
            <div class="row">
                <g:each in="${featureContent}" var="dataSet">
                    <div class="col-md-3">
                        <div class="text-center animated fadeInLeftNow notransition overflowHid">
                            <div class="icon-box-top">
                                <i class="fontawesome-icon medium circle-white center ${dataSet.iconClass}"></i>
                                <h1>${dataSet?.title}</h1>
                                <p class="text-justify">
                                    ${dataSet?.description}
                                </p>
                            </div>
                        </div>
                        <p class="fontupper pull-right">
                            <a href="${createLink(controller: 'home', action: 'headingDetails',params: [id:dataSet.id])}" class="colortext">Read More <i class="icon-double-angle-right"></i></a>
                        </p>
                    </div>
                </g:each>
            </div>
        </div>
    </section>

    <div class="container animated fadeInUpNow notransition fadeInUp topspace10">
        <g:each in="${homeVoice}" var="openOvice" status="i">
            <h1 class="small text-center"><span class="colortext">${openOvice.title}</span></h1>
            <div class="br-hr type_short">
                <span class="br-hr-h">
                    <i class="${openOvice.iconClass}"></i>
                </span>
            </div>

            <section class="grayarea">
                <div class="cbp-qtrotator animated fadeInLeftNow notransition fadeInLeft">
                    %{--<h1 class="small text-center topspace40">Quality Products</h1>--}%
                    <blockquote>
                        <p class="bigquote text-left">
                            <img alt="${openOvice.name}" class="pull-right testavatar" width="150" height="150" src="${imgSrc.fromIdentifier(imagePath: openOvice.imagePath)}">
                            <i class="icon-quote-left colortext quoteicon"></i>${openOvice.description}<i class="icon-quote-right colortext quoteicon"></i>
                        </p>
                        <footer><a href="#">${openOvice.name}</a></footer>
                    </blockquote>
                </div>
            </section>
        </g:each>
    </div>

<!-- /.home-features end-->
    <g:if test="${successStory}">
        <section class="grayarea recent-projects-home topspace30 animated fadeInUpNow notransition">
            <div class="container">
                <div class="row">
                    <h1 class="small text-center topspace0">Success Stories</h1>
                    <div class="text-center smalltitle"></div>
                    <div class="col-md-12">
                        <div class="list_carousel text-center">
                            <div class="carousel_nav">
                                <a class="prev" id="car_prev" href="#"><i class="icon-chevron-left"></i></a>
                                <a class="next" id="car_next" href="#"><i class="icon-chevron-right"></i></a>
                            </div>
                            <div class="clearfix">
                            </div>
                            <ul id="carousel-projects">
                                <!--featured-projects 1-->
                                <g:each in="${successStory}" var="dataSetSuc" status="k">
                                    <li>
                                        <div class="boxcontainer">
                                            <img src="${imgSrc.fromIdentifier(imagePath: dataSetSuc?.imagePath)}" alt="">
                                            <div class="roll">
                                                <div class="wrapcaption">
                                                    <a href="#"><i class="icon-link captionicons"></i></a>
                                                    %{--<a data-gal="prettyPhoto[gallery1]" href="${imgSrc.fromIdentifier(imagePath: dataSetSuc?.imagePath)}" title="La Chaux De Fonds"><i class="icon-zoom-in captionicons"></i></a>--}%
                                                </div>
                                            </div>
                                            <h1><a href="#"><span class="colortext">${dataSetSuc?.name}</span></a></h1>
                                            <p>
                                                ${dataSetSuc?.description}
                                            </p>
                                        </div>
                                    </li>
                                </g:each>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
        </section>
        <!-- /.recent-projects-home end-->
    </g:if>

    <section class="container animated fadeInDownNow notransition topspace10 fadeInDown">
        <div class="container animated fadeInUpNow notransition">
            <h2 class="text-center colortext">Quick Links</h2>
            <div class="br-hr type_short">
                <span class="br-hr-h">
                    <i class="icon-pencil"></i>
                </span>
            </div>
            <div class="col-md-12 inline-block" style="padding-left: 25px; padding-right: 10px;">
                <div class="col-md-3">
                    <div class="singledown clearfix">
                        <img src="${assetPath(src: 'archive2.jpg')}">
                        <h2><a href="http://www.educationboardresults.gov.bd/regular/index.php" target="_blank">results archive</a></h2>
                    </div>
                </div>

                <div class="col-md-3">
                    <div class="singledown clearfix">
                        <img src="${assetPath(src: 'oems.jpg')}">
                        <h2><a href="http://esif.teletalk.com.bd/" target="_blank">e-sif</a></h2>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="singledown clearfix">
                        <img src="${assetPath(src: 'board.png')}">
                        <h2><a href="#" target="_blank">mpo information</a></h2>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="singledown clearfix">
                        <img src="${assetPath(src: 'eff.jpg')}">
                        <h2><a href="http://eff.teletalk.com.bd/" target="_blank">e-Form Fill-up</a></h2>
                    </div>
                </div>
            </div>
            <div class="col-md-12 inline-block" style="padding-left: 25px; padding-right: 10px;">
                <div class="col-md-3">
                    <div class="singledown clearfix">
                        <img src="${assetPath(src: 'emis.jpg')}">
                        <h2><a href="#" target="_blank">e-mis</a></h2>
                    </div>
                </div>

                <div class="col-md-3">
                    <div class="singledown clearfix">
                        <img src="${assetPath(src: 'digital.jpg')}">
                        <h2><a href="https://www.teachers.gov.bd/" target="_blank">digital content</a></h2>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="singledown clearfix">
                        <img src="${assetPath(src: 'ministry.jpg')}">
                        <h2><a href="http://www.moedu.gov.bd/" target="_blank">Ministry of Education</a></h2>
                    </div>
                </div>
                <div class="col-md-3">
                    <div class="singledown clearfix">
                        <img src="${assetPath(src: 'bteb.jpg')}">
                        <h2><a href="http://www.bangladesh.gov.bd/" target="_blank">Bangladesh Portal</a></h2>
                    </div>
                </div>
            </div>
        </div>
    </section>

    <section class="container animated fadeInDownNow notransition topspace10 fadeInDown">
        <div class="container animated fadeInUpNow notransition">
            <h2 class="text-center colortext">Location Map</h2>
            <div class="br-hr type_short">
                <span class="br-hr-h">
                    <i class="icon-pencil"></i>
                </span>
            </div>
        </div>
        <div class='embed-container maps'>
            <iframe src="${grailsApplication.config.getProperty('app.home.glocation')}" width="100%" height="450" frameborder="0" style="border:0" allowfullscreen></iframe>
        </div>

    </section>
</div>
    <script>
        $(window).load(function () {
            $('#carousel-projects').carouFredSel({
                responsive: true,
                items: {
                    width: 200,
                    height: 295,
                    visible: {
                        min: 1,
                        max: 4
                    }
                },
                width: '200px',
                height: '295px',
                auto: true,
                circular: true,
                infinite: false,
                prev: {
                    button: "#car_prev",
                    key: "left"
                },
                next: {
                    button: "#car_next",
                    key: "right"
                },
                swipe: {
                    onMouse: true,
                    onTouch: true
                },
                scroll: {
                    easing: "",
                    duration: 1200
                }
            });
            $('.maps.embed-container').on('click', onMapClickHandler);
        });
        // Disable scroll zooming and bind back the click event
        var onMapMouseleaveHandler = function (event) {
            var that = $(this);

            that.on('click', onMapClickHandler);
            that.off('mouseleave', onMapMouseleaveHandler);
            that.find('iframe').css("pointer-events", "none");
        }

        var onMapClickHandler = function (event) {
            var that = $(this);

            // Disable the click handler until the user leaves the map area
            that.off('click', onMapClickHandler);

            // Enable scrolling zoom
            that.find('iframe').css("pointer-events", "auto");

            // Handle the mouse leave event
            that.on('mouseleave', onMapMouseleaveHandler);
        }
    </script>
</body>
</html>
