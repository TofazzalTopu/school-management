<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Student Attendance </title>
    <meta name="layout" content="moduleAttandanceLayout"/>
</head>

<body>

<grailslab:breadCrumbActions breadCrumbTitleText="Manual Srudent Daily Attendance" SHOW_CREATE_LINK="YES" createLinkText="Back" createLinkUrl="${g.createLink(controller: 'attnStudent', action: 'index')}"/>

<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading" id="selectionFilterHolder">
                <div class="form-horizontal">
                    <div class="form-group">
                        <div class="col-md-3">
                            <g:select class=" form-control" id="className" name='className' tabindex="1"
                                      noSelection="${['': 'Select Class']}"
                                      from='${classNameList}' value="${className?.id}"
                                      optionKey="id" optionValue="name"></g:select>
                        </div>
                        <div class="col-md-2">
                            <select class=" form-control" id="sectionName" name="sectionName" tabindex="1">
                                <option value="">All Section</option>
                            </select>
                        </div>
                        <div class="col-md-3">
                            <input class="form-control" type="text" name="attendDate" id="attendDate" placeholder="<g:formatDate date="${new java.util.Date()}" format="dd/MM/yyyy" />"
                                   tabindex="2"/>

                        </div>

                        <div class="col-md-2">
                            <button class="btn btn-info" id="load-btn">Load Student</button>
                        </div>
                    </div>
                </div>
            </header>
            <div class="panel-body" id="student-list-holder" style="display: none;">
                <form class="cmxform form-horizontal" id="subjectMappingForm">
                    <div class="col-md-12">
                        <div class="table-responsive">
                            <table class="table table-striped table-hover table-bordered" id="list-table">
                                <thead>
                                <input type="hidden" name="loadedrow" id="loadedrow" value="0"/>
                                <tr>
                                    <th>SL</th>
                                    <th>Name</th>
                                    <th>STD ID</th>
                                    <th>Roll No</th>
                                    <th>Attendance Status</th>

                                </tr>
                                </thead>
                                <tbody>
                                </tbody>
                            </table>
                        </div>
                    </div>
                    <div class="form-group col-md-12 pull-right">

                        <div class="col-md-2 pull-right">
                            <button class="btn btn-primary" type="submit" id="submit-fees-btn">Submit</button>
                        </div>

                        <div class="col-md-2 pull-right">
                            <button class="btn btn-primary" type="reset" id="reset-fees-btn">Reset</button>
                        </div>
                    </div>
                </form>

            </div>
        </section>
    </div>
</div>

