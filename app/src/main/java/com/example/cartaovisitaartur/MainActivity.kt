package com.example.cartaovisitaartur

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavType
import androidx.navigation.compose.*
import androidx.navigation.compose.rememberNavController
import com.example.cartaovisitaartur.ui.theme.CartaoVisitaArturTheme
import androidx.navigation.navArgument
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.remember
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue




class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CartaoVisitaArturTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    AppNavigation()
                }
            }
        }
    }
}

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val context = LocalContext.current

    // criar DB e DAO
    val db = remember {
        androidx.room.Room.databaseBuilder(
            context,
            com.example.cartaovisitaartur.data.local.AppDatabase::class.java,
            "app_database"
        ).fallbackToDestructiveMigration().build()
    }
    val dao = db.projectDao()

    // criar repo usando o usuário que você informou
    val repo = remember { com.example.cartaovisitaartur.repository.ProjectRepository(dao, com.example.cartaovisitaartur.data.remote.RetrofitInstance.api, "artur-leao-neto") }

    // criar ViewModel com factory
    val viewModel: com.example.cartaovisitaartur.ui.viewmodel.ProjectListViewModel = androidx.lifecycle.viewmodel.compose.viewModel(
        factory = com.example.cartaovisitaartur.ui.viewmodel.ProjectListViewModelFactory(repo)
    )

    NavHost(navController = navController, startDestination = "perfil") {
        composable("perfil") {
            CartaoDeVisitas(
                onVerProjetosClick = {
                    navController.navigate("projetos")
                }
            )
        }

        composable("projetos") {
            TelaListaProjetos(
                viewModel = viewModel,
                onProjetoClick = { projetoId ->
                    navController.navigate("detalhes/$projetoId")
                },
                onVoltar = { navController.popBackStack() }
            )
        }

        composable(
            route = "detalhes/{id}",
            arguments = listOf(navArgument("id") { type = NavType.IntType })
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getInt("id") ?: 0
            TelaDetalhesProjeto(
                id = id,
                repository = repo,
                onVoltar = { navController.popBackStack() }
            )
        }
    }
}



@Composable
fun CartaoDeVisitas(onVerProjetosClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                .background(Color(0xFFBBC4EF))
                .padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.avatar_artur),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(240.dp)
                        .clip(CircleShape)
                )
                Spacer(modifier = Modifier.height(48.dp))
                Text(
                    text = "Artur Ferreira Leão Neto",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = "Tecnologista em Sistemas para Internet",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(48.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    ContatoRow(icone = R.drawable.ic_phone, texto = "(83) 99948-9252")
                    Spacer(modifier = Modifier.height(8.dp))
                    ContatoRow(icone = R.drawable.ic_email, texto = "artur20240022627@alu.uern.br")
                    Spacer(modifier = Modifier.height(8.dp))
                    ContatoRow(icone = R.drawable.ic_location, texto = "Conde - PB")
                    Spacer(modifier = Modifier.height(8.dp))
                    ContatoRow(icone = R.drawable.ic_github, texto = "github.com/artur")

                    Spacer(modifier = Modifier.height(32.dp))

                    // 🔘 Novo botão
                    Button(
                        onClick = onVerProjetosClick,
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF313C93)),
                        shape = RoundedCornerShape(8.dp),
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("Ver Meus Projetos")
                    }
                }
            }
        }
    }
}


@Composable
fun ContatoRow(icone: Int, texto: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = icone),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = texto)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaListaProjetos(
    viewModel: com.example.cartaovisitaartur.ui.viewmodel.ProjectListViewModel,
    onProjetoClick: (Int) -> Unit,
    onVoltar: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Meus Projetos") },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {

            when (uiState) {
                is com.example.cartaovisitaartur.ui.viewmodel.UiState.Loading -> {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator()
                        Spacer(modifier = Modifier.height(12.dp))
                        Text("Carregando projetos...")
                    }
                }

                is com.example.cartaovisitaartur.ui.viewmodel.UiState.Success -> {
                    val projetos =
                        (uiState as com.example.cartaovisitaartur.ui.viewmodel.UiState.Success).projects

                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(projetos) { projeto ->
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { onProjetoClick(projeto.id) },
                                colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F0F8))
                            ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                    Text(projeto.nome, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(projeto.descricao ?: "")
                                }
                            }
                        }
                    }
                }

                is com.example.cartaovisitaartur.ui.viewmodel.UiState.Error -> {
                    val msg =
                        (uiState as com.example.cartaovisitaartur.ui.viewmodel.UiState.Error).message

                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text("Erro: $msg")
                        Spacer(modifier = Modifier.height(12.dp))
                        Button(onClick = { /* você pode implementar retry opcional */ }) {
                            Text("Tentar novamente")
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaDetalhesProjeto(id: Int, repository: com.example.cartaovisitaartur.repository.ProjectRepository, onVoltar: () -> Unit) {
    // coleta diretamente do repositório (Flow -> State)
    val projectState = repository.getProjectByIdFlow(id).collectAsState(initial = null)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(projectState.value?.nome ?: "Detalhes") },
                navigationIcon = {
                    IconButton(onClick = onVoltar) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                            contentDescription = "Voltar"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val projeto = projectState.value
            if (projeto == null) {
                Text("Projeto não encontrado", fontSize = 20.sp)
            } else {
                Text(
                    text = "ID do projeto: ${projeto.id}",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = projeto.descricao ?: "Sem descrição")
            }
        }
    }
}
