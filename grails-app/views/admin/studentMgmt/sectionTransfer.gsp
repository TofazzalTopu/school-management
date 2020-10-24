<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleStdMgmtLayout"/>
    <title>Section Ttransfer</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Section Transfer"/>
<div class="row" id="create-form-holder">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Section Transfer
            </header>
            <div class="panel-body">
                <form class="form-horizontal" role="form">
                    <div class="col-md-12">
                        <div class="form-group">
                            <div class="col-md-2">
                                <g:select class=" form-control " id="examName" name='examName'
                                          tabindex="1"
                                          noSelection="${['': 'Select Exam...']}"
                                          from='${examNameList}' value="${shiftExam?.id}"
                                          optionKey="id" optionValue="name"></g:select>
                            </div>

                            <div class="col-md-2">
                                <select class=" form-control " id="examType" name="examType" tabindex="2">
                                    <option value="byHall">Hall Exam</option>
                                    <option value="byCt">CT Exam</option>
                                </select>
                            </div>

                            <div class="col-md-2">
                                <g:select class=" form-control " id="className" name='className'
                                          tabindex="2"
                                          noSelection="${['': 'Select Class...']}"
                                          from='${classNameList}'
                                          optionKey="id" optionValue="name"></g:select>
                            </div>

                            <div class="col-md-2">
                                <g:select class="form-control" id="groupName" name='groupName' tabindex="3"
                                          noSelection="${['': ' General...']}"
                                          from='${com.grailslab.enums.GroupName.values()}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                            <div class="col-md-2">
                                <select class=" form-control " id="transferType" name="transferType" tabindex="2">
                                    <option value="byRange">By Range (Merit Position)</option>
                                </select>
                            </div>

                            <div class="col-md-2">
                                <button class="btn btn-info" id="load-old-btn">Load Students</button>
                            </div>

                        </div>

                    </div>
                </form>
            </div>

            <g:if test="${flash.message}">
                <div class="ibox-title" id="showMessageHolder">
                    <h4 style="color:blue; text-align: center">${flash.message}</h4>
                </div>
            </g:if>

            <div class="panel-body">
                <div class="col-md-12" id="student-datalist-holder" style="display: none;">
                    <div class="row">
                        <g:form class="cmxform form-horizontal" name="transferSectionForm" id="transferSectionForm" method="POST">
                            <g:hiddenField name="classNameId" id="classNameId"/>
                            <g:hiddenField name="shiftExamId" id="shiftExamId"/>
                            <g:hiddenField name="groupNameVal" id="groupNameVal"/>
                            <g:hiddenField name="examTypeVal" id="examTypeVal"/>
                            <div class="col-md-10 col-md-offset-1">
                                <div class="col-md-12">
                                    <div class="ibox float-e-margins">
                                        <div class="ibox-title">
                                            <h4 style="color:blue;"><span id="numOfStudent"/></h4>
                                        </div>
                                        <div class="ibox-content">
                                            <div class="table-responsive">
                                                <table class="table tree table-bordered table-striped table-condensed" id="list-table">
                                                    <thead>
                                                    <tr>
                                                        <th>Section Name</th>
                                                        <th>Start position</th>
                                                        <th>End Position</th>
                                                    </tr>
                                                    </thead>
                                                    <tbody>
                                                    </tbody>
                                                </table>
                                            </div>

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group col-md-12">
                                <div class="col-md-6 col-md-offset-10">
                                    <button class="btn btn-primary" type="submit" id="submit-fees-btn">Transfer</button>
                                </div>
                            </div>
                        </g:form>
                    </div>
                </div>
            </div>

        </section>
    </div>
</div>



<script>
var shiftExam, className, section, examName,byEnlarge,groupName, printOptionType, printParam, examType;
jQuery(function ($) {
    $('#transferSectionForm').on('submit', function(e) { //use on if jQuery 1.7+
        var formAction = "${g.createLink(controller: 'student',action: 'sectionTransfer')}";
        $("#transferSectionForm").attr("action", formAction);
        $("#submit-fees-btn").attr("disabled", true).html("Transfering...");
    });
    $('#examName').on('change', function (e) {
        examName = $('#examName').val();
        if (examName) {
            var examClassUrl = "${g.createLink(controller: 'remote',action: 'examClassList')}?examName="+examName;
            loadExamClass(examClassUrl, '#className', "#create-form-holder")
        }
    });

    $('#load-old-btn').click(function (e) {
        $("#showMessageHolder").hide(500);
        examName = $('#examName').val();
        className = $('#className').val();
        groupName = $('#groupName').val();
        byEnlarge = $('#byEnlarge').val();

        showLoading("#create-form-holder");
        jQuery.ajax({
            type: 'POST',
            dataType:'JSON',
            url: "${g.createLink(controller: 'student',action: 'loadSectionTransfer')}?id="+examName+"&className="+className+"&groupName="+groupName,
            success: function (data) {
                hideLoading("#create-form-holder");
                if(data.isError==true){
                    $("#student-datalist-holder").hide(500);
                    showErrorMsg(data.message);
                }else {
                    $('#classNameId').val(data.classNameId);
                    $('#shiftExamId').val(data.shiftExamId);
                    $('#groupNameVal').val(data.groupNameVal);
                    $('#examTypeVal').val($('#examType').val());
                    $('#numOfStudent').html('Class Name: <span style ="color:red;">'+data.nameOfCls+'</span>. Number of Student: <span style ="color:red;">'+data.numOfStudent+'</span>');
                    $("#list-table").find("tr:gt(0)").empty();
                    $.each(data.sectionList, function (key, value) {
                        $('#list-table').append('<tr><td>' + value.name +'</td><td><input type="hidden" name="sectionIds" value="'+value.id+'"><input type="number" name="start'+value.id+'" required></td><td><input type="number" name="end'+value.id+'" required </td></tr>');
                    });
                    $("#student-datalist-holder").show(500);

                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            }
        });

        e.preventDefault();
    });
});

</script>
</body>
</html>
