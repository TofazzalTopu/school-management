<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleStdMgmtLayout"/>
    <title>Roll Reorder</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Role Reoerder" SHOW_LINK_BTN='${resultMap?"YES":"NO"}' linkBtnText="Choose Another Class"/>
<g:if test="${flash.message}">
    <div class="alert alert-success">
        <h4 style="color: sienna">${flash.message}</h4>
    </div>
</g:if>
<g:if test="${resultMap}">
    <div class="row" id="create-form-holder" style="display: none;">
</g:if>
<g:else>
    <div class="row" id="create-form-holder">
</g:else>
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                <span class="panel-header-info">Roll Reorder </span>
            </header>
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
                                  noSelection="${['': 'Select Class...']}"
                                  from='${classNameList}'
                                  optionKey="id" optionValue="name"></g:select>
                            </div>
                        </div>
                    </div>
                </form>
            </div>

        </section>
    </div>
</div>

<g:if test="${resultMap}">
    <div class="row" id="attendance-entry-holder">
        <div class="col-sm-12">
            <section class="panel">
                <header class="panel-heading">
                    <span class="panel-header-title">Year : </span> <span class="panel-header-info"><g:if test="${academicYear}">${academicYear?.value}</g:if><g:else> </g:else> ,</span> <span class="panel-header-title"> Class : </span><span class="panel-header-info">${className?.name}</span>
                </header>
                <div class="panel-body">
                    <div class="table-responsive">
                        <g:form name="myForm" method="post" controller="${acaYearType == 'SCHOOL'? school:college}" action="roleReorder"
                                class="create-form form-horizontal">
                            <table class="table table-striped table-hover table-bordered" id="list-table">
                                <thead>
                                <tr>
                                    <th>Section Name</th>
                                    <th>Student ID</th>
                                    <th>Student Name</th>
                                    <th>Role Number</th>
                                </tr>
                                </thead>
                                <tbody>
                                <g:each in="${resultMap}" var="result" status="i">
                                    <tr>
                                        <td>${result?.section?.name}</td>
                                        <td>${result?.studentID}</td>
                                        <td>${result?.name}</td>
                                        <td>
                                            <input type="number" name="student.${result?.id}" class="checkSingle"
                                                   value="${result?.rollNo}"/>
                                        </td>
                                    </tr>
                                </g:each>
                                </tbody>
                            </table>

                            <div class="col-md-12">
                                <button class="btn btn-default" aria-hidden="true" data-dismiss="modal"
                                        type="button">Cancel</button>
                                <button id="create-yes-btn" class="btn btn-large btn-primary" type="submit">Submit</button>
                            </div>
                        </g:form>
                    </div>
                </div>
            </section>
        </div>
    </div>
</g:if>
<script>
var className, academicYear, acaYearType;
jQuery(function ($) {
    $('#className').on('change', function (e) {
        academicYear =$('#academicYear').val();
        className = $('#className').val();
        acaYearType = "${acaYearType?.key}";
        if (className) {
            if (acaYearType == "COLLEGE") {
                window.location.href = '${g.createLink(controller: 'college',action: 'roleReorder')}/' + className+'?academicYear='+academicYear;
            } else {
                window.location.href = '${g.createLink(controller: 'student',action: 'roleReorder')}/' + className+'?academicYear='+academicYear;
            }
        }
    });
    $('.link-url-btn').click(function (e) {
        $("#create-form-holder").toggle(500);
        e.preventDefault();
    });

});
</script>
</body>
</html>
