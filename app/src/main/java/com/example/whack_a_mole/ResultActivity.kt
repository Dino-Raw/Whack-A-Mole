package com.example.whack_a_mole

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.whack_a_mole.databinding.ActivityResultBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.lang.NullPointerException
import kotlin.system.exitProcess

class ResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityResultBinding.inflate(layoutInflater)

        val currentScore = getCurrentScore()
        var recordScore = getRecordScore()

        if (currentScore > recordScore)
        {
            setRecordScore(currentScore)
            recordScore = currentScore
        }

        setScoreTxt(currentScore, recordScore)

        binding.playAgainBtn.setOnClickListener {
            startActivity(Intent(this, GameActivity::class.java))
        }

        binding.menuBtn.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }

        setContentView(binding.root)
    }

    private fun setScoreTxt(currentScore: Int, recordScore: Int) {
        binding.currentScoreTxt.text = currentScore.toString()
        binding.recordScoreTxt.text = recordScore.toString()
    }

    private fun getRecordScore() : Int {
        return try {
             this.getSharedPreferences("score", Context.MODE_PRIVATE)!!.getInt("record", 0)
        }
        catch(e: NullPointerException) {
            0
        }
    }

    private fun getCurrentScore() : Int {
        return try {
            intent.extras!!.getInt("score")
        }
        catch (e: NullPointerException) {
            0
        }
    }

    private fun setRecordScore(score: Int) {
         this.getSharedPreferences("score", Context.MODE_PRIVATE)?.edit()
             ?.putInt("record", score)
             ?.apply()
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
}