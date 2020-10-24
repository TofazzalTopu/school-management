<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleSmsLayout"/>
    <title>Draft SMS</title>
</head>
<body>
<grailslab:breadCrumbActions firstBreadCrumbText="Messaging" firstBreadCrumbUrl="${g.createLink(controller: 'messaging',action: 'drafts')}" breadCrumbTitleText="Draft Message"/>

<form name="messageDraftForm" id="messageDraftForm" method="post" action="save" class="create-form form-horizontal">
    <input type="hidden" name="id" value="${smsDraft?.id}"/>
    <div class="row" id="create-form-holder">
        <div class="col-sm-12">
            <section class="panel">
                <g:if test="${flash.message}">
                    <header class="panel-heading">
                        ${flash.message}
                    </header>
                </g:if>

                <div class="panel-body">
                    <div class="col-md-12">
                        <div class="col-md-10">
                            <div class="form-group">
                                <label for="name" class="col-md-4 control-label">Draft Name</label>

                                <div class="col-md-8">
                                    <input class="form-control" type="text" name="name" value="${smsDraft?.name}" id="name" required=""
                                           placeholder="Draft Name"/>
                                </div>
                            </div>

                            <div class="form-group">

                                <label for="message" class="col-md-4 control-label">Type message</label>

                                <div class="col-md-8">
                                    <textarea id="message" rows="5" maxlength="479" name="message" required="" class="form-control" placeholder="Write message">${smsDraft?.message}</textarea>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="form-group">
                                <div class="col-md-4 col-md-offset-6">
                                    <div class="pull-right">
                                        <button class="btn btn-large btn-primary btn-submit" id="msgSubButton">${smsDraft?"Update Draft":"Save As Draft"}</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </div>
</form>
<script>
    var msgStr;
    jQuery(function ($) {
        $("#msgSubButton").click(function(e){
            if (preventUnicodeSms($('#message').val()) === true){
                $('form#messageDraftForm').submit();
            } else {
                e.preventDefault();
            }
        });
        $('#message').maxlength({
            alwaysShow: true,
            threshold: 479,
            warningClass: "label label-info",
            limitReachedClass: "label label-warning",
            placement: 'bottom',
            message: 'used %charsTyped% of %charsTotal% chars. English 159 & Bangla 67 characters per message.'
        });
    });

</script>
</body>
</html>