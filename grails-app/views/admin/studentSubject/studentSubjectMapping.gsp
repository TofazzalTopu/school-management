<html>
<head>
    <title>Student subject</title>
    <meta name="layout" content="moduleStdMgmtLayout"/>
</head>

<body>
<grailslab:breadCrumbActions firstBreadCrumbUrl="${g.createLink(controller: 'studentSubjects',action: 'index')}" firstBreadCrumbText="Section List" breadCrumbTitleText="${sectionName} Students" SHOW_CREATE_LINK="YES" createLinkText="Manage All" createLinkUrl="${g.createLink(controller: 'studentSubjects', action: 'edit', id:sectionId)}" SHOW_PRINT_BTN="YES"/>
<div class="row" >
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                <span class="panel-header-title">Class: </span> <span class="panel-header-info">${className},</span><span class="panel-header-title">Section: </span> <span class="panel-header-info">${sectionName} </span><br/>
                <span class="panel-header-title">Compulsory Subjects: </span><span class="panel-header-info">${compulsorySubjectStr}</span>
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
                            <th>Selected Compulsory Subject(s)</th>
                            <th>Selected Optional Subject(s)</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        <g:each in="${dataReturn}" var="dataSet" status="i">
                            <tr>
                                <td>${i+1}</td>
                                <td>${dataSet.studentName}</td>
                                <td>${dataSet.studentid}</td>
                                <td>${dataSet.rollNo}</td>
                                <td>${dataSet.compSubjects}</td>
                                <td>${dataSet.optSubjects}</td>
                                <td>
                                    <span class="col-md-12 no-padding"><a href="" referenceId="${dataSet.id}"
                                                                         class="edit-reference" title="Modify Student Subjects"><span
                                                class="green glyphicon glyphicon-edit"></span></a></span>
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
    var subjectId, fullMark,ctEffMark,hallEffMark,className, sectionId, printParam;
    jQuery(function ($) {

        $('#list-table').dataTable({
            "bAutoWidth": true,
            "iDisplayLength": 25,
            "aaSorting": [0,'asc'],
            "aoColumns": [
                null,
                null,
                null,
                { "bSortable": false },
                { "bSortable": false },
                { "bSortable": false },
                { "bSortable": false }
            ]
        });

        $('#list-table').on('click', 'a.edit-reference', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            window.location.href = "${createLink(controller: 'studentSubjects', action: 'edit')}/${sectionId}?studentId=" + referenceId;
            e.preventDefault();
        });

        $('.print-btn').click(function (e) {
            e.preventDefault();
            sectionId = "${sectionId}";
            printParam = "${g.createLink(controller: 'studentReport',action: 'studentSubject','_blank')}/"+sectionId;
            window.open(printParam);
            return false;
        });
    });
    function getActionButtons(nRow, aData) {
        var actionButtons = "";
        actionButtons += '<span class="col-md-12 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="edit-reference" title="Modify Student Subjects">';
        actionButtons += '<span class="green glyphicon glyphicon-edit">';
        actionButtons += '</a></span>';
        return actionButtons;
    }

</script>
</body>
</html>


