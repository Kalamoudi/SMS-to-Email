package com.example.smstoemail.Settings

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.media.AudioManager
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.util.TypedValue
import android.view.MotionEvent
import android.view.View
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.SwitchCompat
import com.example.smstoemail.R
import com.example.smstoemail.Utils
import com.example.smstoemail.sharedPrefs
import kotlinx.coroutines.CoroutineScope

object SettingsUtils {

    fun updateSettingsTheme(){

        if (sharedPrefs.getBoolean("isNightMode", true)) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    // Using animate functionality
    fun addHighlightToLayout(context: Context, relativeLayout: RelativeLayout){

        val animationSet = AnimatorInflater.loadAnimator(context, R.animator.background_color_change) as AnimatorSet

        animationSet.childAnimations.forEach { animator ->
            if (animator is ObjectAnimator) {
                animator.duration = 125 // Change this value to adjust the speed
            }
        }



        animationSet.setTarget(relativeLayout)
        animationSet.start()


        //    animationSet.start()


    }


    // Adds highlight and process given code within it
    @SuppressLint("ClickableViewAccessibility")
    fun processClickWithHighlight(context: Context, relativeLayout: RelativeLayout, receviedCode: () -> Unit){
        val audioManager = relativeLayout.context.getSystemService(AudioManager::class.java)
        val vibrator = relativeLayout.context.getSystemService(Vibrator::class.java)

        relativeLayout.setOnTouchListener{ view, motionEvent->
            when(motionEvent.action) {
                MotionEvent.ACTION_DOWN -> {
                    //       checkbox2Layout.setBackgroundResource(R.drawable.background_clicked)

                   val backgroundColor = Utils.getColorFromAttribute(context, "clickBackgroundClicked")

                    relativeLayout.setBackgroundColor(backgroundColor)
                    relativeLayout.elevation = 8f.dpToPx(context)
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    // Stop the highlight animation when releasing

                    val backgroundColor = Utils.getColorFromAttribute(context, "clickBackgroundNormal")

                    relativeLayout.postDelayed({
                        relativeLayout.setBackgroundColor(backgroundColor)
                        relativeLayout.elevation = 0f.dpToPx(context)

                    },125)


                    // Perform the click action if the touch was released within the layout bounds
                    if (motionEvent.x >= 0 && motionEvent.x <= relativeLayout.width &&
                        motionEvent.y >= 0 && motionEvent.y <= relativeLayout.height
                    ) {
                        // Perform the click action here
                        playBuiltInClickSound(audioManager)
                      //  vibrateDevice(vibrator)
                        receviedCode()
                    }
                }
            }
            true
        }

    }


    private fun playBuiltInClickSound(audioManager: AudioManager) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            audioManager.playSoundEffect(AudioManager.FX_KEY_CLICK, 1.0f)
        } else {
            // For older Android versions, you can use the deprecated method
            audioManager.playSoundEffect(AudioManager.FX_KEYPRESS_STANDARD)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun vibrateDevice(vibrator: Vibrator?) {
        vibrator?.let {
            val vibrationEffect = VibrationEffect.createOneShot(50, VibrationEffect.DEFAULT_AMPLITUDE)
            it.vibrate(vibrationEffect)
        }
    }
    fun Float.dpToPx(context: Context): Float {
        return this * context.resources.displayMetrics.density
    }



}