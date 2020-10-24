<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Student Attendance </title>
    <meta name="layout" content="moduleAttandanceLayout"/>
</head>

<body>

<grailslab:breadCrumbActions breadCrumbTitleText="Student Daily Attendance"  SHOW_CREATE_BTN="YES"  createButtonText="Add Attendance" SHOW_EXTRA_BTN1="YES" extraBtn1Text=" Manual Attendance"  SHOW_LINK_BTN="YES" linkBtnText="Update Late Entry"/>
<grailslab:fullModal modalId="updateLateAttModal" modalLabel="Add Late Entry Student">
    <div class="col-md-12">
        <form class="form-horizontal" role="form" id="updateLateAttModalForm">
            <g:hiddenField name="id"/>
            <div id="data_range"  class="form-group">
                <div id="datepicker" class="input-daterange input-group">
                    <span class="input-group-addon">On</span>
                    <input id="startDate" class="input-sm form-control" type="text" value="" required="required"
                           placeholder="Start Date" tabindex="2" name="startDate">
                    <span class="input-group-addon">to</span>
                    <input id="endDate" class="input-sm form-control" type="text" value="" required="required"
                           placeholder="End Date" tabindex="3" name="endDate">
                </div>
            </div>
            <div class="form-group" style="margin-left: -54px;">
                <label for="className" class="col-md-3 control-label">Select Class</label>
                <div class="col-md-7">
                    <g:select class="form-control " id="className" name='className' tabindex="2"
                              noSelection="${['': 'Select Class Name...']}"
                              from='${classList}'
                              optionKey="id" optionValue="name"></g:select>
                </div>
            </div>
        </form>
    </div>
</grailslab:fullModal>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Student Daily Attendance
            </header>
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th>SL</th>
                            <th>Class - Section</th>
                            <th>StdID</th>
                            <th>Name</th>
                            <th>Roll No.</th>
                            <th>In Time</th>
                            <th>Out Time</th>
                            <th>Status</th>
                            <th>Edit | Delete </th>
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
                                <td>${dataSet[5]}</td>
                                <td>${dataSet[6]}</td>
                                <td>${dataSet[7]}</td>

                                <td>
                                    <span class="col-md-2 no-padding"><a href="javascript:void(0)" studentName="${dataSet[3]}"
                                                                         referenceId="${dataSet.DT_RowId}"
                                                                         class="edit-calender-day"
                                                                         title="Edit Attendance"><span
                                                class="fa fa-edit"></span></a></span>

                                        <span class="col-md-2 no-padding">
                                                        <a href="javascript:void(0)"
                                                        referenceId="${dataSet.DT_RowId}"
                                                        class="delete-calender-day"
                                                        title="Delete Attendance">
                                                        <span class="fa fa-trash"></span>
                                                        </a>
                                    </span>
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
<div class="modal fade" id="mainModal"  tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
<div class="modal-dialog">
    <div class="modal-content">
        <div class="modal-header">
            <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
            <h4 class="modal-title" id="myModalLabel">Mannual Attendance Entry</h4>
        </div>
        <div class="mainModal-confirm publish-result-content">
            <form class="form-horizontal" role="form" id="createFormModal">
                <g:hiddenField name="id" id="stdRecordId"/>
                <div class="modal-body">
                    <div class="row">

                            <div class="form-group">
                                <label for="recordDate" class="col-md-3 control-label">Date</label>
                                <div class="col-md-8">
                                    <input  class="form-control" type="text"  name="recordDate" id="recordDate" placeholder="<g:formatDate date="${new java.util.Date()}" format="dd/MM/yyyy" />"
                                            tabindex="2"/>
                                </div>
                            </div>
                            <div class="form-group">
                                <label for="studentId" class="col-md-3 control-label">Student Name</label>
                                <div class="col-md-8">
                                    <input type="hidden" class="form-control" id="studentId" name="studentId" tabindex="5" />
                                </div>
                            </div>

                        <div class="form-group">
                            <label for="inTime" class="col-md-3 control-label">In  Time</label>
                            <div class="col-md-8">
                                <g:textField id="inTime" class="form-control" name="inTime" required="required"/>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="outTime" class="col-md-3 control-label">Out Time</label>
                            <div class="col-md-8">
                                <g:textField id="outTime" class="form-control" name="outTime"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label for="reason" class="col-md-3 control-label">Comments</label>
                            <div class="col-md-8">
                                <textarea id="reason" name="reason" class="form-control" placeholder="Add your Comments"
                                          tabindex="3"></textarea>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer modal-footer-action-btns">
                    <button type="button" class="btn btn-default" id="dismiss" data-dismiss="modal" aria-hidden="true">Cancel</button>
                    <button type="submit" id="publish-result-yes-btn" class="btn btn-large btn-primary">Submit</button>
                </div>
                <div class="modal-footer modal-refresh-processing" style="display: none;">
                    <i class="fa fa-refresh fa-spin text-center"></i>
                </div>
            </form>
        </div>
        <div class="mainModal-success publish-result-content" style="display: none;">
            <div class="modal-body">
                <div class="row">
                    <div class="col-md-2">
                        <asset:image src="share-modal-icon.jpg" width="60" height="60"/>
                    </div>
                    <div class="col-md-10">
                        <p class="message-content"></p>
                    </div>
                </div>
            </div>
            <div class="modal-footer modal-footer-action-btns">
                <button type="button" class="btn btn-default" data-dismiss="modal" aria-hidden="true">Close</button>
            </div>
        </div>
    </div>