<script>
    var className, sectionName, gender, religion, subjectName,sectionVal, subjectVal, added,attandenceDate, presentStatus, absentStatus;
    jQuery(function ($) {

        $("#subjectMappingForm").submit(function (e) {
            className =$("#className :selected").text();
            sectionName =$("#sectionName :selected").text();
            sectionVal= $('#sectionName').val();
            attandenceDate = $('#attendDate').val();
            // added = $('#added').val() === '1' ? 'Add' : 'Remove';
            var confirmStr = "You are attempting to  Class "+className + "and Section "+sectionName+"all selected Student." +
                    "  \n\nClick 'OK to confirm, or click 'Cancel' to stop this action.";


            if (confirm(confirmStr)) {
                showLoading("#student-list-holder");
                $.ajax({
                    type: "POST",
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'attnStudent',action: 'saveManualAttendence')}?sectionName="+sectionVal+"&attendDate="+attandenceDate,
                    data: $("#subjectMappingForm").serialize(), // serializes the form's elements.
                    success: function (data) {
                        hideLoading("#student-list-holder");
                        if (data.isError === true) {
                            showErrorMsg(data.message);
                        } else {
                            showSuccessMsg(data.message);
                            $("#student-list-holder").hide(500);
                        }
                    }
                });
            }
            e.preventDefault(); // avoid to execute the actual submit of the form.
        });

        $('#attendDate').datepicker({
            endDate : '0d',
            format: 'dd/mm/yyyy',
            autoclose: true
        });

        $('#load-btn').click(function (e) {
            className = $('#className').val();
            sectionName = $('#sectionName').val();
            attandenceDate = $('#attendDate').val();
            $("#student-list-holder").hide(500);
            if(className != ""){
                $('#className').css({"border-color" : "",  "box-shadow": "", "-webkit-box-shadow" : ""});
              if(sectionName!="") {

                 $('#sectionName').css({"border-color": "", "box-shadow": "", "-webkit-box-shadow": ""});
                 showLoading("#selectionFilterHolder");
                 jQuery.ajax({
                     type: 'POST',
                     dataType: 'JSON',
                     url: "${g.createLink(controller: 'attnStudent',action: 'loadStudentManualAttend')}?attendDate=" + attandenceDate + "&sectionName=" + sectionName,
                     success: function (data) {
                         hideLoading("#selectionFilterHolder");
                         if (data.isError === true) {
                             showErrorMsg(data.message);
                         } else {
                             $("#list-table").find("tr:gt(0)").empty();
                             var sl = 0;
                             $('#loadedrow').val(data.studentList.length);
                             $.each(data.studentList, function (key, value) {
                                 if (value.attnStatus === 1) {
                                     presentStatus = "checked";
                                     absentStatus = "";
                                 } else {
                                     presentStatus = "";
                                     absentStatus = "checked"
                                 }
                                 sl++;
                                 $('#list-table').append('<tr><td>' + sl + '</td><td>' + value.name + '</td><td>' + value.studentid + '<input type="hidden" name="studentId[' + sl + ']" value="' + value.id + '" /> </td><td>' + value.rollNo + '<input type="hidden" name="stdNo[' + sl + ']" value="' + value.studentid + '" /></td><td><input type="radio" class="radio-inline" id="inlineRadio1" name="attanStatus[' + sl + ']"  value="1"  ' + presentStatus + '\> Present <input type="radio" class="radio-inline" id="inlineRadio2" name="attanStatus[' + sl + ']" ' + absentStatus + '  \> Absent </td></tr>');
                             });
                             $("#student-list-holder").show(500);
                         }
                     },
                     error: function (XMLHttpRequest, textStatus, errorThrown) {
                     }
                 });
             }else{
                  if(className == ""){$('#className').css({"border-color" : "#ff0039",  "box-shadow": "none", "-webkit-box-shadow" : "none"});}
                  if(sectionName == ""){ $('#sectionName').css({"border-color" : "#ff0039",  "box-shadow": "none", "-webkit-box-shadow" : "none"});}
             }
            }else{
                if(className == ""){$('#className').css({"border-color" : "#ff0039",  "box-shadow": "none", "-webkit-box-shadow" : "none"});}
                if(sectionName == ""){ $('#sectionName').css({"border-color" : "#ff0039",  "box-shadow": "none", "-webkit-box-shadow" : "none"});}


            }
               //e.preventDefault();
        });
        
        $('#className').on('change', function (e) {
            className = $('#className').find("option:selected").val();
            loadSectionOnClassChange(className, 'sectionName');
        });

    });
    function loadSectionOnClassChange(classNameOnchange, controlName) {
        jQuery.ajax({
            type: 'POST',
            dataType: 'JSON',
            url: "${g.createLink(controller: 'remote',action: 'listSection')}?className="+classNameOnchange,
            success: function (data, textStatus) {
                if (data.isError == false) {
                    var $select = $('#'+controlName);
                    $select.find('option').remove();
                    $select.append('<option value="">All Section</option>');
                    $.each(data.sectionList, function (key, value) {
                        $select.append('<option value=' + value.id + '>' + value.name + '</option>');
                    });
                } else {
                    showErrorMsg(data.message);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            }
        });
    }
</script>
</body>
</html>


