package kotlinforandroid.book.numberguess

import androidx.appcompat.app.AppCompatActivity
import android.content.Context
import android.util.AttributeSet
import android.widget.ScrollView
import android.widget.TextView
import android.os.Bundle
import com.example.android.R
import kotlinx.android.synthetic.main.activity_main.* // user interface-related classes from layout file
import android.util.Log
import android.view.View
import kotlinforandroid.book.numberguess.Constants


class MainActivity : AppCompatActivity() {
    var started = false // Kotlin automatically infers the type
    var number = 0
    var tries = 0

    override fun onCreate(savedInstanceState: Bundle?) { // Gets called when the user interface is created
        // The Bundle param might or might not contain saved data from a restart of the UI
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        fetchSavedInstanceData(savedInstanceState)
        doGuess.setEnabled(started)

    }

    override fun onSaveInstanceState(outState: Bundle?) {  // Gets called when the activity is suspended temporarily
        // Whe use this fn to save the state of the activity
        super.onSaveInstanceState(outState)
        putInstanceData(outState)
    }

    fun start(v: View) { // You can find this in the layout and get invoked when user clicks a btn
        log("Game started")
        num.setText("")
        started = true
        doGuess.setEnabled(true)
        status.text = getString(R.string.guess_hint, Constants.LOWER_BOUND, Constants.UPPER_BOUND)
        val span = Constants.UPPER_BOUND - Constants.LOWER_BOUND + 1
        number = Constants.LOWER_BOUND + Math.floor(Math.random()*span).toInt()
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

    ////////////////////// To separate public from private fns

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
        print(msg)
    }
}

class Console(ctx:Context, aset:AttributeSet? = null) // Custom view object, can be placed in any layout
    : ScrollView(ctx, aset) {
    var tv = TextView(ctx)
    var text:String
        get() =  tv.text.toString()
        set(value) { tv.setText(value) }
    init {
        setBackgroundColor(0x40FFFF00)
        addView(tv)
    }

    fun log(msg:String) {
        val l = tv.text.let {
            if(it == "") listOf() else it.split("\n")
        }.takeLast(100) + msg
        tv.text = l.joinToString("\n")
        post(object : Runnable {
            override fun run() {
                fullScroll(ScrollView.FOCUS_DOWN)
            }
        })
    }
}