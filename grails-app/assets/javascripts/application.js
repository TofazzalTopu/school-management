// This is a manifest file that'll be compiled into application.js.
//
// Any JavaScript file within this directory can be referenced here using a relative path.
//
// You're free to add application-wide JavaScript to this file, but it's generally better 
// to create separate JavaScript files as needed.
//

//= require jquery-3.4.0.min
//= require jquery-migrate-1.4.1.min
//= require bootstrap.min
//= require jquery.dcjqaccordion.2.7
//= require jquery.scrollTo.min
//= require jquery.slimscroll.min
//= require jquery.nicescroll
//= require jquery.validate.min
//= require bootstrap-growl.min
//= require jquery.isloading
//= require angularJs/angular.min
//= require bootstrap-datepicker
//= require bootstrap-timepicker.min
//= require jquery.form.min
//= require jquery.dataTables.min
//= require dataTables.bootstrap.min
//= require select2.min
//= require jquery.glSalarySetup
//= require open/lightGallery.min
//= require open/jquery.mixitup.min
//= require math.min
//= require login
//= require bootstrap-switch
//= require bootstrap-rating.min
//= require css3clock/js/css3clock
//= require moment.min
//= require fullcalendar-3.9.0/fullcalendar.min
// require fullcalendar-4.0.1/core/main
// require fullcalendar-4.0.1/interaction/main
// require fullcalendar-4.0.1/daygrid/main
//= require jquery.treegrid
//= require jquery.treegrid.bootstrap3
//= require raphael-min
//= require morris.min
//= require highcharts
//= require jquery.bootstrap-duallistbox
//= require jquery.cascadingdropdown.min
//= require jquery.numeric
//= require bootstrap-colorpicker.min
//= require bootstrap-maxlength.min
// require popper.min
// require tooltip.min
//= require bootbox
//= require scripts
//= require_self

if (typeof jQuery !== 'undefined') {
    (function ($) {
        $('#spinner').ajaxStart(function () {
            $(this).fadeIn();
        }).ajaxStop(function () {
            $(this).fadeOut();
        });
    })(jQuery);
}

function formSuccess(data) {
    $('#myModal .create-content .modal-refresh-processing').hide();
    $('#myModal .create-success .modal-footer-action-btn').show();
    if (data.isError == true) {
        $('#myModal .create-success p.message-content').html(data.message);
    } else {
        $('#myModal .create-success .message-content').html(data.message);
    }
    $('#list-table').DataTable().ajax.reload();
    $('#myModal .create-content').hide(1000);
    $('#myModal .create-success').show(1000);
    $('#myModal .create-content .modal-footer-action-btn').show();
}

function getActionBtn(nRow, aData) {
    var actionButtons = "";
    actionButtons += '<span class="col-md-4 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="edit-reference" title="Edit">';
    actionButtons += '<span class="fa fa-pencil-square-o"></span></a></span>';
    actionButtons += '<span class="col-md-4 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="inactive-reference" title="Inactive">';
    actionButtons += '<span class="green fa fa-retweet"></span></a></span>';
    actionButtons += '<span class="col-md-4 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="delete-reference" title="Delete">';
    actionButtons += '<span class="fa fa-trash"></span></a></span>';
    return actionButtons;
}
function getEditDeleteBtn(nRow, aData) {
    var actionButtons = "";
    actionButtons += '<span class="col-md-4 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="edit-reference" title="Edit">';
    actionButtons += '<span class="fa fa-pencil-square-o"></span></a></span>';
    actionButtons += '<span class="col-md-4 no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="delete-reference" title="Delete">';
    actionButtons += '<span class="fa fa-trash"></span></a></span>';
    return actionButtons;
}


function getActionBtnCustom(nRow, aData,actions) {
    var actionButtons = "";
    var actionsArray = actions.split(",");
    var length = actionsArray.length;
    var arraySiz=parseInt(12/length, 10);
    var count=1;
    $.each( actionsArray, function( key, value ) {
        actionButtons += '<span class="col-md-'+arraySiz+' no-padding"><a href="" referenceId="' + aData.DT_RowId + '" class="reference-'+count+'">';
        actionButtons += '<span class="'+value+'"></span></a></span>';
        count++
    });
    return actionButtons;
}

