package com.delybills.makeaway.flows.main.pomodoro

import android.app.AlertDialog
import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.delybills.makeaway.App
import com.delybills.makeaway.common.BaseFragment
import com.delybills.makeaway.common.ViewModelFactory
import com.delybills.makeaway.databinding.FragmentPomodoroBinding
import javax.inject.Inject

class PomodoroFragment :
    BaseFragment<PomodoroViewModel, PomodoroRepository, FragmentPomodoroBinding>() {

    private var navController: NavController? = null

    private var pomodoroTimer: CountDownTimer? = null
    private var timerRunning = false

    var time: Long = 25 * 60 * 1000

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    override fun provideBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentPomodoroBinding = FragmentPomodoroBinding.inflate(inflater, container, false)

    override fun provideViewModel(): PomodoroViewModel {
        (requireContext().applicationContext as App).appComponent.inject(this)
        return ViewModelProvider(this, viewModelFactory)[PomodoroViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(view)
        setUpPomodoroTimer()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("TIME", time)
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        time = savedInstanceState?.getLong("TIME") ?: 0L
        if (time < 25 * 60 * 1000 && time != 0L) {
            startTimer()
            timerRunning = true
            binding.actionButton.text = "Остановить"
            binding.resetButton.visibility = View.VISIBLE
        } else {
            time = 25 * 60 * 1000
            binding.timerTextView.text = "25:00"
            binding.resetButton.visibility = View.GONE
            timerRunning = false
            binding.actionButton.text = "Начать"
        }
    }

    private fun setUpPomodoroTimer() {
        binding.actionButton.setOnClickListener {
            if (!timerRunning) {
                startTimer()
                timerRunning = true
                binding.actionButton.text = "Остановить"
                binding.resetButton.visibility = View.VISIBLE
            } else {
                pomodoroTimer?.cancel()
                timerRunning = false
                binding.actionButton.text = "Продолжить"
            }
        }

        binding.resetButton.setOnClickListener {
            pomodoroTimer?.cancel()
            time = 25 * 60 * 1000
            binding.timerTextView.text = "25:00"
            binding.resetButton.visibility = View.GONE
            timerRunning = false
            binding.actionButton.text = "Начать"
        }
    }

    private fun startTimer() {
        pomodoroTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = millisUntilFinished / 1000 % 60
                if (seconds in 0..9) {
                    binding.timerTextView.text = "$minutes:0$seconds"
                } else {
                    binding.timerTextView.text = "$minutes:$seconds"}
                time = millisUntilFinished
            }

            override fun onFinish() {
                pomodoroTimer?.cancel()
                timerRunning = false
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage("Начать перерыв?")
                    .setPositiveButton("Да") { _, _ -> startBreakTimer() }
                    .setNegativeButton("Нет") { _, _ ->

                        startTimer()
                        timerRunning = true
                        binding.actionButton.text = "Остановить"
                    }
                    .show()
            }
        }
        pomodoroTimer?.start()
    }

    private fun startBreakTimer() {
        val timerLength: Long = 5 * 60 * 1000
        pomodoroTimer = object : CountDownTimer(timerLength, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                val minutes = millisUntilFinished / 1000 / 60
                val seconds = millisUntilFinished / 1000 % 60
                if (seconds in 0..9) {
                    binding.timerTextView.text = "$minutes:0$seconds"
                } else {
                    binding.timerTextView.text = "$minutes:$seconds"
                }

            }

            override fun onFinish() {
                pomodoroTimer?.cancel()
                timerRunning = false
                val builder = AlertDialog.Builder(requireContext())
                builder.setMessage("Начать сессию?")
                    .setPositiveButton("Да") { _, _ -> startTimer() }
                    .setNegativeButton("Нет") { _, _ -> }
                    .show()
            }
        }
        pomodoroTimer?.start()
    }


    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}