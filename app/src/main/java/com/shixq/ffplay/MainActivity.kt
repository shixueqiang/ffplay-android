package com.shixq.ffplay

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.text.TextUtils
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import com.shixq.ffplay.sdl.SDLActivity
import java.io.File
import java.io.FileOutputStream


class MainActivity : AppCompatActivity() {
    val TAG = "MainActivity"
    val EXE_PATH = "data/data/com.shixq.ffplay/"
    lateinit var mEditText: EditText
    lateinit var mChooseFile: Button
    lateinit var mCmd: EditText
    lateinit var mExe: Button
    lateinit var mPlay: Button
    lateinit var mCmdResult: TextView
    lateinit var file: String
    val READ_REQUEST_CODE: Int = 42
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method
        mEditText = findViewById(R.id.sample_text)
        mChooseFile = findViewById(R.id.choose_file)
        mCmd = findViewById(R.id.cmd)
        mExe = findViewById(R.id.exe)
        mPlay = findViewById(R.id.to_paly)
        mCmdResult = findViewById(R.id.cmd_result)

        mChooseFile.setOnClickListener {
            val permission = ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE)
            if (permission != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 100)
            } else {
                val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
                intent.addCategory(Intent.CATEGORY_OPENABLE)
                intent.type = "*/*"
                startActivityForResult(intent, READ_REQUEST_CODE)
            }
        }

        mPlay.setOnClickListener {
            val url = mEditText.text
            if(url != null) {
                val intent = Intent(this, SDLActivity::class.java)
                if(!TextUtils.isEmpty(url)) {
                    intent.putExtra("PLAY_FILE", url.toString())
                    startActivity(intent)
                }
            }
        }

        mExe.setOnClickListener {
            val exeFile = File(EXE_PATH + "ffmpeg");
            exeFile.setExecutable(true, true);
            val exeCommand = ExeCommand()
            exeCommand.run(EXE_PATH + mCmd.text.toString(), 5000)
            Log.i(TAG, exeCommand.result)
            mCmdResult.setText(exeCommand.result)
        }

        copyBigDataToSD("ffmpeg", EXE_PATH + "ffmpeg")
        copyBigDataToSD("ffprobe", EXE_PATH + "ffprobe")
    }

    fun copyBigDataToSD(asset: String, strOutFileName: String) {
        val file = File(strOutFileName)
        if(file.exists()) {
            return
        }
        val myOutput = FileOutputStream(strOutFileName)
        val myInput = assets.open(asset)
        val buffer = ByteArray(1024)
        var length = myInput.read(buffer)
        while (length > 0) {
            myOutput.write(buffer, 0, length)
            length = myInput.read(buffer)
        }
        myOutput.flush()
        myInput.close()
        myOutput.close()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            var uri: Uri? = null
            if (data != null) {
                uri = data.getData()
                Log.i(TAG, "Uri: " + uri!!.toString())
                file = FileUtils.getPath(this, uri)!!
                mEditText.setText(file)
                mCmd.setText("ffmpeg -i " + "\"" + file + "\"")
                val cursor = getContentResolver().query(uri, null, null, null, null, null)
                try {
                    if (cursor != null && cursor.moveToFirst()) {
                        val displayName = cursor.getString(
                                cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME))
                        Log.i(TAG, "Display Name: $displayName")

                        val sizeIndex = cursor.getColumnIndex(OpenableColumns.SIZE)
                        // If the size is unknown, the value stored is null.  But since an
                        // int can't be null in Java, the behavior is implementation-specific,
                        // which is just a fancy term for "unpredictable".  So as
                        // a rule, check if it's null before assigning to an int.  This will
                        // happen often:  The storage API allows for remote files, whose
                        // size might not be locally known.
                        var size: String? = null
                        if (!cursor.isNull(sizeIndex)) {
                            // Technically the column stores an int, but cursor.getString()
                            // will do the conversion automatically.
                            size = cursor.getString(sizeIndex)
                        } else {
                            size = "Unknown"
                        }
                        Log.i(TAG, "Size: " + size!!)

                    }
                } finally {
                    if (cursor != null) {
                        cursor.close()
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100) {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "video/*"
            startActivityForResult(intent, READ_REQUEST_CODE)
        }
    }
}
