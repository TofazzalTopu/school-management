<%@ page import="com.grailslab.enums.SelectionTypes" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleSmsLayout"/>
    <title>Send SMS</title>
</head>
<body>
<grailslab:breadCrumbActions firstBreadCrumbText="Messaging" firstBreadCrumbUrl="${g.createLink(controller: 'messaging',action: 'index')}" breadCrumbTitleText="Send Message"/>

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
                        <div class="col-md-10">


                            <div class="form-group">

                                <label for="message" class="col-md-4 control-label">Type message</label>

                                <div class="col-md-8">
                                    <textarea id="message" rows="5" maxlength="479" name="message" required="" class="form-control" placeholder="Write message">${draftMessage}</textarea>
                                </div>
                            </div>
                        </div>
                        <div class="row">
                            <div class="form-group">
                                <div class="col-md-6 col-md-offset-6">
                                    <div class="col-md-4">

                                    </div>
                                    <div class="col-md-4">
                                        <div class="btn btn-large btn-info" id="sendMessage">Send Message</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </section>
        </div>
    </div>

    <div class="row" id="list-form-holder">
        <div class="col-sm-12">
            <section class="panel">
                    <header class="panel-heading">
                        Send Message List
                    </header>
                <div class="panel-body">
                    <div class="table-responsive">
                        <table class="table table-striped table-hover table-bordered" id="list-table">
                            <thead>
                            <tr>
                                <th>Serial</th>
                                <th>${selectListHeader}</th>
                                <th>Mobile</th>
                                <th><input type="checkbox" name="checkAll" id="checkAll" checked></th>
                            </tr>
                            </thead>
                            <tbody>
                            <g:each in="${studentList}" var="dataSet" status="i">
                                <tr>
                                    <td>${i + 1}</td>
                                    <td>${dataSet?.name}</td>
                                    <td>${dataSet?.mobile}</td>
                                    <td>
                                        <input type="hidden" name="mobileNos_${dataSet?.id}" value="${dataSet?.mobile}"/>
                                        <input type="checkbox" name="selectIds" class="checkSingle" value="${dataSet?.id}"
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
    var confirmMsg, numberOfMsg;
    jQuery(function ($) {
        var schoolName = "${grailsApplication.config.powersms.send.from.text}";
        var txt = $("textarea#message");
        txt.val( txt.val() + "\n\n"+schoolName);

        $('#message').maxlength({
            alwaysShow: true,
            threshold: 479,
            warningClass: "label label-info",
            limitReachedClass: "label label-warning",
            placement: 'bottom',
            message: 'used %charsTyped% of %charsTotal% chars. English 159 & Bangla 67 characters per message.'
        });

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
                });
                if (isAllChecked == 0) {
                    $("#checkAll").prop("checked", true);
                }
            }
            else {
                $("#checkAll").prop("checked", false);
            }
        });

        $('#sendMessage').click(function (e) {
            var smsTextVal = $("#message").val();
            if ( preventUnicodeSms(smsTextVal)=== true){
                numberOfMsg = 1;
                if (smsTextVal.length > 70) {
                    numberOfMsg = Math.ceil(smsTextVal.length/67);
                }
                confirmMsg = "Are you sure? \n\nYour Message contains non ascii (unicode/Bangla) character and count 67 characters per message. \n\n"+numberOfMsg+" message will send to each number."
            } else {
                numberOfMsg = 1;
                if (smsTextVal.length > 160) {
                    numberOfMsg = Math.ceil(smsTextVal.length/153);
                }
                confirmMsg = "Are you sure? \n\n"+numberOfMsg+" message will send to each number."
            }
            var confirmDel = confirm(confirmMsg);
            if (confirmDel == true) {
                showLoading("#create-form-holder");
                $.ajax({
                    url: "${createLink(controller: 'messaging', action: 'sendMessage')}",
                    type: 'POST',
                    dataType: "json",
                    data: $(".create-form").serialize(),
                    success: function (data) {
                        hideLoading("#create-form-holder");
                        if (data.isError == false) {
                            $("#id").val('');
                            $("#message").val('');
                            showSuccessMsg(data.message);
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