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
    <title>Virtual Library</title>
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

<div class="wrapsemibox">
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

    <h2 class="text-center"><a href="${g.createLink(controller: 'home', action: 'onlineLibrary')}">Virtual Library</a> / ${libraryCategory.categoryName}</h2>
    <g:render template="/open/gallery/librayCategoryWidget" model="[bookList: bookList]"/>
</div>
</body>
</html>
