package com.yoma.yomahackathon

import android.app.Application
import android.content.Context

class YomaHackathonApp: Application() {

    companion object {
        var ctx: Context? = null
    }

    override fun onCreate() {
        super.onCreate()
        ctx = applicationContext
    }
}