function getGridActionBtns(nRow, aData, actions) {
    var actionButtons = "";
    var actionsArray = actions.split(",");
    var length = actionsArray.length;
    var arraySiz=parseInt(12/length, 10);
    var count=1;
    $.each( actionsArray, function( key, value ) {
        if (value.trim() === 'edit') {
            actionButtons += '<span class="col-md-'+arraySiz+' no-padding"><a href="javascript:void(0)" referenceId="' + aData.DT_RowId + '" class="edit-reference" title="Edit">';
            actionButtons += '<span class="fa fa-pencil-square-o"></span></a></span>';
        } else  if (value.trim() === 'editBook') {
            actionButtons += '<span class="col-md-'+arraySiz+' no-padding"><a href="javascript:void(0)" referenceId="' + aData.DT_RowId + '" class="edit-reference" title="Edit">';
            actionButtons += '<span class="fa fa-pencil-square-o"></span></a></span>';
        } else if (value.trim() === 'editDps') {
            actionButtons += '<span class="col-md-'+arraySiz+' no-padding"><a href="javascript:void(0)" referenceId="' + aData.DT_RowId + '" class="editDps-reference" title="Edit">';
            actionButtons += '<span class="fa fa-pencil-square-o"></span></a></span>';
        } else if (value.trim() === 'confirm') {
            actionButtons += '<span class="col-md-'+arraySiz+' no-padding"><a href="javascript:void(0)" referenceId="' + aData.DT_RowId + '" class="confirm-reference" title="Confirm Book Return">';
            actionButtons += '<span class="fa fa-book"></span></a></span>';
        } else if (value.trim() === 'return') {
            actionButtons += '<span class="col-md-'+arraySiz+' no-padding"><a href="javascript:void(0)" referenceId="' + aData.DT_RowId + '" class="return-reference" title="Book Return">';
            actionButtons += '<span class="fa fa-book"></span></a></span>';
        }
        else if (value.trim() === 'editTrans') {
            actionButtons += '<span class="col-md-'+arraySiz+' no-padding"><a href="javascript:void(0)" referenceId="' + aData.DT_RowId + '" class="editTrans-reference" title="Edit">';
            actionButtons += '<span class="fa fa-pencil-square-o"></span></a></span>';
        }
        else if (value.trim() === 'inactive') {
            actionButtons += '<span class="col-md-'+arraySiz+' no-padding"><a href="javascript:void(0)" referenceId="' + aData.DT_RowId + '" class="status-reference" title="Change Status">';
            actionButtons += '<span class="fa-retweet"></span></a></span>';
        } else if (value.trim() === 'delete') {
            actionButtons += '<span class="col-md-'+arraySiz+' no-padding"><a href="javascript:void(0)" referenceId="' + aData.DT_RowId + '" class="delete-reference" title="Delete">';
            actionButtons += '<span class="fa fa-trash"></span></a></span>';
       }
        else if (value.trim() === 'deleteTrans') {
            actionButtons += '<span class="col-md-'+arraySiz+' no-padding"><a href="javascript:void(0)" referenceId="' + aData.DT_RowId + '" class="deleteTrans-reference" title="Delete">';
            actionButtons += '<span class="fa fa-trash"></span></a></span>';
        }
       else if (value.trim() === 'reply') {
            actionButtons += '<span class="col-md-'+arraySiz+' no-padding"><a href="javascript:void(0)" referenceId="' + aData.DT_RowId + '" class="reply-reference" title="Approve or reject Leave">';
            actionButtons += '<span class="fa fa-share-square"></span></a></span>';
        } else if (value.trim() === 'print') {
            actionButtons += '<span class="col-md-'+arraySiz+' no-padding"><a href="javascript:void(0)" referenceId="' + aData.DT_RowId + '" class="print-reference" title="Print">';
            actionButtons += '<span class="fa fa-print"></span></a></span>';
        }
        else if (value.trim() === 'loan') {
            actionButtons += '<span class="col-md-'+arraySiz+' no-padding"><a href="javascript:void(0)" referenceId="' + aData.DT_RowId + '" class="loan-reference" title=" Add Loan">';
            actionButtons += '<span class="glyphicon glyphicon-plus"></span></a></span>';
        } else if (value.trim() === 'ss') {
            actionButtons += '<span class="col-md-'+arraySiz+' no-padding"> <a href="javascript:void(0)" referenceId="' + aData.DT_RowId + '" class="ss-reference" title="Salary  sheet">';
            actionButtons += '<span class="fa fa-print"></span></a></span>';
       } else if (value.trim() === 'bs') {
            actionButtons += '<span class="col-md-'+arraySiz+' no-padding"> <a href="javascript:void(0)" referenceId="' + aData.DT_RowId + '" class="bs-reference" title="Bank Statement">';
            actionButtons += '<span class="fa fa-cc-mastercard"></span></a></span>';
        } else if (value.trim() === 'pf') {
            actionButtons += '<span class="col-md-'+arraySiz+' no-padding"> <a href="javascript:void(0)" referenceId="' + aData.DT_RowId + '" class="pf-reference" title="Profident Fund Statement">';
            actionButtons += '<span class="fa fa-cc-mastercard"></span></a></span>';
        } else if (value.trim() === 'payslip') {
            actionButtons += '<span class="col-md-'+arraySiz+' no-padding"> <a href="javascript:void(0)" referenceId="' + aData.DT_RowId + '" class="payslip-reference" title="Pay slip">';
            actionButtons += '<span class="fa fa-money"></span></a></span>';
       } else if (value.trim() === 'disburse') {
            actionButtons += '<span class="col-md-'+arraySiz+' no-padding"> <a href="javascript:void(0)" referenceId="' + aData.DT_RowId + '" class="disbursement-reference" title="Disburse Salary/Bonus">';
            actionButtons += '<span class="glyphicon glyphicon-ok" style="color: #1528ff"></span></a></span>';
        } else if (value.trim() === 'admit') {
            actionButtons += '<span class="col-md-'+arraySiz+' no-padding"> <a href="javascript:void(0)" referenceId="' + aData.DT_RowId + '" class="admit-reference" title="Admit print">';
            actionButtons += '<span class="fa fa-print"></span></a></span>';
        } else if (value.trim() === 'editCt') {
            actionButtons += '<span class="col-md-'+arraySiz+' no-padding"><a href="javascript:void(0)" referenceId="' + aData.DT_RowId + '" class="editCt-reference" title="Edit Ct Mark">';
            actionButtons += '<span class="fa fa-pencil-square-o"></span></a></span>';
        } else if (value.trim() === 'footNote') {
            actionButtons += '<span class="col-md-'+arraySiz+' no-padding"><a href="javascript:void(0)" referenceId="' + aData.DT_RowId + '" class="footNote-reference" title="Add Foot Note">';
            actionButtons += '<span class="glyphicon glyphicon-edit"></span></a></span>';
        }else if (value.trim() === 'bonusSt') {
            actionButtons += '<span class="col-md-'+arraySiz+' no-padding"><a href="javascript:void(0)" referenceId="' + aData.DT_RowId + '" class="bonusSt-reference" title="Bonus Statement">';
            actionButtons += '<span class="glyphicon glyphicon-usd"></span></a></span>';
        }
        count++
    });
    return actionButtons;
}


