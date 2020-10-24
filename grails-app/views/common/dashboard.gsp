<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="adminLayout"/>
    <title>Home</title>
</head>
<body>
<sec:ifAnyGranted roles="ROLE_TEACHER">
    <grailslab:breadCrumbActions breadCrumbTitleText="Dashboard" SHOW_CREATE_LINK="YES" createLinkText="Manage Routine" createLinkUrl="${g.createLink(controller: 'teacher', action: 'createRoutine')}"/>
</sec:ifAnyGranted>
<sec:ifNotGranted roles="ROLE_TEACHER">
    <grailslab:breadCrumbActions breadCrumbTitleText="Dashboard"/>
</sec:ifNotGranted>

<div class="row">
    <div class="col-md-12">
        <div class="event-calendar clearfix">
            <div class="col-md-9 calendar-block">
                <div id='user_calendar'></div>
            </div>
            <div class="col-md-3 event-list-block">
                <h3>Notifications</h3>
                <ul class="event-list">
                    <li>Class 3 Bangla @ 8:30</li>
                    <li>Student feedback and progress report preparation @ 9:30</li>
                    <li>Class 4 Bangla @ 10:15</li>
                    <li>Meeting with principle @ 12:30</li>
                </ul>
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
            slotDuration: {minutes:15},
            eventSources: [
                {
                    url: "${g.createLink(controller: 'remote',action: 'routineEvents')}",
                    data: function() { // a function that returns an object
                        return {
                            routineType: "teacherRoutine"
                        };
                    }
                }
            ],
            eventRender: function(event, element) {
                element.find('div.fc-title').html(element.find('div.fc-title').text()) ;
                /*var tooltip = new Tooltip(element.el, {
                    title: "some test",
                    placement: 'top',
                    trigger: 'hover',
                    container: 'body'
                });*/
            },
            /*eventClick: function(event, element) {
                $('#taskAndEventModal').modal('show');
                if ( !$(this).hasClass("syllabus-personal") ) {
                    if ( !$(this).hasClass("syllabus-class") ) {
                        $('#myModal2 #clicktitle').html(event.title);
                        $('#myModal2 #clickstart').html(event.tooltip);
                        $('#myModal2').modal('show');
                    }
                }
                if (event.editable  && event.eventId!=null) {

                    jQuery.ajax({
                        type: 'POST',
                        dataType:'JSON',
                        %{--url: "${g.createLink(controller: 'calendar',action: 'getTaskOrEvent')}?id=" + event.eventId,--}%
                        success: function (data, textStatus) {
                            if(data.isError==false){
                                $('div#taskAndEventModal #id').val(data.objId);
                                $('div#taskAndEventModal #name').val(data.name);
                                $('div#taskAndEventModal #startDate').val(data.startDate);
                                $('div#taskAndEventModal #startTime').val(data.startTime);
                                if(data.obj!=undefined && data.obj!=""){
                                    $('div#taskAndEventModal #endDate').val(data.endDate);
                                    $('div#taskAndEventModal #endTime').val(data.endTime);
                                    $('div#taskAndEventModal #shortNote').val(data.obj.shortNote);
                                    if(data.obj.repeatType.name =='WEEKLY'){
                                        var repeatDates =data.obj.repeatDates;
                                        var thisVal;
                                        $("#weekDiv").find('input[type=checkbox]').each(function () {
                                            thisVal = this.value;
                                            if ( repeatDates.indexOf(thisVal) !== -1 ){
                                                $(this).closest('.daypick').addClass('active');
                                                $(this).attr("checked", true);
                                            }
                                        });
                                        $(".r2").trigger( "click" );
                                    }
                                    $('#collapseTwo').addClass('in');
                                }

                                %{--$('#accordion').collapse('show')--}%
                            }else{
                                showErrorMsg(data.message);
                            }
                        },
                        error: function (XMLHttpRequest, textStatus, errorThrown) {
                        }
                    });

                    return false;
                }
                $('#user_calendar').fullCalendar('updateEvent', event);
            },*/
            header: {
                left: 'prev,next today',
                center: 'title',
                right: 'agendaWeek,agendaDay,listWeek'
            },
            defaultDate: new Date(),
            defaultView: 'agendaWeek'
        });
    });
</script>
</body>
</html>
