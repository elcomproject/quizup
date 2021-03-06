package com.elcom.eonline.quizupapp.ui.network

import com.elcom.eonline.quizupapp.ui.activity.model.entity.*
import com.elcom.eonline.quizupapp.ui.activity.model.entity.Followed.Followed
import com.elcom.eonline.quizupapp.ui.activity.model.entity.admod.AdmodAds
import com.elcom.eonline.quizupapp.ui.activity.model.entity.coin.Coin
import com.elcom.eonline.quizupapp.ui.activity.model.entity.profile.Profile
import com.elcom.eonline.quizupapp.ui.activity.model.entity.response.*
import com.elcom.eonline.quizupapp.ui.activity.model.entity.response.topicdetail.Topic
import com.elcom.eonline.quizupapp.ui.activity.model.entity.response.topicdetail.TopicDetail
import com.google.gson.JsonElement
import io.reactivex.Single
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import java.util.ArrayList

/**
 * Created by admin on 3/8/2018.
 */
interface RestService {


    /*Login*/
    @POST("user/login-by-facebook")
    @FormUrlEncoded
    abstract fun loginWithFacebook(@Field("facebook_id") feedId: String,@Field("access_token") id: String): Call<RestData<User>>

    @POST("user/login-by-facebook")
    @FormUrlEncoded
    abstract fun loginWithGoogle(@Field("access_token") id: String): Call<RestData<User>>

    @GET("list-of-topics")
    abstract fun getListTopic(): Call<RestData<List<Caterogy>>>


    // Topic Detail
    @GET("topic/view?")
    abstract fun getTopicDetail(@Query("topic_id") topic_id: String): Call<RestData<TopicDetail>>

    @GET("topic/view?")
    abstract fun getTopicDetailTest(@Query("topic_id") topic_id: String): Call<RestData<JsonElement>>

    @POST("user/solo-match")
    @FormUrlEncoded
    abstract fun getTopicMatchId(@Field("user_id") user_id: String,@Field("topic_id") topic_id: String): Call<RestData<SoloMatch>>

    @POST("post-solo-match-pause")
    @FormUrlEncoded
    abstract fun pauseGame(@Field("topic_id") topic_id: String,@Field("match_id") match_id: String): Call<RestData<JsonElement>>


    @GET("get-search-opponent?")
    abstract fun getTopicMatchIdChallenge(@Query("topic_id") topic_id: String): Call<RestData<JsonElement>>

    @POST("post-delete-match-duel")
    @FormUrlEncoded
    abstract fun removeTopicChallenge(@Field("topic_id") topic_id: String,@Field("match_id") match_id: String): Call<RestData<JsonElement>>



    @POST("user/set-follow-topic")
    @FormUrlEncoded
    abstract fun followAndUnfollowTopic(@Field("topic_id") topic_id: String,@Field("status") status: String): Call<JsonElement>


    // SoloMatchWithTextActivity
    @POST("post-answer-solo-match")
    @FormUrlEncoded
    abstract fun answerTheQuestion(@Field("user_id") user_id: String,@Field("topic_id") topic_id: String,@Field("answer_id") answer_id: String,@Field("question_id") question_id: String,@Field("match_id") match_id: String, @Field("last_question") last_question: String): Call<RestData<AnswerQuestion>>

    // End Game
    @POST("post-solo-match-break")
    @FormUrlEncoded
    abstract fun endGame(@Field("user_id") user_id: String,@Field("topic_id") topic_id: String,@Field("type") type: String,@Field("match_id") match_id: String): Call<RestData<JsonElement>>



    @GET("user/show-list-of-topics?")
    abstract fun getListHomeTopic(@Query("user_id") user_id: String): Call<RestData<List<Caterogy>>>


    @GET("get-buy-coins-list")
    abstract fun getCoinPayment(): Call<RestData<Coin>>


    @POST("post-buy-coins")
    @FormUrlEncoded
    abstract fun postBuyCoin(@Field("coins_number") coins_number: String,@Field("type") type: String): Call<RestData<JsonElement>>

