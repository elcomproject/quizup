package com.elcom.eonline.quizupapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.util.Log
import android.widget.Toast
import com.elcom.eonline.quizupapp.R
import com.elcom.eonline.quizupapp.ui.activity.model.entity.AnswerQuestion
import com.elcom.eonline.quizupapp.ui.activity.model.entity.Introduction
import com.elcom.eonline.quizupapp.ui.activity.model.entity.Result
import com.elcom.eonline.quizupapp.ui.network.RestClient
import com.elcom.eonline.quizupapp.ui.network.RestData
import com.elcom.eonline.quizupapp.utils.ConstantsApp
import com.elcom.eonline.quizupapp.utils.PreferUtils
import com.elcom.eonline.quizupapp.utils.ProgressDialogUtils
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.reward.RewardItem
import com.google.android.gms.ads.reward.RewardedVideoAd
import com.google.android.gms.ads.reward.RewardedVideoAdListener
import com.google.gson.JsonElement
import retrofit2.Callback
import retrofit2.Response
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_solo_match_break.*
import kotlinx.android.synthetic.main.coin_and_ex_layout.*
import retrofit2.Call



class SoloMatchBreakActivity : FragmentActivity(), RewardedVideoAdListener {



    var mQuestion:Introduction? = null
    private var mMatchId = ""
    private var mTopicId = ""
    private var mMinus = "0"
    private var isLoadingVideoAdmod = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        supportActionBar!!.hide()
        setContentView(R.layout.activity_solo_match_break)

        if(intent.hasExtra(ConstantsApp.KEY_INTRODUCTION_VALUE)){
            val bundle = intent.extras
            mQuestion = bundle.getSerializable(ConstantsApp.KEY_INTRODUCTION_VALUE) as Introduction
            mMatchId = bundle.getString(ConstantsApp.KEY_SOLO_MATCH_ID)
            mTopicId = bundle.getString(ConstantsApp.KEY_TOPIC_ID)
            updateUI()
            getResult()
        }

        btn_stop_playing.setOnClickListener {
            endGame()
            startActivityForResult(Intent(this,SoloMatchResultActivity::class.java).putExtra(ConstantsApp.KEY_SOLO_MATCH_ID,mMatchId).putExtra(ConstantsApp.KEY_QUESTION_ID,mTopicId), ConstantsApp.START_ACTIVITY_TO_PLAY_GAME_FROM_QUIZUPACTIVITY)
            overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
        }

        btn_continue_to_play.setOnClickListener {

            if(txt_coins.text.toString().toInt() >= mMinus.toInt()){
                val intent = Intent()
                intent.putExtra(ConstantsApp.KEY_MINUS_GAME, mMinus)
                setResult(ConstantsApp.RESULT_CODE_TO_CONTINUE_TO_PLAY_GAME_FROM_QUIZUPACTIVITY,intent)
                finish()
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            } else {
                Toast.makeText(this,"Không đủ coins", Toast.LENGTH_SHORT).show()
            }

        }

        btnVideoAdmod.setOnClickListener {
            if(isLoadingVideoAdmod){
                showVideoAdmod()
            } else {
                Toast.makeText(this@SoloMatchBreakActivity, "Bạn đã hết lượt xem Video", Toast.LENGTH_SHORT).show()
            }

        }


    }

    private fun endGame(){

        RestClient().getInstance().getRestService().endGame(PreferUtils().getUserId(this),mTopicId,"3",mMatchId).enqueue(object : Callback<RestData<JsonElement>>{
            override fun onFailure(call: Call<RestData<JsonElement>>?, t: Throwable?) {
                Log.e("hailpt","SoloMatchBreakActivity endGame onFailure")
            }

            override fun onResponse(call: Call<RestData<JsonElement>>?, response: Response<RestData<JsonElement>>?) {
                Log.e("hailpt","SoloMatchBreakActivity endGame")
                if (response?.body() != null){
                }
            }
        })
    }

    private fun getResult(){
        ProgressDialogUtils.showProgressDialog(this, 0, 0)
        RestClient().getInstance().getRestService().getResultAfterPlayingGame( PreferUtils().getUserId(this),mTopicId,mMatchId).enqueue(object: Callback<RestData<Result>> {
            override fun onResponse(call: Call<RestData<Result>>?, response: Response<RestData<Result>>?) {
                if (response?.body() != null){
                    ProgressDialogUtils.dismissProgressDialog()
                    mMinus = response.body().data!!.minus_coins!!
                    updateLayout(response.body().data!!)
                }
            }

            override fun onFailure(call: Call<RestData<Result>>?, t: Throwable?) {
                ProgressDialogUtils.dismissProgressDialog()
            }
        })
    }

    private fun updateLayout(result:Result){
        tvNextCoins.setText("TIẾP TỤC - "+mMinus)
        txt_stop_at.text = "+"+result.pointAnswer

        isLoadingVideoAdmod = result.view_video_more_turn!!.toInt() > 0
    }

    private fun updateUI(){
        if (mQuestion != null){
            txt_topic_title.text = mQuestion!!.nameTopic

            txt_coins.text = mQuestion!!.coins
            txt_point.text = mQuestion!!.point


            Picasso.get()
                    .load(PreferUtils().getAvatar(this))
                    .into(imv_topic)
        }
    }


    private lateinit var mRewardedVideoAd: RewardedVideoAd
    private fun showVideoAdmod(){

        MobileAds.initialize(this, "ca-app-pub-7842886552548626/2863752478")
        // Use an activity context to get the rewarded video instance.
        mRewardedVideoAd = MobileAds.getRewardedVideoAdInstance(this)
        mRewardedVideoAd.rewardedVideoAdListener = this
        ProgressDialogUtils.showProgressDialog(this, 0, 0)
        mRewardedVideoAd.loadAd("ca-app-pub-7842886552548626/2863752478",
                AdRequest.Builder().build())

    }

    override fun onRewardedVideoAdClosed() {
        goToActivityAfterWatchingVideoAd()
    }

    override fun onRewardedVideoAdLeftApplication() {

    }

    override fun onRewardedVideoAdLoaded() {
        ProgressDialogUtils.dismissProgressDialog()
        mRewardedVideoAd.show()
    }

    override fun onRewardedVideoAdOpened() {

    }

    override fun onRewardedVideoCompleted() {

    }

    override fun onRewarded(p0: RewardItem?) {
//        mRewardedVideoAd.destroy(this)
//        goToActivityAfterWatchingVideoAd()
    }

    override fun onRewardedVideoStarted() {

    }

    override fun onRewardedVideoAdFailedToLoad(p0: Int) {
        ProgressDialogUtils.dismissProgressDialog()

    }

    private fun goToActivityAfterWatchingVideoAd(){
        val intent = Intent()
        intent.putExtra(ConstantsApp.KEY_MINUS_GAME, "0")
        setResult(ConstantsApp.RESULT_CODE_TO_CONTINUE_TO_PLAY_GAME_FROM_QUIZUPACTIVITY,intent)
        finish()
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
    }



    override fun onBackPressed() {
//        super.onBackPressed()
//        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ConstantsApp.START_ACTIVITY_TO_PLAY_GAME_FROM_QUIZUPACTIVITY){
            when(resultCode){
                ConstantsApp.RESULT_CODE_TO_STOP_GAME_FROM_QUIZUPACTIVITY -> {
                    setResult(ConstantsApp.RESULT_CODE_TO_STOP_GAME_FROM_QUIZUPACTIVITY )
                    finish()
                }
            }
        }

    }

}
