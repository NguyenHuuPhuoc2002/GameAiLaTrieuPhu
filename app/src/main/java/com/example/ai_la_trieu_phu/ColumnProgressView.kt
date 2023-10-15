package com.example.ai_la_trieu_phu

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class ColumnProgressView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private var progress: Int = 0
    private val progressPaint = Paint()

    init {
        progressPaint.color = Color.GREEN // Màu của cột tiến độ
        progressPaint.style = Paint.Style.FILL
    }

    fun setProgress(progress: Int) {
        this.progress = progress
        invalidate() // Yêu cầu vẽ lại Custom View khi tiến độ thay đổi
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas?.let {
            val viewWidth = width
            val viewHeight = height
            val progressHeight = viewHeight * progress / 100
            canvas.drawRect(0f, viewHeight.toFloat() - progressHeight, viewWidth.toFloat(), viewHeight.toFloat(), progressPaint)
        }
    }
}