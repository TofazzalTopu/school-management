<%@ page import="com.grailslab.gschoolcore.AcademicYear; com.grailslab.enums.TcType" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleStdMgmtLayout"/>
    <title>TC or Dropout</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Manage TC/Dropout" SHOW_CREATE_BTN="YES" createButtonText="New TC/Dropout"/>
<div class="row" id="tc-datalist-holder">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                TC/Dropout List
            </header>
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th>Serial</th>
                            <th>Student ID</th>
                            <th>Student name</th>
                            <th>Last Section</th>
                            <th>TC Type</th>
                            <th >Release Date</th>
                            <th >reason</th>
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
                                <td>${dataSet[4]}</td>
                                <td>${dataSet[5]}</td>
                                <td>${dataSet[6]}</td>
                                <td>
                                        <span class="col-md-3 no-padding"><a href="${g.createLink(controller: 'student',action: 'transferCertificate', id:dataSet.DT_RowId)}" referenceId="${dataSet.DT_RowId}"
                                                                             class="print-tc-reference" title="Add/Modify Transfer Certificate"><span
                                                    class="green glyphicon glyphicon-education"></span></a></span>
                                    <span class="col-md-3 no-padding"><a href="${g.createLink(controller: 'studentReport',action: 'printTransferCertificate', id:dataSet.DT_RowId)}" target="_blank" referenceId="${dataSet.DT_RowId}"
                                                                             class="print-tc-reference" title="Print Transfer Certificate"><span
                                                    class="green glyphicon glyphicon-print"></span></a></span>
                                        <span class="col-md-3 no-padding"><a href="" referenceId="${dataSet.DT_RowId}"
                                                                             class="delete-reference"
                                                                             title="Delete"><span
                                                    class="green glyphicon glyphicon-trash"></span></a></span>
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
                <h4 class="modal-title" id="myModalLabel">TC & Dropout</h4>
            </div>
            <div class="mainModal-confirm publish-result-content">
                <form class="form-horizontal" role="form" id="createFormModal">
                    <g:hiddenField name="id"/>
                    <g:hiddenField name="academicYear" id="academicYear" value="${workingYear.key}"/>
                    <div class="modal-body">
                        <div class="row">
                            <div class="form-group">
                                <label for="student" class="col-md-3 control-label">Student Name</label>
                                <div class="col-md-8">
                                        <input type="hidden" class="form-control" id="student" name="student" tabindex="5" />
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="tcType" class="col-md-3 control-label">TC Type *</label>
                                <div class="col-md-8">
                                    <g:select tabindex="1" class="form-control"
                                              id="tcType" name='tcType'
                                              from='${com.grailslab.enums.TcType.values()}'
                                              optionKey="key" optionValue="value"></g:select>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="releaseDate" class="col-md-3 control-label">Release Date</label>
                                <div class="col-md-8">
                                    <input class="form-control" type="text" name="releaseDate" id="releaseDate" placeholder="<g:formatDate date="${new java.util.Date()}" format="dd/MM/yyyy" />"
                                           tabindex="2"/>
                                </div>
                            </div>

                            <div class="form-group">
                                <label for="reason" class="col-md-3 control-label">Reason</label>
                                <div class="col-md-8">
                                    <textarea id="reason" name="reason" class="form-control" required="required" placeholder="Transfer Reason"
                                              tabindex="3"></textarea>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="schoolName" class="col-md-3 control-label">School Name</label>
                                <div class="col-md-8">
                                    <input type="text" id="schoolName" name="schoolName" class="form-control" placeholder="School where admit next"
                                           tabindex="3"/>
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
            <div class="mainModal-success publish-result-content" style="display: none;">
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-2">
                            <asset:image src="share-modal-icon.jpg" width="60" height="60"/>
                        </div>
                        <div class="col-md-10">
                            <p class="message-content"></p>
                        </div>
                    </div>
                </div>
                <div class="modal-footer modal-footer-action-btns">
                    <button type="button" class="btn btn-default" data-dismiss="modal" aria-hidden="true">Close</button>
                </div>
            </div>
        </div>
    </div>
