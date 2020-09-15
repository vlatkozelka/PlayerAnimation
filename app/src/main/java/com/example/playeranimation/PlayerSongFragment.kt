package com.example.playeranimation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.item_player_song.*

/**
 * Created by Ali on 9/15/2020.
 */
class PlayerSongFragment : Fragment() {

    var position = 0

    companion object {
        const val ARG_POSITION = "position"

        fun newInstance(position: Int): PlayerSongFragment {
            return PlayerSongFragment().apply {
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
        return inflater.inflate(R.layout.item_player_song, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        when (position) {
            0 -> iv_cover_art.setImageResource(R.drawable.car_bomb)
            1 -> iv_cover_art.setImageResource(R.drawable.car_bomb_meta)
            2 -> iv_cover_art.setImageResource(R.drawable.car_bomb_mordial)
        }
    }


}