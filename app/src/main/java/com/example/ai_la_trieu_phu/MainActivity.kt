package com.example.ai_la_trieu_phu

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.content.Intent
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
//lắng nghe sự kiện khi việc phát nhạc của MediaPlayer kết thúc.
class MainActivity : AppCompatActivity(), MediaPlayer.OnCompletionListener {
    @SuppressLint("MissingInflatedId")
    private var mediaPlayer: MediaPlayer? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main2)
        findViewById<Button>(R.id.btnstart).setOnClickListener {
            val intent = Intent(this, Manhinh2::class.java)
////            var editTextValue = findViewById<EditText>(R.id.edtuser).text.toString()
////            if (editTextValue.isEmpty()) {
////                Toast.makeText(this, "Please enter a value", Toast.LENGTH_SHORT).show()
////                return@setOnClickListener
////            }
//            intent.putExtra("EDIT_TEXT_VALUE", editTextValue)
            startActivity(intent)
            mediaPlayer?.pause()
        }
        mediaPlayer = MediaPlayer.create(this, R.raw.nhac_nen)
        mediaPlayer?.start()
        // Phát âm thanh khi cần
        mediaPlayer?.setOnCompletionListener(this)
    }
    override fun onCompletion(mp: MediaPlayer?) {
        // Khi nhạc kết thúc, bắt đầu phát lại từ đầu
        mediaPlayer?.seekTo(0)
        mediaPlayer?.start()
    }

    override fun onDestroy() {
        super.onDestroy()
        // Giải phóng tài nguyên khi không cần sử dụng nữa
        mediaPlayer?.release()
        mediaPlayer = null
    }
}