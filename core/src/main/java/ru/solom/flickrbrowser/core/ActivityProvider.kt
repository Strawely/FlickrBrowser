package ru.solom.flickrbrowser.core

import androidx.appcompat.app.AppCompatActivity

interface ActivityProvider {
    var currentActivity: AppCompatActivity?
    fun requireActivity(): AppCompatActivity
}
