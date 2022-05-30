package com.example.projectanonym.ui.home

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.ActivityNotFoundException
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.projectanonym.CameraPermissionsDialogFragment
import com.example.projectanonym.databinding.FragmentHomeBinding

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    // Declare the imageView1 as a var for later global use
    // ( lateinit indicates a later initialization )
    lateinit var imageView1: ImageView

    // Declare request codes for the startActivityForResult function
    private val requestCodeCamera = 1
    private val requestCodeGallery = 2

    // Declare a Boolean to check whether the user responded positively to the Dialog
    private var userDialogPositiveResponse = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeViewModel =
            ViewModelProvider(this)[HomeViewModel::class.java]

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //val textView: TextView = binding.textHome
        //homeViewModel.text.observe(viewLifecycleOwner, Observer {
        //    textView.text = it
        //})

        // Binding the objects in order to be able to call on them in later code
        imageView1 = binding.imageView1

        val buttonChooseImage: Button = binding.buttonChooseImage
        val buttonCapturePhoto: Button = binding.ButtonCapturePhoto
        val buttonAnonymize: Button = binding.ButtonAnonymize

        // Button listeners to make the buttons functional
        buttonChooseImage.setOnClickListener {
            Log.d("buttonChooseImage","Clicked")
            requestOpenGalleryPermission()
        }
        buttonCapturePhoto.setOnClickListener {
            Log.d("buttonCapturePhoto","Clicked")
            // Check for Camera permission first
            requestCameraPermission()
        }
        buttonAnonymize.setOnClickListener { Log.d("buttonAnonymize","Clicked") }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    /*
        Functions for the app
     */

    // Register the permissions callback, which handles the user's response to the
    // system permissions dialog. Save the return value, an instance of
    // ActivityResultLauncher. You can use either a val, as shown in this snippet,
    // or a lateinit var in your onAttach() or onCreate() method.
    private val requestPermissionLauncher =
        registerForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { isGranted: Boolean ->
            if (isGranted) {
                // Permission is granted. Continue the action or workflow in your
                // app.
            } else {
                // Explain to the user that the feature is unavailable because the
                // features requires a permission that the user has denied. At the
                // same time, respect the user's decision. Don't link to system
                // settings in an effort to convince the user to change their
                // decision.
            }
        }

    // fun for recommended process of checking for a permission,
    // and requesting a permission from the user when necessary
    private fun requestCameraPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                dispatchTakePictureIntent()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.CAMERA
            ) -> {
            // In an educational UI, explain to the user why your app requires this
            // permission for a specific feature to behave as expected. In this UI,
            // include a "cancel" or "no thanks" button that allows the user to
            // continue using your app without granting the permission.
            //TODO: Needs a dialog in case the permission was denied
                Log.e("PERMISSION", "NOT GRANTED")
                //Show the CameraPermissionsDialog
                showCameraPermissionsDialog()
                //If user responded positively, call the permission request again
                if (userDialogPositiveResponse)
                    requestPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    // fun for recommended process of checking for a permission,
    // and requesting a permission from the user when necessary
    private fun requestOpenGalleryPermission() {
        when {
            ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED -> {
                // You can use the API that requires the permission.
                dispatchOpenGalleryIntent()
            }
            ActivityCompat.shouldShowRequestPermissionRationale(
                requireActivity(),
                Manifest.permission.READ_EXTERNAL_STORAGE
            ) -> {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected. In this UI,
                // include a "cancel" or "no thanks" button that allows the user to
                // continue using your app without granting the permission.
                //TODO: Needs a dialog in case the permission was denied
                Log.e("PERMISSION", "NOT GRANTED")
            }
            else -> {
                // You can directly ask for the permission.
                // The registered ActivityResultCallback gets the result of this request.
                requestPermissionLauncher.launch(Manifest.permission.READ_EXTERNAL_STORAGE)
            }
        }
    }

    // Function for capturing a picture
    private fun dispatchTakePictureIntent() {
        val takePictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        try {
            startActivityForResult(takePictureIntent, requestCodeCamera)
        } catch (e: ActivityNotFoundException) {
            // Display error state to the user
        }
    }

    // Function for opening gallery and selecting an image
    private fun dispatchOpenGalleryIntent() {
        val openGalleryIntent = Intent("android.intent.action.GET_CONTENT")
        openGalleryIntent.type = "image/*"
        try {
            startActivityForResult(openGalleryIntent, requestCodeGallery)
        } catch (e: ActivityNotFoundException) {
            // Display error state to the user
        }
    }

    // Function for showing CameraPermissionsDialog
    private fun showCameraPermissionsDialog() {
        val newFragment = CameraPermissionsDialogFragment()
        newFragment.show(requireFragmentManager(), "Camera")
        //Set userDialogPositiveResponse to false in case the dialog was called before
        userDialogPositiveResponse = false
        //Check whether the user responded positively to the dialog
        if (newFragment.userRespondedPositively) {
            userDialogPositiveResponse = true
        }
    }

    // onActivityResult function
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        when (requestCode) {
            requestCodeCamera ->
                if (requestCode == requestCodeCamera && resultCode == RESULT_OK) {
                    val imageBitmap = data?.extras?.get("data") as Bitmap
                    imageView1.setImageBitmap(imageBitmap)
                }
            requestCodeGallery ->
                if (requestCode == requestCodeGallery && resultCode == RESULT_OK) {
                    val imageBitmap = data?.data
                    imageView1.setImageURI(imageBitmap)
                }
        }
    }
}