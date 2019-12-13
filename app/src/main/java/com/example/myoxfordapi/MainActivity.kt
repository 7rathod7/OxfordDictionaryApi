package com.example.myoxfordapi

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.github.kittinunf.fuel.Fuel
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textview.MaterialTextView
import org.json.JSONArray
import org.json.JSONObject


class MainActivity : AppCompatActivity() {

    var base_url: String = "https://od-api.oxforddictionaries.com/api/v2"
    var search_word: TextInputEditText? = null
    var search_button: MaterialButton? = null
    var search_word_def: MaterialTextView? = null
    var language: String? = "en-gb"
    var fields: String? = "pronunciations"
    var strictMatch: String? = "false"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        search_word = findViewById<TextInputEditText>(R.id.search_word) as TextInputEditText
        search_button = findViewById<MaterialButton>(R.id.search_button) as MaterialButton
        search_word_def = findViewById<MaterialTextView>(R.id.search_word_def) as MaterialTextView

        search_button?.setOnClickListener {
            Log.e("url", base_url + "/entries/" + language + "/" + search_word?.text)
            Fuel.get(base_url + "/entries/" + language + "/" + search_word?.text)
                .header("Accept", "application/json")
                .header("app_id", "<your app id>")
                .header("app_key", "<your app key>")
                .response { request, response, result ->
                    if (response.responseMessage == "OK") {
                        var res_json: JSONObject = JSONObject(String(response.data))
                        var res_arr: JSONArray = res_json.getJSONArray("results")
                        var lEntries: JSONObject = res_arr.getJSONObject(0)
                        var laAraay: JSONArray = lEntries.getJSONArray("lexicalEntries")
                        var entries: JSONObject = laAraay.getJSONObject(0)
                        var e: JSONArray = entries.getJSONArray("entries")
                        var jsonObj: JSONObject = e.getJSONObject(0)
                        var senseArray: JSONArray = jsonObj.getJSONArray("senses")
                        var d: JSONObject = senseArray.getJSONObject(0)
                        var de: JSONArray = d.getJSONArray("definitions")
                        search_word_def?.text = de[0].toString()
                        Log.e("data", de.toString())
                    } else {
                        Toast.makeText(applicationContext, "Request failed", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }


}
