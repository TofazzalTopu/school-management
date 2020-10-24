<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Member Entry</title>
    <meta name="layout" content="moduleLibraryLayout"/>
</head>
<body>

<grailslab:breadCrumbActions SHOW_CREATE_BTN="YES" createButtonText="Add New" controllerName="${params.controller}"/>

<div class="modal fade" id="myModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <form class="form-horizontal" role="form" id="modalForm">
                <g:hiddenField name="objId" id="objId"/>
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                    <h4 class="modal-title" id="myModalLabel">Library Member Registration</h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="form-group">
                            <label for="referenceId" class="col-md-3 control-label"  >Reference Name </label>
                            <div class="col-md-7">
                                <input type="hidden" class="form-control" id="referenceId" name="referenceId" tabindex="5"  />
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="name" class="col-md-3 control-label">Name</label>
                            <div class="col-md-7">
                                <input class="form-control" type="text" name="name"  id="name" placeholder="Member Name"
                                       tabindex="2"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="mobile" class="col-md-3 control-label">Mobile Number</label>
                            <div class="col-md-7">
                                <input class="form-control" type="text" id="mobile" name="mobile" class="form-control"  placeholder="phone number"
                                       tabindex="3"></textarea>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="address" class="col-md-3 control-label">Permanent Address</label>
                            <div class="col-md-7">
                                <g:textArea class="form-control" id="address" tabindex="3" name="address" placeholder="Address" rows="2"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="address" class="col-md-3 control-label">Present Address</label>
                            <div class="col-md-7">
                                <g:textArea class="form-control" id="presentAddress" tabindex="3" name="presentAddress" placeholder="Present Address" rows="2"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="email" class="col-md-3 control-label">Email</label>
                            <div class="col-md-7">
                                <input class="form-control" type="text" id="email" name="email" class="form-control"  placeholder="email"
                                       tabindex="3"></input>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="address" class="col-md-3 control-label">Membership Date</label>
                            <div class="col-md-7">
                                <input  class="form-control" type="text"  name="memberShipDate" id="memberShipDate" placeholder="<g:formatDate date="${new java.util.Date()}" format="dd/MM/yyyy" />"
                                        tabindex="2"/>
                            </div>
                        </div>

                    </div>

                </div>
                <div class="modal-footer">
                    <button class="btn  btn-default" data-dismiss="modal" aria-hidden="true">Cancel</button>
                    <button type="button" class="btn btn-primary" id="libraryMemberBtn">Save</button>
                </div>
            </form>
        </div>
    </div>
</div>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Library Member List
            </header>

            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th >SL</th>
                            <th >Name</th>
                            <th>Membership ID</th>
                            <th>Reference ID</th>
                            <th>Mobile No.</th>
                            <th>Present Address</th>
                            <th>Membership Date</th>
                            <th>Action</th>
                        </tr>
                        </thead>
                    </table>
                </div>
            </div>
        </section>
    </div>
</div>

<script>
    jQuery(function ($) {
        var memberList = $('#list-table').DataTable({
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "sAjaxSource": "${g.createLink(controller: 'libraryMember',action: 'list')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(7)', nRow).html(getGridActionBtns(nRow, aData, 'edit,delete'));
                return nRow;
            },
            "aoColumns": [
                {"bSortable": false},
                null,
                {"bSortable": false},
                {"bSortable": false},
                {"bSortable": false},
                {"bSortable": false},
                {"bSortable": false},
                {"bSortable": false}
            ]
        });
        $('#memberShipDate').datepicker({
            format: 'dd/mm/yyyy',
            default: new Date(),
            setData: new Date(),
            autoclose: true
        });
        $(document).on('click', '#libraryMemberBtn', function (e) {
            e.preventDefault();
            showLoading('#create-form-holder')
            $.ajax({
                url: "${createLink(controller: 'libraryMember', action: 'save')}",
                type: 'post',
                dataType: "json",
                data: $("#modalForm").serialize(),
                success: function (data) {
                    hideLoading('#create-form-holder')
                    if (data.isError == false) {
                        memberList.draw(false);
                        showSuccessMsg(data.message);

                        $('#objId').val('');
                        $('#name').val('');
                        $('#address').val('');
                        $('#presentAddress').val('');
                        $('#mobile').val('');
                        $('#email').val('');
                        $('#memberShipDate').val('');
                        $('#myModal').modal('hide');

                    //    $('#myModal').hide();
                    } else {
                        showErrorMsg(data.message);
                    }

                },
                failure: function (data) {
                }
            })
            return false;
        });

        $('#referenceId').select2({
            placeholder: "Search for a student [name]",
            allowClear: true,
            minimumInputLength:3,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: "${g.createLink(controller: 'remote',action: 'studentTypeAheadListWithStdId')}",
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
        $('#referenceId').on('change', function (e) {
          //  var referenceId = $(control).attr('referenceId');
          var  stdentId =$('#referenceId').val();
           jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'libraryMember',action: 'getInfoFromStudent')}?stdId=" + stdentId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        $('#referenceId').val();
                        $('#name').val(data.memberName);
                        $('#address').val(data.memberAddress);
                        $('#presentAddress').val(data.permanentAddress);
                        $('#mobile').val(data.memberMobile);
                        $('#email').val(data.memberEmail);
                        $('#myModal .create-content').show();
                    } else {
                        alert(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            e.preventDefault();
        });

        $('.create-new-btn').click(function (e) {
            $('#myModal').modal('hide');
            $('#myModal').modal('show');
            e.preventDefault();
        });

        $(".cancel-btn").click(function () {
            var validator = $("#modalForm").validate();
            validator.resetForm();
            $("#hiddenId").val('');
            $('#myModal').modal('hide');
        });

        $('#list-table').on('click', 'a.edit-reference', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'libraryMember',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        $("#libraryMemberBtn").html("Edit");
                        $("#objId").val(data.obj.id);
                        $("#select2-chosen-1").empty().append(data.studentName);
                        $("#s2id_referenceId").css("pointer-events","none");
                        $('#referenceId').val(data.obj.referenceId);
                        $('#name').val(data.obj.name);
                        $('#address').val(data.obj.address);
                        $('#presentAddress').val(data.obj.presentAddress);
                        $('#mobile').val(data.obj.mobile);
                        $('#email').val(data.obj.email);
                        $('#memberShipDate').datepicker('setDate', new Date(data.obj.memberShipDate));
                        $('#myModal').modal('show');
                    } else {
                        alert(data.message);
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
                    url: "${g.createLink(controller: 'libraryMember',action: 'delete')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            memberList.draw(false);
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
    });

</script>
</body>
</html>