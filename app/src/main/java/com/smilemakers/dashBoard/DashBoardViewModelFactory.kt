package com.smilemakers.dashBoard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.smilemakers.dashBoard.dashBoardFragment.DashboardFragmentVM
import com.smilemakers.login.LoginVM
import com.smilemakers.login.UserRepository

@Suppress("UNCHECKED_CAST")
class DashBoardViewModelFactory(
    private val repository: DashBoardRepository
) : ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return DashboardFragmentVM(repository) as T
    }
}