<!DOCTYPE html>
<head>
    <title>Lesson Plan</title>
    <meta name="layout" content="open-ltpl"/>
</head>
<body>
<section class="pageheader-default text-center">
    <div class="semitransparentbg">
        <h1 class="animated fadeInLeftBig notransition">Lesson Plan</h1>
    </div>
</section>

<div class="wrapsemibox">
    <div class="row">
        <div class="col-sm-12">
            <section>
                <header class="panel-heading">
                    <span class="panel-header-info">Select Class, Section and Week Number to see Lesson Plan</span>
                </header>
                <div class="panel-body">
                    <div class="form-horizontal">
                        <div class="form-group">
                            <div class="col-md-2">
                                <g:select class=" form-control" id="studentClassName" name='studentClassName' tabindex="1"
                                          noSelection="${['': 'All Class...']}"
                                          from='${classNameList}'
                                          optionKey="id" optionValue="name"></g:select>

                            </div>
                            <div class="col-md-2">
                                <g:select tabindex="1" class="form-control" id="section"
                                          name='section'
                                          noSelection="${['': 'Select Section']}"
                                          from='${sectionList}'
                                          optionKey="id" optionValue="name"></g:select>

                            </div>
                            <div class="col-md-2">
                                <g:select tabindex="3" class="form-control" id="weekNo"
                                          name='weekNo' value="${loadWeek}"
                                          noSelection="${['': 'Working Week(s)...']}"
                                          from='${lessonWeekList}'
                                          optionKey="id" optionValue="name"></g:select>
                            </div>
                            <div class="col-md-2">
                                <g:select tabindex="4" class="form-control" id="subjectName"
                                          name='subjectName' value="${loadSubject?.id}"
                                          noSelection="${['': 'All Subject...']}"
                                          from='${subjectList}'
                                          optionKey="id" optionValue="name"></g:select>
                            </div>
                            <div class="col-md-2">
                                <button class="btn btn-info" id="load-btn">Load Lesson Plan</button>
                            </div>
                            <div class="col-md-2 ">
                                <button class="btn btn-info" id="print-btn">Print </button>
                            </div>
                        </div>
                    </div>
                </div>

            </section>
        </div>
    </div>
</div>
<script>
    var className,section;
    jQuery(function ($) {
        $('#studentClassName').on('change', function (e) {
            className = $('#studentClassName').val();

            if (className) {
                loadSectionExam( className, '#section')
            }
            $('#section').val("").trigger("change");


        });

        jQuery(function ($) {
            $('#load-btn').click(function (e) {
                var weekNo = $('#weekNo').val();
                var subjectName = $('#subjectName').val();
                 var section = $('#section').val();
                if((weekNo != "")||(section != "")){
                    window.location.href = "${g.createLink(controller: 'home',action: 'lessonPlan')}/"+section+"?weekNo="+weekNo+"&subjectId="+subjectName;
                }
                e.preventDefault();
            });
        });
        $('.print-btn').click(function (e) {
            e.preventDefault();
           var  section = $('#studentSection').val();
            var weekNo = $('#weekNo').val();
            var printSubjectName = $('#subjectName').val();
            //var sectionId = "${section?.id}";
          if(weekNo != ""){
                var sectionParam ="${g.createLink(controller: 'lessonPlan',action: 'download')}/"+section+"&weekNo="+weekNo+"&subject="+printSubjectName;
                window.open(sectionParam);
            }
            return false; // avoid to execute the actual submit of the form.
        });
    });




</script>
</body>
</html>