<%@ page import="com.grailslab.enums.GroupName" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Student subject</title>
    <meta name="layout" content="moduleStdMgmtLayout"/>
</head>

<body>
<grailslab:breadCrumbActions firstBreadCrumbUrl="${g.createLink(controller: 'classSubjects',action: 'index')}" firstBreadCrumbText="Class List" breadCrumbTitleText="Subject List" SHOW_CREATE_BTN="YES" createButtonText="Add New Subject" SHOW_EXTRA_BTN1="YES" extraBtn1Text="Edit All Subject " SHOW_PRINT_BTN="YES"/>

<div class="row" >
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                <span class="panel-header-title">Class: </span> <span class="panel-header-info">${className.name} </span>
            </header>
            <div class="panel-body">
                <div class="table-responsive">
                    <table class="table table-striped table-hover table-bordered" id="list-table">
                        <thead>
                        <tr>
                            <th>SL</th>
                            <th>Subject Name</th>
                            <th>Type</th>
                            <th>CT Exam</th>
                            <th>CT Effective</th>
                            <th>Hall Exam</th>
                            <th>Hall Effective</th>
                            <th>Tabulation Mark</th>
                            <th>Action</th>
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
                               %{-- <td>${dataSet[7]}</td>--}%

                                <td>
                                    <span class="col-md-6 no-padding"><a href="" referenceId="${dataSet.DT_RowId}"
                                                                         class="edit-reference" title="Edit"><span
                                                class="green glyphicon glyphicon-edit"></span></a></span>
                                    <span class="col-md-6 no-padding"><a href="" referenceId="${dataSet.DT_RowId}"
                                                                         class="delete-reference"
                                                                         title="Delete"><span
                                                class="green glyphicon glyphicon-trash"></span></a></span>
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

