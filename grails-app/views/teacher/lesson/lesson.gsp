<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleLessonAndFeedbackLayout"/>
    <title>Lesson <g:message code="top.header.brand" default="| GrailsLab"/></title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Class List"/>


<div class="row" >
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Lesson Plan List
            </header>
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                            <tr>
                                <th>Serial</th>
                                <th>Class</th>
                                <th>Section</th>
                                <th>Order</th>
                                <th>Section In Charge</th>
                                <th>Shift</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
            </div>
        </section>
    </div>
</div>
<script>
    var sectionTable, academicYear, className, lessonManageUrl, lessonCreateUrl;
    jQuery(function ($) {
        lessonCreateUrl = "${g.createLink(controller: 'lesson',action: 'create')}?sectionId=";
        lessonManageUrl = "${g.createLink(controller: 'lesson',action: 'lessonPlan')}?sectionId=";
        sectionTable = $('#list-table').DataTable({
            "sDom": "<'row'<'col-md-2 academicYear-filter-holder'><'col-md-2 className-filter-holder'><'col-md-8'f>r>t<'row'<'col-md-3'l><'col-md-3'i><'col-md-6'p>>",
            "bAutoWidth": true,
            "scrollX": false,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'asc'],
            "sAjaxSource": "${g.createLink(controller: 'lesson',action: 'list')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(6)', nRow).html(getActionButtons(nRow, aData));
                return nRow;
            },
            "fnServerParams": function (aoData) {
                aoData.push(
                    {"name": "className", "value": $('#filterClassName').val()},
                    {"name": "academicYear", "value": $('#filterAcademicYear').val()}
                );

            },
            "aoColumns": [
                null,
                null,
                null,
                null,
                null,
                null,
                { "bSortable": false }
            ]
        });
        $('#list-table_wrapper div.academicYear-filter-holder').html('<select id="filterAcademicYear" class="form-control" name="filterAcademicYear"><g:each in="${academicYearList}" var="academicYear"><option value="${academicYear.key}" ${academicYear.key==workingYear.key?'selected':''}>${academicYear.value}</option> </g:each></select>');
        $('#list-table_wrapper div.className-filter-holder').html('<select id="filterClassName" class="form-control" name="filterClassName"><option value="">All Class</option><g:each in="${classList}" var="className"><option value="${className.id}">${className.name}</option> </g:each></select>');

        $('#filterAcademicYear').on('change', function (e) {
            showLoading("#data-table-holder");
            $('#filterClassName').val("").trigger("change");
            sectionTable.draw(false);
            hideLoading("#data-table-holder");
        });

        $('#filterClassName').on('change', function (e) {
            academicYear = $('#filterAcademicYear').val();
            className = $('#filterClassName').val();
            sectionTable.draw(false);
        });
        function getActionButtons(nRow, aData) {
            var actionButtons = "";
            actionButtons += '<span class="col-md-3 no-padding"><a target="_blank" href="' + lessonCreateUrl + aData.DT_RowId + '" class="lesson-create-reference" title="Create Lesson Plan">';
            actionButtons += '<span class="fa fa-plus"></span>';
            actionButtons += '</a></span>';
            actionButtons += '<span class="col-md-3 no-padding"><a target="_blank" href="' + lessonManageUrl + aData.DT_RowId + '" class="lesson-manage-reference" title="Manage Lesson Plan">';
            actionButtons += '<span class="glyphicon glyphicon-tasks"></span>';
            actionButtons += '</a></span>';
            return actionButtons;
        }
    });

</script>
</body>
</html>
