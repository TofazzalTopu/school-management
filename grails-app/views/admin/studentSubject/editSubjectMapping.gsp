<html>
<head>
    <title>Update Student Subject</title>
    <meta name="layout" content="moduleStdMgmtLayout"/>
</head>

<body>
<grailslab:breadCrumbActions firstBreadCrumbUrl="${g.createLink(controller: 'studentSubjects',action: 'subjects', params: [id:sectionId])}" firstBreadCrumbText="Student List" breadCrumbTitleText="Update Subject Mapping" SHOW_LINK_BTN="YES" linkBtnText="Back"/>

<div class="row" >
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                <span class="panel-header-title">Class: </span> <span class="panel-header-info">${className},</span><span class="panel-header-title">Section: </span> <span class="panel-header-info">${sectionName} </span><br/>
                <br/><span class="panel-header-title">Compulsory Subjects: </span><span class="panel-header-info">${compulsorySubjectStr}</span>
            </header>
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th>SL</th>
                            <th>Name</th>
                            <th>STD ID</th>
                            <th>Roll No</th>
                            <th>Choose Compulsory</th>
                            <th>Choose Optional</th>
                        </tr>
                        </thead>
                        <tbody>
                        <g:each in="${studentSubjectList}" var="stdSubject" status="i">
                            <tr>
                                <td>${i+1}</td>
                                <td>${stdSubject.studentName}</td>
                                <td>${stdSubject.studentid}</td>
                                <td>${stdSubject.rollNo}</td>
                                <td>
                                    <g:if test="${optComOptions}">
                                        <g:select multiple="true" value="${stdSubject.compSubjectIds}" name="compulsorySub" id="compulsorySub_${stdSubject.id}" isOptional="0" student="${stdSubject.id}" class="form-control subject-multi-select"
                                                  from="${optComOptions}" optionKey="id" optionValue="name"/>
                                    </g:if>
                                    <g:else>
                                        -
                                    </g:else>
                                </td>
                                <td>
                                    <g:if test="${allowOptionalSubject && optComOptions}">
                                        <g:select multiple="true" value="${stdSubject.optSubjectIds}" name="optionalSub" id="optionalSub_${stdSubject.id}" isOptional="1" student="${stdSubject.id}" class="form-control subject-multi-select"
                                                  from="${optComOptions}" optionKey="id" optionValue="name"/>
                                    </g:if>
                                    <g:else>
                                        -
                                    </g:else>
                                </td>
                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                </div>
            </div>
        </section>
    </div>
</div>

<!-- Modal -->

<script>
    var added, stdId, subjectId, isOptional, new_data, ctrlName;
    jQuery(function ($) {
        $(".subject-multi-select").select2({
            placeholder: "Select One or More Subject",
            allowClear: true
        });

        $(".subject-multi-select").on("change", function(e) {
            ctrlName = $(this).attr("id");
            isOptional = $(this).attr("isOptional");
            stdId = $(this).attr("student");
            if (e.added !== undefined && e.added != "") {
                added = 1;
                subjectId = e.added.id;
            } else {
                added = 0;
                subjectId = e.removed.id;
            }
            $.ajax({
                url: "${createLink(controller: 'studentSubjects', action: 'saveAlternative')}/"+stdId+"?added="+added+"&subjectId="+subjectId+"&isOptional="+isOptional,
                type: 'post',
                dataType: "json",
                success: function (data) {
                    if (data.isError === true) {
                    new_data = $.grep($('#'+ctrlName).select2('data'), function (value) {
                        return value['id'] != subjectId;
                    });
                    $('#'+ctrlName).select2('data', new_data);
                        showErrorMsg(data.message);
                    }
                },
                failure: function (data) {
                }
            });
        });

        $('.link-url-btn').click(function (e) {
            window.location.href = "${createLink(controller: 'studentSubjects', action: 'subjects')}/${sectionId}";
            e.preventDefault();
        });
    });
</script>
</body>
</html>


