package com.example.myapplication

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import com.example.myapplication.ui.theme.MyApplicationTheme
import androidx.compose.material3.Button
import androidx.compose.foundation.layout.wrapContentWidth
import android.widget.Toast
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.Manifest

class MainActivity : ComponentActivity() {
    private lateinit var locationManager: LocationManager
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.SpaceEvenly,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Greeting("Android")
                        PushButton()
                    }
                }
            }
        }

        // 初始化 LocationManager
        locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager

        // 檢查定位權限
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED
        ) {
            // 如果沒有權限，則請求權限
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                LOCATION_PERMISSION_REQUEST_CODE
            )
        } else {
            // 如果已經有權限，啟動位置更新
            startLocationUpdates()
        }
    }

    private fun startLocationUpdates() {
        // define LocationListener
        val locationListener = object : LocationListener {
            override fun onLocationChanged(location: Location) {
                // 當位置變更時執行的操作
                val latitude = location.latitude
                val longitude = location.longitude
                // 處理位置資訊
            }

            override fun onStatusChanged(provider: String?, status: Int, extras: Bundle?) {
                // 當位置提供者狀態變更時執行的操作
            }

            override fun onProviderEnabled(provider: String) {
                // 當位置提供者啟用時執行的操作
            }

            override fun onProviderDisabled(provider: String) {
                // 當位置提供者停用時執行的操作
            }
        }

        //位置更新
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER,
            MIN_TIME_BETWEEN_UPDATES,
            MIN_DISTANCE_CHANGE_FOR_UPDATES.toFloat(),
            locationListener
        )
    }

    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        const val MIN_TIME_BETWEEN_UPDATES: Long = 1000 // 更新間隔時間（ms）
        const val MIN_DISTANCE_CHANGE_FOR_UPDATES: Long = 10 // 最小距離變化（公尺）
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MyApplicationTheme {
        Greeting("Android")
    }
}

@Composable
fun PushButton() {
    val context = LocalContext.current
    Button(
        onClick = { /* Do something */
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER)
            val latitude = location?.latitude
            val longitude = location?.longitude
            val message = "Latitude: $latitude, Longitude: $longitude"
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        },
        modifier = Modifier.wrapContentWidth()
    ) {
        Text("My location")
    }
}