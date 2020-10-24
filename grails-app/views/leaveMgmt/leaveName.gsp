<html>
<head>
    <title>Leave Entry</title>
    <meta name="layout" content="moduleLeaveManagementLayout"/>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Leave Entry" SHOW_CREATE_LINK="YES" createLinkText="Add New Leave Entry" createLinkUrl="${g.createLink(controller: 'leaveName', action: 'create')}"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
               Leave Name List
            </header>

            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th>Name</th>
                            <th>Leave Type</th>
                            <th>Number Of Day</th>
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
    jQuery(function ($) {
        var leaveNameTable = $('#list-table').DataTable({
           "bAutoWidth": true,
           "scrollX": false,
           "bServerSide": true,
           "iDisplayLength": 25,
           "aaSorting": [0, 'asc'],
            "sAjaxSource": "${g.createLink(controller: 'leaveName',action: 'list')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(3)', nRow).html(getGridActionBtns(nRow, aData, 'edit,delete'));
                return nRow;
            },
            "aoColumns": [
                null,
                null,
                null,
                {"bSortable": false}
            ]
        });
        $('#list-table').on('click', 'a.edit-reference', function (e) {
            e.preventDefault();
            var control = this;
            var referenceId = $(control).attr('referenceId');
            window.location.href = "${g.createLink(controller: 'leaveName',action: 'edit')}?id="+referenceId;
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
                    url: "${g.createLink(controller: 'leaveName',action: 'inactive')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            leaveNameTable.draw(false);
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
</script>
</body>
</html>