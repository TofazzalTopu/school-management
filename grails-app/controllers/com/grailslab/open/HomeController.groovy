package com.grailslab.open


import com.grailslab.CommonUtils
import com.grailslab.command.ListSectionCommand
import com.grailslab.enums.*
import com.grailslab.gschoolcore.ActiveStatus
import com.grailslab.hr.Employee
import com.grailslab.hr.HrCategory
import com.grailslab.hr.HrStaffCategory
import com.grailslab.library.BookCategory
import com.grailslab.library.BookDetails
import com.grailslab.library.OnlineLibrary
import com.grailslab.library.OnlineLibraryCategory
import com.grailslab.settings.ClassName
import com.grailslab.settings.LessonWeek
import com.grailslab.settings.Section
import com.grailslab.settings.SubjectName
import grails.converters.JSON
import org.joda.time.DateTimeConstants
import org.joda.time.LocalDate

class HomeController {

    def userService
    def employeeService
    def messageSource
    def galleryService
    def hrResourceService
    def timeLineService
    def lessonService
    def classSubjectsService
    def classNameService
    def schoolService
    def sectionService

    def shiftExamService
    def examService
    def studentService
    def classRoutineService
    def quickLinkService
    def uploadService
    def noticeBoardService
    def sliderImageService

    def index() {
        def homeVoice = OpenMgmtVoice.findAllByOpenContentTypeAndActiveStatus(OpenContentType.voice,ActiveStatus.ACTIVE, [max: 3,sort: "sortOrder"])
        def homeCarousel = sliderImageService.sliderImageList(SliderType.HOME)
        if(schoolService.runningSchool() != 'nhs') {
            def allHomeQuickLinks = quickLinkService.allHomeQuickLinks()
            def featureContent = OpenContent.findAllByOpenContentTypeAndActiveStatus(OpenContentType.Feature_Content,ActiveStatus.ACTIVE, [max: 4,sort: "sortOrder"])
            def successStory = OpenSuccessStory.findAllByActiveStatus(ActiveStatus.ACTIVE, [max: 20])
            def quickLink = OpenQuickLink.findAllByActiveStatus(ActiveStatus.ACTIVE, [max: 15,sort: "sortIndex"])
            render(view: '/open/home', model: [featureContent:featureContent,homeVoice:homeVoice,homeCarousel:homeCarousel,successStory:successStory,quickLink:quickLink, allHomeQuickLinks:allHomeQuickLinks])
            return
        }
        def primaryNotice = noticeBoardService.noticeBoardList(NoticeCategory.PRIMARY)
        def highSchoolNotice = noticeBoardService.noticeBoardList(NoticeCategory.HIGH_SCHOOL)
        def collegeNotice = noticeBoardService.noticeBoardList(NoticeCategory.COLLEGE)
        render(view: 'indexNew', model: [
                homeVoice: homeVoice,
                primaryNotice: primaryNotice,
                highSchoolNotice: highSchoolNotice,
                collegeNotice: collegeNotice,
                homeCarousel: homeCarousel
        ])
    }

    def ourSchool() {
        String viewPage = '/open/aboutUs/ourSchool'
        if(schoolService.runningSchool() == 'nhs') {
            viewPage = '/open/aboutUs/ourSchoolNew'
        }
        def schoolContent = OpenContent.findAllByOpenContentTypeAndActiveStatus(OpenContentType.About_School,ActiveStatus.ACTIVE, [max: 1,sort: "sortOrder"])
        def facilityContent = OpenContent.findAllByOpenContentTypeAndActiveStatus(OpenContentType.School_Facilities,ActiveStatus.ACTIVE, [max: 1,sort: "sortOrder"])
        def homeCarousel = sliderImageService.sliderImageList(SliderType.ABOUT)
        render(view: viewPage, model: [schoolContent: schoolContent, facilityContent: facilityContent,homeCarousel:homeCarousel])
    }

    def faq() {
        String layoutName = 'open-ltpl'
        if(schoolService.runningSchool() == 'nhs') {
            layoutName = 'newMain'
        }
        def faqList = FaqCategory.findAllByOpenContAndActiveStatus(OpenCont.HOME_FAQ,ActiveStatus.ACTIVE,[sort: "sortPosition"])
        render(view: '/open/aboutUs/faq', layout: layoutName, model: [faqList: faqList])
    }

