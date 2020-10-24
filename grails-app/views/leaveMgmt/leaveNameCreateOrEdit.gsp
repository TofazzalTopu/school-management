<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="moduleLeaveManagementLayout"/>
        <title>Add/Update Leave Management</title>
    </head>
    <body>
        <grailslab:breadCrumbActions firstBreadCrumbUrl="${g.createLink(controller: 'leaveName', action: 'index')}" firstBreadCrumbText="Leave Name List" breadCrumbTitleText="Add/Edit Leave "/>
        <div class="wrapbox">
            <section class="panel">
                <div class="panel-body create-content">
                    <g:render template="/layouts/errors" bean="${command?.errors}"/>
                    <g:form class="form-horizontal" method="post" controller="leaveName" action="save">
                        <g:hiddenField name="id" value="${leaveName?.id}"/>
                        <g:if test="${flash.message}">
                            <h4 class="text-center" style="color: sienna">${flash.message}</h4>
                        </g:if>

                        <div class="modal-body">
                            <div class="col-sm-12">
                                <div class="row">
                                    <div class="form-group">
                                        <label class="control-label col-md-2" for="name">Name <span class="required-indicator">* </span> </label>
                                        <div class="col-sm-6">
                                            <input type="text" placeholder="Enter the Leave Name" required="true" id="name"name="name" value="${leaveName?.name}"  class="form-control">
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label  class="control-label col-md-2" for="payType">Leave Type<span class="required-indicator">*</span></label>
                                        <div class="col-md-6">
                                            <g:select class="form-control" id="payType" name='payType'
                                                      from='${com.grailslab.enums.LeavePayType.paidUnPaidLeave()}' optionKey="key" optionValue="value"
                                                      required="required" value="${leaveName?.payType}"></g:select>
                                        </div>
                                    </div>
                                    <div class="form-group">
                                        <label class="control-label col-md-2" for="numberOfDay">Number Of Day <span class="required-indicator">* </span> </label>
                                        <div class="col-sm-6">
                                            <input type="number" placeholder="Number Of Day"  required="true" id="numberOfDay"name="numberOfDay" value="${leaveName?.numberOfDay}"  class="form-control">
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="modal-footer modal-footer-action-btn col-md-6 col-md-offset-3">
                            <button type="reset" class="btn btn-default" aria-hidden="true">Reset</button>
                            <button type="submit" id="create-yes-btn" class="btn btn-large btn-primary">${leaveName? "Update":"Save"}</button>
                        </div>
                    </g:form>
                </div>
            </section>
        </div>
    </body>
</html>
