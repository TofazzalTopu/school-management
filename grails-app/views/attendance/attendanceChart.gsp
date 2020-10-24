<%@ page import="com.grailslab.enums.SelectionTypes" %>
<!DOCTYPE html>
<html xmlns="http://www.w3.org/1999/html">
<head>
    <meta name="layout" content="moduleParentsLayout"/>
    <title>My Attendance</title>
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
                        <a class="current" href="#">My Attendance</a>
                    </li>
                </ul>
            </div>
            <div class="col-md-7">
                <ul class="pull-right" style="padding-top:10px;">
                    <g:select class=" form-control " id="attnMonth" name='attnMonth'
                              tabindex="1" value="${currMonth?.toUpperCase()}"
                              from='${com.grailslab.enums.YearMonths.values()}'
                              optionKey="key" optionValue="value"></g:select>
                </ul>
            </div>
        </div>
    </div>
</div>

<div class="row" id="attnHolder">
    <div class="col-sm-12">
        <section class="panel">
            <div id="page-wrapper">
                <!-- /.row -->
                <div class="col-lg-12">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Attendance
                        </div>
                        <!-- /.panel-heading -->
                        <div id="bar-example"></div>
                        <div class="panel-body">
                            <div id="subjectmarkchart" style="min-width: 80px; height: 10px;"></div>
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                </div>

                <!-- /.col-lg-6 -->
            </div>
            <!-- /.row -->
        </section>
    </div>
</div>

<script>
    var myAttnChart;
    jQuery(function ($) {
        myAttnChart = Morris.Bar({
            element: 'bar-example',
            data: [
                { y: '01/10', a: 09.35 },
                { y: '02/10', a: 09.00 },
                { y: '03/10', a: 09.32 },
                { y: '04/10', a: 09.02 },
                { y: '05/10', a: 09.99 },
                { y: '06/10', a: 09.98 },
                { y: '07/10', a: 09.02 },
                { y: '08/10', a: 09.09 },
                { y: '09/10', a: 09.00 },
                { y: '10/10', a: 09.23 },
                { y: '11/10', a: 09.56 },
                { y: '12/10', a: 09.43 },
                { y: '13/10', a: 09.04 },
                { y: '14/10', a: 09.03 },
                { y: '15/10', a: 09.12 },
                { y: '16/10', a: 09.00 },
                { y: '17/10', a: 10.00 },
                { y: '18/10', a: 09.06 },
                { y: '19/10', a: 09.08 },
                { y: '20/10', a: 09.08 },
                { y: '21/10', a: 09.43 },
                { y: '22/10', a: 09.13 },
                { y: '23/10', a: 09.00 },
                { y: '24/10', a: 09.32 },
                { y: '25/10', a: 9.34 },
                { y: '26/10', a: 09.99 },
                { y: '27/10', a: 09.08 },
                { y: '28/10', a: 09.21 },
                { y: '29/10', a: 09.22 }
            ],
            xkey: 'y',
            ykeys: ['a'],
            labels: ['In Time'],
            barColors: function (row, series, type) {
                if (row.y == "09.00") {
                    return "#0b62a4";
                } else if (row.y == "09.99"){
                    return "red";
                }
                return "#1AB244";
            },
            hoverCallback: function (index, options, content, row) {
                if (row.a == "09.00") {
                    return row.y+"<br/>Holiday";
                } else if (row.a == "09.99"){
                    return row.y+"<br/>Absent";
                }
                return row.y+"<br/>In Time: "+row.a;
            }
        });
        loadAttndance();
        $('#attnMonth').on('change', function (e) {
            showLoading("#attnHolder");
            loadAttndance($(this).val());
            hideLoading("#attnHolder");
        });
    });
    function loadAttndance(monthName) {
        $.ajax({
            url: "${createLink(controller: 'attendance', action: 'getAttendance')}",
            type: 'post',
            dataType: "json",
            data: {'currMonth':monthName},
            success: function (data) {
                myAttnChart.setData(data.attndanceList);
            },
            failure: function (data) {
            }
        });
    }
   </script>


<div class="col-md-6 ">
    <div class="panel panel-default">
        <div class="panel-heading">
            <span class="panel-header-title">Present status</span><span class="panel-header-info">(${academicYear})</span>
        </div>
        <!-- /.panel-heading -->
        <div class="panel-body">

            <div id="grade1Chart"  class="center-block"  style=" width: 340px; height: 200px;"></div>
        </div>
        <!-- /.panel-body -->
    </div>
    <!-- /.panel -->
</div>
<!-- /.col-md-6 -->
<div class="col-md-6">
    <div class="panel panel-default">
        <div class="panel-heading">
            <span class="panel-header-title">Present status</span><span class="panel-header-info">(${currMonth})</span>
        </div>
        <!-- /.panel-heading -->
        <div class="panel-body">
            <div id="grade2Chart" class="center-block" style="width: 340px; height: 200px;"></div>
        </div>
        <!-- /.panel-body -->
    </div>
    <!-- /.panel -->
</div>

