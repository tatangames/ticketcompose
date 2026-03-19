package com.santaananorte.ticket.vistas.login

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.PowerManager
import android.provider.Settings
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.onesignal.OneSignal
import com.santaananorte.ticket.R
import com.santaananorte.ticket.model.ordenes.ModeloTicketPendiente
import com.santaananorte.ticket.viewmodel.NuevasOrdenesViewModel
import com.tatanstudios.astropollococina.componentes.CustomModal1Boton
import com.tatanstudios.astropollococina.componentes.CustomToasty
import com.tatanstudios.astropollococina.componentes.LoadingModal
import com.tatanstudios.astropollococina.componentes.ToastType
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState

// Paleta de colores institucional
private val AzulOscuro  = Color(0xFF0D1B4B)
private val AzulMedio   = Color(0xFF1A3A7A)
private val AzulClaro   = Color(0xFF2E5FBF)
private val DoradoSuave = Color(0xFFD4AF37)
private val BlancoPuro  = Color(0xFFFFFFFF)
private val GrisCard    = Color(0xFFF4F6FB)
private val GrisTexto   = Color(0xFF5A6580)

@Composable
fun LoginScreen(navController: NavHostController, viewModel: NuevasOrdenesViewModel = viewModel()) {

    val ctx = LocalContext.current
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val keyboardController = LocalSoftwareKeyboardController.current

    val isLoading by viewModel.isLoading.observeAsState(initial = false)
    val resultado by viewModel.resultado.observeAsState()

    var listaTickets by remember { mutableStateOf<List<ModeloTicketPendiente>>(emptyList()) }
    val elevation by animateDpAsState(if (isPressed) 10.dp else 4.dp)

    var showModal1Boton by remember { mutableStateOf(false) }
    var modalMensajeString by remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        viewModel.nuevasOrdenesRetrofit()
    }

    resultado?.getContentIfNotHandled()?.let { result ->
        when (result.success) {
            1 -> { listaTickets = result.lista }
            else -> {
                CustomToasty(ctx, stringResource(id = R.string.error_reintentar_de_nuevo), ToastType.ERROR)
            }
        }
    }

    val fondoDegradado = Brush.verticalGradient(
        colors = listOf(AzulOscuro, AzulMedio, AzulClaro)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = fondoDegradado)
            .statusBarsPadding()
            .navigationBarsPadding()  // ← respeta los botones de navegación
            .imePadding()
    ) {
        // Todo en un solo LazyColumn con scroll
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(0.dp)
        ) {

            // ── Header ────────────────────────────────────────────────────
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier
                            .size(110.dp)
                            .background(
                                color = BlancoPuro,
                                shape = RoundedCornerShape(55.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.logope),
                            contentDescription = stringResource(id = R.string.app_name),
                            modifier = Modifier.size(90.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Text(
                        text = stringResource(id = R.string.santa_ana),
                        fontFamily = FontFamily(Font(R.font.montserratmedium)),
                        color = BlancoPuro,
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    // Línea dorada decorativa
                    Box(
                        modifier = Modifier
                            .size(width = 50.dp, height = 3.dp)
                            .background(color = DoradoSuave, shape = RoundedCornerShape(2.dp))
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = "Tickets Pendientes",
                        color = BlancoPuro.copy(alpha = 0.75f),
                        fontSize = 14.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            // ── Botón recargar ────────────────────────────────────────────
            item {
                Button(
                    onClick = {
                        keyboardController?.hide()
                        viewModel.nuevasOrdenesRetrofit()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                        .shadow(elevation = elevation, shape = RoundedCornerShape(30.dp)),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isPressed) DoradoSuave.copy(alpha = 0.85f) else DoradoSuave,
                        contentColor = AzulOscuro,
                    ),
                    interactionSource = interactionSource,
                    shape = RoundedCornerShape(30.dp)
                ) {
                    Text(
                        text = stringResource(id = R.string.recargar),
                        style = TextStyle(
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
            }

            // ── Inicio del panel blanco redondeado ────────────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = GrisCard,
                            shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
                        )
                        .padding(top = 20.dp, start = 16.dp, end = 16.dp)
                ) {
                    if (listaTickets.isEmpty() && !isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Sin tickets pendientes",
                                color = GrisTexto,
                                fontSize = 15.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }

            // ── Items de la lista ─────────────────────────────────────────
            items(listaTickets) { ticket ->
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = GrisCard)
                        .padding(horizontal = 16.dp, vertical = 5.dp)
                ) {
                    TicketItem(ticket)
                }
            }

            // ── Cierre del panel blanco + espacio final ───────────────────
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(color = GrisCard)
                        .padding(bottom = 24.dp)
                )
            }
        }

        // Loading encima de todo
        if (isLoading) {
            LoadingModal(isLoading = true)
        }
    }

    if (showModal1Boton) {
        CustomModal1Boton(
            showModal1Boton,
            modalMensajeString,
            onDismiss = { showModal1Boton = false }
        )
    }
}

// ── Item de cada ticket ──────────────────────────────────────────────────────

@Composable
fun TicketItem(ticket: ModeloTicketPendiente) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
        colors = CardDefaults.cardColors(containerColor = BlancoPuro)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {

            // Nombre unidad
            Text(
                text = ticket.nombreUnidad,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = AzulOscuro,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))
            Divider(color = Color(0xFFE8ECF4), thickness = 1.dp)
            Spacer(modifier = Modifier.height(8.dp))

            // Tipo incidente
            Row(verticalAlignment = Alignment.Top) {
                Text(
                    text = "Incidente  ",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = GrisTexto
                )
                Text(
                    text = ticket.tipoIncidente ?: "Sin descripción",
                    fontSize = 15.sp,
                    color = AzulOscuro,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Fecha registro
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Fecha          ",
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = GrisTexto
                )
                Text(
                    text = ticket.fechaRegistro,
                    fontSize = 15.sp,
                    color = AzulClaro,
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

// ── OneSignal ────────────────────────────────────────────────────────────────

fun getOneSignalUserId(): String {
    return OneSignal.User.pushSubscription.id
}