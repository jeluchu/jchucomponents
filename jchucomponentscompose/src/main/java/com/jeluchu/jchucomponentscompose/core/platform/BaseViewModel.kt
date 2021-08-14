package com.jeluchu.jchucomponentscompose.core.platform

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.jeluchu.jchucomponentscompose.core.exception.Failure

abstract class BaseViewModel : ViewModel() {

    var failure: MutableLiveData<Failure> = MutableLiveData()
    var showSpinner: MutableLiveData<Boolean> = MutableLiveData()

    protected fun handleShowSpinner(show: Boolean) {
        this.showSpinner.value = show
    }

    protected fun handleFailure(failure: Failure) {
        this.failure.value = failure
    }

}