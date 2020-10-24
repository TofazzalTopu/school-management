<html>
<head>
    <title>Salary Increment</title>
    <meta name="layout" content="moduleHRLayout"/>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Salary Increment" SHOW_LINK_BTN="YES" linkBtnText="Confirm Salary Increment" SHOW_CREATE_BTN="YES" createButtonText="Add Increment"/>
<div class="row" id="create-form-holder" style="display: none;" >
    <div class="col-md-12">
        <section class="panel">
            <div class="panel-body">
                <div class="row">
                    <div class="col-md-10 col-md-offset-1">
                        <form class="cmxform form-horizontal" id="create-form">
                            <g:hiddenField name="id"/>
                            <g:hiddenField name="previousSalary"/>
                            <g:hiddenField name="previousdpsAmount"/>
                            <div class="row">
                                <div class="form-group">
                                    <div class="col-md-3">
                                        <grailslab:select name="academicYear"  enums="true" label="Year"
                                                          from="${academicYearList}"></grailslab:select>
                                    </div>
                                    <div class="col-md-4">
                                        <grailslab:select name="yearMonths"  label="Month" enums="true" from="${com.grailslab.enums.YearMonths.values()}"></grailslab:select>
                                    </div>

                                </div>
                                <div class="form-group">
                                    <label for="employee" class="col-md-1 control-label">Employee</label>
                                    <div class="col-md-6">
                                        <input type="hidden" class="form-control" id="employee" name="employee" tabindex="2" />
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label for="oldBasic" class="col-md-2 control-label">Old Basic</label>
                                    <div class="col-md-2">
                                        <input class="form-control" type="text" name="oldBasic" id="oldBasic" readonly="readonly"
                                               tabindex="2"/>
                                    </div>
                                    <label for="incrementPercentage" class="col-md-2 control-label">Increment (%)</label>
                                    <div class="col-md-2">
                                        <input class="form-control sal-increment-group textField-onlyAllow-number" type="text" name="incrementPercentage" id="incrementPercentage"
                                               tabindex="2"/>
                                    </div>
                                    <label for="newBasic" class="col-md-2 control-label">New Basic</label>
                                    <div class="col-md-2">
                                        <input class="form-control" type="text" name="newBasic" id="newBasic" readonly="readonly"
                                               tabindex="2"/>
                                    </div>
                                </div>
                            <div class="form-group">
                                <label for="houseRent" class="col-md-4 control-label">HouseRent (35% of new basic/Custom)</label>
                                <div class="col-md-2">
                                    <input class="form-control sal-increment-group textField-onlyAllow-number" type="text" name="houseRent" id="houseRent"
                                           tabindex="2"/>
                                </div>
                                <label for="medical" class="col-md-1 control-label"> Medical </label>
                                <div class="col-md-2">
                                    <input class="form-control sal-increment-group textField-onlyAllow-number" type="text" name="medical" id="medical"
                                           tabindex="2"/>
                                </div>
                                <label for="grossSalary" class="col-md-1 control-label">Gross</label>
                                <div class="col-md-2">
                                    <input class="form-control" type="text" name="grossSalary" id="grossSalary" readonly="readonly"
                                           tabindex="2"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="inCharge" class="col-md-1 control-label">Incharge</label>
                                <div class="col-md-2">
                                    <input class="form-control sal-increment-group textField-onlyAllow-number" type="text" name="inCharge" id="inCharge"
                                           tabindex="2"/>
                                </div>
                                <label for="mobileAllowance" class="col-md-1 control-label">M.Allowence</label>
                                <div class="col-md-2">
                                    <input class="form-control sal-increment-group textField-onlyAllow-number" type="text" name="mobileAllowance" id="mobileAllowance"
                                           tabindex="2"/>
                                </div>
                                <label for="others" class="col-md-1 control-label">Others</label>
                                <div class="col-md-2">
                                    <input class="form-control sal-increment-group textField-onlyAllow-number" type="text" name="others" id="others"
                                           tabindex="2"/>
                                </div>
                                <label for="totalSalary" class="col-md-1 control-label">Total</label>
                                <div class="col-md-2">
                                    <input class="form-control" type="text" name="totalSalary" id="totalSalary" readonly="readonly"
                                           tabindex="2"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="dpsAmountSchool" class="col-md-4 control-label">PF (5% of Total Salary)</label>
                                <div class="col-md-2">
                                    <input class="form-control" type="text" name="dpsAmountSchool" id="dpsAmountSchool" readonly="readonly"
                                           tabindex="2"/>
                                </div>
                                <label for="netIncrement" class="col-md-2 control-label">net Increment</label>
                                <div class="col-md-2">
                                    <input class="form-control" type="text" name="netIncrement" id="netIncrement"
                                           tabindex="2"/>
                                </div>
                            </div>
                                <div class="pull-right">
                                    <button class="btn btn-primary" tabindex="7" id="form-submit-btn" type="submit">Save</button>
                                    <button class="btn btn-default cancel-btn" tabindex="8"
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

