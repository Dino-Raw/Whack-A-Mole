package com.example.whack_a_mole

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.whack_a_mole.databinding.ActivityMainBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.lang.NullPointerException
import java.lang.System.exit
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)

        binding.playBtn.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }

        binding.rulesBtn.setOnClickListener {
            MaterialAlertDialogBuilder(this)
                .setTitle(R.string.rules)
                .setMessage(R.string.rules_data)
                .create()
                .show()
        }

        binding.resetScoreBtn.setOnClickListener {
            setRecordScore(0)
            binding.recordScoreTxt.text = getRecordScore().toString()
        }

        binding.recordScoreTxt.text = getRecordScore().toString()

        setContentView(binding.root)
    }

    private fun getRecordScore() : Int
    {
        return try {
            this.getSharedPreferences("score", Context.MODE_PRIVATE)!!.getInt("record", 0)
        }
        catch(e: NullPointerException) {
            0
        }
    }

    override fun onBackPressed() {
        val builder = MaterialAlertDialogBuilder(this)
        builder
            .setTitle("Quit the game?")
            .setPositiveButton("YES") {_, _ -> this.finishAffinity() }
            .setNegativeButton("NO") {dialog, _-> dialog.dismiss()}

        val customDialog = builder.create()
        customDialog.show()
    }

    private fun setRecordScore(score: Int) {
        this.getSharedPreferences("score", Context.MODE_PRIVATE)?.edit()
            ?.putInt("record", score)
            ?.apply()
    }

}