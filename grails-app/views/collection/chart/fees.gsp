<%@ page import="com.grailslab.enums.NodeType; com.grailslab.enums.AccountType; com.grailslab.enums.FeeAppliedType; com.grailslab.enums.PrintOptionType" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleCollectionLayout"/>
    <title>Fees</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Chart Of Account" SHOW_CREATE_BTN="YES" createButtonText="Add New Head" SHOW_PRINT_BTN="YES"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table tree table-bordered table-striped table-condensed" id="list-table">
                        <thead>
                            <tr>
                                <th class="col-md-6">Fees Head</th>
                                <th class="col-md-2">Fee Type</th>
                                <th class="col-md-2">Scholarship Aplicable?</th>
                                <th class="col-md-2">Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            <g:each in="${dataReturn}" var="dataSet">
                                <tr class="${dataSet.treeClassName}">
                                    <td>${dataSet.name}</td>
                                    <td>${dataSet.accountType}</td>
                                    <td>${dataSet.scholarshipHead}</td>
                                    <td>
                                        <g:if test="${dataSet.allowChild}">
                                            <span class="col-md-4 no-padding"><a href="#" refid="${dataSet.id}"
                                                                                 headertext="${dataSet.name}"
                                                                                 class="add-reference" title="Add"><span
                                                        class="fa fa-plus"></span></a></span>
                                        </g:if>

                                        <g:if test="${dataSet.allowEdit}">
                                            <span class="col-md-4 no-padding"><a href="#" refid="${dataSet.id}"
                                                                                 headertext="${dataSet.name}"
                                                                                 class="edit-reference" title="Edit"><span
                                                        class="fa fa-edit"></span></a></span>
                                        </g:if>
                                        <span class="col-md-4 no-padding"><a href="#" refid="${dataSet.id}"
                                                                             headertext="${dataSet.name}"
                                                                             refhaschield="${dataSet.hasChild}"
                                                                             class="delete-reference" title="Delete"><span
                                                    class="fa fa-trash-o"></span></a></span>
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
<div class="modal fade" id="accountingHeadModal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel"
     aria-hidden="true">
    <div class="modal-dialog">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span
                        class="sr-only">Close</span></button>
                <h4 class="modal-title" id="accountingHeadModalLabel">Add Fees Head</h4>
            </div>

            <div class="action-fees-head-confirm fees-head-content">
                <form class="form-horizontal" role="form" id="create-form">
                    <g:hiddenField name="id"/>
                    <g:hiddenField name="parentId" id="parentId"/>
                    <div class="modal-body">
                        <div class="row">
                            <div class="row">
                                <div class="form-group">
                                    <label class="col-md-3  control-label">Name<span class="required">*</span></label>

                                    <div class="col-md-7">
                                        <g:textField class="form-control" id="name" name="name"
                                                     placeholder="Header Name" required="required"/>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <label class="col-md-3  control-label">Code</label>

                                    <div class="col-md-7">
                                        <input type="text" name="code"
                                               id="code" required="required" placeholder="1000,1001.."
                                               class="form-control">
                                    </div>
                                </div>

                                <div class="form-group" id="feeTypeHolder">
                                    <label class="col-md-3  control-label">Fee Type<span class="required">*</span></label>
                                    <div class="col-md-7">
                                        <g:select class="form-control" id="accountType" name='accountType'
                                                  from='${com.grailslab.enums.AccountType.feeMenuItems()}'
                                                  optionKey="key" optionValue="value" required="required"></g:select>
                                    </div>
                                </div>
                                <div class="form-group" id="allow-subhead-holder">
                                    <div class="col-md-offset-3 col-md-5">
                                        <label class="checkbox-inline">
                                            <input tabindex="4" type="checkbox" title="Allow Sub Head" name="allowSubHead"
                                                   id="allowSubHead">Allow Sub Head
                                        </label>
                                    </div>
                                </div>

                                <div class="form-group">
                                    <div class="col-md-offset-3 col-md-5">
                                        <label class="checkbox-inline">
                                            <input tabindex="5" type="checkbox" title=" Scholarship Applied " name="scholarshipHead"
                                                   id="scholarshipHead">Scholarship Applied ( Full free / Half Free)
                                        </label>
                                    </div>
                                </div>
                                <div class="form-group">
                                    <label class="col-md-3 control-label">Fee Varies *</label>

                                    <div class="col-md-7 radio">
                                        <label class="radio-inline">
                                            <input id="appliedAllStd" type="radio" checked name="appliedType"
                                                   value="${com.grailslab.enums.FeeAppliedType.ALL_STD.key}">
                                            ${FeeAppliedType.ALL_STD.value}
                                        </label>
                                        <label class="radio-inline">
                                            <input id="appliedClassStd" type="radio" name="appliedType"
                                                   value="${FeeAppliedType.CLASS_STD.key}">
                                            ${FeeAppliedType.CLASS_STD.value}
                                        </label>
                                    </div>
                                </div>


                            </div>
                        </div>
                    </div>

                    <div class="modal-footer modal-footer-action-btns">
                        <button type="button" class="btn btn-default" data-dismiss="modal"
                                aria-hidden="true">Cancel</button>
                        <button type="submit" id="fees-head-yes-btn"
                                class="btn btn-large btn-primary">Submit</button>
                    </div>

                    <div class="modal-footer modal-refresh-processing" style="display: none;">
                        <i class="fa fa-refresh fa-spin text-center"></i>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>


