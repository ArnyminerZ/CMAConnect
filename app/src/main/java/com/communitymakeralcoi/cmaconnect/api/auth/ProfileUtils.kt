package com.communitymakeralcoi.cmaconnect.api.auth

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import android.widget.Toast
import com.communitymakeralcoi.cmaconnect.R
import com.communitymakeralcoi.cmaconnect.api.utils.FileUtils
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.storage.FirebaseStorage
import java.io.File


private val tag = "ProfileUtils"
fun uploadProfileImage(user: FirebaseUser, imagePath: String, context: Context, view: View){
    val storage = FirebaseStorage.getInstance()
    val storageRef = storage.reference
    val extension = FileUtils.extensionFromStringPath(imagePath)
    val imageRef = storageRef.child("profileImages/${user.uid}.$extension")

    val file = Uri.fromFile(File(imagePath))

    val uploadTask = imageRef.putFile(file)
    val urlTask = uploadTask.continueWithTask { task ->
        if (!task.isSuccessful) {
            // TODO: Upload error
        }

        // Continue with the task to get the download URL
        imageRef.downloadUrl
    }.addOnCompleteListener { task ->
        if (task.isSuccessful) {
            val downloadUri = task.result

            val profileUpdates = UserProfileChangeRequest.Builder()
                    .setPhotoUri(downloadUri)
                    .build()

            user.updateProfile(profileUpdates)
                    .addOnCompleteListener { updateTask ->
                        if (updateTask.isSuccessful) {
                            Log.d(tag, "User profile updated.")
                            Toast.makeText(context, R.string.updated_profile, Toast.LENGTH_SHORT).show()
                        }
                    }
        } else {
            Log.d(tag, "There was an error when updating the user profile image.")
            Snackbar.make(view, R.string.error_profile, Snackbar.LENGTH_SHORT).show()
        }
    }
}

fun changeEmail(user: FirebaseUser, email: String, context: Context, view: View){
    user.updateEmail(email)
            .addOnCompleteListener {
                if (it.isSuccessful) {
                    Log.d(tag, "User email address updated.")
                    Toast.makeText(context, R.string.updated_profile, Toast.LENGTH_SHORT).show()
                }else{
                    Log.d(tag, "There was an error when updating the user email address.")
                    Snackbar.make(view, R.string.error_profile, Snackbar.LENGTH_SHORT).show()
                }
            }
}
fun changeUsername(user: FirebaseUser, username: String, context: Context, view: View){
    val profileUpdates = UserProfileChangeRequest.Builder()
            .setDisplayName(username)
            .build()

    user.updateProfile(profileUpdates)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(tag, "User profile updated.")
                    Toast.makeText(context, R.string.updated_profile, Toast.LENGTH_SHORT).show()
                }else{
                    Log.d(tag, "There was an error when updating the user username.")
                    Snackbar.make(view, R.string.error_profile, Snackbar.LENGTH_SHORT).show()
                }
            }
}