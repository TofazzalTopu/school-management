<html>
<head>
    <title>Manage Registration  list</title>
    <meta name="layout" content="moduleWebLayout"/>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Manage Registration List" SHOW_PRINT_BTN="YES"/>
<div class="row" id="create-form-holder">
    <div class="col-sm-12">
        <div class="panel">
            <div class="panel-body">
                <div class="col-md-12" id="stu-manage-report-holder">
                    <div class="form-group">
                        <label for="fesProgram" class="col-md-2 control-label">Festival Name</label>
                        <div class="col-md-7">
                            <g:select class=" form-control " id="fesProgram" name='fesProgram'
                                      tabindex="1"
                                      from='${fesProgramList}' value="${fesProgram?.id}"
                                      optionKey="id" optionValue="name"></g:select>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>

<grailslab:dataTable dataSet="${dataReturn}" tableHead="SL, Name, Topics, SL NO, Institute Name, Class Name ,Roll No, Contact" actions="fa fa-trash"></grailslab:dataTable>
<script>
    var featureType ,printParam, name,topics,instituteName,className,rollNo,contact ;
    jQuery(function ($) {
        $('#list-table').dataTable({
            "sDom": "<'row'<'col-md-6'><'col-md-6'f>r>t<'row'<'col-md-4'l><'col-md-4'i><'col-md-4'p>>",
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "deferLoading": ${totalCount?:0},
            "sAjaxSource": "${g.createLink(controller: 'festival',action: 'fesRegList')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(8)', nRow).html(getActionButtons(nRow, aData));
                return nRow;
            },
            "aoColumns": [
                null,
                null,
                null,
                null,
                null,
                null,
                {"bSortable": false},
                {"bSortable": false},
                {"bSortable": false}
            ]
        });

        $('#list-table').on('click', 'a.reference-1', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure  want to Delete?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'festival',action: 'fesRegDelete')}?id=" + referenceId,
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
        $('.print-btn').click(function (e) {
            e.preventDefault();
            name = $('#name').val();
            className = $('#className').find("option:selected").val();
            instituteName = $('#instituteName').val();
            rollNo = $('#rollNo').val();
            contactNo = $('#contactNo').val();
            printParam = "${g.createLink(controller: 'studentReport',action: 'fesRegistration','_blank')}?name="+name+"&className="+className+"&instituteName="+instituteName+"&rollNo="+rollNo+"&contactNo="+contactNo;
            window.open(printParam);
            return false;

        });
    });

    function getActionButtons(nRow, aData) {
        return getActionBtnCustom(nRow, aData,'fa fa-trash');
    }

</script>
</body>
</html>