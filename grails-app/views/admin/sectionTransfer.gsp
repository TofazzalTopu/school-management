<!DOCTYPE html>
<html>
<head>
    <meta name="layout" content="moduleStdMgmtLayout"/>
    <title>Lesson</title>
</head>
<body>
<grailslab:breadCrumbActions firstBreadCrumbUrl="${g.createLink(controller: 'section',action: 'index')}" firstBreadCrumbText="Section List" breadCrumbTitleText="Move next year"/>

<section class="panel">
    <header class="panel-heading">
        Transfer Section from <span style ="color:red;">${academicYear.value}</span> To  <span style ="color:red;">next year</span>
    </header>
        <div class="panel-body">
            <form id="section" controller="section" action="saveSection" method="post">
                <select multiple="multiple" size="15" name="sectionTransferholder">
                    <g:each var="section" in="${sectionList}">
                        <option value="${section.id}">${section.name}</option>
                    </g:each>
                    <g:each var="nextSection" in="${nextYearSectionList}">
                        <option value="${nextSection.id}" selected="selected" disabled>${nextSection.name}</option>
                    </g:each>
                    </select>
                <br>
                <div class="row">
                    <div class="form-group">
                        <div class="col-md-offset-10 col-lg-2">
                            <input type="submit" class="btn btn-primary btn-submit"  tabindex="4"/>
                        </div>
                    </div>
                </div>
            </form>
    </div>


</section>
    <script>
        var demo1 = $('select[name="sectionTransferholder"]').bootstrapDualListbox({
            showFilterInputs: false,
            eventRemoveAllOverride: true
        });
        $("#section").submit(function(e) {
            e.preventDefault();
            var selected = ($('[name="sectionTransferholder"]').val());
            $.ajax({
                url: "${createLink(controller: 'section', action: 'saveTransfer')}?academicYear=${academicYear.key}&sectionIds="+selected,
                type: 'post',
                dataType: "json",
                success: function (data) {
                    hideLoading("#create-form");
                    if (data.isError == false) {
                        showSuccessMsg(data.message);
                        window.location.href = "${createLink(controller: 'section', action: 'index')}";
                    } else {
                        showErrorMsg(data.message);
                    }
                },
                failure: function (data) {
                }
            })
        });
    </script>

    </body>
</html>
