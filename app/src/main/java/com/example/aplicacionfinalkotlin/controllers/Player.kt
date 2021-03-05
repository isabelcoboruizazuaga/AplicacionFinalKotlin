package com.example.aplicacionfinalkotlin.controllers

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.Canvas

class Player(private val image: Bitmap) {
    private var x: Int = 0
    private var y: Int = 0
    private val w: Int
    private val h: Int
    private val screenWidth = Resources.getSystem().displayMetrics.widthPixels
    private val screenHeight = Resources.getSystem().displayMetrics.heightPixels

    init {
        w = image.width
        h = image.height

        x = 100
        y = 100
    }

    /**
     * Draws the object on to the canvas.
     */
    fun draw(canvas: Canvas) {
        canvas.drawBitmap(image, x.toFloat(), y.toFloat(), null)
    }

    /**
     * update properties for the game object
     * when the player moves the screen the position updates
     */
    fun updatePosition(player_x: Int, player_y: Int) {
        x = player_x - w / 2
        y = player_y - h / 2
    }

}