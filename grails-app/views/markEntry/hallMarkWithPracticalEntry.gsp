<%@ page import="com.grailslab.enums.AttendStatus;" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="adminLayout"/>
    <title>Add Hall Mark </title>
</head>
<body>
<grailslab:breadCrumbActions firstBreadCrumbUrl="${g.createLink(controller: 'markEntry',action: 'index')}" firstBreadCrumbText="Mark Entry" breadCrumbTitleText="Hall Mark" SHOW_EXTRA_BTN1 = "YES" extraBtn1Text="Delete All" extraBtn1Icon = "fa fa-remove" SHOW_PRINT_BTN="YES" printBtnText="Print"/>
<div class="row" id="create-form-holder">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                <span class="panel-header-title">Class : </span> <span class="panel-header-info">${className},</span>  <span class="panel-header-title">Section : </span> <span class="panel-header-info">${section},</span><span class="panel-header-title"> Term : </span><span class="panel-header-info">${examTerm},</span><span class="panel-header-title"> Subject: </span><span class="panel-header-info">${subject},</span><span class="panel-header-title">Full Mark : </span><span class="panel-header-info">${examSchedule?.hallExamMark} </span><span class="panel-header-title">Exam Date: </span><span class="panel-header-info">${examDate} </span>
            </header>
            <div class="panel-body">
                <form class="form-horizontal" id="create-form">
                    <g:hiddenField name="id" />
                    <g:hiddenField name="examScheduleId" id="examScheduleId" value="${examSchedule?.id}" />
                    <div class="form-group col-md-6">
                        <div class="col-md-12">
                            <g:select class="form-control" id="studentId" name='studentId'
                                      from='${studentList}'
                                      optionKey="id" optionValue="name" required=""></g:select>
                        </div>
                    </div>
                    <div class="form-group col-md-3">
                        <div class="col-md-12">
                            <g:select class="form-control" id="attendStatus" name='attendStatus'
                                      from='${AttendStatus.values()}'
                                      optionKey="key" optionValue="value"></g:select>
                        </div>
                    </div>
                    <div class="form-group col-md-3">
                        <div class="col-md-12">
                            <input type="text" name="markObtain" placeholder="Total" class="form-control" id="markObtain" readonly/>
                        </div>
                    </div>
                    <g:if test="${hallWrittenMark}">
                        <div class="form-group col-md-2">
                            <div class="col-md-12">
                                <input type="number" max="${hallWrittenMark}" required="true" tabindex="1" name="hallWrittenMark" placeholder="${grailsApplication.config.getProperty('app.markentry.hall.input1')}" class="form-control hallMarkInputKeyUp" id="hallWrittenMark"/>
                            </div>
                        </div>
                    </g:if>
                    <g:if test="${hallObjectiveMark}">
                        <div class="form-group col-md-2">
                            <div class="col-md-12">
                                <input type="number" max="${hallObjectiveMark}" required="true" tabindex="2"  name="hallObjectiveMark" placeholder="${grailsApplication.config.getProperty('app.markentry.hall.input2')}" class="form-control hallMarkInputKeyUp" id="hallObjectiveMark"/>
                            </div>
                        </div>
                    </g:if>
                    <g:if test="${hallPracticalMark}">
                        <div class="form-group col-md-2">
                            <div class="col-md-12">
                                <input type="number" max="${hallPracticalMark}"  required="true"  tabindex="3" name="hallPracticalMark" placeholder="${grailsApplication.config.getProperty('app.markentry.hall.input3')}" class="form-control hallMarkInputKeyUp" id="hallPracticalMark"/>
                            </div>
                        </div>
                    </g:if>

                    <g:if test="${hallSbaMark}">
                        <div class="form-group col-md-2">
                            <div class="col-md-12">
                                <input type="number" max="${hallSbaMark}"  required="true"  tabindex="4" name="hallSbaMark" placeholder="${grailsApplication.config.getProperty('app.markentry.hall.input4')}" class="form-control hallMarkInputKeyUp" id="hallSbaMark"/>
                            </div>
                        </div>
                    </g:if>
                    <g:if test="${hallInput5}">
                        <div class="form-group col-md-2">
                            <div class="col-md-12">
                                <input type="number" max="${hallInput5}" required="true" tabindex="5" name="hallInput5" placeholder="${grailsApplication.config.getProperty('app.markentry.hall.input5')}" class="form-control hallMarkInputKeyUp" id="hallInput5"/>
                            </div>
                        </div>
                    </g:if>

                    <div class="form-group col-md-3" >
                        <div class="col-md-12">
                            <button id="hall-mark-submit-btn" class="btn btn-primary" tabindex="6" type="submit">Add Mark</button>
                        </div>
                    </div>
                </form>

            </div>
        </section>
    </div>
