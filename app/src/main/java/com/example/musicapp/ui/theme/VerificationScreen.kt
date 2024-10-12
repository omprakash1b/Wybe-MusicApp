package com.example.musicapp.ui.theme

import android.app.Activity
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.musicapp.R
import com.google.android.gms.auth.api.identity.BeginSignInRequest
import com.google.android.gms.auth.api.identity.Identity
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@Composable
fun VerificationScreen(appViewModel: AppViewModel){

    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    var signInResult by remember { mutableStateOf<SignInResult?>(null) }
    val oneTapClient = remember { Identity.getSignInClient(context) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartIntentSenderForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            coroutineScope.launch {
                try {
                    val credential = oneTapClient.getSignInCredentialFromIntent(result.data)
                    val googleCredential = GoogleAuthProvider.getCredential(credential.googleIdToken, null)
                    FirebaseAuth.getInstance().signInWithCredential(googleCredential).await()
                    signInResult = SignInResult.Success
                } catch (e: ApiException) {
                    signInResult = SignInResult.Failure(e)
                }
            }
        }
    }


    val signInRequest = remember {
        BeginSignInRequest.builder()
            .setGoogleIdTokenRequestOptions(
                BeginSignInRequest.GoogleIdTokenRequestOptions.builder()
                    .setSupported(true)
                    .setServerClientId(context.getString(R.string.your_web_client_id)) // Replace with your web client ID
                    .setFilterByAuthorizedAccounts(false)
                    .build()
            )
            .setAutoSelectEnabled(true)
            .build()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.Black),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(painter = painterResource(id = R.drawable.logo_copy), contentDescription = "", modifier = Modifier.padding(10.dp))
        Button(
            shape = RoundedCornerShape(10.dp),
            colors = ButtonColors(
                containerColor = Color.White,
                contentColor = Color.Black,
                disabledContentColor = Color.Transparent,
                disabledContainerColor = Color.Transparent
            ),
            onClick = {
                coroutineScope.launch {
                    try {
                        val result = oneTapClient.beginSignIn(signInRequest).await()
                        launcher.launch(
                            IntentSenderRequest.Builder(result.pendingIntent.intentSender).build()
                        )
                    } catch (e: Exception) {
                        signInResult = SignInResult.Failure(e)
                    }
                }
            }) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.padding(5.dp)
            ) {
                Image(painter = painterResource(id = R.drawable.google), contentDescription = "", modifier = Modifier.size(30.dp))
                Text("Sign in with Google")
            }
        }
    }

    // Handle sign-in result (success, failure, or in progress)
    when (signInResult) {
        is SignInResult.Success -> {
            // User is signed in
            Toast.makeText(context,"Signin Success",Toast.LENGTH_SHORT).show()
            //navHostController.navigate(route = AppScreens.MainScreen.name)
            auth.currentUser!!.let { appViewModel.setUser(it) }
        }
        is SignInResult.Failure -> {
            // Handle sign-in failure
            Text("Sign in failed: ${(signInResult as SignInResult.Failure).exception.message}")
        }
        null -> {
            // Sign-in process is in progress
        }
    }
}

sealed class SignInResult {
    data object Success : SignInResult()
    data class Failure(val exception: Exception) : SignInResult()
}




