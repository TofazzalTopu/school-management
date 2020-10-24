<%@ page defaultCodec="none"%>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="open-ltpl"/>
    <title>Progress TimeLine</title>
</head>
<body>

<div class="wrapbox">

    <section class="pageheader-default text-center">
        <div class="semitransparentbg">
            <h1 class="animated fadeInLeftBig notransition">Progress Time Line</h1>
        </div>
    </section>

    <div class="wrapsemibox">
        <div class="animated fadeInUpNow notransition fadeInUp topspace10">
            <section class="container animated fadeInUpBig notransition">
                <div class="row">
                    <div class="col-md-12">
                        <ul class="cbp_tmtimeline">

                            <g:each in="${timelineList}" var="dataSet">
                                <li>
                                    <time class="cbp_tmtime"><span>${dataSet?.title}</span></time>
                                    <div class="cbp_tmicon">
                                        <i class="${dataSet?.iconClass}"></i>
                                    </div>

                                    <div class="cbp_tmlabel">
                                        <h2>${dataSet?.name}</h2>
                                        ${dataSet?.description}
                                    </div>
                                </li>
                            </g:each>
                        </ul>
                    </div>
                </div>
            </section>
        </div>
    </div>
</div>

</body>
</html>
