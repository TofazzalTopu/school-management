<html>
<head>
    <title>TimeLine Content</title>
    <meta name="layout" content="moduleWebLayout"/>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="TimeLine Content" SHOW_CREATE_LINK="YES" createLinkText="Add New TimeLine" createLinkUrl="${g.createLink(controller: 'timeLine', action: 'create')}"/>
<g:if test='${flash.message}'>
    <div class="errorHandler alert alert-danger">
        <i class="fa fa-remove-sign"></i>
        ${flash.message}
    </div>
</g:if>
<grailslab:dataTable dataSet="${dataReturn}" tableHead="Order, Title, Name, Icon, Status, Description" actions="fa fa-pencil-square-o,fa fa-retweet, fa fa-trash"></grailslab:dataTable>
<script>
    jQuery(function ($) {
        $('#list-table').dataTable({
            "sDom": "<'row'<'col-md-6'><'col-md-6'f>r>t<'row'<'col-md-4'l><'col-md-4'i><'col-md-4'p>>",
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "deferLoading": ${totalCount?:0},
            "sAjaxSource": "${g.createLink(controller: 'timeLine',action: 'list')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(6)', nRow).html(getActionButtons(nRow, aData));
                return nRow;
            },
            "order": [[0, "desc"]],
            "aoColumns": [
                null,
                null,
                null,
                null,
                null,
                {"bSortable": false},
                {"bSortable": false}
            ]
        });
        $('#list-table').on('click', 'a.reference-1', function (e) {
            e.preventDefault();
            var control = this;
            var referenceId = $(control).attr('referenceId');
            window.location.href = "${g.createLink(controller: 'timeLine',action: 'edit')}/"+referenceId;
        });

        $('#list-table').on('click', 'a.reference-2', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'timeLine',action: 'inactive')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            $('#list-table').DataTable().ajax.reload();
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

        $('#list-table').on('click', 'a.reference-3', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure delete this content?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'timeLine',action: 'delete')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            $('#list-table').DataTable().ajax.reload();
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

    function getActionButtons(nRow, aData) {
        return getActionBtnCustom(nRow, aData,'fa fa-pencil-square-o,fa fa-retweet, fa fa-trash');
    }

</script>
</body>
</html>