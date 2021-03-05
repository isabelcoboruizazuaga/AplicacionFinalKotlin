package com.example.aplicacionfinalkotlin.controllers

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas
import kotlin.random.Random

class Enemy(var image: Bitmap, var health: Int) {
    public var movement=true
    var x: Int = 0
    var y: Int = 0
    var w: Int = 0
    var h: Int = 0
    private var xVelocity = 5
    private var yVelocity = 5
    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    private val screenHeight = Resources.getSystem().displayMetrics.heightPixels

    init {
        w = image.width
        h = image.height

        x = Random.nextInt(w,screenWidth-w)
        y = Random.nextInt(h,screenHeight-h)
    }

    /**
     * Draws the object on to the canvas.
     */
    fun draw(canvas: Canvas) {
        canvas.drawBitmap(image, x.toFloat(), y.toFloat(), null)
    }

    /**
     * update properties for the game object
     */
    fun update() {
        if (movement) {
            if (x > screenWidth - image.width || x <= 1) {
                xVelocity = xVelocity * -1
            }
            if (y > screenHeight - image.height || y < image.height) {
                yVelocity = yVelocity * -1
            }

            x += (xVelocity)
            y += (yVelocity)
        }
    }

    fun touched(x2: Float, y2: Float): Boolean {
        var boolean= x2 > x && x2 < x + w && y2 > y && y2 < y + h
        return boolean
    }

}