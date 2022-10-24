package ru.sfedu.mvvmproject.screens

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ru.sfedu.mvvmproject.R
import ru.sfedu.mvvmproject.UserActionListener
import ru.sfedu.mvvmproject.model.User
import ru.sfedu.mvvmproject.model.UsersListener
import ru.sfedu.mvvmproject.model.UsersService
import ru.sfedu.mvvmproject.tasks.*

data class UserListItem(
    val user: User,
    val isInProgress: Boolean
)

class UsersListViewModel(
    private val usersService: UsersService
) : BaseViewModel(), UserActionListener {

    private val _users = MutableLiveData<Result<List<UserListItem>>>()
    val users: LiveData<Result<List<UserListItem>>> = _users

    private val _actionShowDetails = MutableLiveData<Event<User>>()
    val actionShowDetails: LiveData<Event<User>> = _actionShowDetails

    private val _actionShowToast = MutableLiveData<Event<Int>>()
    val actionShowToast: LiveData<Event<Int>> = _actionShowToast

    private val userIdIsInProgress = mutableListOf<Long>()
    private var usersResult: Result<List<User>> = EmptyResult()
        set(value) {
            field = value
            notifyUpdates()
        }

    private val listener: UsersListener = {
        usersResult = if (it.isEmpty()) {
            EmptyResult()
        } else {
            SuccessResult(it)
        }
        //usersResult = SuccessResult(it)
        //_users.value = it
    }

    init {
        usersService.addListener(listener)
        loadUsers()
    }

    override fun onCleared() {
        super.onCleared()
        usersService.removeListener(listener)

    }

    fun loadUsers() {
        usersResult = PendingResult()
        usersService.loadUsers().onError {
                usersResult = ErrorResult(it)
            }
            .autoCancel()
    }

//    fun moveUser(user: User, moveBy: Int) {
//        if (isInProgress(user)) return
//        addProgressTo(user)
//        usersService.moveUser(user, moveBy)
//            .onSuccess {
//            removeProgressFrom(user)
//        }
//            .onError {
//                removeProgressFrom(user)
//            }
//            .autoCancel()
//    }

//    fun deleteUser(user: User) {
//        if (isInProgress(user)) return
//        addProgressTo(user)
//        usersService.deleteUser(user)
//            .onSuccess {
//                removeProgressFrom(user)
//            }
//            .onError {
//                removeProgressFrom(user)
//            }
//            .autoCancel()
//    }

//    fun showDetails(user: User) {
//        _actionShowDetails.value = Event(user)
//    }

    private fun addProgressTo(user: User) {
        userIdIsInProgress.add(user.id)
        notifyUpdates()
    }

    private fun removeProgressFrom(user: User) {
        userIdIsInProgress.remove(user.id)
        notifyUpdates()
    }

    private fun isInProgress(user: User): Boolean {
        return userIdIsInProgress.contains(user.id)
    }

    private fun notifyUpdates() {
        _users.postValue(usersResult.map { users ->
            users.map { user -> UserListItem(user, isInProgress(user)) }
        })
    }

    override fun onUserMove(user: User, moveBy: Int) {
        if (isInProgress(user)) return
        addProgressTo(user)
        usersService.moveUser(user, moveBy)
            .onSuccess {
                removeProgressFrom(user)
            }
            .onError {
                removeProgressFrom(user)
                _actionShowToast.value = Event(R.string.cant_move_user)
            }
            .autoCancel()
    }

    override fun onUserDelete(user: User) {
        if (isInProgress(user)) return
        addProgressTo(user)
        usersService.deleteUser(user)
            .onSuccess {
                removeProgressFrom(user)
            }
            .onError {
                removeProgressFrom(user)
                _actionShowToast.value = Event(R.string.cant_delete_user)
            }
            .autoCancel()
    }

    override fun onUserDetails(user: User) {
        _actionShowDetails.value = Event(user)
    }
}