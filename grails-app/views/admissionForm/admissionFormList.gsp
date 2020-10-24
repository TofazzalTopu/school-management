<html>
<head>
    <title>Admission  list</title>
    <meta name="layout" content="moduleOnlineAddmissionLayout"/>
</head>

<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Manage Admission  List" SHOW_CREATE_LINK="YES" createLinkText="Add New" createLinkUrl="${g.createLink(controller:'admissionForm', action: 'create')}"/>

<g:if test='${flash.message}'>
    <div class="errorHandler alert alert-danger">
        <i class="fa fa-remove-sign"></i>
        ${flash.message}
    </div>
</g:if>
<grailslab:dataTable dataSet="${dataReturn}" tableHead="SL, Class Name, Apply Type, Start Date , Close Date , Birth Date Range" actions="fa fa-pencil-square-o, fa fa-trash"></grailslab:dataTable>
<script>
    var featureType;
    jQuery(function ($) {
        $('#list-table').DataTable({
            "sDom": "<'row'<'col-md-6'><'col-md-6'f>r>t<'row'<'col-md-4'l><'col-md-4'i><'col-md-4'p>>",
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "deferLoading": ${totalCount?:0},
            "sAjaxSource": "${g.createLink(controller: 'admissionForm',action: 'list')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(6)', nRow).html(getActionButtons(nRow, aData));
                return nRow;
            },
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
            window.location.href = "${g.createLink(controller: 'admissionForm',action: 'edit')}?id="+referenceId;
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
                    url: "${g.createLink(controller: 'admissionForm',action: 'delete')}?id=" + referenceId,
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
        return getActionBtnCustom(nRow, aData,'fa fa-pencil-square-o, fa fa-trash');
    }

</script>
</body>
</html>