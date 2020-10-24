


jQuery(document).ready(function(){
	
	
    jQuery("#smart_status").change(function(){
        var type = jQuery(this).val();
        
        if(type==1){
            jQuery(".smart_status_yes").show();
			$('#smart_nid, #smart_nid_file').prop('required',true);
        }
        else if(type==2){
            jQuery(".smart_status_yes").hide();
			$('#smart_nid, #smart_nid_file').removeAttr('required');
        }
    });
    
	
	
    jQuery(".result_type").change(function(){
        var type = jQuery(this).val();
        jQuery(".result_div, .result_gpa").hide();
        if(type==1){
            jQuery(".result_gpa").show();
        }
        else if(type==2){
            jQuery(".result_div").show();
        }
    });
    
        jQuery(".number").keyup(function(){
            var str = jQuery(this).val();
            jQuery(this).val(str.replace(/[^0-9]/g,''));
        });
        
        jQuery(".alphabet").keyup(function(){
            var str = jQuery(this).val();
            jQuery(this).val(str.replace(/[^a-z A-Z .]/g,''));
        });
        
        jQuery(".upper").keyup(function(){
            var str = jQuery(this).val();
            jQuery(this).val(str.toUpperCase());
        });
        
        jQuery(".btn-model-close").click(function(){
            jQuery('.model').slideUp();
        });
    
    
});


function print_list(type){
    var dates=$("#date").val();
    var date=dates.split("to");
    var url=base_url+'index.php/sonaliadmin/print_sheet/'+type+'/'+date[0]+'/'+date[1];
    var win = window.open(url, '_blank');
    win.focus();
}

function find_center_info(current){
    $("#center_data").html('<div class="loding"><img src="'+base_url+'assets/custom/img/loading.gif"></div>');
   $.post(base_url+'index.php/site/find_center_info',
   {
       exam_date:$("#date").val(),
       c_code:$("#c_code").val(),
       zilla:$("#zilla").val(),
       sub_code:$("#sub_code").val()
   },
   function(result){
    $("#center_data").html(result);
   });
}


function find_income(current) {
    $.post(base_url + 'index.php/sonaliadmin/findincome/' + current,
            {
                sl: $("#sl").val(),
                date: $("#date").val(),
                fina_code: $("#fina_code").val(),
                pay_title: $("#pay_title").val(),
                amount: $("#amount").val(),
                eiin: $("#eiin").val(),
                app_name: $("#app_name").val(),
                app_mobile: $("#app_mobile").val()
            },
    function (result) {
        $(".body_right").html(result);
    });
}



function find_institute(current) {
    $.post(base_url + 'index.php/admin/findinstitute/' + current,
            {
                eiin: $("#eiin").val(),
                name: $("#name").val(),
                zilla: $("#zilla").val(),
                thana: $("#thana").val(),
            },
    function (result) {
        $(".body_right").html(result);
    });
}


function find_etif(current) {
    $.post(base_url + 'index.php/admin/etiflist/' + current+"/TRUE",
            {
                eiin: $("#eiin").val(),
                nid: $("#nid").val(),
                name: $("#name").val(),
                mobile: $("#mobile").val(),
                jsc: $("#jsc").val(),
                ssc: $("#ssc").val(),
                hsc: $("#hsc").val(),
                approved: $("#approved").val()
            },
    function (result) {
        $(".body_right").html(result);
    });
}

function view_etif(id) {
    jQuery('.model').slideDown();
    $(".model-content").html('');
    $.post(base_url + 'index.php/admin/etifview/' + id,
            {
            },
    function (result) {
        $(".model-content").html(result);
    });
}


function find_examiner(current){
   $.post(base_url+'index.php/examiner/datalistfind/'+current,
   {
       exam:$("#exam").val(),
       year:$("#year").val(),
       ecode:$("#ecode").val(),
       subcode:$("#subcode").val(),
       ename:$("#ename").val(),
       desig:$("#desig").val(),
       mobile:$("#mobile").val(),
       dis_date:$("#dis_date").val(),
       dis_time:$("#dis_time").val(),
       etype:$("#etype").val(),
       eiin:$("#eiin").val()
   },
   function(result){
     $(".body_section_blank").html(result);
   });
}








$(document).ready(function () {
    // Setup - add a text input to each footer cell
    $('#example2 tfoot th').each(function () {
        var title = $(this).text();
        if (title != "") {
            titles = title.split("*");
            if (titles.length > 1) {
                var str = '<select class="select_find  form-control">';
                str += '<option value="">Select ' + titles[0] + '</option>';
                for (i = 1; i < titles.length; i++) {
                    str += '<option value="' + titles[i] + '">' + titles[i] + '</option>';
                }
                str += '</select>';
                $(this).html(str);
            } else {
                $(this).html('<input class="input_find form-control" type="text" placeholder="' + title + '" />');
            }

        }
    });

    // DataTable
    var table = $('#example2').DataTable({
        "paging": true,
        "lengthChange": true,
        "searching": true,
        "ordering": false,
        "info": true,
        "autoWidth": true
    });

    // Apply the search
    table.columns().every(function () {
        var that = this;

        $('input, select', this.footer()).on('keyup change', function () {
            if (that.search() !== this.value) {
                that
                        .search(this.value)
                        .draw();
            }
        });
    });
    
    
    $(".datepicker").datepicker({
        changeMonth: true,
        changeYear: true,
        dateFormat: 'yy-mm-dd',
        yearRange: "-100:+100",
        minDate: '-100Y',
        maxDate: '+100Y'
    });
    
    $("#dobTeacher").datepicker({
        changeMonth: true,
        changeYear: true,
        dateFormat: 'yy-mm-dd',
        yearRange: "-100:+100",
        maxDate: '-18Y'
    });
    
    
});



