package com.grailslab.gschoolcore

/**
 * Created by aminul on 8/25/2015.
 */
public enum AcademicYear {
    Y2022('2022'),
    Y2021('2021'),
    Y2020('2020'),
    Y2019('2019'),
    Y2018('2018'),
    Y2017('2017'),
    Y2016('2016'),
    Y2015('2015'),
    Y2014('2014'),

    Y2021_2022('2021-2022'),
    Y2020_2021('2020-2021'),
    Y2019_2020('2019-2020'),
    Y2018_2019('2018-2019'),
	Y2017_2018('2017-2018'),
    Y2016_2017('2016-2017'),
    Y2015_2016('2015-2016'),

    final String value

    static Collection<AcademicYear> schoolYears(){
        return [Y2022, Y2021, Y2020, Y2019, Y2018, Y2017, Y2016, Y2015, Y2014]
    }
    static Collection<AcademicYear> collegeYears(){
        return [Y2021_2022, Y2020_2021, Y2019_2020, Y2018_2019, Y2017_2018, Y2016_2017, Y2015_2016]
    }
    AcademicYear(String value) {
        this.value = value
    }

    String toString() { value }
    String getKey() { name() }

    public static AcademicYear getYearByString(String year){
        for(AcademicYear e : AcademicYear.values()){
            if(year == e.value) return e;
        }
        return null;
    }
}