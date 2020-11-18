package com.smilemakers.ui.dashBoard.patientFragment.addPatient


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.bumptech.glide.Glide
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener

import com.smilemakers.R
import com.smilemakers.ui.dashBoard.patientFragment.PatientFragmentVM
import com.smilemakers.ui.dashBoard.patientFragment.PatientViewModelFactory
import com.smilemakers.databinding.FragmentAddPatientBinding
import com.smilemakers.utils.*
import org.kodein.di.KodeinAware
import org.kodein.di.android.x.kodein
import org.kodein.di.generic.instance
import java.io.IOException

/**
 * A simple [Fragment] subclass.
 */
class AddPatientFragment : Fragment(), KodeinAware {

    override val kodein by kodein()

    companion object {
        //   lateinit var mActivity: DashboardActivity
        var fragment: AddPatientFragment? = null

        fun newInstance(): AddPatientFragment? {
            //  this.mActivity = mActivity
            if (fragment == null)
                fragment = AddPatientFragment()
            val bundle = Bundle()
            fragment?.arguments = bundle
            return fragment
        }
    }

    var binding: FragmentAddPatientBinding? = null
    private val factory: PatientViewModelFactory by instance()
    var img: ImageView? = null
    var viewModel: PatientFragmentVM? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_add_patient, container, false)
        viewModel =
            ViewModelProviders.of(this, factory).get(PatientFragmentVM::class.java)
        binding?.vm = viewModel
        binding?.lifecycleOwner = this

        img = binding?.ivImg

        binding?.igImage!!.setOnClickListener {
            Dexter.withActivity(context as Activity)
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

        return binding?.root
    }

    private fun showSettingsDialog() {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context!!)
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
        val uri = Uri.fromParts("package", context!!.getPackageName(), null)
        intent.data = uri
        startActivityForResult(intent, 101)
    }

    private fun showImagePickerOptions() {
        ImagePickerActivity.showImagePickerOptions(
            context!!,
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
        val intent = Intent(context, ImagePickerActivity::class.java)
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
        val intent = Intent(context, ImagePickerActivity::class.java)
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
                        context!!.saveData(
                            context!!,
                            context!!.getString(R.string.image),
                            uri.toString()
                        )
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

    override fun onViewStateRestored(savedInstanceState: Bundle?) {
        super.onViewStateRestored(savedInstanceState)

        val imgstr = context!!.getData(context!!, context!!.getString(R.string.image))
        if (imgstr != null && !(imgstr.isEmpty())) {
            loadImageWithUri(img!!, imgstr)
        }
    }
}
