package com.example.jobsity.people

import com.example.jobsity.dataclasses.PersonResult

class PeopleRepository {

    suspend fun searchPerson(name: String): List<PersonResult>{
        return ShowIndexApi.retrofitService.searchPerson(name)
    }
}