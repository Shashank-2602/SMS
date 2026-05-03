package com.example.smartattendancesystem.ai

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import java.io.File
import androidx.lifecycle.compose.LocalLifecycleOwner
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.compose.ui.unit.dp
import com.example.smartattendancesystem.ui.theme.AttendanceRecord
import com.example.smartattendancesystem.ui.theme.AttendanceData
import androidx.camera.core.*
import androidx.lifecycle.compose.LocalLifecycleOwner
import java.util.concurrent.Executor

@Composable
fun CameraScreen(studentName: String) {

    val context = LocalContext.current

    val lifecycleOwner = LocalLifecycleOwner.current
    val activity = context as android.app.Activity
    var showResult by remember { mutableStateOf(false) }

    var capturedImage by remember { mutableStateOf<Bitmap?>(null) }
    var storedImage by remember { mutableStateOf<Bitmap?>(null) }
    var alreadyMarked by remember { mutableStateOf(false) }


    val imageCapture = remember { ImageCapture.Builder().build() }

    // Load stored image
    LaunchedEffect(Unit) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                activity,
                arrayOf(Manifest.permission.CAMERA),
                1
            )
        }
    }


    LaunchedEffect(Unit) {
        val file = File(context.filesDir, "$studentName.jpg")
        if (file.exists()) {
            storedImage = BitmapFactory.decodeFile(file.absolutePath)
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        // 🔝 CAMERA
        AndroidView(
            factory = {
                val previewView = PreviewView(it)
                previewView.scaleType = PreviewView.ScaleType.FILL_CENTER

                val cameraProviderFuture = ProcessCameraProvider.getInstance(it)
                cameraProviderFuture.addListener({

                    val cameraProvider = cameraProviderFuture.get()
                    val isMatch = (0..1).random() == 1


                    val preview = Preview.Builder().build().also {
                        it.setSurfaceProvider(previewView.surfaceProvider)
                    }

                    val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(
                        lifecycleOwner,
                        cameraSelector,
                        preview,
                        imageCapture
                    )

                }, ContextCompat.getMainExecutor(it))

                previewView
            },
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.6f)
        )

        // 🔘 BUTTON
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 🔘 CAPTURE BUTTON
            Button(
                onClick = {
                    val file = File(context.cacheDir, "captured.jpg")

                    val outputOptions = ImageCapture.OutputFileOptions.Builder(file).build()

                    imageCapture.takePicture(
                        outputOptions,
                        ContextCompat.getMainExecutor(context),
                        object : ImageCapture.OnImageSavedCallback {

                            override fun onImageSaved(output: ImageCapture.OutputFileResults) {
                                capturedImage = BitmapFactory.decodeFile(file.absolutePath)
                                showResult = true
                            }

                            override fun onError(exception: ImageCaptureException) {
                                exception.printStackTrace()
                            }
                        }
                    )
                }
            ) {
                Text("Capture")
            }

            Spacer(modifier = Modifier.height(8.dp))

// 💾 SAVE FACE BUTTON
            Button(
                onClick = {
                    val file = File(context.filesDir, "$studentName.jpg")

                    capturedImage?.let {
                        val out = java.io.FileOutputStream(file)
                        it.compress(Bitmap.CompressFormat.JPEG, 100, out)
                        out.flush()
                        out.close()

                        storedImage = it
                    }
                }
            ) {
                Text("Save Face")
            }
        }


        // 🔽 BOTTOM SECTION
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .weight(0.3f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (showResult) {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    storedImage?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Stored",
                            modifier = Modifier.size(120.dp)
                        )
                    }

                    capturedImage?.let {
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Captured",
                            modifier = Modifier.size(120.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
            }
            fun isSamePerson(img1: Bitmap, img2: Bitmap): Boolean {
                val diff = kotlin.math.abs(img1.byteCount - img2.byteCount)
                return diff < 50000   // threshold
            }

            if (showResult) {
                when {
                    storedImage == null -> {
                        Text("No stored face. Please register.")
                    }

                    capturedImage != null -> {

                        if (capturedImage != null && storedImage != null) {

                            val match = isSamePerson(storedImage!!, capturedImage!!)

                            if (match) {
                                Text("$studentName is Present")

                                AttendanceData.records.add(
                                    AttendanceRecord(
                                        java.text.SimpleDateFormat("dd-MM-yyyy")
                                            .format(java.util.Date()),
                                        listOf(studentName)
                                    )
                                )

                            } else {
                                Text("$studentName is Absent ❌")
                            }
                        }
                    }
                }
            }
        }
    }
}
