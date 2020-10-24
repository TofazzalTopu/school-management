<%@ page defaultCodec="none" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="newMain"/>
    <title>Home</title>
</head>
<body>
    <div class="wrapsemibox">
        <section class="container">
            <div class="col-md-7 animated fadeInLeft notransition">
                <g:each in="${schoolContent}" var="dataSet">

                    <h1 class="smalltitle">
                        <span>${dataSet?.title}</span>
                    </h1>
                    ${dataSet?.description}
                </g:each>

            </div>

            <div class="col-md-4 animated fadeInRight notransition">
                <g:each in="${facilityContent}" var="dataSet">
                    <h1 class="smalltitle">
                        <span>${dataSet?.title}</span>
                    </h1>
                    <blockquote>
                        ${dataSet?.description}
                    </blockquote>
                </g:each>
            </div>
        </section>
    </div>
</div>

</body>
</html>
