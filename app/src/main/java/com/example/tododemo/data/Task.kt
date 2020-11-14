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
package com.example.tododemo.data

import android.R.attr.x
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.view.View
import android.widget.TextView
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.dailystudio.devbricksx.annotations.*
import com.dailystudio.devbricksx.ui.AbsSingleLineViewHolder
import com.dailystudio.devbricksx.utils.ResourcesCompatUtils
import com.example.tododemo.R
import java.util.*


@Entity(tableName = "tasks")
data class Task @JvmOverloads constructor(
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "completed") var isCompleted: Boolean = false,
    @PrimaryKey @ColumnInfo(name = "entryid") var id: String = UUID.randomUUID().toString()
) {

    val titleForList: String
        get() = if (title.isNotEmpty()) title else description

    val isActive
        get() = !isCompleted

    val isEmpty
        get() = title.isEmpty() || description.isEmpty()
}

@Adapter(viewType = ViewType.SingleLine, viewHolder = DTaskViewHolder::class, paged = false)
@ListFragment(layout = R.layout.fragment_tasks)
@ViewModel
@RoomCompanion(
    primaryKeys = ["entryid"]
)
class DTask(
    @JvmField val entryid: String = UUID.randomUUID().toString(),
    @JvmField var title: String = "",
    @JvmField var description: String = "",
    @JvmField var completed: Boolean = false
)


class DTaskViewHolder(itemView: View) : AbsSingleLineViewHolder<DTask>(itemView) {

    override fun getIcon(item: DTask): Drawable? {
        val resId = if (item.completed) {
            R.drawable.ic_done
        } else {
            R.drawable.ic_todo
        }
        return ResourcesCompatUtils.getDrawable(
            itemView.context,
            resId
        )
    }

    override fun bindText(item: DTask, titleView: TextView?) {
        super.bindText(item, titleView)

        val textView = titleView ?: return
        if (item.completed) {
            textView.paintFlags = textView.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            textView.paintFlags = textView.paintFlags and (Paint.STRIKE_THRU_TEXT_FLAG).inv()
        }
    }

    override fun getText(item: DTask): CharSequence? {
        return item.title?.capitalize()
    }

}
