package ru.solom.flickrbrowser.main

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.activity.result.contract.ActivityResultContract

/**
 * An [ActivityResultContract] to prompt the user to select a path for creating a new
 * image, returning the [Uri] of the item that was created.
 * The input is the suggested name for the new file
 */
class ImageFileActivityContract : ActivityResultContract<String, Uri>() {
    override fun createIntent(context: Context, input: String?): Intent {
        val extension = input?.substringAfterLast('.') ?: "*"
        return Intent(Intent.ACTION_CREATE_DOCUMENT)
            .setType("image/$extension")
            .putExtra(Intent.EXTRA_TITLE, input)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Uri? {
        return if (intent == null || resultCode != Activity.RESULT_OK) null else intent.data
    }
}
