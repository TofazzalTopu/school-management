<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleStdMgmtLayout"/>
    <title>Batch Promotion</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Batch Promotion by result"/>
<div class="row" id="create-form-holder">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Batch Promotion by Result - Select start and end position to promotion each section
            <div class="panel-body">
                <form class="form-horizontal" role="form">
                    <div class="col-md-12" id="batch-promotion-holder">
                        <div class="form-group">
                            <div class="col-md-2">
                                <g:select tabindex="1" class="form-control"
                                          id="academicYear" name='academicYear'
                                          noSelection="${['': 'Select Year...']}"
                                          from='${academicYearList}'
                                          optionKey="key" optionValue="value"></g:select>
                            </div>
                            <div class="col-md-2">
                                <g:select class=" form-control " id="className" name='className'
                                          tabindex="2"
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
                                    <option value="byMeritPosition">By Range (Merit Position)</option>
                                    <option value="byRoll">By Roll No (Old Class) </option>
                                </select>
                            </div>

                            <div class="col-md-2">
                                <button class="btn btn-info" id="load-old-btn">Load Students</button>
                            </div>

                        </div>
                    </div>
                </form>
            </div>

            </header>
            <g:if test="${flash.message}">
                <div class="ibox-title" id="showMessageHolder">
                    <h4 style="color:blue; text-align: center">${flash.message}</h4>
                </div>
            </g:if>

            <div class="panel-body">
                <div class="col-md-12" id="student-datalist-holder" style="display: none;">
                    <div class="row">
                        <form class="cmxform form-horizontal" id="batchPromotionForm" method="POST">
                            <g:hiddenField name="oldClassId" id="oldClassId"/>
                            <g:hiddenField name="oldYear" id="oldYear"/>
                            <g:hiddenField name="newYear" id="newYear"/>
                            <g:hiddenField name="groupNameVal" id="groupNameVal"/>
                            <g:hiddenField name="transferTypeVal" id="transferTypeVal"/>
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
                                    <button class="btn btn-primary" type="submit">Promote</button>
                                </div>
                            </div>
                        </form>
                    </div>
                </div>
            </div>

        </section>
    </div>
</div>

<script>
var shiftExam, className, section, examName, groupName, academicYear, transferType;
jQuery(function ($) {
    $('#load-old-btn').click(function (e) {
        $("#showMessageHolder").hide(500);
        academicYear = $('#academicYear').val();
        className = $('#className').val();
        groupName = $('#groupName').val();
        transferType = $('#transferType').val();

        showLoading("#create-form-holder");
        jQuery.ajax({
            type: 'POST',
            dataType:'JSON',
            url: "${g.createLink(controller: 'student',action: 'loadBatchPromotion')}?className="+className+"&groupName="+groupName+"&academicYear="+academicYear+"&transferType="+transferType,
            success: function (data) {
                hideLoading("#create-form-holder");
                if(data.isError==true){
                    $("#student-datalist-holder").hide(500);
                    showErrorMsg(data.message);
                }else {
                    $('#oldClassId').val(data.oldClassId);
                    $('#newClassId').val(data.newClassId);
                    $('#oldYear').val(data.oldYear);
                    $('#newYear').val(data.newYear);
                    $('#groupNameVal').val(data.groupNameVal);
                    $('#transferTypeVal').val(data.transferTypeVal);
                    $('#numOfStudent').html('Year: <span style ="color:red;">'+data.academicYearStr+'</span>. ' + 'Promotion Class Name: <span style ="color:red;">'+data.nameOfCls+'</span>. Number of Student: <span style ="color:red;">'+data.numOfStudent+'</span>');
                    $("#list-table").find("tr:gt(0)").empty();
                    $.each(data.sectionList, function (key, value) {
                        $('#list-table').append('<tr><td>' + value.name +'</td><td><input type="hidden" name="sectionIds" value="'+value.id+'"><input type="number" name="start'+value.id+'" required></td><td><input type="number" name="end'+value.id+'" required</td></tr>');
                    });
                    $("#student-datalist-holder").show(500);

                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            }
        });

        e.preventDefault();
    });

    $('#batchPromotionForm').on('submit', function(e) { //use on if jQuery 1.7+
        if( confirm('Are you sure to promote? This action can not undone') ){
            var formAction = "${g.createLink(controller: 'student', action: 'batchPromotion')}";
            $("#batchPromotionForm").attr("action", formAction);
        } else {
            e.preventDefault();
        }
    });

});

</script>
</body>
</html>
