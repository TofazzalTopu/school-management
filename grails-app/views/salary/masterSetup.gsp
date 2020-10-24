
<html>
<head>
    <title>Generate Salary</title>
    <meta name="layout" content="moduleHRLayout"/>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Generate Salary" SHOW_LINK_BTN="YES" linkBtnText="Confirm Salary Disburse" SHOW_CREATE_BTN="YES" createButtonText="New Salary"/>
<div class="row" id="create-form-holder" style="display: none;" >
    <div class="col-md-12">
         <section class="panel">
             <header class="panel-heading">
                 <span class="panel-header-info">Generate Salary</span>
             </header>
            <div class="panel-body">
                <div class="row">
                    <div class="col-md-10 col-md-offset-1">
                        <form class="cmxform form-horizontal" id="create-form">
                            <div class="row">
                                <div class="form-group">
                                  <div class="col-md-3">
                                        <grailslab:select name="academicYear"  enums="true" label="Year"
                                                          from="${academicYearList}"></grailslab:select>
                                  </div>
                                    <div class="col-md-3">
                                        <grailslab:select name="yearMonths"  label="Month" enums="true" from="${com.grailslab.enums.YearMonths.values()}"></grailslab:select>
                                    </div>
                                    <div class="col-md-3">
                                        <button class="btn btn-primary" tabindex="7" id="form-submit-btn" type="submit">Save</button>
                                        <button class="btn btn-default cancel-btn" tabindex="8"
                                                type="reset">Cancel</button>
                                    </div>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>

