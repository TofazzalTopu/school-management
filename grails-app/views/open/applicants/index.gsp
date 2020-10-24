<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="layout" content="open-ltpl"/>
    <style>
    .input-small {
        height: 28px !important;
        margin-bottom: 3px !important;
        line-height: 10px !important;
        padding: 4px 5px !important;
        font-size: 12.5px !important;
        -webkit-border-radius: 4px;
        -moz-border-radius: 4px;
        border-radius: 4px;
    }

    .label-small {
        height: 28px !important;
        margin-bottom: 3px !important;
        line-height: 10px !important;
        padding: 8px 5px !important;
        font-size: 13.5px !important;
        -webkit-border-radius: 4px;
        -moz-border-radius: 4px;
        border-radius: 4px;
    }

    </style>
</head>

<body>
<div id="wrap">
    <div id="main">
        <h4>Application Form</h4>

        <form name="cmaForm" id="cmaForm" method="post" class="form-horizontal">
            <input type="hidden" name="recordRequestPrimaryServiceID" id="recordRequestPrimaryServiceID"
                   value="100">

            <div class="row">
                <div class="col-md-6">

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small" for="name">Name</label>

                        <div class="col-md-6">
                            <input class="form-control pageRequired input-small" id="name" name="name"
                                   placeholder="Insert Students Name"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required required label-small" for="religion">Religion</label>

                        <div class="col-md-6">
                            <g:select class="form-control pageRequired input-small" id="religion" name='religion'
                                      noSelection="${['': 'Select Religion...']}"
                                      from='${com.grailslab.enums.Religion.values()}'
                                      optionKey="key" optionValue="value"></g:select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required required label-small" for="mobile">Mobile</label>

                        <div class="col-md-6">
                            <input class="form-control pageRequired input-small" id="mobile" name="mobile"
                                   placeholder="Insert Mobile Number"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small" for="email">Email</label>

                        <div class="col-md-6">
                            <input class="form-control input-small" id="email" name="email"
                                   placeholder="Insert email address"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small" for="phone">Phone</label>

                        <div class="col-md-6">
                            <input class="form-control" id="phone" name="phone"
                                   placeholder="Insert Phone Number"/>
                        </div>

                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small" for="birthDate">Birth Date</label>

                        <div class="col-md-6">
                            <input class="form-control picDate" id="birthDate" name="birthDate"
                                   placeholder="Insert Birth Date"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required required label-small" for="gender">Gender</label>

                        <div class="col-md-6">

                            <g:select class="form-control pageRequired" id="gender" name='gender'
                                      noSelection="${['': 'Select Gender...']}"
                                      from='${com.grailslab.enums.Gender.values()}'
                                      optionKey="key" optionValue="value"></g:select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required required label-small"
                               for="className">Want To Admit Class</label>

                        <div class="col-md-6">

                            <g:select class="form-control pageRequired" id="className" name='className'
                                      noSelection="${['': 'Select Admission Class...']}"
                                      from='${com.grailslab.settings.ClassName.list()}'
                                      optionKey="id" optionValue="name"></g:select>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small" for="nationality">Nationality</label>

                        <div class="col-md-6">
                            <input class="form-control" id="nationality" name="nationality"
                                   placeholder="Insert nationality"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small"
                               for="nationality">Has Brother/Sister In School</label>

                        <div class="col-md-6">
                            <input type="checkbox" class="form-control" id="hasBroSis"
                                   name="hasBroSis"/>
                        </div>
                    </div>
                </div>

                <div class="col-md-6">

                    <h2>Present Address</h2>

                    <div class="form-group">
                        <label class="col-md-4 control-label required required label-small" for="area">Area</label>

                        <div class="col-md-6">
                            <textArea class="form-control pageRequired" id="area"
                                      name="area"></textArea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small" for="houseNumber">House Number</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control" id="houseNumber"
                                   name="houseNumber"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small" for="roadNumber">Road Number</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control" id="roadNumber" name="roadNumber"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required required label-small" for="district">District</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control pageRequired" id="district"
                                   name="district"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required required label-small" for="upazilas">Upazila</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control pageRequired" id="upazilas"
                                   name="upazilas"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small" for="postOffice">Post</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control" id="postOffice" name="postOffice"/>
                        </div>
                    </div>

                    <h2>Permanent Address</h2>

                    <div class="form-group">
                        <label class="col-md-4 control-label required required label-small" for="areaPm">Area</label>

                        <div class="col-md-6">
                            <textArea class="form-control pageRequired" id="areaPm"
                                      name="areaPm"></textArea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small" for="houseNumberPm">House Number</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control" id="houseNumberPm"
                                   name="houseNumberPm"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small" for="roadNumberPm">Road Number</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control" id="roadNumberPm"
                                   name="roadNumberPm"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required required label-small" for="districtPm">District</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control pageRequired" id="districtPm"
                                   name="districtPm"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required required label-small" for="upazilasPm">Upazila</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control pageRequired" id="upazilasPm"
                                   name="upazilasPm"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small" for="postOfficePm">Post</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control" id="postOfficePm"
                                   name="postOfficePm"/>
                        </div>
                    </div>

                </div>

                <div class="col-md-6">

                    <h2>Fathers Information (Mandatory)</h2>

                    <div class="form-group">
                        <label class="col-md-4 control-label required"
                               for="nameFather">Fathers Name</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control pageRequired required label-small" id="nameFather"
                                   name="nameFather"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label" for="birthDateFather">Birth Date</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control picDate required label-small" id="birthDateFather"
                                   name="birthDateFather"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small" for="bloodGroupFather">Blood Group</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control" id="bloodGroupFather"
                                   name="bloodGroupFather"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small" for="professionFather">Profession</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control" id="professionFather"
                                   name="professionFather"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small"
                               for="eduQualificationFather">Educational Qualification</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control" id="eduQualificationFather"
                                   name="eduQualificationFather"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small" for="mobileFather">Mobile</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control" id="mobileFather"
                                   name="mobileFather"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label"
                               for="avgIncomeFather">Average Income</label>

                        <div class="col-md-6">
                            <input type="number" class="form-control required label-small" id="avgIncomeFather"
                                   name="avgIncomeFather"/>
                        </div>
                    </div>

                    <h2>Mothers Information (Mandatory)</h2>

                    <div class="form-group">
                        <label class="col-md-4 control-label required"
                               for="nameMother">Parent Name</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control pageRequired required label-small" id="nameMother"
                                   name="nameMother"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small" for="birthDateMother">Birth Date</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control picDate" id="birthDateMother"
                                   name="birthDateMother"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small" for="bloodGroupMother">Blood Group</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control" id="bloodGroupMother"
                                   name="bloodGroupMother"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small" for="professionMother">Profession</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control" id="professionMother"
                                   name="professionMother"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small"
                               for="eduQualificationMother">Educational Qualification</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control" id="eduQualificationMother"
                                   name="eduQualificationMother"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small" for="mobileMother">Mobile</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control" id="mobileMother"
                                   name="mobileMother"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small"
                               for="avgIncomeMother">Average Income</label>

                        <div class="col-md-6">
                            <input type="number" class="form-control" id="avgIncomeMother"
                                   name="avgIncomeMother"/>
                        </div>
                    </div>

                    <h2>Legal Guardian (If Any)</h2>

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small" for="nameGuardian">Guardian Name</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control" id="nameGuardian"
                                   name="nameGuardian"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small" for="birthDateGuardian">Birth Date</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control picDate" id="birthDateGuardian"
                                   name="birthDateGuardian"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small"
                               for="bloodGroupGuardian">Blood Group</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control" id="bloodGroupGuardian"
                                   name="bloodGroupGuardian"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small"
                               for="professionGuardian">Profession</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control" id="professionGuardian"
                                   name="professionGuardian"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label"
                               for="eduQualificationGuardian">Educational Qualification</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control required label-small" id="eduQualificationGuardian"
                                   name="eduQualificationGuardian"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small" for="mobileGuardian">Mobile</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control" id="mobileGuardian"
                                   name="mobileGuardian"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small"
                               for="avgIncomeGuardian">Average Income</label>

                        <div class="col-md-6">
                            <input type="number" class="form-control" id="avgIncomeGuardian"
                                   name="avgIncomeGuardian"/>
                        </div>
                    </div>
                </div>

                <div class="col-md-6">

                    <h2>Previous School</h2>

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small" for="nameSchool">Schools Name</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control" id="nameSchool" name="nameSchool"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small" for="addressSchool">Address</label>

                        <div class="col-md-6">
                            <textarea class="form-control" id="addressSchool"
                                      name="addressSchool"></textarea>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small"
                               for="previousClassSchool">Previous Class</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control" id="previousClassSchool"
                                   name="previousClassSchool"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small"
                               for="tcSchool">Transfer Certificate</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control" id="tcSchool" name="tcSchool"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small"
                               for="tcDateSchool">Transfer Certificate Date</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control picDate" id="tcDateSchool"
                                   name="tcDateSchool"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label class="col-md-4 control-label required label-small" for="phoneSchool">Phone Number</label>

                        <div class="col-md-6">
                            <input type="text" class="form-control" id="phoneSchool"
                                   name="phoneSchool"/>
                        </div>
                    </div>

                </div>
            </div>
        </form>
    </div>
</div>
<script>
    $('.picDate').datepicker({
        format: 'dd/mm/yyyy',
        endDate: new Date(),
        autoclose: true
    });
</script>
</body>
</html>
