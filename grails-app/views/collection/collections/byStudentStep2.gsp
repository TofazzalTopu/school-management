<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleCollectionLayout"/>
    <title>Collection By Student Step 2</title>
</head>
<body>
<grailslab:breadCrumbActions firstBreadCrumbText="By Student" firstBreadCrumbUrl="${g.createLink(controller: 'collections',action: 'byStudent')}" breadCrumbTitleText="Quick Collection By Student"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
                <header class="panel-heading">
                    <span class="panel-header-title">ST ID :</span> <span class="panel-header-info">${student?.studentID},</span><span class="panel-header-title">Name: </span><span class="panel-header-info">${student?.name},</span><span class="panel-header-title">Roll No: </span><span class="panel-header-info">${student?.rollNo},</span><span class="panel-header-title">Section: </span><span class="panel-header-info">${student?.section?.name} [ ${student?.className?.name} ] </span>
                </header>
                <div class="panel-body">
                    <div class="col-md-12" id="class-fee-list-holder">
                        <div class="row">
                            <form class="cmxform form-inline" role="form" method="post" id="create-form" action="${g.createLink(controller: 'collections',action: 'payInvoice')}">
                                <g:hiddenField name="studentId" id="studentId" value="${student?.id}"/>
                                    <g:render template='/collection/collections/feeReceiveFrom'/>
                            </form>
                        </div>
                    </div>
                </div>
        </section>
    </div>
</div>
</body>
</html>
</html>