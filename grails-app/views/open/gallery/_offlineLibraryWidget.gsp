<div class="container-fluid">
    <div class="row">
        <g:each in="${categoryList}" var="dataSet">
            <div class="col-lg-3" style="background-color:lavender;">
                <nav class="navbar navbar-expand-lg navbar-light bg-light">
                    <a href="${g.createLink(controller: 'home', action: 'offlineLibrary', id: dataSet.id)}">
                        <h5 class="text-center">${dataSet.name}</h5>
                        <p class="text-center">${dataSet.description}</p>
                    </a>
                </nav>
            </div>
        </g:each>
    </div>
</div>
