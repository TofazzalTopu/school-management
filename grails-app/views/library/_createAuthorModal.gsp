<div class="modal fade" data-backdrop="static" data-keyboard="false" id="author-modal" tabindex="-1" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true" style="display: none;">
    <div class="modal-dialog ">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">×</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="modalLabelId">Add/Edit</h4>
            </div>
        </div>
        <section class="panel">
            <div class="panel-body create-content">
                <form class="form-horizontal" role="form" id="author-modal-form" novalidate="novalidate">
                    <input type="hidden" name="id" id="hiddenId" value="">
                    <div class="modal-body">

                        <div class="form-group">
                            <label class="col-md-4 control-label"> Author Name <span class="required">*</span></label>
                            <div class="col-md-8">
                                <input type="text" name="name" id="name" class="form-control">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-4 control-label"> Bengali Name </label>
                            <div class="col-md-8">
                                <input type="text" name="bengaliName" id="bengaliName" class="form-control">
                            </div>
                        </div>

                        <div class="form-group">
                            <label class="col-md-4 control-label"> Country </label>
                            <div class="col-md-8">
                                <input type="text" name="country" id="country" class="form-control">
                            </div>
                        </div>

                        <div class="modal-footer modal-footer-action-btn">
                            <button type="button" class="btn btn-default" data-dismiss="modal" aria-hidden="true">Cancel</button>
                            <button type="submit" id="create-yes-btn" class="btn btn-large btn-primary">Submit</button>
                        </div>
                        <div class="modal-footer modal-refresh-processing" style="display: none;"><i class="fa fa-refresh fa-spin text-center"></i>
                        </div>
                    </div>
                </form>
            </div>
            <div class="create-success" style="display: none;">
                <div class="modal-body">
                    <div class="row">
                        <div class="col-md-2">
                            <img src="/gschool/assets/share-modal-icon.jpg" width="60" height="60">
                        </div>
                        <div class="col-md-10"><p class="message-content"></p></div>
                    </div>
                </div>
                <div class="modal-footer modal-footer-action-btn">
                    <button type="button" class="btn btn-default cancel-btn" data-dismiss="modal" aria-hidden="true">Close</button>
                </div>
            </div>
        </section>
    </div>
</div>