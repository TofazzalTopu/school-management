<%--
  Created by IntelliJ IDEA.
  User: Grailslab
  Date: 11/15/2016
  Time: 11:32 AM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Edit Online Registration </title>
    <meta charset="utf-8">
    <meta name="layout" content="moduleOnlineAddmissionLayout"/>
</head>

<grailslab:breadCrumbActions firstBreadCrumbUrl="${g.createLink(controller: 'onlineRegistration', action: 'index')}" firstBreadCrumbText="Applicants List" breadCrumbTitleText="Edit"/>


<div class="row">
    <div class="col-md-12">
        <section class="panel">
                <div class="panel-body  create-content">
                    <g:form class="form-horizontal" enctype="multipart/form-data" method="post" controller="onlineRegistration" action="save">
                    <g:if test="${flash.message}">
                        <h4 class="text-center" style="color: sienna">${flash.message}</h4>
                    </g:if>
                    <g:hiddenField name="regId" value="${registration?.id}"/>

                    <div class="row">
                        <div class="col-md-4">
                            <label for="serialNo">Serial No</label>
                            <input readonly class="form-control" type="text" name="serialNo" id="serialNo" value="${registration?.serialNo}"
                                   placeholder="Auto Generate SL No"/><br/>

                            <label for="applyNo">Apply No</label>
                            <input readonly class="form-control" type="text" name="applyNo" id="applyNo" value="${registration?.applyNo}"
                                   placeholder="Auto Generate Apply no"/><br/>

                            <label for="className">Class Name</label>
                            <g:if test="${registration}">
                                <g:select  class="form-control auto-select-dropdown" id="className" name='className' disabled="true" tabindex="2"
                                           from='${classNameList}' value="${registration?.className?.id}"
                                           optionKey="id" optionValue="name"></g:select>
                            </g:if>
                            <g:else>
                                <g:select  class="form-control auto-select-dropdown" id="className" name='className' tabindex="2"
                                           from='${classNameList}' value="${registration?.className?.id}"
                                           optionKey="id" optionValue="name"></g:select>
                            </g:else>
                             <br/>

                            <label for="birthDate">Birth Date <span class="required-indicator">* </span></label>
                            <input class="form-control  pickDate" value="${g.formatDate(format:'dd/MM/yyyy', date: registration?.birthDate)}" type="text" name="birthDate" id="birthDate" Placeholder="Birth Date"
                            placeholder="dd/mm/yyyy" required
                            tabindex="3"/><br/>
                    </div>

                      <div class="col-md-4">
                          <label for="name">Student Name <span class="required-indicator">* </span> </label>
                          <input class="form-control" value="${registration?.name}" type="text" name="name" id="name" placeholder="Student Name" tabindex="1"/><br/>
                          <label for="nameBangla">নাম (বাংলা)</label>
                          <input class="form-control"  value="${registration?.nameBangla}" type="text" name="nameBangla" id="nameBangla" placeholder="নাম (বাংলা)" tabindex="2"/><br/>
                          <label for="mobile">Mobile No <span class="required-indicator">* </span> </label>
                          <input class="form-control"  value="${registration?.mobile}" type="text" name="mobile" id="mobile" placeholder="Mobile no" tabindex="2"/><br/>
                          <label for="birthCertificateNo">Birth Certificate No </label>
                          <input class="form-control" value="${registration?.birthCertificateNo}" type="text" name="birthCertificateNo" id="birthCertificateNo" placeholder="Birth Certificate No" tabindex="8"/><br/>
                        </div>

                        <div class="col-md-4 off-set-1">
                            <g:if test="${registration?.imagePath}">
                                <img src="${imgSrc.fromIdentifier(imagePath: registration?.imagePath)}" id="ImagePreview" width="200px" height="170px"/>
                            </g:if>
                            <g:else>
                                <asset:image src="no-image.jpg" alt="avatar" id="ImagePreview" width="200px" height="170px"/>
                            </g:else>
                            <input type="file" name="pImage" id="pImage" />
                            <p>Maximum Size:1mb </p><br/>
                            <div class="checkbox">
                                <label>
                                    <input type="checkbox"id="hasBroOrSisInSchool" name="hasBroOrSisInSchool" ${registration?.hasBroOrSisInSchool?'checked': (registration?.hasBroOrSisInSchool? 'checked':'')}> Has Brother/ Sister in school </label>
                            </div>
                        </div>
                    </div>

                        <div class="row">
                            <div class="col-md-4">

                                <label for="presentAddress">Present Address <span class="required-indicator">* </span> </label>
                                <textarea rows="3" id="presentAddress" name="presentAddress" class="form-control"placeholder="Present Address"
                                          tabindex="10">${registration?.presentAddress}</textarea><br/>
                                <label for="permanentAddress">Permanant Address</label>
                                <textarea rows="3" id="permanentAddress" name="permanentAddress" class="form-control"placeholder="Permanant Address"
                                          tabindex="11">${registration?.permanentAddress}</textarea><br/>

                            </div>

                            <div class="col-md-4">
                                <label for="gender">Gender <span class="required-indicator">* </span></label>
                                <g:select class="form-control" id="gender" name='gender' tabindex="8" value="${registration?.gender}"
                                          from='${com.grailslab.enums.Gender.values()}'
                                          optionKey="key" optionValue="value"></g:select><br/>

                                <label for="religion">Religion <span class="required-indicator">* </span></label>
                                <g:select class="form-control" id="religion" name='religion' tabindex="4" value="${registration?.religion}"
                                          from='${com.grailslab.enums.Religion.values()}'
                                          optionKey="key" optionValue="value"></g:select><br/>

                                <label for="email">Email(If any)</label>
                                <input class="form-control" value="${registration?.email}" type="email" name="email" id="email" placeholder="Email (If any)" tabindex="6"/><br/>
                            </div>

                            <div class="col-md-4">
                                <label for="nationality">Nationality <span class="required-indicator">* </span></label>
                                <select class="form-control pageRequired" id="nationality" name="nationality">
                                    <option value="bd">Bangladeshi</option>
                                </select><br/>

                                <label for="bloodGroup">Blood Group</label>
                                <g:select class="form-control pageRequired" id="bloodGroup" name='bloodGroup'
                                          from='${com.grailslab.enums.BloodGroup.values()}'
                                          optionKey="key" value="${registration?.bloodGroup? registration.bloodGroup.key : (registration?.bloodGroup? registration.bloodGroup.key:'')}" optionValue="value" noSelection="${['': 'Select Blood Group']}">
                                </g:select>
                            </div>
                        </div>

                        <hr/>

                        <div class="row">
                             <div class="col-md-4">
                                 <label for="fathersName">Father  Name <span class="required-indicator">* </span></label>
                                 <input class="form-control" value="${registration?.fathersName}" type="text" name="fathersName" id="fathersName" placeholder="Father's Name "
                                        tabindex="12"/><br/>
                                 <label for="fathersMobile">Father Mobile No</label>
                                 <input type="text" class="form-control" id="fathersMobile" name='fathersMobile'placeholder=" Mobile No"tabindex="13" value="${registration?.fathersMobile}"/><br/>
                             </div>

                            <div class="col-md-4">
                                <label for="fathersProfession">Father  Profession</label>
                                <g:select class="form-control" id="fathersProfession" name='fathersProfession' value="${registration?.fathersProfession}"
                                          tabindex="21"
                                          noSelection="${['': 'Select Mother Profession...']}"
                                          from='${com.grailslab.settings.Profession.list()}'
                                          optionKey="name" optionValue="name"></g:select> <br/>
                                <label for="fathersIncome">Father  Income</label>
                                <input class="form-control" type="text" name="fathersIncome" id="fathersIncome" tabindex="16" placeholder="Average Income" value="${registration?.fathersIncome}"/><br/>
                            </div>

                            <div class="col-md-4">
                                <div class="form-group">
                                    <div class="col-sm-offset-3 col-sm-6">
                                        <div class="checkbox">
                                            <label>
                                                <input type="checkbox"id="fathersIsalive" name="fathersIsalive" ${registration?.fathersIsalive?'checked': (registration?.fathersIsalive? 'checked':'')}> Father is alive </label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                         </div>

                        <hr/>

                        <div class="row">
                            <div class="col-md-4">
                                <label for="mothersName">Mother Name <span class="required-indicator">* </span></label>
                                <input class="form-control" value="${registration?.mothersName}" type="text" name="mothersName" id="mothersName" placeholder="Mother Name "
                                       tabindex="19"/><br/>
                                <label for="mothersMobile">Mother Mobile No</label>
                                <input class="form-control" type="text" name="mothersMobile" id="mothersMobile"placeholder="Mobile No" value="${registration?.mothersMobile}" tabindex="20"/><br/>
                            </div>

                            <div class="col-md-4">
                                <label for="mothersProfession">Mother Professsion</label>
                                <g:select class="form-control" id="mothersProfession" name='mothersProfession' value="${registration?.mothersProfession}"
                                          tabindex="21"
                                          noSelection="${['': 'Select Mother Profession...']}"
                                          from='${com.grailslab.settings.Profession.list()}'
                                          optionKey="name" optionValue="name"></g:select><br/>
                                <label for="mothersIncome">Mother Income</label>
                                <input class="form-control pickDate" type="text" name="mothersIncome" id="mothersIncome" placeholder="Average Income"
                                       value="${registration?.mothersIncome}" tabindex="22"/><br/>
                            </div>

                            <div class="col-md-4">
                                <div class="form-group">
                                    <div class="col-sm-offset-3 col-sm-6">
                                        <div class="checkbox">
                                            <label>
                                                <input type="checkbox"id="mothersIsalive" name="mothersIsalive" ${registration?.mothersIsalive?'checked': (registration?.mothersIsalive? 'checked':'')}>Mother is alive</label>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <hr/>

                        <div class="row">
                            <div class="col-md-4">
                                <label for="legalGuardianName">Legal Guardian Name</label>
                                <input class="form-control" value="${registration?.legalGuardianName}" type="text" name="legalGuardianName" id="legalGuardianName" placeholder="Name "
                                       tabindex="28"/><br/>

                                <label for="legalGuardianProfession">Legal Guardian Profession</label>
                                <g:select class="form-control" id="legalGuardianProfession" name='legalGuardianProfession' value="${registration?.legalGuardianProfession}"
                                          tabindex="21"
                                          noSelection="${['': 'Select Mother Profession...']}"
                                          from='${com.grailslab.settings.Profession.list()}'
                                          optionKey="name" optionValue="name"></g:select><br/>
                            </div>

                            <div class="col-md-4">
                                <label for="legalGuardianMobile">Legal Guardian Mobile</label>
                                <input class="form-control"value="${registration?.legalGuardianMobile}" type="text" name="legalGuardianMobile" id="legalGuardianMobile" placeholder="Mobile"
                                       tabindex="31"/>
                            </div>
                            <div class="col-md-4">
                                <label for="relationWithLegalGuardian">Relation With Legal Guardian</label>
                                <input class="form-control" value="${registration?.relationWithLegalGuardian}" type="text" name="relationWithLegalGuardian" id="relationWithLegalGuardian" placeholder="Relation with guardian" tabindex="34"/>
                            </div>
                        </div>

                        <hr/>

                       <div class="row">
                           <div class="col-md-4">
                               <label for="preSchoolName">Previous School Name</label>
                               <input id="preSchoolName" name="preSchoolName" class="form-control" placeholder="School Name"
                                  value="${registration?.preSchoolName}" tabindex="37"/><br/>
                           </div>

                           <div class="col-md-4">
                               <label for="preSchoolAddress">Previous School Address</label>
                               <input id="preSchoolAddress" name="preSchoolAddress" class="form-control" placeholder="School Address"
                                      value="${registration?.preSchoolAddress}"tabindex="38"/>
                           </div>

                           <div class="col-md-4">
                               <label for="preSchoolTcDate">Previous School Tc Date</label>
                               <input id="preSchoolTcDate" name="preSchoolTcDate" class="form-control"placeholder="TC Date" value="${g.formatDate(format:'dd/MM/yyyy', date: registration?.preSchoolTcDate)}"
                               %{-- value="${registration?.preSchoolTcDate}"--}% tabindex="39"/>
                           </div>
                       </div>

                        <div class="row">
                            <div class="col-md-4">
                            <label for="preSchoolClass">Previous School CLass Name</label>

                                <input id="preSchoolClass" name="preSchoolClass" class="form-control"placeholder="Last Class"
                                       value="${registration?.preSchoolClass}" tabindex="40"/>
                            </div>

                        </div>

                            <div class="form-group">
                                <div class="col-md-offset-10 col-lg-6">
                                    <button class="btn btn-default cancel-btn" tabindex="16" type="reset">Cancel</button>
                                    <button class="btn btn-primary" tabindex="40" type="submit">Save</button>
                                </div>
                            </div>

                </g:form>
                </div>

        </section>
    </div>
</div>

<script>
    $(function() {
        $('#birthDate').datepicker({
            format: 'dd/mm/yyyy',
            forceParse: false,
            autoclose: true
        })

        $('#preSchoolTcDate').datepicker({
            format: 'mm/dd/yyyy',
            forceParse: false,
            autoclose: true

        })

        $(".cancel-btn").click(function () {
            var validator = $("#create-form").validate();
            validator.resetForm();
            $("#regId").val('');
            $("#create-form-holder").hide(500);
        });

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