<script>
    jQuery(function ($) {

        $('.tree').treegrid({
            expanderExpandedClass: 'glyphicon glyphicon-minus',
            expanderCollapsedClass: 'glyphicon glyphicon-plus',
            initialState: 'expanded'
        });
        $('.create-new-btn').click(function (e) {
            $("#accountingHeadModalLabel").html("Fees & stationaries");
            $('#accountingHeadModal').modal('show');
            e.preventDefault();
        });

        $('.extra-btn-1').click(function (e) {
            $('#accountingHeadModal').modal('show');
            e.preventDefault();
        });

        var validator = $('#create-form').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                name: {
                    required: true,
                    maxlength: 200
                },
                code: {
                    required: true,
                    number: true,
                    minlength: 4,
                    min: 1000
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
                    url: "${createLink(controller: 'chart', action: 'saveAccount')}",
                    type: 'post',
                    dataType: "json",
                    data: $("#create-form").serialize(),
                    success: function (data) {
                        if (data.isError == false) {
                            validator.resetForm();
                            showSuccessMsg(data.message);
                            if( $("#id").val().length === 0 ){
                                $(".form-group").removeClass("has-error");
                                $('#name').val("");
                                $('#code').val("");
                            }else {
                                window.location.href = "${g.createLink(controller: 'chart',action: 'fees')}";
                            }
                        } else {
                            showErrorMsg(data.message);
                        }
                    },
                    failure: function (data) {
                    }
                })
            }
        });

        $('#list-table').on('click', 'a.add-reference', function (e) {
            var control = this;
            var refid = $(control).attr('refid');
            var headertext = $(control).attr('headertext');
            validator.resetForm();
            $("#id").val('');
            $("#parentId").val('');
            $('#allowSubHead').prop('checked', false);
            $("#allow-subhead-holder").hide();
            $('#scholarshipHead').prop('checked', false);
            $("#feeTypeHolder").hide();
            $("#parentId").val(refid);
            $(".form-group").removeClass("has-error");
            $("#accountingHeadModalLabel").html(headertext);
            $('#accountingHeadModal').modal('show');
            $("#name").focus();
            e.preventDefault();
        });

        $('#list-table').on('click', 'a.edit-reference', function (e) {
            var control = this;
            var referenceId = $(control).attr('refid');

            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
               url: "${g.createLink(controller: 'chart',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        validator.resetForm();
                        $("#id").val('');
                        $("#parentId").val('');
                        $('#id').val(data.obj.id);
                        $('#name').val(data.obj.name);
                        $('#code').val(data.obj.code);
                        $( "#scholarshipHead" ).prop( "checked", data.obj.scholarshipHead );
                        $('#accountType').val(data.obj.accountType.name);
                        $('#accountType option:not(:selected)').prop('disabled', true);
                        if(data.obj.appliedType.name=='${FeeAppliedType.ALL_STD.key}'){
                            $('#appliedAllStd').prop('checked', true);
                        }else if(data.obj.appliedType.name=='${FeeAppliedType.CLASS_STD.key}'){
                            $('#appliedClassStd').prop('checked', true);
                        }

                        if(data.obj.nodeType.name=='${com.grailslab.enums.NodeType.FIRST.key}'){
                            if(data.obj.displayHead==false){
                                $('#allowSubHead').prop('checked', true);
                            }else {
                                $('#allowSubHead').prop('checked', false);
                            }
                        } else{
                            $('#allowSubHead').prop('checked', false);
                            $("#allow-subhead-holder").hide();
                        }

                        $("#accountingHeadModalLabel").html("Edit Fees header");
                        $('#accountingHeadModal').modal('show');
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
            var control = this;
            var referenceId = $(control).attr('refid');
            var refhaschield = $(control).attr('refhaschield');
            if (refhaschield == 'false') {
                var confirmDel = confirm("Are you sure?");
                if (confirmDel == true) {
                    var control = this;
                    jQuery.ajax({
                        type: 'POST',
                        dataType: 'JSON',
                        data: {id: referenceId},
                        url: "${g.createLink(controller: 'chart',action: 'delete')}",
                        success: function (data, textStatus) {
                            if (data.isError == false) {
                                showSuccessMsg(data.message);
                                window.location.href = "${g.createLink(controller: 'chart',action: 'fees')}";
                            } else {
                                showErrorMsg(data.message);
                            }
                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                        }
                    });
                }
            } else {
                alert('Delete Child First.')
            }
            e.preventDefault();
        });
        $('#accountingHeadModal').on('hide.bs.modal', function (e) {
            window.location.href = "${g.createLink(controller: 'chart',action: 'fees')}";
        });

        $('.print-btn').click(function (e) {
            var printOptionType ="PDF";
            $('#printBtnModal').modal('hide');
            var sectionParam ="${g.createLink(controller: 'accountsReport',action: 'chart','_blank')}?accountType="+"${AccountType.INCOME.key}"+"&printOptionType="+printOptionType;
            window.open(sectionParam);
            e.preventDefault();
        });

    });

</script>
</body>
</html>
