<%@ page import="com.grailslab.enums.TcType" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleStdMgmtLayout"/>
    <title>Transfer Certificate </title>
    <script src="//cdn.tinymce.com/4/tinymce.min.js"></script>
</head>
<body>
<grailslab:breadCrumbActions firstBreadCrumbUrl="${g.createLink(controller: 'student',action: 'tc')}" firstBreadCrumbText="TC/Dropout" breadCrumbTitleText="Transfer Certificate"/>
<div class="row" id="tc-datalist-holder">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                TC/Dropout List
            </header>
            <div class="panel-body">
                <div class="row">
                    <div class="col-lg-12" id="create-form-holder">
                        <g:if test='${flash.message}'>
                            <div class="alert alert-warning" role="alert"><h2>
                                ${flash.message}
                            </h2></div>
                        </g:if>
                        <g:else>
                            <form class="cmxform form-horizontal" id="create-form" method="post" action="${g.createLink(controller: 'student',action: 'updateTcOrDropOut')}">
                                <g:hiddenField name="id" value="${tcObj?.id}"/>
                                <div class="row">
                                    <div class="form-group">
                                        <label for="tcType" class="col-md-3 control-label">TC Type *</label>
                                        <div class="col-md-8">
                                            <g:select tabindex="1" class="form-control" value="${tcObj?.tcType.key}"
                                                      id="tcType" name='tcType'
                                                      from='${TcType.values()}'
                                                      optionKey="key" optionValue="value"></g:select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="releaseDate" class="col-md-3 control-label">Release Date</label>
                                        <div class="col-md-8">
                                            <input class="form-control" type="text" name="releaseDate" id="releaseDate" value="<g:formatDate format="dd/MM/yyyy" date="${tcObj?.releaseDate}"/>" tabindex="2"/>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="reason" class="col-md-3 control-label">Reason</label>
                                        <div class="col-md-8">
                                            <textarea id="reason" name="reason" class="form-control" required="required" placeholder="Transfer Reason"
                                                      tabindex="3">${tcObj?.reason}</textarea>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label for="schoolName" class="col-md-3 control-label">School Name</label>
                                        <div class="col-md-8">
                                            <input type="text" id="schoolName" name="schoolName" value="${tcObj?.schoolName}" class="form-control" placeholder="School where admit next"
                                                   tabindex="3"/>
                                        </div>
                                    </div>

                                    <div class="form-group">
                                        <label for="releaseText" class="col-md-3 control-label">Certificate Text</label>

                                        <div class="col-md-8">
                                            <textarea id="releaseText" name="releaseText" class="form-control add-html-content-area">${raw(tcObj?.releaseText)}</textarea>
                                        </div>
                                    </div>
                                    <br>
                                    <div class="row">
                                        <div class="form-group">
                                            <div class="col-md-offset-3 col-md-8">
                                                <div class="pull-right">
                                                    <button class="btn btn-default cancel-btn" tabindex="3" type="reset">Cancel</button>
                                                    <button class="btn btn-info" name="tcSubmitButton" value="updateOnlyBtn" tabindex="2" type="submit">Update & Close</button>
                                                </div>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </form>
                        </g:else>
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>
<script>
    tinymce.init({
        setup: function (editor) {
            editor.on('change', function () {
                tinymce.triggerSave();
            });
        },
        theme: "modern",
        mode : "specific_textareas",
        selector:'textarea.add-html-content-area',
        menubar: false,
        toolbar_items_size: 'small',
        plugins: [
            "lists hr anchor pagebreak spellchecker wordcount code fullscreen insertdatetime nonbreaking table textcolor"
        ],
        toolbar1: "bold italic underline strikethrough | alignleft aligncenter alignright alignjustify | fontsizeselect | bullist numlist | forecolor | table"

    });
</script>
<script>
    jQuery('#create-form').submit( function(e){
        e.preventDefault();
        if ( true ) jQuery('#create-form')[0].submit();
    });
    $('#releaseDate').datepicker({
        format: 'dd/mm/yyyy',
//        startDate: new Date(),
        autoclose: true
    });
</script>
</body>
</html>
