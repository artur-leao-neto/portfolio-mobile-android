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
            TelaDetalhesProjeto(id, onVoltar = { navController.popBackStack() })
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

data class Projeto(val id: Int, val nome: String, val descricao: String)

val mockProjetos = listOf(
    Projeto(1, "App de Receitas", "Aplicativo que exibe receitas e ingredientes."),
    Projeto(2, "Catálogo de Filmes", "Lista de filmes com avaliação e sinopse."),
    Projeto(3, "To-Do List", "Aplicativo simples de tarefas diárias."),
    Projeto(4, "Site Pessoal", "Página web com portfólio e contatos."),
)


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaDetalhesProjeto(id: Int, onVoltar: () -> Unit) {
    val projeto = mockProjetos.find { it.id == id }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(projeto?.nome ?: "Detalhes") },
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
            Text(
                text = "ID do projeto: ${projeto?.id}",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = projeto?.descricao ?: "Descrição não encontrada")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TelaListaProjetos(onProjetoClick: (Int) -> Unit, onVoltar: () -> Unit) {
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(mockProjetos) { projeto ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onProjetoClick(projeto.id) },
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F0F8))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(projeto.nome, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(projeto.descricao)
                    }
                }
            }
        }
    }
}


