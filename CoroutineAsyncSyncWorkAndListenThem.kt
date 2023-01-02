import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.route4me.notesapp.R
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class MainActivity : AppCompatActivity() {

    private val state = MutableStateFlow("empty")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        observeStateUpdates()

    }

    private fun observeStateUpdates() {
        runSync()
        runAsync()

        lifecycleScope.launch {
            // observe state updates
            state.collect {
                // subscribe to flow updates and print state.value to logcat.
                if (it != "empty")
                    Log.d("Tag", it)

            }
        }
    }

    private fun runSync() {
        println("runSync method.")
        //  launch 1000 coroutines. Invoke doWork(index/number of coroutine) one after another. Example 1, 2, 3, 4, 5, etc.

        lifecycleScope.launch {

            // launch 1000 coroutines with one coroutine and this is associated to synch
            repeat(1000) {
                withContext(IO) {
                    doWork("$it")
                }
            }

        }


    }

    private fun runAsync() {
        println("runAsync method.")

        /**
         * launch 1000 coroutines with the same dispatcher "IO" and
         * then call the function doWork with "index of coroutine parameter" in async way. Example 1, 2, 5, 3, 4, 8, etc.
         */
        lifecycleScope.launch {

            repeat(1000) {

                CoroutineScope(IO).launch {

                    doWork("$it")

                }

            }
        }
    }

    private suspend fun doWork(name: String) {
        delay(500)
        state.update { "$name completed." }
    }
}
