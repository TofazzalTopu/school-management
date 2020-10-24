package com.grailslab.enums

/**
 * Created by Hasnat on 5/27/2014.
 */
public enum MainUserType {
    SuperAdmin("SuperAdmin"),
    Admin("Admin"),
    Teacher("Teacher"),
    Accounts("Accounts"),
    Library("Library"),
    HR("Hr"),
    SCHOOL_HEAD("School Head"),
    Organizer("Program Organizer"),
    Parent("Parent"),
    Employee("Employee"),
    Student("Student")

    final String value

    MainUserType(String value) {
        this.value = value
    }
    static Collection<MainUserType> workingUserType(){
        return [Teacher,Accounts,Library,HR,Organizer,Student]
    }
    static Collection<MainUserType> saUserType(){
        return [Admin,Teacher,Accounts,Library,HR,SCHOOL_HEAD,Organizer,Student]
    }
    String toString() { value }
    String getKey() { name() }
}
