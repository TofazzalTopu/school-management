<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Section  Attendance</title>
    <meta name="layout" content="adminLayout"/>/>
</head>
<body>
<grailslab:breadCrumbActions firstBreadCrumbUrl="${g.createLink(controller: 'exam',action: 'entry')}" firstBreadCrumbText="Mark Entry" breadCrumbTitleText="${section?.name} Attendance" SHOW_CREATE_BTN="YES" createButtonText="Add Attendance"/>
<grailslab:fullModal modalLabel="Section: ${section?.name}, Total Working Day :${section?.className?.workingDays}">
    <g:hiddenField name="section" id="section" value="${section?.id}"/>
    <grailslab:select name="student" id="student" from="${studentList}" noSelect="true"></grailslab:select>
    <grailslab:input name="markObtain" id="markObtain" label="Total Attendance" required="true"></grailslab:input>
</grailslab:fullModal>
<grailslab:dataTable dataSet="${dataReturn}"
                     tableHead="Roll No, Student Name, STD-ID, First Term Mark, Second Term Mark, Attendance, Percentage" actions="fa fa-pencil-square-o"></grailslab:dataTable>

<script>
    jQuery(function ($) {
        $('#student').select2();
        var validator = $('#modalForm').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                markObtain: {
                    required: true,
                    number: true,
                    min: 0,
                    max: 1200
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
                $.ajax({
                    url: "${createLink(controller: 'previousTerm', action: 'save')}",
                    type: 'post',
                    dataType: "json",
                    data: $("#modalForm").serialize(),
                    success: function (data) {
                        if (data.isError == true) {
                            showErrorMsg(data.message);
                            $('#markObtain').val('');
                        } else {
                            showSuccessMsg(data.message);
                            $("#student option:selected").remove();
                            $('#student').select2("destroy");
                            $('#student').select2().enable(true);
                            $('#list-table').DataTable().ajax.reload();
                            $('#markObtain').val('');
                            $('#id').val('');
                            $('#hiddenId').val('');
                        }
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
            "aaSorting": [0, 'desc'],
            "deferLoading":  ${totalCount?:0},
            "sServerMethod": "POST",
            "sAjaxSource": "${g.createLink(controller: 'previousTerm',action: 'listTermMark')}",
            "fnServerParams": function (aoData) {
                aoData.push({"name": "id", "value": ${section?.id}});
            },
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(7)', nRow).html(getActionButtons(nRow, aData));
                return nRow;
            },
            "aoColumns": [
                null,
                null,
                null,
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
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'previousTerm',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        $('#hiddenId').val(data.preId);
                        $('#student').append('<option value="' + data.stdId + '" selected="selected">' + data.studentID + '</option>');
                        $('#student').select2("destroy");
                        $('#student').select2().enable(false);
                        $('#markObtain').val(data.obtainMark);
                        $('#myModal').modal('show');
                    } else {
                        showErrorMsg(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            e.preventDefault();
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
            window.location.href = "${createLink(controller: 'exam', action: 'entry')}";
        });

    });

    function getActionButtons(nRow, aData) {
        return getActionBtnCustom(nRow, aData,'fa fa-pencil-square-o');
    }
</script>
</body>
</html>