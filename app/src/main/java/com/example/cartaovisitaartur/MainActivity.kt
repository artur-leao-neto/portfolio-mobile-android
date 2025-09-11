package com.example.cartaovisitaartur

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import com.example.cartaovisitaartur.ui.theme.CartaoVisitaArturTheme
import com.example.cartaovisitaartur.R
import androidx.compose.foundation.shape.RoundedCornerShape


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CartaoVisitaArturTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    CartaoDeVisitas()
                }
            }
        }
    }
}

@Composable
fun CartaoDeVisitas() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color(0xFF04497E))
                .clip(RoundedCornerShape(bottomStart = 32.dp, bottomEnd = 32.dp))
                .padding(vertical = 32.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {

                Image(
                    painter = painterResource(id = R.drawable.avatar_artur),
                    contentDescription = "Avatar",
                    modifier = Modifier
                        .size(120.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Artur Ferreira Leão Neto",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                Text(
                    text = "Tecnologista em Sistemas para Internet",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.Start
        ) {
            ContatoRow(icone = R.drawable.ic_phone, texto = "(83) 99948-9252")
            Spacer(modifier = Modifier.height(8.dp))
            ContatoRow(icone = R.drawable.ic_email, texto = "artur20240022627@alu.uern.br")
            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ContatoRow(icone = R.drawable.ic_location, texto = "Conde - PB")
                ContatoRow(icone = R.drawable.ic_github, texto = "github.com/artur")
            }
        }
    }
}

@Composable
fun ContatoRow(icone: Int, texto: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(id = icone),
            contentDescription = null,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = texto)
    }
}
