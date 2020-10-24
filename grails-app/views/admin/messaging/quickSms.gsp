<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleSmsLayout"/>
    <title>Send Quick Message</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Send Message to number(s)"/>
<div class="row" id="create-form-holder">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Write Your Message with comma separated number(s) to send
            </header>
            <div class="panel-body">
                <form class="form-horizontal" role="form" id="create-form">
                    <div class="row">
                        <div class="form-group">
                            <label class="col-md-2 control-label">Phone Number(s)<span class="required">*</span></label>

                            <div class="col-md-6">
                                <g:textArea rows="5" class="form-control" id="phoneNumbers" tabindex="1" name="phoneNumbers"
                                            placeholder="Insert comma separated Phone Number(s)"/>
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-2 control-label">Text Message<span class="required">*</span></label>

                            <div class="col-md-6">
                                <textarea id="textMessage" rows="5" maxlength="479" name="textMessage" required="" class="form-control" placeholder="Write message"></textarea>
                            </div>
                        </div>
                    </div>

                    <div class="row">
                        <div class="form-group">
                            <div class="col-md-offset-8 col-lg-4">
                                <button class="btn btn-primary btn-submit" tabindex="11" type="submit">Send Message</button>
                                <button class="btn btn-default cancel-btn" tabindex="12" type="reset">Cancel</button>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </section>
    </div>
</div>
<script>
    var confirmMsg, numberOfMsg;
    jQuery(function ($) {
        var schoolName = "${grailsApplication.config.powersms.send.from.text}";
        var txt = $("textarea#textMessage");
        txt.val( txt.val() + "\n\n"+schoolName);
        $('#textMessage').maxlength({
            alwaysShow: true,
            threshold: 479,
            warningClass: "label label-info",
            limitReachedClass: "label label-warning",
            placement: 'bottom',
            message: 'used %charsTyped% of %charsTotal% chars. English 159 & Bangla 67 characters per message.'
        });
        var validator = $('#create-form').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                phoneNumbers: {
                    required: true,
                    minlength: 11,
                    maxlength: 4000
                },
                textMessage: {
                    required: true,
                    minlength: 3,
                    maxlength: 479
                }
            },
            highlight: function (e) {
                $(e).closest('.form-group').removeClass('has-info').addClass('has-error');
            },
            success: function (e) {
                $(e).closest('.form-group').removeClass('has-error').addClass('has-info');
                $(e).remove();
            },
            submitHandler: function (form) {
                var smsTextVal = $("#textMessage").val();
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
                        url: "${createLink(controller: 'messaging', action: 'sendQuickSms')}",
                        type: 'post',
                        dataType: "json",
                        data: $("#create-form").serialize(),
                        success: function (data) {
                            hideLoading("#create-form-holder");
                            if (data.isError == false) {
                                clearForm(form);
                                showSuccessMsg(data.message);
                            } else {
                                showErrorMsg(data.message);
                            }
                        },
                        failure: function (data) {
                        }
                    })
                }
            }
        });
        $(".cancel-btn").click(function () {
            var validator = $("#create-form").validate();
            validator.resetForm();
        });
    });
</script>
</body>
</html>