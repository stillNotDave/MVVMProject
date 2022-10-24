package ru.sfedu.mvvmproject

import ru.sfedu.mvvmproject.model.User

interface Navigator {

    fun showDetails(user: User)

    fun goBack()

    fun showToast(messageRes: Int)
}