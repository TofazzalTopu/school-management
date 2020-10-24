<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleOnlineAddmissionLayout"/>
    <title>Add/Update Admission</title>
    <script src="//cdn.tinymce.com/4/tinymce.min.js"></script>
</head>

<body>
<grailslab:breadCrumbActions firstBreadCrumbUrl="${g.createLink(controller: 'admissionForm', action: 'index')}" firstBreadCrumbText="Admission List" breadCrumbTitleText="Add/Edit Admission"/>
<div class="wrapbox">
    <section class="panel">
        <div class="panel-body create-content">
            <g:render template="/layouts/errors" bean="${command?.errors}"/>
            <g:form class="form-horizontal" method="post" controller="admissionForm" action="save">
                <g:hiddenField name="regFormId" value="${regAdmissionForm?.id}"/>
                <g:if test="${flash.message}">
                    <h4 class="text-center" style="color: sienna">${flash.message}</h4>
                </g:if>

                <div class="modal-body">
                  <div class="col-sm-12">
                    <div class="row">
                        <div class="form-group">
                            <label for="academicYear" class="col-md-2 control-label">Admission Year<span class="required-indicator">*</span></label>
                            <div class="col-md-6">
                                <g:if  test="${regAdmissionForm?.id}">
                                    <input type="text" placeholder="Admission Year" value="${regAdmissionForm?.academicYear?.value}"  readonly class="form-control">
                                </g:if>
                                <g:else>
                                    <g:select tabindex="1" class="form-control"
                                              id="academicYear" name='academicYear'
                                              from='${academicYearList}'
                                              optionKey="key" optionValue="value"></g:select>
                                </g:else>
                            </div>
                        </div>

                        <div class="form-group">
                            <label  class="control-label col-md-2" for="schoolClassIds">Class List<span class="required-indicator">*</span></label>
                            <div class="col-md-6">
                                <g:if  test="${regAdmissionForm?.id}">
                                    <input type="text" placeholder="Class Name" id="schoolClassIds"name="schoolClassIds" value="${regAdmissionForm?.className?.name}"  readonly class="form-control">
                                 </g:if>
                                <g:else>
                                <g:select multiple="multiple" class="form-control"   id="schoolClassIds" name='schoolClassIds'
                                          from='${classList}'  optionKey="id" optionValue="name"
                                          required="required"></g:select>
                                </g:else>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="control-label col-md-2" for="serialPrefix">Serial Prifix<span class="required-indicator">* </span> </label>
                            <div class="col-sm-2">
                                <input type="text" placeholder="Serial Prefix" id="serialPrefix" name="serialPrefix" value="${regAdmissionForm?.serialPrefix}"  class="form-control">
                            </div>
                            <label class="control-label col-md-2" for="applicationPrefix">Application Prefix<span class="required-indicator">* </span> </label>
                            <div class="col-sm-2">
                                <input type="text" placeholder="Serial Prefix" id="applicationPrefix" name="applicationPrefix" value="${regAdmissionForm?.applicationPrefix}"  class="form-control">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="control-label col-md-2" for="formPrice">Form  Price <span class="required-indicator">* </span> </label>
                            <div class="col-sm-2">
                                <input type="text" placeholder="Form Price" id="formPrice"name="formPrice" value="${regAdmissionForm?.formPrice}"  class="form-control">
                            </div>
                            <label  class="control-label col-md-2" for="applyType">Apply Type<span class="required-indicator">*</span></label>
                            <div class="col-md-2">
                                <g:select  class="form-control"   id="applyType" name='applyType'
                                           from='${com.grailslab.enums.ApplicationApplyType.values()}'  optionKey="key" optionValue="value"
                                           required="required" value="${regAdmissionForm?.applyType}"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="regOpenDate" class="control-label col-md-2"> Registration <span class="required-indicator">*</span></label>
                            <div class="col-md-6" style="margin-left: 15px;">
                                <div class="form-group" class="col-md-1">
                                    <div class="input-daterange input-group">
                                        <span class="input-group-addon">Open Date</span>
                                        <g:textField class="input-sm form-control" id="regOpenDate" name="regOpenDate" value="${g.formatDate(format:'dd/MM/yyyy', date: regAdmissionForm?.regOpenDate)}"
                                                     tabindex="2" placeholder="dd/mm/yyyy" required="required"/>
                                        <span class="input-group-addon">Close Date</span>
                                        <g:textField class="input-sm form-control" id="regCloseDate" name="regCloseDate" value="${g.formatDate(format:'dd/MM/yyyy', date: regAdmissionForm?.regCloseDate)}"
                                                     tabindex="3" placeholder="dd/mm/yyyy" required="required"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="fromBirthDate" class="control-label col-md-2"> Birth Date <span class="required-indicator">*</span></label>
                            <div class="col-md-6" style="margin-left: 15px;">
                                <div class="form-group" class="col-md-1">
                                    <div class="input-daterange input-group">
                                        <span class="input-group-addon">From Date</span>
                                        <g:textField class="input-sm form-control" id="fromBirthDate" name="fromBirthDate" value="${g.formatDate(format:'dd/MM/yyyy', date: regAdmissionForm?.fromBirthDate)}"
                                                     tabindex="2" placeholder="dd/mm/yyyy"/>
                                        <span class="input-group-addon">To Date</span>
                                        <g:textField class="input-sm form-control" id="toBirthDate" name="toBirthDate" value="${g.formatDate(format:'dd/MM/yyyy', date: regAdmissionForm?.toBirthDate)}"
                                                     tabindex="3" placeholder="dd/mm/yyyy"/>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="control-label col-md-2" for="formPrice">Application Condition Text</label>
                            <div class="col-sm-6">
                                <input type="text" placeholder="Band School Name" id="bandSchool" name="bandSchool" value="${regAdmissionForm?.bandSchool}"  class="form-control">
                            </div>
                        </div>
                        <div class="form-group">
                            <label  class="col-md-2 control-label">Registration form Instruction Text</label>
                            <div class="col-md-8">
                                <textarea class="form-control" rows="10" cols="5" name="regFormNote" required>
                                    ${regAdmissionForm?.regFormNote}
                                </textarea>
                            </div>
                        </div>
                        <div class="form-group">
                            <label  class="col-md-2 control-label">Admit Card Instruction Text</label>
                            <div class="col-md-8">
                                <textarea class="form-control" rows="10" cols="5" name="instruction" required>
                                    ${regAdmissionForm?.instruction}
                                </textarea>
                            </div>
                        </div>

                    </div>
                </div>
                </div>
                <div class="modal-footer modal-footer-action-btn col-md-6 col-md-offset-3">
                    <button type="reset" class="btn btn-default" aria-hidden="true">Reset</button>
                    <button type="submit" id="create-yes-btn" class="btn btn-large btn-primary">${regAdmissionForm? "Update":"Save"}</button>
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
        <g:if test="${!regAdmissionForm?.id}">
        $("#schoolClassIds").select2({
            placeholder: "Select One or More  Class List(s)",
            allowClear: true
        });
        </g:if>
    });
</script>
</body>
</html>
