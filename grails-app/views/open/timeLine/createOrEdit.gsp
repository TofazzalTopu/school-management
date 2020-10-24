<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleWebLayout"/>
    <script src="//cdn.tinymce.com/4/tinymce.min.js"></script>
    <title>Add/Edit TimeLine</title>
</head>

<body>
<grailslab:breadCrumbActions firstBreadCrumbUrl="${g.createLink(controller: 'timeLine', action: 'index')}" firstBreadCrumbText="Time Line" breadCrumbTitleText="Add/Edit Time Line"/>
<div class="wrapbox">

    <section class="panel">
        <div class="panel-body create-content">
            <g:form class="form-horizontal" method="post" controller="timeLine" action="save">
                <g:if test="${flash.message}">
                    <h4 class="text-center" style="color: sienna">${flash.message}</h4>
                </g:if>

                <div class="modal-body">
                      <div class="row">
                <g:hiddenField name="id" value="${timeLine?.id}"/>

                <div class="form-group">
                    <label for="title" class="control-label col-md-2">Title<span class="required">*</span></label>
                    <div class="col-md-4">
                        <g:textField class="form-control" id="title" name="title" value="${timeLine?.title}"
                                     required="required"
                                     placeholder="Title"/>
                    </div>

                    <label for="sortOrder" class="control-label col-md-2">Position <span class="required">*</span></label>
                    <div class="col-md-2">
                        <input type="number" class="form-control" id="sortOrder" name="sortOrder" value="${timeLine?.sortOrder}"
                               required="true"
                               placeholder="Sort Position"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="name" class="control-label col-md-2">Name<span class="required">*</span></label>
                    <div class="col-md-8">
                        <g:textField class="form-control" id="name" name="name"
                                     value="${timeLine?.name}"
                                     placeholder="Short Description"/>
                    </div>
                </div>
                <div class="form-group">
                    <label for="iconClass" class="control-label col-md-2">Icon<span class="required">*</span></label>
                    <div class="col-md-3">
                        <g:textField class="form-control" id="iconClass" name="iconClass"
                                     value="${timeLine?.iconClass?timeLine?.iconClass:'icon-lightbulb'}"
                                     placeholder="Icon"/>
                    </div>
                    <div class="col-md-5"><a href="https://www.wowthemes.net/demo/calypso/icons.html" target="_blank">Find available icons</a> </div>
                </div>
                <div class="form-group">
                    <div class="col-md-10 col-md-offset-1">
                        <textarea class="form-control" rows="20" cols="20" name="description" required
                                  style="width:100%; height:450px">
                            ${timeLine?.description}

                        </textarea>
                    </div>
                </div>
                </div>
                <div class="modal-footer modal-footer-action-btn col-md-10 col-md-offset-1">
                    <button type="reset" class="btn btn-default"
                            aria-hidden="true">Reset</button>
                    <button type="submit" id="create-yes-btn" class="btn btn-large btn-primary">Submit</button>
                </div>
            </g:form>

        </div>
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

        toolbar1: "newdocument fullpage | bold italic underline strikethrough | alignleft aligncenter alignright alignjustify | styleselect formatselect fontselect fontsizeselect",
        toolbar2: "cut copy paste | searchreplace | bullist numlist | outdent indent blockquote | undo redo | link unlink anchor image media code | insertdatetime preview | forecolor backcolor",
        toolbar3: "table | hr removeformat | subscript superscript | charmap emoticons | print fullscreen | ltr rtl | spellchecker | visualchars visualblocks nonbreaking template pagebreak restoredraft",

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
        ],

        templates: [
            {title: 'Test template 1', content: 'Test 1'},
            {title: 'Test template 2', content: 'Test 2'}
        ]
    });
</script>

<script>
    jQuery(function ($) {
    });
</script>
</body>
</html>