</div>
<script>
    jQuery(function ($) {
        $('#releaseDate').datepicker({
            format: 'dd/mm/yyyy',
            autoclose: true
        });
        $('#student').select2({
            placeholder: "Search for a Student [studentId/name/Father Name/mobile",
            allowClear: true,
            minimumInputLength:3,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: "${g.createLink(controller: 'remote',action: 'studentTypeAheadList')}",
                dataType: 'json',
                quietMillis: 250,
                data: function (term, page) {
                    return {
                        q: term, // search term
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
        $('.create-new-btn').click(function (e) {
            $('#mainModal .mainModal-success').hide();
            $('#mainModal .mainModal-confirm').show();
            $('#mainModal').modal('show');
            e.preventDefault();
        });

        $('#dismiss').click(function (e) {
            $('#student').select2("val", "");
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
                url: "${g.createLink(controller: 'student', action: 'saveTcOrDropOut')}?",
                success: function (data) {
                    $('#student').select2("val", "");
//                    clearForm("#createFormModal");
                    $("#reason").val("");
                    $("#schoolName").val("");
                    $('#mainModal .modal-refresh-processing').hide();
                    $('#mainModal .modal-footer-action-btns').show();
                    if(data.isError==true){
                        $('div.mainModal-success p.message-content').html(data.message);
                    }else {
                        $('div.mainModal-success p.message-content').html(data.message);
                        var table = $('#list-table').DataTable().ajax.reload();
                    }
                    $('#mainModal .mainModal-confirm').hide(1000);
                    $('#mainModal .mainModal-success').show(1000);
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            return false; // avoid to execute the actual submit of the form.
        });

        $('#list-table').dataTable({
            "sDom": "<'row'<'col-md-3 academicYear-filter-holder'><'col-md-3 tcType-filter-holder'><'col-md-6'f>r>t<'row'<'col-md-3'l><'col-md-3'i><'col-md-6'p>>",
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'desc'],
            "deferLoading": ${totalCount?:0},
            "sAjaxSource": "${g.createLink(controller: 'student',action: 'listTcOrDropOut')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(7)', nRow).html(getActionButtons(nRow, aData));
                return nRow;
            },
            "fnServerParams": function (aoData) {
                aoData.push({ "name": "academicYear", "value": $('#academicYear-filter').val()},{ "name": "tcType",  "value": $('#tcType-filter').val()});
            },
            "aoColumns": [
                null,
                null,
                { "bSortable": false },
                { "bSortable": false },
                { "bSortable": false },
                { "bSortable": false },
                { "bSortable": false },
                { "bSortable": false }
            ]
        });
        $('#list-table_wrapper div.academicYear-filter-holder').html('<select id="academicYear-filter" class="form-control" name="academicYear-filter"><g:each in="${academicYearList}" var="academicYear"><option value="${academicYear.key}" ${academicYear.key==workingYear.key?'selected':''}>${academicYear.value}</option> </g:each></select>');
        $('#list-table_wrapper div.tcType-filter-holder').html('<select id="tcType-filter" class="form-control" name="tcType-filter"><option value="">All ..</option><g:each in="${TcType.values()}" var="tctype"><option value="${tctype.key}">${tctype.value}</option> </g:each></select>');


        $('#tcType-filter').on('change', function (e) {
            showLoading("#data-table-holder");
            $("#list-table").DataTable().draw(false);
            hideLoading("#data-table-holder");
        });
        $('#academicYear-filter').on('change', function (e) {
            $('#tcType-filter').val("").trigger("change");
        });

        $('#list-table').on('click', 'a.edit-reference', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'tcAndDropOut',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        var releaseDate = data.obj.releaseDate;
                        clearForm('#create-form');
                        $('#dropOutId').val(data.obj.id);
                        $('#releaseDate').datepicker('setDate', new Date(releaseDate));
                        $('#reason').val(data.obj.reason);
                        $('#student').val(data.obj.student.id);
                        $('#student').select2("destroy");
                        $('#student').select2().enable(false);
                        $('#mainModal .mainModal-success').hide();
                        $('#mainModal .mainModal-confirm').show();
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

        $('#list-table').on('click', 'a.delete-reference', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'student',action: 'deleteTc')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            $("#list-table").DataTable().row(selectRow).remove().draw(false);
                            showSuccessMsg(data.message);
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
    });
    function repoFormatResult(repo) {
        var markup = '<div class="row-fluid">' +
                '<div class="span12">' + repo.name + '</div>' +
                '</div>';
        return markup;
    }

    function repoFormatSelection(repo) {
        return repo.name;
    }
    function getActionButtons(nRow, aData) {
        var tcUrl = "${g.createLink(controller: 'student',action: 'transferCertificate')}/"+aData.DT_RowId;
        var tcPrintUrl = "${g.createLink(controller: 'studentReport',action: 'printTransferCertificate')}?id="+aData.DT_RowId;
        var actionButtons = "";
        actionButtons += '<span class="col-md-3 no-padding"><a href="'+tcUrl+'" referenceId="' + aData.DT_RowId + '" class="print-tc-reference" title="Add/Modify Transfer Certificate">';
        actionButtons += '<span class="green glyphicon glyphicon-education"></span>';
        actionButtons += '</a></span>';
        actionButtons += '<span class="col-md-3 no-padding"><a target="_blank" href="'+tcPrintUrl+'" referenceId="' + aData.DT_RowId + '" class="print-tc-reference" title="Print Transfer Certificate">';
        actionButtons += '<span class="green glyphicon glyphicon-print"></span>';
        actionButtons += '</a></span>';
        actionButtons += '<span class="col-md-3 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="delete-reference" title="Delete">';
        actionButtons += '<span class="red glyphicon glyphicon-trash"></span>';
        actionButtons += '</a></span>';
        return actionButtons;
    }

</script>
</body>
</html>