<div class="col-md-6 ">
    <div class="panel panel-default">
        <div class="panel-heading">
            <span class="panel-header-title">Attendance status</span><span class="panel-header-info">(${academicYear})</span>
        </div>
        <!-- /.panel-heading -->
        <div class="panel-body">

            <div id="grade3Chart"  class="center-block"  style=" width: 340px; height: 200px;"></div>
        </div>
        <!-- /.panel-body -->
    </div>
    <!-- /.panel -->
</div>
<!-- /.col-md-6 -->
<div class="col-md-6">
    <div class="panel panel-default">
        <div class="panel-heading">
            <span class="panel-header-title">Attendance status</span><span class="panel-header-info">(${currMonth})</span>
        </div>
        <!-- /.panel-heading -->
        <div class="panel-body">
            <div id="grade4Chart" class="center-block" style="width: 300px; height: 200px;"></div>
        </div>
        <!-- /.panel-body -->
    </div>
    <!-- /.panel -->
</div>

   <script>
        jQuery(function ($) {
            $(function () {
                $('#grade1Chart').highcharts({
                    chart: {
                        plotBackgroundColor: null,
                        plotBorderWidth: null,
                        plotShadow: false,
                        type: 'pie'
                    },


                    title: {
                        text: '',
                        style: {
                            display: 'none'
                        }
                    },

                    // Remove button menu for highcharts exporting add
                    exporting: false,

                    //For address false to credits add
                    credits: {
                        enabled: false
                    },
                    tooltip: {
                        pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
                    },
                    plotOptions: {
                        pie: {
                            allowPointSelect: true,
                            cursor: 'pointer',
                            dataLabels: {
                                enabled: true,
                                format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                                style: {
                                    color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                                }
                            }
                        }
                    },


                    series: [{
                        colorByPoint: true,
                        data: [{

                            name: 'Present ',
                            color: 'green',
                            y: 70.33


                        }, {
                            name: 'Absent',
                            color: 'orange',
                            y: 29.67
                        }]
                    }]
                });
            });
            $('#grade2Chart').highcharts({
                chart: {
                    plotBackgroundColor: null,
                    plotBorderWidth: null,
                    plotShadow: false,
                    type: 'pie'
                },

                title: {
                    text: '',
                    style: {
                        display: 'none'
                    }
                },

                // Remove button menu for highcharts exporting add
                exporting:false,

                //For address false to credits add
                credits: {
                    enabled: false
                },
                tooltip: {
                    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
                },
                plotOptions: {
                    pie: {
                        allowPointSelect: true,
                        cursor: 'pointer',
                        dataLabels: {
                            enabled: true,
                            format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                            style: {
                                color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                            }
                        }
                    }
                },


                series: [{
                    colorByPoint: true,
                    data: [{
                        colorByPoint: true,
                        name: 'Present',
                        color: 'green',
                        y:  75.00


                    },   {

                        name: 'Absent',
                        color: 'orange',
                        y: 25.00


                    }]
                }]
            });

            $(function () {
                $('#grade3Chart').highcharts({
                    chart: {
                        plotBackgroundColor: null,
                        plotBorderWidth: null,
                        plotShadow: false,
                        type: 'pie'
                    },


                    title: {
                        text: '',
                        style: {
                            display: 'none'
                        }
                    },

                    // Remove button menu for highcharts exporting add
                    exporting: false,

                    //For address false to credits add
                    credits: {
                        enabled: false
                    },
                    tooltip: {
                        pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
                    },
                    plotOptions: {
                        pie: {
                            allowPointSelect: true,
                            cursor: 'pointer',
                            dataLabels: {
                                enabled: true,
                                format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                                style: {
                                    color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                                }
                            }
                        }
                    },


                    series: [{
                        colorByPoint: true,
                        data: [{

                            name: 'In Time ',
                            color: 'green',
                            y: 92.00


                        }, {
                            name: 'Late',
                            color: 'orange',
                            y: 08.00
                        }]
                    }]
                });
            });
            $('#grade4Chart').highcharts({
                chart: {
                    plotBackgroundColor: null,
                    plotBorderWidth: null,
                    plotShadow: false,
                    type: 'pie'
                },

                title: {
                    text: '',
                    style: {
                        display: 'none'
                    }
                },

                // Remove button menu for highcharts exporting add
                exporting:false,

                //For address false to credits add
                credits: {
                    enabled: false
                },
                tooltip: {
                    pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
                },
                plotOptions: {
                    pie: {
                        allowPointSelect: true,
                        cursor: 'pointer',
                        dataLabels: {
                            enabled: true,
                            format: '<b>{point.name}</b>: {point.percentage:.1f} %',
                            style: {
                                color: (Highcharts.theme && Highcharts.theme.contrastTextColor) || 'black'
                            }
                        }
                    }
                },


                series: [{
                    colorByPoint: true,
                    data: [{
                        name: 'In Time',
                        color: 'green',
                        y:  88.00


                    },   {

                        name: 'Late',
                        color: 'orange',
                        y: 12.00


                    }]
                }]
            });
        });

    </script>

</body>
</html>