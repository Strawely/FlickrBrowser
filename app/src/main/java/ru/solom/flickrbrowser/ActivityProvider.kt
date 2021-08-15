package ru.solom.flickrbrowser

import androidx.appcompat.app.AppCompatActivity

interface ActivityProvider {
    val currentActivity: AppCompatActivity?
    fun requireActivity(): AppCompatActivity
}
