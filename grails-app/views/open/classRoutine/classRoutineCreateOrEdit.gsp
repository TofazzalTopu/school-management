
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleWebLayout"/>
    <script src="//cdn.tinymce.com/4/tinymce.min.js"></script>
    <title>Routine Image</title>
</head>

<body>
<grailslab:breadCrumbActions firstBreadCrumbUrl="${g.createLink(controller: 'classRoutine', action: 'index')}" firstBreadCrumbText="Routine List" breadCrumbTitleText="Add/Edit Routine Image"/>
<div class="wrapbox">

    <section class="panel">
        <div class="panel-body create-content">
            <g:form class="form-horizontal" enctype="multipart/form-data" method="post" controller="classRoutine" action="save">
                <g:if test="${flash.message}">
                    <h4 class="text-center" style="color: sienna">${flash.message}</h4>
                </g:if>

                <div class="modal-body">
                    <div class="row">
                        <g:hiddenField name="id" value="${classRoutine?.id}"/>

                        <div class="form-group">
                            <label for="className" class="control-label col-md-2">Class Name <span class="required">*</span></label>
                        <div class="col-md-4">
                                    <g:select class="form-control auto-select-dropdown" value="${classRoutine?.className?.id}"  id="className" name='className' tabindex="2"
                                                noSelection="${['': 'Select Class Name...']}"
                                              from='${allClassName}'
                                              optionKey="id"  optionValue="name"></g:select>

                        </div>

                        </div>
                        <div class="form-group">
                            <label for="sortIndex" class="control-label col-md-2">Position <span class="required">*</span></label>
                            <div class="col-md-3">
                                <input type="number" class="form-control" id="sortIndex" name="sortIndex" value="${classRoutine?.sortIndex}"
                                       required="required"
                                       placeholder="Sort Position"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="pImage" class="control-label col-md-2">Image <span class="required">*</span></label>
                            <div class="col-md-4">
                                <input type="file" name="pImage" id="pImage"/>
                                <p>Miximum Size : 1mb, Dimension: 1600 X 900</p>
                            </div>
                            <div class="col-md-4">
                                <g:if test="${classRoutine?.imagePath}">
                                    <img src="${imgSrc.fromIdentifier(imagePath: classRoutine?.imagePath)}" id="ImagePreview" width="200px" height="170px"/>
                                </g:if>
                                <g:else>
                                    <asset:image src="no-image.jpg" alt="avatar" id="ImagePreview" width="200px" height="170px"/>
                                </g:else>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer modal-footer-action-btn col-md-10 col-md-offset-1">
                    <button type="reset" class="btn btn-default" aria-hidden="true">Reset</button>
                    <button type="submit" id="create-yes-btn" class="btn btn-large btn-primary">Submit</button>
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

        $("#pImage").change(function () {
            readImageURL(this);
        });

        function readImageURL(input) {
            if (input.files && input.files[0]) {
                var reader = new FileReader();
                reader.onload = function (e) {
                    $('#ImagePreview').attr('src', e.target.result);
                };
                reader.readAsDataURL(input.files[0]);
            }
        }
    });
</script>
</body>
</html>
