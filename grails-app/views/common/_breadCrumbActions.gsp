<div class="row" id="bread-action-holder">
    <div class="col-md-5" style="padding-top:8px;">
        <!--breadcrumbs start -->
        <ul class="breadcrumbs-alt">
            <li>
                <a href="${g.createLink(controller: 'login',action: 'loginSuccess')}">Home</a>
            </li>
            <g:if test="${firstBreadCrumbUrl}">
                <li>
                    <a  href="${firstBreadCrumbUrl}">${firstBreadCrumbText}</a>
                </li>
            </g:if>
            <g:if test="${secondBreadCrumbUrl}">
                <li>
                    <a  href="${secondBreadCrumbUrl}">${secondBreadCrumbText}</a>
                </li>
            </g:if>
            <g:if test="${thirdBreadCrumbUrl}">
                <li>
                    <a  href="${thirdBreadCrumbUrl}">${thirdBreadCrumbText}</a>
                </li>
            </g:if>
            <g:if test="${breadCrumbTitleText}">
                <li>
                    <a class="current" href="#">${breadCrumbTitleText}</a>
                </li>
            </g:if>
        </ul>
        <!--breadcrumbs end -->
    </div>
    <div class="col-md-7">
        <!--breadcrumbs start -->
        <ul class="pull-right" style="padding-top:10px;">
                <g:if test="${SHOW_CREATE_BTN}">
                    <div class="btn-group btn-margin-left">
                        <a class="btn btn-primary create-new-btn" href="javascript:void(0);">
                            <i class="${createBtnIcon}"></i> ${createButtonText}
                        </a>
                    </div>
                </g:if>
            %{--<sec:access url="/${controllerName}/${linkBtnAction}">--}%
                <g:if test="${SHOW_LINK_BTN}">
                    <div class="btn-group btn-margin-left">
                        <a class="btn btn-success link-url-btn" href="#">
                            <i class="${linkBtnIcon}"></i> ${linkBtnText}
                        </a>
                    </div>
                </g:if>
            %{--</sec:access>--}%
                <g:if test="${SHOW_LIST_BTN}">
                    <div class="btn-group btn-margin-left">
                        <a class="btn btn-info" href="${listLinkUrl}">
                            <i class="${listBtnIcon}"></i> ${listBtnText}
                        </a>
                    </div>
                </g:if>
                <g:if test="${SHOW_CREATE_LINK}">
                    <div class="btn-group btn-margin-left">
                        <a class="btn btn-primary" href="${createLinkUrl}">
                            <i class="${createLinkIcon}"></i> ${createLinkText}
                        </a>
                    </div>
                </g:if>
                <g:if test="${SHOW_EXTRA_BTN1}">
                    <div class="btn-group btn-margin-left">
                        <a class="btn btn-primary extra-btn-1" href="javascript:void(0);">
                            <i class="${extraBtn1Icon}"></i> ${extraBtn1Text}
                        </a>
                    </div>
                </g:if>
                <g:if test="${SHOW_PRINT_BTN}">
                    <div class="btn-group btn-margin-left">
                        <a class="btn btn-primary print-btn" href="javascript:void(0);">
                            <i class="${printBtnIcon}"></i> ${printBtnText}
                        </a>
                    </div>
                </g:if>
        </ul>
        <!--breadcrumbs end -->
    </div>
</div>