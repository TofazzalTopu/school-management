<%@ page import="com.grailslab.enums.ExamType" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleExam&ResultLayout"/>
    <title>Evaluation Entry</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Evaluation Entry"/>
<g:render template='evaluationEntry'/>
<script type="application/javascript">
    jQuery(function ($) {
        $("#markEntrySubjectSelectHolder").show(500);
    });
</script>
</body>
</html>