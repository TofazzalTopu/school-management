<html>
<head>
    <title>Bonus Setting</title>
    <meta name="layout" content="moduleHRLayout"/>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Bonus Setting"  SHOW_CREATE_BTN="YES" createButtonText="Generate Bonus Sheet"/>
<div class="row" id="create-form-holder" style="display: none;" >
    <div class="col-md-12">
        <section class="panel">
            <div class="panel-body">
                <div class="row">
                    <div class="col-md-10 col-md-offset-1">
                        <form class="cmxform form-horizontal" id="create-form">
                            <g:hiddenField name="id"/>
                            <div class="row">
                                <div class="form-group">
                                    <label for="festivalName" class="col-md-2 control-label">Festival Name</label>
                                    <div class="col-md-2">
                                        <input class="form-control" type="text" name="festivalName" id="festivalName" tabindex="1"/>
                                    </div>
                                    <div class="col-md-4">
                                        <grailslab:select name="academicYear"  enums="true" label="Year"
                                                          from="${academicYearList}"></grailslab:select>
                                    </div>
                                    <label for="joinBefore" class="col-md-2 control-label">Joined Before</label>
                                    <div class="col-md-2">
                                        <input class="form-control" type="text"   name="joinBefore"  id="joinBefore" placeholder="<g:formatDate date="${new java.util.Date()}" format="dd/MM/yyyy" />"
                                               tabindex="2"/>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label for="basedOn" class="col-md-2 control-label">Bonus Calculate</label>
                                    <div class="col-md-2">
                                        <select name="basedOn" id="basedOn"  class="form-control">\
                                            <option value="grossSalary" selected>Gross</option>
                                            <option value="basic">Basic</option>
                                            <option value="totalSalary">Total Salary</option>
                                            <option value="basicOrGross">Basic or Gross</option>
                                        </select>
                                    </div>
                                    <label for="basicPercentage" class="col-md-2 control-label">Basic Percentage</label>
                                    <div class="col-md-2">
                                        <input class="form-control" type="text"   name="basicPercentage"  id="basicPercentage" tabindex="4" placeholder="%"/>
                                    </div>
                                    <label for="grossPercentage" class="col-md-2 control-label">Gross/Totoal Percentage</label>
                                    <div class="col-md-2">
                                        <input class="form-control" type="text"   name="grossPercentage"  id="grossPercentage" tabindex="4" placeholder="%"/>
                                    </div>
                                </div>

                                <div class="pull-right">
                                    <button class="btn btn-primary" tabindex="7" id="form-submit-btn" type="submit">Save</button>
                                    <button class="btn btn-default cancel-btn" tabindex="5"
                                            type="reset">Cancel</button>
                                </div>
                            </div>
                    </div>
                </form>
                </div>
            </div>
        </section>
    </div>
</div>
</div>

<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Salary Bonus List
            </header>

            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th class="">SL NO</th>
                            <th>Festival Name</th>
                            <th>Joined Before</th>
                            <th>Bonus Calculate</th>
                            <th>Basic Percentage</th>
                            <th>Gross Percentage</th>
                            <th>Status</th>
                            <th class="col-md-2"> </th>
                        </tr>
                        </thead>
                        <tbody>

                        </tbody>
                    </table>
                </div>
            </div>
        </section>
    </div>
</div>

