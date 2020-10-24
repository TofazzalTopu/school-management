<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta name="layout" content="moduleParentsLayout"/>
    <title>Mark Sheet Chart</title>
</head>

<body>
    <div class="row">
        <div class="col-sm-12">
            <div class="row" id="bread-action-holder">
                <div class="col-md-5" style="padding-top:8px;">
                    <ul class="breadcrumbs-alt">
                        <li>
                            <a href="${g.createLink(controller: 'login', action: 'loginSuccess')}">Home</a>
                        </li>
                        <li>
                            <a class="current" href="#">Progress report</a>
                        </li>
                    </ul>
                    <!--breadcrumbs end -->
                </div>
                <div class="col-md-7">
                    <ul class="pull-right" style="padding-top:10px;">
                        <g:select class=" form-control " id="examName" name='examName'
                                  tabindex="1"
                                  from='${examNameList}'
                                  optionKey="id" optionValue="name"></g:select>
                    </ul>
                    <!--breadcrumbs end -->
                </div>
            </div>
        </div>
    </div>

    <div class="row">
        <div class="col-sm-12">
            <section class="panel">
                <div class="panel-heading">
                    First Term Examination
                </div>
                <div class="panel-body">
                    <div id="chart" style="min-width: 800px; height: 400px;"></div>
                </div>
            </section>
        </div>
    </div>
<script>
    jQuery(function ($) {
        $(function () {
            $('#chart').highcharts({
                chart: {
                    type: 'column'
                },
                title: {
                    text: 'Mark Comparison'
                },
                subtitle: {
                    text: ''
                },
                xAxis: {
                    categories: [
                        'Bangla',
                        'English',
                        'Mathematics',
                        'Social Science',
                        'General Science',
                        'Religion',
                        'ICT',
                        'History',
                        'Geometry',
                        'Economic'
                    ],
                    crosshair: true
                },
                yAxis: {
                    min: 0,
                    title: {
                        text: ''
                    }
                },
                tooltip: {
                    headerFormat: '<span style="font-size:10px">{point.key}</span><table>',
                    pointFormat: '<tr><td style="color:{series.color};padding:0">{series.name}: </td>' +
                    '<td style="padding:0"><b>{point.y:.1f}</b></td></tr>',
                    footerFormat: '</table>',
                    shared: true,
                    useHTML: true
                },
                plotOptions: {
                    column: {
                        pointPadding: 0.2,
                        borderWidth: 0
                    }
                },
                series: [{
                    name: 'Exam mark',
                    data: [100, 100, 100, 100, 100, 100, 100, 100, 100, 100]

                }, {
                    name: 'Highest Mark',
                    data: [83.00, 95.00, 98.00, 93.00, 89.00, 84.0, 95.0, 88.00, 91.00, 83.00]

                }, {
                    name: 'Average mark',
                    data: [67.00, 66.00, 69.00, 76.00, 61.00, 70.00, 62.00, 66.00, 59.00, 55.00]

                }, {
                    name: 'Obtain Mark',
                    data: [78.00, 78.00, 56.00, 57.00, 63.00, 78.00, 78.00, 51.00, 75.00, 81.00]

                }]
            });
        });

    });

</script>
</body>
</html>
</html>
