<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="open-ltpl-admin"/>
    <title>Home</title>
</head>
<body>

<div class="wrapbox">

    <section class="panel">
        <div class="panel-body create-content">
            <g:form class="form-horizontal" method="post" controller="allContent" action="saveFaqQs">

                <div class="modal-body">
                        <div class="row">
                <g:hiddenField name="catId" value="${params?.catId}"/>
                <g:hiddenField name="id" value="${faqQuestion?.id}"/>

                <g:hiddenField name="prevController" value="${params.prevController}"/>
                <g:hiddenField name="prevAction" value="${params.prevAction}"/>

                <div class="form-group">
                    <label for="name" class="control-label col-md-3">Title</label>

                    <div class="col-md-7">
                        <g:textField class="form-control" id="name" name="name" value="${faqQuestion?.name}"
                                     required="true"
                                     placeholder="Title"/>
                    </div>
                </div>

                <div class="form-group">
                    <label for="answers" class="control-label col-md-3">Answers</label>

                    <div class="col-md-7">
                        <g:textArea class="form-control" id="Answers" name="answers" value="${faqQuestion?.answers}"
                                    placeholder="Answers"/>
                    </div>
                </div>

                <div class="form-group">
                    <label for="sortPosition" class="control-label col-md-3">Position</label>

                    <div class="col-md-3">
                        <input type="number" class="form-control" id="sortPosition" name="sortPosition"
                               value="${faqQuestion?.sortPosition}"
                               required="true"
                               placeholder="Sort Position"/>
                    </div>
                </div>
                </div>

                <div class="modal-footer modal-footer-action-btn col-md-10 col-md-offset-1">
                    <button type="reset" class="btn btn-default"
                            aria-hidden="true">Reset</button>
                    <button type="submit" id="create-yes-btn" class="btn btn-large btn-primary">Submit</button>
                    <g:if test="${faqQuestion}">
                        <button type="button" id="${params.id}"
                                class="btn btn-large btn-danger delete-btn">Delete</button>
                    </g:if>

                </div>
            </g:form>

        </div>
        </div>
    </section>
</div>

<script>
    jQuery(function ($) {
        $('.editBtn').click(function (e) {
            var value = $(this).attr('id');
            var url = "${g.createLink(controller: 'allContent',action: 'saveUpdate')}?id=" + value+"&prevController=${params.prevController}&prevAction=${params.prevAction}";
            window.location.replace(url);
            e.preventDefault();
        });

        $('.delete-btn').click(function (e) {
            var value = $(this).attr('id');
            var url = "${g.createLink(controller: 'allContent',action: 'deleteQus')}?id=" + value+"&prevController=${params.prevController}&prevAction=${params.prevAction}";
            window.location.replace(url);
            e.preventDefault();
        });

        $('.inactive-btn').click(function (e) {
            var value = $(this).attr('id');
            var url = "${g.createLink(controller: 'allContent',action: 'inactiveContent')}?id=" + value+"&prevController=${params.prevController}&prevAction=${params.prevAction}";
            window.location.replace(url);
            e.preventDefault();
        });
    });
</script>
</body>
</html>
