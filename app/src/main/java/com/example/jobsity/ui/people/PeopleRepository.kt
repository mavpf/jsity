package com.example.jobsity.ui.people

import com.example.jobsity.data.classes.PersonResult
import com.example.jobsity.network.api.ServiceApiHelper
import javax.inject.Inject

class PeopleRepository @Inject constructor(
    private val serviceApiHelper: ServiceApiHelper
) {

    suspend fun searchPerson(name: String): List<PersonResult>{
        return serviceApiHelper.searchPerson(name)
    }
}