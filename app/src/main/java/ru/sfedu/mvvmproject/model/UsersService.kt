package ru.sfedu.mvvmproject.model

import com.github.javafaker.Faker
import ru.sfedu.mvvmproject.UserNotFoundException
import ru.sfedu.mvvmproject.tasks.SimpleTask
import ru.sfedu.mvvmproject.tasks.Task
import java.util.*
import java.util.concurrent.Callable

typealias UsersListener = (users: List<User>) -> Unit

class UsersService {

    private var users = mutableListOf<User>()
    private var loaded = false
    private val listeners = mutableListOf<UsersListener>()

    init {

    }

    fun loadUsers(): Task<Unit> = SimpleTask<Unit>(Callable {
        Thread.sleep(2000)
        val faker = Faker.instance()
        IMAGES.shuffle()
        users = (1..100).map {
            User(
                id = it.toLong(),
                name = faker.name().firstName(),
                company = faker.company().name(),
                photo = IMAGES[it % IMAGES.size]
            )
        }.toMutableList()
        loaded = true
        notifyChanges()
    })


    fun getById(id: Long): Task<UserDetails> = SimpleTask<UserDetails>(Callable {
        Thread.sleep(2000)
        val user = users.firstOrNull { it.id == id } ?: throw UserNotFoundException()
        return@Callable UserDetails(
            user = user,
            details = Faker.instance().lorem().paragraphs(3).joinToString("\n\n")
        )
    })

    fun deleteUser(user: User): Task<Unit> = SimpleTask<Unit>(Callable {
        Thread.sleep(2000)
        val indexToDelete = users.indexOfFirst { it.id == user.id }
        if (indexToDelete != -1) {
            //users = ArrayList(users)
            users.removeAt(indexToDelete)
            notifyChanges()
        }
    })

//    fun fireUser(user: User) {
//        val index = users.indexOfFirst { it.id == user.id }
//        if (index == -1) return
//        val updatedUser = users[index].copy(company = "")
//        //users[index].company = ""
//        users = ArrayList(users)
//        users[index] = updatedUser
//        notifyChanges()
//    }

    fun moveUser(user: User, moveBy: Int): Task<Unit> = SimpleTask<Unit>(Callable {
        Thread.sleep(2000)
        val oldIndex = users.indexOfFirst { it.id == user.id }
        if (oldIndex == -1) return@Callable
        val newIndex = oldIndex + moveBy
        if (newIndex < 0 || newIndex >= users.size) return@Callable
        //users = ArrayList(users)
        Collections.swap(users, oldIndex, newIndex)
        notifyChanges()
    })

    fun addListener(listener: UsersListener) {
        listeners.add(listener)
        if (loaded) {
            listener.invoke(users)
        }
    }

    fun removeListener(listener: UsersListener) {
        listeners.remove(listener)
    }

    private fun notifyChanges() {
        if (!loaded) return
        listeners.forEach { it.invoke(users) }
    }