    def link() {
       /* def linkList = FaqCategory.findAllByOpenContAndActiveStatus(OpenCont.HOME_LINKS,ActiveStatus.ACTIVE,[sort: "sortPosition"])*/
        def galleryQuickLinks = quickLinkService.allGalleryQuickLinks()
        String layoutName = 'open-ltpl'
        if(schoolService.runningSchool() == 'nhs') {
            layoutName = 'newMain'
        }
        render(view: '/open/gallery/links', layout: layoutName, model: [galleryQuickLinks:galleryQuickLinks])
    }

    def managingCommittee() {
        String layoutName = 'open-ltpl'
        if(schoolService.runningSchool() == 'nhs') {
            layoutName = 'newMain'
        }
        def managingCommittee = OpenMgmtVoice.findAllByOpenContentTypeAndActiveStatusAndImagePathIsNotNull(OpenContentType.Mgmt_Committee,ActiveStatus.ACTIVE, [max: 20,sort: "sortOrder"])
        render(view: '/open/aboutUs/managingCommittee', layout: layoutName, model: [managingCommittee: managingCommittee])
    }

    def resources() {
        def staffCategories = hrResourceService.getStaffCategories()

        HrStaffCategory loadCategory
        String keyType
        if(params.stype){
            keyType = params.stype
            loadCategory = HrStaffCategory.findByKeyName(keyType)
        }
        if(!loadCategory && staffCategories){
            loadCategory = staffCategories.first()
        }

        Employee headMaster
        Employee assistantHeadMaster
        List<HrCategory> hrCategoryList = HrCategory.findAllByHrKeyTypeInList([HrKeyType.AHM,HrKeyType.OSTAFF, HrKeyType.TEACHER, HrKeyType.SSTAFF] as List, [sort: "sortOrder"])

        List hrResource = new ArrayList()
        if(loadCategory){
            String staffType = "%${loadCategory.keyName}%"
            headMaster = Employee.findByActiveStatusAndHrStaffCategoryIlikeAndSortOrder(ActiveStatus.ACTIVE, staffType, 1, [sort: "sortOrder"])
            assistantHeadMaster = Employee.findByActiveStatusAndHrStaffCategoryIlikeAndSortOrder(ActiveStatus.ACTIVE, staffType, 2, [sort: "sortOrder"])
            def hrList
            for(hrCategory in hrCategoryList) {
                hrList = Employee.findAllByHrCategoryAndActiveStatusAndHrStaffCategoryIlikeAndSortOrderGreaterThan(hrCategory, ActiveStatus.ACTIVE, staffType, 2, [sort: "sortOrder"])
                if (hrList.size()> 0 ){
                    hrResource.add([hrCategory: hrCategory, hrList:hrList])
                }
            }
        }
        String layoutName = 'open-ltpl'
        if(schoolService.runningSchool() == 'nhs') {
            layoutName = 'newMain'
        }
        render(view: '/open/aboutUs/teacherStuff_multiList', layout: layoutName, model: [
                headMaster:headMaster, assistantHeadMaster:assistantHeadMaster,
                hrResource:hrResource, staffCategories: staffCategories,stype:loadCategory?.keyName, sname:loadCategory?.name
        ])
    }

    def timeline() {
        String layoutName = 'open-ltpl'
        if(schoolService.runningSchool() == 'nhs') {
            layoutName = 'newMain'
        }
        def timelineList = timeLineService.showTimeLine()
        render(view: '/open/timeLine/show', layout: layoutName, model: [timelineList: timelineList])
    }

    def classSectionList(ListSectionCommand command){
        if (!request.method.equals('POST')) {
            redirect(action: 'index')
            return
        }
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        if (command.hasErrors()) {
            def errorList = command?.errors?.allErrors?.collect { messageSource.getMessage(it, null) }
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, errorList?.join('\n'))
            outPut = result as JSON
            render outPut
            return
        }
        def sectionList = sectionService.classSectionsDDList(command.className, command.academicYear)

