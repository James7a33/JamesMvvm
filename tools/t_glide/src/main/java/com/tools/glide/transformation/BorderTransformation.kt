package com.tools.glide.transformation

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import androidx.annotation.ColorInt
import com.bumptech.glide.load.Key
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation
import java.security.MessageDigest

/**
 * @Author: james
 * @Date: 2024/4/16 15:32
 * @Description:边框
 */
class BorderTransformation(borderWidth: Float, @ColorInt borderColor: Int) : BitmapTransformation() {

    private val ID = javaClass.name
    private val mBorderPaint: Paint = Paint()
    private val mBorderWidth: Float = Resources.getSystem().displayMetrics.density * borderWidth

    override fun transform(
        pool: BitmapPool, toTransform: Bitmap, outWidth: Int, outHeight: Int
    ): Bitmap {
        val width = toTransform.width
        val height = toTransform.height
        val bitmap = pool[width, height, Bitmap.Config.ARGB_8888]
        val canvas = Canvas(bitmap)
        val paint = Paint()

        //绘制原图像
        canvas.drawBitmap(toTransform, null, Rect(0, 0, width, height), paint)

        //描绘边框
        paint.isAntiAlias = true
        canvas.drawRect(
            0 + mBorderWidth / 2,
            0 + mBorderWidth / 2,
            width - mBorderWidth / 2,
            height - mBorderWidth / 2,
            mBorderPaint
        )
        return bitmap
    }

    override fun updateDiskCacheKey(messageDigest: MessageDigest) {
        messageDigest.update((ID + mBorderWidth * 10).toByteArray(Key.CHARSET))
    }

    init {
        mBorderPaint.isDither = true
        mBorderPaint.isAntiAlias = true
        mBorderPaint.color = borderColor
        mBorderPaint.style = Paint.Style.STROKE
        mBorderPaint.strokeWidth = mBorderWidth
    }
}