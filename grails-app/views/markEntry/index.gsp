<%@ page import="com.grailslab.enums.ExamType" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="adminLayout"/>
    <title>CT Mark Entry</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Mark Entry"/>
<g:render template='markEntrySelection'/>
<script type="application/javascript">
    jQuery(function ($) {
        $("#markEntrySubjectSelectHolder").show(500);
    });
</script>
</body>
</html>