package com.example.tododemo

import com.dailystudio.devbricksx.fragment.AbsAboutFragment

class AboutFragment : AbsAboutFragment() {

    override val appThumbResource: Int
        get() = R.drawable.app_thumb
    override val appDescription: CharSequence?
        get() = getString(R.string.app_desc)
    override val appIconResource: Int
        get() = R.mipmap.ic_launcher
    override val appName: CharSequence?
        get() = getString(R.string.app_name)


}