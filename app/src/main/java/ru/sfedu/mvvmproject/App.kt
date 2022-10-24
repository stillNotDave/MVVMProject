package ru.sfedu.mvvmproject

import android.app.Application
import ru.sfedu.mvvmproject.model.UsersService

class App : Application() {
    val usersService = UsersService()
}