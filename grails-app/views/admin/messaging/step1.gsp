<%@ page import="com.grailslab.enums.SelectionTypes" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleSmsLayout"/>
    <title>Messaging Step 1</title>
</head>
<body>
<grailslab:breadCrumbActions firstBreadCrumbText="Messaging" firstBreadCrumbUrl="${g.createLink(controller: 'messaging',action: 'index')}" breadCrumbTitleText="${title}"/>
<g:if test="${selectionType == com.grailslab.enums.SelectionTypes.BY_STUDENT.key}">
<div class="row" id="create-form-holder">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Add Student to send message
            </header>
            <div class="panel-body">
                <div class="row">

                    <div class="col-md-6">
                        <input type="hidden" class="form-control" id="student" name="student" tabindex="3" />
                    </div>

                    <div class="col-md-2">
                        <button id="add-btn" class="btn btn-large btn-primary">Add Student</button>
                    </div>
                </div>

            </div>
        </section>
    </div>
</div>
</g:if>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                ${title}
            </header>

            <div class="panel-body">
                <div class="table-responsive">
                    <g:form class="form-horizontal" method="POST" controller="messaging" action="step2">
                        <input type="hidden" name="selectionType" value="${selectionType}">
                        <input type="hidden" name="draftSmsId" value="${draftSmsId}">
                        <input type="hidden" name="academicYear" value="${academicYear?.key}">
                        <table class="table table-striped table-hover table-bordered" id="list-table">
                            <thead>
                            <tr>
                                <th>Serial</th>
                                <th>${title}</th>
                                <th><input type="checkbox" name="checkAll" id="checkAll" checked> Manage All</th>
                            </tr>
                            </thead>
                            <tbody>
                            <g:each in="${dataList}" var="dataSet" status="i">
                                <tr>
                                    <td>${i + 1}</td>
                                    <td>${dataSet?.name}</td>
                                    <td>
                                        <input type="checkbox" name="checkedId" class="checkSingle" value="${dataSet?.id}"
                                               checked>
                                    </td>
                                </tr>
                            </g:each>
                            </tbody>
                        </table>
                        <div class="row">
                            <div class="form-group">
                                <div class="col-md-offset-10 col-md-2">
                                    <button class="btn btn-primary btn-submit" type="submit">Next</button>
                                </div>
                            </div>
                        </div>
                    </g:form>
                </div>
            </div>
        </section>
    </div>
</div>

<script>
    $('#student').select2({
        placeholder: "Search for a Student [studentId/name/Class Name/mobile",
        allowClear: true,
        minimumInputLength:3,
        ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
            url: "${g.createLink(controller: 'remote',action: 'studentTypeAheadList')}",
            dataType: 'json',
            quietMillis: 250,
            data: function (term, page) {
                return {
                    q: term, // search term
                    academicYear: "${academicYear?.key}" // search term
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


    jQuery(function ($) {

        $("#checkAll").change(function () {
            if (this.checked) {
                $(".checkSingle").each(function () {
                    this.checked = true;
                })
            } else {
                $(".checkSingle").each(function () {
                    this.checked = false;
                })
            }
        });

        $(".checkSingle").click(function () {
            if ($(this).is(":checked")) {
                var isAllChecked = 0;
                $(".checkSingle").each(function () {
                    if (!this.checked)
                        isAllChecked = 1;
                })
                if (isAllChecked == 0) {
                    $("#checkAll").prop("checked", true);
                }
            }
            else {
                $("#checkAll").prop("checked", false);
            }
        });

        var count = 0;
        var selectedOption, studentId, studentDetail;
        $('#add-btn').click(function (e) {
            selectedOption = $("#student").select2('data');
            if (selectedOption != '') {
                count = count + 1;
                studentId = selectedOption.id;
                studentDetail = selectedOption.name;
                $('#list-table').append(
                        '<tr>' +
                        '<td>' + count + '</td>' +
                        '<td>' + studentDetail + '</td>' +
                        '<td><input type="checkbox" name="checkedId" class="checkSingle" value="' + studentId + '" checked></td>' +
                        '</tr>'
                );
            }
            e.preventDefault();
        });

    });

</script>
</body>
</html>