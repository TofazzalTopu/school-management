<%@ page import="com.grailslab.enums.GroupName; com.grailslab.enums.Shift" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Section</title>
    <meta name="layout" content="moduleStdMgmtLayout"/>
</head>

<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Section" SHOW_CREATE_BTN="YES" createButtonText="Add New Section" SHOW_LINK_BTN="YES" linkBtnText="Section Transfer"/>
<div class="row" id="create-form-holder" style="display: none;">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Add Section
            </header>
            <div class="panel-body">
                <form class="cmxform" id="create-form">
                    <g:hiddenField name="id" id="sectionId"/>
                    <div class="row">

                        <div class="col-md-3">
                            <div class="form-group">
                                <label class="control-label">Section Name</label><span class="required">*</span>
                                <g:textField class="form-control" id="name" tabindex="1" name="name"
                                             placeholder="Enter Section Name."/>
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="form-group">
                                <label class="control-label">Order</label><span class="required">*</span>
                                <g:textField class="form-control" id="sortOrder" tabindex="1" name="sortOrder"
                                             placeholder="Sort Order"/>
                            </div>
                        </div>

                        <div class="col-md-3">
                            <div class="form-group">
                                <label class="control-label">Class Name</label><span class="required">*</span>
                                <g:select class="form-control " id="className" name='className' tabindex="2"
                                          noSelection="${['': 'Select Class Name...']}"
                                          from='${classNameList}'
                                          optionKey="id" optionValue="name" />
                            </div>
                        </div>

                        <div class="col-md-3">
                            <div class="form-group">
                                <label class="control-label">Class Shift</label><span class="required">*</span>
                                <g:select class="form-control" id="shift" name='shift' tabindex="3"
                                          noSelection="${['': 'Select Shift...']}"
                                          from='${com.grailslab.enums.Shift.values()}'
                                          optionKey="key" optionValue="value" />
                            </div>
                        </div>

                </div>
                    <div class="row">

                        <div class="col-md-3">
                            <div class="form-group">
                                <label class="control-label">Academic Year</label> <span class="required">*</span>
                                <g:select tabindex="1" class="form-control"
                                          id="academicYear" name='academicYear'
                                          noSelection="${['': 'Select Year...']}"
                                          from='${academicYearList}'
                                          optionKey="key" optionValue="value" />
                            </div>
                        </div>

                        <div class="col-md-3">
                            <div class="form-group">
                                <label class="control-label">Group</label>
                                <g:select class="form-control" id="groupName" name='groupName' tabindex="3"
                                          noSelection="${['': 'No Group...']}"
                                          from='${com.grailslab.enums.GroupName.values()}'
                                          optionKey="key" optionValue="value" />
                            </div>
                        </div>
                        <div class="col-md-3">
                            <div class="form-group">
                                <label class="control-label">Class Teacher</label>
                                <g:select class="form-control auto-select-dropdown" id="employee" name='employee' tabindex="4"
                                          noSelection="${['': 'Select Class Teacher...']}"
                                          from='${teacherList}'
                                          optionKey="id" optionValue="name" />
                            </div>
                        </div>

                        <div class="col-md-3">
                            <div class="form-group">
                                <label class="control-label">Class Room</label>
                                <g:select class=" form-control" id="classRoom" name='classRoom' tabindex="5"
                                          noSelection="${['': 'Select Class Room...']}"
                                          from='${classRoomList}'
                                          optionKey="id" optionValue="name" />
                            </div>
                        </div>

                    </div>
                    <div class="row">
                        <div class="form-group">
                        <div class="col-md-offset-8 col-lg-4">
                            <button class="btn btn-primary" tabindex="6" type="submit">Save</button>
                            <button class="btn btn-default cancel-btn" tabindex="7" type="reset">Cancel</button>
                        </div>
                    </div>
                    </div>
                </form>
            </div>
        </section>
    </div>
</div>