<div class="row" id="disburse-form-holder" style="display: none;" >
    <div class="col-md-12">
        <section class="panel">
            <header class="panel-heading">
                <span class="panel-header-info">Confirm Salary Disburse (Action can't undo)</span>
            </header>
            <div class="panel-body">
                <div class="row">
                    <div class="col-md-10 col-md-offset-1">
                        <form class="cmxform form-horizontal" id="create-form-disburse">
                            <div class="row">
                                <div class="form-group">
                                    <div class="col-md-3">
                                        <grailslab:select name="academicYear" enums="true" label="Year"
                                                          from="${academicYearList}"></grailslab:select>
                                    </div>

                                    <div class="col-md-3">
                                        <grailslab:select name="yearMonths"  label="Month" enums="true" from="${com.grailslab.enums.YearMonths.values()}"></grailslab:select>
                                    </div>
                                    <div class="col-md-3">
                                        <button class="btn btn-primary" tabindex="5" type="submit">Submit</button>
                                        <button class="btn btn-default cancel-btn-disburse" tabindex="6"
                                                type="reset">Cancel</button>
                                    </div>

                                </div>

                            </div>
                        </form>
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Salary Month List
            </header>

            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th class="col-md-1">SL NO</th>
                            <th class="col-md-2">Year</th>
                            <th class="col-md-2">Month</th>
                            <th class="col-md-2">Status</th>
                            <th class="col-md-5"></th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </section>
    </div>
</div>

<script>
    var $modalObj;
    var salaryMasterSetup;
    var reportUrl;
    jQuery(function ($) {
         salaryMasterSetup = $('#list-table').DataTable({
             "sDom": "<'row'<'col-md-3 academicYear-filter-holder'><'col-md-9'f>r>t<'row'<'col-md-3'l><'col-md-3'i><'col-md-6'p>>",
            "bAutoWidth": true,
            "scrollX": false,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'desc'],
            "sAjaxSource": "${g.createLink(controller: 'salarySetup',action: 'masterSetupList')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
              if(aData[3].trim()==='Prepared'){
                 $('td:eq(3)', nRow).html('<a class="regenerate" href="javascript:void(0)" referenceId="'+ aData.DT_RowId+ '"  title="Regenerate"><span class="glyphicon glyphicon-refresh"></span></a>');
              } else {
                  $('td:eq(3)', nRow).html('<span class="glyphicon glyphicon-ok" title="Salary disbursed to account"></span>');
              }

                if(aData[3].trim()==='Disbursement') {
                   $('td:eq(4)', nRow).html(getGridActionBtns(nRow, aData, 'ss, bs, pf, payslip,'));
                }else{
                    $('td:eq(4)', nRow).html(getGridActionBtns(nRow, aData, 'delete, ss, bs, pf, payslip, footNote,'));
                }
                return nRow;
            },
             "fnServerParams": function (aoData) {
                 aoData.push(
                         {"name": "academicYear", "value": $('#filterAcademicYear').val()}
                 );
             },
            "aoColumns": [
                {"bSortable": true,"sClass": "center"},
                {"bSortable": false,"sClass": "center"},
                {"bSortable": true,"sClass": "center"},
                {"bSortable": false,"sClass": "center"},
                {"bSortable": false}
            ]
        });

       $('#list-table').on('click', 'a.footNote-reference', function (e){
            $("#footNoteFormModal").show(500);
            $("#name").focus();
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'salarySetup',action: 'footNoteEdit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        glModal('${g.createLink(controller: 'remote',action: 'loadModal', params: [modalName:"/modals/footNote"])}',function(modalObj){
                            $modalObj = modalObj
                            $("#id").val(data.ID);
                            $("#footNote").val(data.footNote);
                        });
                    } else {
                        showErrorMsg(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            e.preventDefault();

        });
        $('#load_popup_modal_show_id').on('click', '#footNoteSave', function (e) {
            e.preventDefault();
            showLoading('#create-form-holder')
            $.ajax({
                url: "${createLink(controller: 'salarySetup', action: 'saveFootNote')}",
                type: 'post',
                dataType: "json",
                data: $("#footNoteFormModal").serialize(),
                success: function (data) {
                    hideLoading('#create-form-holder')
                    if (data.isError == false) {
                        salaryMasterSetup.draw(false);
                        showSuccessMsg(data.message);
                        $modalObj.modal('hide');
                    } else {
                        showErrorMsg(data.message);
                    }

                },
                failure: function (data) {
                }
            })
            return false;
        });
        $('#list-table_wrapper div.academicYear-filter-holder').html('<select id="filterAcademicYear" class="form-control" name="filterAcademicYear"><g:each in="${academicYearList}" var="academicYear"><option value="${academicYear.key}" ${academicYear.key==workingYear.key?'selected':''}>${academicYear.value}</option> </g:each></select>');
        $('#filterAcademicYear').on('change', function (e) {
            showLoading("#data-table-holder");
            salaryMasterSetup.draw(false);
            hideLoading("#data-table-holder");
        });
        $('.create-new-btn').click(function (e) {
            $("#disburse-form-holder").hide(500);
            $("#create-form-holder").toggle(500);
            $("#name").focus();
            e.preventDefault();
        });

        $('.link-url-btn').click(function (e) {
            $("#create-form-holder").hide(500);
            $("#disburse-form-holder").toggle(500);
            e.preventDefault();
        });

        $(".cancel-btn-disburse").click(function () {
            $("#disburse-form-holder").hide(500);
        });

        $(".cancel-btn").click(function () {
            $("#create-form-holder").hide(500);
        });

        $("#create-form").submit(function(e) {
            e.preventDefault();
            showLoading('#create-form-holder')
            $.ajax({
                url: "${createLink(controller: 'salarySetup', action: 'masterSetupSave')}",
                type: 'post',
                dataType: "json",
                data: $("#create-form").serialize(),
                success: function (data) {
                    hideLoading('#create-form-holder')
                    if (data.isError == false) {
                         salaryMasterSetup.draw(false);
                        showSuccessMsg(data.message);
                        $("#create-form-holder").hide(500);
                       } else {
                        showErrorMsg(data.message);
                    }

                },
                failure: function (data) {
                }
            })
            return false;
        });

        $("#create-form-disburse").submit(function(e) {
            var confirmStr = "Are you sure disburse salary? Action can't be undone.\n\nClick 'OK to confirm, or click 'Cancel' to stop this action.";
            bootbox.confirm(confirmStr, function(clickAction) {
                if(clickAction) {
                    showLoading('#disburse-form-holder');
                    $.ajax({
                        url: "${createLink(controller: 'salarySetup', action: 'disburseSalary')}",
                        type: 'post',
                        dataType: "json",
                        data: $("#create-form-disburse").serialize(),
                        success: function (data) {
                            hideLoading('#disburse-form-holder');
                            if (data.isError == false) {
                                $("#create-form-holder").hide(500);
                                showSuccessMsg(data.message);
                                salaryMasterSetup.draw(false);
                            } else {
                                showErrorMsg(data.message);
                            }
                        },
                        failure: function (data) {
                        }
                    })
                }
            });
            e.preventDefault();
        });


        $('#list-table').on('click', 'a.delete-reference', function (e) {
            var confirmStr = "Are you want to delete this  ." +
                    "  \n\nClick 'OK to confirm, or click 'Cancel' to stop this action.";
              var control = this;
            var referenceId = $(control).attr('referenceId');
            bootbox.confirm(confirmStr, function(clickAction) {
                if(clickAction) {
                    jQuery.ajax({
                        type: 'POST',
                        dataType: 'JSON',
                        url: "${g.createLink(controller: 'salarySetup',action: 'masterDelete')}?id=" + referenceId,
                        success: function (data, textStatus) {
                            if (data.isError == false) {
                               showSuccessMsg(data.message);
                                salaryMasterSetup.draw(false);
                            } else {
                                showErrorMsg(data.message);
                            }
                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                        }
                    });
                }
            });
            e.preventDefault();
        })

        $('#list-table').on('click', 'a.ss-reference', function (e) {
            e.preventDefault();
            var control = this;
            var referenceId = $(control).attr('referenceId');
            var printParam ="${g.createLink(controller: 'salaryReport',action: 'salarySheet','_blank')}/"+referenceId;
            window.open(printParam);
            return false;
        })
        $('#list-table').on('click', 'a.bs-reference', function (e) {
            e.preventDefault();
            var control = this;
            var referenceId = $(control).attr('referenceId');
            var printParam ="${g.createLink(controller: 'salaryReport',action: 'bankStatement','_blank')}/"+referenceId;
            window.open(printParam);
            return false;
        })
        $('#list-table').on('click', 'a.pf-reference', function (e) {
            e.preventDefault();
            var control = this;
            var referenceId = $(control).attr('referenceId');
            var printParam ="${g.createLink(controller: 'salaryReport',action: 'pfStatement','_blank')}/"+referenceId;
            window.open(printParam);
            return false;
        })

        $('#list-table').on('click', 'a.payslip-reference', function (e) {
            e.preventDefault();
            var control = this;
            var referenceId = $(control).attr('referenceId');
            var printParam ="${g.createLink(controller: 'salaryReport',action: 'payslip','_blank')}/"+referenceId;
            window.open(printParam);
            return false;
        })

        $('#list-table').on('click', 'a.regenerate', function (e) {
         var confirmStr = "Are you want to Regenerated this  ." +
                 "  \n\nClick 'OK to confirm, or click 'Cancel' to stop this action.";
             var control = this;
             var referenceId = $(control).attr('referenceId');
             bootbox.confirm(confirmStr, function(clickAction) {
             if(clickAction) {
                 showLoading('#list-table_wrapper')
                    jQuery.ajax({
                     type: 'POST',
                     dataType: 'JSON',
                     url: "${createLink(controller: 'salarySetup', action: 'masterSetupSave')}?id="+referenceId,
                     success: function (data, textStatus) {
                         hideLoading('#list-table_wrapper')
                         if (data.isError == false) {
                             showSuccessMsg(data.message);
                             salaryMasterSetup.draw(false);

                         } else {
                             showSuccessMsg(data.message);
                         }
                     },
                     error: function (XMLHttpRequest, textStatus, errorThrown) {
                     }
                 });
            }
         });

        });
    });

</script>
</body>
</html>