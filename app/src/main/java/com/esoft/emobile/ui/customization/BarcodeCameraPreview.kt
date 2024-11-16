package com.esoft.emobile.ui.customization

import android.annotation.SuppressLint
import android.graphics.Rect
import android.view.MotionEvent
import androidx.camera.core.CameraControl
import androidx.camera.core.CameraSelector
import androidx.camera.core.FocusMeteringAction
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.SurfaceOrientedMeteringPointFactory
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.esoft.emobile.support.AccessKeyValidator
import com.google.mlkit.vision.barcode.BarcodeScannerOptions
import com.google.mlkit.vision.barcode.BarcodeScanning
import com.google.mlkit.vision.barcode.ZoomSuggestionOptions
import com.google.mlkit.vision.barcode.common.Barcode
import com.google.mlkit.vision.common.InputImage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.util.concurrent.TimeUnit

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun BarcodeCameraPreview(
    onBarcodeDetected: (barcode: String, boundingBox: Rect?) -> Unit,
    onClosePreview: () -> Unit,
    enableAutoFocus: Boolean = false,
    enableAutoZoom: Float? = 0f,
    minimumTimeBetweenAnalysis: Long = 100L
) {
    val context = LocalContext.current
    val lifecycleOwner = androidx.lifecycle.compose.LocalLifecycleOwner.current
    val cameraProviderFuture = remember { ProcessCameraProvider.getInstance(context) }
    val previewView = remember { PreviewView(context) }
    val coroutineScope = rememberCoroutineScope()

    var cameraControl by remember { mutableStateOf<CameraControl?>(null) }
    var lineColor by remember { mutableStateOf(Color.Red) }
    val executor = remember { ContextCompat.getMainExecutor(context) }

    DisposableEffect(lifecycleOwner) {
        val listener = Runnable {
            try {
                val cameraProvider = cameraProviderFuture.get()

                val preview = Preview.Builder()
                    .setTargetRotation(previewView.display.rotation)
                    .build()
                    .also { it.surfaceProvider = previewView.surfaceProvider }

                val imageAnalysis = ImageAnalysis.Builder()
                    .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
                    .setOutputImageFormat(ImageAnalysis.OUTPUT_IMAGE_FORMAT_YUV_420_888)
                    .setTargetRotation(previewView.display.rotation)
                    .build()
                    .also {
                        it.setAnalyzer(
                            executor,
                            BarcodeAnalyzer(
                                onBarcodeDetected = { barcode, boundingBox ->
                                    onBarcodeDetected(barcode, boundingBox)
                                },
                                minimumTimeBetweenAnalysis = minimumTimeBetweenAnalysis,
                                zoomCallback = { suggestedZoom ->
                                    if (suggestedZoom == 0f)
                                        cameraControl?.setLinearZoom(suggestedZoom)
                                    true
                                },
                                onBarcodeFound = {
                                    coroutineScope.launch(Dispatchers.Main) {
                                        lineColor =
                                            Color.Green // Atualiza a cor da linha dentro de uma corrotina
                                        cameraControl?.cancelFocusAndMetering()
                                        withContext(Dispatchers.IO) {
                                            Thread.sleep(200)
                                        }
                                        onClosePreview()
                                    }

                                }
                            )
                        )
                    }

                cameraProvider.unbindAll()
                val camera = cameraProvider.bindToLifecycle(
                    lifecycleOwner,
                    CameraSelector.DEFAULT_BACK_CAMERA,
                    preview,
                    imageAnalysis
                )

                cameraControl = camera.cameraControl

                cameraControl?.let { control ->
                    if (enableAutoFocus) {
                        enableAutoFocus(control)
                    }

                    enableAutoZoom?.takeIf { it in 0f..1f }?.let { zoom ->
                        control.setLinearZoom(zoom)
                    }
                }

            } catch (e: Exception) {
                Timber.e("CameraPreview", "Binding failed: ${e.message}", e)
            }
        }

        cameraProviderFuture.addListener(listener, executor)

        onDispose {
            cameraProviderFuture.get().unbindAll()
        }
    }

    AndroidView(
        factory = { previewView },
        modifier = Modifier
            .fillMaxSize()
            .pointerInteropFilter { motionEvent ->
                if (motionEvent.action == MotionEvent.ACTION_DOWN) {
                    cameraControl?.let { control ->
                        val factory = SurfaceOrientedMeteringPointFactory(
                            previewView.width.toFloat(), previewView.height.toFloat()
                        )
                        val autoFocusPoint = factory.createPoint(motionEvent.x, motionEvent.y)
                        val autoFocusAction = FocusMeteringAction
                            .Builder(autoFocusPoint, FocusMeteringAction.FLAG_AF)
                            .setAutoCancelDuration(5, TimeUnit.SECONDS)
                            .build()
                        control.startFocusAndMetering(autoFocusAction)
                    }
                    true
                } else {
                    false
                }
            }
    )

    Box(contentAlignment = Alignment.TopEnd, modifier = Modifier.fillMaxSize()) {
        IconButton(onClick = { onClosePreview() }) {
            Icon(
                Icons.Filled.Close,
                contentDescription = "Fechar",
                tint = Color.White,
                modifier = Modifier.size(48.dp)
            )
        }
    }

    // Linha Vertical Indicadora
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.Center)
                .width(2.5.dp)
                .fillMaxSize(fraction = 0.8f)
                .alpha(0.4f)
                .background(lineColor) // Cor da linha depende do estado
        )
    }
}


