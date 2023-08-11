package com.example.smstoemail.GoogleSignIn

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint

object GoogleSignInUtils {

    fun generateGoogleAccountIcon(displayName: String): Bitmap {
        val iconSize = 128 // Adjust the size as needed
        val icon = Bitmap.createBitmap(iconSize, iconSize, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(icon)

        val backgroundColor = Color.parseColor("#FF9800") // Orange color or any other color you prefer
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
        val initials = displayName.split(" ").mapNotNull { it.firstOrNull()?.toUpperCase() }.joinToString("")
        canvas.drawText(initials, x, y, textPaint)

        return icon
    }

}