</div>

<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th class="col-md-1">STD-ID</th>
                            <th class="col-md-2">Student Name</th>
                            <th class="col-md-1">ROLL</th>
                            <th class="col-md-1">${grailsApplication.config.getProperty('app.markentry.hall.input1')}</th>
                            <th class="col-md-1">${grailsApplication.config.getProperty('app.markentry.hall.input2')}</th>
                            <th class="col-md-1">${grailsApplication.config.getProperty('app.markentry.hall.input3')}</th>
                            <th class="col-md-1">${grailsApplication.config.getProperty('app.markentry.hall.input4')}</th>
                            <th class="col-md-1">${grailsApplication.config.getProperty('app.markentry.hall.input5')}</th>
                            <th class="col-md-1">Hall Mark</th>
                            <th class="col-md-1">Action</th>
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
                                <td>${dataSet[8]}</td>
                                <td>
                                    <span class="col-md-4 no-padding"><a href="" referenceId="${dataSet.DT_RowId}" class="edit-reference" title="Edit"><span class="glyphicon glyphicon-edit"></span></a></span>
                                    <span class="col-md-4 no-padding"><a href="" referenceId="${dataSet.DT_RowId}" class="delete-reference" title="Delete"><span class="glyphicon glyphicon-trash"></span></a></span>

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

