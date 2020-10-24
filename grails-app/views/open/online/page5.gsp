<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="open-ltpl"/>
      <title>Online Application - Step5 </title>
</head>

<body>
    <div class="wrapbox">
        <section class="pageheader-default text-center">
            <div class="semitransparentbg">
                <h1 class="animated fadeInLeftBig notransition">Step 5</h1>
            </div>
        </section>
        <div class="wrapsemibox">
            <div class="container animated fadeInUpNow notransition fadeInUp topspace0">
                <div class="col-md-offset-1 col-md-10">
                    <h1 class="smalltitle">
                        <span>Upload Photo</span>
                    </h1>
                    <g:render template="/layouts/errors" bean="${command?.errors}"/>
                    <form role="form"  enctype="multipart/form-data" class="form-horizontal"method="post"action="${g.createLink(controller: 'online',action: 'preview')}">
                        <g:hiddenField name="regId" value="${registration?.id}"/>
                        <div class="row">
                            <div class="form-group">
                                <label  class="control-label col-md-2">Applicants Image <span class="required">*</span></label>
                                <div class="col-md-4">
                                    <input type="file" name="pImage" id="pImage" />
                                    <p>Miximum Size : 100kb, Ratio Width :Height=7:9</p>
                                </div>
                            <div class="col-md-4">
                                <g:if test="${registration?.imagePath}">

                                    <img src="${imgSrc.fromIdentifier(imagePath: registration?.imagePath)}" id="ImagePreview" width="200px" height="170px"/>

                                    </div>
                                </g:if>
                                <g:else>
                                    <asset:image src="no-image.jpg" alt="avatar" id="ImagePreview" width="200px" height="170px"/>
                                </g:else>
                            </div>
                        </div>
                        <div class="row pull-right">
                            <div class="form-group">
                                <button type="button" class="btn btn-default preBtn">Previous</button>
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
                $('#pTcDate').datepicker({
                    format: 'mm/dd/yyyy',
                    startDate: '-3d'
                })
            });

            $("#pImage").change(function () {
                readImageURL(this);
            });

            function readImageURL(input) {
                if (input.files && input.files[0]) {
                    var reader = new FileReader();
                    reader.onload = function (e) {
                        $('#ImagePreview').attr('src', e.target.result);
                    };
                    reader.readAsDataURL(input.files[0]);
                }
            }

            $('.preBtn').click(function (){
                window.history.back();
            })
        });
    </script>
</body>
</html>