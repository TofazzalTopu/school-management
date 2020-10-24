<grailslab:fullModal label="Personal Info Of ${employee?.name}" formId="formPersonalInfo" modalId="modalPersonalInfo"
                     modalClass="modal-big"
                     hiddenId="formPersonalInfoId">

    <div class="form-group">
        <label class="col-md-2 control-label">Name<span class="required">*</span></label>

        <div class="col-md-4">
            <input type="text" name="name" id="name" class="form-control"/>
        </div>

        <label class="col-md-2 control-label">Birth Date <span class="required">*</span></label>

        <div class="col-md-4">
            <input type="text" name="birthDate" id="birthDate" class="form-control"/>
        </div>
    </div>

    <div class="form-group">
        <label class="col-md-2 control-label">Fathers Name <span class="required">*</span></label>

        <div class="col-md-4">
            <input type="text" name="fathersName" id="fathersName" class="form-control"/>
        </div>

        <label class="col-md-2 control-label">Mothers Name <span class="required">*</span></label>

        <div class="col-md-4">
            <input type="text" name="mothersName" id="mothersName" class="form-control"/>
        </div>
    </div>

    <div class="form-group">
        <label class="col-md-2 control-label">Present Add<span class="required">*</span></label>

        <div class="col-md-4">
            <textarea name="presentAddress" id="presentAddress" class="form-control"></textarea>
        </div>

        <label class="col-md-2 control-label">Permanent Add<span class="required">*</span></label>

        <div class="col-md-4">
            <textarea name="permanentAddress" id="permanentAddress" class="form-control"></textarea>
        </div>
    </div>

    <div class="form-group">
        <label class="col-md-2 control-label">Email <span class="required">*</span></label>

        <div class="col-md-4">
            <input type="text" name="emailId" id="emailId" class="form-control"/>
        </div>

        <label class="col-md-2 control-label">Blood Group </label>

        <div class="col-md-4">
            <input type="text" name="bloodGroup" id="bloodGroup" class="form-control"/>
        </div>

    </div>

    <div class="form-group">

        <label class="col-md-2 control-label">National ID</label>

        <div class="col-md-4">
            <input type="text" name="nationalId" id="nationalId" class="form-control"/>
        </div>

        <label class="col-md-2 control-label">Facebook Link</label>

        <div class="col-md-4">
            <input type="text" name="fbId" id="fbId" class="form-control"/>
        </div>
    </div>

    <div class="form-group">
        <label class="col-md-2 control-label">Mobile No<span class="required">*</span></label>

        <div class="col-md-4">
            <input type="text" name="mobile" id="mobile" class="form-control"/>
        </div>

        <label class="col-md-2 control-label">Card No</label>

        <div class="col-md-4">
            <input type="text" name="cardNo" id="cardNo" class="form-control"/>
        </div>
    </div>

    <div class="form-group">
        <label class="col-md-2 control-label">About Me <span class="required">*</span></label>
        <div class="col-md-5">
            <textarea name="aboutMe" id="aboutMe" class="form-control" rows="10"></textarea>
        </div>
        <label class="col-md-2 control-label">Profile Image</label>
        <div class="col-md-3">
            <input type="file" name="pImage" id="pImage"/>
            <br/>
            <g:if test="${employee?.imagePath}">
                <img src="${imgSrc.fromIdentifier(imagePath: employee?.imagePath)}" id="ImagePreview" alt=" " class="thumbnail"
                     width="190px" height="190px">
            </g:if>
            <g:else>
                <asset:image src="no-image.jpg" alt="avatar" id="ImagePreview" width="190px" height="190px" class="thumbnail"/>
            </g:else>
        </div>
    </div>

</grailslab:fullModal>
<script>
    $(document).ready(function () {
        $("#pImage").change(function () {
            readImageURL(this);
        });
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
</script>