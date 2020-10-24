<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleCollectionLayout"/>
    <title>Scholarship</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Scholarship" SHOW_CREATE_BTN="YES"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <div class="panel-body">
                    <div class="table-responsive">
                        <table class="table table-striped table-hover table-bordered" id="list-table">
                            <thead>
                            <tr>
                                <th>Serial</th>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Roll</th>
                                <th>Class</th>
                                <th>Section</th>
                                <th>Scholarship</th>
                                <th>Action</th>
                            </tr>
                            </thead>
                        </table>
                    </div>
            </div>
        </section>
    </div>
</div>
<!-- Modal -->

<div class="modal fade" id="create-form-holder-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
                        class="sr-only">Close</span></button>
                <h4 class="modal-title" id="create-form-holder-label">Add Student Scholarship</h4>
            </div>
            <form class="form-horizontal" role="form" id="create-form">
                <g:hiddenField name="id" id="studentID"/>
                <div class="modal-body">
                    <div class="row">
                        <div class="row">
                            <div class="form-group">
                                <label class="col-md-3 control-label">Student<span class="required">*</span></label>

                                <div class="col-md-8" id="student_dd-holder">
                                    <input type="hidden" class="form-control" id="student" name="student" tabindex="1" />
                                </div>
                                <div class="col-md-8" id="student-name-holder" style="display: none">
                                    <input class="form-control" id="student2" name="student2" tabindex="1" />
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-3 control-label">Applied Free <span class="required">*</span></label>
                                <div class="col-md-8">
                                    <label class="radio-inline">
                                        <input id="appliedFull" type="radio" checked name="scholarshipType"
                                               value="FULL">
                                        full Free
                                    </label>
                                    <label class="radio-inline">
                                        <input id="appliedHalf" type="radio" name="scholarshipType"
                                               value="HALF">
                                        half Free
                                    </label>
                                </div>
                            </div>

                        </div>
                    </div>
                </div>
                <div class="modal-footer modal-footer-action-btns">
                    <button type="button" class="btn btn-default cancel-btn" data-dismiss="modal" aria-hidden="true">Cancel</button>
                    <button type="submit" id="print-schedule-yes-btn" class="btn btn-large btn-primary btn-submit">Submit</button>
                </div>
            </form>
        </div>
    </div>
</div>



<script>
    var workingYear, viewHrefUrl, academicYear, className, sectionName, printParam;
    jQuery(function ($) {
        var stuAttnList = $('#list-table').DataTable({
            "sDom": "<'row'<'col-md-3 academicYear-filter-holder'><'col-md-3 className-filter-holder'><'col-md-3 section-filter-holder'><'col-md-3'f>r>t<'row'<'col-md-3'l><'col-md-3'i><'col-md-6'p>>",
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0,'desc'],
            "sAjaxSource": "${g.createLink(controller: 'discount',action: 'scholarshipList')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(7)', nRow).html(getActionButtons(nRow, aData));
                return nRow;
            },
            "fnServerParams": function (aoData) {
                aoData.push({"name": "sectionName", "value": $('#filterSection').val()},
                    {"name": "className", "value": $('#filterClassName').val()},
                    {"name": "academicYear", "value": $('#filterAcademicYear').val()});
            },
            "aoColumns": [
                null,
                null,
                { "bSortable": false },
                { "bSortable": false },
                { "bSortable": false },
                { "bSortable": false },
                { "bSortable": false },
                { "bSortable": false }
            ]
        });
        $('#list-table_wrapper div.academicYear-filter-holder').html('<select id="filterAcademicYear" class="form-control" name="filterAcademicYear"><g:each in="${academicYearList}" var="academicYear"><option value="${academicYear.key}" ${academicYear.key==workingYear.key?'selected':''}>${academicYear.value}</option> </g:each></select>');
        $('#list-table_wrapper div.className-filter-holder').html('<select id="filterClassName" class="form-control" name="filterClassName"><option value="">All Class</option><g:each in="${classList}" var="className"><option value="${className.id}">${className.name}</option> </g:each></select>');
        $('#list-table_wrapper div.section-filter-holder').html('<select id="filterSection" class="form-control" name="filterSection"><option value="">All Section</option></select>');

        $('#filterAcademicYear').on('change', function (e) {
            $('#filterClassName').val("").trigger("change");
        });
        $('#filterClassName').on('change', function (e) {
            academicYear = $('#filterAcademicYear').val();
            className = $('#filterClassName').val();
            loadSectionOnClassChange(academicYear, className);
            $('#filterSection').val("").trigger("change");
        });
        $('#filterSection').on('change', function (e) {
            showLoading("#data-table-holder");
            $("#list-table").DataTable().draw(false);
            hideLoading("#data-table-holder");
        });

        $('#student').select2({
            placeholder: "Select Student...",
            allowClear: true,
            minimumInputLength:3,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: "${g.createLink(controller: 'remote',action: 'studentTypeAheadList')}",
                dataType: 'json',
                quietMillis: 250,
                data: function (term, page) {
                    return {
                        q: term, // search term
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

        var validator = $('#create-form').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {

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
                    url: "${createLink(controller: 'discount', action: 'saveScholarship')}",
                    type: 'post',
                    dataType: "json",
                    data: $("#create-form").serialize(),
                    success: function (data) {
                        if (data.isError == false) {
                            $("#student").val("");
                            showSuccessMsg(data.message);
                            var table = $('#list-table').DataTable().ajax.reload();
                            var scholarshipID = $("#scholarshipID").val();
                            if(scholarshipID !=undefined && scholarshipID !=""){
                                window.location.href = "${g.createLink(controller: 'discount',action: 'scholarship')}";
                            }
                            $("#create-form-holder").hide(500);
                        } else {
                            showErrorMsg(data.message);
                        }

                    },
                    failure: function (data) {
                    }
                })
            }

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
                    url: "${g.createLink(controller: 'discount',action: 'scholarshipDelete')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            $("#list-table").DataTable().draw(false);
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

        $('.create-new-btn').click(function (e) {
            $('#create-form-holder-modal').modal('show');
            e.preventDefault();
        });

    });

    function repoFormatResult(repo) {
        var markup = '<div class="row-fluid">' +
                '<div class="span12">' + repo.name + '</div>' +
                '</div>';
        return markup;
    }

    function repoFormatSelection(repo) {
        return repo.name;
    }
    function loadSectionOnClassChange(academicYear, classNameOnchange) {
        var $filterSection = $('#filterSection');
        $filterSection.find('option').remove();
        $filterSection.append('<option value="">All Section</option>');
        if (classNameOnchange.length === 0) {
            return false
        }
        jQuery.ajax({
            type: 'POST',
            dataType: 'JSON',
            url: "${g.createLink(controller: 'remote',action: 'classSectionList')}?academicYear=" + academicYear+"&className="+classNameOnchange,
            success: function (data, textStatus) {
                if (data.isError == false) {
                    $.each(data.sectionList, function (key, value) {
                        $filterSection.append('<option value=' + value.id + '>' + value.name + '</option>');
                    });
                } else {
                    showErrorMsg(data.message);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            }
        });
    }
    function getActionButtons(nRow, aData) {
        var actionButtons = "";
        actionButtons += '<span class="col-md-6 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="delete-reference" title="Delete">';
        actionButtons += '<span class="green glyphicon glyphicon-trash"></span>';
        actionButtons += '</a></span>';
        return actionButtons;
    }

</script>
</body>
</html>
