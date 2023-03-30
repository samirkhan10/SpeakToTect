package com.example.speaktotextyt


import android.content.ClipData
import android.content.ClipboardManager
import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import android.widget.*
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity(), AdapterView.OnItemSelectedListener {

    var etText: EditText? = null
    var ivMic: ImageView? = null
    var ivCopy: ImageView? = null
    var spLangs: Spinner? = null
    var lcode = "en-US"

    // Languages included
    var languages = arrayOf<String?>(
        "English", "Tamil", "Hindi", "Spanish", "French",
        "Arabic", "Chinese", "Japanese", "German"
    )

    // Language codes
    var lCodes = arrayOf(
        "en-US", "ta-IN", "hi-IN", "es-CL", "fr-FR",
        "ar-SA", "zh-TW", "jp-JP", "de-DE"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // initialize views
        etText = findViewById(R.id.etSpeech)
        ivMic = findViewById(R.id.ivSpeak)
        ivCopy = findViewById(R.id.ivCopy)
        spLangs = findViewById(R.id.spLang)

        // set onSelectedItemListener for the spinner
        spLangs?.setOnItemSelectedListener(this)

        // setting array adapter for spinner
        val adapter: ArrayAdapter<*> = ArrayAdapter<Any?>(
            this,
            androidx.appcompat.R.layout.support_simple_spinner_dropdown_item,
            languages
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spLangs?.setAdapter(adapter)

        // on click listener for mic icon
        ivMic?.setOnClickListener(View.OnClickListener { // creating intent using RecognizerIntent to convert speech to text
            val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
            intent.putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, lcode)
            intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Speak now!")
            // starting intent for result
            activityResultLauncher.launch(intent)
        })

        // on click listener to copy the speech
        ivCopy?.setOnClickListener(View.OnClickListener { // code to copy to clipboard
            val clipboardManager = getSystemService(CLIPBOARD_SERVICE) as ClipboardManager
            clipboardManager.setPrimaryClip(
                ClipData.newPlainText(
                    "label",
                    etText?.getText().toString().trim { it <= ' ' })
            )
            Toast.makeText(this@MainActivity, "Copied!", Toast.LENGTH_SHORT).show()
        })
    }

    // activity result launcher to start intent
    var activityResultLauncher = registerForActivityResult<Intent, ActivityResult>(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // if result is not empty
        if (result.resultCode == RESULT_OK && result.data != null) {
            // get data and append it to editText
            val d = result.data!!.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            etText!!.setText(etText!!.text.toString() + " " + d!![0])
        }
    }

    override fun onItemSelected(adapterView: AdapterView<*>?, view: View, i: Int, l: Long) {
        // setting lcode corresponding
        // to the language selected
        lcode = lCodes[i]
    }

    override fun onNothingSelected(adapterView: AdapterView<*>?) {
        // automatically generated method
        // for implementing onItemSelectedListener
    }
}