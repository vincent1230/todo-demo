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
import androidx.fragment.app.Fragment
import com.example.tododemo.R

class TasksFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.tasks_frag, container, false)
        setHasOptionsMenu(true)
        return view
    }

    override fun onOptionsItemSelected(item: MenuItem) =
        when (item.itemId) {
            R.id.menu_clear -> {

                true
            }
            R.id.menu_filter -> {
                showFilteringPopUpMenu()
                true
            }
            R.id.menu_refresh -> {
                true
            }
            else -> false
        }

    private fun showFilteringPopUpMenu() {
        val view = activity?.findViewById<View>(R.id.menu_filter) ?: return
        PopupMenu(requireContext(), view).run {
            menuInflater.inflate(R.menu.filter_tasks, menu)

            setOnMenuItemClickListener {

                true
            }
            show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.tasks_fragment_menu, menu)
    }
}
