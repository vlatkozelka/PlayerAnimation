package com.example.playeranimation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_mini_player.*
import org.notests.sharedsequence.distinctUntilChanged
import org.notests.sharedsequence.drive
import org.notests.sharedsequence.map

/**
 * Created by Ali on 9/15/2020.
 */

class MiniPlayerFragment : Fragment() {

    val changeListener = object: ViewPager2.OnPageChangeCallback(){
        override fun onPageSelected(position: Int) {
            super.onPageSelected(position)
            MainActivity.emitNewState(MainActivity.appState.copy(currentPosition = position))
        }
    }

    val disposabes: ArrayList<Disposable> = arrayListOf()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_mini_player, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = object : FragmentStateAdapter(fragmentManager!!, activity!!.lifecycle) {
            override fun getItemCount(): Int {
                return 3
            }

            override fun createFragment(position: Int): Fragment {
                return MiniPlayerSongFragment.newInstance(position)
            }

        }


        pager_mini_player.adapter = adapter
    }

    override fun onResume() {
        super.onResume()
        pager_mini_player.registerOnPageChangeCallback(changeListener)
        disposabes.addAll(listOf(
            MainActivity.appStateDriver.map { it.currentPosition }.distinctUntilChanged().drive { pager_mini_player.setCurrentItem(it, true) }
        ))
    }

    override fun onPause() {
        super.onPause()
        pager_mini_player.unregisterOnPageChangeCallback(changeListener)
        disposabes.forEach { it.dispose() }
    }

}