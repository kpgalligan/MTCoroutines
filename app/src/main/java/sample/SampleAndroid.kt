package sample

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Looper
import android.widget.TextView
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.newSingleThreadContext

actual class Sample {
    actual fun checkMe() = 44
}

actual object Platform {
    actual val name: String = "Android"
}

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Sample().checkMe()
        setContentView(R.layout.activity_main)
        findViewById<TextView>(R.id.main_text).text = hello()
    }
}

actual val myBackgroundDispatcher: CoroutineDispatcher
    get() = Dispatchers.IO

actual val stateDispatcher: CoroutineDispatcher = newSingleThreadContext("state dispatcher")