function createCombo(dataObject,selectId,optionId,optionName,noSelectionStr) {
    var noSelection = noSelectionStr?noSelectionStr:"Select One....";
    var comboId='#'+selectId;
    var select='<option value="">'+noSelection+'</option>';
    $.each( dataObject, function( key, value ) {
        select+= '<option value="'+value[optionId]+'">'+value[optionName]+'</option>';
    });
    $(comboId).html(select);
}

function showSuccessMsg(msg){
    showMsg("success","glyphicon glyphicon-ok","<strong> Success</strong><br/>", msg);
}
function showErrorMsg(msg){
    showMsg("danger","glyphicon glyphicon-remove", "<strong> Error</strong><br/>", msg);
}
function showInfoMsg(msg){
    showMsg("info","glyphicon glyphicon-info-sign","<strong> Info</strong><br/>", msg);
}

// function showMsgType(type,msg){
//     if(type.trim()==='success'){
//         showMsg(type,"glyphicon glyphicon-ok","<strong> Success</strong><br/>", msg);
//     }
//     if(type.trim()==='danger'){
//         showMsg(type,"glyphicon glyphicon-remove", "<strong> Error</strong><br/>", msg);
//     }
//     if(type.trim()==='info'){
//         showMsg(type,"glyphicon glyphicon-info-sign", "<strong> Info</strong><br/>", msg);
//     }
//
//     if(type.trim()==='warning'){
//         showMsg(type,"glyphicon glyphicon-warning-sign", "<strong> Warning</strong><br/>", msg);
//     }
//
// }

