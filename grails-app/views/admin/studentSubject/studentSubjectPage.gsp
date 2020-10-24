<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Student Subject Mapping</title>
    <meta name="layout" content="moduleStdMgmtLayout"/>
</head>

<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Student Subject Mapping" SHOW_CREATE_BTN="YES" createButtonText="Add Common Mapping"/>
<div class="row" >
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Section List
            </header>
            <div class="panel-body">
                <table class="table table-striped table-hover table-bordered" id="list-table">
                    <thead>
                    <tr>
                        <th>Serial</th>
                        <th>Class Name</th>
                        <th>Section</th>
                        <th>Subjects</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <g:each in="${dataReturns}" var="dataSet" status="i">
                        <tr>
                            <td>${dataSet[0]}</td>
                            <td>${dataSet[1]}</td>
                            <td>${dataSet[2]}</td>
                            <td>${dataSet[3]}</td>
                            <td>
                                <span class="col-md-10 no-padding"><a href="${g.createLink(controller: 'studentSubjects', action: 'subjects', params: [id:dataSet.DT_RowId])}" referenceId="${dataSet.DT_RowId}"
                                                                     class="map-std-sub-reference" title="Maps Subject to Student"><span
                                            class="green glyphicon glyphicon-user"></span>Subject Mapping</a></span>
                            </td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
        </section>
    </div>
</div>
<script>
    var mappingHref, className;
    jQuery(function ($) {
        mappingHref = "${g.createLink(controller: 'studentSubjects',action: 'subjects')}/";
        var $table = $('#list-table').dataTable({
            "sDom": "<'row'<'col-md-12 className-filter-holder'>r>t<'row'<'col-md-3'l><'col-md-3'i><'col-md-6'p>>",
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'asc'],
            "deferLoading": ${totalCount},
            "sAjaxSource": "${g.createLink(controller: 'studentSubjects',action: 'list')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(4)', nRow).html(getActionButtons(nRow, aData));
                return nRow;
            },
            "fnServerParams": function (aoData) {
                aoData.push({"name": "className", "value": $('#filterClassName').val()});
            },
            "aoColumns": [
                {"bSortable": false},
                {"bSortable": false},
                {"bSortable": false},
                {"bSortable": false},
                {"bSortable": false}
            ]
        });
        $('#list-table_wrapper div.className-filter-holder').html('<select id="filterClassName" class="form-control" name="filterClassName"><option value="">All Class</option><g:each in="${classNameList}" var="className"><option value="${className.id}">${className.name}</option> </g:each></select>');
        $('#filterClassName').on('change', function (e) {
            showLoading("#data-table-holder");
            $table.DataTable().draw(false);
            hideLoading("#data-table-holder");
        });
        $('.create-new-btn').click(function (e) {
            e.preventDefault();
            className = $('#filterClassName').val();
            window.location.href = "${g.createLink(controller: 'studentSubjects',action: 'commonMapping')}/"+className;
        });

    });
    function getActionButtons(nRow, aData) {
        var actionButtons = "";
        actionButtons += '<span class="col-md-10 no-padding"><a href="' + mappingHref + aData.DT_RowId + '" class="map-std-sub-reference" title="Maps Subject to Student">';
        actionButtons += '<span class="green glyphicon glyphicon-user"></span>Subject Mapping';
        actionButtons += '</a></span>';
        return actionButtons;
    }
</script>
</body>
</html>


