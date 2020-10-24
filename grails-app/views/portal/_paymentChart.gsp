<div class="col-md-12" id="class-fee-list-holder">
    <div class="row">
        <form class="cmxform form-inline" role="form" method="post" id="create-form" action="${g.createLink(controller: 'portal',action: 'payInvoiceOnline')}">
            <g:hiddenField name="studentId" id="studentId" value="${student?.id}"/>
            <g:render template='/portal/feeReceiveFormOnline'/>
        </form>
    </div>
</div>