/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.tododemo.taskdetail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dailystudio.devbricksx.development.Logger
import com.example.tododemo.R
import com.example.tododemo.data.DTask
import com.example.tododemo.data.Task
import com.example.tododemo.data.model.DTaskViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TaskDetailFragment : Fragment() {

    private lateinit var entryId: String

    private var titleView: EditText? = null
    private var descView: EditText? = null
    private var completedView: CheckBox? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.taskdetail_frag, container, false)

        setupViews(view)

        val args: TaskDetailFragmentArgs by navArgs()
        entryId = args.entryid
        Logger.debug("parsed id: $entryId")

        if (entryId.isNotBlank()) {
            lifecycleScope.launch(Dispatchers.IO) {
                val viewModel = ViewModelProvider(this@TaskDetailFragment)
                        .get(DTaskViewModel::class.java)

                val task = viewModel.getDTask(entryId)
                task?.let {
                    withContext(Dispatchers.Main) {
                        attachTask(it)
                    }
                }
            }
        }

        setHasOptionsMenu(true)
        return view
    }

    private fun attachTask(task: DTask) {
        titleView?.setText(task.title)
        descView?.setText(task.description)
        completedView?.isChecked = task.completed
    }

    private fun setupViews(view: View) {
        titleView = view.findViewById(R.id.title)
        descView = view.findViewById(R.id.description)
        completedView = view.findViewById(R.id.completed)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.menu_delete -> {
                if (entryId.isNotBlank()) {
                    deleteTask()
                }
                findNavController().popBackStack()
                true
            }

            R.id.menu_done -> {
                Logger.debug("entryId: $entryId")
                if (entryId.isBlank()) {
                    Logger.debug("create: $entryId")

                    createNewTask()
                } else {
                    Logger.debug("update: $entryId")

                    updateExistTask()
                }

                findNavController().popBackStack()
                true
            }
            else -> false
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.taskdetail_fragment_menu, menu)
    }

    private fun createNewTask() {
        val task = DTask().also {
            fillTaskWithUserInput(it)
        }

        Logger.debug("create a new task: $task")

        lifecycleScope.launch(Dispatchers.IO) {
            val viewModel = ViewModelProvider(this@TaskDetailFragment)
                .get(DTaskViewModel::class.java)

            viewModel.insertOrUpdateDTask(task)
        }
    }

    private fun updateExistTask() {
        val eId = entryId ?: return

        lifecycleScope.launch(Dispatchers.IO) {
            val viewModel = ViewModelProvider(this@TaskDetailFragment)
                    .get(DTaskViewModel::class.java)

            val task = viewModel.getDTask(eId) ?: return@launch

            fillTaskWithUserInput(task)

            Logger.debug("updated task: $task")

            viewModel.insertOrUpdateDTask(task)
        }
    }

    private fun deleteTask() {
        val eId = entryId ?: return

        lifecycleScope.launch(Dispatchers.IO) {
            val viewModel = ViewModelProvider(this@TaskDetailFragment)
                    .get(DTaskViewModel::class.java)

            val task = viewModel.getDTask(eId) ?: return@launch

            Logger.debug("delete task: $task")

            viewModel.deleteDTask(task)
        }
    }

    private fun fillTaskWithUserInput(task: DTask) {
        titleView?.let {
            task.title = it.text.toString()
        }

        descView?.let {
            task.description = it.text.toString()
        }

        completedView?.let {
            task.completed = it.isChecked
        }
    }


}