<script type="application/javascript">
    var hallWritten, hallPractical, hallObjective,hallSba,hallInput, totalMark, numOfStudent, reloadUrl;
    jQuery(function ($) {
        isHallWrittenExist = "${hallWrittenMark ? true: false}";
        reloadUrl = "${createLink(controller: 'markEntry', action: 'addHallMark')}/" + ${examSchedule?.id};

        $('.hallMarkInputKeyUp').keyup(function () {
            var $markObtain = $('#markObtain');
            hallWritten = $('#hallWrittenMark').val() ? $('#hallWrittenMark').val() : 0;
            hallPractical = $('#hallPracticalMark').val()? $('#hallPracticalMark').val() : 0;
            hallObjective = $('#hallObjectiveMark').val()? $('#hallObjectiveMark').val() : 0;
            hallSba = $('#hallSbaMark').val()? $('#hallSbaMark').val() : 0;
            hallInput = $('#hallInput5').val()? $('#hallInput5').val() : 0;
            if(jQuery.isNumeric(this.value) && this.value >= 0) {
                totalMark = math.chain(hallWritten).add(hallPractical).add(hallObjective).add(hallSba).add(hallInput).done();
                $markObtain.val(totalMark);
            }
        });
        if (isHallWrittenExist === "true") {
            $("#hallWrittenMark").focus();
        } else {
            $("#hallObjectiveMark").focus();
        }

        $('#attendStatus').on('change', function (e) {
            var attendStatus = $('#attendStatus').val();
            if(attendStatus != "${AttendStatus.PRESENT.key}"){
                $('#markObtain').val('0');
                $('#hallWrittenMark').val('0');
                $('#hallPracticalMark').val('0');
                $('#hallObjectiveMark').val('0');
                $('#hallSbaMark').val('0');
                $('#hallInput5').val('0');
            } else{
                $('#markObtain').val('');
                $('#hallWrittenMark').val('');
                $('#hallPracticalMark').val('');
                $('#hallObjectiveMark').val('');
                $('#hallSbaMark').val('');
                $('#hallInput5').val('');
            }
        });
        var $markTable = $('#list-table').dataTable({
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "deferLoading":  ${totalCount?:0},
            "sServerMethod": "POST",
            "sAjaxSource": "${g.createLink(controller: 'markEntry',action: 'listHallMark')}",
            "fnServerParams": function ( aoData ) {
                aoData.push( { "name": "examScheduleId", "value": ${examSchedule?.id}});
            },
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if(aData.DT_RowId ==undefined){
                    return true;
                }
                $('td:eq(9)', nRow).html(getActionButtons(nRow, aData));
                return nRow;
            },
            "order": [[ 2, "desc" ]],
            "aoColumns": [
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                { "bSortable": false }
            ]
        });

        $('#create-form').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
            highlight: function (e) {
                $(e).closest('.form-group').removeClass('has-info').addClass('has-error');
            },
            success: function (e) {
                $(e).closest('.form-group').removeClass('has-error').addClass('has-info');
                $(e).remove();
            },
            submitHandler: function (form) {
                showLoading("#create-form-holder");
                $.ajax({
                    url: "${createLink(controller: 'markEntry', action: 'saveHallMark')}",
                    type: 'post',
                    dataType: "json",
                    data: $("#create-form").serialize(),
                    success: function (data) {
                        if(data.isError==true){
                            showErrorMsg(data.message);
                        }else {
                            $('#studentId :selected').remove();
                            numOfStudent = $('#studentId option[value!=""]').length;
                            if (numOfStudent ===0){
                                window.location.href = reloadUrl;
                            }else {
                                $('#attendStatus').val("PRESENT");
                                $('#markObtain').val('');
                                $('#hallWrittenMark').val('');
                                $('#hallPracticalMark').val('');
                                $('#hallObjectiveMark').val('');
                                $('#hallSbaMark').val('');
                                $('#hallInput5').val('');
                                $('#id').val('');
                                if (isHallWrittenExist === "true") {
                                    $("#hallWrittenMark").focus();
                                } else {
                                    $("#hallObjectiveMark").focus();
                                }
                            }
                            $markTable.DataTable().ajax.reload();
                        }
                        hideLoading("#create-form-holder");
                    },
                    failure: function (data) {
                    }
                })
            }
        });

        $(".cancel-btn").click(function(){
            window.location.href = reloadUrl;
        });
        $('.print-btn').click(function (e) {
            e.preventDefault();
            var printOptionType = 'PDF';
            var sectionParam ="${g.createLink(controller: 'markEntry',action: 'examMarkPrint','_blank')}?printOptionType="+printOptionType+"&examSchedule="+ "${examSchedule?.id}&inputType=hallMark";
            window.open(sectionParam);
            return false; // avoid to execute the actual submit of the form.
        });

        $('#list-table').on('click', 'a.edit-reference', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'markEntry',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        $('#id').val(data.obj.id);
                        $('#studentId').append('<option value="' + data.obj.student.id + '" selected="selected">' + data.studentID + '</option>');
                        $('#attendStatus').val(data.obj.hallAttendStatus.name);
                        $('#markObtain').val(data.obj.hallObtainMark);
                        $('#hallWrittenMark').val(data.obj.hallWrittenMark);
                        $('#hallPracticalMark').val(data.obj.hallPracticalMark);
                        $('#hallObjectiveMark').val(data.obj.hallObjectiveMark);
                        $('#hallSbaMark').val(data.obj.hallSbaMark);
                        $('#hallInput5').val(data.obj.hallInput5);
                        $('#create-form').show(500);
                        $("#hallWrittenMark").focus();
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
                showLoading("#list-table");
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'markEntry',action: 'delete')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        hideLoading("#list-table");
                        if (data.isError == false) {
                            showSuccessMsg(data.message);
                            window.location.href = reloadUrl;
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

        // extra-btn-1 -> is the class name for Delete All button
        $('.extra-btn-1').click(function (e) {
            var confirmDel = confirm("Are you sure delete all mark entry? This action can't undone");
            if (confirmDel == true) {
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'examMark',action: 'deleteHallMark')}?id=" + ${examSchedule.id},
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            showSuccessMsg(data.message);
                            window.location.href = reloadUrl;
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

    function getActionButtons(nRow, aData) {
        var actionButtons = "";
        actionButtons += '<span class="col-md-4 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="edit-reference" title="Edit">';
        actionButtons += '<span class="glyphicon glyphicon-edit"></span>';
        actionButtons += '</a></span>';
        actionButtons += '<span class="col-md-4 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="delete-reference" title="Delete">';
        actionButtons += '<span class="glyphicon glyphicon-trash"></span>';
        actionButtons += '</a></span>';
        return actionButtons;
    }
</script>
</body>
</html>