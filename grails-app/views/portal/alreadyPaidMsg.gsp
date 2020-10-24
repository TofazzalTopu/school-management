<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleParentsLayout"/>
    <title>Fee Alrady Paid</title>
</head>
<body>
<grailslab:breadCrumbActions firstBreadCrumbText="By Student" firstBreadCrumbUrl="${g.createLink(controller: 'collections',action: 'byStudent2')}" breadCrumbTitleText="Quick Collection By Student"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <g:if test='${flash.message}'>
                <header class="panel-heading">
                    ${flash.message}
                </header>
            </g:if>
        </section>
    </div>
</div>

</body>
</html>
</html>