<%@ page import="com.grailslab.enums.ExamType; com.grailslab.enums.AttendStatus; com.grailslab.enums.ScheduleStatus" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="adminLayout"/>
    <title>CT 2 Mark Entry</title>
</head>
<body>
<grailslab:breadCrumbActions firstBreadCrumbUrl="${g.createLink(controller: 'markEntry',action: 'index')}" firstBreadCrumbText="Mark Entry" breadCrumbTitleText="CT 2 Mark" SHOW_EXTRA_BTN1 = "YES" extraBtn1Text="Delete All" extraBtn1Icon = "fa fa-remove" SHOW_PRINT_BTN="YES" printBtnText="Print"/>
<div class="row" id="create-form-holder">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                <span class="panel-header-title">Class : </span> <span class="panel-header-info">${className},</span>   <span class="panel-header-title">Section : </span> <span class="panel-header-info">${section},</span><span class="panel-header-title">Term : </span><span class="panel-header-info">${examTerm},</span><span class="panel-header-title"> Subject: </span><span class="panel-header-info">${subject},</span><span class="panel-header-title">Full Mark : </span><span class="panel-header-info">${examSchedule.ctExamMark2} </span>
            </header>
            <div class="panel-body">
                <form class="form-horizontal" id="create-form">
                    <g:hiddenField name="id" />
                    <g:hiddenField name="examScheduleId" id="examScheduleId" value="${examSchedule?.id}" />
                    <g:hiddenField name="ctMarkFor" id="ctMarkFor" value="${ctMarkFor}" />
                    <div class="form-group col-md-5">
                        <div class="col-md-12">
                            <g:select class="form-control" id="studentId" name='studentId' tabindex="1"
                                      from='${studentList}'
                                      optionKey="id" optionValue="name" required=""></g:select>
                        </div>
                    </div>
                    <div class="form-group col-md-2">
                        <div class="col-md-12">
                            <g:select class="form-control" id="attendStatus" name='attendStatus' tabindex="2"
                                      from='${AttendStatus.values()}'
                                      optionKey="key" optionValue="value"></g:select>
                        </div>
                    </div>
                    <div class="form-group col-md-2">
                        <div class="col-md-12">
                            <input type="text" name="markObtain" class="form-control" tabindex="3" id="markObtain"/>
                        </div>
                    </div>
                    <div class="form-group col-md-2">
                        <div class="col-md-12">
                            <button id="ct-mark-submit-btn" class="btn btn-primary" tabindex="4" type="submit">Add Mark</button>
                        </div>
                    </div>
                </form>
            </div>
        </section>
    </div>
</div>

<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th class="col-md-2">STD-ID</th>
                            <th class="col-md-2">Name</th>
                            <th class="col-md-1">Roll</th>
                            <th class="col-md-2">CT Mark</th>
                            <th class="col-md-2">CT 2 Mark</th>
                            <th class="col-md-2">Subject</th>
                            <th class="col-md-1">Action</th>
                        </tr>
                        </thead>
                        <tbody>
                        <g:each in="${dataReturn}" var="dataSet">
                            <tr>
                                <td>${dataSet[0]}</td>
                                <td>${dataSet[1]}</td>
                                <td>${dataSet[2]}</td>
                                <td>${dataSet[3]}</td>
                                <td>${dataSet[4]}</td>
                                <td>${dataSet[5]}</td>
                                <td>
                                    <span class="col-md-4 no-padding"><a href="" referenceId="${dataSet.DT_RowId}" class="edit-reference" title="Edit"><span class="glyphicon glyphicon-edit"></span></a></span>
                                    <span class="col-md-4 no-padding"><a href="" referenceId="${dataSet.DT_RowId}" class="delete-reference" title="Delete"><span class="glyphicon glyphicon-trash"></span></a></span>
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


