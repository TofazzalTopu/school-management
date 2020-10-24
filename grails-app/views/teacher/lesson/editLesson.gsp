<%@ page import="com.grailslab.enums.ExamTerm" %>
<!DOCTYPE html>
<head>
    <title>Lesson Plan</title>
    <meta name="layout" content="moduleLessonAndFeedbackLayout"/>
</head>
<body>
<grailslab:breadCrumbActions firstBreadCrumbUrl="${g.createLink(controller: 'lesson',action: 'lessonPlan', params: [id:section?.id])}" firstBreadCrumbText="Lesson Plan" breadCrumbTitleText="${className?.name?.toUpperCase()} - ${section? section.name.toUpperCase(): 'All'}"/>
<div class="row" id="holderNo1">
    <div class="col-sm-12">
        <section class="panel">
            <div class="panel-body">
                <div class="col-lg-12">
                    <form class="cmxform form-horizontal" id="create-form">
                        <g:hiddenField name="lessonId" id="lessonId" value="${lessonObj?.id}"/>
                        <g:hiddenField name="lessonDetailId" id="lessonDetailId" value="${lessonDetails.id}"/>
                        <div class="row">
                            <div class="form-group">
                                <label for="employee" class="col-md-2 control-label">Teacher *</label>

                                <div class="col-md-3">
                                    <select class="form-control" id="employee" name="employee" tabindex="1">
                                        <option value="${lessonDetails?.employee?.id}">${lessonDetails?.employee?.empID} - ${lessonDetails?.employee?.name}</option>
                                    </select>
                                </div>

                                <label for="examTerm" class="col-md-2 control-label">Exam Name *</label>

                                <div class="col-md-3">
                                    <g:select class="form-control" id="examTerm" name='examTerm' tabindex="1"
                                              noSelection="${['': 'Select Term...']}"
                                              from='${ExamTerm.values()}'
                                              optionKey="key" optionValue="value" value="${lessonObj?.examTerm?.key}" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="className" class="col-md-2 control-label">Class Name *</label>

                                <div class="col-md-3">
                                    <g:select class="form-control" id="className" name='className'  value="${className?.id}" tabindex="2"
                                              from='${classNameList}'
                                              optionKey="id" optionValue="name" />
                                </div>

                                <label for="section" class="col-md-2 control-label">Section Name </label>

                                <div class="col-md-3">
                                    <g:select class="form-control" id="section" value="${section?.id}" name='section' tabindex="2"
                                              from='${sectionList}' noSelection="${['': 'All Section']}"
                                              optionKey="id" optionValue="name" />
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="subject" class="col-md-2 control-label">Subjects *</label>

                                <div class="col-md-3">
                                    <g:select class="form-control" id="subject" name='subject' tabindex="2"
                                              noSelection="${['': 'Select Subject...']}"
                                              from='${subjectList}'
                                              optionKey="id" optionValue="name" value="${lessonDetails?.subject?.id}" />
                                </div>

                                <label for="" class="col-md-2 control-label">Lesson Date *</label>

                                <div class="col-md-3">
                                    <input class="form-control col-md-12" type="text" name="lessonDate" id="lessonDate" value="<g:formatDate format="dd/MM/yyyy" date="${lessonObj?.lessonDate}"/>"
                                           tabindex="6"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="homeWork" class="col-md-2 control-label">Home Work</label>

                                <div class="col-md-3">
                                    <label class="checkbox-inline">
                                        <g:if test="${lessonDetails?.oralHomeWork}">
                                            <input type="checkbox" id="oralHomeWork" name="oralHomeWork"  checked> Oral
                                        </g:if>
                                        <g:else>
                                            <input type="checkbox" id="oralHomeWork" name="oralHomeWork"> Oral
                                        </g:else>
                                    </label>
                                    <label class="checkbox-inline">
                                        <g:if test="${lessonDetails?.writtenHomeWork}">
                                            <input type="checkbox" id="writtenHomeWork" name="writtenHomeWork" checked> Written
                                        </g:if>
                                        <g:else>
                                            <input type="checkbox" id="writtenHomeWork" name="writtenHomeWork"> Written
                                        </g:else>
                                    </label>
                                </div>

                                <label for="" class="col-md-2 control-label">Exam Date</label>

                                <div class="col-md-3">
                                    <input class="form-control col-md-12" type="text" name="examDate" id="examDate" value="<g:formatDate format="dd/MM/yyyy" date="${lessonDetails?.examDate}"/>" tabindex="6"/>
                                </div>
                            </div>
                            <div class="form-group">

                            </div>

                            <div class="form-group">
                                <label for="topics" class="col-sm-2 control-label">Topics</label>

                                <div class="col-sm-8">
                                    <textarea rows="10" cols="50" id="topics" name="topics" class="form-control add-html-content-area">${raw(lessonDetails.topics)}</textarea>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="homeWork" class="col-sm-2 control-label">Comments</label>

                                <div class="col-sm-8">
                                    <textarea rows="4" cols="50" id="homeWork" name="homeWork" class="form-control add-html-content-area">${raw(lessonDetails.homeWork)}</textarea>
                                </div>
                            </div>

                            <br>

                            <div class="row">
                                <div class="form-group">
                                    <div class="col-md-offset-8 col-lg-4">
                                        <button class="btn btn-primary" tabindex="2" type="submit">Update</button>
                                        <button class="btn btn-default cancel-btn" tabindex="3">Cancel</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </section>
    </div>
