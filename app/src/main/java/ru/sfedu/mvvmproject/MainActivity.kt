package ru.sfedu.mvvmproject

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.support.v4.os.IResultReceiver.Default
import android.widget.Toast
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import ru.sfedu.mvvmproject.databinding.ActivityMainBinding
import ru.sfedu.mvvmproject.model.User
import ru.sfedu.mvvmproject.model.UsersListener
import ru.sfedu.mvvmproject.model.UsersService
import ru.sfedu.mvvmproject.screens.UserDetailsFragment
import ru.sfedu.mvvmproject.screens.UsersListFragment

class MainActivity : AppCompatActivity(), Navigator {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragmentContainer, UsersListFragment())
                .commit()
        }

    }

    override fun showDetails(user: User) {
        supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.fragmentContainer, UserDetailsFragment.newInstance(user.id))
            .commit()
    }

    override fun goBack() {
        onBackPressed()
    }

    override fun showToast(messageRes: Int) {
        Toast.makeText(this, messageRes, Toast.LENGTH_SHORT).show()
    }
}