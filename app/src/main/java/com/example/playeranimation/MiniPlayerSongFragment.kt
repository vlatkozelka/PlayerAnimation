package com.example.playeranimation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.item_miniplayer_song.*

/**
 * Created by Ali on 9/15/2020.
 */
class MiniPlayerSongFragment : Fragment() {

    var position = 0

    companion object {
        const val ARG_POSITION = "position"

        fun newInstance(position: Int): MiniPlayerSongFragment {
            return MiniPlayerSongFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_POSITION, position)
                }
            }
        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        position = arguments?.getInt(ARG_POSITION, 0) ?: 0
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.item_miniplayer_song, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (position) {
            0 -> tv_song_name.setText("Car Bomb")
            1 -> tv_song_name.setText("Car Bomb Meta")
            2 -> tv_song_name.setText("Car Bomb Mordial")
        }
    }


}