<!-- Modal -->
<div class="modal fade" id="classSubjectModal"  tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <form class="form-horizontal" role="form" id="createFormModal">
                <g:hiddenField name="id" id="subjectsId"/>
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                    <h4 class="modal-title" id="myModalLabel">Add Subject</h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="form-group">
                            <label class="col-md-4 control-label">Class Name</label>
                            <div class="col-md-6">
                                <label class="control-label">: <strong>${className?.name}</strong></label>
                            </div>
                        </div>
                        <g:if test="${className.groupsAvailable}">
                            <div class="form-group">
                                <label for="groupName" class="col-md-4 control-label">Group</label>
                                <div class="col-md-6" id="groupNameHolder">
                                    <g:select class=" form-control" id="groupName" name='groupName' tabindex="1"
                                              noSelection="${['': 'All Group...']}"
                                              from="${GroupName.values()}"
                                              optionKey="key" optionValue="value">
                                    </g:select>
                                </div>
                            </div>
                        </g:if>
                        <div class="form-group">
                            <label for="subject" class="col-md-4 control-label">Subject *</label>
                            <div class="col-md-6" id="subjectDDHolder">
                                <g:select class=" form-control" id="subject" name='subject' tabindex="1"
                                          noSelection="${['': 'Select Subject...']}"
                                          from="${subjectList}"
                                          optionKey="id" optionValue="name" required="required">
                                </g:select>
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="subjectType" class="col-md-4 control-label">Subject Type</label>
                            <div class="col-md-6">
                                <g:select class=" form-control" id="subjectType" name='subjectType' tabindex="1"
                                          from="${subjectTypeList}"
                                          optionKey="key" optionValue="value">
                                </g:select>
                            </div>
                        </div>
                        <div class="form-group" id="alternativeSubHolder" style="display: none;">
                            <label for="subject" class="col-md-4 control-label">Available Subjects</label>
                            <div class="col-md-6">
                                <select multiple name="alternativeSubIds" id="alternativeSubIds" class="form-control"></select>
                            </div>
                        </div>
                        <div class="form-group" id="tabu-mark-holder">


                            <div class="form-group">
                                <label class="col-md-4 control-label">Tabulation Effective Mark (%) *</label>
                                <div class="col-md-3">
                                    <input type="text" class="addNumeric form-control" id="weightOnResult" name='weightOnResult' placeholder="Effective Mark">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">Short Order *</label>
                                <div class="col-md-3">
                                    <input type="text" class="form-control addNumeric" id="sortOrder" name='sortOrder' placeholder="Short Order">
                                </div>
                            </div>
                            <div class="form-group">
                                <label class="col-md-4 control-label">Minimum Pass Mark</label>
                                <div class="col-md-3">
                                    <input type="text" class=" form-control addNumeric" id="passMark" name='passMark' placeholder="Pass Mark">
                                </div>
                            </div>
                        </div>

                        <div  class="form-group">
                            <div class="col-md-6 col-md-offset-4">
                                <div class="padding-left-20px">
                                    <label class="checkbox">
                                        <input type="checkbox" id="isExtracurricular" name="isExtracurricular">Is Extra Curricular? (No Pass/Fail)
                                    </label>
                                    <label class="checkbox">
                                        <input type="checkbox" id="isOtherActivity" name="isOtherActivity"> Only Final Term mark input?
                                    </label>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <div class="col-md-10 col-md-offset-1 table-responsive">
                                <table class="table table-bordered table-condensed table-marks">
                                    <thead>
                                    <tr>
                                        <td colspan="3">CT exam Mark Distribution (For each exam Term)</td>
                                        <td colspan="2">
                                            <label class="checkbox-inline">
                                            <input type="checkbox" id="isCtExam" name="isCtExam"> CT Exam?
                                        </label>
                                        </td>

                                    </tr>
                                    <tr>
                                        <td></td>
                                        <td>CT 1 Exam</td>
                                        <td>CT 2 Exam</td>
                                        <td>CT 3 Exam</td>
                                        <td>Effective (%)</td>
                                    </tr>
                                    </thead>
                                    <tbody>
                                    <tr>
                                        <td>
                                            Exam Mark
                                        </td>
                                        <td>
                                            <input type="text" value="0" name="ctMark" placeholder="CT 1 Exam Mark" id="ctMark" class="form-control addNumeric">
                                        </td>
                                        <td>
                                            <input type="text" value="0" name="ctMark2" placeholder="CT 2 Exam Mark" id="ctMark2" class="form-control addNumeric">
                                        </td>
                                        <td>
                                            <input type="text" value="0" name="ctMark3" placeholder="CT 3 Exam Mark" id="ctMark3" class="form-control addNumeric">
                                        </td>
                                        <td>
                                            <input type="text" value="0" name="ctEffMark" id="ctEffMark" class="form-control addNumeric">
                                        </td>
                                    </tr>
                                    <tr>
                                        <td colspan="5">Hall exam Mark Distribution (For each exam Term)</td>
                                    </tr>
                                    <tr>
                                        <td colspan="2">Hall Exam</td>
                                        <td colspan="2">Effective (%)</td>
                                        <td>&nbsp;</td>
                                    </tr>
                                    <tr>
                                        <td colspan="2"><input type="text" value=""  step="1" name="hallMark" placeholder="Exam Mark" id="hallMark" class="form-control addNumeric summationOfExamMark"></td>
                                        <td colspan="2"><input type="text" value="100"  step="1" name="hallEffMark" id="hallEffMark" class="form-control addNumeric"></td>
                                        <td>
                                            <input type="checkbox" id="isHallPractical" name="isHallPractical"> Is Detail Mark Input
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>

                        <div class="form-group" id="hall-mark-holder">
                            <div class="col-md-10 col-md-offset-1 table-responsive" id="hall-mark-distribution" style="display: none;">
                                <table class="table table-bordered table-condensed table-marks">

                                <thead>
                                <tr><td colspan="100">Detail Mark Distribution <label class="pull-right"><input  type="checkbox" id="isPassSeparately" name="isPassSeparately"> Is Passing Mark Separately</label></td></tr>
                                <tr>
                                    <td></td>
                                    <td>${grailsApplication.config.getProperty("app.markentry.hall.input1")}</td>
                                    <td>${grailsApplication.config.getProperty("app.markentry.hall.input2")}</td>
                                    <td>${grailsApplication.config.getProperty("app.markentry.hall.input3")}</td>
                                    <td>${grailsApplication.config.getProperty("app.markentry.hall.input4")}</td>
                                    <td>${grailsApplication.config.getProperty("app.markentry.hall.input5")}</td>
                                </tr>
                                </thead>
                                    <tbody>
                                    <tr>
                                        <td>
                                           Exam Mark
                                        </td>
                                        <td>
                                            <input type="text"  step="1" name="hallWrittenMark" id="hallWrittenMark" value="0" class="form-control addNumeric num1 examMarkOfSubject">
                                        </td>
                                        <td>
                                            <input type="text" step="1" name="hallObjectiveMark" id="hallObjectiveMark" value="0" class="form-control addNumeric num2 examMarkOfSubject">
                                        </td>
                                        <td>
                                            <input type="text"  step="1" name="hallPracticalMark" id="hallPracticalMark" value="0" class="form-control addNumeric num3 examMarkOfSubject">
                                        </td>
                                        <td>
                                            <input type="text"  step="1" name="hallSbaMark" id="hallSbaMark" value="0" class="form-control addNumeric num4 examMarkOfSubject">
                                        </td>
                                        <td>
                                            <input type="text"  step="1" name="hallInput5" id="hallInput5" value="0" class="form-control addNumeric num5 examMarkOfSubject">
                                        </td>
                                    </tr>

                                    <tr class="addLabelBeforeRow" id="pass-mark-distribution" style="display: none;">
                                        <td>
                                           Pass Mark
                                        </td>
                                        <td>
                                            <input type="text"  name="writtenPassMark" id="writtenPassMark" class="form-control addNumeric" placeholder="Pass Mark">
                                        </td>
                                        <td>
                                            <input type="text" name="objectivePassMark" id="objectivePassMark" class="form-control addNumeric" placeholder="Pass Mark">
                                        </td>
                                        <td>
                                            <input type="text"  name="practicalPassMark" id="practicalPassMark" class="form-control addNumeric" placeholder="Pass Mark">
                                        </td>
                                        <td>
                                            <input type="text"  name="sbaPassMark" id="sbaPassMark" class="form-control addNumeric" placeholder="Pass Mark">
                                        </td>
                                        <td>
                                            <input type="text"  name="input5PassMark" id="input5PassMark" class="form-control addNumeric" placeholder="Pass Mark">
                                        </td>
                                    </tr>
                                    </tbody>
                                </table>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button class="btn  btn-default" data-dismiss="modal" aria-hidden="true">Cancel</button>
                    <button type="submit" class="btn btn-primary" id="submitButton">Save changes</button>
                </div>
            </form>
        </div>
    </div>
