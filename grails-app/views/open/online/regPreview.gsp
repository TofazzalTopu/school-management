<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="open-ltpl"/>
    <title>Online Application - Preview</title>
    <style>
    .bigtext {
        font-size:15px;
    }
    </style>
</head>

<body>
    <div class="wrapbox">
        <section class="pageheader-default text-center">
            <div class="semitransparentbg">
                <h1 class="animated fadeInLeftBig notransition">Application Preview</h1>
                <p class="animated fadeInRightBig container page-description">Form No: ${registration?.serialNo}</p>
            </div>
        </section>
        <div class="wrapsemibox">
            <div class="container animated fadeInUpNow notransition fadeInUp topspace0">
                <form role="form" class="form-horizontal" method="post" action="${g.createLink(controller: 'online',action: 'submit')}" target="_blank">
                    <g:hiddenField name="id" value="${registration?.id}"/>
                    <section class="container animated fadeInDownNow notransition topspace20 fadeInDown">
                        <div class="row">
                            <div class=" col-md-12">
                                <h3 class="smalltitle">
                                    <span>Personal Details</span>
                                </h3>
                                <div class="col-md-4">
                                    <p class="bigtext"><span class="colortext">Class Name:</span> ${registration?.className?.name}</p>
                                    <p class="bigtext"><span class="colortext">Name:</span> ${registration?.name}</p>
                                    <p class="bigtext"><span class="colortext">নাম (বাংলা):</span> ${registration?.nameBangla}</p>
                                    <p class="bigtext"><span class="colortext">Mobile No:</span> ${registration?.mobile}</p>
                                    <p class="bigtext"><span class="colortext">Birth Date:</span> <g:formatDate date="${registration?.birthDate}" format="dd/MM/yyyy"/></p>
                                    <p class="bigtext"><span class="colortext">Present Address:</span> ${registration?.presentAddress}</p>
                                    <p class="bigtext"><span class="colortext">Has Brother/ Sister in school:</span> ${registration?.hasBroOrSisInSchool? "Yes":"No"}</p>
                                </div>

                                <div class="col-md-4">
                                    <p class="bigtext"><span class="colortext">Gender:</span> ${registration?.gender}</p>
                                    <p class="bigtext"><span class="colortext">Religion:</span> ${registration?.religion.key}</p>
                                    <p class="bigtext"><span class="colortext">Birth Certificate No:</span> ${registration?.birthCertificateNo}</p>
                                    <p class="bigtext"><span class="colortext">Nationality:</span> Bangladesh</p>
                                    <p class="bigtext"><span class="colortext">Email:</span> ${registration?.email}</p>
                                    <p class="bigtext"><span class="colortext">Permanent Address:</span> ${registration?.permanentAddress}</p>
                                    <p class="bigtext"><span class="colortext">Blood Group:</span> ${registration?.bloodGroup}</p>
                                </div>

                                <div class="col-md-4 ">
                                    <div class="text-center">
                                        <g:if test="${registration?.imagePath}">
                                            <img src="${imgSrc.fromIdentifier(imagePath: registration?.imagePath)}" id="ImagePreview" width="200px" height="170px" />
                                            <div class="caption">
                                                <h5>${registration?.name}</h5>
                                            </div>
                                        </g:if>
                                        <g:else>
                                            <asset:image  src="no-image.jpg" alt="avatar" id="ImagePreview" width="200px" height="170px"/>
                                            <div class="caption">
                                                <h5>Not Uploaded</h5>
                                            </div>
                                        </g:else>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </section>
                    <h3 class="smalltitle">
                        <span>Father's Information</span>
                    </h3>
                    <div class="row">
                        <div class= "col-md-12">
                            <div class="col-md-4">
                                <p class="bigtext"><span class="colortext">Name:</span> ${registration?.fathersName}</p>
                                <p class="bigtext"><span class="colortext">Profession:</span> ${registration?.fathersProfession}</p>
                            </div>

                            <div class="col-md-4">
                                <p class="bigtext"><span class="colortext">Monthly Average Income:</span> ${registration?.fathersIncome}</p>
                                <p class="bigtext"><span class="colortext">Mobile No:</span> ${registration?.fathersMobile}</p>
                            </div>

                            <div class="col-md-4">
                                <p class="bigtext"><span class="colortext">Is alive:</span> ${registration?.fathersIsalive? "Yes" : "No"}</p>
                            </div>
                        </div>
                    </div>

                    <h3 class="smalltitle">
                        <span>Mother's Information</span>
                    </h3>

                    <div class="row">
                        <div class="col-md-12">
                            <div class="col-md-4">
                                <p class="bigtext"><span class="colortext">Name:</span>${registration?.mothersName}</p>
                                <p class="bigtext"><span class="colortext">Profession:</span> ${registration?.mothersProfession}</p>
                            </div>

                            <div class="col-md-4">
                                <p class="bigtext"><span class="colortext">Monthly Average Income:</span> ${registration?.mothersIncome}</p>
                                <p class="bigtext"><span class="colortext">Mobile No:</span> ${registration?.mothersMobile}</p>
                            </div>

                            <div class="col-md-4">
                                <p class="bigtext"><span class="colortext">Is alive:</span> ${registration?.mothersIsalive? "Yes":"No"}</p>
                            </div>
                        </div>
                    </div>

                    <h3 class="smalltitle">
                        <span>Legal Guardian</span>
                    </h3>

                    <div class="row">
                        <div class="col-md-12">
                            <div class="col-md-4">
                                <p class="bigtext"><span class="colortext">Name:</span> ${registration?.legalGuardianName}</p>
                                <p class="bigtext"><span class="colortext">Profession:</span> ${registration?.legalGuardianProfession}</p>
                            </div>

                            <div class="col-md-4">
                                <p class="bigtext"><span class="colortext">Mobile No:</span>${registration?.legalGuardianMobile}</p>
                            </div>

                            <div class="col-md-4">
                                <p class="bigtext"><span class="colortext">Relation With Guardian:</span> ${registration?.relationWithLegalGuardian}</p>
                            </div>
                        </div>
                    </div>

                    <h3 class="smalltitle">
                        <span>Previous School Information</span>
                    </h3>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="col-md-4">
                                <p class="bigtext"><span class="colortext">School Name:</span> ${registration?.preSchoolName}</p>
                                <p class="bigtext"><span class="colortext">Address:</span> ${registration?.preSchoolAddress}</p>
                            </div>

                            <div class="col-md-4">
                                <p class="bigtext"><span class="colortext">Previous Class:</span> ${registration?.preSchoolClass}</p>
                            </div>

                            <div class="col-md-4">
                                <p class="bigtext"><span class="colortext">TC Date:</span> <g:formatDate date="${registration?.preSchoolTcDate}" format="dd/MM/yyyy"/>
                            </div>
                        </div>
                    </div>

                    <div class="row pull-right">
                        <div class="form-group">
                            <g:if test="${readOnlyMode}">
                                <button type="submit" name="submitBtn" value="printPdf" class="btn btn-info">Print Form</button>
                            </g:if>
                            <g:else>
                                <button class="btn btn-default" type="button">Previous </button>
                                <button class="btn btn-default" name="submitBtn" value="saveAsDraft" type="submit">Save as draft</button>
                                <button class="btn btn-default" name="save" type="submit">Submit</button>
                            </g:else>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</body>
</html>