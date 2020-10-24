<g:if test="${it}">
    <g:if test="${it instanceof java.util.ArrayList}">
        <g:each in="${it}" var="obj">
            <g:hasErrors bean="${obj}">
                <g:set var="errorsInList" value="${true}"/>
            </g:hasErrors>
        </g:each>
        <g:if test="${errorsInList}">
            <!-- BEGIN ALERT -->
            <div class="alert alert-block alert-danger fade in">
                <button type="button" class="close" data-dismiss="alert"></button>
                <g:each in="${it}" var="obj">
                    <g:hasErrors bean="${obj}">
                        <p><g:renderErrors bean="${obj}"/></p>
                    </g:hasErrors>
                </g:each>
            </div>
            <!-- END ALERT -->
        </g:if>
    </g:if>
    <g:else>
        <g:hasErrors bean="${it}">
            <!-- BEGIN ALERT -->
            <div class="alert alert-block alert-danger fade in">
                <button type="button" class="close" data-dismiss="alert"></button>
                <p><g:renderErrors bean="${it}"/></p>
            </div>
            <!-- END ALERT -->
        </g:hasErrors>
    </g:else>
</g:if>