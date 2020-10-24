<%@ page contentType="text/html;charset=UTF-8" %>

<html>
<head>
    <meta name="layout" content="open-ltpl"/>
    <title>Video Gallery</title>
    <style>
    .pic-caption {
        cursor: pointer
    }

    .wrapper .mix {
        display: none;
    }

    .filter_menu ul {
        margin: 10px;
        padding: 10px;
        list-style: circle;

    }

    .filter_menu ul li {
        float: left;
        padding: 7px 15px;
        display: block;
        cursor: pointer;
        text-transform: capitalize;
        font-size: 14px;

    }

    .filter_menu ul li.active {
        background: #F56A51;
    }

    .active {
        background: #F56A51;
    }

    ul#lightGallery {
        width: 95%;
        margin: auto
    }

    .gallery li {
        display: block;
        float: left;
        margin-bottom: 16px;
        margin-right: 6px;
        width: 300px;
        height: 300px;
        padding: 10px;
        overflow: hidden
    }

    .filter {
        color: #000000;
    }

    .pic-image {
        max-width: 270px;
        max-height: 270px;
    }
    </style>
</head>

<body>
<section class="pageheader-default text-center">
    <div class="semitransparentbg">
        <h1 class="animated fadeInLeftBig notransition">Video Gallery..</h1>
    </div>
</section>

<div class="wrapsemibox">
    <section class="animated fadeInDown notransition">
        <div class="row blogindex">

            <section class="wrapper">
                <!--Effect: Bottom to Top -->
                <div class="row" style="clear: both">
                    <br>
                    <g:each in="${videoList}" var="dataSet">
                        <div class="col-md-3">
                            <div class="pic">
                                <div class="mix class-room">
                                    <a href="${g.createLink(controller: 'home', action: 'videoSlide', params: [album:dataSet.DT_RowId])}">
                                        <img src="${assetPath(src: 'video-placeholder.png')}" alt="Image"
                                             style="min-height: 200px; height: 200px" class="img-thumbnail">
                                        <span class="pic-caption bottom-to-top">
                                            <p class="text-center">${dataSet.name}</p>
                                        </span>
                                    </a>
                                </div>
                            </div>
                        </div>

                    </g:each>

                </div>
            </section>
        </div>
    </section>
</div>
<script>
    $(function () {
        //mixit up initialize
        $('.pic').mixItUp();

        //menu active
        $('ul li').click(function () {
            $('.filter_menu ul.items li.active').removeClass('active');
            $(this).addClass('active');
        });
    });
</script>
</body>
</html>