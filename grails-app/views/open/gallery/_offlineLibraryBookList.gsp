<div class="container-fluid">
    <div class="bs-docs-grid show-grid">
        <div class="row">
            <g:each in="${bookList}" var="book">
                <div class="col-md-3">
                    ${book.name}
                </div>
            </g:each>
        </div>
    </div>
</div>

