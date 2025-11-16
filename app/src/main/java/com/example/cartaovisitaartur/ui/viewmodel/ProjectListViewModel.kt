package com.example.cartaovisitaartur.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.cartaovisitaartur.domain.Project
import com.example.cartaovisitaartur.repository.ProjectRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

sealed class UiState {
    object Loading : UiState()
    data class Success(val projects: List<Project>) : UiState()
    data class Error(val message: String) : UiState()
}

class ProjectListViewModel(private val repository: ProjectRepository) : ViewModel() {

    private val _uiState = MutableStateFlow<UiState>(UiState.Loading)
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        // 1) coletar do banco (sempre observável)
        viewModelScope.launch {
            repository.getProjectsFlow().collectLatest { projects ->
                // se já sincronizamos, mostramos; mas enquanto sincroniza, manter Loading
                if (_uiState.value is UiState.Loading) {
                    // quando o banco emitir pela primeira vez, poderíamos já mostrar (mas o PDF pede indicador enquanto busca)
                    _uiState.value = UiState.Success(projects)
                } else {
                    _uiState.value = UiState.Success(projects)
                }
            }
        }

        // 2) sincronizar com a rede (show loading during sync)
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            val result = repository.syncFromNetwork()
            if (result.isFailure) {
                _uiState.value = UiState.Error(result.exceptionOrNull()?.localizedMessage ?: "Erro ao carregar")
            } else {
                // após a sincronização, os dados do DB serão emitidos automaticamente pela coleta acima
                // mas garantimos que não fique em Loading
                // Se o DB ainda estiver vazio, a coleta já atualizou para Success([]) possivelmente.
            }
        }
    }
}
