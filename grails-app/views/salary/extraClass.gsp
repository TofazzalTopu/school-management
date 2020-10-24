<html>
<head>
    <title>Extra Class</title>
    <meta name="layout" content="moduleHRLayout"/>
    <style>
    .form-group {
        margin-bottom: 5px;
    }
    </style>

</head>

<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Extra Class" SHOW_CREATE_BTN="YES" createButtonText="New Extra Class"  SHOW_PRINT_BTN="YES"/>
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
                                <label for="numOfClass" class="col-md-1 control-label">Class</label>
                                <div class="col-md-3">
                                    <input class="form-control" type="text" name="numOfClass" id="numOfClass" placeholder="Number of Classes"
                                           tabindex="2"/>
                                    </div>

                                <label for="rate" class="col-md-1 control-label">Rate</label>
                                <div class="col-md-2">
                                    <input class="form-control" type="text" name="rate" id="rate" placeholder="Rate"
                                           tabindex="2"/>
                                </div>
                                <label for="amount" class="col-md-1 control-label">Amount</label>
                                <div class="col-md-2">
                                    <input class="form-control" type="text" name="amount" id="amount" placeholder="Amount" readonly
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

<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Extra Class List
            </header>

            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th class="">SL NO</th>
                            <th>Employee-ID</th>
                            <th>Name</th>
                            <th>Class</th>
                            <th>Rate</th>
                            <th>Amount</th>
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
    var extraClassTable, extraClassRate;
    jQuery(function ($) {
        extraClassRate = "${extraClassRate}";
         extraClassTable = $('#list-table').DataTable({
            "sDom": "<'row'<'col-md-1 academicYear-filter-holder'><'col-md-2 month-filter-holder'><'col-md-9'f>r>t<'row'<'col-md-3'l><'col-md-3'i><'col-md-6'p>>",
            "bAutoWidth": true,
            "scrollX": false,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'asc'],
            "sAjaxSource": "${g.createLink(controller: 'extraClass',action: 'list')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }

                $('td:eq(6)', nRow).html(getGridActionBtns(nRow, aData, 'edit,delete'));
                return nRow;
            },
            "fnServerParams": function (aoData) {
                aoData.push(
                        {"name": "academicYear", "value": $('#filterAcademicYear').val()},
                        {"name": "yearMonths", "value": $('#filterYearMonths').val()}
                );


            },
            "aoColumns": [
                {"bSortable": false,"sClass": "center"},
                null,
                null,
                {"bSortable": false,"sClass": "center"},
                {"bSortable": false,"sClass": "center"},
                {"bSortable": false,"sClass": "center"},
                {"bSortable": false}
            ]
        });
        $('#list-table_wrapper div.academicYear-filter-holder').html('<select id="filterAcademicYear" class="form-control" name="filterAcademicYear"><g:each in="${academicYearList}" var="academicYear"><option value="${academicYear.key}" ${academicYear.key==workingYear.key? 'selected':''}>${academicYear.value}</option> </g:each></select>');
        $('#list-table_wrapper div.month-filter-holder').html('<select id="filterYearMonths" class="form-control" name="filterYearMonths"><g:each in="${com.grailslab.enums.YearMonths.values()}" var="yearMonths"><option value="${yearMonths.key}">${yearMonths.value}</option></g:each></select>');

        $('#filterAcademicYear').on('change', function (e) {
            showLoading("#data-table-holder");
            extraClassTable.draw(false);
            hideLoading("#data-table-holder");
        });
        $('#filterYearMonths').on('change', function (e) {
            showLoading("#data-table-holder");
            extraClassTable.draw(false);
            hideLoading("#data-table-holder");
        });

        $('.create-new-btn').click(function (e) {
            $("#create-form-holder").toggle(500);
            $("#form-submit-btn").html("Save");
            $("#id").val("");
            $('#academicYear').attr('readonly', false);
            $("#academicYear").css("pointer-events","auto");
            $('#yearMonths').attr('readonly', false);
            $("#yearMonths").css("pointer-events","auto");
            $("#academicYear").val("");
            $("#yearMonths").val("");
            $("#numOfClass").val("");
            $("#rate").val(extraClassRate);
            $("#amount").val("");
            $("#select2-chosen-1").empty().append("Search for a Employee [employeeId/name/designation]");
            $("#s2id_employee").css("pointer-events","auto");
            $("#name").focus();
            e.preventDefault();
        });

        $(".cancel-btn").click(function () {
            $("#create-form-holder").hide(500);
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

        $("#create-form").submit(function(e) {
        e.preventDefault();
            showLoading("#create-form-holder");
            $.ajax({
                url: "${createLink(controller: 'extraClass', action: 'save')}",
                type: 'post',
                dataType: "json",
                data: $("#create-form").serialize(),
                success: function (data) {
                    hideLoading("#create-form-holder");
                    if (data.isError == false) {
                        $("#select2-chosen-1").empty().append("Search for a Employee  [Name]");
                        $("#numOfClass").val(" ");
                        $("#amount").val(" ");
                        extraClassTable.draw(false);
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

        $('#list-table').on('click', 'a.edit-reference', function (e) {
            $("#create-form-holder").show(500);
            $("#name").focus();
            var control = this;
            var referenceId = $(control).attr('referenceId');
           // alert(referenceId)

            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'extraClass',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        $("#form-submit-btn").html("Edit");
                        $("#id").val(data.obj.id)
                        $('#academicYear').val(data.obj.academicYear.name)
                        $('#yearMonths').val(data.obj.yearMonths.name)
                        $('#academicYear').attr('readonly', true);
                        $("#academicYear").css("pointer-events","none");
                        $('#yearMonths').attr('readonly', true);
                        $("#yearMonths").css("pointer-events","none");
                        $("#employee").val(data.obj.employee.id)
                        $("#select2-chosen-1").empty().append(data.employeeName);
                        $("#s2id_employee").css("pointer-events","none");

                       $("#numOfClass").val(data.obj.numOfClass)
                       $("#rate").val(data.obj.rate)
                       $("#amount").val(math.multiply(data.obj.numOfClass ,data.obj.rate))
                       $("#create-form-holder").show(500);
                       $("#name").focus();

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
                        url: "${g.createLink(controller: 'extraClass',action: 'delete')}?id=" + referenceId,
                        success: function (data, textStatus) {
                            if (data.isError == false) {
                               showSuccessMsg(data.message);
                                extraClassTable.draw(false);
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
        $("#numOfClass").numeric();
        $("#rate").numeric({decimalPlaces: 2 });
        $("#numOfClass").blur(function(){
            if($("#numOfClass").length>0  && $("#rate").length>0 ){
                $("#amount").val(math.multiply($("#numOfClass").val(),$("#rate").val()))
            }
        })
        $("#rate").blur(function(){
            if($("#numOfClass").length>0 && $("#rate").length>0 ){
                $("#amount").val( math.multiply($("#numOfClass").val(),$("#rate").val()))
            }
        })
        $('.print-btn').click(function (e) {
            e.preventDefault();
            var  academicYear = $('#filterAcademicYear').find("option:selected").val();
            var  yearMonths = $('#filterYearMonths').find("option:selected").val();
            var printParam ="${g.createLink(controller: 'salaryReport',action: 'extraClass','_blank')}?academicYear="+academicYear+"&yearMonths="+yearMonths;
            window.open(printParam);
            return false;
        });
    });
</script>
</body>
</html>