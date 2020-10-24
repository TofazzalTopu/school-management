<%@ page import="com.grailslab.enums.HrKeyType" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Working Day Entry list</title>
    <meta name="layout" content="adminLayout"/>
</head>
<body>

<grailslab:breadCrumbActions firstBreadCrumbUrl="${g.createLink(controller: 'previousTerm', action: 'attendance')}" firstBreadCrumbText="Attendance Entry" breadCrumbTitleText="Working Days"/>

<div class="row" id="create-form-holder">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Working days
            </header>

            <div class="panel-body">
                <div class="table-responsive">
                    <g:form name="myForm" method="post" controller="previousTerm" action="workDays"
                            class="form-horizontal">
                        <table class="table table-striped table-hover table-bordered" id="list-table">
                            <thead>
                            <tr>
                                <th>SL</th>
                                <th>Class Name</th>
                                <th>Working Days Entry</th>
                            </tr>
                            </thead>
                            <tbody>
                            <g:each in="${classNameList}" var="dataSet" status="i">
                                <tr>
                                    <td>${i+1}</td>
                                    <td>${dataSet?.name}</td>
                                    <td>
                                        <input type="number" name="workingDay.${dataSet?.id}" class="checkSingle"
                                               value="${dataSet?.workingDays}"/>
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

</body>
</html>