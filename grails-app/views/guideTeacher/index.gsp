<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleStdMgmtLayout"/>
    <title>Guide Teacher</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Guide Teacher" SHOW_CREATE_BTN="YES" createButtonText="Add new" SHOW_PRINT_BTN="YES" printBtnText="Report"/>
<div class="row" id="print-form-holder" style="display: none;">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Guide Teacher Report
            </header>
            <div class="panel-body">
                <div class="row">
                    <div class="form-horizontal" role="form">
                        <div class="form-group">
                            <label for="empGuideCategoryId" class="col-md-2 control-label">Category/Shift Name</label>
                            <div class="col-md-6">
                                <g:select name="empGuideCategoryId" id="empGuideCategoryId" class="form-control"
                                          from="${hrCategoryList}"
                                          noSelection="${['': 'All']}" optionKey="id"
                                          optionValue="name"/>

                            </div>
                        </div>

                        <div class="form-group">
                            <label for="empGuideEmployeeId" class="col-md-2 control-label" >Teacher Name </label>
                            <div class="col-md-6">
                                <input type="hidden" class="form-control" id="empGuideEmployeeId" name="empGuideEmployeeId" tabindex="1"/>
                            </div>
                        </div>


                        <div class="form-group">
                            <label for="empGuidePrintOptionType" class="col-md-2 control-label">Report Type</label>
                            <div class="col-md-4">
                                <g:select class="form-control" id="empGuidePrintOptionType" name='empGuidePrintOptionType'
                                          from='${com.grailslab.enums.PrintOptionType.values()}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <button type="button" id="empGuideReportBtn" class="btn btn-primary">Show Report</button>
                            </div>
                        </div>

                    </div>
                </div>
            </div>
        </section>
    </div>
</div>
<div class="row" id="tc-datalist-holder">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Guide Teacher
            </header>
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th>Serial</th>
                            <th>Teacher ID</th>
                            <th>Name</th>
                            <th>Student IDS</th>
                            <th >Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        <g:each in="${dataReturn}" var="dataSet" status="i">
                            <tr>
                                <td>${dataSet[0]}</td>
                                <td>${dataSet[1]}</td>
                                <td>${dataSet[2]}</td>
                                <td>${dataSet[3]}</td>
                                <td>
                                    <span class="col-md-6 no-padding"><a href="" referenceId="${dataSet.DT_RowId}"
                                                                         class="edit-reference" title="Edit"><span
                                                class="green glyphicon glyphicon-edit"></span></a></span>
                                    <span class="col-md-3 no-padding"><a href="${g.createLink(controller: 'guideTeacher',action: 'printStudentList', id:dataSet.employeeId)}" target="_blank" referenceId="${dataSet.DT_RowId}"
                                                                             class="print-tc-reference" title="Print guide student List"><span
                                                    class="green glyphicon glyphicon-print"></span></a></span>
                                        %{--<span class="col-md-3 no-padding"><a href="" referenceId="${dataSet.DT_RowId}"
                                                                             class="delete-reference"
                                                                             title="Delete"><span
                                                    class="green glyphicon glyphicon-trash"></span></a></span>--}%
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
<div class="modal fade" id="mainModal"  tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="myModalLabel">Guide Teacher</h4>
            </div>
            <div class="mainModal-confirm publish-result-content">
                <form class="form-horizontal" role="form" id="createFormModal">
                    <g:hiddenField name="id" id="guideTeacherId"/>
                    <div class="modal-body">
                        <div class="row">
                            <div class="form-group">
                                <label for="employeeId" class="col-md-2 control-label">Teacher  <span class="required"></span></label>
                                <div class="col-md-8">
                                    <input type="hidden" class="form-control"  id="employeeId" name="employee" tabindex="2" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="studentIds" class="col-md-2 control-label">Student IDs</label>
                                <div class="col-md-8">
                                    <textarea id="studentIds" rows="10" cols="10" name="studentIds" class="form-control" required="required" placeholder="Student IDs (Comma separated)"
                                              tabindex="3"></textarea>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div class="modal-footer modal-footer-action-btns">
                        <button type="button" class="btn btn-default" id="dismiss" data-dismiss="modal" aria-hidden="true">Cancel</button>
                        <button type="submit" id="publish-result-yes-btn" class="btn btn-large btn-primary">Submit</button>
                    </div>
                    <div class="modal-footer modal-refresh-processing" style="display: none;">
                        <i class="fa fa-refresh fa-spin text-center"></i>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>
