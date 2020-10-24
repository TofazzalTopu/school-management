<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleSmsLayout"/>
    <title>Send Result to Students</title>
</head>
<body>
<grailslab:breadCrumbActions firstBreadCrumbText="Messaging" firstBreadCrumbUrl="${g.createLink(controller: 'messaging',action: 'index')}" breadCrumbTitleText="Send Result"/>

<g:form name="myForm" method="post" action="save" class="create-form form-horizontal">
    <div class="row" id="create-form-holder">
        <div class="col-sm-12">
            <section class="panel">
                <g:if test="${flash.message}">
                    <header class="panel-heading">
                        ${flash.message}
                    </header>
                </g:if>

                <div class="panel-body">
                    <div class="col-md-12">
                        <div class="row">
                            <div class="form-group" id="sent-btn-holder">
                                <div class="col-md-8 col-md-offset-9">
                                    <div class="col-md-8">
                                        <div class="btn btn-large btn-info" id="sendMessage">Send Result Message</div>
                                    </div>
                                </div>
                            </div>
                            <div class="form-group">
                                <div class="col-md-12" id="success-status-holder" style="display: none">
                                    <div class="col-md-6 col-md-offset-3">
                                        <div class="alert alert-success"><h2><span id="succMsg"></span> </h2></div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </div>

    <div class="row" id="create-form-holder">
        <div class="col-sm-12">
            <section class="panel">
                <header class="panel-heading">
                    Student List For Message
                </header>
                <div class="panel-body">
                    <div class="table-responsive">
                        <table class="table table-striped table-hover table-bordered" id="list-table">
                            <thead>
                            <tr>
                                <th>Serial</th>
                                <th>Section</th>
                                <th>Student</th>
                                <th>Mobile</th>
                                <th>Message</th>
                                <th>NumOfMsg</th>
                                <th>Msg Sent?</th>
                                <th><input type="checkbox" name="checkAll" id="checkAll" checked></th>
                            </tr>
                            </thead>
                            <tbody>
                            <g:each in="${studentList}" var="dataSet" status="i">
                                <tr>
                                    <td>${i + 1}</td>
                                    <td>${dataSet?.sectionStr}</td>
                                    <td>${dataSet?.studentStr}</td>
                                    <td>${dataSet?.mobileNo}</td>
                                    <td>${dataSet?.msgStr}</td>
                                    <td>${dataSet?.numOfMsg}</td>
                                    <td>${dataSet?.msgStatus}</td>
                                    <td>
                                        <input type="checkbox" name="tabulationIds" class="checkSingle" value="${dataSet?.id}"
                                               checked/>
                                    </td>
                                </tr>
                            </g:each>
                            </tbody>
                        </table>
                    </div>
                </div>
            </section>
        </div>
    </div>
</g:form>
<script>
    jQuery(function ($) {

        $("#checkAll").change(function () {
            if (this.checked) {
                $(".checkSingle").each(function () {
                    this.checked = true;
                })
            } else {
                $(".checkSingle").each(function () {
                    this.checked = false;
                })
            }
        });

        $(".checkSingle").click(function () {
            if ($(this).is(":checked")) {
                var isAllChecked = 0;
                $(".checkSingle").each(function () {
                    if (!this.checked)
                        isAllChecked = 1;
                })
                if (isAllChecked == 0) {
                    $("#checkAll").prop("checked", true);
                }
            }
            else {
                $("#checkAll").prop("checked", false);
            }
        });

        $('#sendMessage').click(function (e) {
            var confirmDel = confirm("Are you sure? Message will send to all Selected in list.");
            if (confirmDel == true) {
                showLoading("#create-form-holder");
                $.ajax({
                    url: "${createLink(controller: 'messaging', action: 'sendResultMessage')}",
                    type: 'POST',
                    dataType: "json",
                    data: $(".create-form").serialize(),
                    success: function (data) {
                        hideLoading("#create-form-holder");
                        if (data.isError == false) {
                            showSuccessMsg(data.message);
                            $("#succMsg").html(data.message);
                            $("#sent-btn-holder").hide();
                            $("#success-status-holder").show();
                        } else {
                            alert(data.message);
                        }
                    },
                    failure: function (data) {
                    }
                });
            }
        });

    });

</script>
</body>
</html>