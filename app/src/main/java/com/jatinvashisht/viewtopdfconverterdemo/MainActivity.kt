package com.jatinvashisht.viewtopdfconverterdemo

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.pdf.PdfDocument
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
import android.graphics.Paint
import android.graphics.Typeface
import android.widget.Toast
import androidx.compose.material.Button
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.MultiplePermissionsState
import com.google.accompanist.permissions.PermissionStatus
import com.google.accompanist.permissions.rememberMultiplePermissionsState
import com.jatinvashisht.viewtopdfconverterdemo.ui.theme.ViewToPDFConverterDemoTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import java.io.File
import java.io.FileOutputStream

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
                        val directory = getDirectory()
                        MainUi(context = context, directory = directory)
                    }
                }
            }
        }
    }
    private fun getDirectory(): File{
        val mediaDir = externalMediaDirs.firstOrNull()?.let{
            File(it, resources.getString(R.string.app_name))
                .apply {
                    mkdir()
                }
        }
        return if(mediaDir != null && mediaDir.exists()) mediaDir else filesDir
    }
}


@Composable
fun MainUi(context: Context, directory: File) {
    // this was defined to draw bitmap
    val paint = Paint()

    // defining page properties
    val pageHeight = 1120
    val pageWidth = 792
    val pdfDocument = PdfDocument()
    val title = Paint()
    val myPageInfo = PdfDocument.PageInfo.Builder(pageWidth, pageHeight, 1).create()
    val myPage = pdfDocument.startPage(myPageInfo)
    val canvas = myPage.canvas
    // defining properties of our text
    title.typeface = Typeface.defaultFromStyle(Typeface.NORMAL)
    title.textSize = 15f
    title.textAlign = Paint.Align.CENTER
    title.color = ContextCompat.getColor(context, R.color.black)

    // drawing text using canvas
    canvas.drawText("This is random text used to test the canvas", 396f, 560f, title)
    canvas.drawCircle(500f, 510f, 10f, Paint().apply {
        color = ContextCompat.getColor(context, R.color.purple_700)
    })

    // finishing page
    pdfDocument.finishPage(myPage)
    val file = File(directory, "viewtopdf.pdf")

    Column(modifier = Modifier.fillMaxSize()) {
        Button(onClick = {
            try {
                pdfDocument.writeTo(FileOutputStream(file))
                Toast.makeText(context, "file saved successfully", Toast.LENGTH_LONG).show()
            }catch(e: Exception){
                Log.d("mainactivity", "error occurred while saving file\n $e")
                Toast.makeText(context, "file saved successfully", Toast.LENGTH_LONG).show()
            }
        }) {
            Text("export as pdf")
        }
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
