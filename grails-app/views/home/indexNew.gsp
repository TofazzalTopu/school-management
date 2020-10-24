<%@ page contentType="text/html;charset=UTF-8" %>
<%@ page defaultCodec="none" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta name="layout" content="newMain"/>
    <title>Home</title>
</head>
<body>
<div class="body_section">
    <div class="body_left">
        <div class="service_menu">
            <a href="http://www.educationboardresults.gov.bd/regular/index.php" target="_blank">
                <div class="info-box">
                    <div class="info-box-icon bg-aqua">
                        <img src="${assetPath(src: 'archive2.jpg')}">
                    </div>

                    <div class="info-box-content">
                        <div class="info-box-number">results archive</div>
                    </div>
                </div>
            </a>
            <a href="http://esif.teletalk.com.bd/" target="_blank">
                <div class="info-box">
                    <div class="info-box-icon bg-green">
                        <img src="${assetPath(src: 'oems.jpg')}">
                    </div>

                    <div class="info-box-content">
                        <div class="info-box-number">e-sif</div>
                    </div>
                </div>
            </a>
            <a href="#" target="_blank">
                <div class="info-box">
                    <div class="info-box-icon bg-red">
                        <img src="${assetPath(src: 'board.png')}">
                    </div>

                    <div class="info-box-content">
                        <div class="info-box-number">mpo information</div>
                    </div>
                </div>
            </a>
            <a href="http://eff.teletalk.com.bd/" target="_blank">
                <div class="info-box">
                    <div class="info-box-icon bg-green">
                        <img src="${assetPath(src: 'eff.jpg')}">
                    </div>

                    <div class="info-box-content">
                        <div class="info-box-number">e-Form Fill-up</div>
                    </div>
                </div>
            </a>

            <a href="#" target="_blank">
                <div class="info-box">
                    <div class="info-box-icon bg-yellow">
                        <img src="${assetPath(src: 'emis.jpg')}">
                    </div>

                    <div class="info-box-content">
                        <div class="info-box-number">e-mis</div>
                    </div>
                </div>
            </a>

            <a href="https://www.teachers.gov.bd/" target="_blank">
                <div class="info-box">
                    <div class="info-box-icon bg-red">
                        <img src="${assetPath(src: 'digital.jpg')}">
                    </div>

                    <div class="info-box-content">
                        <div class="info-box-number">digital content</div>
                    </div>
                </div>
            </a>
            <a href="http://www.moedu.gov.bd/" target="_blank">
                <div class="info-box info-box-big">
                    <div class="info-box-icon bg-success">
                        <img src="${assetPath(src: 'ministry.jpg')}">
                    </div>

                    <div class="info-box-content">
                        <div style="color:red" class="info-box-number">Ministry of Education</div>
                    </div>
                </div>
            </a>
            <a href="http://www.bangladesh.gov.bd/" target="_blank">
                <div class="info-box info-box-big">
                    <div class="info-box-icon bg-success">
                        <img src="${assetPath(src: 'bteb.jpg')}">
                    </div>

                    <div class="info-box-content">
                        <div style="color:red" class="info-box-number">Bangladesh Portal</div>
                    </div>
                </div>
            </a>
        </div>
    </div>

    <div class="body_right">
        <blockquote class="home">
            <table>
                <tbody>
                    <tr>
                        <td colspan="2">
                            <section class="carousel carousel-fade slide home-slider" id="c-slide" data-ride="carousel" data-interval="4500"
                                     data-pause="false">
                                <ol class="carousel-indicators">

                                    <g:each in="${homeCarousel}" var="dataSet" status="i">
                                        <li data-target="#c-slide" data-slide-to="${i}" class="${i == 0 ? 'active' : ''}"></li>
                                    </g:each>

                                </ol>

                                <div class="carousel-inner">

                                    <g:each in="${homeCarousel}" var="dataSetImg" status="j">
                                        <div class="item ${j == 0 ? 'active' : ''}"
                                             style="background: url('${imgSrc.fromIdentifier(imagePath: dataSetImg?.imagePath)}')"></div>
                                    </g:each>

                                </div>
                                <a class="left carousel-control animated fadeInLeft" href="#c-slide" data-slide="prev"><i
                                        class="icon-angle-left"></i></a>
                                <a class="right carousel-control animated fadeInRight" href="#c-slide" data-slide="next"><i
                                        class="icon-angle-right"></i></a>
                            </section>
                        </td>
                        <td class="product"><div class="product_title">Latest Notice</div>
                            <div class="product_body">
                                <notice:scrollTextSideNotice scrollamount="3" class="product_marquee" direction="down" behavior="scroll" ulClass="product_list" style="height: 240px;"/>
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
            <table>
                <tbody>
                    <tr>
                        <g:each in="${homeVoice}" var="openOvice" status="i">
                            <td class="product voice">
                                <div class="product_title">${openOvice.title}</div>
                                <div class="voice-description">
                                   %{-- ${openOvice.description}
                                    <br>
                                    -${openOvice.name}--}%
                                    <p class="bigquote text-left">
                                        <img alt="${openOvice.name}" class="pull-right testavatar" width="150" height="150" src="${imgSrc.fromIdentifier(imagePath: openOvice.imagePath)}">
                                        <i class="icon-quote-left colortext quoteicon"></i>${openOvice.description}<i class="icon-quote-right colortext quoteicon"></i>
                                    </p>
                                    <footer><a href="#">${openOvice.name}</a></footer>
                                </div>
                            </td>
                        </g:each>
                    </tr>
                </tbody>
            </table>
            <table>
                <tbody>
                    <tr>
                        <td class="product"><div class="product_title">Primary Section</div>
                            <div class="product_body">
                                <ul class="product_list">
                                    <g:each in="${primaryNotice}" var="notice">
                                        <li>
                                            <a target="_blank" href="${notice?.link}">
                                                ${notice?.scrollText}
                                            </a>
                                        </li>
                                    </g:each>
                                </ul>
                            </div>
                        </td>
                        <td class="product">
                            <div class="product_title">High Section</div>
                            <div class="product_body">
                                <ul class="product_list">
                                    <g:each in="${highSchoolNotice}" var="notice">
                                        <li>
                                            <a target="_blank" href="${notice?.link}">
                                                ${notice?.scrollText}
                                            </a>
                                        </li>
                                    </g:each>
                                </ul>
                            </div>
                        </td>
                        <td class="product">
                            <div class="product_title">College Section</div>
                            <div class="product_body">
                                <ul class="product_list">
                                    <g:each in="${collegeNotice}" var="notice">
                                        <li>
                                            <a target="_blank" href="${notice?.link}">
                                                ${notice?.scrollText}
                                            </a>
                                        </li>
                                    </g:each>
                                </ul>
                            </div>
                        </td>
                    </tr>
                </tbody>
            </table>
        </blockquote>
    </div>
    <div class="cb"></div>

    <script>
        $('.new-target-link').find('a').prop('target', '_blank');
        /*$('.voice-description').each(function () {
            var decode = $.parseHTML($(this).text());
            $(this).html(decode);
        });*/
    </script>
</div>
</body>
</html>