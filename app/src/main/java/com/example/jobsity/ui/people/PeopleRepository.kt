package com.example.jobsity.ui.people

import com.example.jobsity.data.classes.PersonResult

class PeopleRepository {

    suspend fun searchPerson(name: String): List<PersonResult>{
        return ShowIndexApi.retrofitService.searchPerson(name)
    }
}