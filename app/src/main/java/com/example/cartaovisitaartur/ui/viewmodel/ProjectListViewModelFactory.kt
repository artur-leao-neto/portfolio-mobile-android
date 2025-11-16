package com.example.cartaovisitaartur.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.cartaovisitaartur.repository.ProjectRepository

class ProjectListViewModelFactory(private val repository: ProjectRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProjectListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ProjectListViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
