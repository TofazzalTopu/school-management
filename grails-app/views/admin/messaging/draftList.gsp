<%@ page import="com.grailslab.enums.SelectionTypes" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleSmsLayout"/>
    <title>Messaging</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Draft Message List" SHOW_CREATE_LINK="YES" createLinkText="New Draft/Message" createLinkUrl="${g.createLink(controller: 'messaging',action: 'draftSms')}"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Draft Messages
            </header>
            <grailslab:dataTable dataSet="${dataReturn}" tableHead="SL, Name, Message, NumOfSms" actions="glyphicon glyphicon-circle-arrow-right, fa fa-retweet, fa fa-trash"></grailslab:dataTable>
        </section>
    </div>
</div>
<div class="modal fade" id="draftSmsSendModal"  tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <g:form class="form-horizontal" role="form" method="POST" controller="messaging" action="step1">
                <g:hiddenField name="draftSmsId"/>
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                    <h4 class="modal-title" id="myModalLabel">Selection Type</h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="form-group">
                            <div class="col-md-offset-1 col-md-10">
                                <g:each in="${com.grailslab.enums.SelectionTypes.values()}" var="type">
                                    <label>
                                        <input id="${type.key}" type="radio" name="selectionType"
                                               value="${type.key}">
                                        ${type.value}
                                    </label>
                                </g:each>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button class="btn  btn-default" data-dismiss="modal" aria-hidden="true">Cancel</button>
                    <button class="btn btn-primary btn-submit" type="submit" id="submitButton">Next</button>
                </div>
            </g:form>
        </div>
    </div>
</div>
<script>

    jQuery(function ($) {

        $('#list-table').dataTable({
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "order": [[ 0,"desc"]],
            "deferLoading": ${totalCount?:0},
            "sAjaxSource": "${g.createLink(controller: 'messaging',action: 'draftList')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(4)', nRow).html(getActionButtons(nRow, aData));
                return nRow;
            },
            "aoColumns": [
                null,
                null,
                null,
                null,
                {"bSortable": false}
            ]
        });

        $('#list-table').on('click', 'a.reference-1', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            $('#draftSmsSendModal').modal('show');
            $('#draftSmsId').val(referenceId);
            e.preventDefault();
        });
        $('#list-table').on('click', 'a.reference-2', function (e) {
            e.preventDefault();
            var control = this;
            var referenceId = $(control).attr('referenceId');
            window.location.href = "${g.createLink(controller: 'messaging',action: 'edit')}?id="+referenceId;
        });

        $('#list-table').on('click', 'a.reference-3', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'messaging',action: 'delete')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            showSuccessMsg(data.message);
                            $("#list-table").DataTable().row(selectRow).remove().draw(false);
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

    function getActionButtons(nRow, aData) {
        return getActionBtnCustom(nRow, aData,'glyphicon glyphicon-circle-arrow-right, fa fa-retweet, fa fa-trash');
    }
</script>
</body>
</html>