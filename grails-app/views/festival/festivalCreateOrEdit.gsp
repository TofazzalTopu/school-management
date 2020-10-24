<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleWebLayout"/>
    <title>Add/Update Olympiad</title>
    <script src="//cdn.tinymce.com/4/tinymce.min.js"></script>
</head>
<body>
<grailslab:breadCrumbActions firstBreadCrumbUrl="${g.createLink(controller: 'festival', action: 'index')}" firstBreadCrumbText="Olympiad List" breadCrumbTitleText="Add/Edit Olympiad"/>
<div class="wrapbox">
    <section class="panel">
        <div class="panel-body create-content">
            <g:form class="form-horizontal" method="post" controller="festival" action="save">
                <g:if test="${flash.message}">
                    <h4 class="text-center" style="color: sienna">${flash.message}</h4>
                </g:if>

                <div class="modal-body">
                  <div class="col-sm-12">
                    <div class="row">
                        <g:hiddenField name="id" value="${fesProgram?.id}"/>
                        <div class="form-group">
                            <label for="name" class="control-label col-md-2">Contest Name <span class="required">*</span></label>
                            <div class="col-md-9">
                                <g:textField class="form-control" id="name" name="name" value="${fesProgram?.name}"
                                             required="required"
                                             placeholder="Contest Name"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="startDate" class="control-label col-md-2"> Program <span class="required">*</span></label>
                            <div class="col-md-6">
                                <div class="form-group"  id="data_range">
                                    <div class="input-daterange input-group">
                                        <span class="input-group-addon">Start Date</span>
                                        <g:textField class="input-sm form-control" id="startDate" value="${g.formatDate(format:'dd/MM/yyyy', date: fesProgram?.startDate)}" name="startDate"
                                                     tabindex="2" placeholder="dd/mm/yyyy" required="required"/>
                                        <span class="input-group-addon">End Date</span>
                                        <g:textField class="input-sm form-control" id="endDate"  value="${g.formatDate(format:'dd/MM/yyyy', date: fesProgram?.endDate)}" name="endDate"
                                                     tabindex="3" placeholder="dd/mm/yyyy" required="required"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="regOpenDate" class="control-label col-md-2"> Registration <span class="required">*</span></label>
                            <div class="col-md-6">
                                <div class="form-group" class="col-md-1" id="data_range2">
                                    <div class="input-daterange input-group">
                                        <span class="input-group-addon">Open Date</span>
                                        <g:textField class="input-sm form-control" id="regOpenDate" name="regOpenDate" value="${g.formatDate(format:'dd/MM/yyyy', date: fesProgram?.regOpenDate)}"
                                                     tabindex="2" placeholder="dd/mm/yyyy" required="required"/>
                                        <span class="input-group-addon">Close Date</span>
                                        <g:textField class="input-sm form-control" id="regCloseDate" name="regCloseDate" value="${g.formatDate(format:'dd/MM/yyyy', date: fesProgram?.regCloseDate)}"
                                                     tabindex="3" placeholder="dd/mm/yyyy" required="required"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="olympiadTopics" class="control-label col-md-2">Add Topic(s)</label>
                            <div class="col-md-6">
                            <g:select multiple="multiple" class="form-control topic-name" value="${selectedTopics}" id="olympiadTopics" name='olympiadTopics'
                                      from='${com.grailslab.enums.OlympiadType.values()}'
                                      optionKey="key" optionValue="value" required="required"></g:select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="name" class="col-md-2 control-label">Help Contact</label>
                            <div class="col-md-8">
                                <textarea class="form-control" rows="10" cols="5" name="helpContact" required>
                                    ${fesProgram?.helpContact}
                                </textarea>
                            </div>
                        </div>

                    </div>
                </div>
                </div>
                <div class="modal-footer modal-footer-action-btn col-md-6 col-md-offset-3">
                    <button type="reset" class="btn btn-default" aria-hidden="true">Reset</button>
                    <button type="submit" id="create-yes-btn" class="btn btn-large btn-primary">${fesProgram? "Update":"Save"}</button>
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

<script>
    jQuery(function ($) {
        $('.input-daterange').datepicker({
            keyboardNavigation: false,
            todayBtn:true,
            format: 'dd/mm/yyyy',
            forceParse: false,
            autoclose: true
        });

        $("#olympiadTopics").select2({
            placeholder: "Select One or More Contest Topic(s)",
            allowClear: true
        });
    });
</script>
</body>
</html>
