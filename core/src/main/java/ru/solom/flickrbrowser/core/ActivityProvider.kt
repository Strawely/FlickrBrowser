package ru.solom.flickrbrowser.core

import androidx.appcompat.app.AppCompatActivity

interface ActivityProvider {
    val currentActivity: AppCompatActivity?
    fun requireActivity(): AppCompatActivity
}
