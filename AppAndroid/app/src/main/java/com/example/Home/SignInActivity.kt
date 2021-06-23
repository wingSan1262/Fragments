package com.example.Home

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.LinearLayout
import android.widget.EditText
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import android.os.Handler
import android.os.Message
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import java.io.DataOutputStream
import java.nio.charset.StandardCharsets


const val SIGN_IN_UPDATE = 99

class SignInActivity : AppCompatActivity() {

    var USER_ACCOUNT_INFO_PREF : String? = "user-account-info"
    var USER_NAME_PREF_KEY : String? = "user-account-name"

    var userName = ""
    var password = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.new_signing_activity)

        val signInButton : LinearLayout = findViewById(R.id.do_sign_in)
        val textEditUserName : EditText = findViewById(R.id.username)
        val signInNotify  : ConstraintLayout = findViewById(R.id.sign_in_notify)
        val textEditPassword : EditText = findViewById(R.id.password_field)

        val mHandler = object : Handler(this.mainLooper) {
            override fun handleMessage(msg: Message) {
                if (msg != null) {
                    when (msg.what) {
                        SIGN_IN_UPDATE -> {updateUiLogIn(msg.obj as String)
                            // TODO update to check the login information if the email is and password is OK
                            // TODO then safe it to sharedPreference
                            signInNotify.visibility = View.GONE
                            val intent = Intent (this@SignInActivity, HomeActivity::class.java)
                            startActivity(intent)
                        }


                    }
                }
            }
        }

        signInButton.setOnClickListener {
            signInNotify.visibility = View.VISIBLE
            GetObjectsFromServer.getJSON(getString(R.string.URL_WEB_SERVICE),mHandler, textEditUserName.text.toString(), textEditUserName.text.toString())

//            val sharedPref = this.getSharedPreferences(
//                USER_ACCOUNT_INFO_PREF, Context.MODE_PRIVATE)
//            var editor  = sharedPref.edit()
//            editor.putString(USER_NAME_PREF_KEY, textEditUserName.text.toString())
//            editor.apply()
        }

    }

    object GetObjectsFromServer {

        //this method is actually fetching the json string
        fun getJSON(urlWebService: String, mHandler: Handler, userName : String, password : String) {
            /*
         * As fetching the json string is a network operation
         * And we cannot perform a network operation in main thread
         * so we need an AsyncTask
         * The constrains defined here are
         * Void -> We are not passing anything
         * Void -> Nothing at progress update as well
         * String -> After completion it should return a string and it will be the json string
         * */


            val returnJson = arrayOf<String>(null.toString())

            class GetJSON : AsyncTask<Void, Void, String>() {

                //this method will be called before execution
                //you can display a progress bar or something
                //so that user can understand that he should wait
                //as network operation may take some time
                override fun onPreExecute() {
                    super.onPreExecute()
                }

                //this method will be called after execution
                //so here we are displaying a toast with the json string
                override fun onPostExecute(s: String) {
                    super.onPostExecute(s)
                    returnJson[0] = s
                    val message = mHandler.obtainMessage(SIGN_IN_UPDATE, s)
                    mHandler.sendMessage(message)
                    //                Toast.makeText(getApplicationContext(), s, Toast.LENGTH_SHORT).show();
                }

                //in this method we are fetching the json string
                override fun doInBackground(vararg voids: Void): String? {


                    try {
                        //creating a URL
                        val url = URL(urlWebService)

                        //Opening the URL using HttpURLConnection
                        val conn = url.openConnection() as HttpURLConnection

                        val urlParameters : String = "user_name=$userName&password=$password"
                        val postData = urlParameters.toByteArray(StandardCharsets.UTF_8)
                        val postDataLength = postData.size
                        conn.doOutput = true
                        conn.instanceFollowRedirects = false
                        conn.requestMethod = "POST"
                        conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded")
                        conn.setRequestProperty("charset", "utf-8")
                        conn.setRequestProperty("Content-Length", Integer.toString(postDataLength))
                        conn.useCaches = false
                        DataOutputStream(conn.outputStream).use { wr -> wr.write(postData) }

                        //StringBuilder object to read the string from the service
                        val sb = StringBuilder()

                        //We will use a buffered reader to read the string from service
                        val bufferedReader = BufferedReader(InputStreamReader(conn.inputStream))

                        //read values from each line
                        bufferedReader.forEachLine {
                            sb.append(bufferedReader.readLine() + "\n")
                        }

                        //finally returning the read string
                        return sb.toString().trim { it <= ' ' }
                    } catch (e: Exception) {
                        return null
                    }

                }
            }

            //creating asynctask object and executing it
            val getJSON = GetJSON()
            getJSON.execute()
        }


    }

    // TODO not used currently
    class loggingIn (passedContext : Context, passedHandler: Handler): Thread() {
        var mContext: Context
        var jsonString : String = ""
        var mHandler : Handler

        init {
            mContext = passedContext
            mHandler = passedHandler
        }

        public override fun run() {
            println("${Thread.currentThread()} has run.")

            try {
                //creating a URL
                val url = URL(mContext.getString(R.string.URL_WEB_SERVICE))

                //Opening the URL using HttpURLConnection
                val conn = url.openConnection() as HttpURLConnection


                //StringBuilder object to read the string from the service
                val sb = StringBuilder()

                //We will use a buffered reader to read the string from service
                val bufferedReader = BufferedReader(InputStreamReader(conn.getInputStream()))

                //A simple string to read values from each line
                var json: String

                //reading until we don't find null
                json = bufferedReader.readLine()
                while ( json != null) {

                    //appending it to string builder
                    sb.append(json + "\n")
                    json = bufferedReader.readLine()
                }

                //finally returning the read string
                jsonString = sb.toString()
            } catch (e: Exception) {
                Throwable().printStackTrace()
            }

            val message = mHandler.obtainMessage(SIGN_IN_UPDATE, jsonString)
            mHandler.sendMessage(message)

        }
    }

    public fun updateUiLogIn (jsonString : String){
        Log.i("SignInPostReques", jsonString)
    }
}
