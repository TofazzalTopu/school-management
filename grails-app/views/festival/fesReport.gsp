<%@ page import="com.grailslab.enums.ScheduleStatus" %>
<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleWebLayout"/>
    <title> Registration Report</title>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Program  Report "/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Program  Report
            </header>
            <div class="panel-body">
                <form class="form-inline" role="form">
                        <div class="form-group">
                                <g:select class=" form-control " id="fesProgram" name='fesProgram'
                                          tabindex="1"  noSelection="${['': 'Select Program..']}"
                                          from='${fesProgramList}'
                                          optionKey="id" optionValue="name"></g:select>

                        </div>
                        <div class="form-group ">
                            <select name="topicName" id="topicName" class="form-control" tabindex="2">
                                <option value="">Select Topic</option>
                            </select>

                        </div>
                        <div class="form-group">
                            <g:select class="form-control" id="studentPrintOptionType" name='studentPrintOptionType'
                                      from='${com.grailslab.enums.PrintOptionType.values()}'
                                      optionKey="key" optionValue="value"></g:select>
                        </div>
                        <button type="button" id="studentReportBtn" class="btn btn-primary">Show Report</button>
                </form>
            </div>
        </section>
    </div>
</div>

<script>
    var reportParam,fesProgram ,printOptionType,topicName,reportSortType,reportType;
    jQuery(function ($) {
        $('#fesProgram').on('change', function (e) {
            var fesProgram = $('#fesProgram').val();
            loadOlympiadtopicChange(fesProgram, 'topicName');
        });

        $('#studentReportBtn').click(function (e) {
            e.preventDefault();
            fesProgram=$('#fesProgram').find("option:selected").val();
            topicName = $('#topicName').find("option:selected").val();
            printOptionType = $('#studentPrintOptionType').find("option:selected").val();
            reportParam ="${g.createLink(controller: 'festival',action: 'registrationList','_blank')}?fesProgram="+fesProgram+"&topicName="+topicName+"&printOptionType="+printOptionType;
            window.open(reportParam);
        });
    });
    function loadOlympiadtopicChange(fesProgram, controlName) {
        jQuery.ajax({
            type: 'POST',
            dataType: 'JSON',
            url: "${g.createLink(controller: 'festival',action: 'registrationTopics')}?id="+fesProgram,
            success: function (data, textStatus) {
                if (data.isError == false) {
                    var $select = $('#'+controlName);
                    $select.find('option').remove();
                    $select.append('<option value="">All Topics</option>');
                    $.each(data.registrationTopicList, function (key, value) {
                        $select.append('<option value=' + value.id + '>' + value.name + '</option>');
                    });
                } else {
                    showErrorMsg(data.message);
                }
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
            }
        });
    }
</script>

</body>
</html>