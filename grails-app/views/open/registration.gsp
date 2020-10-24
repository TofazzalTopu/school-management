<%@ page import="com.grailslab.settings.ClassName; com.grailslab.enums.Gender; com.grailslab.enums.Religion" defaultCodec="none" %>
<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="open-ltpl"/>
    <title>Registration</title>
    <style>
    .form-group {
        padding-right: 10px;
        padding-left: 10px;
    }
    </style>
</head>

<body>
<section class="pageheader-default text-center">
    <div class="semitransparentbg">
        <h1 class="animated fadeInLeftBig notransition">Admission Form</h1>
    </div>
</section>

<div class="wrapsemibox">

    <section class="container animated fadeInUp notransition">
        <div class="row">
            <div class="col-md-6 col-md-offset-3 animated fadeInLeft notransition">
                <br>
                <div align="center">
                    <h3 id="returnResult"></h3>
                </div>

                <form class="form-horizontal" role="form" id="create-form">
                    <g:hiddenField name="id" id="id"/>
                    <div class="form">
                        <div class="row">
                            <div class="form-group">
                                <input class="col-md-10 form-control" type="text" name="name" placeholder="Name"
                                       id="name">
                            </div>

                            <div class="form-group">
                                <input class="form-control" type="email" name="username"
                                       placeholder="E-mail"
                                       id="username">
                            </div>

                            <div class="form-group">
                                <input class="form-control" type="password" name="password" id="password"
                                       placeholder="Password">
                            </div>

                            <div class="form-group">
                                <input class="form-control" type="password" name="confirm_password" id="confirm_password"
                                       placeholder="Confirm Password">
                            </div>

                        </div>

                        <div class="row">
                            <div class="form-group">
                                <div class="col-md-offset-8 col-lg-4">
                                    <button class="btn btn-default cancel-btn" type="reset">Cancel</button>
                                    <button class="btn btn-primary btn-submit" type="submit">Save</button>
                                </div>
                            </div>
                        </div>
                    </div>
                </form>
            </div>
        </div>
    </section>
</div>


<script>

    $('#birthDate').datepicker({
        format: 'dd/mm/yyyy',
        autoclose: true
    });

    jQuery(function ($) {

        var validator = $('#create-form').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                name: {
                    required: true,
                    maxlength: 200
                },
                username: {
                    required: true,
                    maxlength: 100
                },
                password: {
                    required: true,
                    maxlength: 100
                },
                confirm_password: {
                    required: true
                },
            },
            errorPlacement: function (error, element) {

            },

            highlight: function (e) {
                $(e).closest('.form-group').removeClass('has-info').addClass('has-error');
            },
            success: function (e) {
                $(e).closest('.form-group').removeClass('has-error').addClass('has-info');
                $(e).remove();
            },
            submitHandler: function (form) {
                if ($('#password').val() != $('#confirm_password').val()) {
                    alert('Password Missmatch');
                    return false;
                }
                $.ajax({
                    url: "${createLink(controller: 'home', action: 'save')}",
                    type: 'post',
                    dataType: "json",
                    data: $("#create-form").serialize(),
                    success: function (data) {
                        if (data.isError == false) {
                            $('#returnResult').html(data.message);
                            console.log(data);
                        } else {
                            $('#returnResult').html(data.message);
                            console.log(data);
                        }

                    },
                    failure: function (data) {
                    }
                })
            }
        });
    });
</script>
</body>
</html>