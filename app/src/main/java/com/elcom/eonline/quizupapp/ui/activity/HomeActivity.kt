package com.elcom.eonline.quizupapp.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.support.v4.app.ActivityCompat
import android.support.v4.app.Fragment
import android.view.View
import com.elcom.eonline.quizupapp.R
import com.elcom.eonline.quizupapp.ui.fragment.HomeFragment
import com.elcom.eonline.quizupapp.ui.fragment.ListTopicGroupTotalFragment
import com.elcom.eonline.quizupapp.ui.fragment.LiveChallengeFragment
import com.elcom.eonline.quizupapp.ui.fragment.SettingFragment
import com.elcom.eonline.quizupapp.utils.BottomNavigationViewHelper
import com.elcom.eonline.quizupapp.utils.ConstantsApp
import com.elcom.eonline.quizupapp.utils.LogUtils
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : BaseActivityQuiz() {




    var mainFragment = ListTopicGroupTotalFragment()
    var liveChallengeFragment = LiveChallengeFragment()
    var homeFragment = HomeFragment()
    var settingFragment = SettingFragment()
    var mEmail:String = ""
    var selectedFragment: Fragment? = mainFragment
    var mTagOfFragment = "homeFragment"

    public var mImage: Bitmap? = null
    override fun getLayout(): Int {
        return R.layout.activity_home
    }

    override fun initView() {

    }

    override fun initData() {

        navigation.selectedItemId = R.id.action_item1
        BottomNavigationViewHelper.removeShiftMode(navigation)
        navigation.setOnNavigationItemSelectedListener { item ->

            when (item.itemId) {

                R.id.action_item2 -> {
                    selectedFragment = mainFragment
                    mTagOfFragment = "mainFragment"
                }

                R.id.action_item1 -> {
                    selectedFragment = homeFragment
                    mTagOfFragment = "homeFragment"
                }

                R.id.action_item3 -> {
                    selectedFragment = liveChallengeFragment
                    mTagOfFragment = "liveChallengeFragment"
                }


                R.id.action_item4 -> {
                    selectedFragment = settingFragment
                    mTagOfFragment = "settingFragment"
                }
//                R.id.action_item4 -> selectedFragment = SettingFragment()
//                R.id.action_item5 -> selectedFragment = SettingFragment()
            }
            val transaction = supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frame_layout, selectedFragment, mTagOfFragment)
            transaction.commit()
            true

        }

        //Manually displaying the first fragment - one time only
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.frame_layout, homeFragment,mTagOfFragment)
        transaction.commit()

        if(intent.hasExtra(ConstantsApp.KEY_QUESTION_ID)){
            val  topicId = intent.getStringExtra(ConstantsApp.KEY_QUESTION_ID)
            val  matchId = intent.getStringExtra(ConstantsApp.KEY_SOLO_MATCH_ID)
            val  questionNumber = intent.getIntExtra(ConstantsApp.KEY_QUESTION_NUMBER,1)
            moveToGameIntroduction(topicId,matchId,questionNumber)
        }
    }

    private fun moveToGameIntroduction(topic_id:String, match_id:String, question_number:Int){
        val intent = Intent(applicationContext, SoloQuestionIntro::class.java)
        intent.putExtra(ConstantsApp.KEY_QUESTION_ID,topic_id)
        intent.putExtra(ConstantsApp.KEY_SOLO_MATCH_ID,match_id)
        intent.putExtra(ConstantsApp.KEY_QUESTION_NUMBER,question_number)
        startActivityForResult(intent, ConstantsApp.START_ACTIVITY_TO_PLAY_GAME_FROM_QUIZUPACTIVITY)
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }


    override fun onResume() {
        super.onResume()



    }


    fun addHeader(pView:View){
        lnHeader.addView(pView)
    }

    fun sendTest(){
        val intent = Intent()
        intent.action = "com.example.SendBroadcast"
        intent.putExtra("name", "Ambition")
        intent.addFlags(Intent.FLAG_INCLUDE_STOPPED_PACKAGES)
        sendBroadcast(intent)
    }

    override fun onBackPressed() {

        val currentFragment = supportFragmentManager.findFragmentById(R.id.frame_layout)
        LogUtils.e("hailpt","onSomeoneInviteYouToPlayGame onBackPressed "+currentFragment.tag)
        if (currentFragment.tag == "mainFragment" ){
            LogUtils.e("hailpt","onSomeoneInviteYouToPlayGame onBackPressed ")
        }

        if (supportFragmentManager.backStackEntryCount == 0) {
            finish();
        } else {
            supportFragmentManager.popBackStackImmediate()
        }
    }

    fun isStoragePermissionGranted(): Boolean {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE) === PackageManager.PERMISSION_GRANTED) {
                return true
            } else {

                ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
                return false
            }
        } else { //permission is automatically granted on sdk<23 upon installation
            return true
        }
    }



}
