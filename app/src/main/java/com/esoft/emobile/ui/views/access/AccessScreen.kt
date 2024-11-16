package com.esoft.emobile.ui.views.access

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.esoft.emobile.ui.components.form.fields.TextField
import com.esoft.emobile.ui.customization.PlacaTextField
import com.esoft.emobile.ui.customization.buttons.LoadingButton
import com.esoft.emobile.ui.customization.logos.LogoSisgt
import com.esoft.emobile.ui.transformations.AcronymVisualTransformation
import com.rodrigofs.etiquetaskotlin.ui.screens.access.AccessViewModel

@Composable
fun AccessScreen(
    navController: NavController,
) {
    val viewModel = hiltViewModel<AccessViewModel>()

    val state by viewModel.uiState.collectAsStateWithLifecycle()

    val keyFocusRequester = remember { FocusRequester() }

    val keyboardController = LocalSoftwareKeyboardController.current

    fun access() {
        viewModel.access()
    }

    Scaffold(modifier = Modifier.padding(16.dp)) { paddinfValues ->
        Column(
            modifier = Modifier
                .padding(paddinfValues)
                .fillMaxWidth()
                .fillMaxHeight()
        ) {
            Spacer(modifier = Modifier.fillMaxHeight(0.1f))

            LogoSisgt(modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.fillMaxHeight(0.1f))

            TextField(
                modifier = Modifier
                    .padding(bottom = 8.dp),
                label = "Sigla",
                form = state.form,
                fieldState = state.form.acronym,
                maxLength = 3,
                visualTransformation = AcronymVisualTransformation(),
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.None,
                    autoCorrect = false,
                    keyboardType = KeyboardType.Text,
                    imeAction = ImeAction.Done
                )
            ).apply {
                    changed = { if (it?.length!! <= 3) it.uppercase() ?: "" }
                }.Field()

            Spacer(modifier = Modifier.padding(8.dp))

            PlacaTextField(
                modifier = Modifier.padding(bottom = 8.dp),
                form = state.form,
                focusRequester = keyFocusRequester,
                isEnabled = true,
                keyboardController = keyboardController,
            )

            Spacer(modifier = Modifier.padding(8.dp))

            LoadingButton(
                onClick = {
                    access()
                },
                modifier = Modifier.fillMaxWidth(),
                loading = false,
            ) {
                Text("Acessar", textAlign = TextAlign.Center)
            }
        }
    }
}