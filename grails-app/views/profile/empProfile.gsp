<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta name="layout" content="adminLayout"/>
    <title>Profile</title>
</head>

<body>
<grailslab:breadCrumbActions breadCrumbTitleText="${employee?.name} Profile"/>

<grailslab:fullModal label="Job History  Of ${employee.name}" modalId="job-history-modal" hiddenId="jobHistoryHiddenId"
                     formId="job-history-formId">
    <input type="hidden" id="employeeId" name="employee"/>
    <grailslab:input required="true" name="company" label="Company"></grailslab:input>
    <grailslab:input required="true" name="jobTitle" label="Job Title"></grailslab:input>
    <grailslab:input required="true" name="joiningDate" label="Joining Date"></grailslab:input>
    <grailslab:input required="true" name="endDate" label="End Date"></grailslab:input>
    <grailslab:textArea required="true" name="location" label="Location"></grailslab:textArea>
</grailslab:fullModal>

<div class="row">
    <div class="col-md-12">
        <section class="panel">
            <div class="panel-body profile-information">
                <div class="col-md-2">
                    <div class="profile-pic text-center">
                        <g:if test="${employee?.imagePath}">
                            <img src="${imgSrc.fromIdentifier(imagePath: employee?.imagePath)}"
                                 alt="employee?.imagePath">
                        </g:if>
                        <g:else>
                            <asset:image src="no-image.jpg" alt="avatar"/>
                        </g:else>
                    </div>
                </div>

                <div class="col-md-6">
                    <div class="profile-desk">
                        <h1>${employee.name}</h1>
                        <span class="text-muted">${designation}</span>
                        <h5>
                            ${employee?.aboutMe}
                        </h5>
                    </div>
                </div>

                <div class="well col-md-4">
                    <section class="panel">
                        <div class="panel-body">
                            <div class="row m-bot20">
                                <div class="col-md-7 col-xs-7">Email Alert</div>

                                <div class="col-md-5 col-xs-5">
                                    <input type="checkbox" name="emailAlert" checked class="switch-mini">
                                </div>
                            </div>

                            <div class="row m-bot20">
                                <div class="col-md-7 col-xs-7">Sms Alert</div>

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
                        <a data-toggle="tab" href="#personalInfo">
                            Personal Details
                        </a>

                    </li>
                    <li>
                        <a data-toggle="tab" href="#job-history">
                            Job History
                        </a>
                    </li>
                    <li>
                        <a data-toggle="tab" href="#educationalInfo">
                            Academic Qualification
                        </a>
                    </li>
                </ul>
            </header>

            <div class="panel-body">
                <div class="tab-content tasi-tab">
                    <div id="personalInfo" class="tab-pane active">
                        <g:render template="personalInfo"/>
                    </div>


                    <div id="job-history" class="tab-pane ">
                        <div class="row">
                            <sec:access controller='profile' action='edit'>
                                <div class="col-md-12">
                                    <input type="button" class="btn btn-primary insert-job-History" value="Add New Job">
                                </div>
                            </sec:access>
                            <div class="col-md-12">
                                <grailslab:dataTable dataSet="${dataJobInfoList}"
                                                     tableHead="Company,Job Title Name,Joining Date,End Date,Location"
                                                     tableId="job-datable-id"></grailslab:dataTable>
                            </div>
                        </div>
                    </div>

                    <div id="educationalInfo" class="tab-pane ">
                        <div class="row">
                            <sec:access controller='profile' action='employmentHistoryEdit'>
                                <div class="col-md-12">
                                    <input type="button" class="btn btn-primary insertEducationalInfo"
                                           value="Add Academic Info">
                                </div>
                            </sec:access>
                            <div class="col-md-12">
                                <grailslab:dataTable dataSet="${academicInfoList}"
                                                     tableHead="Certification,Institution,Major Subject,Passing Year,Result,Duration"
                                                     tableId="educational-datable-id"></grailslab:dataTable>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>


