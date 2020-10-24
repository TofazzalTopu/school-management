<%@ page defaultCodec="none" %>
<html>
<head>
    <meta name="layout" content="open-ltpl"/>
    <title>Online Registration</title>
</head>

<body>
    <div class="wrapbox">
        <section class="pageheader-default text-center">
            <div class="semitransparentbg">
                <h4 class="animated fadeInLeftBig notransition">${fesProgram?.name}</h4>
                <g:if test="${programDate}">
                    <p class="animated fadeInLeftBig notransition">Date: ${programDate}</p>
                </g:if>
            </div>
        </section>
        <div class="wrapsemibox">
            <div class="container animated fadeInUpNow notransition fadeInUp topspace0">
                <div class="col-md-offset-1 col-md-10">
                    <h3 class="smalltitle">
                        <span>Registration form</span>
                    </h3>
                    <g:if test="${flash.message}">
                        <p class="text-center" style="color: sienna">${flash.message}</p>
                    </g:if>
                    <form role="form" class="form-horizontal" method="post" action="${g.createLink(action: 'festival')}">
                        <g:hiddenField name="id" value="${fesProgram.id}"/>
                        <div class="row">
                            <div class="form-group">
                                <label class="col-md-3 control-label" for="name">Name <span class="required-indicator">* </span> </label>
                                <div class="col-md-9">
                                    <input type="text" placeholder="Name" id="name" name="name" value="${command?.name}" class="form-control" required>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-3 control-label" for="instituteName">Name Of Institute <span class="required-indicator">* </span> </label>
                                <div class="col-md-9">
                                    <input type="text" placeholder="Institute Name" id="instituteName" value="${command?.instituteName}" name="instituteName" class="form-control" required>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-md-9 col-md-offset-3">
                                    <div class="col-md-4">
                                       <g:select class="form-control" id="className" name='className' value="${command?.className?.id}" tabindex="4" required="required"
                                                 noSelection="${['': 'Select Class...*']}"
                                                 from='${classNameList}'
                                                 optionKey="id" optionValue="name"></g:select>
                                    </div>
                                    <div class="col-md-4">
                                        <input class="form-control"  type="text" name="rollNo" id="rollNo" value="${command?.rollNo}" placeholder="Roll No*" required tabindex="5"/>
                                    </div>
                                    <div class="col-md-4">
                                        <input class="form-control"  type="text" name="contactNo" value="${command?.contactNo}" id="contactNo" placeholder="Contact No*" required tabindex="6"/>
                                    </div>
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-md-3 control-label" for="instituteName">1. Olypmiad</label>
                                <div class="col-md-9">
                                    <label class="control-label" for="instituteName">Select Option(s) you prefer: <span class="required-indicator">* </span></label>
                                    <g:each in="${olympiadTopicList}" var="olympiadTopic">
                                        <div class="checkbox">
                                            <label>
                                                <input type="checkbox" name="olympiadTopics" id="${olympiadTopic.key}" value="${olympiadTopic.key}"> ${olympiadTopic.value}
                                            </label>

                                        </div>
                                    </g:each>
                                </div>
                            </div>
                            <div id="scienceDetailHolder" style="display: none;">
                                <div class="form-group">
                                    <label class="checkbox-inline col-md-3 control-label"></label>
                                    <div class="col-md-9">
                                        <input type="checkbox"   id="scientificPaper" name="scientificPaper"> 1. Scientific Paper/Project
                                        <input type="checkbox"   id="slideShow" name="slideShow"> 2. Scientific Slide Show Presentation
                                     </div>
                                </div>
                            </div>
                            <div id="scientificPaperHolder" style="display: none;">
                                <div class="form-group">
                                    <label class="col-md-3 control-label" for="instituteName">1. Scientific Paper/Project:</label>
                                    <div class="col-md-9">
                                        <label class="control-label" for="instituteName">Two students in each group</label><br/>
                                        <input type="text" placeholder="Name of the Project*" id="spProjectName" value="${command?.spProjectName}" name="spProjectName" class="form-control">
                                        <table class="table table-bordered">
                                            <thead>
                                            <tr>
                                                <th class="col-md-1">SL.</th>
                                                <th class="col-md-4">Name</th>
                                                <th class="col-md-2">Class</th>
                                                <th class="col-md-2">Roll No</th>
                                                <th class="col-md-3">Contact no</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <tr>
                                                <td>1</td>
                                                <td class="sp1StudentName"></td>
                                                <td class="sp1ClassName"></td>
                                                <td class="sp1RollNo"></td>
                                                <td class="sp1ContactNo"></td>
                                            </tr>
                                            <tr>
                                                <td>2</td>
                                                <td><input type="text" class="form-control" id="spStudentName" value="${command?.spStudentName}" name="spStudentName"></td>
                                                <td><g:select class="form-control classname-step" value="${command?.spClassName?.id}" id="spClassName" name='spClassName' tabindex="4"  noSelection="${['': 'Select Class...']}"
                                                              from='${classNameList}'
                                                              optionKey="id" optionValue="name"></g:select></td>
                                                <td><input type="text" class="form-control" value="${command?.spRollNo}" id="spRollNo" name="spRollNo"></td>
                                                <td><input type="text" class="form-control" id="spContactNo" value="${command?.spContactNo}" name="spContactNo"></td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                            <div id="slideShowHolder" style="display: none;">
                                <div class="form-group">
                                    <label class="col-md-3 control-label" for="instituteName">2. Scientific Slide Show Presentation:</label>
                                    <div class="col-md-9">
                                        <label class="control-label" for="instituteName">Two students in each group</label><br/>
                                        <input type="text" placeholder="Name of the Project*" value="${command?.ssProjectName}" id="ssProjectName" name="ssProjectName" class="form-control">
                                        <table class="table table-bordered">
                                            <thead>
                                            <tr>
                                                <th class="col-md-1">SL.</th>
                                                <th class="col-md-4">Name</th>
                                                <th class="col-md-2">Class</th>
                                                <th class="col-md-2">Roll No</th>
                                                <th class="col-md-3">Contact no</th>
                                            </tr>
                                            </thead>
                                            <tbody>
                                            <tr>
                                                <td>1</td>
                                                <td class="ss1StudentName"></td>
                                                <td class="ss1ClassName"></td>
                                                <td class="ss1RollNo"></td>
                                                <td class="ss1ContactNo"></td>
                                            </tr>
                                            <tr>
                                                <td>2</td>
                                                <td><input type="text" class="form-control" value="${command?.ssStudentName}" id="ssStudentName" name="ssStudentName"></td>
                                                <td><g:select class="form-control classname-step" value="${command?.ssClassName?.id}" id="ssClassName" name='ssClassName' tabindex="4"  noSelection="${['': 'Select Class...']}"
                                                              from='${classNameList}'
                                                              optionKey="id" optionValue="name"></g:select></td>
                                                <td><input type="text" class="form-control" id="ssRollNo" value="${command?.ssRollNo}" name="ssRollNo"></td>
                                                <td><input type="text" class="form-control" id="ssContactNo" value="${command?.ssContactNo}" name="ssContactNo"></td>
                                            </tr>
                                            </tbody>
                                        </table>
                                    </div>
                                </div>
                            </div>
                            <div class="row pull-right">
                                <div class="form-group">
                                    <button type="reset" class="btn btn-default">Reset</button>
                                    <button type="submit" class="btn btn-default">Submit</button>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-md-3 control-label" for="instituteName">Contact:</label>
                            <div class="col-md-9">
                                <label class="control-label" for="instituteName">${fesProgram?.helpContact}</label>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>

    <script>
        var name, instituteName, className, rollNo, contactNo;
        $(function() {
            $('#science').click(function(){
                if (this.checked) {
                    $('#scienceDetailHolder').show();
                } else {
                    $("#scientificPaper").prop("checked", false );
                    $("#slideShow").prop( "checked", false );
                    $('#scienceDetailHolder').hide();
                    $('#scientificPaperHolder').hide();
                    $('#slideShowHolder').hide();
                }
            });

            $('#scientificPaper').click(function(){
                if (this.checked) {
                    name = $("#name").val();
                    instituteName = $("#instituteName").val();
                    className = $("#className").val();
                    rollNo = $("#rollNo").val();
                    contactNo = $("#contactNo").val();
                    if(name === undefined || name === "") {
                        alert("Please fill in your name first");
                        $("#name").focus();
                        return false;
                    }
                    if(instituteName === undefined || instituteName === "") {
                        alert("Please fill in your school/institute first");
                        $("#instituteName").focus();
                        return false;
                    }
                    if(className === undefined || className === "") {
                        alert("Please select your class name first");
                        $("#className").focus();
                        return false;
                    }
                    if(rollNo === undefined || rollNo === "") {
                        alert("Please fill in your roll No first");
                        $("#rollNo").focus();
                        return false;
                    }
                    if(contactNo === undefined || contactNo === "") {
                        alert("Please fill in your contact No first");
                        $("#contactNo").focus();
                        return false;
                    }
                    className = $("#className").find("option:selected").text();
                    $(".sp1StudentName").html(name);
                    $(".sp1ClassName").html(className);
                    $(".sp1RollNo").html(rollNo);
                    $(".sp1ContactNo").html(contactNo);
                    $('#scientificPaperHolder').show();
                } else {
                    $('#scientificPaperHolder').hide();
                }
            });

            $('#slideShow').click(function(){
                if (this.checked) {
                    name = $("#name").val();
                    instituteName = $("#instituteName").val();
                    className = $("#className").val();
                    rollNo = $("#rollNo").val();
                    contactNo = $("#contactNo").val();
                    if(name === undefined || name === "") {
                        alert("Please fill in your name first");
                        $("#name").focus();
                        return false;
                    }
                    if(instituteName === undefined || instituteName === "") {
                        alert("Please fill in your school/institute first");
                        $("#instituteName").focus();
                        return false;
                    }
                    if(className === undefined || className === "") {
                        alert("Please select your class name first");
                        $("#className").focus();
                        return false;
                    }
                    if(rollNo === undefined || rollNo === "") {
                        alert("Please fill in your roll No first");
                        $("#rollNo").focus();
                        return false;
                    }
                    if(contactNo === undefined || contactNo === "") {
                        alert("Please fill in your contact No first");
                        $("#contactNo").focus();
                        return false;
                    }
                    className = $("#className").find("option:selected").text();
                    $(".ss1StudentName").html(name);
                    $(".ss1ClassName").html(className);
                    $(".ss1RollNo").html(rollNo);
                    $(".ss1ContactNo").html(contactNo);
                    $('#slideShowHolder').show();
                } else {
                    $('#slideShowHolder').hide();
                }
            });

        });
    </script>
</body>
</html>