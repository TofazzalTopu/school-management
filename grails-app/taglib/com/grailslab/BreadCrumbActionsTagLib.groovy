package com.grailslab

class BreadCrumbActionsTagLib {
//    static defaultEncodeAs = [taglib:'html']
//    static defaultEncodeAs = 'html'
    static namespace = "grailslab"
    def breadCrumbActions = { attrs ->
//        def controllerName = attrs.controllerName ?: 'home'
        def firstBreadCrumbText = attrs.firstBreadCrumbText ?: 'BreadCrumb 1'
        def firstBreadCrumbUrl = attrs.firstBreadCrumbUrl
        def secondBreadCrumbText = attrs.secondBreadCrumbText ?: 'BreadCrumb 2'
        def secondBreadCrumbUrl = attrs.secondBreadCrumbUrl
        def thirdBreadCrumbText = attrs.thirdBreadCrumbText ?: 'BreadCrumb 3'
        def thirdBreadCrumbUrl = attrs.thirdBreadCrumbUrl
        def breadCrumbTitleText = attrs.breadCrumbTitleText ?: "Page Title"

        boolean SHOW_CREATE_BTN = attrs.SHOW_CREATE_BTN == 'YES';
        def createButtonText = attrs.createButtonText ?: 'Add New'
        def createBtnIcon = attrs.createBtnIcon ?: "fa fa-plus"
//        def createBtnAction = attrs.createBtnAction ?: 'index'

        boolean SHOW_CREATE_LINK = attrs.SHOW_CREATE_LINK == 'YES';
        def createLinkText = attrs.createLinkText ?: 'Add New'
        def createLinkIcon = attrs.createLinkIcon ?: "fa fa-plus"
        def createLinkUrl = attrs.createLinkUrl

        boolean SHOW_PRINT_BTN = attrs.SHOW_PRINT_BTN == 'YES';
        def printBtnText = attrs.printBtnText ?: 'Print'
        def printBtnIcon = attrs.printBtnIcon ?: "fa fa-print"
//        def printBtnAction = attrs.printBtnAction ?: 'index'

        boolean SHOW_LINK_BTN = attrs.SHOW_LINK_BTN == 'YES';
        def linkBtnText = attrs.linkBtnText ?: 'Go To'
        def linkBtnIcon = attrs.linkBtnIcon ?: "fa fa-external-link"
//        def linkBtnAction = attrs.linkBtnAction ?: 'index'

        boolean SHOW_LIST_BTN = attrs.SHOW_LIST_BTN == 'YES';
        def listBtnText = attrs.listBtnText ?: 'List'
        def listBtnIcon = attrs.listBtnIcon ?: "fa fa-external-link"
        def listLinkUrl= attrs.listLinkUrl

        boolean SHOW_EXTRA_BTN1 = attrs.SHOW_EXTRA_BTN1 == 'YES';
        def extraBtn1Text = attrs.extraBtn1Text ?: 'Btn 1'
        def extraBtn1Icon = attrs.extraBtn1Icon ?: "fa fa-plus"

        out << render(template: "/common/breadCrumbActions", model: [controllerName      : controllerName,
                                                                     firstBreadCrumbText : firstBreadCrumbText, firstBreadCrumbUrl: firstBreadCrumbUrl,
                                                                     secondBreadCrumbText: secondBreadCrumbText, secondBreadCrumbUrl: secondBreadCrumbUrl,
                                                                     thirdBreadCrumbText : thirdBreadCrumbText, thirdBreadCrumbUrl: thirdBreadCrumbUrl,
                                                                     breadCrumbTitleText : breadCrumbTitleText,
                                                                     SHOW_CREATE_BTN     : SHOW_CREATE_BTN, createButtonText: createButtonText, createBtnIcon: createBtnIcon,
                                                                     SHOW_CREATE_LINK    : SHOW_CREATE_LINK, createLinkText: createLinkText, createLinkIcon: createLinkIcon, createLinkUrl: createLinkUrl,
                                                                     SHOW_PRINT_BTN      : SHOW_PRINT_BTN, printBtnText: printBtnText, printBtnIcon: printBtnIcon,
                                                                     SHOW_LINK_BTN       : SHOW_LINK_BTN, linkBtnText: linkBtnText, linkBtnIcon: linkBtnIcon,
                                                                     SHOW_LIST_BTN       : SHOW_LIST_BTN, listBtnText: listBtnText, listBtnIcon: listBtnIcon, listLinkUrl: listLinkUrl,
                                                                     SHOW_EXTRA_BTN1     : SHOW_EXTRA_BTN1, extraBtn1Text: extraBtn1Text, extraBtn1Icon: extraBtn1Icon,
        ])
    }

