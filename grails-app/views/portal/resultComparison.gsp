<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleParentsLayout"/>
    <title>Result Comparason Chart</title>
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
                Result Progress
            </div>
            <!-- /.panel-heading -->
            <div class="panel-body">
                <div id="bar-example"></div>
            </div>
        </section>
    </div>
</div>

<script>
    Morris.Bar({
        element: 'bar-example',
        data: [
            { y: 'Bangla', a: 100, b: 90, c:89 },
            { y: 'English', a: 75,  b: 65, c:89 },
            { y: 'Mathematics', a: 50,  b: 40, c:89 },
            { y: 'Science', a: 75,  b: 65, c:89 },
            { y: 'Social Science', a: 50,  b: 40, c:89 },
            { y: 'Religion', a: 75,  b: 65, c:89},
            { y: 'ICT', a: 100, b: 90, c:89 }
        ],
        xkey: 'y',
        ykeys: ['a', 'b', 'c'],
        labels: ['1st Trem ', '2nd Trem ', 'Final']
    });
</script>
</body>
</html>