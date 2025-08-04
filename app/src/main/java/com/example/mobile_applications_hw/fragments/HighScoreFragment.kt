package com.example.mobile_applications_hw.fragments


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mobile_applications_hw.MenuActivity
import com.example.mobile_applications_hw.R
import com.example.mobile_applications_hw.ScoreManager
import com.example.mobile_applications_hw.adapters.ScoreAdapter
import com.example.mobile_applications_hw.interfaces.Callback_HighScoreItemClicked
import com.example.mobile_applications_hw.model.Score
import com.google.android.material.button.MaterialButton

class HighScoreFragment : Fragment() {

    private lateinit var highScore_BTN_main_menu: MaterialButton

    private lateinit var highScore_BTN_clear: MaterialButton

    private lateinit var highScore_RV_list: RecyclerView

    var score: MutableList<Score> = mutableListOf()
    private val scoreAdapter = ScoreAdapter(score)

    var highScoreItemClicked: Callback_HighScoreItemClicked? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v = inflater.inflate(R.layout.fragment_high_score, container, false)
        findViews(v)
        initViews(context)
        return v
    }

    private fun initViews(context: Context?) {
        if (context == null) {
            return
        }
        highScore_BTN_main_menu.setOnClickListener {
            val intent = Intent(activity, MenuActivity::class.java)
            startActivity(intent)
        }

        highScore_BTN_clear.setOnClickListener {
            ScoreManager.getInstance(requireContext()).clearScores()
            scoreAdapter.notifyDataSetChanged()
            updateScore()
        }

        highScore_RV_list.adapter = scoreAdapter
        scoreAdapter.callback = highScoreItemClicked
        highScore_RV_list.layoutManager = LinearLayoutManager(context)

    }

    private fun itemClicked(lat: Double, lon: Double) {
        highScoreItemClicked?.highScoreItemClicked(lat,lon)
    }

    private fun findViews(v: View) {
        highScore_BTN_main_menu = v.findViewById(R.id.highScore_BTN_main_menu)
        highScore_RV_list = v.findViewById(R.id.highScore_RV_list)
        highScore_BTN_clear = v.findViewById(R.id.highScore_BTN_clear)

    }

    fun updateScore() {
        val newScores = ScoreManager.getInstance(requireContext()).scores
        scoreAdapter.updateScores(newScores)
    }
}