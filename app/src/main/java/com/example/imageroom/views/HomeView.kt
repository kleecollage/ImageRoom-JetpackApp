package com.example.imageroom.views

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.imageroom.viewModel.ImagesViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeView(navController: NavController, viewModel: ImagesViewModel) {
    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(title = { Text(text = "Image Room") })
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { navController.navigate("AddPhotoView") }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "")
            }
        }
    ) {
        ContentHomeView(it, viewModel)
    }
}

@Composable
fun ContentHomeView(pad: PaddingValues, viewModel: ImagesViewModel) {
    val images by viewModel.imageList.collectAsState()
    Column(modifier = Modifier.padding(pad)) {
        LazyRow {
            items(images) { item ->
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    PhotoView(imagePath = item.ruta)
                    Button(onClick = { viewModel.deleteImage(item) }, colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Red
                    )) {
                        Text(text = "Eliminar")
                    }
                }
            }
        }
    }
}

@Composable
fun PhotoView(imagePath: String) {
    val bitmap = remember {
        val file = File(imagePath)
        if (file.exists()) {
            decodeSampledBitmapFromFile(file.absolutePath, 800, 600)
        } else {
            null
        }
    }
    if (bitmap != null) {
    Image(
        bitmap = bitmap.asImageBitmap(),
        contentDescription = "",
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .fillMaxHeight(0.7f)
            .padding(15.dp)
            .shadow(10.dp, shape = RoundedCornerShape(16.dp))
            .clip(RoundedCornerShape(16.dp)),
        contentScale = ContentScale.Fit
    )} else {
        Text(
            text = "Imagen no disponible",
            color = Color.Red,
            modifier = Modifier.padding(15.dp)
        )
    }
}

fun decodeSampledBitmapFromFile(filePath: String, reqWidth: Int, reqHeight: Int): Bitmap {
    // First decode with inJustDecodeBounds=true to check dimensions
    val options = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
    }
    BitmapFactory.decodeFile(filePath, options)

    // Calculate inSampleSize
    options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight)

    // Decode bitmap with inSampleSize set
    options.inJustDecodeBounds = false
    return BitmapFactory.decodeFile(filePath, options)
}

fun calculateInSampleSize(options: BitmapFactory.Options, reqWidth: Int, reqHeight: Int): Int {
    val (height: Int, width: Int) = options.run { outHeight to outWidth }
    var inSampleSize = 1

    if (height > reqHeight || width > reqWidth) {
        val halfHeight: Int = height / 2
        val halfWidth: Int = width / 2

        // Calculate the largest inSampleSize value that is a power of 2 and keeps both
        // height and width larger than the requested height and width.
        while (halfHeight / inSampleSize >= reqHeight && halfWidth / inSampleSize >= reqWidth) {
            inSampleSize *= 2
        }
    }

    return inSampleSize
}















