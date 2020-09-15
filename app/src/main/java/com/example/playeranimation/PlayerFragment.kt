package com.example.playeranimation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_player.*
import kotlinx.android.synthetic.main.fragment_player.tv_song_name
import kotlinx.android.synthetic.main.item_miniplayer_song.*
import org.notests.sharedsequence.distinctUntilChanged
import org.notests.sharedsequence.drive
import org.notests.sharedsequence.map

/**
 * Created by Ali on 9/15/2020.
 */

class PlayerFragment : Fragment() {

    val changeListener = object : ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            MainActivity.emitNewState(MainActivity.appState.copy(currentPosition = position))
        }
    }

    val disposables: ArrayList<Disposable> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = object : FragmentStateAdapter(fragmentManager!!, activity!!.lifecycle) {
            override fun getItemCount(): Int {
                return 3
            }

            override fun createFragment(position: Int): Fragment {
                return PlayerSongFragment.newInstance(position)
            }

        }
        pager_songs.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        pager_songs.registerOnPageChangeCallback(changeListener)
        disposables.addAll(
            listOf(
                MainActivity.appStateDriver.drive { Log.d("AppState", it.toString()) },
                MainActivity.appStateDriver.map { it.currentPosition }.distinctUntilChanged().drive {
                    pager_songs.setCurrentItem(it)
                    when (it) {
                        0 -> tv_song_name.setText("Car Bomb")
                        1 -> tv_song_name.setText("Car Bomb Meta")
                        2 -> tv_song_name.setText("Car Bomb Mordial")
                    }
                },
                MainActivity.appStateDriver.map { it.isPanelUp }.distinctUntilChanged().drive {
                    if(it){
                        pager_songs.visibility = View.VISIBLE
                    }else{
                        pager_songs.visibility = View.INVISIBLE
                    }
                }
                )

        )
    }

    override fun onPause() {
        super.onPause()
        pager_songs.unregisterOnPageChangeCallback(changeListener)
        disposables.forEach { it.dispose() }
    }
}