<script>
    jQuery(function ($) {
        $('#employeeId').select2({
            placeholder: "Search for [employeeId/name]",
            allowClear: true,
            minimumInputLength:3,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: "${g.createLink(controller: 'remote',action: 'employeeList')}",
                dataType: 'json',
                quietMillis: 250,
                data: function (term, page) {
                    return {
                        q: term
                    };
                },
                results: function (data, page) { // parse the results into the format expected by Select2.
                    // since we are using custom formatting functions we do not need to alter the remote JSON data
                    return { results: data.items };
                },
                cache: true
            },
            formatResult: repoFormatResult, // omitted for brevity, see the source of this page
            formatSelection: repoFormatSelection,  // omitted for brevity, see the source of this page
            dropdownCssClass: "bigdrop", // apply css that makes the dropdown taller
            escapeMarkup: function (m) { return m; } // we do not want to escape markup since we are displaying html in results
        });
        $('#empGuideEmployeeId').select2({
            placeholder: "Search for [employeeId/name]",
            allowClear: true,
            minimumInputLength:3,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: "${g.createLink(controller: 'remote',action: 'employeeList')}",
                dataType: 'json',
                quietMillis: 250,
                data: function (term, page) {
                    return {
                        q: term
                    };
                },
                results: function (data, page) { // parse the results into the format expected by Select2.
                    // since we are using custom formatting functions we do not need to alter the remote JSON data
                    return { results: data.items };
                },
                cache: true
            },
            formatResult: repoFormatResult, // omitted for brevity, see the source of this page
            formatSelection: repoFormatSelection,  // omitted for brevity, see the source of this page
            dropdownCssClass: "bigdrop", // apply css that makes the dropdown taller
            escapeMarkup: function (m) { return m; } // we do not want to escape markup since we are displaying html in results
        });

        $('#list-table').dataTable({
            "sDom": "<'row'<'col-md-6 academicYear-filter-holder'><'col-md-6'f>r>t<'row'<'col-md-3'l><'col-md-3'i><'col-md-6'p>>",
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'desc'],
            "deferLoading": ${totalCount?:0},
            "sAjaxSource": "${g.createLink(controller: 'guideTeacher',action: 'list')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(4)', nRow).html(getActionButtons(nRow, aData));
                return nRow;
            },
            "fnServerParams": function (aoData) {
                aoData.push({ "name": "academicYear", "value": $('#academicYear-filter').val()});
            },
            "aoColumns": [
                null,
                null,
                null,
                null,
                { "bSortable": false }
            ]
        });
        $('#list-table_wrapper div.academicYear-filter-holder').html('<select id="academicYear-filter" class="form-control" name="academicYear-filter"><g:each in="${academicYearList}" var="academicYear"><option value="${academicYear.key}" ${academicYear.key==workingYear.key?'selected':''}>${academicYear.value}</option> </g:each></select>');

        $('#list-table').on('click', 'a.edit-reference', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'guideTeacher',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        $('#guideTeacherId').val(data.obj.id);
                        $('#studentIds').val(data.obj.studentIds);
                        $("#employeeId").val(data.obj.employeeId);
                        $("#select2-chosen-1").empty().append(data.obj.name);
                        $("#s2id_employee").css("pointer-events","none");
                        $('#mainModal').modal('show');
                    } else {
                        showErrorMsg(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            e.preventDefault();
        });

        $('#academicYear-filter').on('change', function (e) {
            $('#tcType-filter').val("").trigger("change");
        });
        $('.create-new-btn').click(function (e) {
            $('#mainModal .mainModal-confirm').show();
            $('#mainModal').modal('show');
            e.preventDefault();
        });
        $('.print-btn').click(function (e) {
            $("#print-form-holder").toggle(500);
            e.preventDefault();
        });
        $('#dismiss').click(function (e) {
            $('#employeeId').select2("val", "");
            clearForm("#createFormModal");
            e.preventDefault();
        });

        $("#createFormModal").submit(function() {
            $('#mainModal .modal-footer-action-btns').hide();
            $('#mainModal .modal-refresh-processing').show();
            jQuery.ajax({
                type: 'POST',
                dataType:'JSON',
                data: $("#createFormModal").serialize(),
                url: "${g.createLink(controller: 'guideTeacher', action: 'saveOrUpdate')}?",
                success: function (data) {
                    $('#mainModal .modal-refresh-processing').hide();
                    $('#mainModal .modal-footer-action-btns').show();
                    if(data.isError==true){
                        showErrorMsg(data.message)
                    }else {
                        $('#guideTeacherId').val("");
                        $('#employeeId').select2("val", "");
                        $("#studentIds").val("");
                        $('#list-table').DataTable().ajax.reload();
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            return false; // avoid to execute the actual submit of the form.
        });
        $('#empGuideReportBtn').click(function (e) {
            e.preventDefault();
            var  hrCategoryId = $('#empGuideCategoryId').val();
            var  employeeId = $("#empGuideEmployeeId").val()
            var printOptionType = $('#empGuidePrintOptionType').val();
            var reportParam ="${g.createLink(controller: 'guideTeacher',action: 'printStudentList','_blank')}?hrCategory="+hrCategoryId+"&id="+employeeId+"&printOptionType="+printOptionType+"&academicYear=Y2020";
            window.open(reportParam);
            return false;
        });

    });

    function getActionButtons(nRow, aData) {
        var tcPrintUrl = "${g.createLink(controller: 'guideTeacher',action: 'printStudentList')}?id="+aData.employeeId+"&academicYear="+aData.academicYear;
        var actionButtons = "";
        actionButtons += '<span class="col-md-3 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="edit-reference" title="Edit">';
        actionButtons += '<span class="red glyphicon glyphicon-edit"></span>';
        actionButtons += '</a></span>';
        actionButtons += '<span class="col-md-3 no-padding"><a target="_blank" href="'+tcPrintUrl+'" referenceId="' + aData.DT_RowId + '" class="print-tc-reference" title="Print Transfer Certificate">';
        actionButtons += '<span class="green glyphicon glyphicon-print"></span>';
        actionButtons += '</a></span>';
        /*actionButtons += '<span class="col-md-3 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="delete-reference" title="Delete">';
        actionButtons += '<span class="red glyphicon glyphicon-trash"></span>';
        actionButtons += '</a></span>';*/
        return actionButtons;
    }

</script>
</body>
</html>
