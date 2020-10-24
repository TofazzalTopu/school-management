<%@ page import="com.grailslab.settings.ClassName; com.grailslab.CommonUtils" defaultCodec="none" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="layout" content="moduleOnlineAddmissionLayout"/>
    <title>Online Application</title>
</head>
<html>
<body>
<sec:ifAnyGranted roles="ROLE_SUPER_ADMIN,ROLE_ADMIN">
    <grailslab:breadCrumbActions breadCrumbTitleText="Applicants List" SHOW_CREATE_LINK="YES" createLinkText="New Registration" createLinkUrl="${g.createLink(controller: 'onlineRegistration', action: 'create')}"/>
</sec:ifAnyGranted>
<sec:ifNotGranted roles="ROLE_SUPER_ADMIN,ROLE_ADMIN">
    <grailslab:breadCrumbActions breadCrumbTitleText="Applicants List" />
</sec:ifNotGranted>

<div class="row">
    <div class="col-md-12">
        <section class="panel">
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th>SL</th>
                            <th>Image</th>
                            <th>Form No</th>
                            <th>Name</th>
                            <th>Class Name</th>
                            <th>Fathers Name</th>
                            <th>Mobile</th>
                            <th>Birth Date</th>
                            <th>Admit No</th>
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
    var editUrl,admitUrl;
    $(document).ready(function () {
        editUrl = "${g.createLink(controller: 'onlineRegistration', action: 'edit')}/";
        admitUrl = "${g.createLink(controller: 'onlineRegistrationReport', action: 'printAdmit')}?serialNo=";
        var applicantList = $('#list-table').DataTable({
            "sDom": "<'row'<'col-md-3 class-filter-holder dataTables_length'><'col-md-5 status-filter-holder dataTables_length'><'col-md-4'f>r>t<'row'<'col-md-4'l><'col-md-4'i><'col-md-4'p>>",
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "sAjaxSource": "${g.createLink(controller: 'onlineRegistration',action: 'list')}",

            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(9)', nRow).html(getActionButtons(nRow, aData));
                return nRow;
            },
            "fnServerParams": function (aoData) {
                aoData.push({"name": "className", "value": $('#classFilter').val()},{"name": "applicantStatus", "value": $('#statusFilter').val()});
            },
            "aoColumns": [
                null,
                {"bSortable": false},
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

        $('#list-table_wrapper div.class-filter-holder').html('<select id="classFilter" class="form-control" name="classFilter"><option value="">Select Class</option> <g:each in="${classNameList}" var="className"><option value="${className.id}">${className.name}</option> </g:each></select>');
        $('#list-table_wrapper div.status-filter-holder').html('<select id="statusFilter" class="form-control" name="statusFilter"><g:each in="${applicantStatusList}" var="applicantStatus"><option value="${applicantStatus.key}" ${applicantStatus.key == com.grailslab.enums.ApplicantStatus.Apply.key?"selected":""}>${applicantStatus.value}</option> </g:each></select>');

        $('#classFilter').on('change', function (e) {
            showLoading("#data-table-holder");
            applicantList.draw(true);
            hideLoading("#data-table-holder");
        });
        $('#statusFilter').on('change', function (e) {
            showLoading("#data-table-holder");
            applicantList.draw(true);
            hideLoading("#data-table-holder");
        });

        $('#list-table').on('click', 'a.delete-reference', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure delete this applicant?\n\n The action can't be undone");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'onlineRegistration', action: 'inactive')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            applicantList.draw(false);
                            showSuccessMsg(data.message);
                        } else {
                            showErrorMsg(data.message);
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            }
            e.preventDefault();                     //btn btn-primary create-new-btn
        });


        $('#list-table').on('click', 'a.select-reference', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure select for admission?\n\nApplicant will moved as selected list for admission");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'onlineRegistration', action: 'selectApplicant')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            applicantList.draw(false);
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
        $('#list-table').on('click', 'a.print-admit-reference', function (e) {
            e.preventDefault();
            var control = this;
            var referenceId = $(control).attr('referenceId');
            var  printParam = "${g.createLink(controller: 'onlineRegistrationReport',action: 'admitCard')}?regOnlineId="+referenceId;
            window.open(printParam);
            return false;
        })
        $('#list-table').on('click', 'a.remove-reference', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure remove application from admission list?\n\nApplicant will moved as admit card/exam given status");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'onlineRegistration', action: 'removeApplicant')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            applicantList.draw(false);
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
        var actionButtons = "";
        if (aData.applicantStatus === "Apply" || aData.applicantStatus === "Draft") {
            actionButtons += '<span class="col-md-3 no-padding"><a href="#" referenceId="' + aData.DT_RowId + '" class="delete-reference" title="Delete">';
            actionButtons += '<span class="fa fa-trash"></span>';
            actionButtons += '</a></span>';
            actionButtons += '<span class="col-md-3 no-padding"><a href="'+editUrl+aData.DT_RowId + '" class="edit-link" title="Edit">';
            actionButtons += '<span class="fa fa-pencil-square-o"></span>';
            actionButtons += '</a></span>';
            if (aData.hasRight === true) {
                actionButtons += '<span class="col-md-3 no-padding"><a href="#" referenceId="' + aData.DT_RowId + '" class="select-reference" title="Selected for admission">';
                actionButtons += '<span class="fa fa-check-square"></span>';
                actionButtons += '</a></span>';
            }
        } else if (aData.applicantStatus === "AdmitCard" || aData.applicantStatus === "GiveExam"){
            actionButtons += '<span class="col-md-3 no-padding"><a href="#" referenceId="' + aData.DT_RowId + '" class="delete-reference" title="Delete">';
            actionButtons += '<span class="fa fa-trash"></span>';
            actionButtons += '</a></span>';
            if (aData.hasRight === true) {
                actionButtons += '<span class="col-md-3 no-padding"><a href="#" referenceId="' + aData.DT_RowId + '" class="select-reference" title="Selected for admission">';
                actionButtons += '<span class="fa fa-check-square"></span>';
                actionButtons += '</a></span>';
            }
            actionButtons += '<span class="col-md-3 no-padding"><a href="#" referenceId="' + aData.DT_RowId + '" class="print-admit-reference" title="Print Admit Card">';
            actionButtons += '<span class="fa fa-print"></span>';
            actionButtons += '</a></span>';
        } else if(aData.applicantStatus === "Selected"){
            if (aData.hasRight === true) {
                actionButtons += '<span class="col-md-3 no-padding"><a href="#" referenceId="' + aData.DT_RowId + '" class="remove-reference" title="Remove from selected">';
                actionButtons += '<span class="fa fa-times"></span>';
                actionButtons += '</a></span>';
            }
            actionButtons += '<span class="col-md-3 no-padding"><a href="#" referenceId="' + aData.DT_RowId + '" class="print-admit-reference" title="Print Admit Card">';
            actionButtons += '<span class="fa fa-print"></span>';
            actionButtons += '</a></span>';
        }
        return actionButtons;
    };
</script>
</body>
</html>
