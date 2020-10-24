package myschool

import com.amaruddog.myschool.Role
import com.amaruddog.myschool.User
import com.grailslab.accounting.ChartOfAccount
import com.grailslab.enums.AccountType
import com.grailslab.enums.HrKeyType
import com.grailslab.enums.MainUserType
import com.grailslab.enums.NodeType
import com.grailslab.hr.HrCategory
import com.grailslab.hr.HrStaffCategory
import com.grailslab.settings.School
import com.grailslab.enums.AvailableRoles
import com.amaruddog.myschool.UserRole

class BootStrap {

    def init = { servletContext ->
//        initNewSchool()
    }
    void initNewSchool(){
        seedUserWithRole()
        seedSchoolHr()
        seedAccountData()
//        seedPublicWebSite()
    }

    void seedSchoolHr(){
        HrCategory.findByHrKeyType(HrKeyType.HM) ?: new HrCategory(hrKeyType: HrKeyType.HM, name: 'School Head', sortOrder: 3).save(flush: true)
        HrCategory.findByHrKeyType(HrKeyType.AHM) ?: new HrCategory(hrKeyType: HrKeyType.AHM, name: 'Assistant Head', sortOrder: 2).save(flush: true)
        HrCategory.findByHrKeyType(HrKeyType.TEACHER) ?: new HrCategory(hrKeyType: HrKeyType.TEACHER, name: 'Teacher', sortOrder: 1).save(flush: true)
        HrCategory.findByHrKeyType(HrKeyType.OSTAFF) ?: new HrCategory(hrKeyType: HrKeyType.OSTAFF, name: 'Office Staff', sortOrder: 4).save(flush: true)
        HrCategory.findByHrKeyType(HrKeyType.SSTAFF) ?: new HrCategory(hrKeyType: HrKeyType.SSTAFF, name: 'Support Staff', sortOrder: 5).save(flush: true)

        //Staff Category for showing staff list in public website
//        HrStaffCategory.findByKeyName('general') ?: new HrStaffCategory(keyName: 'general', name: 'General Section', description: 'School Start journey on 1965. Mr. Rahman was the founding Headmaster of the school. Many talented teachers teaching in this school. Now there is 25 full time teacher, 5 staff and 3 support staff in General Section', sortOrder:1).save(flush: true)
//        HrStaffCategory.findByKeyName('vocational') ?: new HrStaffCategory(keyName: 'vocational', name: 'Vocational Section', description: 'Vocational Section start journey on 1997. Mr. ABC was the key person to introduce this sction. Many talented teachers teaching in this school. Now there is 25 full time teacher, 5 staff and 3 support staff in General Section', sortOrder:2).save(flush: true)

        HrStaffCategory.findByKeyName('morning') ?: new HrStaffCategory(keyName: 'morning', name: 'Morning Shift', description: 'Morning Shift start on 2001. Usual Session from 7:30 AM to 10:30 AM. Classes hold from Sunday to Thrusday. Mr. ABC was the key person to introduce this sction. Many talented teachers teaching in this school. Now there is 25 full time teacher, 5 staff and 3 support staff in General Section', sortOrder:1).save(flush: true)
        HrStaffCategory.findByKeyName('day') ?: new HrStaffCategory(keyName: 'day', name: 'Day Shift', description: 'Day Shift start on 2001. Usual Session from 10:30 AM to 2:30 PM. Classes hold from Sunday to Thrusday. Mr. ABC was the key person to introduce this sction. Many talented teachers teaching in this school. Now there is 25 full time teacher, 5 staff and 3 support staff in General Section', sortOrder:2).save(flush: true)

        HrStaffCategory.findByKeyName('primary') ?: new HrStaffCategory(keyName: 'primary', name: 'Primary Section', description: 'Day Shift start on 2001. Usual Session from 10:30 AM to 2:30 PM. Classes hold from Sunday to Thrusday. Mr. ABC was the key person to introduce this sction. Many talented teachers teaching in this school. Now there is 25 full time teacher, 5 staff and 3 support staff in General Section', sortOrder:5).save(flush: true)
        HrStaffCategory.findByKeyName('school') ?: new HrStaffCategory(keyName: 'school', name: 'School Section', description: 'Day Shift start on 2001. Usual Session from 10:30 AM to 2:30 PM. Classes hold from Sunday to Thrusday. Mr. ABC was the key person to introduce this sction. Many talented teachers teaching in this school. Now there is 25 full time teacher, 5 staff and 3 support staff in General Section', sortOrder:6).save(flush: true)
//        HrStaffCategory.findByKeyName('college') ?: new HrStaffCategory(keyName: 'college', name: 'College Section', description: 'Day Shift start on 2001. Usual Session from 10:30 AM to 2:30 PM. Classes hold from Sunday to Thrusday. Mr. ABC was the key person to introduce this sction. Many talented teachers teaching in this school. Now there is 25 full time teacher, 5 staff and 3 support staff in General Section', sortOrder:7).save(flush: true)
    }
    void seedAccountData() {
        ChartOfAccount.findByCode(100000)?:new ChartOfAccount(name:"Asset", code:100000,nodeType: NodeType.ROOT, accountType: AccountType.ASSET).save()
        ChartOfAccount.findByCode(200000)?:new ChartOfAccount(name:"Income", code:200000,nodeType:NodeType.ROOT, accountType:AccountType.INCOME).save()
        ChartOfAccount.findByCode(300000)?:new ChartOfAccount(name:"Expense", code:300000,nodeType:NodeType.ROOT, accountType:AccountType.EXPENSE).save()
    }

    //new school init
    void seedUserWithRole() {
        School school = School.findByName("LightHouse School & College")
        if (!school) {
            school = new School(name: 'LightHouse School & College', address: '25-26, Nawab Sirajuddoullah Road, Narayanganj', email: 'lighthouse@gmail.com', contactNo: '01716282884')
            school.save()
        }
        AvailableRoles.values().each {
            if (!Role.findByAuthority(it.value())) {
                new Role(authority: it.value()).save()
            }
        }
        def roleSwitchUser = Role.findByAuthority(AvailableRoles.SWITCH_USER.value())
        def roleSuperAdmin = Role.findByAuthority(AvailableRoles.SUPER_ADMIN.value())
        def roleAdmin = Role.findByAuthority(AvailableRoles.ADMIN.value())
        def roleAccount = Role.findByAuthority(AvailableRoles.ACCOUNTS.value())

        //admin user
        User admin = User.findByUsername('admin') ?: new User(username: 'admin', password: 'admin@369', schoolId: 10000, mainUserType: MainUserType.Admin).save(flush: true)
        if (!UserRole.findByUserAndRole(admin, roleSwitchUser)) {
            UserRole.create(admin, roleSwitchUser, true)
        }
        if (!UserRole.findByUserAndRole(admin, roleAdmin)) {
            UserRole.create(admin, roleAdmin, true)
        }
        User account = User.findByUsername('account') ?: new User(username: 'account', password: 'account@369', schoolId: 10000, mainUserType: MainUserType.Accounts).save(flush: true)
        if (!UserRole.findByUserAndRole(account, roleAccount)) {
            UserRole.create(account, roleAccount, true)
        }
    }
    def destroy = {
    }
}