    companion object {
        private val IMAGES = mutableListOf(
            "https://images.unsplash.com/photo-1662810144427-db6f49da41a7?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxODY2Nzh8MHwxfHJhbmRvbXx8fHx8fHx8fDE2NjUwMzQ3MjA&ixlib=rb-1.2.1&q=80&w=1080",
            "https://images.unsplash.com/photo-1663946396353-71bf01b00e29?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxODY2Nzh8MHwxfHJhbmRvbXx8fHx8fHx8fDE2NjUwMzQ3NjE&ixlib=rb-1.2.1&q=80&w=1080",
            "https://images.unsplash.com/photo-1663138763894-0cc4a5421dab?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxODY2Nzh8MHwxfHJhbmRvbXx8fHx8fHx8fDE2NjUwMzQ4MDc&ixlib=rb-1.2.1&q=80&w=1080",
            "https://images.unsplash.com/photo-1662589537825-892db53c0556?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxODY2Nzh8MHwxfHJhbmRvbXx8fHx8fHx8fDE2NjUwMzQ4MjE&ixlib=rb-1.2.1&q=80&w=1080",
            "https://images.unsplash.com/photo-1664361238220-9d6bfb7ff41a?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxODY2Nzh8MHwxfHJhbmRvbXx8fHx8fHx8fDE2NjUwMzU1Mzc&ixlib=rb-1.2.1&q=80&w=1080",
            "https://images.unsplash.com/photo-1660160473454-d3781dd93755?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxODY2Nzh8MHwxfHJhbmRvbXx8fHx8fHx8fDE2NjUwMzU1NTE&ixlib=rb-1.2.1&q=80&w=1080",
            "https://images.unsplash.com/photo-1662904162853-cfc087b8d621?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxODY2Nzh8MHwxfHJhbmRvbXx8fHx8fHx8fDE2NjUwMzU1NTk&ixlib=rb-1.2.1&q=80&w=1080",
            "https://images.unsplash.com/photo-1663249741533-70d7c14368bf?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxODY2Nzh8MHwxfHJhbmRvbXx8fHx8fHx8fDE2NjUwMzU1ODI&ixlib=rb-1.2.1&q=80&w=1080",
            "https://images.unsplash.com/photo-1663601453912-0733a877cbc1?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxODY2Nzh8MHwxfHJhbmRvbXx8fHx8fHx8fDE2NjUwMzU1OTI&ixlib=rb-1.2.1&q=80&w=1080",
            "https://images.unsplash.com/photo-1663856542282-bf5647286f63?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxODY2Nzh8MHwxfHJhbmRvbXx8fHx8fHx8fDE2NjUwMzU2MDQ&ixlib=rb-1.2.1&q=80&w=1080",
            "https://images.unsplash.com/photo-1662752296095-82ebd797ae45?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxODY2Nzh8MHwxfHJhbmRvbXx8fHx8fHx8fDE2NjUwMzU2MTQ&ixlib=rb-1.2.1&q=80&w=1080",
            "https://images.unsplash.com/photo-1662996846527-faf9e5e98b17?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxODY2Nzh8MHwxfHJhbmRvbXx8fHx8fHx8fDE2NjUwMzU2Mzk&ixlib=rb-1.2.1&q=80&w=1080",
            "https://images.unsplash.com/photo-1663052666136-9f9409b2ff90?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxODY2Nzh8MHwxfHJhbmRvbXx8fHx8fHx8fDE2NjUwMzU2NDY&ixlib=rb-1.2.1&q=80&w=1080",
            "https://images.unsplash.com/photo-1662496167390-2006fed3bf45?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxODY2Nzh8MHwxfHJhbmRvbXx8fHx8fHx8fDE2NjUwMzU2NTU&ixlib=rb-1.2.1&q=80&w=1080",
            "https://images.unsplash.com/photo-1662497299871-9c59c874c274?crop=entropy&cs=tinysrgb&fit=max&fm=jpg&ixid=MnwxODY2Nzh8MHwxfHJhbmRvbXx8fHx8fHx8fDE2NjUwMzU2NjM&ixlib=rb-1.2.1&q=80&w=1080",
            "https://images.unsplash.com/photo-1600267185393-e158a98703de?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0NjQ0&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1579710039144-85d6bdffddc9?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0Njk1&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1488426862026-3ee34a7d66df?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0ODE0&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1620252655460-080dbec533ca?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0NzQ1&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1613679074971-91fc27180061?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0NzUz&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1485795959911-ea5ebf41b6ae?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0NzU4&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1545996124-0501ebae84d0?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0NzY1&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/flagged/photo-1568225061049-70fb3006b5be?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0Nzcy&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1567186937675-a5131c8a89ea?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0ODYx&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800",
            "https://images.unsplash.com/photo-1546456073-92b9f0a8d413?crop=entropy&cs=tinysrgb&fit=crop&fm=jpg&h=600&ixid=MnwxfDB8MXxyYW5kb218fHx8fHx8fHwxNjI0MDE0ODY1&ixlib=rb-1.2.1&q=80&utm_campaign=api-credit&utm_medium=referral&utm_source=unsplash_source&w=800"
        )
    }

}