</div>
<script>
    var allowOptionalSubject, subjectId, fullMark, ctEffMark, hallEffMark, className, printParam, optionSelected;
    jQuery(function ($) {
        $('.addNumeric').numeric();
        $("#alternativeSubIds").select2({
            placeholder: "Select One or More Subject",
            allowClear: true
        });

        $('#list-table').DataTable({
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'asc'],
            "deferLoading": ${totalCount?:0},
            "fnServerParams": function ( aoData ) {
                aoData.push( { "name": "classId", "value": ${className?.id} } );
            },
            "sAjaxSource": "${g.createLink(controller: 'classSubjects', action: 'list')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(8)', nRow).html(getActionButtons(nRow, aData));
                return nRow;
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
                { "bSortable": false }
            ]
        });


        $('#hasAlternative').click(function(){
            if (this.checked) {
                $('#alternativeSubHolder').show();
            } else {
                $('#alternativeSubHolder').hide();
            }
        });


        $('#isHallPractical').click(function(){
            if (this.checked) {
                $('#hall-mark-distribution').show();
            } else {
                $('#hall-mark-distribution').hide();
            }
            document.getElementById("hallMark").readOnly = true;
        });

        $('#isPassSeparately').click(function(){
            if (this.checked) {
                $('#pass-mark-distribution').show();
            } else {
                $('#pass-mark-distribution').hide();
            }
        });

        $("#createFormModal").submit(function() {
            jQuery.ajax({
                type: 'POST',
                dataType:'JSON',
                data: $("#createFormModal").serialize(),
                url: "${g.createLink(controller: 'classSubjects', action: 'saveSubject')}?className="+"${className?.id}",
                success: function (data) {
                    if(data.isError==true){
                        showErrorMsg(data.message);
                    }else {
                        subjectId =$('#subjectsId').val();
                        if(subjectId != undefined && subjectId !=''){
                            $('#classSubjectModal').modal('hide');
                        }
                        $("#subject option:selected").remove();
                        showSuccessMsg(data.message);
                        var table = $('#list-table').DataTable().ajax.reload();
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            return false; // avoid to execute the actual submit of the form.
        });

        $('#subject').change(function () {
            $('#subjectType').val("COMPULSORY").trigger("change");
        });
        $('#subjectType').change(function () {
            var optionSelected = $(this).find("option:selected").val();
            if(optionSelected === "ALTERNATIVE"){
                var subjectName = $('#subject').val();
                if (subjectName) {
                    jQuery.ajax({
                        type: 'POST',
                        dataType: 'JSON',
                        url: "${g.createLink(controller: 'classSubjects',action: 'alternativeSubjectList')}?id=" + subjectName,
                        success: function (data, textStatus) {
                            if (data.isError == false) {
                                var $selectMulti = $("#alternativeSubIds").select2();
                                $selectMulti.find('option').remove();
                                $.each(data.subjectList, function (key, subject) {
                                    $selectMulti.append('<option value=' + subject.id + '>' + subject.name + '</option>');
                                });
                                $selectMulti.val(data.selectedList).trigger("change");
                                $('#alternativeSubHolder').show();
                            } else {
                                $('#subjectType').val("COMPULSORY");
                                showErrorMsg(data.message);
                            }
                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                        }
                    });
                } else {
                    showErrorMsg("Please select subject first");
                }
            } else {
                $('#alternativeSubHolder').hide();
            }
        });
        $('#groupName').change(function (e) {
            var optionSelected = $(this).find("option:selected").val();
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'classSubjects',action: 'availableSubjectsToAdd')}?id=${className.id}&groupName=" + optionSelected,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        var $selectSub = $('#subject');
                        $selectSub.find('option').remove();
                        $selectSub.append('<option value="">Select Subject</option>');
                        $.each(data.subjectList, function (key, value) {
                            $selectSub.append('<option value=' + value.id + '>' + value.name + '</option>');
                        });
                    } else {
                        showErrorMsg(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            e.preventDefault();
        });


        $('.create-new-btn').click(function (e) {
            $('#classSubjectModal').modal('show');
            e.preventDefault();
        });

        $('#list-table').on('click', 'a.edit-reference', function (e) {
            var control = this;
            var referenceId = $(control).attr('referenceId');
            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'classSubjects',action: 'editSubject')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        clearForm('#createFormModal');
                        $('#subjectsId').val(data.obj.id);
                        $('#className').val(data.obj.className.id);
                        var select = $('#subject');
                        $('option', select).remove();
                        select.append('<option value="' + data.obj.subject.id + '" selected="selected">' + data.subjectName + '</option>');
//                        $('#fullMark').val(data.obj.fullMark);
                        $('#ctMark').val(data.obj.ctMark);
                        $('#ctMark2').val(data.obj.ctMark2);
                        $('#ctMark3').val(data.obj.ctMark3);
                        $('#ctEffMark').val(data.obj.ctEffMark);
                        $('#hallMark').val(data.obj.hallMark);
                        $('#hallEffMark').val(data.obj.hallEffMark);
                        $('#weightOnResult').val(data.obj.weightOnResult);
                        $('#sortOrder').val(data.obj.sortOrder);
                        $('#passMark').val(data.obj.passMark);
                        $( "#isExtracurricular" ).prop( "checked", data.obj.isExtracurricular );
                        $( "#isOtherActivity" ).prop( "checked", data.obj.isOtherActivity );
                        $( "#isCtExam" ).prop( "checked", data.obj.isCtExam );
                        if(data.obj.groupName !=''){
                            $('#groupName').val(data.obj.groupName?data.obj.groupName.name:'');
                        }
                        if(data.obj.subjectType !=''){
                            $('#subjectType').val(data.obj.subjectType.name);
                            if (data.obj.subjectType.name === 'ALTERNATIVE'){
                                var $selectMulti = $("#alternativeSubIds").select2();
                                $selectMulti.find('option').remove();
                                $.each(data.alternativeSubjectList, function (key, subject) {
                                    $selectMulti.append('<option value=' + subject.id + '>' + subject.name + '</option>');
                                });
                                $selectMulti.val(data.selectedSubjectList).trigger("change");
                                $('#alternativeSubHolder').show();
                            }
                        }
                        $( "#isHallPractical" ).prop( "checked", data.obj.isHallPractical );
                        if(data.obj.isHallPractical ===true){
                            $('#hallWrittenMark').val(data.obj.hallWrittenMark);
                            $('#hallPracticalMark').val(data.obj.hallPracticalMark);
                            $('#hallObjectiveMark').val(data.obj.hallObjectiveMark);
                            $('#hallSbaMark').val(data.obj.hallSbaMark);
                            $('#hallInput5').val(data.obj.hallInput5);
                            $('#hall-mark-distribution').show();
                        } else {
                            $('#hall-mark-distribution').hide();
                        }
                        $( "#isPassSeparately" ).prop( "checked", data.obj.isPassSeparately );
                        if(data.obj.isPassSeparately ===true){
                            $('#writtenPassMark').val(data.obj.writtenPassMark);
                            $('#objectivePassMark').val(data.obj.objectivePassMark);
                            $('#practicalPassMark').val(data.obj.practicalPassMark);
                            $('#sbaPassMark').val(data.obj.sbaPassMark);
                            $('#input5PassMark').val(data.obj.input5PassMark);
                            $('#pass-mark-distribution').show();
                        } else {
                            $('#pass-mark-distribution').hide();
                        }
                        $('#classSubjectModal').modal('show');
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
                    url: "${g.createLink(controller: 'classSubjects',action: 'inactive')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            showSuccessMsg(data.message);
                            window.location.href = "${g.createLink(controller: 'classSubjects',action: 'subjects', params: [id:className?.id])}";
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
        $('#classSubjectModal').on('hide.bs.modal', function (e) {
            window.location.href = "${g.createLink(controller: 'classSubjects',action: 'subjects', params: [id:className?.id])}";
        });
        $('.print-btn').click(function (e) {
            e.preventDefault();
             className = "${className?.id}";
             printParam = "${g.createLink(controller: 'studentReport',action: 'classSubject','_blank')}/"+className;
            window.open(printParam);
            return false;
        });

        $('.extra-btn-1').click(function (e) {
            e.preventDefault();
            className = "${className?.id}";
            window.location.href = "${g.createLink(controller: 'classSubjects',action: 'editAllSubject', '_blank')}/"+className;

        });

    });

    /*---------------------------------------------------------------
                Auto summation of input field "Hall Mark"
     ----------------------------------------------------------------*/

        $(".summationOfExamMark").val("");
        $(".examMarkOfSubject").val("");

        function addHallMark() {
            var $num1 = ($(".num1").val() != "" && !isNaN($(".num1").val())) ? Math.round($(".num1").val()) : 0;
            var $num2 = ($(".num2").val() != "" && !isNaN($(".num2").val())) ? Math.round($(".num2").val()) : 0;
            var $num3 = ($(".num3").val() != "" && !isNaN($(".num3").val())) ? Math.round($(".num3").val()) : 0;
            var $num4 = ($(".num4").val() != "" && !isNaN($(".num4").val())) ? Math.round($(".num4").val()) : 0;
            var $num5 = ($(".num5").val() != "" && !isNaN($(".num5").val())) ? Math.round($(".num5").val()) : 0;
            $(".summationOfExamMark").val($num1 + $num2 + $num3 + $num4 + $num5);
        }
        $(".examMarkOfSubject").keyup(function(){
            addHallMark();
        });
    /*--------------------------------------------------------------------*/

    function getActionButtons(nRow, aData) {
        var actionButtons = "";
        actionButtons += '<span class="col-md-6 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="edit-reference" title="Edit">';
        actionButtons += '<span class="green glyphicon glyphicon-edit">';
        actionButtons += '</a></span>';
        actionButtons += '<span class="col-md-6 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="delete-reference" title="Delete">';
        actionButtons += '<span class="red glyphicon glyphicon-trash"></span>';
        actionButtons += '</a></span>';
        return actionButtons;
    }

</script>
</body>
</html>


