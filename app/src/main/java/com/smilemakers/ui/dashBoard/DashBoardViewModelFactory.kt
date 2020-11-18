package com.smilemakers.ui.dashBoard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smilemakers.ui.dashBoard.dashBoardFragment.DashboardFragmentVM

@Suppress("UNCHECKED_CAST")
class DashBoardViewModelFactory(
    private val repository: DashBoardRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DashboardFragmentVM(repository) as T
    }
}