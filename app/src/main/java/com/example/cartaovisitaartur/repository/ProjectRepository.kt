package com.example.cartaovisitaartur.repository

import com.example.cartaovisitaartur.data.local.ProjectDao
import com.example.cartaovisitaartur.data.local.ProjectEntity
import com.example.cartaovisitaartur.data.remote.GithubApiService
import com.example.cartaovisitaartur.domain.Project
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class ProjectRepository(
    private val dao: ProjectDao,
    private val apiService: GithubApiService,
    private val githubUser: String
) {
    fun getProjectsFlow(): Flow<List<Project>> =
        dao.getAllProjects().map { list ->
            list.map { Project(it.id, it.name, it.description) }
        }

    fun getProjectByIdFlow(id: Int): Flow<Project?> =
        dao.getProjectById(id).map { entity ->
            entity?.let { Project(it.id, it.name, it.description) }
        }

    // Faz sincronia com a API e salva no banco
    suspend fun syncFromNetwork(): Result<Unit> = withContext(Dispatchers.IO) {
        try {
            val repos = apiService.listRepos(githubUser)
            val entities = repos.map { ProjectEntity(it.id, it.name, it.description) }
            dao.insertAll(entities)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