    /*Introduction Of a question*/
    @GET("get-question-solo-match?")
    abstract fun getIntroductionOfQuestion(@Query("user_id") user_id: String, @Query("topic_id") topic_id: String,@Query("question_number") question_number: String,@Query("type") type: String, @Query("match_id") match_id: String,@Query("minus_coins") minus_coins: String): Call<RestData<Introduction>>


    @GET("get-question-solo-match?")
    abstract fun getQuestionText(@Query("user_id") user_id: String, @Query("topic_id") topic_id: String, @Query("question_number") question_number: String): Call<RestData<Question>>

    @GET("get-question-show?")
    abstract fun getLiveQuestion(@Query("show_id") show_id: String, @Query("number_question_show") number_question_show: String): Call<RestData<LiveQuestionData>>

    @GET("get-start-time-count-down-show?")
    abstract fun getTimeCountDownLiveQuestion(@Query("show_id") show_id: String): Call<RestData<JsonElement>>

    @GET("get-setting-app")
    abstract fun getSettingAdmod(): Call<RestData<AdmodAds>>

    // SoloMatchResultActivity
    @GET("get-solo-match-result?")
    abstract fun getResultAfterPlayingGame(@Query("user_id") user_id: String, @Query("topic_id") topic_id: String,@Query("match_id") match_id: String): Call<RestData<Result>>

    @GET("get-statistical-data?")
    abstract fun getStatistic(@Query("topic_id") topic_id: String,@Query("match_id") match_id: String): Call<RestData<StatisticalData>>
    @GET("get-statistical-data?")

    abstract fun getStatistic(@Query("topic_id") topic_id: String): Call<RestData<StatisticalRes>>


    // SoloMatchResultActivity
    @GET("get-full-question-duel-match?")
    abstract fun getOpponentChallenge(@Query("topic_id") topic_id: String,@Query("match_id") match_id: String): Call<RestData<ChallengeMatching>>

    // SoloMatchResultActivity
    @POST("post-create-match-duel")
    @FormUrlEncoded
    abstract fun getOpponentChallengeInfo(@Field("topic_id") topic_id: String,@Field("user_id_opponent") user_id_opponent: String): Call<RestData<ChallengeMatching>>


    // Live Challenge
    @GET("get-list-show")
    abstract fun getLiveChallengeList(): Call<RestData<LiveChallengeBig>>


    @POST("post-register-show")
    @FormUrlEncoded
    abstract fun joinLiveChallengeGame(@Field("register_coins") register_coins: String,@Field("show_id") show_id: String): Call<RestData<JsonElement>>


    @GET("get-list-questions-show?")
    abstract fun getLiveChallengeQuestionList(@Query("show_id") show_id: String): Call<RestData<List<ChallengeQuestion>>>


    @POST("post-answer-show")
    @FormUrlEncoded
    abstract fun answerLiveChallengeQuestion(@Field("show_id") show_id: String, @Field("answer_id") answer_id: String,@Field("question_id") question_id: String,@Field("time_answer") time_answer: String): Call<RestData<JsonElement>>


    @POST("post-break-game-show")
    @FormUrlEncoded
    abstract fun breakLiveChallengeQuestion(@Field("show_id") show_id: String, @Field("answer_id") answer_id: String,@Field("question_id") question_id: String,@Field("time_answer") time_answer: String): Call<RestData<JsonElement>>


    /*Live challenge Result*/
    @POST("time-count-down-get-result-show")
    @FormUrlEncoded
    abstract fun getTimeCountDownChallengeShowResult(@Field("show_id") show_id: String): Call<RestData<JsonElement>>

    @POST("get-show-result")
    @FormUrlEncoded
    abstract fun getChallengeShowResult(@Field("show_id") show_id: String): Call<RestData<List<LiveChallengeResult>>>

    @POST("get-show-result")
    @FormUrlEncoded
    abstract fun endLiveChallengeGame(@Field("show_id") show_id: String): Call<RestData<JsonElement>>
    /*Live challenge Result*/


    @GET("profile/detail?")
    abstract fun getProfileData(@Query("user_id") user_id: String): Call<RestData<Profile>>


    @GET("profile/follows?")
    abstract fun getFollowed(@Query("user_id") user_id: String): Call<RestData<Followed>>


    @GET("get-user-state-solo-match")
    abstract fun getMatchWhenComeBackSplash(): Call<RestData<ContinueMatch>>

