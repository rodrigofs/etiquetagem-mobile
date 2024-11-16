package com.esoft.emobile.ui.views.tag

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Print
import androidx.compose.material.icons.filled.Tag
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.esoft.emobile.support.PermissionManager
import com.esoft.emobile.ui.MainActivityViewModel
import com.esoft.emobile.ui.components.form.fields.TextField
import com.esoft.emobile.ui.customization.BarcodeCameraPreview
import com.esoft.emobile.ui.customization.ScreenBase
import com.esoft.emobile.ui.customization.buttons.LoadingButton
import com.esoft.emobile.ui.customization.inputs.ChaveAcessoInput

import com.esoft.emobile.ui.views.tag.partials.InfoCard
import com.esoft.emobile.ui.views.tag.partials.InfoField
import kotlinx.coroutines.launch


@Composable
fun PrintTagScreen(
    modifier: Modifier = Modifier,
    permissionManager: PermissionManager,
    bluetoothViewModel: MainActivityViewModel,
    onConnectPrint: () -> Unit = {},
    navController: NavController
) {

    val viewModel: PrinterViewModel = hiltViewModel()
    val state = viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()
    val focusChaveAcessoRequester = remember { FocusRequester() }
    val focusVolumesRequester = remember { FocusRequester() }

    val keyboardController = LocalSoftwareKeyboardController.current

    var hasCameraPermission by remember { mutableStateOf(false) }

    RequestCameraPermissions(permissionManager) {
        hasCameraPermission = it.hasCameraPermission
    }

    LaunchedEffect(key1 = bluetoothViewModel.selectedDevice.value) {
        if (bluetoothViewModel.selectedDevice.value == null) {
            onConnectPrint()
        }
    }

    LaunchedEffect(Unit) {
        focusChaveAcessoRequester.requestFocus()
    }

    ScreenBase(
        title = "Impressão de Etiquetas",
        onLogoff = { scope.launch { viewModel.clearPlate() } }) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Spacer(modifier = modifier.padding(12.dp))

            InfoCard(title = "DADOS DA OPERAÇÃO") {
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start
                ) {
                    InfoField(
                        label = "UNIDADE: ",
                        value = viewModel._acronym.value.uppercase(),
                        modifier = Modifier.weight(0.5f)
                    )

                    InfoField(
                        label = "PLACA: ",
                        value = viewModel._plate.value.uppercase(),
                        modifier = Modifier.weight(0.5f)
                    )
                }
            }

            Spacer(modifier = modifier.padding(4.dp))

            ChaveAcessoInput(
                type = "NFE",
                form = state.value.form,
                focusRequester = focusChaveAcessoRequester
            ) {
                keyboardController?.hide()
                if (hasCameraPermission) {
                    viewModel.readBarcode()
                }

            }

            Row(
                Modifier
                    .padding(top = 10.dp)
                    .height(IntrinsicSize.Min),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    modifier = modifier
                        .weight(.5f)
                        .focusRequester(focusVolumesRequester)
                        .height(IntrinsicSize.Min),
                    form = state.value.form,
                    fieldState = state.value.form.volumes,
                    keyboardOptions = KeyboardOptions(
                        autoCorrectEnabled = false,
                        keyboardType = KeyboardType.Number,
                        imeAction = ImeAction.Go
                    ),
                    keyboardActions = KeyboardActions(
                        onGo = {
                            scope.launch {
                                viewModel.fetchTag()
                            }
                        }
                    ),
                    label = "Vol"
                ).Field()
                Spacer(modifier = modifier.padding(8.dp))

                LoadingButton(
                    loading = viewModel.fetching.value,
                    onClick = {
                        scope.launch {
                            viewModel.fetchTag()
                        }
                    },
                    modifier = Modifier
                        .weight(.5f)
                        .height(OutlinedTextFieldDefaults.MinHeight)
                        .padding(top = 2.dp),
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tag,
                            contentDescription = "Preparar etiquetas"
                        )

                        Text("Preparar")
                    }
                }
            }

            if (state.value.nf.value.etiquetas.isNotEmpty()) {
                Spacer(modifier = Modifier.padding(16.dp))

                InfoCard(title = "DADOS DA NF-e") {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        InfoField(
                            label = "NF: ",
                            value = state.value.nf.value.nf,
                            modifier = Modifier.weight(0.3f)
                        )

                        InfoField(
                            label = "SÉRIE: ",
                            value = state.value.nf.value.serie,
                            modifier = Modifier.weight(0.3f)
                        )

                        InfoField(
                            label = "VOLUMES: ",
                            value = state.value.nf.value.volumes.toString(),
                            modifier = Modifier.weight(0.3f)
                        )
                    }

                    Spacer(modifier = Modifier.padding(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        InfoField(
                            label = "REMETENTE: ",
                            value = state.value.nf.value.emitNome.uppercase(),
                            modifier = Modifier.weight(0.3f)
                        )
                    }

                    Spacer(modifier = Modifier.padding(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        InfoField(
                            label = "DESTINATARIO: ",
                            value = state.value.nf.value.destNome.uppercase(),
                            modifier = Modifier.weight(0.3f)
                        )
                    }

                    Spacer(modifier = Modifier.padding(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Start
                    ) {
                        InfoField(
                            label = "DESTINO: ",
                            value = "${state.value.nf.value.destCidade.uppercase()} - ${state.value.nf.value.destUf.uppercase()}",
                            modifier = Modifier.weight(0.3f)
                        )
                    }

                }

                Spacer(modifier = Modifier.padding(8.dp))

                LoadingButton(
                    loading = viewModel.printing.value,
                    onClick = {
                        viewModel.startPrint(
                            state.value.nf.value.etiquetas,
                            bluetoothViewModel.zplPrinter
                        )
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Print,
                            contentDescription = "Imprimir etiquetas"
                        )

                        Spacer(modifier = Modifier.padding(4.dp))

                        Text("Imprimir etiquetas")
                    }
                }
            }
        }

//        if (viewModel.showCamera.value) {
//            CameraPreview(barcodeAnalyzer = barcodeAnalyzer, onClosePreview = {
//                viewModel.showCamera.value = false
//            }, enableAutoFocus = autofoco.value)
//        }


    }

    if (viewModel.showCamera.value) {
        BarcodeCameraPreview(
            onBarcodeDetected = { code, _ ->
                state.value.form.accessKey.state.value = code
            },
            onClosePreview = { viewModel.showCamera.value = false },
        )
    }
}


@Composable
private fun RequestCameraPermissions(
    permissionManager: PermissionManager,
    onPermissionsResult: (PermissionsCameraState) -> Unit
) {
    var hasCameraPermission by remember { mutableStateOf(false) }

    permissionManager.RequestCameraPermission {
        hasCameraPermission = true
        onPermissionsResult(PermissionsCameraState(hasCameraPermission))
    }
}

private data class PermissionsCameraState(
    val hasCameraPermission: Boolean
)