    Closure form = { attrs, body ->
        def classname = attrs.class
        def id = attrs.id
        def name = attrs.name
        def writer = getOut()
        writer << "<form class=\"form-horizontal ${attrs.class ?' '+attrs.class :''}\" role=\"form\"  ${attrs.id ? "id=\"${attrs.id}\"" : "id=\"modalForm\""}>\n"
        writer << "<input type=\"hidden\" name=\"id\" id=\"hiddenId\">\n"
        writer << body()
        writer << "</form>"
    }


    Closure input = { attrs, body ->
        def name = attrs.name
        def className = attrs.className
        def label = attrs.label
        def labelSpace = attrs.labelSpace
        def fieldSpace = attrs.fieldSpace
        def required = attrs.required
        def type = attrs.type
        def id = attrs.id
        def value = "value=\"${attrs.value}\""
        def placeholder = "placeholder=\"${attrs.placeholder}\""
        out << "<div class=\"form-group\">\n"
        out << "<label class=\"${attrs.labelSpace ? attrs.labelSpace : 'col-md-4'} control-label\"> ${attrs.label ? attrs.label : 'Insert ' + attrs.name} ${attrs.required ? "<span class=\"required\">*</span>" : ''}</label>\n"
        out << "<div class=\"${attrs.fieldSpace ? attrs.fieldSpace : 'col-md-8'}\">\n"
        out << "<input type=\"${attrs.type ? attrs.type : 'text'}\" name=\"${attrs.name}\" id=\"${attrs.id ? attrs.id : attrs.name}\" class=\"form-control${attrs.className ?' '+attrs.className:''}\" ${attrs.value ? value : ''} ${attrs.placeholder ? placeholder : ''}"
        out << "/>\n"
        out << "</div>\n"
        out << "</div>\n"
    }

    Closure textArea = { attrs, body ->
        def name = attrs.name
        def className = attrs.className
        def label = attrs.label
        def labelSpace = attrs.labelSpace
        def fieldSpace = attrs.fieldSpace
        def required = attrs.required
        def id = attrs.id
        def placeholder = "placeholder=\"${attrs.placeholder}\""
        out << "<div class=\"form-group\">\n"
        out << "<label class=\"${attrs.labelSpace ? attrs.labelSpace : 'col-md-4'} control-label\"> ${attrs.label ? attrs.label : 'Insert ' + attrs.name} ${attrs.required ? "<span class=\"required\">*</span>" : ''}</label>\n"
        out << "<div class=\"${attrs.fieldSpace ? attrs.fieldSpace : 'col-md-8'}\">\n"
        out << "<textarea  name=\"${attrs.name}\" id=\"${attrs.id ? attrs.id : attrs.name}\" class=\"form-control${attrs.className ?' '+attrs.className:''}\" ${attrs.placeholder ? placeholder : ''}>"
        out << "${attrs.value ? attrs.value : ''}"
        out << "</textarea>\n"
        out << "</div>\n"
        out << "</div>\n"
    }

    Closure select = { attrs, body ->
        def name = attrs.name
        def enums = attrs.enums
        def label = attrs.label
        def labelSpace = attrs.labelSpace
        def fieldSpace = attrs.fieldSpace
        def required = attrs.required
        def from = attrs.from
        def id = attrs.id
        def noSelect = " <option value=\"\">${attrs.label ?'Select '+ attrs.label+' .....' : 'Select ' + name + '....'}</option>\n"
        def writer = getOut()
        out << "<div class=\"form-group\">\n"
        out << "<label class=\"${attrs.labelSpace ? attrs.labelSpace : 'col-md-4'} control-label\"> ${attrs.label ? attrs.label : 'Insert ' + attrs.name} ${attrs.required ? "<span class=\"required\">*</span>" : ''}</label>\n"
        out << "<div class=\"${attrs.fieldSpace ? attrs.fieldSpace : 'col-md-8'}\">\n"
        writer << "<select name=\"${attrs.name}\" id=\"${attrs.id ? attrs.id : attrs.name}\" class=\"form-control${attrs.className ?' '+attrs.className:''}\">\n"
        writer << "${attrs.noSelect ? '' : noSelect}"
        from.each { values ->
            if (attrs.enums){
                writer << " <option value=\"${values.key}\">${values.value}</option>\n"
            }else {
                writer << " <option value=\"${values.id}\">${values.name}</option>\n"
            }
        }
        writer << "</select>\n"
        out << "</div>\n"
        out << "</div>\n"
    }

