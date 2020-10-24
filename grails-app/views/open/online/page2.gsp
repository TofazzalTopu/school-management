<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="open-ltpl"/>
    <title>Online Application - Step 2</title>
</head>

<body>
    <div class="wrapbox">
        <section class="pageheader-default text-center">
            <div class="semitransparentbg">
                <h1 class="animated fadeInLeftBig notransition">Step 2</h1>
            </div>
        </section>

        <div class="wrapsemibox">
            <div class="container animated fadeInUpNow notransition fadeInUp topspace0">
                <div class="col-md-offset-2 col-md-8">
                    <h1 class="smalltitle">
                        <span>Your Information</span>
                    </h1>
                    <g:render template="/layouts/errors" bean="${command?.errors}"/>
                    <form role="form" class="form-horizontal"method="post"action="${g.createLink(controller: 'online',action: 'step3')}">
                        <g:hiddenField name="regId" value="${registration?.id}"/>

                        <div class="form-group ${hasErrors(bean: command, field: 'nameBangla', 'has-error')}">
                            <label class="col-sm-3 control-label" for="nameBangla">নাম (বাংলা)  <span class="required-indicator">* </span> </label>
                            <div class="col-sm-6">
                                <input type="text" placeholder="নাম (বাংলা)" id="nameBangla"name="nameBangla" value="${command?.nameBangla? command.nameBangla : (registration?.nameBangla? registration.nameBangla:'')}"  class="form-control">
                            </div>
                        </div>

                        <div class="form-group ${hasErrors(bean: command, field: 'religion', 'has-error')}">
                            <label class="col-sm-3 control-label" for="religion">Religion <span class="required-indicator">* </span> </label>
                            <div class="col-sm-6">
                                <g:select class="form-control pageRequired" id="religion" name='religion'
                                          from='${com.grailslab.enums.Religion.values()}'
                                          optionKey="key" value="${command?.religion? command.religion.key : (registration?.religion? registration.religion.key:'')}"  optionValue="value"></g:select>
                            </div>
                        </div>
                        <div class="form-group ${hasErrors(bean: command, field: 'gender', 'has-error')}">

                            <label class="col-sm-3 control-label" for="gender">Gender <span class="required-indicator">* </span> </label>
                            <div class="col-sm-6">
                                <g:select class="form-control pageRequired" id="gender" name='gender'
                                          from='${com.grailslab.enums.Gender.values()}'
                                          optionKey="key" value="${command?.gender? command.gender.key : (registration?.gender? registration.gender.key:'')}" optionValue="value"></g:select>
                            </div>
                        </div>
                        <div class="form-group ${hasErrors(bean: command, field: 'birthCertificateNo', 'has-error')}">
                            <label class="col-sm-3 control-label" for="birthCertificateNo">Birth Certificate No <span class="required-indicator">* </span> </label>
                            <div class="col-sm-6">
                                <input type="text" placeholder="Birth Certificate No" id="birthCertificateNo"name="birthCertificateNo" value="${command?.birthCertificateNo? command.birthCertificateNo : (registration?.birthCertificateNo? registration.birthCertificateNo:'')}"  class="form-control">
                            </div>
                        </div>


                        <div class="form-group">

                            <label class="col-sm-3 control-label" for="gender">Blood Group </label>
                            <div class="col-sm-6">

                                <g:select class="form-control pageRequired" id="bloodGroup" name='bloodGroup'
                                          from='${com.grailslab.enums.BloodGroup.values()}'
                                          optionKey="key" value="${command?.bloodGroup? command.bloodGroup.key : (registration?.bloodGroup? registration.bloodGroup.key:'')}" optionValue="value" noSelection="${['': 'Select Blood Group']}">

                                </g:select>
                            </div>
                        </div>

                        <div class="form-group  ${hasErrors(bean: command, field: 'nationality', 'has-error')}">

                            <label class="col-sm-3 control-label" for="gender">Nationality  <span class="required-indicator">* </span> </label>
                            <div class="col-sm-6">

                                <select class="form-control pageRequired" id="nationality" name="nationality">
                                    <option value="bd">Bangladeshi</option>
                                </select>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-3 control-label" for="email">Email</label>
                            <div class="col-sm-6">
                                <input type="text" placeholder="Email Address" id="email" name="email" value="${command?.email? command.email : (registration?.email? registration.email:'')}" class="form-control">
                            </div>
                        </div>

                        <div class="form-group">
                            <div class="col-sm-offset-3 col-sm-6">
                                <div class="checkbox">
                                    <label>
                                        <input type="checkbox"id="hasBroOrSisInSchool" name="hasBroOrSisInSchool" ${command?.hasBroOrSisInSchool?'checked': (registration?.hasBroOrSisInSchool? 'checked':'')}> Has Brother/ Sister in school </label>
                                </div>
                            </div>
                        </div>
                        <g:if test="${command?.hasBroOrSisInSchool || registration?.hasBroOrSisInSchool}">
                            <div id="broSisHolder">
                        </g:if>
                        <g:else>
                            <div id="broSisHolder" style="display: none;">
                        </g:else>
                        <div class="form-group">
                            <label class="col-sm-3 control-label" for="email">Student ID</label>
                            <div class="col-sm-6">
                                <input type="text" placeholder="Student ID of your brother or Sister 1" id="broOrSis1Id" name="broOrSis1Id" value="${command?.broOrSis1Id? command.broOrSis1Id : (registration?.broOrSis1Id? registration.broOrSis1Id:'')}" class="form-control">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label" for="email">Student ID</label>
                            <div class="col-sm-6">
                                <input type="text" placeholder="Student ID of your brother or Sister 2" id="broOrSis2Id" name="broOrSis2Id" value="${command?.broOrSis1Id? command.broOrSis1Id : (registration?.broOrSis1Id? registration.broOrSis1Id:'')}" class="form-control">
                            </div>
                        </div>
                    </div>
                        <div class="row pull-right">
                            <div class="form-group">
                                <button type="button" class="btn btn-default preBtn">Previous</button>
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
        $('#hasBroOrSisInSchool').click(function(){
            if (this.checked) {
                $('#broSisHolder').show();
            } else {
                $('#broSisHolder').hide();
            }
        });
        $('.preBtn').click(function (){
            window.history.back();
        })
    });
</script>
</body>
</html>