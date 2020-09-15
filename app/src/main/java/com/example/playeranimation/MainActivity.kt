package com.example.playeranimation

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.motion.widget.MotionLayout
import io.reactivex.disposables.Disposable
import io.reactivex.subjects.BehaviorSubject
import kotlinx.android.synthetic.main.activity_main.*
import org.notests.sharedsequence.*

data class AppState(
    val currentPosition: Int = 0,
    val isPanelUp: Boolean = false
)

class MainActivity : AppCompatActivity() {

    lateinit var ivCoverArt: ImageView

    companion object {
        var appState = AppState()
        private val appStateSubject = BehaviorSubject.create<AppState>()
        val appStateDriver = appStateSubject.asDriver {
            Driver.just(AppState())
        }

        fun emitNewState(newState: AppState) {
            appState = newState
            appStateSubject.onNext(appState)
        }
    }


    val disposables: ArrayList<Disposable> = arrayListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        ivCoverArt = findViewById(R.id.iv_cover_art)

        with(supportFragmentManager) {

            beginTransaction()
                .replace(R.id.player_container, PlayerFragment())
                .commit()

            beginTransaction()
                .replace(R.id.miniplayer_container, MiniPlayerFragment())
                .commit()
        }

        root_layout.addTransitionListener(object : MotionLayout.TransitionListener {
            override fun onTransitionTrigger(p0: MotionLayout?, p1: Int, p2: Boolean, p3: Float) {

            }

            override fun onTransitionStarted(p0: MotionLayout?, p1: Int, p2: Int) {

            }

            override fun onTransitionChange(p0: MotionLayout?, p1: Int, p2: Int, p3: Float) {
                Log.d("ALI", p3.toString())

                if(p2 == R.id.end){
                    if (p3 > 0.975f){
                        emitNewState(appState.copy(isPanelUp = true))
                    }else{
                        emitNewState(appState.copy(isPanelUp = false))
                    }
                }else if(p2 == R.id.start){
                    if (p3 < 0.025f){
                        emitNewState(appState.copy(isPanelUp = true))
                    }else{
                        emitNewState(appState.copy(isPanelUp = false))
                    }
                }
            }

            override fun onTransitionCompleted(p0: MotionLayout?, p1: Int) {
                println("p1 is $p1, start is ${R.id.start}, end is ${R.id.end}")
                if (p1 == R.id.start) {
                    emitNewState(appState.copy(isPanelUp = false))
                } else if (p1 == R.id.end) {
                    emitNewState(appState.copy(isPanelUp = true))
                }
            }

        })

    }

    override fun onResume() {
        super.onResume()
        disposables.addAll(listOf(
            appStateDriver.map { it.currentPosition }.distinctUntilChanged().drive {
                when (it) {
                    0 -> ivCoverArt.setImageResource(R.drawable.car_bomb)
                    1 -> ivCoverArt.setImageResource(R.drawable.car_bomb_meta)
                    2 -> ivCoverArt.setImageResource(R.drawable.car_bomb_mordial)
                }

            }
        ))
    }

    override fun onPause() {
        super.onPause()
        disposables.forEach { it.dispose() }
    }

}