//modal-big modal-large modal-small-mid modal-mid modal

    Closure fullModal = { attrs, body ->
        def modalId = attrs.modalId
        def modalLabel = attrs.modalLabel?:'Add/Edit'
        def labelId = attrs.labelId
        def formClass = attrs.formClass
        def modalClass = attrs.modalClass
        def formId = attrs.formId
        def formName = attrs.formName
        def hiddenId = attrs.hiddenId
        def enctype = attrs.enctype
        def asController = attrs.controller
        def asAction = attrs.action
        def asMethod = attrs.method
        String pathToImage1 = g.resource(dir: 'image', file: 'share-modal-icon.jpg')
        def writer = getOut()
        writer << "<div class=\"modal fade\" data-backdrop=\"static\" data-keyboard=\"false\" id=\"${attrs.modalId ? attrs.modalId : 'myModal'}\" tabindex=\"-1\" role=\"dialog\" aria-labelledby=\"myModalLabel\" aria-hidden=\"true\">\n"
        writer << "<div class=\"modal-dialog ${attrs.modalClass ? attrs.modalClass : ''}\">\n"
        writer << "<div class=\"modal-content\">\n"
        writer << "<div class=\"modal-header\">\n"
        writer << "<button type=\"button\" class=\"close\" data-dismiss=\"modal\"><span aria-hidden=\"true\">&times;</span><span  class=\"sr-only\">Close</span></button>\n"
        writer << "<h4 class=\"modal-title\" id=\"${attrs.labelId ? attrs.labelId : 'modalLabelId'}\">${modalLabel}</h4>\n"
        writer << "</div>\n</div>\n"
        writer << "<section class=\"panel\">\n"
        writer << "<div class=\"panel-body create-content\">\n"
        writer << "<form class=\"form-horizontal\" role=\"form\" id=\"${attrs.formId ? attrs.formId : 'modalForm'}\""
        if(enctype) {
            writer << " enctype=\"${enctype}\""
        }
        if(asController && asAction) {
            writer << " action=\"${createLink(controller: asController, action: asAction)}\""
        } else if (asController) {
            writer << " action=\"${createLink(controller: asController)}\""
        }
        if(asMethod) {
            writer << " method=\"${asMethod}\""
        }
        writer << ">\n"
        writer << "<input type=\"hidden\" name=\"id\" id=\"${attrs.hiddenId ? attrs.hiddenId : 'hiddenId'}\">\n"
        writer << "<div class=\"modal-body\">\n"
        writer << body()
        writer << "<div class=\"modal-footer modal-footer-action-btn\">\n"
        writer << "<button type=\"button\" class=\"btn btn-default\" data-dismiss=\"modal\" aria-hidden=\"true\">Cancel</button>\n"
        writer << "<button type=\"submit\" id=\"create-yes-btn\" class=\"btn btn-large btn-primary\">Submit</button>\n</div>\n"
        writer << "<div class=\"modal-footer modal-refresh-processing\" style=\"display: none;\">"
        writer << "<i class=\"fa fa-refresh fa-spin text-center\"></i>\n</div>\n</div>\n</form>\n</div>\n"
        writer << "<div class=\"create-success\" style=\"display: none;\">\n"
        writer << "<div class=\"modal-body\">\n<div class=\"row\">\n<div class=\"col-md-2\">\n"
        writer << "<img src=\"${pathToImage1}\" width=\"60\" height=\"60\"/>\n</div>\n"
        writer << "<div class=\"col-md-10\"><p class=\"message-content\"></p></div>\n</div>\n</div>\n"
        writer << "<div class=\"modal-footer modal-footer-action-btn\">\n<button type=\"button\" class=\"btn btn-default cancel-btn\" data-dismiss=\"modal\" aria-hidden=\"true\">Close</button>\n</div>\n"
        writer << "</div>\n</section>\n</div>\n</div>\n"
    }
    Closure dataTable = { attrs, body ->

        int btnNumber=1
        def tableId=attrs.tableId
        def dataSet=attrs.dataSet
        def heading=attrs.heading
        def actions=attrs.actions
        String buttons=attrs.buttons
        def buttonsArray=attrs.buttons? buttons.split(","):null
        def panel_heading='<header class="panel-heading" style="color: #009900">\n'+heading+'\n'
        attrs.buttons? panel_heading+='<span class="tools pull-right">\n':''
        buttonsArray.each {values ->
            panel_heading+="<a href=\"javascript:;\" class=\"${values} panel-heading-btn-${btnNumber}\"></a>"
            btnNumber=btnNumber+1
        }
        attrs.buttons? panel_heading+='</span>\n':''
        panel_heading+='</header>'
        String tableHeads=attrs.tableHead
        def headArray=tableHeads.split(",")
        int indexNo=0
        def writer = getOut()
        writer << "<div class=\"row\">\n<div class=\"col-md-12\">\n<section class=\"panel\">\n"
        attrs.heading? writer << panel_heading : writer << ''
        writer << "<div class=\"panel-body\">\n<div class=\"table-responsive\">\n"
        writer << "<table class=\"table table-striped table-hover table-bordered\" id=\"${attrs.tableId ? attrs.tableId : 'list-table'}\">\n<thead>\n<tr>\n"
        headArray.each { values ->
            writer << "<th>${values}</th>\n"
        }
        writer << "<th>Action</th>\n"
        writer << "</tr>\n</thead>\n<tbody>\n"
        dataSet.each { dataValues ->
            indexNo=0
            writer << "<tr>\n"
            headArray.each { values ->
                writer << "<td>${dataValues[indexNo]}</td>"
                indexNo=indexNo+1
            }
            if(attrs.actions){
                String actionsStr=attrs.actions
                def actionsArray=actionsStr.split(",")
                int actionsArraySiz=(int) 12/actionsArray.size()
                int count=1
                writer << "<td>\n"

                actionsArray.each {values->
                    writer << "<span class=\"col-md-${actionsArraySiz} no-padding\"><a href=\"\" referenceId=\"${dataValues.DT_RowId}\" class=\"reference-${count}\">\n"
                    writer << "<span class=\"${values}\"></span></a></span>\n"
                    count=count+1
                }
                writer << "</td>\n"
            }else{
                writer << "<td>\n<span class=\"col-md-4 no-padding\"><a href=\"\" referenceId=\"${dataValues.DT_RowId}\" class=\"edit-reference\" title=\"Edit\">\n"
                writer << "<span class=\"fa fa-pencil-square-o\"></span></a></span>\n"
                writer << "<span class=\"col-md-4 no-padding\"><a href=\"\" referenceId=\"${dataValues.DT_RowId}\" class=\"inactive-reference\" title=\"Inactive\">\n"
                writer << "<span class=\"fa fa-retweet\"></span></a></span>\n"
                writer << "<span class=\"col-md-4 no-padding\"><a href=\"\" referenceId=\"${dataValues.DT_RowId}\" class=\"delete-reference\" title=\"Delete\">\n"
                writer << "<span class=\"fa fa-trash\"></span></a></span>\n</td>\n"
            }
            writer << "</tr>\n"
        }
        writer << "</tbody>\n</table>\n</div>\n</div>\n</section>\n</div>\n</div>\n"
    }


}