function showMsg(type, icon, title, msg){
    $.growl(false, {
        allow_dismiss: true,
        animate: {
            enter: 'animated bounceIn',
            exit: 'animated bounceOut'
        },
        delay:1000,
        offset: 5,
        command: 'closeAll'
    });
    $.growl({
            icon: icon,
            title:title ,
            message: msg
        },
        {
            type:type,
            placement:{
                from:'bottom',
                align:'right'
            }
        });
}
function showLoading(div){
    $(div).isLoading({
        'text': "Processing...",
        'class': "icon-refresh",
        'position': "overlay"
    });
}
function hideLoading(div){
    $(div).isLoading( "hide" );
}

function createModal(){
    $('#myModal .create-success').hide();
    $('#myModal .create-content').show();
    $('#modalForm').resetForm();
    $(".form-group").removeClass( "has-error" );
    $("#hiddenId").val('');
    $('#myModal').modal('show');
}

function cancelModal(){
    $('#modalForm').resetForm();
    $(".form-group").removeClass( "has-error" );
    $("#hiddenId").val('');
    $('#myModal').modal('hide');
}

function gLabAddDateToPicker(issueDate,duration) {
    var dateArray = issueDate.split("/");
    var day= dateArray[0];
    var month= dateArray[1]-1;
    var year= dateArray[2];
    var newDate = new Date();
    newDate.setFullYear(year, month, day);
    newDate.setDate(newDate.getDate()+duration);
    var dd = newDate.getDate();
    var mm = newDate.getMonth() + 1;
    var y = newDate.getFullYear();
    var dateString=("0" + dd).slice(-2)+'/'+("0" + mm).slice(-2)+'/'+y;
    return dateString
}

function gLabGetDuration(startDate,endDate) {

    var dateArray = startDate.split("/");
    var day= dateArray[0];
    var month= dateArray[1]-1;
    var year= dateArray[2];

    var start = new Date(year,month,day);
    var end   = new Date(endDate);
    var diff  = new Date(end - start);
    var days  = diff/1000/60/60/24;

    return days;
}

function gLabSetDateToPicker() {
    var newDate = new Date();
    var dd = newDate.getDate();
    var mm = newDate.getMonth() + 1;
    var y = newDate.getFullYear();
    var dateString=("0" + dd).slice(-2)+'/'+("0" + mm).slice(-2)+'/'+y;
    return dateString
}

function gLabSetDateToPickerFormat() {
    var newDate = new Date();
    var dd = newDate.getDate();
    var mm = newDate.getMonth() + 1;
    var y = newDate.getFullYear();
    var dateString=(y+'-'+("0" + mm).slice(-2)+'-'+"0" + dd).slice(-2);
    return dateString
}

function gLabAddDayToNewDate(duration) {
    var newDate = new Date();
    newDate.setDate(newDate.getDate()+duration);
    var dd = newDate.getDate();
    var mm = newDate.getMonth() + 1;
    var y = newDate.getFullYear();
    var dateString=("0" + dd).slice(-2)+'/'+("0" + mm).slice(-2)+'/'+y;
    return dateString
}

function parseDateToPeaker(duration) {
    var parts = duration.split('/');
    // new Date(year, month [, day [, hours[, minutes[, seconds[, ms]]]]])
    return new Date(parts[2], parts[1]-1, parts[0]); // Note: months are 0-based
}

