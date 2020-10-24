<%@ page import="com.grailslab.gschoolcore.ActiveStatus" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="open-ltpl-admin"/>
    <script src="//cdn.tinymce.com/4/tinymce.min.js"></script>
    <title>Home</title>
</head>
<body>

<div class="wrapbox">

    <section class="panel">
        <div class="panel-body create-content">
            <g:form class="form-horizontal" method="post" controller="allContent" action="saveContent">

                <input type="hidden" name="prevController" value="${prevController}">
                <input type="hidden" name="prevAction" value="${prevAction}">

                <g:if test="${messages}">
                    <h4 class="text-center" style="color: sienna">${messages}</h4>
                </g:if>

                <div class="modal-body">
                        <div class="row">
                <g:hiddenField name="openCont" value="${params?.key}"/>
                <g:hiddenField name="id" value="${allContent?.id}"/>

                <div class="form-group">
                    <label for="title" class="control-label col-md-3">Title</label>

                    <div class="col-md-5">
                        <g:textField class="form-control" id="title" name="title" value="${allContent?.title}"
                                     required="true"
                                     placeholder="Title"/>
                    </div>

                    <label for="sortPosition" class="control-label col-md-1">Position</label>

                    <div class="col-md-2">
                        <input type="number" class="form-control" id="sortPosition" name="sortPosition" value="${allContent?.sortPosition}"
                                     required="true"
                                     placeholder="Sort Position"/>
                    </div>
                </div>

                <div class="form-group">
                    <label for="shortDesc" class="control-label col-md-3">Short Desc</label><span
                        class="required">*</span>

                    <div class="col-md-7">
                        <g:textField class="form-control" id="shortDesc" name="shortDesc"
                                     value="${allContent?.shortDesc}"
                                     placeholder="Short Desc"/>
                    </div>
                </div>

                <div class="form-group">
                    <div class="col-md-10 col-md-offset-1">
                        <textarea class="form-control" rows="20" cols="20" name="detail" required
                                  style="width:100%; height:450px">
                            ${allContent?.detail}

                        </textarea>
                    </div>
                </div>
                </div>

                <div class="modal-footer modal-footer-action-btn col-md-10 col-md-offset-1">
                    <button type="reset" class="btn btn-default"
                            aria-hidden="true">Reset</button>
                    <button type="submit" id="create-yes-btn" class="btn btn-large btn-primary">Submit</button>
                    <g:if test="${allContent}">
                        <button type="button" id="${params.id}" class="btn btn-large btn-danger inactive-btn">
                            <g:if test="${allContent?.activeStatus.key == com.grailslab.gschoolcore.ActiveStatus.INACTIVE.key}">
                                Active
                            </g:if>
                            <g:else>
                                Inactive
                            </g:else>
                        </button>
                        <button type="button" id="${params.id}" class="btn btn-large btn-danger delete-btn">Delete</button>
                    </g:if>

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
        $('.editBtn').click(function (e) {
            var value = $(this).attr('id');
            var url = "${g.createLink(controller: 'allContent',action: 'saveUpdate')}?id=" + value+"&prevController=${prevController}&prevAction=${prevAction}";
            window.location.replace(url);
            e.preventDefault();
        });

        $('.delete-btn').click(function (e) {
            var value = $(this).attr('id');
            var url = "${g.createLink(controller: 'allContent',action: 'deleteContent')}?id=" + value+"&prevController=${prevController}&prevAction=${prevAction}";
            window.location.replace(url);
            e.preventDefault();
        });

        $('.inactive-btn').click(function (e) {
            var value = $(this).attr('id');
            var url = "${g.createLink(controller: 'allContent',action: 'inactiveContent')}?id=" + value+"&prevController=${prevController}&prevAction=${prevAction}";
            window.location.replace(url);
            e.preventDefault();
        });
    });
</script>
</body>
</html>
