<%@ page contentType="text/html;charset=UTF-8" defaultCodec="none" %>
<html>
    <head>
        <meta name="layout" content="open-ltpl"/>
        <title>Notice board</title>
    </head>

<body>
<style>
.noticeDiv {
    max-height: 200px;
    max-width: 300px;
    min-height: 200px;
    min-width: 300px;
    overflow: hidden;
}
.thimsize {
    line-height: 1.6;
    max-height: 50px;
    max-width: 300px;
    min-height: 50px;
    min-width: 300px;
    overflow: hidden;
}
#icons {
    float: right;
}
    .btn-btn{
        float: left;
    }
</style>
    <section class="pageheader-default text-center">
        <div class="semitransparentbg">
            <h2 class="animated fadeInLeftBig notransition">Notice Board</h2>
        </div>
    </section>

<div class="wrapsemibox">
    <div class="container animated fadeInUpNow notransition fadeInUp topspace0">
        <div class="row col-md-11">
            <g:each in="${noticeList}" var="dataSet">
                <div class="col-xs-12 col-sm-6 col-md-6 col-lg-4 col-xl-4">
                    <nav class="navbar navbar-light">
                        <div class="header thimsize" style="padding: 10px !important;">
                            <h5><span style="color: #007AFF"  id="nTitle${dataSet.id}">${dataSet.title}</span></h5>
                        </div>

                        <div class="caption">
                            <a href="#" class="detailShow" id="${dataSet.id}" title="Click to read more..">
                                <div class="noticeDiv" id="nDiv${dataSet.id}" style="padding: 10px !important;">
                                    <p>${dataSet.body}</p>
                                </div>
                            </a>

                            <div class="footer-fixed" id="nFooter${dataSet.id}">
                            <span style="font-weight: bold;color: #007AFF; padding-left: 10px;">Published on: ${dataSet.publishDate}</span>
                                <g:if test="${dataSet.fileLink != null}">
                                    <g:link controller="calendar" action="downloadFile"
                                            params="[identifier: dataSet.fileLink]"
                                            target="_blank"><span class="glyphicon glyphicon-download-alt" style="float: right; padding-right: 10px;"></g:link> </span>
                                </g:if>
                            </div>
                        </div>
                    </nav>
                </div>
            </g:each>

        </div>
    </div>
</div>

<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal" aria-label="Close"><span
                        aria-hidden="true">&times;</span></button>
                <h4 class="modal-title" id="myModalLabel"> Notice .....</h4>
            </div>
            <div class="modal-body">
                Details.....
            </div>
            <div class="modal-footer" style="text-align: left !important;">
                <button class="btn-btn"><i class="fa fa-download"></i> Download</button>

            </div>
        </div>
    </div>
</div>
<script>
    jQuery(function ($) {
        $('.detailShow').click(function (e) {
            $("#myModal").modal("show");
            var value = $(this).attr('id');
            var htmlText = $("#nDiv" + value).html();
            var nTitleText = $("#nTitle" + value).html();
            var nFooterText = $("#nFooter" + value).html();
            $('.modal-title').html(nTitleText);
            $('.modal-body').html(htmlText);
            $('.modal-footer').html(nFooterText);
            e.preventDefault();
        });

    });


</script>
</body>
</html>