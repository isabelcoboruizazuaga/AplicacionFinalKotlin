package com.example.aplicacionfinalkotlin.views

import android.app.Activity
import android.content.Context
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.SurfaceHolder
import android.view.SurfaceView
import com.example.aplicacionfinalkotlin.R
import com.example.aplicacionfinalkotlin.controllers.Enemy
import com.example.aplicacionfinalkotlin.controllers.GameThread
import com.example.aplicacionfinalkotlin.controllers.Player
import java.util.*
import kotlin.math.roundToInt
import kotlin.random.Random

class GameView(context: Context, attributes: AttributeSet) : SurfaceView(context, attributes), SurfaceHolder.Callback, SensorEventListener {
    private lateinit var sensorManager: SensorManager
    var lastClick: Long=0
    var lastCollision: Long=0

    var health=3

    private val thread: GameThread
    private var player: Player? = null

    private var moving: Boolean =false
    private var player_x: Int=100
    private var player_y: Int=100


    private val sprites: MutableList<Enemy> = ArrayList<Enemy>()

    private var bpmPlayer= BitmapFactory.decodeResource(resources, R.drawable.grenade)
    private var bpmHeart1= BitmapFactory.decodeResource(resources, R.drawable.heart)
    private var bpmHeart2= BitmapFactory.decodeResource(resources, R.drawable.heart)
    private var bpmHeart3= BitmapFactory.decodeResource(resources, R.drawable.heart)
    private var bpmHeart4= BitmapFactory.decodeResource(resources, R.drawable.heart)
    private var bpmHeart5= BitmapFactory.decodeResource(resources, R.drawable.heart)
    private var bpmPoti1= BitmapFactory.decodeResource(resources, R.drawable.minipotion)
    private var bpmPoti2= BitmapFactory.decodeResource(resources, R.drawable.minipotion)

    init {
        //Accelerometer initialization
        val sensorManager =
            context.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        sensorManager.registerListener(
            this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),
            SensorManager.SENSOR_STATUS_ACCURACY_MEDIUM
        )

        holder.addCallback(this)
        thread= GameThread(holder,this)
    }


    override fun surfaceCreated(holder: SurfaceHolder) {
        //game objects
        refillEnemies()
        player= Player(bpmPlayer)

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
        for(i in sprites) {
            i!!.update()
        }
        if(moving){
            player!!.updatePosition(player_x,player_y)
        }
        collision()
    }

    /**
     * Everything that has to be drawn on Canvas
     */
    override fun draw(canvas: Canvas) {
        super.draw(canvas)

        if(health>0){
            canvas.drawBitmap(bpmHeart1,40.toFloat(),40.toFloat(),null)
            if(health>1){
                canvas.drawBitmap(bpmHeart2,41.toFloat()+bpmHeart1.width,40.toFloat(),null)
                if(health>2){
                    canvas.drawBitmap(bpmHeart3,42.toFloat()+(bpmHeart1.width *2),40.toFloat(),null)
                    if(health>3){
                        canvas.drawBitmap(bpmHeart4,43.toFloat()+(bpmHeart1.width *3),40.toFloat(),null)
                        if(health>4){
                            canvas.drawBitmap(bpmHeart5,44.toFloat()+(bpmHeart1.width *4),40.toFloat(),null)
                        }
                    }
                }
            }
        }

        canvas.drawBitmap(bpmPoti2,width - 40.toFloat(),40.toFloat(),null)
        canvas.drawBitmap(bpmPoti1,width - 41.toFloat()-bpmPoti1.width,40.toFloat(),null)

        for(i in sprites) {
            i!!.draw(canvas)
        }
        player!!.draw(canvas)
    }

    fun collision(){
        if (System.currentTimeMillis() - lastCollision > 300) {
            lastCollision = System.currentTimeMillis()
            for (i in sprites) {
                if (i!!.touched(player_x.toFloat(), player_y.toFloat())) {
                    //A random number is generated, the enemy has 33% chance to hurt the player
                    val random = Random.nextInt(0, 10)
                    if (random == 0 || random==4 || random==8) {
                        health--
                    }
                    //The player will attack the enemy with their sword and if the enemy dies it disappears
                    i.health--
                    if(i.health<=0){
                        sprites.remove(i)
                        if(sprites.size==0) (context as Activity).finish()
                    }
                }
            }
        }
    }

    fun increaseHealth(){
        health++
    }



    fun refillEnemies(){
        sprites.add(Enemy(BitmapFactory.decodeResource(resources, R.drawable.grenade),2))
        sprites.add(Enemy(BitmapFactory.decodeResource(resources, R.drawable.grenade),2))
        sprites.add(Enemy(BitmapFactory.decodeResource(resources, R.drawable.grenade),2))
        sprites.add(Enemy(BitmapFactory.decodeResource(resources, R.drawable.grenade),2))
        sprites.add(Enemy(BitmapFactory.decodeResource(resources, R.drawable.grenade),2))
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (System.currentTimeMillis() - lastClick > 300) {
            lastClick = System.currentTimeMillis()
            val x = event.x
            val y = event.y

            if(x>width - 40 - bpmPoti1.width && y<40+bpmPoti1.height) health++

        }
        return true
    }

    //Accelerometer methods
    override fun onSensorChanged(event: SensorEvent) {
        synchronized(this) {
            //Sensor values are assigned
            if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                player_x = (player_x - event.values[0]).roundToInt()
                moving = true
                player_y = (player_y + event.values[1]).roundToInt()
                //When it arrives to the horizontal border it stops
                if (player_x >width- bpmPlayer.width || player_x <=1) {
                    player_x = (player_x + event.values[0]).roundToInt()
                }
                //Same happens in vertical border
                if (player_y>height- bpmPlayer.height || player_y  <bpmPlayer.height) {
                    player_y = (player_y - event.values[1]).roundToInt()
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

}