    @GET("profile/friends?")
    abstract fun getFriends(@Query("user_id") user_id: String,@Query("limit") limit: Int,@Query("offset") offset: Int): Call<RestData<Followed>>

    @GET("list-topic-limit?")
    abstract fun getListTopicFromKey(@Query("limit") limit: Int,@Query("offset") offset: Int,@Query("key") key: Int,@Query("category_id") category_id: String): Call<RestData<List<Topic>>>

    @Multipart
    @POST("profile/update")
    abstract fun updateProfile(
                                    @Part("description") description: RequestBody,
//                                    @Part avatar: MultipartBody.Part,
//                                    @Part cover: MultipartBody.Part,
                                    @Part("gender") gender: RequestBody,
                                    @Part("password") password: RequestBody,
                                    @Part("name") email: RequestBody): Call<RestData<JsonElement>>

    @Multipart
    @POST("profile/update")
    abstract fun updateAvatar(
            @Part avatar: MultipartBody.Part): Call<RestData<Profile>>




    @GET("search?")
    abstract fun searchTopic(@Query("keyword") keyword: String,@Query("limit") limit: Int,@Query("offset") offset: Int,@Query("type") type: Int): Call<RestData<List<CaterogySearch>>>


    @GET("search-topic?")
    abstract fun searchTopics(@Query("keyword") keyword: String,@Query("limit") limit: Int,@Query("offset") offset: Int): Call<RestData<List<Caterogy>>>

    @GET("achievements?")
    abstract fun getAchievement(@Query("topic_id") topic_id: String): Call<RestData<Achivement>>


    @POST("delete-history-match?")
    @FormUrlEncoded
    abstract fun removeHistory(@Field("match_id") match_id: Int): Call<RestData<JsonElement>>


    @POST("post-send-answer-guess-word")
    @FormUrlEncoded
    abstract fun sendAnswerGuessWord(@Field("user_id") user_id: String,@Field("topic_id") topic_id: String,@Field("answer_id") answer_id: String,@Field("question_id") question_id: String,@Field("match_id") match_id: String, @Field("last_question") last_question: String): Call<RestData<JsonElement>>


    @POST("profile/set-follow-user?")
    abstract fun followFriend(@Query("follow_id") follow_id: String): Call<RestData<JsonElement>>


    @POST("delete-history-match?")
    @FormUrlEncoded
    abstract fun removeAllHistory(@Field("match_id") match_id: String): Call<RestData<JsonElement>>



    @POST("api/v1/anons/auth/phone/check")
    abstract fun checkPhoneNumber(@Body json: RequestBody): Call<JsonElement>

    @GET("api/volleyJsonObject")
    abstract fun findIdByPhone(): Call<Person>

    @GET("api/volleyJsonObject")
    abstract fun testJavaRx(): Single<List<Person>>

    @GET("api/volleyJsonObject")
    fun testJavaRx2(): Single<Person>


    @GET("api/volleyJsonArray")
    abstract fun getMobiles(): Single<List<Person>>

    @DELETE("/api/v1/auth/groups/{groupID}/members")
    abstract fun deleteMemberGroup(@Path("groupID") groupId: Int, @Query("ids") idDelete: String): Call<Person>

    @Multipart
    @POST("api/v1/anons/ask")
    abstract fun uploadFileQuestion(@Part file: MultipartBody.Part,
                                    @Part("question1") questionOne: RequestBody,
                                    @Part("question2") questionTwo: RequestBody,
                                    @Part("question3") questionThree: RequestBody,
                                    @Part("question4") questionFour: RequestBody,
                                    @Part("email") email: RequestBody): Call<JsonElement>

    @Multipart
    @POST("/api/v1/auth/attachments/chat")
    abstract fun uploadChatFilePhoto(@Part files: ArrayList<MultipartBody.Part>,
                                     @Part("ChannelSid") channelSid: RequestBody): Call<JsonElement>

    @Multipart
    @POST("/api/user/bussiness_join")
    abstract fun uploadTTest(@Part files: ArrayList<MultipartBody.Part>,
                             @Part("data") data: RequestBody): Call<JsonElement>






}