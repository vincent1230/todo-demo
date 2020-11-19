package com.example.tododemo.tasks

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import com.example.tododemo.R
import com.example.tododemo.data.DTask
import com.example.tododemo.data.model.DTaskViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TasksActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.tasks_activity)

        setupViews()
    }

    private fun setupViews() {
        val fab: View? = findViewById(R.id.add_task_fab)

        fab?.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(
                R.id.action_tasks_fragment_dest_to_tasks_fragment_edit)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return findNavController(R.id.nav_host_fragment).navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }

}