<script type="application/javascript">
    var numOfStudent, reloadUrl;
    jQuery(function ($) {
        $('.create-new-btn').click(function (e) {
            $("#markEntrySubjectSelectHolder").toggle(500);
            e.preventDefault();
        });

        reloadUrl = "${createLink(controller: 'markEntry', action: 'addCtMark',id: examSchedule.id)}?ctMarkFor=CLASS_TEST2";
        $('#attendStatus').on('change', function (e) {
            var attendStatus = $('#attendStatus').val();
            if(attendStatus != "${AttendStatus.PRESENT.key}"){
                $('#markObtain').val('0');
            } else{
                $('#markObtain').val('');
            }
        });

        var $markEntryTable = $('#list-table').dataTable({
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "deferLoading":  ${totalCount?:0},
            "sServerMethod": "POST",
            "sAjaxSource": "${g.createLink(controller: 'markEntry',action: 'listCtMark')}",
            "fnServerParams": function ( aoData ) {
                aoData.push( { "name": "examScheduleId", "value": ${examSchedule.id}}, { "name": "ctMarkFor", "value": "CLASS_TEST2"});
            },
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if(aData.DT_RowId ==undefined){
                    return true;
                }
                $('td:eq(6)', nRow).html(getActionButtons(nRow, aData));
                return nRow;
            },
            "order": [[ 2, "desc"]],
            "aoColumns": [
                null,
                null,
                null,
                null,
                null,
                { "bSortable": false },
                { "bSortable": false }
            ]
        });

        $('#create-form').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                markObtain: {
                    required: true,
                    number: true,
                    min:0
                }
            },
            highlight: function (e) {
                $(e).closest('.form-group').removeClass('has-info').addClass('has-error');
            },
            success: function (e) {
                $(e).closest('.form-group').removeClass('has-error').addClass('has-info');
                $(e).remove();
            },
            submitHandler: function (form) {
                showLoading("#create-form-holder");
                $.ajax({
                    url: "${createLink(controller: 'markEntry', action: 'saveCtMark')}",
                    type: 'post',
                    dataType: "json",
                    data: $("#create-form").serialize(),
                    success: function (data) {
                        if(data.isError==true){
                            $('#markObtain').val('');
                            showErrorMsg(data.message);
                        }else {
                            $('#studentId :selected').remove();
                            $('#attendStatus').val("PRESENT");
                            $('#markObtain').val('');
                            $('#id').val('');
                            $markEntryTable.DataTable().ajax.reload();
                        }
                        hideLoading("#create-form-holder");
                    },
                    failure: function (data) {
                    }
                })
            }
        });

        $('.print-btn').click(function (e) {
            e.preventDefault();
            var printOptionType = 'PDF';
            var sectionParam ="${g.createLink(controller: 'markEntry',action: 'examMarkPrint','_blank')}?printOptionType="+printOptionType+"&examSchedule="+ "${examSchedule.id}&inputType=ctMark&ctMarkFor=CLASS_TEST2";
            window.open(sectionParam);
            return false; // avoid to execute the actual submit of the form.
        });

        // extra-btn-1 -> is the class name for Delete All button
        $('.extra-btn-1').click(function (e) {
            var confirmDel = confirm("Are you sure?");
            if (confirmDel == true) {
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'markEntry',action: 'deleteCtMark')}?ctMarkFor=CLASS_TEST2&id=" + ${examSchedule.id},
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            showSuccessMsg(data.message);
                            window.location.href = reloadUrl;
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

        $('#list-table').on('click', 'a.edit-reference', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'markEntry',action: 'edit')}?ctMarkFor=CLASS_TEST2&id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        $('#id').val(data.objid);
                        $('#studentId').append('<option value="' + data.stdid + '" selected="selected">' + data.studentID + '</option>');
                        $('#attendStatus').val(data.attendStatus);
                        $('#markObtain').val(data.markObtain);
                        $('#create-form').show(500);
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
                showLoading("#list-table");
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'markEntry',action: 'delete')}?id=" + referenceId+"&examType=CLASS_TEST&ctMarkFor=CLASS_TEST2",
                    success: function (data, textStatus) {
                        hideLoading("#list-table");
                        if (data.isError == false) {
                            showSuccessMsg(data.message);
                            window.location.href = reloadUrl;
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
        actionButtons += '<span class="col-md-4 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="edit-reference" title="Edit">';
        actionButtons += '<span class="glyphicon glyphicon-edit"></span>';
        actionButtons += '</a></span>';
        actionButtons += '<span class="col-md-4 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="delete-reference" title="Delete">';
        actionButtons += '<span class="glyphicon glyphicon-trash"></span>';
        actionButtons += '</a></span>';
        return actionButtons;
    }
</script>
</body>
</html>