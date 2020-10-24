<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta name="layout" content="moduleParentsLayout"/>
    <title>Profile</title>
</head>

<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Profile"/>
<div class="row">
    <div class="col-md-12">
        <section class="panel">
            <div class="panel-body profile-information">
                <div class="col-md-2">
                    <div class="profile-pic text-center">
                        <g:if test="${registration?.imagePath}">
                            <img src="${imgSrc.fromIdentifier(imagePath: registration?.imagePath)}"
                                 alt="${student?.name}">
                        </g:if>
                        <g:else>
                            <asset:image src="no-image.jpg" alt="avatar"/>
                        </g:else>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="profile-desk">
                        <h1>${student?.name}</h1>
                           <div class="h">
                               <span class="b"><b>Mobile: </b></span> <span class="a"> ${registration?.mobile}</span><br>
                               <span class="b"><b>Roll: </b></span> <span class="a">${student?.rollNo}</span><br>
                               <span class="b"><b>Group:</b></span> <span class="a"> ${student?.section?.groupName?.value}</span><br>
                               <span class="b"><b>Sec:</b></span> <span class="a"> ${student?.section?.name}</span><br>
                               <span class="b"><b>Class: </b></span> <span class="a">${student?.className?.name}</span><br>
                               <br>
                           </div>
                    </div>
                </div>


                <div class="well col-md-4">
                    <section class="panel">
                        <header class="panel-heading">
                            <span class="panel-header-info"><strong>SMS Alerts</strong></span>
                        </header>
                        <div class="panel-body">
                            <div class="row m-bot20">
                                <div class="col-md-7 col-xs-7">Notice</div>

                                <div class="col-md-5 col-xs-5">
                                    <input type="checkbox" name="smsAlert" checked data-on="success" data-off="info"
                                           class="switch-mini">
                                </div>
                            </div>

                            <div class="row m-bot20">
                                <div class="col-md-7 col-xs-7">Late Alert</div>

                                <div class="col-md-5 col-xs-5">
                                    <input type="checkbox" name="lateAlert" checked data-on="danger" data-off="default"
                                           class="switch-mini">
                                </div>
                            </div>

                            <div class="row m-bot20">
                                <div class="col-md-7 col-xs-7">Off Day Alert</div>

                                <div class="col-md-5 col-xs-5">
                                    <input type="checkbox" name="offDayAlert" checked data-on="warning"
                                           data-off="default" class="switch-mini">
                                </div>
                            </div>

                            <div class="row m-bot20">
                                <div class="col-md-7 col-xs-7">Due Alert</div>

                                <div class="col-md-5 col-xs-5">
                                    <input type="checkbox" name="dueAlert" checked data-on="danger" data-off="default"
                                           class="switch-mini">
                                </div>
                            </div>
                        </div>
                    </section>
                </div>
            </div>
        </section>
    </div>

    <div class="col-md-12">
        <section class="panel">
            <header class="panel-heading tab-bg-dark-navy-blue">
                <ul class="nav nav-tabs nav-justified" id="myTab">
                    <li class="active">
                        <a data-toggle="tab" href="#basicInfo">
                           <strong> Basic Info </strong>

                        </a>

                    </li>

                    <li>
                        <a data-toggle="tab" href="#fathersInfo">
                           <strong>Father's Info</strong>
                        </a>
                    </li>

                    <li>
                        <a data-toggle="tab" href="#mothersInfo">
                            <strong>Mother's info</strong>
                        </a>
                    </li>

                    <li>
                        <a data-toggle="tab" href="#othersInfo">
                            <strong>Other's Info</strong>
                        </a>
                    </li>

                </ul>
            </header>

            <div class="panel-body">
                <div class="tab-content tasi-tab">
                    <div id="basicInfo" class="tab-pane active">

                        <table class="table table-hover">
                            <thead>
                            <tr>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td><span class="b"><b>ID: </b></span> <span class="a"> ${student?.studentID}</span></td>
                                <td><span class="b"><b>Name: </b></span> <span class="a">${student?.name}</span></td>
                                <td><span class="b"><b>Mobile: </b></span> <span class="a">${student?.registration?.mobile}</span></td>
                                <td><span class="b"><b>Admission Year: </b></span> <span class="a">${student.registration?.admissionYear}</span></td>
                                <td><span class="b"><b>Card No: </b></span> <span class="a">${student.registration?.cardNo}</span> </td>
                            </tr>
                            <tr>
                                <td><span class="b"><b>Gender: </b></span> <span class="a">${student.registration?.gender}</span></td>
                            </tr>
                            <tr>
                                <td><span class="b"><b>Date of Birth: </b></span> <span class="a">${student.registration?.birthDate}</span></td>
                                <td><span class="b"><b>Religion: </b></span> <span class="a">${student.registration?.religion}</span></td>
                            </tr>
                            <tr>
                                <td><span class="b"><b>Blood Group: </b></span> <span class="a">${student.registration.bloodGroup}</span></td>
                                <td><span class="b"><b>Birth Certificate: </b></span> <span class="a">${student.registration?.birthCertificateNo}</span></td>
                                <td><span class="b"><b>Present Address: </b></span> <span class="a">${student.registration?.presentAddress}</span></td>
                                <td><span class="b"><b>permanent Address: </b></span> <span class="a">${student.registration?.permanentAddress}</span></td>
                            </tr>
                            <tr>
                                <td><span class="b"><b>E-mail: </b></span> <span class="a">${student.registration?.email}</span></td>
                            </tr>
                            </tbody>
                        </table>
                    </div>

                        <div id="fathersInfo" class="tab-pane ">

                            <table class="table table-hover">
                                <thead>
                                <tr>
                                    <th></th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td><span class="b"><b>Father's Name: </b></span> <span class="a">${student.registration?.fathersName}</span></td>
                                    <td><span class="b"><b>Father's Mobile: </b></span> <span class="a">${student.registration?.fathersMobile}</span></td>
                                </tr>
                                <tr>
                                    <td><span class="b"><b>Father's Profession: </b></span> <span class="a">${student.registration?.fathersProfession}</span></td>
                                </tr>
                                <tr>
                                    <td><span class="b"><b>Father's Income: </b></span> <span class="a">${student.registration?.fathersIncome}</span></td>
                                    <td><span class="b"><b>Father Alive: </b></span> <span class="a">${student.registration?.fatherIsAlive}</span></td>
                                </tr>
                                </tbody>
                            </table>

                    </div>

                    <div id="mothersInfo" class="tab-pane ">

                        <table class="table table-hover">
                            <thead>
                                <tr>
                                    <th></th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <td><span class="b"><b>Mother's Name: </b></span> <span class="a">${student.registration.mothersName}</span></td>
                                    <td><span class="b"><b>Mother's Mobile: </b></span> <span class="a">${student.registration?.mothersMobile}</span></td>
                                </tr>
                                <tr>
                                    <td><span class="b"><b>Mother's Profession: </b></span> <span class="a">${student.registration?.mothersProfession}</span></td>
                                    <td><span class="b"><b>Mother's Income: </b></span> <span class="a">${student.registration?.mothersIncome}</span></td>
                                    <td><span class="b"><b>Mother Alive: </b></span> <span class="a">${student.registration?.motherIsAlive}</span></td>
                                </tr>
                                </tbody>
                        </table>

                    </div>

                    <div id="othersInfo" class="tab-pane ">

                        <table class="table table-hover">
                            <thead>
                            <tr>
                                <th></th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <td><span class="b"><b>Guardian's Name: </b></span> <span class="a">${student.registration?.guardianName}</span></td>
                                <td><span class="b"><b>Guardian's Mobile: </b></span> <span class="a">${student.registration?.guardianMobile}</span> </td>
                            </tr>
                            <tr>
                            </tr>
                            <tr>
                            </tr>
                            <tr>
                            </tr>
                            <tr>
                            </tr>
                            </tbody>
                        </table>

                    </div>
                </div>
            </div>
        </section>
  </div>

    <script>
    $(document).ready(function () {
        $('#birthDate').datepicker({
            format: 'dd/mm/yyyy',
            default: new Date(),
            setData: new Date(),
            autoclose: true
        });

        $("#pImage").change(function () {
            readImageURL(this);
        });
        $("[name='emailAlert']").bootstrapSwitch();
        $("[name='smsAlert']").bootstrapSwitch();
        $("[name='lateAlert']").bootstrapSwitch();
        $("[name='offDayAlert']").bootstrapSwitch();
        $("[name='dueAlert']").bootstrapSwitch();
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
    </script>
</body>
</html>
