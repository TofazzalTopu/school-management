<%--
  Created by IntelliJ IDEA.
  User: Hasnat
  Date: 1/23/2015
  Time: 3:32 PM
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="open-ltpl"/>
    <title>Calender</title>

</head>

<body>
    <style>
        .fc-ltr .fc-basic-view .fc-day-number{
            text-align:center;
            color: #1FB5AD;
            font-weight: bold;
            font-size: 25px;
        }
        #public_calendar {
            margin-top: 20px;
        }
        .fc-widget-content {
            background-color: white;
        }
    </style>
    <section class="pageheader-default text-center">
        <div class="semitransparentbg">
            <h1 class="animated fadeInLeftBig notransition">Academic Calendar</h1>
        </div>
    </section>
    <div class="wrapsemibox">
        <section class="animated fadeInDown notransition">
            <div class="row blogindex">
                <!-- MAIN -->
                <div class="col-md-12">
                 <div id='public_calendar'></div>
                </div>
            </div>
        </section>
    </div>
    <script>
        $(document).ready(function() {
            %{--calendar starts--}%
            $('#public_calendar').fullCalendar({
                minTime: "07:30:00",
                firstDay:6,
                slotDuration: {minutes:20},
                eventSources: [
                    {
                        url: "${g.createLink(controller: 'calendar',action: 'publicEvents')}",
                        data: function() { // a function that returns an object
                            return {
                                section: $('#section').val(),
                                eventType: $('#eventType').val(),
                                employee: $('#employee').val()
                            };
                        },
                        error: function() {}
                    }
                ],
                eventRender: function(event, element) {
                    element.find('div.fc-title').html(element.find('div.fc-title').text()) ;
                },
                header: {
                    left: 'prev,next today',
                    center: 'title',
                    right: 'month,agendaWeek,agendaDay'
                },
                defaultDate: new Date(),
                defaultView: 'month'
            });
        });

    </script>
</body>
</html>