<grailslab:fullModal label="Personal Info Of ${employee?.name}" formId="formPersonalInfo" modalId="modalPersonalInfo"
                     modalClass="modal-big"
                     hiddenId="formPersonalInfoId">

    <div class="form-group">
        <label class="col-md-2 control-label">Name<span class="required">*</span></label>

        <div class="col-md-4">
            <input type="text" name="name" id="name" class="form-control"/>
        </div>

        <label class="col-md-2 control-label">Birth Date <span class="required">*</span></label>

        <div class="col-md-4">
            <input type="text" name="birthDate" id="birthDate" class="form-control"/>
        </div>
    </div>

    <div class="form-group">
        <label class="col-md-2 control-label">Fathers Name <span class="required">*</span></label>

        <div class="col-md-4">
            <input type="text" name="fathersName" id="fathersName" class="form-control"/>
        </div>

        <label class="col-md-2 control-label">Mothers Name <span class="required">*</span></label>

        <div class="col-md-4">
            <input type="text" name="mothersName" id="mothersName" class="form-control"/>
        </div>
    </div>

    <div class="form-group">
        <label class="col-md-2 control-label">Present Add<span class="required">*</span></label>

        <div class="col-md-4">
            <textarea name="presentAddress" id="presentAddress" class="form-control"></textarea>
        </div>

        <label class="col-md-2 control-label">Permanent Add<span class="required">*</span></label>

        <div class="col-md-4">
            <textarea name="permanentAddress" id="permanentAddress" class="form-control"></textarea>
        </div>
    </div>

    <div class="form-group">
        <label class="col-md-2 control-label">Email <span class="required">*</span></label>

        <div class="col-md-4">
            <input type="text" name="emailId" id="emailId" class="form-control"/>
        </div>
        <label class="col-md-2 control-label">Mobile No<span class="required">*</span></label>

        <div class="col-md-4">
            <input type="text" name="mobile" id="mobile" class="form-control"/>
        </div>


    </div>

    <div class="form-group">
        <label class="col-md-2 control-label">Blood Group</label>

        <div class="col-md-4">
            <input type="text" name="bloodGroup" id="bloodGroup" class="form-control"/>
        </div>

        <label class="col-md-2 control-label">Facebook Link</label>

        <div class="col-md-4">
            <input type="text" name="fbId" id="fbId" class="form-control"/>
        </div>
    </div>

    <div class="form-group">
        <label class="col-md-2 control-label">National ID</label>

        <div class="col-md-4">
            <input type="text" name="nationalId" id="nationalId" class="form-control"/>
        </div>

        <label class="col-md-2 control-label">Card No</label>

        <div class="col-md-4">
            <input type="text" name="cardNo" id="cardNo" class="form-control"/>
        </div>
    </div>

    <div class="form-group">
        <label class="col-md-2 control-label">About Me</label>
        <div class="col-md-5">
            <textarea name="aboutMe" id="aboutMe" class="form-control" rows="10"></textarea>
        </div>
        <label class="col-md-2 control-label">Profile Image</label>
        <div class="col-md-3">
            <input type="file" name="pImage" id="pImage"/>
            <br/>
            <g:if test="${employee?.imagePath}">
                <img src="${imgSrc.fromIdentifier(imagePath: employee?.imagePath)}" id="ImagePreview" alt=" " class="thumbnail"
                     width="190px" height="190px">
            </g:if>
            <g:else>
                <asset:image src="no-image.jpg" alt="avatar" id="ImagePreview" width="190px" height="190px" class="thumbnail"/>
            </g:else>
        </div>
    </div>

</grailslab:fullModal>

