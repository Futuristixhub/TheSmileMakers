package com.smilemakers.ui.dashBoard.profile

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.util.TypedValue
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.simplemobiletools.commons.extensions.toast
import com.smilemakers.R
import com.smilemakers.databinding.ActivityEditProfileBinding
import com.smilemakers.ui.dashBoard.patientFragment.PatientListener
import com.smilemakers.utils.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.kodein
import org.kodein.di.generic.instance
import java.io.IOException

class EditProfileActivity : AppCompatActivity(), KodeinAware, PatientListener {

    override val kodein by kodein()
    var binding: ActivityEditProfileBinding? = null
    private val factory: ProfileViewModelFactory by instance()
    var img: ImageView? = null
    var viewModel: ProfileVM? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        viewModel =
            ViewModelProviders.of(this, factory).get(ProfileVM::class.java)
        binding?.vm = viewModel
        viewModel?.authListener = this

        val bar: ActionBar? = supportActionBar
        if (bar != null) {
            val tv = TextView(applicationContext)
            val lp: ActionBar.LayoutParams = ActionBar.LayoutParams(
                ActionBar.LayoutParams.MATCH_PARENT,  // Width of TextView
                ActionBar.LayoutParams.WRAP_CONTENT
            ) // Height of TextView
            tv.layoutParams = lp
            tv.setText(getString(R.string.edit_profile))
            tv.setTextColor(Color.WHITE)

            val typedValue = TypedValue()
            resources.getValue(R.dimen.actionBar_text, typedValue, true)
            val myFloatValue = typedValue.float

            tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, myFloatValue)
            bar.setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM)
            bar.setDisplayHomeAsUpEnabled(true)
            bar.setCustomView(tv)
        }

        viewModel?.edt_fname = intent.getStringExtra("fname")
        viewModel?.edt_lname = intent.getStringExtra("lname")
        viewModel?.edt_mob_no = intent.getStringExtra("mobile")
        if (intent.getStringExtra("email").isNullOrEmpty()) {
            binding?.edEmail?.visibility = View.GONE
        }
        viewModel?.edt_email = intent.getStringExtra("email")
        viewModel?.edt_location = intent.getStringExtra("location")
        Glide.with(this).load(Uri.parse(intent.getStringExtra("image"))).into(binding?.ivImg!!)

        img = binding?.ivImg

        binding?.igImage!!.setOnClickListener {
            Dexter.withActivity(this)
                .withPermissions(
                    Manifest.permission.CAMERA,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                )
                .withListener(object : MultiplePermissionsListener {
                    override fun onPermissionsChecked(report: MultiplePermissionsReport) {
                        if (report.areAllPermissionsGranted()) {
                            showImagePickerOptions()
                        }
                        if (report.isAnyPermissionPermanentlyDenied) {
                            showSettingsDialog()
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(
                        permissions: List<PermissionRequest>,
                        token: PermissionToken
                    ) {
                        token.continuePermissionRequest()
                    }
                }).check()

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> onBackPressed()
            else -> return super.onOptionsItemSelected(item)
        }
        return true
    }

    private fun showSettingsDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.dialog_permission_title))
        builder.setMessage(getString(R.string.dialog_permission_message))
        builder.setPositiveButton(getString(R.string.go_to_settings)) { dialog, which ->
            dialog.cancel()
            openSettings()
        }
        builder.setNegativeButton(getString(android.R.string.cancel)) { dialog, which -> dialog.cancel() }
        builder.show()
    }

    private fun openSettings() {
        val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
        val uri = Uri.fromParts("package", this.getPackageName(), null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    private fun showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(
            this,
            object : ImagePickerActivity.PickerOptionListener {
                override fun onTakeCameraSelected() {
                    launchCameraIntent()
                }

                override fun onChooseGallerySelected() {
                    launchGalleryIntent()
                }
            })
    }

    private fun launchCameraIntent() {
        val intent = Intent(this, ImagePickerActivity::class.java)
        intent.putExtra(
            INTENT_IMAGE_PICKER_OPTION,
            REQUEST_IMAGE_CAPTURE
        )

        // setting aspect ratio
        intent.putExtra(INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(INTENT_ASPECT_RATIO_Y, 1)

        // setting maximum bitmap width and height
        intent.putExtra(INTENT_SET_BITMAP_MAX_WIDTH_HEIGHT, true)
        intent.putExtra(INTENT_BITMAP_MAX_WIDTH, 1000)
        intent.putExtra(INTENT_BITMAP_MAX_HEIGHT, 1000)
        startActivityForResult(intent, REQUEST_IMAGE)
    }


    private fun launchGalleryIntent() {
        val intent = Intent(this, ImagePickerActivity::class.java)
        intent.putExtra(
            INTENT_IMAGE_PICKER_OPTION,
            REQUEST_GALLERY_IMAGE
        )

        // setting aspect ratio
        intent.putExtra(INTENT_LOCK_ASPECT_RATIO, true)
        intent.putExtra(INTENT_ASPECT_RATIO_X, 1) // 16x9, 1x1, 3:4, 3:2
        intent.putExtra(INTENT_ASPECT_RATIO_Y, 1)
        startActivityForResult(intent, REQUEST_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                val uri = data!!.getParcelableExtra<Uri>("path")
                try {
                    if (uri.toString() != null || !(uri.toString().isEmpty())) {

                        loadImageWithUri(img!!, uri.toString())
                    }
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
    }

    fun loadImageWithUri(imageView: ImageView, imageUri: String) {

        Glide.with(imageView.context).load(Uri.parse(imageUri)).into(imageView)

        viewModel!!.image = imageUri
        imageView.setColorFilter(
            ContextCompat.getColor(
                imageView.context!!,
                android.R.color.transparent
            )
        )
    }

    override fun onStarted() {
        progress_bar.show()
    }

    override fun onSuccess(message: String) {
        progress_bar.hide()

        showErrorSnackBar(root_layout, message)
        toast(message)
        finish()
    }

    override fun onFailure(message: String) {
        progress_bar.hide()
        showErrorSnackBar(root_layout, message)
    }

}
