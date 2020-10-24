    <div class="modal-dialog">
        <div class="modal-content">
            <form class="form-horizontal" role="form" id="footNoteFormModal">
                <g:hiddenField name="id" id="id"/>
                <div class="modal-header">
                    <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                    <h4 class="modal-title" id="myModalLabel">Add Foot Note</h4>
                </div>
                <div class="modal-body">
                    <div class="row">
                        <div class="form-group">
                            <label for="footNote" class="col-md-2 control-label">Note</label>
                            <div class="col-md-9">
                                <g:textArea class="form-control" id="footNote" tabindex="3" name="footNote" placeholder="Add Foot Note" rows="5"/>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="modal-footer">
                    <button class="btn  btn-default" data-dismiss="modal" aria-hidden="true">Cancel</button>
                    <button type="button" class="btn btn-primary" id="footNoteSave">Save</button>
                </div>
            </form>
        </div>
    </div>
