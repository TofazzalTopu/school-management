<%@ page import="com.grailslab.CommonUtils" contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="open-ltpl"/>
    <title>Online Application</title>
</head>

    <body>
            <section class="pageheader-default text-center">
                <div class="semitransparentbg">
                    <h1 class="animated fadeInLeftBig notransition">Online Application</h1>
                </div>
            </section>

            <div class="wrapsemibox">
                <div class="container animated fadeInUpNow notransition fadeInUp topspace0">
                    <div class="col-md-10 col-md-offset-1 animated slidea notransition anim-slide">
                        <g:if test="${flash.message}">
                            <h4 class="text-center" style="color: sienna">${flash.message}</h4>
                        </g:if>
                        <div class="col-md-7 animated slidea notransition anim-slide">
                            <g:if test="${applyList || regFormList}">

                                    <h3 class="smalltitle">
                                        <span>Select One to Apply</span>
                                    </h3>
                                    <g:each in="${regFormList}" var="regForm" status="i">
                                        <g:if test="${regForm.regOpenDate.before(new Date()) && regForm.regCloseDate.after(new Date().minus(1))}">
                                            <h4><i class="icon-hand-right"></i> <a href="${g.createLink(action: 'step1', params: [id: regForm.id])}">${regForm.className.name} </a> </h4>
                                            <g:if test="${regForm.bandSchool}">
                                                <p style="padding-left:22px; color: red;" class="animated fadeInLeftBig notransition">${regForm.bandSchool}</p>
                                            </g:if>
                                        </g:if>
                                        <g:else>
                                            <h4><i class="icon-hand-right"></i> <a href="javascript:void(0)">${regForm.className.name} </a> </h4>
                                        </g:else>
                                           %{--<p style="padding-left:22px" class="animated fadeInLeftBig notransition">Registration Open: <g:formatDate format="dd-MMM-yyyy" date="${regForm.regOpenDate}"/> to <g:formatDate format="dd-MMM-yyyy" date="${regForm.regCloseDate}"/></p>--}%
                                    </g:each>

                                    %{--<g:each in="${applyList}" var="fesProgram" status="i">
                                        <h4><i class="icon-hand-right"></i> <a href="${g.createLink(action: 'apply', params: [id: fesProgram.id])}">${fesProgram.name} </a> </h4>
                                        <p style="padding-left:22px" class="animated fadeInLeftBig notransition">Registration Open: <g:formatDate format="dd-MMM-yyyy" date="${fesProgram.regOpenDate}"/> to <g:formatDate format="dd-MMM-yyyy" date="${fesProgram.regCloseDate}"/></p>
                                    </g:each>--}%
                            </g:if>
                            <g:else>
                                <h3>Application Date yet not declared. Keep an eye for update or contact office for further information</h3>
                            </g:else>
                        </div>
                        <div class="col-md-5 animated slidea notransition anim-slide">
                            <h3 class="smalltitle">
                                <span>Already Applyed?</span>
                            </h3>
                            <form role="form" class="form-horizontal" method="get" action="${g.createLink(controller: 'online', action: 'preview')}">
                                <div class="input-group">
                                    <input type="text" name="refNo" id="refNo" placeholder="Enter your serial no or applycation no" class="form-control">
                                    <span class="input-group-btn">
                                        <button type="submit" class="btn btn-default">
                                            Go! <i class="fa fa-angle-double-right"></i>
                                        </button>
                                    </span>
                                </div>
                            </form>
                        </div>
                    </div>
                </div>
            </div>
    </body>
</html>