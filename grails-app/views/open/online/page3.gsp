<%@ page import="com.grailslab.settings.Profession" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="open-ltpl"/>
    <title>Online Application - Step 3</title>
</head>

<body>
<div class="wrapbox">
    <section class="pageheader-default text-center">
        <div class="semitransparentbg">
            <h1 class="animated fadeInLeftBig notransition">Step 3</h1>
        </div>
    </section>
    <div class="wrapsemibox">
        <div class="container animated fadeInUpNow notransition fadeInUp topspace0">
            <div class="col-md-offset-2 col-md-8">
                <g:render template="/layouts/errors" bean="${command?.errors}"/>
                <form role="form" class="form-horizontal" method="post"action="${g.createLink(controller: 'online',action: 'step4')}">
                <g:hiddenField name="regId" value="${registration?.id}"/>
                    <h3 class="smalltitle">
                        <span>Address</span>
                    </h3>
                    <div class="form-group ${hasErrors(bean: command, field: 'presentAddress', 'has-error')}">
                        <label class="col-sm-3 control-label">Present Address <span class="required-indicator">* </span> </label>
                        <div class="col-sm-6">
                            <div class="textarea">
                                <textarea placeholder="Present address" id="presentAddress" name="presentAddress"  rows="3" class="form-control">${command?.presentAddress? command.presentAddress : (registration?.presentAddress? registration.presentAddress:'')}</textarea>
                            </div>
                        </div>
                    </div>

                    <div class="form-group ${hasErrors(bean: command, field: 'permanentAddress', 'has-error')}">
                        <label class="col-sm-3 control-label">Permanent Address <span class="required-indicator">* </span> </label>
                        <div class="col-sm-6">
                            <div class="textarea">
                                <textarea placeholder="Permanent address" id="permanentAddress" name="permanentAddress"  rows="3" class="form-control">${command?.permanentAddress? command.permanentAddress : (registration?.permanentAddress? registration.permanentAddress:'')}</textarea>
                            </div>
                        </div>
                    </div>
                    <h3 class="smalltitle">
                        <span>Fathers Information (Mandatory)</span>
                    </h3>
                    <div class="form-group ${hasErrors(bean: command, field: 'fathersName', 'has-error')}">
                        <label class="col-sm-3 control-label" for="fathersName">Father Name<span class="required-indicator">* </span></label>
                        <div class="col-sm-5">
                            <input type="text" placeholder="Father Name" id="fathersName" name="fathersName" value="${command?.fathersName? command.fathersName : (registration?.fathersName? registration.fathersName:'')}" class="form-control">
                        </div>
                        <div class="col-sm-3 ${hasErrors(bean: command, field: 'fathersIsalive', 'has-error')}">
                            <input type="checkbox"id="fathersIsalive" name="fathersIsalive" ${command?.fathersIsalive?'checked':(registration?.fathersIsalive? 'checked':'')}> Is alive <span class="required-indicator">* </span> </label>
                        </div>
                    </div>
                    <div class="form-group ${hasErrors(bean: command, field: 'fathersProfession', 'has-error')}">
                        <label class="col-sm-3 control-label" for="fathersProfession" >Profession<span class="required-indicator">* </span> </label>
                        <div class="col-sm-6">
                            <g:select class="form-control" id="fathersProfession" name='fathersProfession'
                                      tabindex="3"
                                      noSelection="${['': 'Select One...']}" value="${command?.fathersProfession? command.fathersProfession : (registration?.fathersProfession? registration.fathersProfession:'')}"
                                      from='${com.grailslab.settings.Profession.list()}'
                                      optionKey="name" optionValue="name"></g:select>
                        </div>
                    </div>

                    <div class="form-group ${hasErrors(bean: command, field: 'fathersMobile', 'has-error')}">
                        <label class="col-sm-3 control-label" for="fathersMobile">Mobile No<span class="required-indicator">* </span> </label>
                        <div class="col-sm-6">
                            <input type="text" placeholder="Mobile No" id="fathersMobile" name="fathersMobile"  value="${command?.fathersMobile? command.fathersMobile : (registration?.fathersMobile? registration.fathersMobile:'')}" class="form-control">
                        </div>
                    </div>

                    <div class="form-group ${hasErrors(bean: command, field: 'fathersIncome', 'has-error')}">
                        <label for="fathersIncome" class="col-sm-3 control-label">Monthly Average Income<span class="required-indicator">* </span> </label>

                        <div class="col-md-6">
                            <input type="text" name="fathersIncome" id="fathersIncome"  value="${command?.fathersIncome? command.fathersIncome : (registration?.fathersIncome? registration?.fathersIncome:'')}" class="form-control">
                        </div>
                    </div>
                    <h3 class="smalltitle">
                        <span>Mothers Information (Mandatory)</span>
                    </h3>
                    <div class="form-group ${hasErrors(bean: command, field: 'mothersName', 'has-error')}">
                        <label class="col-sm-3 control-label" for="mothersName">Mother Name<span class="required-indicator">* </span> </label>
                        <div class="col-sm-5">
                            <input type="text" placeholder="Mother Name" id="mothersName" name="mothersName"  value="${command?.mothersName? command.mothersName : (registration?.mothersName? registration.mothersName:'')}" class="form-control">
                        </div>
                        <div class="col-sm-3">
                            <input type="checkbox" id="mothersIsalive" name="mothersIsalive" ${command?.mothersIsalive?'checked':(registration?.mothersIsalive? 'checked':'')}> Is alive <span class="required-indicator">* </span> </label>
                        </div>
                    </div>
                    <div class="form-group ${hasErrors(bean: command, field: 'mothersProfession', 'has-error')}">
                        <label class="col-sm-3 control-label" for="mothersProfession">Profession<span class="required-indicator">* </span> </label>
                        <div class="col-sm-6">
                            <g:select class="form-control" id="mothersProfession" name='mothersProfession'
                                      tabindex="3"
                                      noSelection="${['': 'Select One...']}" value="${command?.mothersProfession? command.mothersProfession : (registration?.mothersProfession? registration?.mothersProfession:'')}"
                                      from='${com.grailslab.settings.Profession.list()}'
                                      optionKey="name" optionValue="name"></g:select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="mothersMobile">Mobile No</label>
                        <div class="col-sm-6">
                            <input type="text" placeholder="Mobile No" id="mothersMobile" name="mothersMobile"  value="${command?.mothersMobile? command.mothersMobile : (registration?.mothersMobile? registration?.mothersMobile:'')}" class="form-control">
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="mothersIncome" class="col-sm-3 control-label">Monthly Average Income</label>

                        <div class="col-sm-6">
                            <input type="text" name="mothersIncome" id="mothersIncome"  value="${command?.mothersIncome? command.mothersIncome : (registration?.mothersIncome? registration?.mothersIncome:'')}" class="form-control">
                        </div>
                    </div>


                    <h3 class="smalltitle">
                        <span>Legal Guardian (If Any)</span>
                    </h3>
                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="legalGuardianName">Guardian Name</label>
                        <div class="col-sm-6">
                            <input type="text" placeholder="GuardianName" id="legalGuardianName" name="legalGuardianName"  value="${command?.legalGuardianName? command.legalGuardianName : (registration?.legalGuardianName? registration.legalGuardianName:'')}" class="form-control">
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="legalGuardianProfession">Profession</label>
                        <div class="col-sm-6">
                            <g:select class="form-control" id="legalGuardianProfession" name='legalGuardianProfession'
                                      tabindex="3"
                                      noSelection="${['': 'Select One...']}" value="${command?.legalGuardianProfession? command.legalGuardianProfession : (registration?.legalGuardianProfession? registration.legalGuardianProfession:'')}"
                                      from='${com.grailslab.settings.Profession.list()}'
                                      optionKey="name" optionValue="name"></g:select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-sm-3 control-label" for="legalGuardianMobile">Mobile No</label>
                        <div class="col-sm-6">
                            <input type="text" placeholder="Mobile No" id="legalGuardianMobile" name="legalGuardianMobile"  value="${command?.legalGuardianMobile? command.legalGuardianMobile : (registration?.legalGuardianMobile? registration.legalGuardianMobile:'')}" class="form-control">
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="relationWithLegalGuardian" class="col-sm-3 control-label">Relation With Guardian</label>

                        <div class="col-sm-6">
                            <input type="text" name="relationWithLegalGuardian" id="relationWithLegalGuardian"   value="${command?.relationWithLegalGuardian? command.relationWithLegalGuardian : (registration?.relationWithLegalGuardian? registration.relationWithLegalGuardian:'')}" class="form-control">
                        </div>
                    </div>
                    <div class="row pull-right">
                        <div class="form-group">
                            <button type="button" class="btn btn-default preBtn">Previous </button>
                            <button type="submit" name="submitBtn" value="saveAsDraft" class="btn btn-info">Save as draft</button>
                            <button type="submit" class="btn btn-default">Save & Next</button>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<script>
    $(function () {
        $('.preBtn').click(function (){
            window.history.back();
        })
    });
</script>
</body>
</html>