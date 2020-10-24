<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <title>Teacher Routine</title>
    <meta name="layout" content="moduleStdMgmtLayout"/>
</head>

<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Teacher Routine" SHOW_CREATE_LINK="YES" createLinkText="Add New" createLinkUrl="${g.createLink(controller: 'classRoutine', action: 'teacherRoutineCreate')}"/>
<div class="row" >
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Teacher Routine
            </header>
            <div class="panel-body">
                <div class="row" id="teacher-search-holder">
                    <form class="form-horizontal" role="form" id="create-form">
                        <div class="col-md-12">
                            <div class="form-group">
                                <label for="employeeId" class="col-md-1 control-label col-md-offset-2">Teacher  <span class="required"></span></label>
                                <div class="col-md-6">
                                    <input type="hidden" class="form-control"  id="employeeId" name="employee" tabindex="2" />
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
            eventSources: [
                {
                    url: "${g.createLink(controller: 'remote',action: 'routineEvents')}",
                    data: function() { // a function that returns an object
                        return {
                            routineType: "teacherRoutine",
                            employee: $('#employeeId').val()
                        };
                    }
                }
            ],
            eventRender: function(event, element, view) {
                element.find('div.fc-title').html(element.find('div.fc-title').text()) ;
            },
           /* eventClick: function(event, element) {
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
            defaultView: 'agendaWeek',
        });

        $('#employee').select2({
            placeholder: "Search for a Employee [employeeId/name]",
            allowClear: true,
            minimumInputLength:3,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: "${g.createLink(controller: 'remote',action: 'employeeList')}",
                dataType: 'json',
                quietMillis: 250,
                data: function (term, page) {
                    return {
                        q: term
                    };
                },
                results: function (data, page) { // parse the results into the format expected by Select2.
                    // since we are using custom formatting functions we do not need to alter the remote JSON data
                    return { results: data.items };
                },
                cache: true
            },
            formatResult: repoFormatResult, // omitted for brevity, see the source of this page
            formatSelection: repoFormatSelection,  // omitted for brevity, see the source of this page
            dropdownCssClass: "bigdrop", // apply css that makes the dropdown taller
            escapeMarkup: function (m) { return m; } // we do not want to escape markup since we are displaying html in results
        });

        $('#employeeId').select2({
            placeholder: "Search for [employeeId/name]",
            allowClear: true,
            minimumInputLength:3,
            ajax: { // instead of writing the function to execute the request we use Select2's convenient helper
                url: "${g.createLink(controller: 'remote',action: 'employeeList')}",
                dataType: 'json',
                quietMillis: 250,
                data: function (term, page) {
                    return {
                        q: term
                    };
                },
                results: function (data, page) { // parse the results into the format expected by Select2.
                    // since we are using custom formatting functions we do not need to alter the remote JSON data
                    return { results: data.items };
                },
                cache: true
            },
            formatResult: repoFormatResult, // omitted for brevity, see the source of this page
            formatSelection: repoFormatSelection,  // omitted for brevity, see the source of this page
            dropdownCssClass: "bigdrop", // apply css that makes the dropdown taller
            escapeMarkup: function (m) { return m; } // we do not want to escape markup since we are displaying html in results
        });

        $('#employeeId').change(function () {
            var employeeId = $('#employeeId').val();
            if (employeeId) {
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

    function loadSubjectNames(){
        className =$('#className').val();
        section =$('#section').val();
        if(className && section){
            remoteUrl = "${g.createLink(controller: 'remote',action: 'sectionSubjectList')}?id="+section;
            loadSectionSubject(remoteUrl, className, section, $('#subjectName'),"#class-routine-holder");
        }
    }

</script>
</body>
</html>
