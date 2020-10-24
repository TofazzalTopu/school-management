<html>
<head>
    <title>Notice Board</title>
    <meta name="layout" content="adminLayout"/>
    <script src="//cdn.tinymce.com/4/tinymce.min.js"></script>
</head>

<body>

<grailslab:breadCrumbActions breadCrumbTitleText="Notice Board" SHOW_CREATE_BTN="YES" createButtonText="Add New Notice"/>
<g:if test="${flash.message}">
    <h4 class="text-center" style="color: sienna">${flash.message}</h4>
</g:if>

<grailslab:fullModal label="Notice Board" modalClass="modal-mid" enctype="multipart/form-data" controller="noticeBoard" action="save" method="post">
    <div class="form-group">
        <label class="col-md-3 control-label">Title <span class="required">*</span></label>

        <div class="col-md-8">
            <input type="text" name="title" id="title" class="form-control"/>
        </div>
    </div>

    <div class="form-group">
        <label class="col-md-3 control-label">Scroll Text <span class="required">*</span></label>

        <div class="col-md-6">
            <input type="text" name="scrollText" id="scrollText" class="form-control"/>
        </div>
        <div class="col-md-2">
            <div class="input-group scrollColorPicker">
                <input type="text" value="#ccc" class="form-control" name="scrollColor" id="scrollColor"/>
                <span class="input-group-addon"><i></i></span>
            </div>
        </div>
    </div>

    <div class="form-group">
        <label class="col-md-3 control-label">Notice Details</label>

        <div class="col-md-8">
            <textarea name="details" id="details" class="form-control add-html-content-area"></textarea>
        </div>
    </div>

    <div class="form-group">
        <label class="col-md-3 control-label">Publish Date <span class="required">*</span></label>

        <div class="col-md-3">
            <input type="text" name="publish" id="publish" class="form-control"/>
        </div>

        <label class="col-md-3 control-label">Show In Scroll</label>

        <div class="col-md-1">
            <g:checkBox name="keepScroll" id="keepScroll" class="form-control"/>
        </div>
    </div>

    <div class="form-group">
        <label class="col-md-3 control-label">Expire On <span class="required">*</span></label>

        <div class="col-md-3">
            <input type="text" name="expire" id="expire" class="form-control"/>
        </div>

        <label class="col-md-3 control-label">Display In Board</label>

        <div class="col-md-1">
            <g:checkBox name="keepBoard" id="keepBoard" class="form-control"/>
        </div>
    </div>
    <div class="form-group">
        <label class="col-md-3 control-label">Pdf (Optional)</label>

        <div class="col-md-4">
            <input type="file" name="pdfFile" id="pdfFile"/>
            <p>Maximum Size : 15mb, Only Pdf file</p>
        </div>
        <label class="col-md-2 control-label">Category</label>

        <div class="col-md-3">
            <g:select class="form-control" id="category" name='category'
                      noSelection="${['': 'Select category Type...']}"
                      from='${com.grailslab.enums.NoticeCategory.categoryList()}'
                      optionKey="key" optionValue="value"></g:select>
        </div>
    </div>
</grailslab:fullModal>

<grailslab:dataTable dataSet="${dataReturn}" tableHead="Title,Scroll Text,Board,Scroll,Publish,Expire"
                     actions="fa fa-pencil-square-o,fa fa-scissors,fa fa-trash-o"></grailslab:dataTable>

