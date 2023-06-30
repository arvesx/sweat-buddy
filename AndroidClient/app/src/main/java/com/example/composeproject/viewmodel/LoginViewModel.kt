package com.example.composeproject.viewmodel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import com.example.composeproject.BackendCommunicator
import com.example.composeproject.Screen
import com.example.composeproject.dependencies.fileprocessing.TransmissionObjectBuilder
import com.example.composeproject.dependencies.fileprocessing.TransmissionObjectType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class LoginViewModel : ViewModel() {
    var usernameText = mutableStateOf(TextFieldValue(""))
    var passwordText = mutableStateOf(TextFieldValue(""))

    fun onLogin(navController: NavController, sharedViewModel: SharedViewModel) {
        val scope = CoroutineScope(Dispatchers.IO)
        scope.launch {
            val backendCommunicator = BackendCommunicator.getInstance()
            val to = BackendCommunicator.createTransmissionObject(TransmissionObjectType.LOGIN_MESSAGE)
            to.username = usernameText.value.text
            to.password = passwordText.value.text
            println(to.username)
            println(to.password)
            val answer = backendCommunicator.sendClientInfo(to)

            withContext(Dispatchers.Main) {

                if (answer.success == 1) {
                    sharedViewModel.updateViewModel(answer)
                    sharedViewModel.username.value = usernameText.value.text
                    navController.navigate(Screen.HomeScreen.route) {
                        popUpTo(0)
                    }
                }

            }
        }


    }

}