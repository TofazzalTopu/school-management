<%@ page contentType="text/html;charset=UTF-8" %>
<html>
<head>
    <meta name="layout" content="open-ltpl"/>
    <title>Online Application Form Olympiad</title>
</head>
<body>

<div class="wrapbox">

    <section class="pageheader-default text-center">
        <div class="semitransparentbg">
            <h4 class="animated fadeInLeftBig notransition">NARAYANGANG IDEAL SCHOOL  ANNUAL SCIENCE & CULTUTRAL FESTIVAL ' 2016</h4>
        </div>

    </section>

    <div class="wrapsemibox">
        <div class="container animated fadeInUpNow notransition fadeInUp topspace0">
            <div class="form-group" >
            <div class="text-center">
                <label class="control-label" for="date">Date <span class="required-indicator">
                </span> </label>

                <input type="text" name="daterange" value="27/09/2016 - 29/09/2016" />

            </div>

        </div>
            <div class="form-group">
               <div class="text-center" >
                   <h3> Olympaid Registration form </h3>
               </div>

            </div>


            <div class="col-md-offset-1 col-md-10">
                <form role="form" class="form-horizontal" method="post" id="create-form">
                    <g:hiddenField name="pageNo" value="1"/>
                    <div class="row">
                        <div class="col-md-12">
                            <div class="form-group">
                                <label class="col-sm-3 control-label" for="name ">Name <span class="required-indicator">* </span> </label>
                                <div class="col-sm-9">
                                    <input type="text" placeholder="Name" id="name "name="name" class="form-control">
                                </div>
                            </div>

                            <div class="form-group">
                                <label class="col-sm-3 control-label" for="instituteName">Name Of Institute <span class="required-indicator">* </span> </label>
                                <div class="col-sm-9">
                                    <input type="text" placeholder="Institute Name" id="instituteName" name="instituteName" class="form-control">
                                </div>
                            </div>
                        </div>

                </div>

                <div class="row" >

                    <div class="col-md-12">
                    <label class="col-md-3 control-label" for="className"> <span class="required-indicator">
                    </span> </label>
                    <div class="col-md-3">
                        <input class="form-control"  type="text" name="class" id="className" placeholder=" Class Name "
                               tabindex="5"/><br/>

                    </div>

                    <div class="col-md-3">
                        <input class="form-control"  type="text" name="roll" id="roll" placeholder=" Roll "
                               tabindex="5"/><br/>

                    </div>

                    <div class="col-md-3">
                        <input class="form-control"  type="text" name="contactNo" id="contactNo" placeholder="Contact No"
                               tabindex="6"/><br/>

                    </div>
                    </div>


                </div>

                    <div class="row" >

                        <div class="col-md-12">
                            <div class="form-group">
                                <label class="col-md-3 control-label"> <span class="required-indicator"> </span> </label>
                                <div class="col-md-9">
                                    <h5> Select Options you Prefer :  </h5>
                                    <input type="checkbox" id="languageCompetion" value="languageCompetion"><label for="languageCompetion">Language Competion</label><br>
                                    <input type="checkbox" id="math" value="math"><label for="math"> Math</label><br>
                                    <input type="checkbox" id="science" value="science"><label for="science"> Science </label><br>
                                    <input type="checkbox" id="gKnowledge" value="gKnowledge"><label for="gKnowledge">General Knowledge</label><br>
                                </div>
                            </div>

                        </div>
                    </div>

                <div class="row" >
                    <div class="col-md-12">
                        <label class="col-md-3 control-label" for="name ">Two Students in Each Group : <span class="required-indicator">
                        </span> </label>
                        <div class="col-md-9">

                            <table class="table table-bordered">
                                <thead>
                                <tr>
                                    <th>SL.</th>
                                    <th>Name</th>
                                    <th>Class & Roll</th>
                                    <th>Contact no</th>
                                </tr>
                                </thead>
                                <tbody>
                                <tr>
                                    <th scope="row">1</th>
                                    <td><input type="text" class="form-control" id="eName"></td>
                                    <td><input type="text" class="form-control" id="eClassRoll"></td>
                                    <td><input type="text" class="form-control" id="eContactTo"></td>
                                </tr>
                                <tr>
                                    <th scope="row">2</th>
                                    <td><input type="text" class="form-control" id="gName"></td>
                                    <td><input type="text" class="form-control" id="gClassRoll"></td>
                                    <td><input type="text" class="form-control" id="gContactTo"></td>
                                </tr>
                                </tbody>
                            </table>
                        </div>
                    </div>
                </div>

                <div class="row" >
                <div class="col-md-12">
                    <label class="col-md-3 control-label" for="name ">Scientific Slide Show Presentation: : <span class="required-indicator">
                    </span> </label>
                    <div class="col-md-9">

                        <table class="table table-bordered">
                            <thead>
                            <tr>
                                <th>SL.</th>
                                <th>Name</th>
                                <th>Class & Roll</th>
                                <th>Contact no</th>
                            </tr>
                            </thead>
                            <tbody>
                            <tr>
                                <th scope="row">1</th>
                                <td><input type="text" class="form-control" id="name"></td>
                                <td><input type="text" class="form-control" id="classRoll"></td>
                                <td><input type="text" class="form-control" id="contactTo"></td>
                            </tr>
                            <tr>
                                <th scope="row">2</th>
                                <td><input type="text" class="form-control" id="pName"></td>
                                <td><input type="text" class="form-control" id="pClassRoll"></td>
                                <td><input type="text" class="form-control" id="pContactTo"></td>
                            </tr>

                            </tbody>
                        </table>
                    </div>
                </div>
            </div>
                    <div class="row pull-right">
                        <div class="form-group">
                            <button type="button" class="btn btn-default">Try Later </button>
                            <button type="submit" class="btn btn-default">Save</button>

                        </div>
                    </div>
                </form>
            </div>
        </div>
    </div>
