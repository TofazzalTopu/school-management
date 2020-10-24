<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="open-ltpl"/>
    <title>Online Application - Step 1</title>
</head>

<body>
    <div class="wrapbox">
        <section class="pageheader-default text-center">
            <div class="semitransparentbg">
                <h1 class="animated fadeInLeftBig notransition">Online Application</h1>
            </div>
        </section>

        <div class="wrapsemibox">
            <div class="container animated fadeInUpNow notransition fadeInUp topspace0">
                <div class="col-md-offset-1 col-md-10">
                    <h3 class="smalltitle">
                        <span>Step 1</span>
                    </h3>
                    <g:render template="/layouts/errors" bean="${command?.errors}"/>
                    <form role="form" class="form-horizontal" method="post"  action="${g.createLink(controller: 'online',action: 'step2')}">
                        <g:hiddenField name="regForm" value="${regForm?.id}"/>
                        <div class="row">
                            <div class="col-md-9">
                                <div class="form-group ${hasErrors(bean: command, field: 'className', 'has-error')}">
                                    <label class="col-sm-5 control-label" for="className">Class Name <span class="required-indicator">*</span> </label>
                                    <div class="col-sm-6">
                                        <g:select class="form-control auto-select-dropdown" id="className" name='className' tabindex="2"
                                                  from='${classNameList}' value="${command?.className?.id}"
                                                  optionKey="id" optionValue="name"></g:select>
                                    </div>
                                </div>

                                <div class="form-group ${hasErrors(bean: command, field: 'name', 'has-error')}">
                                    <label class="col-sm-5 control-label" for="name">Name <span class="required-indicator">* </span> </label>
                                    <div class="col-sm-6">
                                        <input type="text" placeholder="Name" value="${command?.name? command?.name  :registration?.name}" id="name" name="name" class="form-control">
                                    </div>
                                </div>

                                <div class="form-group ${hasErrors(bean: command, field: 'fathersName', 'has-error')}">
                                    <label class="col-sm-5 control-label" for="fathersName">Father Name <span class="required-indicator">* </span></label>
                                    <div class="col-sm-6">
                                        <input type="text" placeholder="Father Name" value="${command?.fathersName? command?.fathersName : registration?.fathersName}" id="fathersName" name="fathersName" class="form-control">
                                    </div>
                                </div>

                                <div class="form-group ${hasErrors(bean: command, field: 'mobile', 'has-error')}">
                                    <label class="col-sm-5 control-label" for="mobile">Mobile No <span class="required-indicator">* </span> </label>
                                    <div class="col-sm-6">
                                        <input type="text" placeholder="Mobile Number" value="${command?.mobile? command?.mobile : registration?.mobile}" id="mobile" name="mobile" class="form-control">
                                    </div>
                                </div>

                                <div class="form-group ${hasErrors(bean: command, field: 'birthDate', 'has-error')}">
                                    <label class="col-sm-5 control-label" for="birthDate">Birth Date<span class="required-indicator">* </span> </label>
                                    <div class="col-sm-6">
                                        <input type="text" placeholder="Birth Date" value="${formatDate(date: command?.birthDate? command?.birthDate : registration?.birthDate, format: 'dd/MM/yyyy')}" id="birthDate" name="birthDate" class="form-control">
                                    </div>
                                </div>
                            </div>

                        </div>
                        <div class="row pull-right">
                            <div class="form-group">
                                <a href="${g.createLink(controller: 'online', action: 'apply')}" class="btn btn-info">Try Later</a>
                                <button type="submit" class="btn btn-default">Save & Next</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
<script>
   $(function() {
       $('#birthDate').datepicker({
           format: 'dd/mm/yyyy',
           forceParse: false,
           autoclose: true
       })
   });

</script>
</body>
</html>