package com.smilemakers.ui.dashBoard.profile

import android.app.Application
import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.smilemakers.R
import com.smilemakers.ui.dashBoard.patientFragment.PatientListener
import com.smilemakers.utils.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.File


class ProfileVM(val repository: ProfileRepository, application: Application) : ViewModel() {

    var name: String? = null
    var subname: String? = null
    var mob_no: String? = null
    var email: String? = null
    var location: String? = null
    var image: String? = null
    var authListener: PatientListener? = null

    var edt_fname: String? = null
    var edt_lname: String? = null
    var edt_subname: String? = null
    var edt_mob_no: String? = null
    var edt_email: String? = null
    var edt_location: String? = null

    var edt_cpwd: String? = null
    var edt_npwd: String? = null
    var edt_cfpwd: String? = null

    var profileListener: ProfileListener? = null
    var data: Profile? = null

    fun onEditClick(view: View) {
        view.context.startActivity(
            Intent(view.context, EditProfileActivity::class.java)
                .putExtra("fname", data?.fname)
                .putExtra("lname", data?.lname)
                .putExtra("mobile", data?.mobile)
                .putExtra("email", data?.email)
                .putExtra("location", data?.address)
                .putExtra("image", data?.image)
        )
    }

    var context = application.applicationContext

    fun getData(uid: String?, utype: String?) {
        profileListener!!.onStarted()
        Coroutines.main {
            try {
                val authResponse = repository.getProfileData(uid!!, utype!!)
                authResponse.data?.let {
                    if (authResponse.status == false) {
                        profileListener?.onFailure(authResponse.message!!)
                    } else {
                        profileListener?.onSuccess(it)
                        this.data = it
                        return@main
                    }
                }
                profileListener?.onFailure(authResponse.message!!)
            } catch (e: ApiExceptions) {
                profileListener?.onFailure(e.message!!)
            } catch (e: NoInternetException) {
                profileListener?.onFailure(e.message!!)
            }
        }
    }

    fun onCallClick(view: View) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:" + data?.mobile)
        view.context.startActivity(intent)
    }

    fun onEmailClick(view: View) {
        val emailIntent = Intent(
            Intent.ACTION_SENDTO, Uri.fromParts(
                "mailto", data?.email, null
            )
        )
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Subject")
        emailIntent.putExtra(Intent.EXTRA_TEXT, "Body")
        view.context.startActivity(Intent.createChooser(emailIntent, "Send email..."))
    }

    fun onMapClick(view: View) {
        val mapUri = Uri.parse("geo:0,0?q=" + Uri.encode(data?.address))
        val mapIntent = Intent(Intent.ACTION_VIEW, mapUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        view.context.startActivity(mapIntent)
    }

    fun onChangePwdClick(view: View) {
        view.context.startActivity(Intent(view.context, ChangePasswordActivity::class.java))
    }

    fun onSubmitClick(view: View) {
        view.context.hideKeyboard(view)
        if (isValid(view)) {
            authListener!!.onStarted()
          //  Coroutines.io {
                val validimg = image?.contains("file")
                if (validimg!!) {
                    Coroutines.main {
                        try {
                            val file = File(Uri.parse(image).path)
                            var requestBody =
                                RequestBody.create(MediaType.parse("image/jpeg"), file)
                            var filePart =
                                MultipartBody.Part.createFormData("image", file.name, requestBody)

                            val authResponse =
                                repository.editProfile(
                                    RequestBody.create(
                                        MediaType.parse("text/plain"),
                                        context!!.getData(
                                            context!!,
                                            context.getString(R.string.user_id)
                                        )
                                    ),
                                    RequestBody.create(
                                        MediaType.parse("text/plain"),
                                        context!!.getData(
                                            context!!,
                                            context.getString(R.string.user_type)
                                        )
                                    ),
                                    RequestBody.create(MediaType.parse("text/plain"), edt_fname!!),
                                    RequestBody.create(MediaType.parse("text/plain"), edt_lname!!),
                                    RequestBody.create(MediaType.parse("text/plain"), edt_email!!),
                                    RequestBody.create(
                                        MediaType.parse("text/plain"),
                                        edt_location!!
                                    ),
                                    filePart, requestBody
                                )
                            if (authResponse.status == false) {
                                authListener?.onFailure(authResponse.message!!)
                            } else {
                                authListener?.onSuccess(authResponse.message!!)
                                return@main
                            }
                            authListener?.onFailure(authResponse.message!!)
                        } catch (e: ApiExceptions) {
                            authListener?.onFailure(e.message!!)
                        } catch (e: NoInternetException) {
                            authListener?.onFailure(e.message!!)
                        }
                    }
                } else {
                    Coroutines.main {
                        try {
                            val authResponse =
                                repository.editProfileI(
                                    context!!.getData(
                                        context!!,
                                        context.getString(R.string.user_id)
                                    )!!,
                                    context!!.getData(
                                        context!!,
                                        context.getString(R.string.user_type)
                                    )!!,
                                    edt_fname!!,
                                    edt_lname!!,
                                    edt_email!!,
                                    edt_location!!,
                                    image!!
                                )
                            if (authResponse.status == false) {
                                authListener?.onFailure(authResponse.message!!)
                            } else {
                                authListener?.onSuccess(authResponse.message!!)
                                return@main
                            }
                            authListener?.onFailure(authResponse.message!!)
                        } catch (e: ApiExceptions) {
                            authListener?.onFailure(e.message!!)
                        } catch (e: NoInternetException) {
                            authListener?.onFailure(e.message!!)
                        }
                    }
                }
           // }
        }
    }

    fun isValid(view: View): Boolean {

        if (image.isNullOrEmpty()) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_image))
            return false
        }
        if (edt_fname == null || edt_fname!!.isEmpty()) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_fname))
            return false
        }
        if (edt_lname == null || edt_lname!!.isEmpty()) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_lname))
            return false
        }
        if (edt_location == null || edt_location!!.isEmpty()) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_location))
            return false
        }

        return true
    }

    fun onCPSubmitClick(view: View) {
        view.context.hideKeyboard(view)
        if (isCPValid(view)) {
            (view.context as AppCompatActivity).finish()
        }
    }

    fun isCPValid(view: View): Boolean {

        if (edt_cpwd == null || edt_cpwd!!.isEmpty()) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_cpwd))
            return false
        }
        if (edt_npwd == null || edt_npwd!!.isEmpty()) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_npwd))
            return false
        }
        if (edt_cfpwd == null || edt_cfpwd!!.isEmpty()) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.empty_cfpwd))
            return false
        }
        if (!edt_npwd.equals(edt_cfpwd)) {
            view.context.showErrorSnackBar(view, view.context.getString(R.string.pwd_match_error))
            return false
        }

        return true
    }
}