<div class="row" >
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Section List
            </header>
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th>Serial</th>
                            <th>Class</th>
                            <th>Section</th>
                            <th>Order</th>
                            <th>Section In Charge</th>
                            <th>Shift</th>
                            <th>Action</th>
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

    var sectionTable, academicYear, className;
    jQuery(function ($) {
        $('.auto-select-dropdown').select2();

        var validator = $('#create-form').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                name: {
                    required: true
                },
                shift: {
                    required: true
                },
                className: {
                    required: true
                },
                sortOrder: {
                    required: true
                },
                academicYear: {
                    required: true
                },
                employee: {
                    required: true
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
                $.ajax({
                    url: "${createLink(controller: 'section', action: 'save')}",
                    type: 'post',
                    dataType: "json",
                    data: $("#create-form").serialize(),
                    success: function (data) {
                        if (data.isError == false) {
                            clearForm(form);
                            $('.auto-select-dropdown').select2("destroy");
                            $('.auto-select-dropdown').select2().enable(true);
                            var table = $('#list-table').DataTable().ajax.reload();
                            showSuccessMsg(data.message);
//                        $("#create-form-holder").hide(500);
                        } else {
                            showErrorMsg(data.message);

                        }

                    },
                    failure: function (data) {
                    }
                })
            }
        });

        sectionTable = $('#list-table').DataTable({
            "sDom": "<'row'<'col-md-1 academicYear-filter-holder'><'col-md-2 className-filter-holder'><'col-md-9'f>r>t<'row'<'col-md-3'l><'col-md-3'i><'col-md-6'p>>",
            "bAutoWidth": true,
            "scrollX": false,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'asc'],
            "sAjaxSource": "${g.createLink(controller: 'section',action: 'list')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }

                $('td:eq(6)', nRow).html(getGridActionBtns(nRow, aData, 'edit,delete'));
                return nRow;
            },
            "fnServerParams": function (aoData) {
                aoData.push(
                        {"name": "className", "value": $('#filterClassName').val()},
                        {"name": "academicYear", "value": $('#filterAcademicYear').val()}
                );

            },
            "aoColumns": [
                null,
                null,
                null,
                null,
                null,
                null,
                { "bSortable": false }
            ]
        });
        $('#list-table_wrapper div.academicYear-filter-holder').html('<select id="filterAcademicYear" class="form-control" name="filterAcademicYear"><g:each in="${academicYearList}" var="academicYear"><option value="${academicYear.key}" ${academicYear.key==workingYear.key?'selected':''}>${academicYear.value}</option> </g:each></select>');
        $('#list-table_wrapper div.className-filter-holder').html('<select id="filterClassName" class="form-control" name="filterClassName"><option value="">All Class</option><g:each in="${classList}" var="className"><option value="${className.id}">${className.name}</option> </g:each></select>');

        $('#filterAcademicYear').on('change', function (e) {
            showLoading("#data-table-holder");
            $('#filterClassName').val("").trigger("change");
            sectionTable.draw(false);
            hideLoading("#data-table-holder");
        });

        $('#filterClassName').on('change', function (e) {
            academicYear = $('#filterAcademicYear').val();
            className = $('#filterClassName').val();
            sectionTable.draw(false);
        });

        $('.create-new-btn').click(function (e) {
            $("#create-form-holder").toggle(1000);
            $("#academicYear").val("")
            $("#name").focus();
            e.preventDefault();
        });

        $(".cancel-btn").click(function(){
            var validator = $( "#create-form" ).validate();
            validator.resetForm();
            $("#sectionId").val('');
            clearForm("#create-form");
            $('.auto-select-dropdown').select2("destroy");
            $('.auto-select-dropdown').select2().enable(true);
            $("#create-form-holder").hide(500);
        });


        $('#list-table').on('click', 'a.edit-reference', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'section',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        clearForm('#create-form');
                        $('#sectionId').val(data.obj.id);
                        $('#name').val(data.obj.name);
                        $('#sortOrder').val(data.obj.sortOrder);
                        $('#shift').val(data.obj.shift.name);
                        $('#academicYear').val(data.obj.academicYear.name)
                        $('#groupName').val(data.obj.groupName?data.obj.groupName.name:'');
                        $('#classRoom').val(data.obj.classRoom? data.obj.classRoom.id:'');
                        $('#className').val(data.obj.className.id);
                        $('#employee').val(data.obj.employee? data.obj.employee.id:'');
//                        $('#employee').val(data.obj.employee.id);
                        $('.auto-select-dropdown').select2("destroy");
                        $('.auto-select-dropdown').select2().enable(true);
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
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'section',action: 'inactive')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            $("#list-table").DataTable().row(selectRow).remove().draw(false);
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

        $('.link-url-btn').click(function (e) {
            window.location.href = "${createLink(controller: 'section', action: 'moveSection', params: [academicYear: workingYear.key])}";
            e.preventDefault();
        });
    });

</script>

</body>
</html>


