<%@ page import="com.grailslab.gschoolcore.AcademicYear" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleExam&ResultLayout"/>
    <title>Manage EAS Mark</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="EAS Mark" SHOW_CREATE_BTN="YES" createButtonText="Manage EAS Entry"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                EAS Mark List
            </header>
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th>Serial</th>
                            <th>STD-ID</th>
                            <th>Name</th>
                            <th>Roll No</th>
                            <th>Mobile</th>
                            <th>EAS Mark</th>
                            <th>Section</th>
                            <th>Status</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </section>
    </div>
</div>


<script>
    var workingYear, academicYear, className, sectionName, printParam;
    jQuery(function ($) {

        var table = $('#list-table').dataTable({
            "sDom": "<'row'<'col-md-3 academicYear-filter-holder'><'col-md-3 className-filter-holder'><'col-md-3 section-filter-holder'><'col-md-3'f>r>t<'row'<'col-md-3'l><'col-md-3'i><'col-md-6'p>>",
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [2, 'desc'],
            "sAjaxSource": "${g.createLink(controller: 'eas',action: 'list')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                return nRow;
            },
            "fnServerParams": function (aoData) {
                aoData.push({"name": "sectionName", "value": $('#filterSection').val()},
                        {"name": "className", "value": $('#filterClassName').val()},
                        {"name": "academicYear", "value": $('#filterAcademicYear').val()});
            },
            "aoColumns": [
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                {"bSortable": false},
                {"bSortable": false}
            ]
        });
        $('#list-table_wrapper div.academicYear-filter-holder').html('<select id="filterAcademicYear" class="form-control" name="filterAcademicYear"><g:each in="${academicYearList}" var="academicYear"><option value="${academicYear.key}" ${academicYear.key==workingYear.key?'selected':''}>${academicYear.value}</option> </g:each></select>');
        $('#list-table_wrapper div.className-filter-holder').html('<select id="filterClassName" class="form-control" name="filterClassName"><option value="">All Class</option><g:each in="${classList}" var="className"><option value="${className.id}">${className.name}</option> </g:each></select>');
        $('#list-table_wrapper div.section-filter-holder').html('<select id="filterSection" class="form-control" name="filterSection"><option value="">All Section</option></select>');

        $('#filterAcademicYear').on('change', function (e) {
            $('#filterClassName').val("").trigger("change");
        });
        $('#filterClassName').on('change', function (e) {
            academicYear = $('#filterAcademicYear').val();
            className = $('#filterClassName').val();
            loadSectionOnClassChange(academicYear, className);
            $('#filterSection').val("").trigger("change");
        });

        $('#filterSection').on('change', function (e) {
            showLoading("#data-table-holder");
            $("#list-table").DataTable().draw(false);
            hideLoading("#data-table-holder");
        });
        $('.create-new-btn').click(function (e) {
            e.preventDefault();
            printParam = "${g.createLink(controller: 'eas',action: 'easEntry','_blank')}";
            window.open(printParam);
            return false;
        });

    });

    function loadSectionOnClassChange(academicYear, classNameOnchange) {
        var $filterSection = $('#filterSection');
            $filterSection.find('option').remove();
            $filterSection.append('<option value="">All Section</option>');
            if (classNameOnchange.length === 0) {
                return false
            }
        jQuery.ajax({
            type: 'POST',
            dataType: 'JSON',
            url: "${g.createLink(controller: 'remote',action: 'classSectionList')}?academicYear=" + academicYear+"&className="+classNameOnchange,
            success: function (data, textStatus) {
                if (data.isError == false) {
                    $.each(data.sectionList, function (key, value) {
                        $filterSection.append('<option value=' + value.id + '>' + value.name + '</option>');
                    });
                } else {
                    showErrorMsg(data.message);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            }
        });
    }
</script>
</body>
</html>
