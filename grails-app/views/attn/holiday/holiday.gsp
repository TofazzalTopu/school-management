<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Holiday</title>
    <meta name="layout" content="moduleAttandanceLayout"/>
</head>

<body>

<grailslab:breadCrumbActions breadCrumbTitleText="Holiday, Event and Program" SHOW_CREATE_BTN="YES" createButtonText="Add New Holiday"/>

<grailslab:fullModal modalLabel="Holiday, Event and Program">
                    <div class="col-md-12">
                    <form class="form-inline" role="form" id="create-form">
                        <g:hiddenField name="id"/>
                        <div class="form-group">
                            <label class="sr-only" for="name">Name</label>
                            <input type="text" class="form-control" id="name" name="name" placeholder="Enter Title" required>
                        </div>
                        <div class="form-group">
                            <g:select class="form-control" id="eventType" name='eventType'
                                      from='${com.grailslab.enums.EventType.workingHolidayType()}'
                                      optionKey="key" optionValue="value"></g:select>
                        </div>
                        <div class="form-group" id="data_range">
                            <div class="input-daterange input-group" id="datepicker">
                                <span class="input-group-addon">On</span>
                                <g:textField class="input-sm form-control" id="startDate" name="startDate"
                                             tabindex="2" placeholder="Start Date" required="required"/>
                                <span class="input-group-addon">to</span>
                                <g:textField class="input-sm form-control" id="endDate" name="endDate"
                                             tabindex="3" placeholder="End Date" required="required"/>
                            </div>
                        </div>
                    </form>
                    </div>
</grailslab:fullModal>

<grailslab:dataTable dataSet="${dataReturn}" tableHead="Serial,Name,Type,Start Date,End Date" actions="fa fa-pencil-square-o, fa fa-trash"></grailslab:dataTable>

<script>
    jQuery(function ($) {
        var validator = $('#modalForm').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                name: {
                    required: true,
                    maxlength: 200
                }
            },
            errorPlacement: function (error, element) {
            },
            highlight: function (e) {
                $(e).closest('.form-group').removeClass('has-info').addClass('has-error');
            },
            success: function (e) {
                $(e).closest('.form-group').removeClass('has-error').addClass('has-info');
                $(e).remove();

            },
            submitHandler: function (form) {
                $('#myModal .create-content .modal-footer-action-btn').hide();
                $('#myModal .create-content .modal-refresh-processing').show();
                $.ajax({
                    url: "${createLink(controller: 'holiday', action: 'save')}",
                    type: 'post',
                    dataType: "json",
                    data: $("#modalForm").serialize(),
                    success: function (data) {
                        formSuccess(data);
                    },
                    failure: function (data) {
                    }
                })
            }
        });

        $('#list-table').dataTable({
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "deferLoading": ${totalCount?:0},
            "sAjaxSource": "${g.createLink(controller: 'holiday',action: 'list')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(5)', nRow).html(getActionButtons(nRow, aData));
                return nRow;
            },
            "aoColumns": [
                null,
                null,
                null,
                null,
                null,
                {"bSortable": false}
            ]
        });

        $('.create-new-btn').click(function (e) {
            $('#myModal .create-success').hide();
            $('#myModal .create-content').show();
            validator.resetForm();
            $("#hiddenId").val('');
            $('#myModal').modal('show');
            e.preventDefault();
        });

        $(".cancel-btn").click(function () {
            var validator = $("#modalForm").validate();
            validator.resetForm();
            $("#hiddenId").val('');
            $('#myModal').modal('hide');
        });

        $('#list-table').on('click', 'a.reference-1', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'holiday',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        var startDate=data.obj.startDate;
                        var endDate=data.obj.endDate;
                        $('#hiddenId').val(data.obj.id);
                        $('#name').val(data.obj.name);
                        $('#startDate').datepicker('setDate', new Date(startDate));
                        $('#endDate').datepicker('setDate', new Date(endDate));
                        $('#eventType').val(data.obj.eventType.name);
                        $('#myModal .create-success').hide();
                        $('#myModal .create-content').show();
                        $('#myModal').modal('show');
                    } else {
                        alert(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            e.preventDefault();
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
                    url: "${g.createLink(controller: 'holiday',action: 'inactive')}?id=" + referenceId,
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
    $('#data_range .input-daterange').datepicker({
        keyboardNavigation: false,
        format: 'dd/mm/yyyy',
        forceParse: false,
        autoclose: true
    });
    function getActionButtons(nRow, aData) {
        return getActionBtnCustom(nRow, aData, 'fa fa-pencil-square-o, fa fa-trash');
    }
</script>
</body>
</html>