<script>

    tinymce.init({
        setup: function (editor) {
            editor.on('change', function () {
                tinymce.triggerSave();
            });
        },
        theme: "modern",
        mode: "specific_textareas",
        selector: 'textarea.add-html-content-area',
        menubar: false,
        toolbar_items_size: 'small',
        plugins: [
            "lists hr anchor pagebreak spellchecker wordcount code fullscreen insertdatetime nonbreaking table textcolor"
        ],
        toolbar1: "bold italic underline strikethrough | alignleft aligncenter alignright alignjustify | fontsizeselect | bullist numlist | forecolor | table"

    });


    jQuery(function ($) {
        $('.scrollColorPicker').colorpicker({
            color:'#ccc'
        });
        $('#publish').datepicker({
            format: 'dd/mm/yyyy',
            default: new Date(),
            setData: new Date(),
            autoclose: true
        });

        $('#expire').datepicker({
            format: 'dd/mm/yyyy',
            default: new Date(),
            setData: new Date(),
            autoclose: true
        });


        var validator = $('#modalForm').validate({
            errorElement: 'span',
            errorClass: 'help-block',
            focusInvalid: false,
            rules: {
                title: {
                    required: true
                },
                details: {
                    required: true
                },
                scrollText: {
                    required: true
                },
                publish: {
                    required: true
                },
                expire: {
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
        });

        $('#list-table').dataTable({
            "bAutoWidth": true,
            "bServerSide": true,
            "iDisplayLength": 25,
            "deferLoading": ${totalCount?:0},
            "sAjaxSource": "${g.createLink(controller: 'noticeBoard',action: 'list')}",
            "fnRowCallback": function (nRow, aData, iDisplayIndex) {
                if (aData.DT_RowId == undefined) {
                    return true;
                }
                $('td:eq(6)', nRow).html(getActionButtons(nRow, aData));
                return nRow;
            },
            "aoColumns": [
                null,
                null,
                {"bSortable": false},
                {"bSortable": false},
                {"bSortable": false},
                {"bSortable": false},
                {"bSortable": false}
            ]
        });

        $('.create-new-btn').click(function (e) {
            createModal();
            $('#publish').datepicker('setDate', new Date());
            $(".scrollColorPicker").colorpicker('setValue', "#ccc");
        });

        $(".cancel-btn").click(function () {
            cancelModal();
        });

        $('#list-table').on('click', 'a.reference-1', function (e) {
            var selectRow = $(this).parents('tr');
            var control = this;
            var referenceId = $(control).attr('referenceId');

            jQuery.ajax({
                type: 'POST',
                dataType: 'JSON',
                url: "${g.createLink(controller: 'noticeBoard',action: 'edit')}?id=" + referenceId,
                success: function (data, textStatus) {
                    if (data.isError == false) {
                        var publish = data.obj.publish;
                        var expire = data.obj.expire;
                        $('#hiddenId').val(data.obj.id);
                        $('#publish').datepicker('setDate', new Date(publish));
                        $('#expire').datepicker('setDate', new Date(expire));
                        $('#title').val(data.obj.title);
                        $('#category').val(data.obj.category);
                        if(data.obj.details){
                            tinymce.editors[0].setContent(data.obj.details);
                        }
                        $('#scrollText').val(data.obj.scrollText);
                        if(data.obj.scrollColor){
                            $(".scrollColorPicker").colorpicker('setValue', data.obj.scrollColor);
                        }else{
                            $(".scrollColorPicker").colorpicker('setValue', "#ccc");
                        }
                        $("#keepBoard").prop("checked", data.obj.keepBoard);
                        $("#keepScroll").prop("checked", data.obj.keepScroll);
                        $('#myModal .create-success').hide();
                        $('#myModal .create-content').show();
                        $('#myModal').modal('show');
                    } else {
                        showErrorMsg(data.message);
                    }
                },
                error: function (XMLHttpRequest, textStatus, errorThrown) {
                }
            });
            e.preventDefault();
        });

        $('#list-table').on('click', 'a.reference-2', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'noticeBoard',action: 'inactive')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            $('#list-table').DataTable().ajax.reload();
                            showSuccessMsg(data.message);
                        } else {
                            showErrorMsg(data.message);
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            }
            e.preventDefault();
        });

        $('#list-table').on('click', 'a.reference-3', function (e) {
            var selectRow = $(this).parents('tr');
            var confirmDel = confirm("Are you sure?");
            if (confirmDel == true) {
                var control = this;
                var referenceId = $(control).attr('referenceId');
                jQuery.ajax({
                    type: 'POST',
                    dataType: 'JSON',
                    url: "${g.createLink(controller: 'noticeBoard',action: 'delete')}?id=" + referenceId,
                    success: function (data, textStatus) {
                        if (data.isError == false) {
                            $("#list-table").DataTable().row(selectRow).remove().draw(false);
                            showSuccessMsg(data.message);
                        } else {
                            showErrorMsg(data.message);
                        }
                    },
                    error: function (XMLHttpRequest, textStatus, errorThrown) {
                    }
                });
            }
            e.preventDefault();
        });
    });

    function getActionButtons(nRow, aData) {
        return getActionBtnCustom(nRow, aData, 'fa fa-pencil-square-o,fa fa-scissors,fa fa-trash-o');
    }

</script>
</body>
</html>