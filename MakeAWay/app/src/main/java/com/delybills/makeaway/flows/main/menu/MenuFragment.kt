package com.delybills.makeaway.flows.main.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.delybills.makeaway.App
import com.delybills.makeaway.R
import com.delybills.makeaway.common.BaseFragment
import com.delybills.makeaway.common.ViewModelFactory
import com.delybills.makeaway.databinding.FragmentMenuBinding
import com.delybills.makeaway.flows.main.menu.adapters.CalendarAdapter
import com.delybills.makeaway.flows.main.menu.adapters.DashboardAdapter
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

class MenuFragment : BaseFragment<MenuViewModel, MenuRepository, FragmentMenuBinding>() {

    private var navController: NavController? = null

    @Inject
    lateinit var viewModelFactory: ViewModelFactory

    lateinit var dashBoardLinearLayoutManager: LinearLayoutManager
    lateinit var dashBoardAdapter: DashboardAdapter
    lateinit var calendarAdapter: CalendarAdapter

    override fun provideBinding(
        inflater: LayoutInflater, container: ViewGroup?
    ): FragmentMenuBinding = FragmentMenuBinding.inflate(inflater, container, false)

    override fun provideViewModel(): MenuViewModel {
        (requireContext().applicationContext as App).appComponent.inject(this)
        return ViewModelProvider(this, viewModelFactory)[MenuViewModel::class.java]
    }

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)
        viewModel.getDesks()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navController = Navigation.findNavController(binding.root)

        setUpButtons()
        setUpRecyclerView()
        setUpCalendarView()
        observeResponse()
        observeErrorResponse()
        observeCalendarEvents()
    }

    private fun setUpButtons() {
        binding.addNewDeskButton.setOnClickListener {
            navController?.navigate(R.id.action_menuFragment_to_createDeskFragment)
        }
    }

    private fun getDashboards() {
        viewModel.getDesks()
    }

    private fun setUpRecyclerView() {
        dashBoardAdapter = DashboardAdapter {
            if (!it.name.isNullOrEmpty()) {
                val direction = MenuFragmentDirections.actionMenuFragmentToDeskFragment(it)
                navController?.navigate(direction)
            }
        }
        dashBoardLinearLayoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        with(binding.dashboardRecyclerView) {
            overScrollMode = View.OVER_SCROLL_NEVER
            layoutManager = dashBoardLinearLayoutManager
            adapter = dashBoardAdapter
        }
    }

    private fun setUpCalendarView(){
        calendarAdapter = CalendarAdapter{
            Toast.makeText(context, "Выберите доску \"${it.title}\" в секции выше", Toast.LENGTH_SHORT).show()
        }
        binding.weekView.adapter = calendarAdapter
    }

    private fun observeResponse() {
        lifecycleScope.launch {
            viewModel.messageDataGetDesks.collectLatest {
                dashBoardAdapter.data = it.desks.sortedBy { desk -> desk.deadline }
            }
        }
    }

    private fun observeCalendarEvents(){
        lifecycleScope.launch {
            viewModel.calendarEvents.collectLatest {
                calendarAdapter.submitList(it)
            }
        }
    }

    private fun observeErrorResponse() {
        viewModel.errorResponse.observe(viewLifecycleOwner) {
            if (it.isNetworkFailure) {
                showToast("Проверьте подключение к интернету")
            } else {
                showToast(it.body?.string() ?: "Непредвиденная ошибка")
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
}