</div>

<script>
    jQuery(function ($) {

    var validator = $('#create-form').validate({
        errorElement: 'span',
        focusInvalid: false,
        rules: {
            name: {
                required: true
            },
            fathersName: {
                required: true
            },
            email: {
                email: true
            },
            birthDate: {
                required: true
            },
            religion: {
                required: true
            },
            gender: {
                required: true
            },
            mobile: {
                required: true
            },
            presentAddress: {
                required: true
            }
        },
        highlight: function (e) {
            $(e).closest('.form-group').removeClass('has-info').addClass('has-error');
        },
        success: function (e) {
            $(e).closest('.form-group').removeClass('has-error').addClass('has-info');
            $(e).remove();
        },
        submitHandler: function (form) {
            var formData = new FormData(form);
            $.ajax({
                url: "${createLink(controller: 'olympiad', action: 'save')}",
                type: 'post',
                dataType: "json",
                data: formData,
                mimeType: 'multipart/form-data',
                contentType: false,
                cache: false,
                processData: false,
                success: function (data) {
                 //   hideLoading("#create-form-holder");
                    if (data.isError == false) {
                        clearForm(form);
                        var table = $('#list-table').DataTable().ajax.reload();
                        showSuccessMsg(data.message);
                    } else {
                        showErrorMsg(data.message);
                    }
                },
                failure: function (data) {
                }
            })
        }
    });
        $('#list-table').dataTable({
            "sDom": "<'row'<'col-md-4 section-filter-holder'><'col-md-4'><'col-md-4'f>r>t<'row'<'col-md-3'l><'col-md-3'i><'col-md-6'p>>",
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "aaSorting": [0, 'desc'],
            "deferLoading": ${totalCount?:0},
            "fnServerParams": function (aoData) {
                aoData.push({"name": "rStatus", "value": $('#status').val()});
            },
            "sAjaxSource": "${g.createLink(controller: 'olympiad',action: 'list')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(7)', nRow).html(getActionButtons(nRow, aData));
                return nRow;
            },
            "aoColumns": [
                null,
                null,
                null,
                null,
                null,
                {"bSortable": false},
                null,
                {"bSortable": false}
            ]
        });
    $(function() {
        $('input[name="daterange"]').daterangepicker();
    });

    });

</script>
</body>
</html>