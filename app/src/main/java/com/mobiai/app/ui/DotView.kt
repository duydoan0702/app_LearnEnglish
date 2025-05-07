package com.mobiai.app.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View

class DotView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val defaultPaint = Paint().apply {
        color = Color.parseColor("#dddddd")
        strokeWidth = 1f
        style = Paint.Style.FILL
    }
    private val selectedPaint = Paint().apply {
        color = Color.BLUE
        strokeWidth = 1f
        style = Paint.Style.FILL
    }

    private var dotSize = 8
    private var dotIndex = 4

    private var start = 0f

    fun init(size: Int, dot: Int) {
        this.dotSize = size
        this.dotIndex = dot
        listRect.clear()
        start = 0f
        invalidate()
    }

    val listRect = mutableListOf<RectF>()
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val width = canvas.width.toFloat()
        val height = canvas.height.toFloat()

        val space = 3f
        val dotWidth = (width - (dotSize - 1) * space) / dotSize

        for (i in 1..dotSize) {
            val rectF = RectF(start, 0f, start + dotWidth, height)
            start += dotWidth + space
            listRect.add(rectF)
        }
        drawShape(canvas, listRect)
    }

    private fun drawShape(canvas: Canvas, listRect: List<RectF>) {
        listRect.forEachIndexed { index, rectF ->
            canvas.drawRoundRectPath(
                rectF, height / 2f,
                true, true, true, true,
                if (index <= dotIndex - 1) selectedPaint else defaultPaint
            )
        }
    }

    private fun Canvas.drawRoundRectPath(
        rectF: RectF,
        radius: Float,
        roundTopLeft: Boolean,
        roundTopRight: Boolean,
        roundBottomLeft: Boolean,
        roundBottomRight: Boolean,
        paint: Paint
    ) {
        val path = Path()
        val left = rectF.left
        val top = rectF.top
        val right = rectF.right
        val bottom = rectF.bottom

        path.moveTo(left, top + radius)

        if (roundTopLeft) {
            path.quadTo(left, top, left + radius, top)
        } else {
            path.lineTo(left, top)
        }

        path.lineTo(right - radius, top)

        if (roundTopRight) {
            path.quadTo(right, top, right, top + radius)
        } else {
            path.lineTo(right, top)
        }

        path.lineTo(right, bottom - radius)

        if (roundBottomRight) {
            path.quadTo(right, bottom, right - radius, bottom)
        } else {
            path.lineTo(right, bottom)
        }

        path.lineTo(left + radius, bottom)

        if (roundBottomLeft) {
            path.quadTo(left, bottom, left, bottom - radius)
        } else {
            path.lineTo(left, bottom)
        }

        path.close()

        drawPath(path, paint)
    }

}

//cach dung     """____binding.dotview.init(6,5)

