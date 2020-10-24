<%@ page import="com.grailslab.enums.Religion; com.grailslab.settings.Profession" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleStdMgmtLayout"/>

    <title>Student History</title>
</head>

<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Student History"/>

<div class="row" id="create-form-holder">
    <div class="col-md-12">
        <section class="panel">
            <g:hiddenField name="id" value="${registration?.id}"/>
            <header class="panel-heading tab-bg-dark-navy-blue">
                <ul class="nav nav-tabs nav-justified" id="myTab">
                    <li <g:if test="${selectedTab == "student-basic-info-tab"}">class ='active'</g:if>
                        <g:else>class = ""</g:else>>
                        <a href="${g.createLink(controller: 'registration', action: 'showHistory')}/${registration?.id}">
                            <strong>Basic Info</strong>
                        </a>
                    </li>

                    <li <g:if test="${selectedTab == "academic-history-tab"}">class='active'</g:if>
                        <g:else>class = ""</g:else>>
                        <a href="${g.createLink(controller: 'student', action: 'showAcademicHistory')}/${registration?.id}"><strong>Academic History</strong>
                        </a>
                    </li>

                    <li <g:if test="${selectedTab == "payment-history-tab"}">class = "active"</g:if>
                        <g:else>class = ""</g:else>>
                        <a href="${g.createLink(controller: 'student', action: 'showPaymentHistory')}/${registration?.id}">
                            <strong>Payment History</strong>
                        </a>
                    </li>
                </ul>
            </header>

            <div class="panel-body">
                <div class="tab-content tasi-tab">

                    <div id="student-basic-info-tab" class="tab-pane <g:if
                            test="${selectedTab == "student-basic-info-tab"}">active</g:if><g:else>''</g:else>">

                        <div class="panel-body profile-information">
                            <div class="col-md-4">
                                <div class="profile-pic text-center">
                                    <g:if test="${registration?.imagePath ? true : false}">
                                        <img src="${imgSrc.fromIdentifier(imagePath: registration?.imagePath)}"/>
                                    </g:if>
                                    <g:else>
                                        <asset:image src="no-image.jpg" alt="avatar"/>
                                    </g:else>

                                </div>
                            </div>

                            <div class="col-md-8">
                                <div class="profile-desk">
                                    <h1>${registration?.name}</h1>

                                    <h1>${registration?.nameBangla}</h1>
                                    <hr>

                                    <div class="row">
                                        <div class="col-md-6">
                                            <span class="b"><b>Birth Date:</b></span> <span
                                                class="a">${g.formatDate(format: 'dd/MM/yyyy', date: registration?.birthDate)}</span><br>
                                            <span class="b"><b>Gender:</b></span> <span
                                                class="a">${registration?.gender}</span><br>
                                            <span class="b"><b>Blood Group:</b></span> <span
                                                class="a">${registration?.bloodGroup}</span><br>
                                            <span class="b"><b>Religion :</b></span> <span
                                                class="a">${registration?.religion}</span><br>
                                            <span class="b"><b>Nationality :</b></span> <span
                                                class="a">${registration?.nationality}</span><br>
                                        </div>

                                        <div class="col-md-6">
                                            <span class="b"><b>Student ID:</b></span> <span
                                                class="a">${registration?.studentID}</span><br>
                                            <span class="b"><b>Birth Certificate No:</b></span> <span
                                                class="a">${registration?.birthCertificateNo}</span><br>
                                            <span class="b"><b>Admission Year:</b></span> <span
                                                class="a">${registration?.admissionYear}</span><br>
                                            <span class="b"><b>Device ID:</b></span> <span
                                                class="a">${registration?.deviceId}</span><br>
                                        </div>
                                    </div>

                                    <hr>

                                    <div class="row">
                                        <div class="col-md-12">
                                            <span class="b"><b>Mobile:</b></span> <span
                                                class="a">${registration?.mobile}</span><br>
                                            <span class="b"><b>Present Address:</b></span> <span
                                                class="a">${registration?.presentAddress}</span><br>
                                            <span class="b"><b>Permanent Address:</b></span> <span
                                                class="a">${registration?.permanentAddress}</span><br>
                                            <span class="b"><b>Email (if any):</b></span> <span
                                                class="a">${registration?.email}</span><br>
                                            <hr>
                                            <span class="b"><b>Father's Name :</b></span> <span
                                                class="a">${registration?.fathersName}</span><br>
                                            <span class="b"><b>Father's Mobile No :</b></span> <span
                                                class="a">${registration?.fathersMobile}</span><br>
                                            <span class="b"><b>Father's Profession :</b></span> <span
                                                class="a">${registration?.fathersProfession}</span><br>
                                            <span class="b"><b>Father's Average Income :</b></span> <span
                                                class="a">${registration?.fathersIncome}</span><br>
                                            <hr>
                                            <span class="b"><b>Mother's Name :</b></span> <span
                                                class="a">${registration?.mothersName}</span><br>
                                            <span class="b"><b>Mother's Mobile No :</b></span> <span
                                                class="a">${registration?.mothersMobile}</span><br>
                                            <span class="b"><b>Mother's Profession :</b></span> <span
                                                class="a">${registration?.mothersProfession}</span><br>
                                            <span class="b"><b>Mother's Average Income :</b></span> <span
                                                class="a">${registration?.mothersIncome}</span><br>
                                            <hr>
                                            <span class="b"><b>Guardian's Name :</b></span> <span
                                                class="a">${registration?.guardianName}</span><br>
                                            <span class="b"><b>Guardian's Mobile No :</b></span> <span
                                                class="a">${registration?.guardianMobile}</span><br>
                                            <span class="b"><b>Guardian's Profession :</b></span> <span
                                                class="a">${registration?.mothersProfession}</span><br>
                                            <span class="b"><b>Guardian's Average Income :</b></span> <span
                                                class="a">${registration?.mothersIncome}</span><br>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>

                    <div id="academic-history-tab"
                         class='tab-pane <g:if test="${selectedTab == "academic-history-tab"}">active</g:if>'>
                        <g:each in="${lstStudent}" var="student">
                            <div class="row">
                                <table class="table table-hover table-striped">
                                    <tr>
                                        <td>Class</td>
                                        <td>${student.className.name}</td>
                                        <td>Section</td>
                                        <td>${student.section.name}</td>
                                        <td>Roll</td>
                                        <td>${student.rollNo}</td>
                                        <td>Promotion Status</td>
                                        <td>${student.promotionStatus}</td>
                                        <td>Academic Year</td>
                                        <td>${student.academicYear}</td>
                                    </tr>
                                </table>
                            </div>
                        </g:each>
                    </div>

                <div id="payment-history-tab" class="tab-pane <g:if
                        test="${selectedTab == "payment-history-tab"}">active"</g:if>">
                    <span>Payment History</span>
                </div>
            </div>
        </section>
    </div>
</div>

<script>

</script>
</body>
</html>
