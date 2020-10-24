<div class="modal-dialog">
    <div class="modal-content">
        <form class="form-horizontal" role="form" id="createFormModal">
            <g:hiddenField name="id" id="id"/>
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"><span aria-hidden="true">&times;</span><span class="sr-only">Close</span></button>
                <h4 class="modal-title" id="myModalLabel">Salary Configuration </h4>
            </div>
            <div class="modal-body">
                <div class="row">
                        <div class="form-group">
                        <label for="extraClassRate" class="col-md-4 control-label">Extra Class Rate</label>
                        <div class="col-md-7">
                            <input class="form-control" type="text" name="extraClassRate"  id="extraClassRate"
                                   tabindex="2"/>
                        </div>
                    </div>


                    <div class="form-group">
                        <label for="lateFineForDays" class="col-md-4 control-label">Late Fine For Days</label>
                        <div class="col-md-7">
                            <input class="form-control" type="text" name="lateFineForDays" id="lateFineForDays"
                                   tabindex="2"/>
                        </div>
                    </div>

                    <div class="form-group">
                        <label for="pfContribution" class="col-md-4 control-label">PF Contribuation(%)</label>
                        <div class="col-md-7">
                            <input id="pfContribution" name="pfContribution" class="form-control"
                                      tabindex="3"/>
                        </div>
                    </div>
                    <div class="form-group">
                        <label for="pfCalField" class="col-md-4 control-label">PF Calculation</label>
                        <div class="col-md-7">
                            <select id="pfCalField" name="pfCalField" class="form-control">
                                <option value="basic">Basic</option>
                                <option value="grossSalary">Gross</option>
                                <option value="totalSalary">Total Salary</option>
                         </select>
                        </div>
                    </div>

                </div>

            </div>
            <div class="modal-footer">
                <button class="btn  btn-default" data-dismiss="modal" aria-hidden="true">Cancel</button>
                <button type="button" class="btn btn-primary" id="configbtn">Save</button>
            </div>
        </form>
    </div>
</div>



