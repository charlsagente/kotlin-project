package kotlinforandroid.book.numberguess

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.android.R
import kotlinx.android.synthetic.main.activity_main.*
import android.util.Log

class MainActivity : AppCompatActivity() {
    var started = false
    var number = 0
    var tries = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fetchSavedInstanceData(savedInstanceState)
        doGuess.setEnabled(started)

    }

    override fun onSaveInstanceState(outState: Bundle?) {
        super.onSaveInstanceState(outState)
        putInstanceData(outState)
    }

    fun start(v: View) {
        log("Game started")
        num.setText("")
        started = true
        doGuess.setEnabled(true)
        status.text = getString(R.string.guess_hint, 1, 7)
        number = 1 + Math.floor(Math.random()*7).toInt()
        tries = 0
    }

    fun guess(v:View) {
        if(num.text.toString() == "") return
        tries++
        log("Guessed ${num.text} (tries:${tries})")
        val g = num.text.toString().toInt()
        if(g < number) {
            status.setText(R.string.status_too_low)
            num.setText("")
        } else if(g > number){
            status.setText(R.string.status_too_high)
            num.setText("")
        } else {
            status.text = getString(R.string.status_hit,
                tries)
            started = false
            doGuess.setEnabled(false)
        }
    }

    //////////////////////

    private fun putInstanceData(outState: Bundle?) {
        if (outState != null) with(outState) {
            putBoolean("started",  started)
            putInt("number", number)
            putInt("tries", tries)
            putString("statusMsg", status.text.toString())
            putStringArrayList("logs",
                ArrayList(console.text.split("\n")))
        }
    }

    private fun fetchSavedInstanceData(
        savedInstanceState: Bundle?) {
        if (savedInstanceState != null)
            with(savedInstanceState) {
                started = getBoolean("started")
                number = getInt("number")
                tries = getInt("tries")
                status.text = getString("statusMsg")
                console.text = getStringArrayList("logs")!!.
                    joinToString("\n")
            }
    }

    private fun log(msg:String) {
        Log.d("LOG", msg)
        console.log(msg)
    }



}
