<%--
  Created by IntelliJ IDEA.
  User: Hasnat
  Date: 1/23/2015
  Time: 3:32 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="open-ltpl"/>
    <title>Video Gallery</title>
    <style>
    .pic-caption {
        cursor: pointer
    }
    .wrapper .mix{
        display: none;
    }
    .filter_menu ul {
        margin: 0;
        padding: 0;
        list-style:circle;

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


    ul#lightGallery{
        width: 95%;
        margin: auto;
        padding-top:25px;
    }
    .gallery li {
        display: block;
        float: left;
        margin-bottom: 6px;
        margin-right: 6px;
        width: 270px;
        height: auto;
        padding: 10px;
        overflow: hidden
    }
    .filter {
        color: #000000;
    }
    </style>
</head>

<body>
<section class="pageheader-default text-center">
    <div class="semitransparentbg">
        <h1 class="animated fadeInLeftBig notransition">Click to Show a Video</h1>
    </div>
</section>

<div class="wrapsemibox">
    <div class="row">
        <div class="col-md-12">
            <ul id="lightGallery" class="gallery">
            <g:each in="${videoList}" var="dataSet">
                <li data-src="${dataSet.itemPath}" data-sub-html="#html1">
                    <a href="#">
                        <img src="${assetPath(src: 'video-placeholder.png')}" class="img-responsive" alt="">
                    </a>
                    <span class="pic-caption bottom-to-top">
                        <p class="text-center">${dataSet.name}</p>
                    </span>
                </li>
            </g:each>
            </ul>
        </div>
    </div>
</div>
<script>
    $(document).ready(function() {
        $("#lightGallery").lightGallery({
            thumbWidth: 300,
            animateThumb:true
        });
    });
</script>
</body>
</html>