<div class="row" id="confirm-form-holder" style="display: none;" >
    <div class="col-md-12">
        <section class="panel">el-body">
                <div class="row">
                    <div class="col-md-10 col-md-offset-1">
                        <form class="form-horizontal" id="confirm-increment-form">
                            <div class="row">
                                <div class="form-group">
                                    <div class="col-md-3">
                                        <grailslab:select name="academicYear" enums="true" label="Year" id="confYear"
                                                          from="${academicYearList}"></grailslab:select>
                                    </div>

                                    <div class="col-md-3">
                                        <grailslab:select name="yearMonths"  label="Month" enums="true" id="confMonth" from="${com.grailslab.enums.YearMonths.values()}"></grailslab:select>
                                    </div>
                                    <div class="col-md-3">
                                        <button class="btn btn-primary" tabindex="5" type="submit">Submit</button>
                                        <button class="btn btn-default cancel-btn-increment" tabindex="6"
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
                Salary Increment List
            </header>

            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th class="">SL NO</th>
                            <th>Month</th>
                            <th>Employee-ID</th>
                            <th>Name</th>
                            <th>Designation</th>
                            <th>Increment</th>
                            <th>Status</th>
                            <th>Edit | Delete </th>
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
        $("#netIncrement").change(function(){
            if($("#netIncrement").val().length>0 && $("#netIncrement").val() > 0) {
                $("#incrementPercentage").prop("readonly", true);
                if ($("#oldBasic").val().length> 0) {
                    var oldBasicVal = parseFloat($('#oldBasic').val());
                    var oldHouseRent = parseFloat($('#houseRent').val());
                    var oldGrossSalary = parseFloat($('#grossSalary').val());
                    var oldTotalSalary = parseFloat($('#totalSalary').val());
                    var netIncrement = parseFloat($('#netIncrement').val());
                    var basicIncOnNetIn = Math.round(netIncrement * 0.65);
                    var houseRentIncOnNetIn = netIncrement - basicIncOnNetIn;
                    var newBasicVal = oldBasicVal + basicIncOnNetIn;
                    var newHouseRent = oldHouseRent + houseRentIncOnNetIn;
                    var newTotalSalary = oldTotalSalary + netIncrement;
                    var newDpsAmount = Math.round(newTotalSalary * 0.05);
                    $('#newBasic').val(newBasicVal);
                    $('#houseRent').val(newHouseRent);
                    $('#grossSalary').val(oldGrossSalary + netIncrement);
                    $('#totalSalary').val(newTotalSalary);
                    $('#dpsAmountSchool').val(newDpsAmount);
                }
                $("#houseRent").prop("readonly", true);
                $("#medical").prop("readonly", true);
                $("#inCharge").prop("readonly", true);
                $("#inCharge").prop("readonly", true);
                $("#mobileAllowance").prop("readonly", true);
                $("#others").prop("readonly", true);
            }else{
                $("#incrementPercentage").prop("readonly", false);
                $("#houseRent").prop("readonly", false);
                $("#medical").prop("readonly", false);
                $("#inCharge").prop("readonly", false);
                $("#inCharge").prop("readonly", false);
                $("#mobileAllowance").prop("readonly", false);
                $("#others").prop("readonly", false);
            }
        });
        $("#incrementPercentage").change(function(){
            if($("#incrementPercentage").val().length>0 && $("#incrementPercentage").val() > 0) {
                $("#netIncrement").prop("readonly", true);
            }else{
                $("#netIncrement").prop("readonly", false);
                $("#houseRent").prop("readonly", false);
                $("#medical").prop("readonly", false);
                $("#inCharge").prop("readonly", false);
                $("#inCharge").prop("readonly", false);
                $("#mobileAllowance").prop("readonly", false);
                $("#others").prop("readonly", false);
            }
        });

       var salaryIncrementTable = $('#list-table').DataTable({
            "sDom": "<'row'<'col-md-1 academicYear-filter-holder'><'col-md-2 month-filter-holder'><'col-md-9'f>r>t<'row'<'col-md-3'l><'col-md-3'i><'col-md-6'p>>",
            "bAutoWidth": true,
            "scrollX": false,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'asc'],
            "sAjaxSource": "${g.createLink(controller: 'salIncrement',action: 'list')}",
           "fnServerParams": function (aoData) {
               aoData.push(
                       {"name": "academicYear", "value": $('#filterAcademicYear').val()},
                       {"name": "yearMonths", "value": $('#filterYearMonths').val()}
               );
           },
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }

                $('td:eq(7)', nRow).html(getGridActionBtns(nRow, aData, 'edit,delete , '));

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
                {"bSortable": false,"sClass": "center"},
            ]

        });

        $('#list-table_wrapper div.academicYear-filter-holder').html('<select id="filterAcademicYear" class="form-control" name="filterAcademicYear"><g:each in="${academicYearList}" var="academicYear"><option value="${academicYear.key}" ${academicYear.key==workingYear.key? 'selected':''}>${academicYear.value}</option> </g:each></select>');
        $('#list-table_wrapper div.month-filter-holder').html('<select id="filterYearMonths" class="form-control" name="filterYearMonths"><option value="">All</option><g:each in="${com.grailslab.enums.YearMonths.values()}" var="yearMonths"><option value="${yearMonths.key}">${yearMonths.value}</option></g:each></select>');

        $('#filterAcademicYear').on('change', function (e) {
            showLoading("#create-form-holder");
            salaryIncrementTable.draw(false);
            hideLoading("#create-form-holder");
        });
        $('#filterYearMonths').on('change', function (e) {
            showLoading("#create-form-holder");
            salaryIncrementTable.draw(false);
            hideLoading("#create-form-holder");
        });

        $('.create-new-btn').click(function (e) {
            $("#confirm-form-holder").hide(500);
            $("#create-form-holder").toggle(500);
            $("#form-submit-btn").html("Save");
            $("#id").val("");
            $("#previousSalary").val("");
            $('#academicYear').attr('readonly', false);
            $("#academicYear").css("pointer-events","auto");
            $('#yearMonths').attr('readonly', false);
            $("#yearMonths").css("pointer-events","auto");
            $("#academicYear").val("");
            $("#yearMonths").val("");
            $("#select2-chosen-1").empty().append("Search for a Employee [employeeId/name/designation]");
            $("#s2id_employee").css("pointer-events","auto");
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

        $("#confirm-increment-form").submit(function(e) {
            var confirmStr = "Are you sure disburse salary? Action can't be undone.\n\nClick 'OK to confirm, or click 'Cancel' to stop this action.";
            bootbox.confirm(confirmStr, function(clickAction) {
                if(clickAction) {
                    showLoading("#confirm-form-holder");
                    $.ajax({
                        url: "${createLink(controller: 'salIncrement', action: 'confirmIncrement')}",
                        type: 'post',
                        dataType: "json",
                        data: $("#confirm-increment-form").serialize(),
                        success: function (data) {
                            hideLoading("#confirm-form-holder");
                            if (data.isError == false) {
                                $("#confirm-form-holder").hide(500);
                                showSuccessMsg(data.message);
                                salaryIncrementTable.draw(false);
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
        $('#employee').select2({
            placeholder: "Search for a Employee [employeeId/name/designation]",
            allowClear: true,
            minimumInputLength:3,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: "${g.createLink(controller: 'remote',action: 'employeeWithDesignationList')}",
                dataType: 'json',
                quietMillis: 250,
                data: function (term, page) {
                    return {
                        q: term
                    };
                },
                results: function (data, page) { // parse the results into the format expected by Select2.
                    // since we are using custom formatting functions we do not need to alter the remote JSON data
                    return { results: data.items };
                },
                cache: true
            },
            formatResult: repoFormatResult, // omitted for brevity, see the source of this page
            formatSelection: repoFormatSelection,  // omitted for brevity, see the source of this page
            dropdownCssClass: "bigdrop", // apply css that makes the dropdown taller
            escapeMarkup: function (m) { return m; } // we do not want to escape markup since we are displaying html in results
        });
        $("#employee").on("change", function(e) {
            if($("#employee").val().length>0){
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'salarySetup', action: 'empSalarySetupData')}?id="+$("#employee").val(),
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            $("#oldBasic").val(data.obj.basic);
                            $("#incrementPercentage").val(0);
                            $("#newBasic").val(data.obj.basic);
                            $("#houseRent").val(data.obj.houseRent);
                            $("#medical").val(data.obj.medical);
                            $("#grossSalary").val(data.obj.grossSalary);
                            $("#inCharge").val(data.obj.inCharge);
                            $("#mobileAllowance").val(data.obj.mobileAllowance);
                            $("#others").val(data.obj.others);
                            $("#totalSalary").val(data.obj.totalSalary);
                            $("#previousSalary").val(data.obj.totalSalary);
                            $("#previousdpsAmount").val(data.obj.dpsAmountSchool);
                            $("#dpsAmountSchool").val(data.obj.dpsAmountSchool);
                            $("#netIncrement").val(0);
                        } else {
                             showErrorMsg(data.message);
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            }
        });

        $("#create-form").submit(function(e) {
            e.preventDefault();
            showLoading("#create-form-holder");
            $.ajax({
                url: "${createLink(controller: 'salIncrement', action: 'save')}",
                type: 'post',
                dataType: "json",
                data: $("#create-form").serialize(),
                success: function (data) {
                    hideLoading("#create-form-holder");
                    if (data.isError == false) {
                        clearField();
                        salaryIncrementTable.draw(false);
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

        $('#list-table').on('click', 'a.edit-reference', function (e){
            $("#create-form-holder").show(500);
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'salIncrement',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        $("#form-submit-btn").html("Update");
                        $("#id").val(data.obj.id);
                        $("#previousSalary").val(data.obj.previousSalary);
                        $("#previousdpsAmount").val(data.obj.previousdpsAmount);
                        $('#academicYear').val(data.obj.academicYear.name);
                        $('#yearMonths').val(data.obj.yearMonths.name);
                        $("#oldBasic").val(data.obj.oldBasic)
                        $("#incrementPercentage").val(data.obj.incrementPercentage)
                        $("#newBasic").val(data.obj.newBasic)
                        $("#houseRent").val(data.obj.houseRent);
                        $("#medical").val(data.obj.medical);
                        $("#grossSalary").val(data.obj.grossSalary);
                        $("#inCharge").val(data.obj.inCharge);
                        $("#mobileAllowance").val(data.obj.mobileAllowance);
                        $("#others").val(data.obj.others);
                        $("#totalSalary").val(data.obj.totalSalary);
                        $("#dpsAmountSchool").val(data.obj.dpsAmountSchool);
                        $("#netIncrement").val(data.obj.netIncrement);
                        $("#employee").val(data.obj.employee.id);
                        $("#select2-chosen-1").empty().append(data.employeeName);
                        $("#s2id_employee").css("pointer-events","none");

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
                        url: "${g.createLink(controller: 'salIncrement',action: 'delete')}?id=" + referenceId,
                        success: function (data, textStatus) {
                            if (data.isError == false) {
                                showSuccessMsg(data.message);
                                salaryIncrementTable.draw(false);
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
            $('#id, #previousSalary, #previousdpsAmount, #oldBasic, #incrementPercentage, #newBasic, #houseRent, #medical, #grossSalary, #inCharge, #mobileAllowance, #others,  #totalSalary, #dpsAmountSchool, #netIncrement, #employee').val("").attr('readonly', false).css({cursor:"text"});
            $("#select2-chosen-1").empty().append("Search for a Employee [employeeId/name/designation]");
            $("#s2id_employee").css("pointer-events","auto");
        }
        $('.textField-onlyAllow-number').numeric();
        $('#incrementPercentage').change(function(){
            if ($("#oldBasic").val().length> 0) {
                var oldBasicVal = parseFloat($('#oldBasic').val());
                var incrmnt = parseFloat($('#incrementPercentage').val());
                var newBasicVal = oldBasicVal + oldBasicVal * incrmnt * 0.01;
                var newHouseRent = newBasicVal * 0.35;
                $('#newBasic').val(Math.round(newBasicVal));
                $('#houseRent').val(Math.round(newHouseRent));
            }
            return false;
        });
        $('.sal-increment-group').change(function(){
            $("#grossSalary").val(
                math.chain($("#newBasic").val())
                    .add($("#houseRent").val())
                    .add($("#medical").val())
                    .done())
            $("#totalSalary").val(
                math.chain($("#grossSalary").val())
                    .add($("#inCharge").val())
                    .add($("#mobileAllowance").val())
                    .add($("#others").val())
                    .done())
            var totalSalary = parseFloat($('#totalSalary').val());
            var newIncrement = Math.round(totalSalary * 0.05);
            $("#dpsAmountSchool").val(newIncrement);
            $("#netIncrement").val(
                math.chain(totalSalary)
                    .add(newIncrement)
                    .subtract($("#previousSalary").val())
                    .subtract($("#previousdpsAmount").val())
                    .done())
        });
    });
</script>
</body>
</html>