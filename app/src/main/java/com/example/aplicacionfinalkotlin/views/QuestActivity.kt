package com.example.aplicacionfinalkotlin.views

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager


class QuestActivity : Activity() {
    private lateinit var user: com.example.aplicacionfinalkotlin.models.User

    public var userPotions=1
    public var userDmg= 1
    public var level= 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        val intent = intent
        userDmg= intent.getIntExtra("dmg",1)
        userPotions= intent.getIntExtra("potions",0)
        level= intent.getIntExtra("level",1)

        //Game creation
        val gv = GameView(this,userDmg,userPotions ,level,null)
        setContentView(gv)

    }
}