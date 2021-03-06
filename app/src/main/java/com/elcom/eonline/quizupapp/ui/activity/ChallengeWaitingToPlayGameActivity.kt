package com.elcom.eonline.quizupapp.ui.activity

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.elcom.eonline.quizupapp.R
import com.elcom.eonline.quizupapp.ui.activity.model.entity.ChallengeMatching
import com.elcom.eonline.quizupapp.ui.network.RestClient
import com.elcom.eonline.quizupapp.ui.network.RestData
import com.elcom.eonline.quizupapp.utils.ConstantsApp
import com.elcom.eonline.quizupapp.utils.PreferUtils
import com.google.gson.Gson
import com.google.gson.JsonArray
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_challenge_waiting_to_playgame.*
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.json.JSONArray



class ChallengeWaitingToPlayGameActivity : BaseActivityQuiz() {


    private var opponentId = ""
    private var mChallengeMatching:ChallengeMatching? = null
    private var mMatchId = ""
    private var mTopicId = ""
    private var mQuestionNumber = 0
    private var numberOfRightAnswerFromMe = 0
    private var isFromOpoonentOrYou = false // true = you -- false = opponent
    override fun getLayout(): Int {
        return R.layout.activity_challenge_waiting_to_playgame
    }

    override fun initView() {

    }

    override fun initData() {

        if(intent.hasExtra("data")){
            isFromOpoonentOrYou = true
            val data = intent.extras.getString("data")

            val mObject = JSONObject(data)

//            tvName.text = mObject["name"] as String

            mTopicId = mObject["topicId"] as String

            if(intent.hasExtra("accept")){
                opponentId = mObject["sendId"] as String
            } else {
                opponentId = mObject["userSendId"] as String
            }

//            Toast.makeText(this, "opponentId "+ mObject["sendId"] + " "+mObject["toId"], Toast.LENGTH_SHORT).show()

            getData(mTopicId,opponentId)

        } else {
            isFromOpoonentOrYou = false
            val data = intent.extras.getString("data_op")

            val mObject = JSONObject(data)


            mChallengeMatching = Gson().fromJson(mObject["question"] as String, ChallengeMatching::class.java)

            updateLayout(mChallengeMatching!!)
            mTopicId = mObject["topicId"] as String
            opponentId = mObject["toId"] as String
            mMatchId = mChallengeMatching!!.matchId!!
            moveToPlayingGame()
//            Toast.makeText(this, "Được mời Ok",Toast.LENGTH_SHORT).show()
            Log.e("hailpt"," ChallengeWaitingToPlayGameActivity ~~> "+mObject)

        }
    }

    fun getData(topicId:String, opponentId:String) {

        RestClient().getInstance().getRestService().getOpponentChallengeInfo(topicId,opponentId).enqueue(object : Callback<RestData<ChallengeMatching>> {

            override fun onFailure(call: Call<RestData<ChallengeMatching>>?, t: Throwable?) {

            }

            override fun onResponse(call: Call<RestData<ChallengeMatching>>?, response: Response<RestData<ChallengeMatching>>?) {
                if(response?.body() != null){
                    val data = response.body().data
                    mChallengeMatching = data
                    mMatchId = data!!.matchId!!
                    updateLayout(data)
                    setTimeEndMatchDuel()
                    sendQuestionMatchDuel(mChallengeMatching!!)
                    moveToPlayingGame()
                }
            }
        })
    }

    private fun updateLayout(challengeMatching:ChallengeMatching){

        if(isFromOpoonentOrYou) {
            Picasso.get().load(mChallengeMatching!!.player!!.avatar).into(imvMyself)
            Picasso.get().load(mChallengeMatching!!.opponent!!.avatar).into(imvOb)
            tvOpName.text = mChallengeMatching!!.opponent!!.name
        } else {
            Picasso.get().load(mChallengeMatching!!.opponent!!.avatar).into(imvMyself)
            Picasso.get().load(mChallengeMatching!!.player!!.avatar).into(imvOb)
            tvOpName.text = mChallengeMatching!!.player!!.name
        }
    }

    fun setTimeEndMatchDuel(){
        val myInfo = JSONObject()
        try {
            myInfo.put("topicId", mTopicId)
            myInfo.put("sendId", PreferUtils().getUserId(this))
            myInfo.put("toId", opponentId )
            myInfo.put("statusUserBot", false)
            myInfo.put("matchId", mMatchId)

        } catch (e: JSONException) {

        }
        ConstantsApp.socketManage.setTimeEndMatchDuel(myInfo)
    }

    fun sendQuestionMatchDuel(challengeMatching:ChallengeMatching){

        val myInfo = JSONObject()
        try {
            myInfo.put("topicId", mTopicId)
            myInfo.put("sendId", PreferUtils().getUserId(this))
            myInfo.put("toId", opponentId )
            myInfo.put("name", PreferUtils().getName(this))
            myInfo.put("userSendId", opponentId)
            myInfo.put("matchId", mMatchId)

            val gson = Gson()
            val question = gson.toJson(challengeMatching)

            myInfo.put("question",question)
        } catch (e: JSONException) {

        }
        ConstantsApp.socketManage.sendQuestionMatchDuel(myInfo)
    }


    private fun moveToPlayingGame(){

        Handler().postDelayed(Runnable {
            if ( mChallengeMatching != null){
                val mainIntent = Intent(this, ChallengeMatchFriendQuestionIntro::class.java)
                val bundle = Bundle()
                bundle.putSerializable("value", mChallengeMatching)
                mainIntent.putExtras(bundle)
                mainIntent.putExtra(ConstantsApp.KEY_QUESTION_ID,mTopicId)
                mainIntent.putExtra(ConstantsApp.KEY_SOLO_MATCH_ID,mMatchId)
                mainIntent.putExtra(ConstantsApp.KEY_CHALLENGE_IS_OPPONENT,isFromOpoonentOrYou)
                startActivityForResult(mainIntent, ConstantsApp.START_ACTIVITY_TO_PLAY_GAME_FROM_QUIZUPACTIVITY)
            }

        }, 4000)
    }

//    fun getTimeCountDown(userSendId: String, toId: String ){
//        val myInfo = JSONObject()
//        try {
//            myInfo.put("topicId", mTopicId)
//            myInfo.put("sendId", PreferUtils().getUserId(this))
//            myInfo.put("toId", toId )
//            myInfo.put("challenge", "true")
//            myInfo.put("url", "url")
//            myInfo.put("name", "Ambitionnnn")
//            myInfo.put("topicName", "topicName")
//            myInfo.put("urlTopic", "urlTopic")
//            myInfo.put("userSendId", userSendId)
//        } catch (e: JSONException) {
//
//        }
//        ConstantsApp.socketManage.sendChallengeInformation(myInfo)
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ConstantsApp.START_ACTIVITY_TO_PLAY_GAME_FROM_QUIZUPACTIVITY){
            when(resultCode){

                ConstantsApp.RESULT_CODE_TO_STOP_GAME_FROM_QUIZUPACTIVITY -> {
                    setResult(ConstantsApp.RESULT_CODE_TO_STOP_GAME_FROM_QUIZUPACTIVITY)
                    finish()
                }

                ConstantsApp.RESULT_CODE_TO_CONTINUE_TO_PLAY_GAME_FROM_QUIZUPACTIVITY -> {
                    setResult(ConstantsApp.RESULT_CODE_FROM_RIGHT_ANSWER_USING_COINS)
                    finish()
                }
            }
        }
    }

}
