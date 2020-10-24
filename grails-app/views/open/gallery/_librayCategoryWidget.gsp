<div class="container-fluid">
    <div class="row">
        <g:each in="${bookList}" var="book">
                 <li class="list-group-item list-group-item-action list-group-item-secondary">
                     &nbsp &nbsp ${book.bookName}
                    &nbsp; &nbsp;<a title="Download" href="${g.createLink(controller: 'home', action: 'downloadBook', params: [identifier: book.link, forceDownload: true])}"><span id="icond"
                        class="glyphicon glyphicon-download-alt"  ></span> </a>
                    <a title="read" target="_blank" href="${g.createLink(controller: 'home', action: 'downloadBook', params: [identifier: book.link])}"><span id="iconb"
                        class="glyphicon glyphicon-book"></span></a>
                </li>

        </g:each>

    </div>
</div>

<style>

li{
    font-size:16px;
}
li:hover {
    background-color:#e6cb76;

}

a:hover {
    font-size: 1em;
}
#icond {
    float: left;
}
#iconb {
    padding-left: 20px;
    float: left;
}

</style>