<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Grade Point</title>
    <meta name="layout" content="moduleStdMgmtLayout"/>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Grade Point" />
<div class="row" id="create-form-holder" style="display: none;">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Add Grade Point
            </header>
            <div class="panel-body">
                <form class="cmxform form-horizontal" id="create-form">
                    <g:hiddenField name="id" id="greadPointId"/>

                    <div class="row">
                        <div class="form-group">
                        <label for="gPoint" class="control-label col-md-4">Grade Point</label><span
                                class="required">*</span>

                        <div class="col-md-7">
                            <g:textField class="form-control" id="gPoint" tabindex="1" name="gPoint"
                                         placeholder="Enter Greade Point"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="fromMark" class="control-label col-md-4">From Marks</label>

                        <div class="col-md-7">
                            <g:textField class="form-control" id="fromMark" tabindex="1" name="fromMark"
                                         placeholder="Enter Marks "/>
                        </div>
                    </div>
                    <div class="form-group">
                            <label for="upToMark" class="control-label col-md-4">Up To Marks</label>

                            <div class="col-md-7">
                                <g:textField class="form-control" id="upToMark" tabindex="1" name="upToMark"
                                             placeholder="Enter Marks "/>
                            </div>
                    </div>
                    <div class="form-group">
                        <label for="laterGrade" class="control-label col-md-4">Letter Grade</label><span
                            class="required">*</span>

                        <div class="col-md-7">
                            <g:textField class="form-control" id="laterGrade" tabindex="1" name="laterGrade"
                                         placeholder="Enter Letter Grade"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="credentials" class="control-label col-md-4">Credentials</label><span
                            class="required">*</span>

                        <div class="col-md-7">
                            <g:textField class="form-control" id="credentials" tabindex="1" name="credentials"
                                         placeholder="Enter Credentials"/>
                        </div>
                    </div>
                </div>


                    <div class="row">
                        <div class="form-group">
                            <div class="col-md-offset-8 col-lg-4">
                                <button class="btn btn-primary" tabindex="3" type="submit">Save</button>
                                <button class="btn btn-default cancel-btn" tabindex="4"
                                        type="reset">Cancel</button>
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
                Grade Point  List
            </header>
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th class="col-md-2">Serial</th>
                            <th class="col-md-2">G.P</th>
                            <th class="col-md-2">Marks</th>
                            <th class="col-md-2">L.Grade</th>
                            <th class="col-md-3">Credentials</th>
                            <th class="col-md-3">Action</th>
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
                                <td>
                                    <a href="" referenceId="${dataSet["DT_RowId"]}" class="reference-1">
                                        <span class="fa fa-pencil-square-o"></span>
                                    </a>
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

<script>
    jQuery(function ($) {

        var validator = $('#create-form').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                gPoint: {
                    required: true
                },
                fromMark: {
                    required: true
                },
                upToMark: {
                    required: true
                },
                laterGrade: {
                    required: true
                },
                credentials: {
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
                    url: "${createLink(controller: 'gradePoints', action: 'update')}",
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

        var oTable1 = $('#list-table').dataTable({
            "sDom": "<'row'<'col-md-6 className-filter-holder'><'col-md-6'f>r>t<'row'<'col-md-3'l><'col-md-3'i><'col-md-6'p>>",
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'desc'],
            "deferLoading": ${totalCount?:0},
            "sAjaxSource": "${g.createLink(controller: 'gradePoints',action: 'list')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(5)', nRow).html(getActionButtons(nRow, aData));
                return nRow;
            },
            "fnServerParams": function (aoData) {
                aoData.push({"name": "className", "value": $('#filterClassName').val()});
            },
            "aoColumns": [
                null,
                null,
                null,
                null,
                null,
                { "bSortable": false }
            ]
        });
        $('#list-table_wrapper div.className-filter-holder').html('<select id="filterClassName" class="form-control" name="filterClassName"><option value="">All Class</option><g:each in="${classNameList}" var="className"><option value="${className.id}">${className.name}</option> </g:each></select>');

        $('#filterClassName').on('change', function (e) {
            $("#list-table").DataTable().draw(false);
        });


        $('#list-table').on('click', 'a.reference-1', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            // alert(referenceId);
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'gradePoints',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        $('#greadPointId').val(data.obj.id);
                        $('#gPoint').val(data.obj.gPoint);
                        //$('#fromMark').val(data.obj.fromMark + " - " + data.obj.upToMark);
                        $('#fromMark').val(data.obj.fromMark);
                        $('#upToMark').val(data.obj.upToMark);
                        $('#laterGrade').val(data.obj.laterGrade);
                        $('#credentials').val(data.obj.credentials);
                        $("#create-form-holder").show(500);
                        $("#gPoint").focus();

                    } else {
                        showErrorMsg(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            e.preventDefault();
        });

        $(".cancel-btn").click(function(){
            var validator = $( "#create-form" ).validate();
            validator.resetForm();
            $("#greadPointId").val('');
            clearForm("#create-form");
            $("#create-form-holder").hide(500);
        });

    });

    function getActionButtons(nRow, aData) {
        return getActionBtnCustom(nRow, aData, 'fa fa-pencil-square-o');
    }


</script>
</body>
</html>


