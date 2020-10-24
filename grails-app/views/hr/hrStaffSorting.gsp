<%@ page import="com.grailslab.enums.HrKeyType" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Hr Staff Sorting</title>
    <meta name="layout" content="moduleHRLayout"/>
</head>
<body>
<grailslab:breadCrumbActions firstBreadCrumbUrl="${g.createLink(controller: 'hrCategory',action: 'hrStaffCategory')}" firstBreadCrumbText="Staff Category" breadCrumbTitleText="Show Order"/>
<div class="row" id="create-form-holder">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Hr Sorting
            </header>

            <div class="panel-body">
                <div class="table-responsive">
                    <g:form name="myForm" method="post" controller="hrCategory" action="staffSortingSave"
                            class="create-form form-horizontal">
                        <table class="table table-striped table-hover table-bordered" id="list-table">
                            <thead>
                            <tr>
                                <th>Picture</th>
                                <th>ID</th>
                                <th>Name</th>
                                <th>Designation</th>
                                <th>Show Order</th>
                            </tr>
                            </thead>
                            <tbody>
                            <g:each in="${employeeList}" var="dataSet" status="i">
                                <tr>
                                    <td>
                                        <g:if test="${dataSet?.imagePath}">
                                            <img src="${imgSrc.fromIdentifierThumb(imagePath: dataSet?.imagePath)}"/>
                                        </g:if>
                                        <g:else>
                                            <asset:image src="no-image.jpg" alt="avatar" style="width:45px;height:45px;"/>
                                        </g:else>
                                    </td>
                                    <td>${dataSet?.empId}</td>
                                    <td>${dataSet?.name}</td>
                                    <td>${dataSet?.designation}</td>
                                    <td>
                                        <input type="number" name="sortOrder.${dataSet?.id}" class="checkSingle"
                                               value="${dataSet?.empOrder}"/>
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