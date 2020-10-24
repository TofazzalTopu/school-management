<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="adminLayout"/>
    <title>Report Not Found </title>
</head>

<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Report Not Fount"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <div class="panel-body">
                <div class="alert alert-warning" role="alert"><h2>
                    <g:if test='${flash.message}'>
                            ${flash.message}
                    </g:if>
                </h2></div>

            </div>
        </section>
    </div>
</div>

</body>
</html>