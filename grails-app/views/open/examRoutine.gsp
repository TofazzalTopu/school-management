<%--
  Created by IntelliJ IDEA.
  User: Hasnat
  Date: 1/23/2015
  Time: 3:32 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="open-ltpl"/>
    <title>Exam Routine</title>
</head>

<body>
<section class="pageheader-default text-center">
    <div class="semitransparentbg">
        <h1 class="animated fadeInLeftBig notransition">Exam Routine</h1>
        <g:if test="${flash.message}">
            <h4 class="text-center" style="color: sienna">${flash.message}</h4>
        </g:if>
    </div>
</section>
<div class="wrapsemibox">
    <section class="container animated fadeInDown notransition">
        <div class="row blogindex">
            <sec:ifLoggedIn>
                <g:form enctype="multipart/form-data" method="POST" controller="examSchedule" action="examRoutineUpload">
                    <label class="col-md-2 control-label">Change Routine (JPG/PNG)</label>

                    <div class="col-md-5">
                        <input type="file" name="routineFile" id="routineFile"/>
                        <p>Maximum Size : 5mb</p>
                    </div>
                    <button class="btn btn-primary" type="submit">Upload</button>
                </g:form>
            </sec:ifLoggedIn>

            <div class="col-md-12">
                <div class="text-center">
                    <img src="${createLink(controller: 'calendar', action: 'examRoutineFile')}"  class="img-responsive aligncenter" alt="">
                </div>
            </div>
        </div>
    </section>
</div>
</body>
</html>