</div>



<script>
    $('#lessonDate').datepicker({
        format: 'dd/mm/yyyy',
        endDate: '+90d',
        autoclose: true
    });

    $('#examDate').datepicker({
        format: 'dd/mm/yyyy',
        endDate: '+90d',
        autoclose: true
    });

    jQuery(function ($) {
        var validator = $('#create-form').validate({
            errorElement: 'span',
            focusInvalid: false,
            rules: {
                examTerm: {
                    required: true
                },
                className: {
                    required: true
                },
                lessonDate: {
                    required: true
                },
                subject: {
                    required: true
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
                if(!$("#topics").val()) {
                    alert("Please provide Lesson Topic.");
                    return false;
                }
                var examTerm = $("#examTerm").val();
                showLoading("#create-form-holder");
                $.ajax({
                    url: "${createLink(controller: 'lesson', action: 'update')}",
                    type: 'post',
                    dataType: "json",
                    data: $("#create-form").serialize(),
                    success: function (data) {
                        hideLoading("#create-form-holder");
                        if (data.isError == false) {
                            showSuccessMsg(data.message);
                            var className = $('#className').val();
                            var section = $("#section").val();
                            var examTerm = $("#examTerm").val();
                            var subject = $("#subject").val();
                            var lessonDate = $("#lessonDate").val();
                            //window.location.href = "$--{g.createLink(controller: 'lesson',action: 'create', params: [id:section?.id])}?examTerm="+examTerm;
                            window.location.href = "${g.createLink(controller: 'lesson',action: 'create')}?examTerm="+examTerm+"&subject="+subject+"&lessonDate="+lessonDate+"&classNameId="+className+"&sectionId="+section;
                        } else {
                            showErrorMsg(data.message);
                        }
                    },
                    failure: function (data) {
                    }
                })
            }
        });
        $(".cancel-btn").click(function (e) {
            e.preventDefault();
            window.location.href = "${g.createLink(controller: 'lesson',action: 'lessonPlan', params: [id:section?.id])}";
        });

        $('#className').on('change', function (e) {
            var className = $('#className').val();
            //window.location.href = "$--{g.createLink(controller: 'lesson',action: 'edit',params: [id:lessonDetails?.id])}?classNameId="+className;
            loadSection(className, 'section', 'holderNo1')
        });

        $('#section').on('change', function (e) {
            var className = $('#className').val();
            var section = $("#section").val();
            //window.location.href = "$--{g.createLink(controller: 'lesson',action: 'edit',params: [id:lessonDetails?.id])}?classNameId="+className+"&sectionId="+section;
            loadSectionSubject('holderNo1','classname','section','subject');
        });

        function loadSection(className, section, loadingCtrl){
            showLoading(loadingCtrl);
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'remote',action: 'listSection')}?className="+className,
                success: function (data, textStatus) {
                    hideLoading(loadingCtrl);
                    if (data.isError == false) {
                        var $select = $('#'+section);
                        $select.find("option:gt(0)").remove();
                        $.each(data.sectionList,function(key, value)
                        {
                            $select.append('<option value=' + value.id + '>' + value.name + '</option>');
                        });
                    } else {
                        showErrorMsg(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
        }

        function loadSectionSubject(holderId, classId, sectionId, subjectId) {
            var section = $('#'+sectionId).val();
            var url;
            if (section) {
                url= "${g.createLink(controller: 'remote',action: 'sectionSubjectList')}?id=" + section
                showLoading("#"+holderId);
            } else {
                var className = $('#'+classId).val();
                if(className){
                    url= "${g.createLink(controller: 'remote',action: 'classSubjectList')}?id=" + className
                    showLoading("#"+holderId);
                }
            }
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: url,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        var $select = $('#'+subjectId);
                        $select.find('option').remove();
                        $select.append('<option value="">Select Subject</option>');
                        $.each(data.subjectList, function (key, value) {
                            $select.append('<option value=' + value.id + '>' + value.name + '</option>');
                        });
                    } else {
                        showErrorMsg(data.message);
                    }
                    hideLoading("#"+holderId);
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });

        }
    });

</script>
</body>
</html>