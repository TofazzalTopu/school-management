<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta name="layout" content="moduleParentsLayout"/>
    <title>Profile</title>
</head>

<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Online Payment"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                <span class="panel-header-title">ST ID :</span> <span class="panel-header-info">${student?.studentID},</span><span class="panel-header-title">Name: </span><span class="panel-header-info">${student?.name},</span><span class="panel-header-title">Roll No: </span><span class="panel-header-info">${student?.rollNo},</span><span class="panel-header-title">Section: </span><span class="panel-header-info">${student?.section?.name} [ ${student?.className?.name} ] </span>
            </header>
            <div class="panel-body" id="portalPaymentBody">
                <g:if test="${hasPending}">
                    <g:render template='/portal/paymentVerification'/>
                </g:if>
                <g:else>
                    <g:render template='/portal/paymentChart'/>
                </g:else>
            </div>
        </section>
    </div>
</div>
</body>
</html>
</html>