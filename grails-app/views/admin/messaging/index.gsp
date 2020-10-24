<%@ page import="com.grailslab.enums.SelectionTypes" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleSmsLayout"/>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="SMS" SHOW_CREATE_BTN="YES" createButtonText="Send SMS"/>
<div class="row" id="create-form-holder" style="display: none;">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                <g:if test="${flash.message}">
                    <div class="message center"><h4>${flash.message}</h4></div>
                </g:if>
            </header>
            <div class="panel-body">
                <g:form class="form-horizontal" method="POST" controller="messaging" action="step1">
                    <div class="col-md-12">
                        <div class="form-group">
                            <label class="col-md-2 control-label">Selection Type<span class="required">*</span></label>
                            <div class="col-md-6 radio">
                                <g:each in="${com.grailslab.enums.SelectionTypes.values()}" var="type">
                                    <label>
                                        <input id="${type.key}" type="radio" name="selectionType"
                                               value="${type.key}">
                                        ${type.value}
                                    </label>
                                </g:each>
                            </div>
                            <div class="col-md-3">
                                <g:select tabindex="2" class="form-control" id="academicYear"
                                          name='academicYear'
                                          from='${academicYearList}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="form-group">
                            <div class="col-md-offset-8 col-lg-4">
                                <button class="btn btn-primary btn-submit" type="submit">Next</button>
                            </div>
                        </div>
                    </div>
                </g:form>
            </div>
        </section>
    </div>
</div>
<sec:ifAllGranted roles="ROLE_SUPER_ADMIN">
<grailslab:fullModal modalLabel="SMS Purchase">
    <div class="col-md-12">
        <form class="form-inline" role="form" id="create-form">
            <g:hiddenField name="id"/>
            <div class="form-group">
                <input type="text" class="form-control" id="numOfSms" name="numOfSms" placeholder="Total SMS Purchase" required>
            </div>
            <div class="form-group">
                <input type="text" class="form-control" id="smsPrice" name="smsPrice" placeholder="Price" required>
            </div>
            <div class="form-group" id="data_range">
                <div class="input-daterange input-group" id="datepicker">
                    <span class="input-group-addon">On</span>
                    <g:textField class="input-sm form-control" id="billingDate" name="billingDate"
                                 tabindex="2" placeholder="Buy Date" required="required"/>
                    <span class="input-group-addon">to</span>
                    <g:textField class="input-sm form-control" id="expireDate" name="expireDate"
                                 tabindex="3" placeholder="Expire Date" required="required"/>
                </div>
            </div>
        </form>
    </div>
</grailslab:fullModal>

<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                SMS Balance
            </header>
            <div class="panel-body">
                <div class="btn-group btn-margin-left">
                    <a href="javascript:void(0);" class="btn btn-primary" id="addSmsBalance">
                        <i class="fa fa fa-plus"></i> Add SMS Balance
                    </a>
                </div>
            </div>
        </section>
    </div>
</div>
</sec:ifAllGranted>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                SMS History and Balance
            </header>

            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th>Date</th>
                            <th>Type</th>
                            <th>Message</th>
                            <th>Receiver</th>
                            <th>SMS Sent</th>
                            <th>SMS Balance</th>
                            <th>Expire Date</th>
                            <th>Sent by</th>
                        </tr>
                        </thead>
                        <tbody>
                        <g:each in="${dataReturn}" var="dataSet" status="i">
                            <tr>
                                <td>${dataSet[0]}</td>
                                <td>${dataSet[1]}</td>
                                <td>${dataSet[2]}</td>
                                <td>${dataSet[3]}</td>
                                <td>${dataSet[4]}</td>
                                <td>${dataSet[5]}</td>
                                <td>${dataSet[6]}</td>
                                <td>${dataSet[7]}</td>
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
        var validator = $('#modalForm').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                numOfSms: {
                    required: true
                },
                smsPrice: {
                    required: true
                }
            },
            errorPlacement: function (error, element) {
            },
            highlight: function (e) {
                $(e).closest('.form-group').removeClass('has-info').addClass('has-error');
            },
            success: function (e) {
                $(e).closest('.form-group').removeClass('has-error').addClass('has-info');
                $(e).remove();

            },
            submitHandler: function (form) {
                $('#myModal .create-content .modal-footer-action-btn').hide();
                $('#myModal .create-content .modal-refresh-processing').show();
                $.ajax({
                    url: "${createLink(controller: 'messaging', action: 'smsPurchase')}",
                    type: 'post',
                    dataType: "json",
                    data: $("#modalForm").serialize(),
                    success: function (data) {
                        formSuccess(data);
                    },
                    failure: function (data) {
                    }
                })
            }
        });

        $('#addSmsBalance').click(function (e) {
            $('#myModal .create-success').hide();
            $('#myModal .create-content').show();
            $("#hiddenId").val('');
            $('#myModal').modal('show');
            e.preventDefault();
        });
        $('#data_range .input-daterange').datepicker({
            keyboardNavigation: false,
            format: 'dd/mm/yyyy',
            forceParse: false,
            autoclose: true
        });

        $('#list-table').dataTable({
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "deferLoading": ${totalCount?:0},
            "order": [[ 0, "desc" ]],
            "sAjaxSource": "${g.createLink(controller: 'messaging',action: 'smsList')}",
            "aoColumns": [
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
            ]
        });

        $('.create-new-btn').click(function (e) {
            $("#create-form-holder").toggle(500);
            e.preventDefault();
        });
    });

</script>
</body>
</html>