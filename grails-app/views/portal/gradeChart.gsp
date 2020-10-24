<%@ page import="com.grailslab.enums.SelectionTypes" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleParentsLayout"/>
    <title>Grade Average Chart </title>
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

            <div id="page-wrapper">

                <!-- /.row -->
                <div class="row">
                    <div class="col-md-12">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                Student Letter Grade Male and Female
                            </div>
                            <!-- /.panel-heading -->
                            <div class="panel-body">

                            <div id="gradeChart" class="center-block" style="width: 500px; height: 550px;"></div>

                            </div>
                            <!-- /.panel-body -->
                        </div>
                        <!-- /.panel -->
                    </div>
                    <!-- /.col-md-6 -->
                    <div class="col-md-6 ">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                Letter Grade: A+
                            </div>
                            <!-- /.panel-heading -->
                            <div class="panel-body">

                            <div id="grade1Chart"  class="center-block"  style=" width: 300px; height: 200px;"></div>
                            </div>
                            <!-- /.panel-body -->
                        </div>
                        <!-- /.panel -->
                    </div>
                    <!-- /.col-md-6 -->
                    <div class="col-md-6">
                        <div class="panel panel-default">
                            <div class="panel-heading">
                                Letter Grade: A
                            </div>
                            <!-- /.panel-heading -->
                            <div class="panel-body">
                            <div id="grade2Chart" class="center-block" style="width: 300px; height: 200px;"></div>
                            </div>
                            <!-- /.panel-body -->
                        </div>
                        <!-- /.panel -->
                    </div>
                    <!-- /.col-md-6 -->

                <div class="col-md-6">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Letter Grade: A-
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            <div id="grade3Chart" class="center-block" style="width: 300px; height: 200px;"></div>
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                </div>
                <!-- /.col-md-6 -->
                <div class="col-md-6">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Letter Grade: B
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            <div id="grade4Chart" class="center-block" style="width: 300px; height: 200px;"></div>
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                </div>
                <!-- /.col-md-6 -->


                <div class="col-md-6">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Letter Grade: C
                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            <div id="grade5Chart" class="center-block" style="width: 300px; height: 200px;"></div>
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                </div>
                <!-- /.col-md-6 -->
                <div class="col-md-6">
                    <div class="panel panel-default">
                        <div class="panel-heading">
                            Letter Grade: F

                        </div>
                        <!-- /.panel-heading -->
                        <div class="panel-body">
                            <div id="grade6Chart" class="center-block" style="width: 300px; height: 200px;"></div>
                        </div>
                        <!-- /.panel-body -->
                    </div>
                    <!-- /.panel -->
                </div>
                <!-- /.col-md-6 -->



                    <!-- /.col-md-6 -->

                    <!-- /.col-md-6 -->
                </div>
                <!-- /.row -->
            </div>

        </section>
    </div>
</div>

<script>

    jQuery(function ($) {

        $(function () {
            $('#gradeChart').highcharts({
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
                    name: 'Letter Grade',
                    colorByPoint: true,
                    data: [{
                        name: 'A-',
                        y: 56.33
                    }, {
                        name: 'B',
                        y: 24.03,
                        sliced: true,
                        selected: true
                    }, {
                        name: 'C',
                        y: 10.38
                    }, {
                        name: 'D',
                        y: 4.77
                    }, {
                        name: 'F',
                        y: 0.91
                    }, {
                        name: 'A+',
                        y: 0.2
                    }]
                }]
            });
        });

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
                    name: 'Letter Grade : A+',
                    colorByPoint: true,
                    data: [{

                        name: 'Male ',
                        color: 'green',
                        y:  56.33

                    },   {

                        name: 'Female',
                        color: 'orange',
                        y: 25.00


                    }]
                }]
            });
        });

        $(function () {
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
                    name: 'Letter Grade : A+',
                    colorByPoint: true,
                    data: [{

                        name: 'Male ',
                        color: 'green',
                        y:  56.33

                    },   {

                        name: 'Female',
                        color: 'orange',
                        y: 25.00


                    }]
                }]
            });
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
                    name: 'Letter Grade : A+',
                    colorByPoint: true,
                    data: [{

                        name: 'Male ',
                        color: 'green',
                        y:  56.33

                    },   {

                        name: 'Female',
                        color: 'orange',
                        y: 25.00


                    }]
                }]
            });
        });



        $(function () {
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
                    name: 'Letter Grade : A+',
                    colorByPoint: true,
                    data: [{

                        name: 'Male ',
                        color: 'green',
                        y:  56.33

                    },   {

                        name: 'Female',
                        color: 'orange',
                        y: 25.00


                    }]
                }]
            });
        });



        $(function () {
            $('#grade5Chart').highcharts({
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
                    name: 'Letter Grade : A+',
                    colorByPoint: true,
                    data: [{

                        name: 'Male ',
                        color: 'green',
                        y:  56.33

                    },   {

                        name: 'Female',
                        color: 'orange',
                        y: 25.00


                    }]
                }]
            });
        });



        $(function () {
            $('#grade6Chart').highcharts({
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
                    name: 'Letter Grade : A+',
                    colorByPoint: true,
                    data: [{

                        name: 'Male ',
                        color: 'green',
                        y:  56.33

                    },   {

                        name: 'Female',
                        color: 'orange',
                        y: 25.00


                    }]
                }]
            });
        });
    });


</script>
</body>
</html>