private fun enableAutoFocus(cameraControl: CameraControl) {
    val autoFocusPoint = SurfaceOrientedMeteringPointFactory(1f, 1f).createPoint(0.5f, 0.5f)
    val autoFocusAction = FocusMeteringAction.Builder(autoFocusPoint, FocusMeteringAction.FLAG_AF)
        .setAutoCancelDuration(5, TimeUnit.SECONDS)
        .build()
    cameraControl.startFocusAndMetering(autoFocusAction)
}

class BarcodeAnalyzer(
    private val onBarcodeDetected: (barcode: String, boundingBox: Rect?) -> Unit,
    private val minimumTimeBetweenAnalysis: Long,
    private val onBarcodeFound: () -> Unit,
    zoomCallback: (Float) -> Boolean
) : ImageAnalysis.Analyzer {

    private val zoomSuggestionOptions = ZoomSuggestionOptions.Builder(zoomCallback)
        .setMaxSupportedZoomRatio(3f)
        .build()

    private val scanner by lazy {
        BarcodeScanning.getClient(
            BarcodeScannerOptions.Builder()
                .setBarcodeFormats(Barcode.FORMAT_CODE_128)
                .setZoomSuggestionOptions(zoomSuggestionOptions)
                .build()
        )
    }

    private var lastAnalyzedTimestamp = 0L
    private var lastDetectedBarcode: String? = null
    private val nfeValidator = AccessKeyValidator()

    private fun shouldAnalyzeBarcode(barcode: String): Boolean {
        return lastDetectedBarcode != barcode
    }

    @SuppressLint("UnsafeOptInUsageError")
    override fun analyze(imageProxy: ImageProxy) {
        val currentTimestamp = System.currentTimeMillis()

        if (currentTimestamp - lastAnalyzedTimestamp >= minimumTimeBetweenAnalysis) {
            lastAnalyzedTimestamp = currentTimestamp

            val rotationDegrees = imageProxy.imageInfo.rotationDegrees
            val mediaImage = imageProxy.image
            if (mediaImage != null && (rotationDegrees == 0 || rotationDegrees == 90)) {
                val image = InputImage.fromMediaImage(mediaImage, rotationDegrees)

                scanner.process(image)
                    .addOnSuccessListener { barcodes ->
                        if (barcodes.isNotEmpty()) {
                            barcodes.forEach { barcode ->
                                val barcodeValue = barcode.rawValue ?: return@forEach
                                if (shouldAnalyzeBarcode(barcodeValue)) {
                                    if (nfeValidator.isValid(barcodeValue)) {
                                        lastDetectedBarcode = barcodeValue
                                        onBarcodeDetected(barcodeValue, barcode.boundingBox)
                                        onBarcodeFound()
                                        return@addOnSuccessListener
                                    }
                                }
                            }
                        }
                    }
                    .addOnFailureListener {
                        // Log or handle the failure if needed
                    }
                    .addOnCompleteListener {
                        imageProxy.close()
                    }
            } else {
                imageProxy.close()
            }
        } else {
            imageProxy.close()
        }
    }
}
