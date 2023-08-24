package com.kuaapps.smstoemail.GoogleSignIn

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import androidx.core.content.ContextCompat
import com.kuaapps.smstoemail.R
import com.kuaapps.smstoemail.sharedPrefs
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import java.lang.System.currentTimeMillis
import java.util.Random

object GoogleSignInUtils {

    fun generateGoogleAccountIcon(account: GoogleSignInAccount?, backgroundColor: Int): Bitmap {
        val iconSize = 128 // Adjust the size as needed
        val icon = Bitmap.createBitmap(iconSize, iconSize, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(icon)


       // val backgroundColor = Color.parseColor("#FF9800") // Orange color or any other color you prefer

        val textColor = Color.WHITE
        // Draw background
        val backgroundPaint = Paint()
        backgroundPaint.color = backgroundColor
        canvas.drawCircle(iconSize / 2f, iconSize / 2f, iconSize / 2f, backgroundPaint)

        // Draw initial letter
        val textPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        textPaint.color = textColor
        textPaint.textSize = iconSize * 0.4f
        textPaint.textAlign = Paint.Align.CENTER

        val x = iconSize / 2f
        val y = iconSize / 2f - (textPaint.descent() + textPaint.ascent()) / 2
        val displayName = account?.displayName ?: ""
        val initials = displayName.split(" ").mapNotNull { it.firstOrNull()?.toUpperCase() }.joinToString("")
        canvas.drawText(initials, x, y, textPaint)

        return icon
    }


    fun generateColorArray(context: Context, account: GoogleSignInAccount?): Int{

        val colorArray =  arrayOf(
            R.color.purpleIcon,
            R.color.brownIcon,
         //   R.color.redIcon,
            R.color.yellowIcon,
            R.color.greenIcon,
            R.color.violetIcon,
            R.color.blueIcon,
            R.color.greyIcon,
            R.color.orangeIcon,
         //   R.color.pinkIcon

        )

        val random = Random(currentTimeMillis())

        var backgroundColor = ContextCompat.getColor(context, colorArray[random.nextInt(colorArray.size)])
        if(account != null){
            backgroundColor = sharedPrefs.getInt("accountIconColor", 0)
        }
        else{
            sharedPrefs.edit().putInt("accountIconColor", backgroundColor).apply()
        }

        return backgroundColor

    }



}