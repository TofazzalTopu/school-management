<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Class Routine</title>
    <meta name="layout" content="moduleStdMgmtLayout"/>
</head>

<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Class Routine" SHOW_CREATE_LINK="YES" createLinkText="Add New" createLinkUrl="${g.createLink(controller: 'classRoutine', action: 'classRoutineCreate')}"/>
<div class="row" >
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Class Routine
            </header>
            <div class="panel-body">
                <div class="row" id="class-routine-holder">
                    <form class="form-horizontal" role="form" id="create-form">
                        <div class="col-md-12">
                            <div class="form-group">
                                <label for="academicYear" class="col-md-1 control-label">Year</label>
                                <div class="col-md-3">
                                    <g:select tabindex="1" class="form-control academic-year-step"
                                              id="academicYear" name='academicYear'
                                              noSelection="${['': 'Select Year...']}"
                                              from='${academicYearList}'
                                              optionKey="key" optionValue="value"></g:select>
                                </div>
                                <label for="className" class="col-md-1 control-label">Class</label>
                                <div class="col-md-3">
                                    <g:select class=" form-control class-name-step" id="className" name='className'
                                              tabindex="2"
                                              noSelection="${['': 'Select Class...']}"
                                              from='${classNameList}'
                                              optionKey="id" optionValue="name"></g:select>
                                </div>
                                <label for="section" class="col-md-1 control-label">Section</label>
                                <div class="col-md-3">
                                    <select class="form-control section-name-step" id="section" name='section'>
                                        <option  value="">Select Section</option>
                                    </select>
                                </div>
                            </div>
                        </div>
                    </form>
                </div>
            </div>
        </section>
    </div>
</div>


<div class="row">
    <div class="col-md-12">
        <div class="event-calendar clearfix">
            <div class="col-md-12 calendar-block">
                <div id='user_calendar'></div>
            </div>
        </div>
    </div>
</div>

<script>
    var academicYear, className, subject, remoteUrl, section;
    jQuery(function ($) {
        $('#user_calendar').fullCalendar({
            minTime: "07:30:00",
            firstDay:6,
            slotDuration: {minutes:12},
            header: {
                left: 'prev,next today',
                center: 'title',
                right: 'agendaWeek,agendaDay,listWeek'
            },
            defaultDate: new Date(),
            defaultView: 'agendaWeek',
            eventSources: [
                {
                    url: "${g.createLink(controller: 'remote',action: 'routineEvents')}",
                    data: function() { // a function that returns an object
                        return {
                            routineType: "classRoutine",
                            sectionId: $('#section').val()
                        };
                    }

                }
            ],
            eventRender: function(event, element) {
                element.find('div.fc-title').html(element.find('div.fc-title').text()) ;
            }
        })


        $('#class-routine-holder').cascadingDropdown({
            selectBoxes: [
                {
                    selector: '.academic-year-step',
                    onChange: function(value){
                        loadSectionNames();
                    }
                },
                {
                    selector: '.class-name-step',
                    requires: ['.academic-year-step'],
                    onChange: function (value) {
                        loadSectionNames();
                    }
                },
                {
                    selector: '.section-name-step',
                    requires: ['.class-name-step']
                }
            ]
        });


        $('#section').change(function () {
            var sectionId = $('#section').val();
            if (sectionId) {
                $('#user_calendar').fullCalendar( 'refetchEvents' );
            }
        });
    });

    function loadSectionNames(){
        academicYear =$('#academicYear').val();
        className =$('#className').val();
        if(academicYear && className){
            remoteUrl = "${g.createLink(controller: 'remote',action: 'classSectionList')}?className="+className+"&academicYear="+academicYear;
            loadClassSection(remoteUrl, $('#section'),"#class-routine-holder");
        }
    }
</script>
</body>
</html>


