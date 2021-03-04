package com.example.aplicacionfinalkotlin.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import com.example.aplicacionfinalkotlin.R
import com.example.aplicacionfinalkotlin.ui.main.PlaceholderFragment
import com.firebase.ui.auth.data.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.ArrayList
import kotlin.random.Random


class FirstFragment : PlaceholderFragment() {
    var sword=false
    var dragonExists=false
    var woods= 0
    var traps=0
    var meat=0
    private var dbReference: DatabaseReference? = null
    private var database: FirebaseDatabase? = null
    private var mAuth: FirebaseAuth? = null
    private lateinit var user: com.example.aplicacionfinalkotlin.models.User
    lateinit var stateText1: TextView
    lateinit var stateText2: TextView
    lateinit var btnOption1: Button
    lateinit var btnOption2: Button
    lateinit var btnOption3: Button
    lateinit var btn_action1: Button
    lateinit var btn_action2: Button
    lateinit var btn_action3: Button
    lateinit var tv_woods: TextView
    lateinit var tv_meat: TextView
    lateinit var tv_traps: TextView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_first, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Initialization of layout items
        stateText1 = requireView().findViewById<View>(R.id.txtState1) as TextView
        stateText2 = requireView().findViewById<View>(R.id.txtState2) as TextView
        btnOption1 = requireView().findViewById<View>(R.id.btnOption1) as Button
        btnOption2 = requireView().findViewById<View>(R.id.btnOption2) as Button
        btnOption3 = requireView().findViewById<View>(R.id.btnOption3) as Button
        btn_action1 = requireView().findViewById<View>(R.id.btn_action1) as Button
        btn_action2 = requireView().findViewById<View>(R.id.btn_action2) as Button
        btn_action3 = requireView().findViewById<View>(R.id.btn_action3) as Button
        tv_woods = requireView().findViewById<View>(R.id.tv_woods) as TextView
        tv_traps = requireView().findViewById<View>(R.id.tv_traps) as TextView
        tv_meat = requireView().findViewById<View>(R.id.tv_meat) as TextView

        //User uid extracted
        mAuth = FirebaseAuth.getInstance()
        var uid= mAuth!!.uid

