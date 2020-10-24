<%@ page import="com.grailslab.CommonUtils" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="open-ltpl"/>
    <title>Online Application - Step 4</title>
</head>

<body>
    <div class="wrapbox">
        <section class="pageheader-default text-center">
            <div class="semitransparentbg">
                <h1 class="animated fadeInLeftBig notransition">Step 4</h1>
            </div>
        </section>
        <div class="wrapsemibox">
            <div class="container animated fadeInUpNow notransition fadeInUp topspace0">
                <div class="col-md-offset-2 col-md-8">
                    <h3 class="smalltitle">
                        <span>Previous School</span>
                    </h3>
                    <g:render template="/layouts/errors" bean="${command?.errors}"/>
                    <form role="form" class="form-horizontal" method="post"action="${g.createLink(controller: 'online',action: 'step5')}">
                        <g:hiddenField name="regId" value="${registration?.id}"/>
                        <div class="form-group">
                            <label class="col-sm-3 control-label" for="preSchoolName">Schools Name</label>
                            <div class="col-sm-6">
                                <input type="text" placeholder="School Name" id="preSchoolName" name="preSchoolName"  value="${command?.preSchoolName? command.preSchoolName : (registration?.preSchoolName? registration.preSchoolName:'')}" class="form-control">
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label" for="preSchoolAddress">Address</label>
                            <div class="col-sm-6">
                                <div class="textarea">
                                    <textarea placeholder="Address" rows="3" id="preSchoolAddress" name="preSchoolAddress"class="form-control" > ${command?.preSchoolAddress? command.preSchoolAddress : (registration?.preSchoolAddress? registration.preSchoolAddress:'')} </textarea>
                                </div>
                            </div>
                        </div>
                        <div class="form-group">
                            <label class="col-sm-3 control-label" for="preSchoolClass">Previous Class</label>
                            <div class="col-sm-6">
                                <input type="text" placeholder="PreviousClass" id="preSchoolClass" name="preSchoolClass" value="${command?.preSchoolClass? command.preSchoolName : (registration?.preSchoolClass? registration.preSchoolClass:'')}" class="form-control">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-sm-3 control-label" for="preSchoolTcDate">TC Date</label>
                            <div class="col-sm-6">
                                <input type="text" placeholder="TC Date" id="preSchoolTcDate" name="preSchoolTcDate" value="${command?.preSchoolTcDate? command.preSchoolName : (registration?.preSchoolTcDate?   com.grailslab.CommonUtils.getUiDateStrForCalenderDateEdit(registration.preSchoolTcDate):'')}" class="form-control">
                            </div>
                        </div>
                        <div class="row pull-right">
                            <div class="form-group">
                                <button type="button"  class="btn btn-default preBtn">Previous </button>
                                <button type="submit" class="btn btn-default">Save & Next</button>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </div>
    </div>
    <script>
        $(function () {
            $(function () {
                $('#preSchoolTcDate').datepicker({
                    format: 'mm/dd/yyyy',
                    startDate: '-3d'
                });

                $('.preBtn').click(function (){
                    window.history.back();
                })
            });
        });
    </script>
</body>
</html>