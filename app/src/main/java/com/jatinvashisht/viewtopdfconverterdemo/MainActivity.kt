package com.jatinvashisht.viewtopdfconverterdemo

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.jatinvashisht.viewtopdfconverterdemo.ui.theme.ViewToPDFConverterDemoTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalPermissionsApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ViewToPDFConverterDemoTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    val permissionState = rememberMultiplePermissionsState(
                        permissions = listOf(
                            Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                        )
                    )

                    val context = LocalContext.current
                    SideEffect {
                        if (!permissionState.allPermissionsGranted) {
                            permissionState.launchMultiplePermissionRequest()
                        }
                    }

                    val allPermissionsGiven = requestPermission(permissionState = permissionState)
//                    val permissions = listOf<String>(
//                        Manifest.permission.READ_EXTERNAL_STORAGE,
//                        Manifest.permission.WRITE_EXTERNAL_STORAGE
//                    )
//                    permissions.forEach { permission ->
//                        val isPermissionGranted = isPermissionGranted(permission, context)
//                        Log.d("mainscreen", "is permission granted is $isPermissionGranted")
//                        allPermissionsGiven = allPermissionsGiven && isPermissionGranted
//                    }
                    if (allPermissionsGiven) {
                        MainUi()
                    }
                }
            }
        }
    }
}


@Composable
fun MainUi() {
    Column(modifier = Modifier.fillMaxSize()) {

    }
}

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun requestPermission(permissionState: MultiplePermissionsState): Boolean {
    permissionState.permissions.forEach { perm ->
        when (perm.permission) {
            Manifest.permission.READ_EXTERNAL_STORAGE -> {
                when (perm.status) {
                    is PermissionStatus.Denied -> {

                    }
                    PermissionStatus.Granted -> {

                    }
                }
            }
            Manifest.permission.WRITE_EXTERNAL_STORAGE -> {
                when (perm.status) {
                    is PermissionStatus.Denied -> {

                    }
                    PermissionStatus.Granted -> {

                    }
                }
            }
        }
    }
    val allPermissionsGranted = permissionState.allPermissionsGranted
    return allPermissionsGranted
}

private fun isPermissionGranted(permission: String, context: Context): Boolean {
    return ContextCompat.checkSelfPermission(
        context,
        permission
    ) == PackageManager.PERMISSION_GRANTED
}
