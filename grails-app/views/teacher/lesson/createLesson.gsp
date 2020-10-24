<!DOCTYPE html>
<head>
    <title>Lesson Plan</title>
    <meta name="layout" content="moduleLessonAndFeedbackLayout"/>
</head>
<body>
<grailslab:breadCrumbActions firstBreadCrumbUrl="${g.createLink(controller: 'lesson',action: 'lessonPlan', params: [classNameId : className?.id, sectionId : section?.id])}" firstBreadCrumbText="Lesson Plan" breadCrumbTitleText="${className?.name?.toUpperCase()} - ${section? section.name.toUpperCase(): 'All'}"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <div class="panel-body">
                <div class="col-lg-12">
                    <form class="cmxform form-horizontal" id="create-form">
                        <div class="row">
                            <div class="form-group">
                                <label for="employee" class="col-md-2 control-label">Teacher *</label>

                                <div class="col-md-3">
                                    <select class="form-control" id="employee" name="employee" tabindex="1">
                                        <option value="${workingTeacher?.id}">${workingTeacher?.empID} - ${workingTeacher?.name}</option>
                                    </select>
                                </div>
                                <label for="examTerm" class="col-md-2 control-label">Exam Name *</label>

                                <div class="col-md-3">
                                    <g:select class="form-control" id="examTerm" name='examTerm' tabindex="1"
                                              from='${com.grailslab.enums.ExamTerm.values()}' value="${examTerm?.key}"
                                              optionKey="key" optionValue="value" />
                                </div>
                            </div>
                            <div class="form-group">
                                    <label for="className" class="col-md-2 control-label">Class Name *</label>

                                    <div class="col-md-3">
                                        <g:select class="form-control" id="className" name='className'  value="${className?.id}" tabindex="2"
                                                  from='${classNameList}'
                                                  optionKey="id" optionValue="name" />
                                    </div>
                                    <label for="subject" class="col-md-2 control-label">Subjects *</label>

                                    <div class="col-md-3">
                                        <g:select class="form-control" id="subject" name='subject' value="${subject?.id}" tabindex="3"
                                                  noSelection="${['': 'Select Subject...']}"
                                                  from='${subjectList}'
                                                  optionKey="id" optionValue="name" />
                                    </div>
                                    %{--<label for="employee" class="col-md-2 control-label">Section Name</label>

                                    <div class="col-md-3">
                                        <g:select class="form-control" id="section" value="${section?.id}" name='section' tabindex="2"
                                                  from='${sectionList}' noSelection="${['': 'All Section...']}"
                                                  optionKey="id" optionValue="name" />
                                    </div>--}%
                            </div>
                            <div class="form-group">
                                <label for="homeWork" class="col-md-2 control-label">Home Work</label>

                                <div class="col-md-3">
                                    <label class="checkbox-inline">
                                        <input type="checkbox" id="oralHomeWork" name="oralHomeWork" > Oral
                                    </label>
                                    <label class="checkbox-inline">
                                        <input type="checkbox" id="writtenHomeWork" name="writtenHomeWork" > Written
                                    </label>
                                </div>

                                <label for="examDate" class="col-md-2 control-label">Exam Date</label>

                                <div class="col-md-3">
                                    <input class="form-control col-md-12" type="text" name="examDate"
                                           id="examDate"
                                           tabindex="5"/>
                                </div>

                            </div>
                            <div class="form-group">
                                <label for="allSection" class="col-md-2 control-label"></label>

                                <div class="col-md-3">
                                    <label class="checkbox-inline">
                                        <input class="allSectionCheckbox" type="checkbox" id="allSection" name="allSection" value="all" checked="checked" > All Section
                                    </label>
                                </div>
                                <label class="col-md-2 control-label" id="lessonDateLbl">Lesson Date *</label>

                                <div class="col-md-3" id="lessonDateDiv">
                                    <input class="form-control col-md-12" type="text" name="lessonDate"
                                           id="lessonDate" value="<g:formatDate format="dd/MM/yyyy" date="${lessonDate}"/>"
                                           tabindex="4"/>
                                </div>

                            </div>
                            <div  id="allSectionHolder">
                                <g:each in="${sectionList}" var="sectionObj">
                                    <g:hiddenField name="sectionObjIds" value="${sectionObj.id}"/>
                                    <div class="form-group">
                                        <label class="col-md-2 control-label">Section</label>

                                        <div class="col-md-3">
                                            <label class="checkbox-inline">
                                                <g:checkBox id="section${sectionObj.id}" name="section${sectionObj.id}" sectionObjId="${sectionObj.id}" />${sectionObj.name}
                                            </label>
                                        </div>
                                        <label class="col-md-2 control-label">Lesson Date *</label>

                                        <div class="col-md-3">
                                            <input class="secDatepicker form-control col-md-12" type="text" name="lessonDate${sectionObj.id}"
                                                   id="lessonDate${sectionObj.id}" value="<g:formatDate format="dd/MM/yyyy" date="${lessonDate}"/>"
                                                   tabindex="4" sectionObjId="${sectionObj.id}"/>
                                        </div>
                                    </div>
                                </g:each>
                            </div>

                            <div class="form-group">
                                <label for="topics" class="col-sm-2 control-label">Topics</label>

                                <div class="col-sm-8">
                                    <textarea rows="10" cols="50" id="topics" tabindex="6" name="topics" class="form-control add-html-content-area"></textarea>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="homeWork" class="col-sm-2 control-label">Comments</label>

                                <div class="col-sm-8">
                                    <textarea rows="4" cols="50" id="homeWork" tabindex="7" name="homeWork" class="form-control add-html-content-area"></textarea>
                                </div>
                            </div>
                            <div class="row">
                                <div class="form-group">
                                    <div class="col-md-offset-8 col-lg-4">
                                        <button class="btn btn-primary" tabindex="8" type="submit">Save</button>
                                        <button class="btn btn-default cancel-btn" tabindex="9"
                                                type="reset">Cancel</button>
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
<g:if test="${lessonList}">
    <div class="row">
        <div class="col-sm-12">
            <section class="panel">
                <header class="panel-heading">
                    Your Today's working Lesson(s)
                </header>
                <div class="panel-body">
                    <div class="col-md-12">
                        <div class="table-responsive">
                            <table class="table table-bordered table-striped table-hover dataTable no-footer" id="list-table">
                                <thead>
                                <tr>
                                    <th class="col-md-2">Lesson Date</th>
                                    <th class="col-md-1">Subject</th>
                                    <th class="col-md-4">Lesson Topics</th>
                                    <th class="col-md-2">Home Work</th>
                                    <th class="col-md-1">Exam</th>
                                    <th class="col-md-2">Action</th>
                                </tr>
                                </thead>
                                <tbody>
                                <g:each in="${lessonList}" var="lesson" status="i">
                                    <tr class="even">
                                        <td>${i+1}. ${lesson.lessonDate.format('E, dd MMM yyyy')} <p>${lesson.className}, ${lesson.sectionName}</p></td>
                                        <td>${lesson.subjectName}</td>
                                        <td>${raw(lesson.topics)}</td>
                                        <td>${raw(lesson.homeWork)}</td>
                                        <td>${lesson.examDate?.format('dd-MMM-yy')}</td>
                                        <td>
                                            <span class="col-md-4 no-padding"><a href="${g.createLink(controller: 'lesson',action: 'edit',id:lesson.lessonDetailId)}" referenceId="${lesson.lessonDetailId}"
                                                                                 class="edit-reference" title="Edit Lesson"><span
                                                        class="green glyphicon glyphicon-edit"></span></a></span>
                                            <span class="col-md-4 no-padding"><a href="" lessonId="${lesson.lessonId}" referenceId="${lesson.lessonDetailId}"
                                                                                 class="delete-reference"
                                                                                 title="Delete Lesson"><span
                                                        class="green glyphicon glyphicon-trash"></span></a></span>
                                        </td>
                                    </tr>
                                </g:each>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </div>