        //Database initialization
        database = FirebaseDatabase.getInstance()
        dbReference = database!!.reference.child("User")
        dbReference = FirebaseDatabase.getInstance().reference.child("User").child(uid.toString())
        val eventListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                user= dataSnapshot.getValue(com.example.aplicacionfinalkotlin.models.User::class.java)!!
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("onDataChange", "Error!", databaseError.toException())
            }
        }
        dbReference!!.addValueEventListener(eventListener)


        //OnClick Listeners of option buttons
        //First button
        val clickListener = View.OnClickListener {view ->
            val b = view as Button
            val buttonText = b.text.toString()
            when (buttonText) {
                getResources().getString(R.string.runAway) -> option1_bt1()
                getResources().getString(R.string.enter)-> option2_bt1()
                getResources().getString(R.string.back) -> option3_bt2()
                getResources().getString(R.string.carry) -> option3_bt1()
                "Ignorarlo"-> ignore_btn()
            }
        }
        //Second button
        val clickListener2 = View.OnClickListener {view ->
            val b = view as Button
            val buttonText = b.text.toString()
            when (buttonText) {
                getResources().getString(R.string.refuge) -> option1_bt2()
                getResources().getString(R.string.knock) -> option2_bt2()
                getResources().getString(R.string.sorry) -> option2_bt2()
                getResources().getString(R.string.cut) -> finalOption()
                "Acercarse"-> approach_btn()
            }
        }
        btnOption1.setOnClickListener(clickListener)
        btnOption2.setOnClickListener(clickListener2)
        initializeStateText()

        //Woods Thread
        GlobalScope.launch(Dispatchers.Main) {
            while (woods < 100) {
                tv_woods.setText("woods: " + woods)

                if(woods >= 5) btn_action1.visibility= View.VISIBLE
                else btn_action1.visibility= View.INVISIBLE

                woods++
                delay(2000 )
            }
        }

        //Onclick for action buttons
        val makeTraps = View.OnClickListener {view ->
            if(woods>5) {
                traps++
                woods=woods-5
                var delay= (10000/traps).toLong()

                tv_traps.visibility=View.VISIBLE
                tv_meat.visibility=View.VISIBLE
                tv_traps.setText("traps: " + traps)

                //Meat thread, only when there are traps
                GlobalScope.launch(Dispatchers.Main) {
                    while (true) {
                        tv_meat.setText("meat: " + meat)

                        if(meat >= 50) btn_action2.visibility= View.VISIBLE
                    else btn_action2.visibility= View.INVISIBLE
                        meat++
                        delay(delay)
                    }
                }
            }
        }
        btn_action1.setOnClickListener(makeTraps)

        //On click action button 2
        val putMeat= View.OnClickListener { view ->
            if (meat>=50) {
                meat=meat-50
                stateText1.text = "Pones algo de carne en las trampas..."
                stateText2.text = "Oyes un rugido"
                stateText2.visibility=View.VISIBLE

                btnOption1.visibility = View.VISIBLE
                btnOption2.visibility = View.VISIBLE
                btnOption1.text = "Ignorarlo"
                btnOption2.text = "Acercarse"
            }
        }
        btn_action2.setOnClickListener(putMeat)

        //On click action button 3
        val save= View.OnClickListener { view ->
            user.woods=woods
            user.meat=meat
            user.traps=traps
            user.sword=sword
            saveInDatabase()
        }
        btn_action3.setOnClickListener(save)
    }


    fun saveInDatabase(){
        dbReference!!.setValue(user)
    }

    fun initializeStateText() {
        stateText1.text = getResources().getString(R.string.night)
        try {
            Thread.sleep(2000)
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        btnOption1.visibility = View.VISIBLE
        btnOption2.visibility = View.VISIBLE
        btnOption1.text = getResources().getString(R.string.runAway)
        btnOption2.text = getResources().getString(R.string.refuge)
    }

    fun option1_bt1(){
        stateText1.text=  getResources().getString(R.string.cantEscape)
    }
    fun option1_bt2(){
        stateText2.visibility= View.VISIBLE

        stateText1.text=  getResources().getString(R.string.smoke)
        stateText2.text=  getResources().getString(R.string.hut)
        btnOption1.text= getResources().getString(R.string.enter)
        btnOption2.text= getResources().getString(R.string.knock)
    }
    fun option2_bt1(){
        stateText2.visibility= View.VISIBLE

        stateText1.text= getResources().getString(R.string.lumberjack)
        stateText2.text=  getResources().getString(R.string.modals)
        btnOption1.text= getResources().getString(R.string.back)
        btnOption2.text= getResources().getString(R.string.sorry)
    }

    fun option2_bt2(){
        stateText2.visibility= View.VISIBLE

        stateText1.text=  getResources().getString(R.string.lost)
        stateText2.text=  getResources().getString(R.string.awake)
        btnOption1.text= getResources().getString(R.string.carry)
        btnOption2.text= getResources().getString(R.string.cut)
    }

    fun option3_bt2(){
        stateText2.visibility= View.VISIBLE

        stateText1.text=  getResources().getString(R.string.cave)
        stateText2.text=  getResources().getString(R.string.awake)
        btnOption1.text= getResources().getString(R.string.carry)
        btnOption2.text= getResources().getString(R.string.cut)
    }
    fun option3_bt1(){
        stateText2.visibility= View.INVISIBLE

        stateText1.text= getResources().getString(R.string.cutting)
    }

    fun finalOption(){
        stateText2.visibility= View.INVISIBLE

        stateText1.text=  getResources().getString(R.string.wood)
        btnOption1.visibility= View.INVISIBLE
        btnOption2.visibility= View.INVISIBLE

        tv_woods.visibility=View.VISIBLE
    }

    fun ignore_btn(){
        stateText1.text=  "Ignoras el sonido"
        stateText2.text=  "Cuando vas a la zona la carne ya no está"

        btnOption1.visibility= View.INVISIBLE
        btnOption2.visibility= View.INVISIBLE
    }

    fun approach_btn(){
        stateText1.text=  "Te acercas con cautela..."
        stateText2.text=  "Hay un enorme dragón, coge la carne y huye al verte"
        dragonExists=true

        btnOption1.visibility= View.INVISIBLE
        btnOption2.visibility= View.INVISIBLE

        //The dragon can return with an object
        GlobalScope.launch(Dispatchers.Main) {
            delay(5000)
            val random = Random.nextInt(0, 2)
            if(random==1){
                stateText1.text="The dragon has returned!"
                stateText2.text="He has brought an iron sword"
                sword=true
                user.sword=true
                saveInDatabase()
                Toast.makeText(context,"Iron sword added to inventory",Toast.LENGTH_SHORT).show()
            }
            dragonExists=false
        }
    }

}