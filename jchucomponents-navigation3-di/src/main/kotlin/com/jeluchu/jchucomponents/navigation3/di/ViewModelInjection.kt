package com.jeluchu.jchucomponents.navigation3.di

import androidx.compose.runtime.Composable
import androidx.lifecycle.ViewModel
import androidx.navigation3.runtime.NavKey
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
inline fun <reified T : ViewModel> inject(): T = koinViewModel()

@Composable
inline fun <reified T : ViewModel> injectByParams(key: NavKey): T =
    koinViewModel { parametersOf(key) }

@Composable
inline fun <reified T : ViewModel> injectByParams(vararg params: Any?): T =
    koinViewModel { parametersOf(*params) }
