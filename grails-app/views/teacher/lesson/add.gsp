<!DOCTYPE html>
<head>
    <title>Lesson Plan</title>
    <meta name="layout" content="moduleLessonAndFeedbackLayout"/>
</head>
<body>
<grailslab:breadCrumbActions breadCrumbTitleText="Add Lesson Plan"/>
<div class="row">
    <div class="col-sm-12">
        <section class="panel">
            <header class="panel-heading">
                Lesson Plan By Week
            </header>
            <div class="panel-body">
                <div class="row" id="holderNo1">
                    <div class="form-horizontal" role="form">
                        <div class="form-group">
                            <label for="ssClassname" class="col-md-2 control-label">Class Name</label>
                            <div class="col-md-6">
                                <g:select class="form-control ss-classname-step" id="ssClassname" name='ssClassname' tabindex="4"
                                          noSelection="${['': 'Select Class...']}"
                                          from='${classNameList}'
                                          optionKey="id" optionValue="name" />
                            </div>
                        </div>
                        <div class="form-group">
                            <label for="ssSection" class="col-md-2 control-label">Section Name</label>
                            <div class="col-md-6">
                                <select name="ssSection" id="ssSection" class="form-control ss-section-step" tabindex="3" >
                                    <option value="">Select Section</option>
                                </select>
                            </div>
                        </div>




                        <div class="form-group">
                            <div class="col-sm-offset-2 col-sm-10">
                                <button class="btn btn-default" id="add-lesson-btn">Add Lesson Plan</button>
                                <button class="btn btn-default" id="manage-lesson-btn">Manage Lesson Plan</button>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    </div>
</div>
<script>
    var className, section, sectionId, actionParam;
    jQuery(function ($) {
        $('#holderNo1').cascadingDropdown({
            selectBoxes: [
                {
                    selector: '.ss-classname-step',
                    onChange: function (value) {
                        loadStudentSubSection('ssClassname','ssSection','holderNo1');
                    }
                },
                {
                    selector: '.ss-section-step',
                    requires: ['.ss-classname-step'],
                    onChange: function (value) {
                        loadActionButton('ssSection');
                    }
                }
            ]
        });

        function loadStudentSubSection(frmId, toId, holderId){
            className =$('#'+frmId).val();
            if (className) {
                loadSection(className, '#'+toId, "#"+holderId)
            }
        }
        function loadActionButton(frmId){
            section =$('#'+frmId).val();
            if (section) {
                $('#add-lesson-btn').removeClass('btn-default').addClass('btn-primary');
                $('#manage-lesson-btn').removeClass('btn-default').addClass('btn-primary');
            } else {
                $('#add-lesson-btn').removeClass('btn-primary').addClass('btn-default');
                $('#manage-lesson-btn').removeClass('btn-primary').addClass('btn-default');
            }
        }

        $('#ssClassname').on('change', function (e) {
            $('#ssSection').val("").trigger("change");
        });
        $('#add-lesson-btn').click(function (e) {
            e.preventDefault();
            sectionId = $('#ssSection').val();
            if(sectionId != ""){
                actionParam ="${g.createLink(controller: 'lesson',action: 'create','_blank')}?sectionId="+sectionId;
                window.open(actionParam);
            }

        });
        $('#manage-lesson-btn').click(function (e) {
            e.preventDefault();
            sectionId = $('#ssSection').val();
            if(sectionId != ""){
                actionParam ="${g.createLink(controller: 'lesson',action: 'lessonPlan','_blank')}?sectionId="+sectionId;
                window.open(actionParam);
            }

        });
    });
    function loadSection(className, sectionCtrl, loadingCtrl){
        showLoading(loadingCtrl);
        jQuery.ajax({
            type: 'POST',
            dataType: 'JSON',
            url: "${g.createLink(controller: 'remote',action: 'listSection')}?className="+className,
            success: function (data, textStatus) {
                hideLoading(loadingCtrl);
                if (data.isError == false) {
                    var $select = $(sectionCtrl);
                    $select.find("option:gt(0)").remove();
                    $.each(data.sectionList,function(key, value)
                    {
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