<script>
    var extraClassTable;
    jQuery(function ($) {
        var salaryBonusTable = $('#list-table').DataTable({
            "sDom": "<'row'<'col-md-3 academicYear-filter-holder'><'col-md-9'f>r>t<'row'<'col-md-3'l><'col-md-3'i><'col-md-6'p>>",
            "bAutoWidth": true,
            "scrollX": false,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'asc'],
            "sAjaxSource": "${g.createLink(controller: 'salaryBonus',action: 'list')}",
            "fnServerParams": function (aoData) {
                aoData.push(
                    {"name": "academicYear", "value": $('#filterAcademicYear').val()}
                );
            },
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                if(aData[6].trim()==='Draft'){
                    $('td:eq(6)', nRow).html('<a class="regenerate" href="javascript:void(0)" referenceId="'+ aData.DT_RowId+ '"  title="Regenerate"><span class="glyphicon glyphicon-refresh"></span></a>');
                } else {
                    $('td:eq(6)', nRow).html('<span class="glyphicon glyphicon-ok" title="Salary disbursed to account"></span>');
                }
                if(aData[6].trim()==='Draft') {
                    $('td:eq(7)', nRow).html(getGridActionBtns(nRow, aData, 'delete, bonusSt,bs,disburse'));
                }else{
                    $('td:eq(7)', nRow).html(getGridActionBtns(nRow, aData, 'bonusSt,bs'));
                }
                return nRow;
            },
            "aoColumns": [
                {"bSortable": true,"sClass": "center"},
                null,
                null,
                null,
                null,
                null,
                null,
                {"bSortable": false,"sClass": "center"}
            ]

        });
        $('#joinBefore').datepicker({
            format: 'dd/mm/yyyy',
            endDate:'today',
            autoclose: true
        });

        $('#list-table_wrapper div.academicYear-filter-holder').html('<select id="filterAcademicYear" class="form-control" name="filterAcademicYear"><g:each in="${academicYearList}" var="academicYear"><option value="${academicYear.key}" ${academicYear.key==workingYear.key? 'selected':''}>${academicYear.value}</option> </g:each></select>');
        $('#filterAcademicYear').on('change', function (e) {
            showLoading("#data-table-holder");
            salaryBonusTable.draw(false);
            hideLoading("#data-table-holder");
        });
        $('.create-new-btn').click(function (e) {
            $("#confirm-form-holder").hide(500);
            $("#create-form-holder").toggle(500);
            $("#form-submit-btn").html("Save");
            $("#id").val("");
            $('#academicYear').attr('readonly', false);
            $("#academicYear").css("pointer-events","auto");
            $("#academicYear").val("");
            e.preventDefault();
        });

        $(".cancel-btn").click(function () {
            $("#create-form-holder").hide(500);
        });
        $(".cancel-btn-increment").click(function () {
            $("#confirm-form-holder").hide(500);
        });

        $('.link-url-btn').click(function (e) {
            $("#confYear").val("");
            $("#confMonth").val("");
            $("#create-form-holder").hide(500);
            $("#confirm-form-holder").toggle(500);
            e.preventDefault();
        });

        $("#create-form").submit(function(e) {
            e.preventDefault();
            showLoading("#create-form-holder");
            $.ajax({
                url: "${createLink(controller: 'salaryBonus', action: 'save')}",
                type: 'post',
                dataType: "json",
                data: $("#create-form").serialize(),
                success: function (data) {
                    clearField();
                    salaryBonusTable.draw(false);
                    hideLoading("#create-form-holder");
                    if (data.isError == false) {
                        showSuccessMsg(data.message);
                    } else {
                        showErrorMsg(data.message);
                    }

                },
                failure: function (data) {
                }
            })
            return false;
        });
        $('#list-table').on('click', 'a.regenerate', function (e) {
            var confirmStr = "Are you sure want to Regenerated this." +
                "  \n\nClick 'OK to confirm, or click 'Cancel' to stop this action.";
            var control = this;
            var referenceId = $(control).attr('referenceId');
            bootbox.confirm(confirmStr, function(clickAction) {
                if(clickAction) {
                    showLoading("#create-form-holder");
                    jQuery.ajax({
                        type: 'POST',
                        dataType: 'JSON',
                        url: "${createLink(controller: 'salaryBonus', action: 'regenerate')}?id="+referenceId,
                        success: function (data, textStatus) {
                            showLoading("#create-form-holder");
                            if (data.isError == false) {
                                showSuccessMsg(data.message);

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
        $('#list-table').on('click', 'a.disbursement-reference', function (e) {
            var confirmStr = "Are you sure want to disburse bonus sheet? No more edit will perform." +
                "  \n\nClick 'OK to confirm, or click 'Cancel' to stop this action.";
            var selectRow = $(this).parents('tr');
            var control = this;
            var referenceId = $(control).attr('referenceId');
            bootbox.confirm(confirmStr, function(clickAction) {
                if(clickAction) {
                    jQuery.ajax({
                        type: 'POST',
                        dataType: 'JSON',
                        url: "${g.createLink(controller: 'salaryBonus',action: 'disbursement')}?id=" + referenceId,
                        success: function (data, textStatus) {
                            if (data.isError == false) {
                                showSuccessMsg(data.message);
                                salaryBonusTable.draw(false);
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
        });

        $('#list-table').on('click', 'a.bonusSt-reference', function (e) {
            e.preventDefault();
            var control = this;
            var referenceId = $(control).attr('referenceId');
            var reportParam ="${g.createLink(controller: 'salaryReport',action: 'bonusReport','_blank')}?id="+referenceId+"&statementType=generalStatement&printOptionType=PDF";
            window.open(reportParam);
            return false;
        });
        $('#list-table').on('click', 'a.bs-reference', function (e) {
            e.preventDefault();
            var control = this;
            var referenceId = $(control).attr('referenceId');
            var reportParam ="${g.createLink(controller: 'salaryReport',action: 'bonusReport','_blank')}?id="+referenceId+"&statementType=bankStatement&printOptionType=PDF";
            window.open(reportParam);
            return false;
        });

        $('#list-table').on('click', 'a.delete-reference', function (e) {
            var confirmStr = "Are you want to delete this  ." +
                "  \n\nClick 'OK to confirm, or click 'Cancel' to stop this action.";
            var selectRow = $(this).parents('tr');
            var control = this;
            var referenceId = $(control).attr('referenceId');
            bootbox.confirm(confirmStr, function(clickAction) {
                if(clickAction) {
                    jQuery.ajax({
                        type: 'POST',
                        dataType: 'JSON',
                        url: "${g.createLink(controller: 'salaryBonus',action: 'delete')}?id=" + referenceId,
                        success: function (data, textStatus) {
                            if (data.isError == false) {
                                showSuccessMsg(data.message);
                                salaryBonusTable.draw(false);
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
        });

        function clearField(){
            $("#form-submit-btn").html("Save");
            $('#festivalName, #basicPercentage, #grossPercentage, #bonusCalculate, #joinBefore').val("");
        }



    });
</script>
</body>
</html>