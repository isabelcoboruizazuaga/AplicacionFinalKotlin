package com.example.aplicacionfinalkotlin.views

import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.aplicacionfinalkotlin.R
import com.example.aplicacionfinalkotlin.controllers.GameThread
import com.example.aplicacionfinalkotlin.controllers.Grenade
import com.example.aplicacionfinalkotlin.controllers.Player

class GameView(context: Context, attributes: AttributeSet) : SurfaceView(context, attributes), SurfaceHolder.Callback {
    private val thread: GameThread
    private var grenade: Grenade? = null
    private var player: Player? = null

    private var touched: Boolean =false
    private var touched_x: Int=0
    private var touched_y: Int=0

    init {
        holder.addCallback(this)
        thread= GameThread(holder,this)
    }


    override fun surfaceCreated(holder: SurfaceHolder) {
        //game objects
        grenade = Grenade(BitmapFactory.decodeResource(resources, R.drawable.grenade))
        player= Player(BitmapFactory.decodeResource(resources, R.drawable.grenade))

        //The game Thread is started
        thread.setRunning(true)
        thread.start()
    }

    override fun surfaceChanged(holder: SurfaceHolder, format: Int, width: Int, height: Int) {

    }

    override fun surfaceDestroyed(holder: SurfaceHolder) {
        var retry=true
        while (retry) {
            try {
                thread.setRunning(false)
                thread.join()
            } catch (e: Exception) {
                e.printStackTrace()
            }

            retry = false
        }
    }

    /**
     * Function to update the positions of player and game objects
     */
    fun update() {
        grenade!!.update()
        if(touched){
            player!!.updateTouch(touched_x,touched_y)
        }
    }

    /**
     * Everything that has to be drawn on Canvas
     */
    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        grenade!!.draw(canvas)
        player!!.draw(canvas)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        // when there is a touch on the screen the position is getted
        touched_x = event.x.toInt()
        touched_y = event.y.toInt()

        val action = event.action
        when (action) {
            MotionEvent.ACTION_DOWN -> touched = true
            MotionEvent.ACTION_MOVE -> touched = true
            MotionEvent.ACTION_UP -> touched = false
            MotionEvent.ACTION_CANCEL -> touched = false
            MotionEvent.ACTION_OUTSIDE -> touched = false
        }
        return true
    }

}