</g:if>


<script>

    var referenceId;
    $( document ).ready(function() {
        if($('#allSection').prop("checked")){
            $('#allSectionHolder').hide();
            $('#lessonDateDiv').show();
            $('#lessonDateLbl').show();
        }else{
            $('#allSectionHolder').show();
            $('#lessonDateDiv').hide();
            $('#lessonDateLbl').hide();
        }

    });

    jQuery(function ($) {
        $("#topics").focus();
        $(".secDatepicker").datepicker({
            format: 'dd/mm/yyyy',
            endDate: '+365d',
            autoclose: true
        });
        $('#lessonDate').datepicker({
            format: 'dd/mm/yyyy',
            endDate: '+365d',
            autoclose: true
        });

        $('#examDate').datepicker({
            format: 'dd/mm/yyyy',
            endDate: '+365d',
            autoclose: true
        });

        var validator = $('#create-form').validate({
            errorElement: 'span',
            focusInvalid: false,
            rules: {
                examTerm: {
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
                var className = $("#className").val();
                var subjectName = $("#subject").val();
                var lessonDate = $("#lessonDate").val();
                var oralHomeWork = $("#oralHomeWork").val();
                var writtenHomeWork = $("#writtenHomeWork").val();
                showLoading("#create-form");
                $.ajax({
                    url: "${createLink(controller: 'lesson', action: 'save')}",
                    type: 'post',
                    dataType: "json",
                    data: $("#create-form").serialize(),
                    success: function (data) {
                        hideLoading("#create-form");
                        if (data.isError == false) {
                            showSuccessMsg(data.message);
                            //window.location.href = "$--{g.createLink(controller: 'lesson',action: 'create', params: [id:section?.id])}?examTerm="+examTerm+"&subject="+subjectName+"&lessonDate="+lessonDate;
                            window.location.href = "${g.createLink(controller: 'lesson',action: 'create')}?examTerm="+examTerm+"&subject="+subjectName+"&lessonDate="+lessonDate+"&classNameId="+className;
                        } else {
                            showErrorMsg(data.message);
                        }
                    },
                    failure: function (data) {
                    }
                })
            }
        });
        $('#className').on('change', function (e) {
            var className = $('#className').val();
            var section = $("#section").val();
            var examTerm = $("#examTerm").val();
            var subjectName = $("#subject").val();
            var lessonDate = $("#lessonDate").val();
            window.location.href = "${g.createLink(controller: 'lesson',action: 'create')}?examTerm="+examTerm+"&subject="+subjectName+"&lessonDate="+lessonDate+"&classNameId="+className+"&sectionId="+section;
        });

        $('#list-table').on('click', 'a.delete-reference', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                var lessonId = $(control).attr('lessonId');
                var className = $('#className').val();
                var section = $("#section").val();
                var examTerm = $("#examTerm").val();
                var subjectName = $("#subject").val();
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'lesson',action: 'delete')}?id=" +lessonId +"&lessonDetailId="+referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            showSuccessMsg(data.message);
                            window.location.href = "${g.createLink(controller: 'lesson',action: 'create')}?examTerm="+examTerm+"&subject="+subjectName+"&classNameId="+className+"&sectionId="+section;
//                            successMsg(data.message);
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

        $(".cancel-btn").click(function (e) {
            e.preventDefault();
            window.location.href = "${g.createLink(controller: 'lesson',action: 'lessonPlan', params: [id:section?.id])}";
        });


        $('.allSectionCheckbox').click(function(){
            if(this.checked){
                $('#allSectionHolder').hide();
                $('#lessonDateDiv').show();
                $('#lessonDateLbl').show();
            }else{
                $('#allSectionHolder').show();
                $('#lessonDateDiv').hide();
                $('#lessonDateLbl').hide();
            }
        });
    });

</script>
</body>
</html>