        if(!sectionList){
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, "No Section added")
            outPut = result as JSON
            render outPut
            return
        }
        result.put(CommonUtils.IS_ERROR, Boolean.FALSE)
        result.put('sectionList', sectionList)
        outPut = result as JSON
        render outPut
        return
    }

    def lessonPlan(Long id){
        String layoutName = 'open-ltpl'
        if(schoolService.runningSchool() == 'nhs') {
            layoutName = 'newMain'
        }
        Section section = Section.read(id)
        def classNameList = classNameService.classNameDropDownList(AcaYearType.SCHOOL)
        if (!section) {
            render(view: '/open/lessonPlan', layout: layoutName, model: [classNameList: classNameList])
            return
        }
        def lessonWeekList = lessonService.lessonWeeksList(section)
        Integer loadWeek = null
        if(params.weekNo){
            loadWeek = params.getInt("weekNo")
        } else {
            LocalDate toDay = new LocalDate()
            if(toDay.getDayOfWeek()==DateTimeConstants.FRIDAY){
                toDay = toDay.minusDays(1)
            }else if(toDay.getDayOfWeek()==DateTimeConstants.SATURDAY){
                toDay = toDay.minusDays(2)
            }
            LessonWeek lessonWeek=lessonService.getWeek(toDay.toDate())
            if (lessonWeek) {
                loadWeek = lessonWeek.weekNumber
            } else {
                def lastWeek = lessonWeekList?.first()
                if (lastWeek) {
                    loadWeek = lastWeek.id
                } else {
                    loadWeek = 1
                }
            }
        }
        SubjectName loadSubject = null
        if(params.subjectId){
            loadSubject = SubjectName.read(params.getLong('subjectId'))
        }
        ClassName className = section.className

        def sectionList = sectionService.sectionDropDownList()
        def subjectList = classSubjectsService.subjectDropDownList(className)
        render(view: '/open/lessonPlan', layout: layoutName, model: [sectionList:sectionList,classNameList: classNameList, lessonList: result, subjectList: subjectList,loadSubject:loadSubject,lessonWeekList:lessonWeekList,loadWeek:loadWeek])
    }

    def save() {
        LinkedHashMap result = new LinkedHashMap()
        String outPut
        if (!request.method.equals('POST')) {
            result.put(CommonUtils.IS_ERROR, Boolean.TRUE)
            result.put(CommonUtils.MESSAGE, 'Invalid Request')
            outPut = result as JSON
            render outPut
            return
        }

        result = userService.saveOrUpdate(params)
        outPut = result as JSON
        render outPut
    }

    def headingDetails(Long id){
        OpenContent openContent = OpenContent.read(id)
        if(!openContent){
           redirect(action: 'index')
            return
        }
        render(view: '/open/aboutUs/headingDetails',model: [openContent:openContent,])
    }

    def image(){
        String layoutName = 'open-ltpl'
        if(schoolService.runningSchool() == 'nhs') {
            layoutName = 'newMain'
        }
        List dataReturns=galleryService.imageList()
        render(view: '/open/gallery/image', layout: layoutName, model: [imageList:dataReturns])
    }

    def imageSlide(Long album){
        if (!album){
            redirect(action: 'image')
            return
        }
        OpenGallery pictureGallery=OpenGallery.read(album)
        if(!pictureGallery){
            render(view: '/open/gallery/imageSlide',model: [imageList:null,albumTitle:"Album Not Found"])
            return
        }
        String albumTitle=pictureGallery.name
        def imageList=OpenGalleryItem.findAllByOpenGalleryAndActiveStatus(pictureGallery, ActiveStatus.ACTIVE, [max: 20, sort: "sortOrder", order: "asc"])
        render(view: '/open/gallery/imageSlide',model: [imageList:imageList,albumTitle:albumTitle])
    }

    def video(){
        String layoutName = 'open-ltpl'
        if(schoolService.runningSchool() == 'nhs') {
            layoutName = 'newMain'
        }
        println params
        List dataReturns = galleryService.videoList()
        render(view: '/open/gallery/video', layout: layoutName,model: [videoList:dataReturns])
    }

    def videoSlide(Long album){
        if (!album){
            redirect(action: 'image')
            return
        }
        OpenGallery videoGallery=OpenGallery.read(album)
        if(!videoGallery){
            render(view: '/open/gallery/videoSlide',model: [imageList:null,albumTitle:"Album Not Found"])
            return
        }
        String albumTitle=videoGallery.name
        def videoList=OpenGalleryItem.findAllByOpenGalleryAndActiveStatus(videoGallery, ActiveStatus.ACTIVE, [max: 20, sort: "sortOrder", order: "asc"])
        render(view: '/open/gallery/videoSlide',model: [videoList:videoList,albumTitle:albumTitle])
    }

    def onlineLibrary(Long id) {
        String layoutName = 'open-ltpl'
        if(schoolService.runningSchool() == 'nhs') {
            layoutName = 'newMain'
        }
        OnlineLibraryCategory libraryCategory
        if (id) {
            libraryCategory = OnlineLibraryCategory.read(id)
        }
        if (libraryCategory) {
            List bookList = OnlineLibrary.findAllByActiveStatusAndCategoryId(ActiveStatus.ACTIVE, libraryCategory.id)
            if(schoolService.runningSchool() == 'nhs') {
                render(view: '/open/gallery/categoryLibraryNew', layout: layoutName,
                        model: [
                                libraryCategory: libraryCategory,
                                bookList: bookList
                        ])
                return
            } else {
                def homeCarousel = sliderImageService.sliderImageList(SliderType.VIRTUAL_LIBRARY)
                render(view: '/open/gallery/categoryLibrary', layout: layoutName,
                        model: [
                                libraryCategory: libraryCategory,
                                bookList: bookList,
                                homeCarousel: homeCarousel
                        ])
                return
            }
        } else {
            List categoryList = OnlineLibraryCategory.findAllByActiveStatus(ActiveStatus.ACTIVE, [sort: "sortOrder", order: "asc"])
            if(schoolService.runningSchool() == 'nhs') {
                render(view: '/open/gallery/onlineLibraryNew', layout: layoutName,
                        model: [
                                categoryList: categoryList,
                        ])
                return
            } else {
                def homeCarousel = sliderImageService.sliderImageList(SliderType.VIRTUAL_LIBRARY)
                render(view: '/open/gallery/onlineLibrary', layout: layoutName,
                        model: [
                                categoryList: categoryList,
                                homeCarousel: homeCarousel
                        ])
                return
            }
        }
    }

    def offlineLibrary(Long id) {
        String layoutName = 'open-ltpl'
        if(schoolService.runningSchool() == 'nhs') {
            layoutName = 'newMain'
        }

        BookCategory bookCategory
        if (id) {
            bookCategory = BookCategory.read(id)
        }
        if (bookCategory) {
            List bookList = BookDetails.findAllByActiveStatusAndCategory(ActiveStatus.ACTIVE, bookCategory)
            if (schoolService.runningSchool() == 'nhs') {
                render(view: '/open/gallery/offlineLibraryCategoryBook', layout: layoutName,
                        model: [libraryCategory: bookCategory, bookList: bookList])
                return
            } else {
                def homeCarousel = sliderImageService.sliderImageList(SliderType.OFFLINE_LIBRARY)
                render(view: '/open/gallery/offlineCategoryLibrary', layout: layoutName, model: [libraryCategory: bookCategory, bookList: bookList, homeCarousel: homeCarousel])
                return
            }
        } else {
            List categoryList = BookCategory.findAllByActiveStatus(ActiveStatus.ACTIVE, [sort: "name", order: "asc"])
            if (schoolService.runningSchool() == 'nhs') {
                render(view: '/open/gallery/offlineLibraryNew', layout: layoutName, model: [categoryList: categoryList])
                return
            } else {
                def homeCarousel = sliderImageService.sliderImageList(SliderType.OFFLINE_LIBRARY)
                render(view: '/open/gallery/offlineLibrary', layout: layoutName, model: [categoryList: categoryList, homeCarousel: homeCarousel])
                return
            }
        }
    }

    def downloadBook() {
        if ( params.identifier == null) {
            flash.message = "File not found."
            redirect (action:'index')
        } else {
            boolean forceDownload = params.getBoolean("forceDownload")
            byte[] buffer = uploadService.getFileInByte(params.identifier, response)
            if(buffer){
                /*response.outputStream << buffer
                response.outputStream.flush()*/

                String outputFileName = "online_book.pdf"
                String contentDisposition = forceDownload ? "attachment;filename=\"${outputFileName}\"" : "filename=\"${outputFileName}\""
                response.setContentType("application/pdf")
                response.setHeader("Content-disposition", contentDisposition)
                response.setHeader("Content-Length", "${buffer.length}")
                response.outputStream << buffer
            }  else {
                flash.message = "File not found."
                redirect (action:'index')
            }
        }
    }
}
