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
<section class="pageheader-default text-center">
    <div class="semitransparentbg">
        <h1 class="animated fadeInLeftBig notransition">${libraryCategory.categoryName}</h1>
    </div>
</section>
<g:render template="/open/gallery/librayCategoryWidget" model="[libraryCategory: libraryCategory, bookList: bookList]"/>
</body>
</html>