function differenceBtnTwoDate(date1,date2) {
    if(date1>date2){
        var timeDiff = Math.abs(date2.getTime() - date1.getTime());
        var diffDays = Math.floor(timeDiff / (1000 * 3600 * 24));
        return 0
    }else{
        var timeDiff = Math.abs(date2.getTime() - date1.getTime());
        var diffDays = Math.floor(timeDiff / (1000 * 3600 * 24));
        return diffDays
    }
}

function preventUnicodeSms(msgStr) {
    for (var i = 0; i < msgStr.length; i++)
        if (msgStr.charCodeAt(i) > 127)
            return true;
    return false;
}
function preventUnicodeSmsOld(msgStr) {
    var rxp = /^[\x00-\x7F]+$/;
    if(!rxp.test(msgStr)) {
        return true;
    }
    return false
}
function loadExamName(examNameUrl, academicYear, classCtrl, loadingCtrl){
    showLoading(loadingCtrl);
    jQuery.ajax({
        type: 'POST',
        dataType: 'JSON',
        url: examNameUrl,
        success: function (data, textStatus) {
            hideLoading(loadingCtrl);
            if (data.isError == false) {
                var $select = $(classCtrl);
                $select.find("option:gt(0)").remove();
                $.each(data.examNameList,function(key, value)
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

function loadClassSection(clsSectionUrl, sectionCtrl, loadingCtrl){
    showLoading(loadingCtrl);
    jQuery.ajax({
        type: 'POST',
        dataType: 'JSON',
        url: clsSectionUrl,
        success: function (data, textStatus) {
            hideLoading(loadingCtrl);
            var $select = $(sectionCtrl);
            $select.find('option:gt(0)').remove();
            if (data.isError == false) {
                $.each(data.sectionList,function(key, value)
                {
                    $select.append('<option value=' + value.id + '>' + value.name + '</option>');
                });
            } else {
                showErrorMsg(data.message);
            }
            $select.trigger("change");
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}
function loadStudentSubject(studentUrl, subjectCtrl, loadingCtrl){
    showLoading(loadingCtrl);
    jQuery.ajax({
        type: 'POST',
        dataType: 'JSON',
        url: studentUrl,
        success: function (data, textStatus) {
            hideLoading(loadingCtrl);
            var $select = $(subjectCtrl);
            $select.find('option:gt(0)').remove();
            if (data.isError == false) {
                $.each(data.subjectList,function(key, value)
                {
                    $select.append('<option value=' + value.id + '>' + value.name + '</option>');
                });
            } else {
                showErrorMsg(data.message);
            }
            $select.trigger("change");
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

function loadSectionSubject(classSubjectUrl,className ,section, subjectCtrl, loadingCtrl){
    showLoading(loadingCtrl);
    jQuery.ajax({
        type: 'POST',
        dataType: 'JSON',
        url:classSubjectUrl,
        success: function (data, textStatus) {
            hideLoading(loadingCtrl);
            if (data.isError == false) {
                var $select = $(subjectCtrl);
                $select.find('option:gt(0)').remove();
                $.each(data.subjectList, function (key, value) {
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
function loadSubjectWeek(classSubjectUrl, subjectCtrl, loadingCtrl){
    showLoading(loadingCtrl);
    jQuery.ajax({
        type: 'POST',
        dataType: 'JSON',
        url:classSubjectUrl,
        success: function (data, textStatus) {
            hideLoading(loadingCtrl);
            if (data.isError == false) {
                var $select = $(subjectCtrl);
                $select.find('option:gt(0)').remove();
                $.each(data.lessonWeekList, function (key, value) {
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

//Load Exam Related class and section
function loadExamNames(yearNameUrl, examNameCtrl, loadingCtrl){
    showLoading(loadingCtrl);
    jQuery.ajax({
        type: 'POST',
        dataType: 'JSON',
        url: yearNameUrl,
        success: function (data, textStatus) {
            hideLoading(loadingCtrl);
            var $select = $(examNameCtrl);
            $select.find('option:gt(0)').remove();
            if (data.isError == false) {
                $.each(data.examNameList,function(key, value)
                {
                    $select.append('<option value=' + value.id + '>' + value.name + '</option>');
                });
            } else {
                showErrorMsg(data.message);
            }
            $select.trigger("change");
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

function loadExamClass(examClassUrl, classCtrl, loadingCtrl){
    showLoading(loadingCtrl);
    jQuery.ajax({
        type: 'POST',
        dataType: 'JSON',
        url: examClassUrl,
        success: function (data, textStatus) {
            hideLoading(loadingCtrl);
            var $select = $(classCtrl);
            $select.find('option:gt(0)').remove();
            if (data.isError == false) {
                $.each(data.classNameList,function(key, value)
                {
                    $select.append('<option value=' + value.id + '>' + value.name + '</option>');
                });
            } else {
                showErrorMsg(data.message);
            }
            $select.trigger("change");
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}


function loadExamAndClassList(academicYearUrl, examNameCtrl, classCtrl, loadingCtrl){
    showLoading(loadingCtrl);
    jQuery.ajax({
        type: 'POST',
        dataType: 'JSON',
        url: academicYearUrl,
        success: function (data, textStatus) {
            hideLoading(loadingCtrl);
            if (data.isError == false) {
                var $select = $(classCtrl);
                $select.find('option:gt(0)').remove();
                $.each(data.classNameList,function(key, value)
                {
                    $select.append('<option value=' + value.id + '>' + value.name + '</option>');
                });
                var $examSelect = $(examNameCtrl);
                $examSelect.find('option:gt(0)').remove();
                $.each(data.examNameList,function(key, value)
                {
                    $examSelect.append('<option value=' + value.id + '>' + value.name + '</option>');
                });


            }
            else {
                showErrorMsg(data.message);
            }
            $select.trigger("change");
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

function loadClassNames(classNameUrl, classCtrl, loadingCtrl){
    showLoading(loadingCtrl);
    jQuery.ajax({
        type: 'POST',
        dataType: 'JSON',
        url: classNameUrl,
        success: function (data, textStatus) {
            hideLoading(loadingCtrl);
            var $select = $(classCtrl);
            $select.find('option:gt(0)').remove();
            if (data.isError == false) {
                $.each(data.classNameList,function(key, value)
                {
                    $select.append('<option value=' + value.id + '>' + value.name + '</option>');
                });
            } else {
                showErrorMsg(data.message);
            }
            $select.trigger("change");
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

function loadExamAsClassSectionList(examAsSectionListUrl, sectionCtrl, loadingCtrl){
    showLoading(loadingCtrl);
    jQuery.ajax({
        type: 'POST',
        dataType: 'JSON',
        url: examAsSectionListUrl,
        success: function (data, textStatus) {
            hideLoading(loadingCtrl);
            var $select = $(sectionCtrl);
            $select.find('option:gt(0)').remove();
            if (data.isError == false) {

                $.each(data.sectionNameList,function(key, value)
                {
                    $select.append('<option value=' + value.id + '>' + value.name + '</option>');
                });
            } else {
                showErrorMsg(data.message);
            }
            $select.trigger("change");
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
        }
    });
}

function repoFormatResult(repo) {
    var markup = '<div class="row-fluid">' +
        '<div class="span12">' + repo.name + '</div>' +
        '</div>';
    return markup;
}

function repoFormatSelection(repo) {
    return repo.name;
}



var glModal = function(url,callbackFnk) {
    var $modal=$('#load_popup_modal_show_id').load(url,function(){
        $modal.modal('show');
        if(typeof callbackFnk == 'function'){
            callbackFnk.call(this, $modal);
        }
    });
}
function clearForm(form) {
    $(':input', form).each(function () {
        var type = this.type;
        var tag = this.tagName.toLowerCase(); // normalize case
        // password inputs, and textareas
        if (type == 'text' || type == 'password' || type == 'hidden' || tag == 'textarea' || type == 'email' || type == 'tel' || type == 'number') {
            this.value = "";
        }

        // checkboxes and radios need to have their checked state cleared
        else if (type == 'checkbox' || type == 'radio')
            this.checked = false;

        // select elements need to have their 'selectedIndex' property set to -1
        else if (tag == 'select') {
            this.value = "";
        }

    });
}