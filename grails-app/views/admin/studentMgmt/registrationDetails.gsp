<%@ page import="com.grailslab.enums.Religion; com.grailslab.settings.Profession" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleStdMgmtLayout"/>
    <title>Registration Details</title>
</head>

<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Student Information Details"/>
<div class="row" id="create-form-holder">
    <div class="col-md-12">
        <section class="panel">
            <div class="panel-body create-content">
            <g:form class="form-horizontal" enctype="multipart/form-data" method="post" controller="registration" action="saveDetailsReg">
                    <g:hiddenField name="id" value="${commandObj?.id?: registration.id}"/>

                    <div class="row">
                        <g:render template="/layouts/errors" bean="${commandObj?.errors}"/>
                        <div class="col-md-4">
                            <label for="name">Student ID</label>
                            <input readonly class="form-control" type="text" name="studentID" id="studentID" value="${commandObj?.studentID?: registration.studentID}"
                                   placeholder="Auto Generate Student_ID"/><br/>
                            <label for="name">Student Card No</label>
                            <input class="form-control" value="${commandObj?.cardNo?: registration.cardNo}" type="text" name="cardNo" id="cardNo" placeholder="Card Number" tabindex="7"/><br/>
                            <label for="name">Device ID</label>
                            <input class="form-control" value="${commandObj?.deviceId?:registration.deviceId}" type="text" name="deviceId" id="deviceId" placeholder="Device ID" tabindex="7"/><br/>
                            <label for="name">Birth Date</label>
                            <input class="form-control  pickDate" value="${g.formatDate(format:'dd/MM/yyyy', date: commandObj?.birthDate?:registration.birthDate)}" type="text" name="birthDate" id="birthDate" Placeholder="Birthdate"
                                   placeholder="dd/mm/yyyy"
                                   tabindex="3"/><br/>
                            <label for="name">Blood Group</label>
                            <input class="form-control" value="${commandObj?.bloodGroup?:registration.bloodGroup}" type="text" name="bloodGroup" id="bloodGroup"placeholder="Blood Group"
                                   tabindex="5"/><br/>
                            <label for="name">Birth Certificate No</label>
                            <input class="form-control" value="${commandObj?.birthCertificateNo?:registration.birthCertificateNo}" type="text" name="birthCertificateNo" id="birthCertificateNo" placeholder="Birth Certificate No" tabindex="8"/><br/>
                            <label for="name">Admission Year</label>
                            <input class="form-control" value="${commandObj?.admissionYear?:registration.admissionYear}" type="text" onkeypress='return event.charCode' name="admissionYear" id="admissionYear" placeholder="Admission ID" tabindex="7"/><br/>
                        </div>

                        <div class="col-md-4">
                            <label for="name">Name</label>
                            <input class="form-control" value="${commandObj?.name?:registration.name}" type="text" name="name" id="name" placeholder="Student Name" tabindex="1"/><br/>
                            <label for="nameBangla">নাম (বাংলা)</label>
                            <input class="form-control"  value="${commandObj?.nameBangla?:registration.nameBangla}" type="text" name="nameBangla" id="nameBangla" placeholder="নাম (বাংলা)" tabindex="2"/><br/>
                            <label for="name">Mobile No</label>
                            <input class="form-control"  value="${commandObj?.mobile?:registration.mobile}" type="text" name="mobile" id="mobile" placeholder="Mobile no" tabindex="2"/><br/>
                            <label for="name">Gender</label>
                            <g:select class="form-control" id="gender" name='gender' tabindex="8" value="${commandObj?.gender?:registration.gender}"
                                      from='${com.grailslab.enums.Gender.values()}'
                                      optionKey="key" optionValue="value"></g:select>
                            <br/>
                            <label for="name">Religion</label>
                            <g:select class="form-control" id="religion" name='religion' tabindex="4" value="${commandObj?.religion?:registration.religion}"
                                      noSelection="${['': 'Select Relegion...']}"
                                      from='${com.grailslab.enums.Religion.values()}'
                                      optionKey="key" optionValue="value"></g:select><br/>

                            <label for="name">Email (if any)</label>
                            <input class="form-control" value="${commandObj?.email?:registration.email}" type="email" name="email" id="email" placeholder="Email (If any)" tabindex="6"/>
                             <br/>

                            <label for="name">Nationality</label>
                            <input class="form-control" value="${commandObj?.nationality?:registration.nationality}" type="text" name="nationality" id="nationality" placeholder="Nationality" tabindex="6"/><br/>
                              </div>

                            <div class="col-md-4 off-set-1">
                                <g:if test="${registration?.imagePath?true:false}">
                                    <img src="${imgSrc.fromIdentifier(imagePath: registration?.imagePath)}" id="ImagePreview" width="200px" height="170px"/>
                                </g:if>
                                <g:else>
                                    <asset:image src="no-image.jpg" alt="avatar" id="ImagePreview" width="200px" height="170px"/>
                                </g:else>
                                <input type="file" name="pImage" id="pImage" />
                                <p>Maximum Size:1mb </p><br/>

                                <label for="name">Present Address</label>
                                <textarea rows="3" id="presentAddress" name="presentAddress" class="form-control"placeholder="Present Address"
                                          tabindex="10">${commandObj?.presentAddress?:registration.presentAddress}</textarea><br/>
                                <label for="name">Permanent Address</label>
                                <textarea rows="3" id="permanentAddress" name="permanentAddress" class="form-control"placeholder="Permanant Address"
                                          tabindex="11">${commandObj?.permanentAddress?:registration.permanentAddress}</textarea><br/>

                            </div>
                        </div>
                    <hr/>
                    <div class="row">
                        <div class="col-md-4">
                            <label for="name">Father's Name</label>
                            <input class="form-control" value="${commandObj?.fathersName?:registration.fathersName}" type="text" name="fathersName" id="fathersName" placeholder="Fathers Name "
                                   tabindex="12"/><br/>
                            <label for="name">Father's Mobile No</label>
                            <input class="form-control" value="${commandObj?.fathersMobile?:registration.fathersMobile}" type="text" name="fathersMobile" id="fathersMobile" placeholder="Mobile Number" tabindex="16"/><br/>
                        </div>
                            <div class="col-md-4">
                                <label for="name">Father's Profession</label>
                                <g:select class="form-control" id="fathersProfession" name='fathersProfession' placeholder="fatherprofession" value="${commandObj?.fathersProfession?:registration.fathersProfession}"
                                          tabindex="14"
                                          noSelection="${['': 'Select Father Profession..']}"
                                          from='${Profession.list()}'
                                          optionKey="id" optionValue="name"></g:select><br/>
                        </div>
                        <div class="col-md-4">

                            <label for="name">Father's Average Income</label>
                            <input class="form-control" type="number" name="fathersIncome" id="fathersIncome"tabindex="17" placeholder="Monthly Average Income" value="${commandObj?.fathersIncome?:registration.fathersIncome}"/>
                        </div>
                    </div>
                    <hr/>
                    <div class="row">
                        <div class="col-md-4">
                            <label for="name">Mother's Name</label>
                            <input class="form-control" value="${commandObj?.mothersName?:registration.mothersName}" type="text" name="mothersName" id="mothersName" placeholder="Mother Name "
                                   tabindex="19"/><br/>
                            <label for="name">Mother's Mobile No</label>
                            <input class="form-control" type="text" name="mothersMobile" id="mothersMobile"placeholder="Mobile No" value="${commandObj?.mothersMobile?:registration.mothersMobile}" tabindex="20"/><br/>

                        </div>
                        <div class="col-md-4">
                            <label for="name">Mother's Profession</label>
                            <g:select class="form-control" id="mothersProfession" name='mothersProfession' value="${commandObj?.mothersProfession?:registration.mothersProfession}"
                                      tabindex="21"
                                      noSelection="${['': 'Select Mother Profession...']}"
                                      from='${com.grailslab.settings.Profession.list()}'
                                      optionKey="id" optionValue="name"></g:select>
                        </div>
                        <div class="col-md-4">
                            <label for="name">Mother's Average Income</label>
                            <input class="form-control" type="number" name="mothersIncome" id="mothersIncome" placeholder="Monthly Average Income" value="${commandObj?.mothersIncome?:registration.mothersIncome}" tabindex="24"/>
                        </div>
                    </div>
                <hr/>
                    <div class="row">
                        <div class="col-lg-12">
                        <div class="form-group">
                            <div class="col-md-4">
                                <label for="name">Guardian's Name</label>
                                <input class="form-control" value="${commandObj?.guardianName?:registration.guardianName}" type="text" name="guardianName" id="guardianName" placeholder="Name "
                                       tabindex="28"/><br/>
                            </div>
                                <div class="col-md-4">
                                    <label for="name">Guardian's Mobile</label>
                                    <input class="form-control" type="text" name="guardianMobile" id="guardianMobile" placeholder="Mobile" value="${commandObj?.guardianMobile?:registration.guardianMobile}"tabindex="29"/><br/>
                                </div>
                                </div>
                        <hr/>
                    <div class="form-group">
                        <div class="col-md-offset-10 col-lg-6">
                            <button class="btn btn-primary" tabindex="40" type="submit">Save</button>
                            <button class="btn btn-default cancel-btn" tabindex="16" type="reset">Cancel</button>
                        </div>
                    </div>
                 </g:form>
             </div>
            </div>
        </section>
    </div>
</div>

<script>
  jQuery(function ($) {
      $('.pickDate').datepicker({
          format: 'dd/mm/yyyy',
          endDate: new Date(),
          autoclose: true
      });
      $("#admissionYear").numeric();
  });
</script>
</body>
</html>
