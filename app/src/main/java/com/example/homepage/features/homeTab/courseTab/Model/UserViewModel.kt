package com.example.homepage.features.homeTab.courseTab.Model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.homepage.utils.models.CourseData
import com.example.homepage.features.homeTab.courseTab.repos.UserRepository

class UserViewModel : ViewModel() {

    private lateinit var repository: UserRepository
    private val _allUsers = MutableLiveData<List<CourseData>>()
    val allUsers: LiveData<List<CourseData>> = _allUsers


    fun initialize(semesterSelected: String) {
        repository = UserRepository(semesterSelected).getInstance()
        repository.loadUsers(_allUsers)
    }

}