</div>
</div>

<script>
    var recordDate, className, sectionName, printParam,select2Contain;
    jQuery(function ($) {
        var stuAttnList = $('#list-table').DataTable({
            "sDom": "<'row'<'col-md-3 date-holder'><'col-md-2 className-filter-holder'><'col-md-2 section-filter-holder'><'col-md-5'f>r>t<'row'<'col-md-3'l><'col-md-3'i><'col-md-6'p>>",
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'asc'],
            "deferLoading": ${totalCount?:0},
            "sAjaxSource": "${g.createLink(controller: 'attnStudent',action: 'list')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }

                $('td:eq(8)', nRow).html(getActionButtons(nRow, aData));
                return nRow;
            },
            "fnServerParams": function (aoData) {
                aoData.push({"name": "attnRecordDate", "value": $('#attnRecordDate').val()},
                        {"name": "sectionName", "value": $('#filterSection').val()},
                        {"name": "className", "value": $('#filterClassName').val()}
                );
            },
            "aoColumns": [
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                {"bSortable":     false}
            ]
        });
        $('#list-table_wrapper div.date-holder').html('<input id="attnRecordDate" class="form-control attnRecordDateField" placeholder="Enter Date" value="${lastRecord}" type="text" name="attnRecordDate">');
        $('#list-table_wrapper div.className-filter-holder').html('<select id="filterClassName" class="form-control" name="filterClassName"><option value="">All Class</option><g:each in="${classList}" var="className"><option value="${className.id}">${className.name}</option> </g:each></select>');
        $('#list-table_wrapper div.section-filter-holder').html('<select id="filterSection" class="form-control" name="filterSection"><option value="">All Section</option></select>');

        $('.print-btn').click(function (e) {
            e.preventDefault();
            recordDate = $('#attnRecordDate').val();
            className = $('#filterClassName').val();
            sectionName = $('#filterSection').val();
            printParam = "${g.createLink(controller: 'attendanceReport',action: 'stdDailyAttendance','_blank')}?rStartDate="+recordDate+"&className="+className+"&sectionName="+sectionName;
            window.open(printParam);
            return false;
        });

        $('#attnRecordDate').datepicker({
            format: 'dd/mm/yyyy',
            autoclose: true
        });
        $('#attnRecordDate').change(function(e){
            $("#list-table").DataTable().draw(false);
        });
        $('#filterClassName').on('change', function (e) {
            className = $('#filterClassName').val();
            loadSectionOnClassChange(className);
            $('#filterSection').val("").trigger("change");
        });
        $('#filterSection').on('change', function (e) {
            showLoading("#data-table-holder");
            $("#list-table").DataTable().draw(false);
            hideLoading("#data-table-holder");
        });
      $('#recordDate').datepicker({
           format: 'dd/mm/yyyy',
            endDate:'today',
            autoclose: true
        });

        $('#inTime').timepicker({
            defaultTime: '08:00 AM'
        });
        $('#outTime').timepicker({
            defaultTime: ''
        });
        $('#data_range .input-daterange').datepicker({
            keyboardNavigation: false,
            format: 'dd/mm/yyyy',
            forceParse: false,
            autoclose: true
        });

        $('.link-url-btn').click(function (e) {
         $('#myModal .create-success').hide();
         $('#myModal .create-content').show();
        // $('#myModal').modal('show');
         $('#updateLateAttModal').modal('show');
         e.preventDefault();
         });
        $("#updateLateAttModalForm").submit(function(e) {
            $('#updateLateAttModal .modal-footer-action-btns').hide();
            $('#updateLateAttModal .modal-refresh-processing').show();
            jQuery.ajax({
                type:'POST',
                dataType:'JSON',
                data: $("#updateLateAttModalForm").serialize(),
                url: "${g.createLink (controller: 'attnStudent', action:'updateLateEntry')}",
                success: function(data){
                    stuAttnList.draw(false);
                    if(data.isError == true){
                        showErrorMsg(data.message);
                    }else{
                        showSuccessMsg(data.message);
                    }
                }
            });
            $('#updateLateAttModal .modal-footer-action-btns').show();
            $('#updateLateAttModal .modal-refresh-processing').hide();
            $('#updateLateAttModal').modal('hide');
            e.preventDefault();
        });
        $('#studentId').select2({
            placeholder: "Search for a Student [studentId/name/Father Name/mobile",
            allowClear: true,
            minimumInputLength:3,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: "${g.createLink(controller: 'remote',action: 'studentTypeAheadList')}",
                dataType: 'json',
                quietMillis: 250,
                data: function (term, page) {
                    return {
                        q: term ,// search term
                       // attendDate:$('#attnRecordDate').val(),
                        sectionName:$('#filterClassName').val()
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

        function repoFormatResult(repo) {
            var markup = '<div class="row-fluid">' +
                    '<div class="span12">' + repo.name + '</div>' +
                    '</div>';
            return markup;
        }

        function repoFormatSelection(repo) {
            return repo.name;
        }
        $('.create-new-btn').click(function (e) {
            $("#myModalLabel").html("Manual Attendance Entry");
            $("#publish-result-yes-btn").html("Submit");
            $('#id').val("");
            $("#recordDate").datepicker('setDate', null);
            $("#recordDate").css("pointer-events","auto");
            $("#select2-chosen-1").empty().append("Search for a Student [studentId/name/Father Name/mobile");
            $("#s2id_studentId").css("pointer-events","auto");
            $('#reason').val("");

               $('#mainModal .mainModal-success').hide();
               $('#mainModal .mainModal-confirm').show();
               $('#mainModal').modal('show');

         e.preventDefault();
        });

        $('.extra-btn-1').click(function (e) {
            window.location.href = "${g.createLink(controller: 'attnStudent',action: 'manualAttendList', '_blank')}/";

        });

        $("#createFormModal").submit(function() {
            $('#mainModal .modal-footer-action-btns').hide();
            $('#mainModal .modal-refresh-processing').show();
            jQuery.ajax({
                type: 'POST',
                dataType:'JSON',
                data: $("#createFormModal").serialize(),
                url: "${g.createLink(controller: 'attnStudent', action: 'saveIndividualAttendance')}",
                success: function (data) {
                    $('#mainModal .modal-refresh-processing').hide();
                    $('#mainModal .modal-footer-action-btns').show();
                    if(data.isError==true){
                        showErrorMsg(data.message);
                    }else {
                        showSuccessMsg(data.message);
                        var table = $('#list-table').DataTable().ajax.reload();
                    }
                    $('#mainModal .mainModal-confirm').hide(1000);
                    $('#mainModal .mainModal-success').show(1000);
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            $('#mainModal .modal-footer-action-btns').show();
            $('#mainModal .modal-refresh-processing').hide();
            $('#mainModal').modal('hide');
            return false; // avoid to execute the actual submit of the form.
        });

        $('#list-table').on('click', 'a.edit-calender-day', function (e) {
            $('#mainModal .mainModal-success').hide();
            $('#mainModal .mainModal-confirm').show();
            $('#studentId').select2('val', '');
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
             type: 'POST',
             dataType: 'JSON',
             url: "${g.createLink(controller: 'attnStudent',action: 'edit')}?id=" + referenceId,
             success: function (data, textStatus) {
                 $("#myModalLabel").html("Update Manual Attendance Entry");
                 $("#publish-result-yes-btn").html("Update");
                 $('#stdRecordId').val(data.obj.id);
                 var attnDate=data.attnDate;
                 $('#recordDate').datepicker('setDate',new Date(attnDate)).css("pointer-events","none");
                 $("#select2-chosen-1").empty().append(data.studentName);
                 $("#s2id_studentId").css("pointer-events","none");
                 $('#reason').val(data.obj.remarks);
                 if (data.inTime) {
                     var d=  data.inTime;
                     $('#inTime').timepicker('setTime', d);
                 }
                 if (data.outTime) {
                     var dd=  data.outTime;
                     $('#outTime').timepicker('setTime', dd);
                 }

                 $('#mainModal').modal('show');
             },
             error: function (XMLHttpRequest, textStatus, errorThrown) {
             }
             });
            e.preventDefault();
        });

       var updateFormValidator = $('#updateFormModal').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
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
                $.ajax({
                    url: "${createLink(controller: 'attnStudent', action: 'update')}",
                    type: 'post',
                    dataType: "json",
                    data: $("#updateFormModal").serialize(),
                    success: function (data) {
                        if (data.isError == false) {
                            $('#updateModal .modal-refresh-processing').hide();
                           /* $('#updateFormModal').modal('hide');*/
                            showSuccessMsg(data.message);
                        } else {
                            showErrorMsg(data.message);
                        }
                    },
                    failure: function (data) {
                    }
                })
            }
        });

        $('#list-table').on('click', 'a.delete-calender-day', function (e) {
            var confirmStr = "Are you want to delete this attendance ." +
                    "  \n\nClick 'OK to confirm, or click 'Cancel' to stop this action.";
            var selectRow = $(this).parents('tr');
            var control = this;
            var referenceId = $(control).attr('referenceId');
            bootbox.confirm(confirmStr, function(clickAction) {
                if(clickAction) {
                    jQuery.ajax({
                        type: 'POST',
                        dataType: 'JSON',
                        url: "${g.createLink(controller: 'attnStudent',action: 'delete')}?id=" + referenceId,
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
            });
            e.preventDefault();
        });
    });
    function loadSectionOnClassChange(classNameOnchange) {
        var $filterSection = $('#filterSection');
        $filterSection.find('option').remove();
        $filterSection.append('<option value="">All Section</option>');
        if (classNameOnchange.length === 0) {
            return false
        }
        jQuery.ajax({
            type: 'POST',
            dataType: 'JSON',
            url: "${g.createLink(controller: 'remote',action: 'classSectionList')}?className=" + classNameOnchange,
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
        actionButtons += '<span class="col-md-2 no-padding"><a href="" studentName="'+aData[3]+'" referenceId="' + aData.DT_RowId + '" class="edit-calender-day" title="Edit Attendance">';
        actionButtons += '<span class="fa fa-edit"></span></a></span>';
        actionButtons += '<span class="col-md-2 no-padding"><a href="" referenceId="' + aData.DT_RowId + ' " class="delete-calender-day"  title="Delete Attendance">';
        actionButtons += '<span class="fa fa-trash"></span></a></span>';
        return actionButtons;
    }
</script>
</body>
</html>


