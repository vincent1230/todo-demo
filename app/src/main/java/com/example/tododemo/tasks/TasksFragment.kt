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

package com.example.tododemo.tasks

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.PopupMenu
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment.findNavController
import androidx.paging.PagedList
import androidx.recyclerview.widget.RecyclerView
import com.dailystudio.devbricksx.development.Logger
import com.example.tododemo.AboutFragment
import com.example.tododemo.R
import com.example.tododemo.data.DTask
import com.example.tododemo.data.fragment.DTasksListFragment
import com.example.tododemo.data.model.DTaskViewModel
import kotlin.math.min

class TasksFragment : DTasksListFragment() {

    private var filter = TasksFilterType.ALL_TASKS

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = super.onCreateView(inflater, container, savedInstanceState)

        view?.let {
            setupFragmentViews(it)
        }

        setHasOptionsMenu(true)
        return view
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.menu_about -> {
                val fragment = AboutFragment()

                fragment.show(parentFragmentManager, "about")

                true
            }

            R.id.menu_filter -> {
                showFilteringPopUpMenu()
                true
            }

            else -> false
        }

    private fun showFilteringPopUpMenu() {
        val view = activity?.findViewById<View>(R.id.menu_filter) ?: return
        PopupMenu(requireContext(), view).run {
            menuInflater.inflate(R.menu.filter_tasks, menu)

            setOnMenuItemClickListener {
                when (it.itemId) {
                    R.id.all -> filter = TasksFilterType.ALL_TASKS
                    R.id.active -> filter = TasksFilterType.ACTIVE_TASKS
                    R.id.completed -> filter = TasksFilterType.COMPLETED_TASKS
                }

                refresh()
                true
            }
            show()
        }
    }

    protected fun refresh() {
        applyBindings()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tasks_fragment_menu, menu)
    }

    private fun setupFragmentViews(fragmentView: View) {
        val fab: View? = fragmentView.findViewById(R.id.add_task_fab)

        fab?.setOnClickListener {
            findNavController(this).navigate(
                    TasksFragmentDirections.actionTasksFragmentDestToTasksFragmentEdit(
                            ""))
        }
    }

    override fun onItemClick(recyclerView: RecyclerView, itemView: View, position: Int, item: DTask, id: Long) {
        super.onItemClick(recyclerView, itemView, position, item, id)

        findNavController(this).navigate(
                TasksFragmentDirections.actionTasksFragmentDestToTasksFragmentEdit(
                        item.entryid))

    }

    override fun getLiveData(): LiveData<List<DTask>> {
        val viewModel = ViewModelProvider(this).get(
                DTaskViewModel::class.java)

        val tasksLive = viewModel.allDTasksLive

        return Transformations.switchMap(tasksLive) { tasks ->
            val filtered = mutableListOf<DTask>()

            for ((i, task) in tasks.withIndex()) {
                when (filter) {
                    TasksFilterType.ALL_TASKS -> {
                        filtered.add(task)
                    }

                    TasksFilterType.ACTIVE_TASKS -> {
                        if (!task.completed) {
                            filtered.add(task)
                        }
                    }

                    TasksFilterType.COMPLETED_TASKS -> {
                        if (task.completed) {
                            filtered.add(task)
                        }
                    }
                }

            }

            val ret = MutableLiveData<List<DTask>>(filtered)
            Logger.debug("ret: $ret")
            ret
        }
    }

}