<grailslab:fullModal label="Edu Info Of ${employee.name}" formId="formEduInfo" modalId="modalEduInfo"
                     hiddenId="formEduInfoId">
    <input type="hidden" name="employee" id="employeeIdEdu" label="Name"/>
    <grailslab:input name="name" label="Name"></grailslab:input>
    <grailslab:select required="true" name="certification" label="Certification"
                      from="${com.grailslab.hr.HrCertification.list()}"></grailslab:select>
    <grailslab:select required="true" name="institution" label="Institution"
                      from="${com.grailslab.hr.Institution.list()}"></grailslab:select>
    <grailslab:select required="true" name="majorSubject" label="Major Subject"
                      from="${com.grailslab.settings.SubjectName.list()}"></grailslab:select>
    <grailslab:select name="board" label="Board"
                      from="${com.grailslab.hr.Board.list()}"></grailslab:select>
    <grailslab:input required="true" name="passingYear" label="Passing Year"></grailslab:input>
    <grailslab:input required="true" name="result" label="Result"></grailslab:input>
    <grailslab:input required="true" name="duration" label="Duration"></grailslab:input>
</grailslab:fullModal>

<script>

    $('#birthDate').datepicker({
        format: 'dd/mm/yyyy',
        default: new Date(),
        setData: new Date(),
        autoclose: true
    });
    $('#joiningDate').datepicker({
        format: 'dd/mm/yyyy',
        default: new Date(),
        setData: new Date(),
        autoclose: true
    });
    $('#endDate').datepicker({
        format: 'dd/mm/yyyy',
        default: new Date(),
        setData: new Date(),
        autoclose: true
    });
    $('#passingYear').datepicker({
        format: 'dd/mm/yyyy',
        default: new Date(),
        setData: new Date(),
        autoclose: true
    });
    $(document).ready(function () {
        $('.insert-personalInfo').click(function (e) {
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'profile',action: 'edit')}",
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        var dateBirth = data.obj.birthDate;
                        $('#formPersonalInfo').resetForm();
                        $('#pImage').val("");
                        $('#name').val(data.obj.name);
                        $('#fathersName').val(data.obj.fathersName);
                        $('#mothersName').val(data.obj.mothersName);
                        $('#presentAddress').val(data.obj.presentAddress);
                        $('#permanentAddress').val(data.obj.permanentAddress);
                        $('#emailId').val(data.obj.emailId);
                        $('#bloodGroup').val(data.obj.bloodGroup);
                        $('#cardNo').val(data.obj.cardNo);
                        $('#mobile').val(data.obj.mobile);
                        $('#fbId').val(data.obj.fbId);
                        $('#nationalId').val(data.obj.nationalId);
                        $('#aboutMe').val(data.obj.aboutMe);
                        $('#birthDate').datepicker('setDate', new Date(dateBirth));
                        $('#modalPersonalInfo .create-success').hide();
                        $('#modalPersonalInfo .create-content').show();
                        $(".form-group").removeClass("has-error");
                        $('#modalPersonalInfo').modal('show');
                    } else {
                        showErrorMsg(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
        });

        $('.insert-job-History').click(function (e) {
            $('#job-history-modal .create-success').hide();
            $('#job-history-modal .create-content').show();
            $('#job-history-formId').resetForm();
            $(".form-group").removeClass("has-error");
            $("#jobHistoryHiddenId").val('');
            $('#job-history-modal').modal('show');
            e.preventDefault();
        });

        $('#formPersonalInfo').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                name: {
                    required: true
                },
                fathersName: {
                    required: true
                },
                mothersName: {
                    required: true
                },
                birthDate: {
                    required: true
                },
                presentAddress: {
                    required: true
                },
                permanentAddress: {
                    required: true
                },
                emailId: {
                    required: true,
                    email: true
                },
                mobile: {
                    required: true,
                    digits: true,
                    minlength: 11,
                    maxlength: 13
                }
            },
            highlight: function (e) {
                $(e).closest('.form-group').removeClass('has-info').addClass('has-error');
            },
            success: function (e) {
                $(e).closest('.form-group').removeClass('has-error').addClass('has-info');
                $(e).remove();

            },
            submitHandler: function (form) {
                $('#modalPersonalInfo .create-content .modal-footer-action-btn').hide();
                $('#modalPersonalInfo .create-content .modal-refresh-processing').show();
                var formData = new FormData(form);
                $.ajax({
                    url: "${createLink(controller: 'profile', action: 'personalInfoUpdate')}",
                    type: 'post',
                    dataType: "json",
                    data: formData,
                    mimeType: 'multipart/form-data',
                    contentType: false,
                    cache: false,
                    processData: false,
                    success: function (data) {
                        $('#modalPersonalInfo .create-content .modal-refresh-processing').hide();
                        $('#modalPersonalInfo .create-success .modal-footer-action-btn').show();
                        if (data.isError == true) {
                            $('#modalPersonalInfo .create-success p.message-content').html(data.message);
                        } else {
                            $('#modalPersonalInfo .create-success .message-content').html(data.message);
                        }
                        $('#modalPersonalInfo .create-content').hide(1000);
                        $('#modalPersonalInfo .create-success').show(1000);
                        $('#modalPersonalInfo .create-content .modal-footer-action-btn').show();
                    },
                    failure: function (data) {
                    }
                })
            }
        });
        $('#job-history-formId').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                company: {
                    required: true
                },
                jobTitle: {
                    required: true
                }
            },
            errorPlacement: function (error, element) {
            },
            highlight: function (e) {
                $(e).closest('.form-group').removeClass('has-info').addClass('has-error');
            },
            success: function (e) {
                $(e).closest('.form-group').removeClass('has-error').addClass('has-info');
                $(e).remove();

            },
            submitHandler: function (form) {
                $('#job-history-modal .create-content .modal-footer-action-btn').hide();
                $('#job-history-modal .create-content .modal-refresh-processing').show();
                $.ajax({
                    url: "${createLink(controller: 'profile', action: 'employmentHistorySave')}",
                    type: 'post',
                    dataType: "json",
                    data: $("#job-history-formId").serialize(),
                    success: function (data) {
                        $('#job-history-modal .create-content .modal-refresh-processing').hide();
                        $('#job-history-modal .create-success .modal-footer-action-btn').show();
                        if (data.isError == true) {
                            $('#job-history-modal .create-success p.message-content').html(data.message);
                            $('#job-history-modal .create-content').hide(1000);
                            $('#job-history-modal .create-success').show(1000);
                            $('#job-history-modal .create-content .modal-footer-action-btn').show();
                        } else {
                            $('#job-history-modal .create-success .message-content').html(data.message);
                            $('#job-history-modal .create-content').hide(1000);
                            $('#job-history-modal .create-success').show(1000);
                            $('#job-history-modal .create-content .modal-footer-action-btn').show();
                        }
                    },
                    failure: function (data) {
                    }
                })
            }
        });

        $('#job-datable-id').on('click', 'a.edit-reference', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'profile',action: 'employmentHistoryEdit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        var joiningDate = data.obj.joiningDate
                        var endDate = data.obj.endDate
                        $('#jobHistoryHiddenId').val(data.obj.id);
                        $('#company').val(data.obj.company);
                        $('#employeeId').val(data.obj.employee.id);
                        $('#jobTitle').val(data.obj.jobTitle);
                        $('#location').val(data.obj.location);
                        $('#joiningDate').datepicker('setDate', new Date(joiningDate));
                        $('#endDate').datepicker('setDate', new Date(endDate));
                        $('#job-history-modal .create-success').hide();
                        $('#job-history-modal .create-content').show();
                        $(".form-group").removeClass("has-error");
                        $('#job-history-modal').modal('show');
                    } else {
                        showErrorMsg(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            e.preventDefault();
        });


        $('#job-datable-id').on('click', 'a.delete-reference', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'profile',action: 'employmentHistoryDelete')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            selectRow.remove();
                            showSuccessMsg(data.message);
                        } else {
                            showErrorMsg(data.message);
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            }
            e.preventDefault();
        });

        $('#job-datable-id').on('click', 'a.inactive-reference', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'profile',action: 'employmentHistoryInactive')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            selectRow.remove();
                            showSuccessMsg(data.message);
                        } else {
                            showErrorMsg(data.message);
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            }
            e.preventDefault();
        });

        $('.insertEducationalInfo').click(function (e) {
            $('#modalEduInfo .create-success').hide();
            $('#modalEduInfo .create-content').show();
            $('#formEduInfo').resetForm();
            $(".form-group").removeClass("has-error");
            $("#formEduInfoId").val('');
            $('#modalEduInfo').modal('show');
            e.preventDefault();
        });

        $('#formEduInfo').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                certification: {
                    required: true
                },
                institution: {
                    required: true
                },
                majorSubject: {
                    required: true
                },
                result: {
                    required: true
                }
            },
            errorPlacement: function (error, element) {
            },
            highlight: function (e) {
                $(e).closest('.form-group').removeClass('has-info').addClass('has-error');
            },
            success: function (e) {
                $(e).closest('.form-group').removeClass('has-error').addClass('has-info');
                $(e).remove();

            },
            submitHandler: function (form) {
                $('#modalEduInfo .create-content .modal-footer-action-btn').hide();
                $('#modalEduInfo .create-content .modal-refresh-processing').show();
                $.ajax({
                    url: "${createLink(controller: 'profile', action: 'formEduInfoSave')}",
                    type: 'post',
                    dataType: "json",
                    data: $("#formEduInfo").serialize(),
                    success: function (data) {
                        $('#modalEduInfo .create-content .modal-refresh-processing').hide();
                        $('#modalEduInfo .create-success .modal-footer-action-btn').show();
                        if (data.isError == true) {
                            $('#modalEduInfo .create-success p.message-content').html(data.message);
                        } else {
                            $('#modalEduInfo .create-success .message-content').html(data.message);
                        }
                        $('#modalEduInfo .create-content').hide(1000);
                        $('#modalEduInfo .create-success').show(1000);
                        $('#modalEduInfo .create-content .modal-footer-action-btn').show();
                    },
                    failure: function (data) {
                    }
                })
            }
        });

        $('#educational-datable-id').on('click', 'a.edit-reference', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'profile',action: 'educationalInfoEdit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        var passingYear = data.obj.passingYear
                        var endDate = data.obj.endDate
                        $('#formEduInfoId').val(data.obj.id);
                        $('#employeeIdEdu').val(data.obj.employee.id);
                        $('#name').val(data.obj.name);
                        $('#certification').val(data.obj.certification.id);
                        $('#institution').val(data.obj.institution.id);
                        $('#majorSubject').val(data.obj.majorSubject.id);
                        $('#board').val(data.obj.board.id);
                        $('#result').val(data.obj.result);
                        $('#duration').val(data.obj.duration);
                        $('#passingYear').datepicker('setDate', new Date(passingYear));
                        $('#modalEduInfo .create-success').hide();
                        $('#modalEduInfo .create-content').show();
                        $(".form-group").removeClass("has-error");
                        $('#modalEduInfo').modal('show');
                    } else {
                        showErrorMsg(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            e.preventDefault();
        });

        $('#educational-datable-id').on('click', 'a.delete-reference', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'profile',action: 'educationalInfoDelete')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            selectRow.remove();
                            showSuccessMsg(data.message);
                        } else {
                            showErrorMsg(data.message);
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            }
            e.preventDefault();
        });

        $('#educational-datable-id').on('click', 'a.inactive-reference', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'profile',action: 'educationalInfoInactive')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            selectRow.remove();
                            showSuccessMsg(data.message);
                        } else {
                            showErrorMsg(data.message);
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            }
            e.preventDefault();
        });

        $("#pImage").change(function () {
            readImageURL(this);
        });


        $('.cancel-btn').click(function (e) {
            location.reload();
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
