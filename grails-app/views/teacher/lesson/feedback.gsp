<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleLessonAndFeedbackLayout"/>
    <title>All Feedback</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Manage Feedback"
                             SHOW_CREATE_LINK="YES"
                             createLinkText="New Feedback"
                             createLinkUrl="${g.createLink(controller: 'feedback', action: 'addFeedback')}" />

<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Feedback List
            </header>
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th>SL</th>
                            <th>Class</th>
                            <th>Section</th>
                            <th>Week No</th>
                            <th>STD-ID</th>
                            <th>Name</th>
                            <th>Roll No</th>
                            <th>Subject Added</th>
                            <th>Avg. Rating</th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </section>
    </div>
</div>




</div>
<script>
    var  subject, weekNo, className, sectionName, classUrl, sectionListUrl,feedbackTable;
    $(function ($) {
        $('.kv-ltr-theme-fa-star').rating({
            hoverOnClear: false,
            theme: 'krajee-fa'
        });
        feedbackTable= $('#list-table').DataTable({
            "sDom": "<'row'<'col-md-3 className-filter-holder'><'col-md-3 section-filter-holder'><'col-md-3 week-filter-holder'><'col-md-3'f>r>t<'row'<'col-md-3'l><'col-md-3'i><'col-md-6'p>>",
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'desc'],
            "sAjaxSource": "${g.createLink(controller: 'feedback',action: 'list')}",
            "fnServerParams": function (aoData) {
                aoData.push({"name": "className", "value": $('#filterClassName').val()},
                    {"name": "sectionName", "value": $('#filterSection').val()},
                    {"name": "weekNo", "value": $('#filterWeeks').val()});
            },
            "aoColumns": [
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            ]
        });

        $('#list-table_wrapper div.className-filter-holder').html('<select id="filterClassName" class="form-control" ' +
            'name="filterClassName"><option value="">All Class</option><g:each in="${classNameList}" var="className">'+
            '<option value="${className.id}">${className.name}</option> </g:each></select>');
        $('#list-table_wrapper div.section-filter-holder').html('<select id="filterSection" class="form-control" name="filterSection"><option value="">All Section</option></select>');
        $('#list-table_wrapper div.week-filter-holder').html('<select id="filterWeeks" class="form-control" name="filterWeeks"><option value="">All Weeks</option></select>');

        $('#filterClassName').on('change', function (e) {
            className =$('#filterClassName').val();
            if (className) {
                classUrl = "${g.createLink(controller: 'remote',action: 'listSection')}?className="+className;
                loadClassSection(classUrl, '#filterSection', "#stu-manage-report-holder")
            }
            $('#filterSection').val("").trigger("change");
        });

        $('#filterSection').on('change', function (e) {
            sectionName = $('#filterSection').val();
            if(className!="" && sectionName!=""){
                sectionListUrl = "${g.createLink(controller: 'remote',action: 'lessonWeekList')}?id="+sectionName+"&className="+className;
                loadSubjectWeek(sectionListUrl, $('#filterWeeks'), "#stu-manage-report-holder");
            }
            $('#filterWeeks').val("").trigger("change");
        });
        $('#filterWeeks').on('change', function (e) {
            showLoading("#list-table");
            feedbackTable.draw(false);
            hideLoading("#list-table");
        });
    });
</script>

</body>
</html>