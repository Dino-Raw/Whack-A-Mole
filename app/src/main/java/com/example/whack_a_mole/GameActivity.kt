package com.example.whack_a_mole

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.os.Bundle
import android.os.Handler
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.example.whack_a_mole.databinding.ActivityGameBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import kotlin.random.Random


class GameActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameBinding
    private lateinit var runnableTime: Runnable
    private lateinit var runnableMole: Runnable
    private lateinit var moleArray: ArrayList<ImageView>
    private lateinit var animationArray: ArrayList<AnimationDrawable>

    private var currentTime = 30
    private var score = 0
    private val mHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameBinding.inflate(layoutInflater)

        moleArray = arrayListOf(
            binding.mole1,
            binding.mole2,
            binding.mole3,
            binding.mole4,
            binding.mole5,
            binding.mole6,
            binding.mole7,
            binding.mole8,
            binding.mole9)

        animationArray = arrayListOf()

        moleArray.forEach { mole ->
            mole.setBackgroundResource(R.drawable.animation_mole)
            animationArray.add(mole.background as AnimationDrawable)
            mole.isEnabled = false

            mole.setOnClickListener {
                if(mole.isClickable) {
                    binding.scoreTxt.text = (++score).toString()
                    mole.isEnabled = false
                    mole.setImageResource(R.drawable.boom_mole5_1)
                }
            }
        }

        startGame()
        setContentView(binding.root)
    }

    private fun startGame() {
        var prevRand = 0
        var nextRand = 0

        runnableTime = Runnable {
            mHandler.postDelayed(runnableTime, 1000)
            binding.timeTxt.text = (currentTime--).toString()
            println(currentTime)
            if (currentTime < 0) finishGame()
        }

        runnableMole = Runnable {
            mHandler.postDelayed(runnableMole, 500)

            moleArray[prevRand].isEnabled = false
            prevRand = nextRand

            do nextRand = Random.nextInt(0, 9)
            while(prevRand == nextRand)

            moleArray[nextRand].isEnabled = true
            moleArray[nextRand].setImageResource(0)
            moleArray[nextRand].setBackgroundResource(R.drawable.animation_mole)
            animationArray[prevRand].stop()
            animationArray[nextRand].start()
        }

        mHandler.postDelayed(runnableTime, 0)
        mHandler.postDelayed(runnableMole, 0)
    }

    private fun finishGame() {
        mHandler.removeCallbacks(runnableTime)
        mHandler.removeCallbacks(runnableMole)

        val intent = Intent(this, ResultActivity::class.java)
        intent.putExtra("score", score)
        startActivity(intent)
    }

    private fun resumeGame() {
        mHandler.postDelayed(runnableMole, 500)
        mHandler.postDelayed(runnableTime, 1000)
    }

    private fun pauseGame(){
        mHandler.removeCallbacks(runnableTime)
        mHandler.removeCallbacks(runnableMole)
    }

    override fun onBackPressed() {
        pauseGame()

        val builder = MaterialAlertDialogBuilder(this)
        builder
            .setTitle("Finish the game?")
            .setPositiveButton("YES") {_, _ -> finishGame() }
            .setNegativeButton("NO") {dialog, _->
                dialog.dismiss()
                resumeGame()
            }

        val customDialog = builder.create()
        customDialog.show()
    }

}