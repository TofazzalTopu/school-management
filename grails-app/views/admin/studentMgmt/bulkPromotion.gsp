<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleStdMgmtLayout"/>
    <title>Student Bulk Promotion</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Manage Bulk Promotion"/>
<div class="row" id="create-form-holder">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Student Bulk Promotion By Section (Same Roll No in New Section)
            </header>
            <div class="panel-body">
                <div class="row">
                    <div class="col-md-8 col-md-offset-2">
                        <div class="row">
                            <div class="form-horizontal">
                                <div class="form-group">
                                    <label for="academicYearOld" class="col-md-3 control-label">Academic Year*</label>
                                    <div class="col-md-8">
                                        <g:select tabindex="1" class="form-control"
                                                  id="academicYearOld" name='academicYearOld'
                                                  from='${academicYearList}'
                                                  optionKey="key" optionValue="value"></g:select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="classNameOld" class="col-md-3 control-label">Class Name*</label>
                                    <div class="col-md-5">
                                        <g:select tabindex="2" class="form-control" id="classNameOld"
                                                  name='classNameOld'
                                                  from='${classNameList}'
                                                  optionKey="id" optionValue="name"></g:select>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="sectionOld" class="col-md-3 control-label">Section Name *</label>
                                    <div class="col-md-5">
                                        <select id="sectionOld" name='sectionOld' class="form-control" required="required">
                                            <option value="">Select Section</option>
                                        </select>
                                    </div>
                                    <div class="col-md-3">
                                        <button class="btn btn-info" id="load-old-btn">Load Promotion</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="col-md-12 alert-info" id="readmission-student-holder" style="display: none;">
                    </br>
                        <div class="row">
                            <form class="cmxform form-horizontal" id="readmission-form">
                                <g:hiddenField name="academicYear"/>
                                <g:hiddenField name="oldSectionId"/>
                                <div class="form-group col-md-2">
                                    <label class="col-md-4 control-label">Year</label>
                                    <div class="col-md-8">
                                        <label class="control-label">: <span id="yearHolder"></span> </label>
                                    </div>
                                </div>
                                <div class="form-group col-md-2">
                                    <label class="col-md-3 control-label">Class</label>
                                    <div class="col-md-9">
                                        <label class="control-label">: <span id="classNameHolder"></span></label>
                                    </div>
                                </div>
                                <div class="form-group col-md-6">
                                    <label for="section" class="col-md-4 control-label">Section *</label>
                                    <div class="col-md-4">
                                        <select id="section" name='section' class="form-control" required="required"></select>
                                    </div>
                                    <div class="col-md-3">
                                        <button class="btn btn-primary" id="submit-readmission-btn">Submit</button>
                                    </div>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>


<script>
    var className, academicYear,section, sectionOld, classUrl;
    jQuery(function ($) {
        $('#academicYearOld').on('change', function (e) {
            $('#classNameOld').val("").trigger("change");
        });
        $('#classNameOld').on('change', function (e) {
            className = $('#classNameOld').val();
            academicYear = $('#academicYearOld').val();
            if (academicYear && className) {
                classUrl = "${g.createLink(controller: 'remote',action: 'listSection')}?className="+className+"&academicYear="+academicYear;
                loadClassSection(classUrl, '#sectionOld', "#create-form-holder")
            }
        });
        $('#load-old-btn').click(function (e) {
            className = $('#classNameOld').val();
            academicYear = $('#academicYearOld').val();
            sectionOld = $('#sectionOld').val();
            if (sectionOld) {
                jQuery.ajax({
                    type: 'POST',
                    dataType:'JSON',
                    url: "${g.createLink(controller: 'student',action: 'loadBulkPromotion')}?academicYear="+academicYear+"&className="+className+"&sectionOld="+sectionOld,
                    success: function (data) {
                        showLoading("#create-form-holder");
                        if(data.isError==true){
                            $("#student-datalist-holder").hide(500);
                            $("#readmission-student-holder").hide(500);
                            showErrorMsg(data.message);
                        }else {
                            $('#oldSectionId').val(data.oldSectionId);
                            $('#academicYear').val(data.academicYear);
                            $('#yearHolder').html(data.academicYearStr);
                            $('#classNameHolder').html(data.className);

                            var $select1 = $('#section');
                            $select1.find('option').remove();
                            $select1.append('<option value="">Select Section</option>');
                            $.each(data.sectionList,function(key, value)
                            {
                                $select1.append('<option value=' + value.id + '>' + value.name + '</option>');
                            });
                            $("#readmission-student-holder").show(500);
                        }
                        hideLoading("#create-form-holder");
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            } else {
                showErrorMsg("Select Section");
            }
            e.preventDefault();
        });


        var validator = $('#readmission-form').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                student: {
                    required: true
                },
                academicYear: {
                    required: true
                },
                section: {
                    required: true
                },
                rollNo: {
                    required: true,
                    digits: true
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
                showLoading("#create-form-holder");
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    data: $("#readmission-form").serialize(),
                    url: "${g.createLink(controller: 'student',action: 'saveBulkPromotion')}",
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            $("#readmission-student-holder").hide(500);
                            showSuccessMsg(data.message);
                        } else {
                            showErrorMsg(data.message);
                        }
                        hideLoading("#create-form-holder");
                    },
                    failure: function (data) {
                    }
                })
            }
        });
    });


</script>
</body>
</html>
