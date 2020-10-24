<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Class Subject</title>
    <meta name="layout" content="moduleStdMgmtLayout"/>
</head>

<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Class Subject Mapping"/>
<div class="row" >
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Class List
            </header>
            <div class="panel-body">
                <table class="table table-striped table-hover table-bordered" id="list-table">
                    <thead>
                    <tr>
                        <th>Serial</th>
                        <th>Class Name</th>
                        <th>Subjects</th>
                        <th>Action</th>
                    </tr>
                    </thead>
                    <tbody>
                    <g:each in="${dataReturns}" var="dataSet" status="i">
                        <tr>
                            <td>${i+1}</td>
                            <td>${dataSet.className}</td>
                            <td>${dataSet.subjects}</td>
                            <td>
                                <sec:access controller="classSubjects" action="edit">
                                    <span class="col-md-10 no-padding"><a href="${g.createLink(controller: 'classSubjects',action: 'subjects', params: [id:dataSet.id])}" class="edit-reference" title="Maps Subject to Class "><span class="green glyphicon glyphicon-tint">Subject Mapping</span></a></span>
                                </sec:access>
                            </td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
        </section>
    </div>
</div>
<script>
    jQuery(function ($) {
        $('#list-table').dataTable({
            "iDisplayLength": 25
        });
    });
</script>
</body>
</html>


