<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleLeaveManagementLayout"/>
    <title>Leave Application</title>
    <script src="//cdn.tinymce.com/4/tinymce.min.js"></script>
</head>

<body>
<grailslab:breadCrumbActions firstBreadCrumbUrl="${g.createLink(controller: 'leaveApproval', action: 'index')}" firstBreadCrumbText="Leave Application List" breadCrumbTitleText="Add/Edit Application "/>
<div class="wrapbox">
    <section class="panel">
        <div class="panel-body create-content">
            <g:render template="/layouts/errors" bean="${command?.errors}"/>
            <g:form class="form-horizontal" method="post" controller="leaveApproval" action="saveLeaveApplication">
                <g:hiddenField name="id" value="${leaveApply?.id}"/>
                <g:if test="${flash.message}">
                    <h4 class="text-center" style="color: sienna">${flash.message}</h4>
                </g:if>
                <div class="modal-body">
                  <div class="col-sm-12">
                    <div class="row">
                        <div class="form-group">
                            <label  class="col-md-2 control-label">Application Text</label>
                            <div class="col-md-8">
                                <textarea class="form-control" rows="10" cols="5" name="leaveTemplate" required>
                                    ${templateText}
                                </textarea>
                            </div>
                        </div>

                    </div>
                </div>
                </div>
                <div class="modal-footer modal-footer-action-btn col-md-6 col-md-offset-3">
                    <button type="submit" id="create-yes-btn" class="btn btn-large btn-primary">Print</button>
                </div>
            </g:form>
        </div>
    </section>

</div>

<script type="text/javascript">
    tinymce.init({
        selector: "textarea",
        plugins: [
            "advlist autolink autosave link image lists charmap print preview hr anchor pagebreak spellchecker",
            "searchreplace wordcount visualblocks visualchars code fullscreen insertdatetime media nonbreaking",
            "table contextmenu directionality emoticons template textcolor paste textcolor colorpicker textpattern"
        ],

        toolbar1: "bold italic underline strikethrough | alignleft aligncenter alignright alignjustify | styleselect formatselect fontselect fontsizeselect",
        toolbar2: "bullist numlist | outdent indent blockquote | undo redo | forecolor backcolor | table",

        menubar: false,
        toolbar_items_size: 'small',

        style_formats: [
            {title: 'Bold text', inline: 'b'},
            {title: 'Red text', inline: 'span', styles: {color: '#ff0000'}},
            {title: 'Red header', block: 'h1', styles: {color: '#ff0000'}},
            {title: 'Example 1', inline: 'span', classes: 'example1'},
            {title: 'Example 2', inline: 'span', classes: 'example2'},
            {title: 'Table styles'},
            {title: 'Table row 1', selector: 'tr', classes: 'tablerow1'}
        ]
    });
</script>

</body>
</html>
