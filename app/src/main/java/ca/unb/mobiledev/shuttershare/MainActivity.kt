package ca.unb.mobiledev.shuttershare

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Rational
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.AspectRatio
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.core.UseCaseGroup
import androidx.camera.core.ViewPort
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.concurrent.futures.await
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import ca.unb.mobiledev.shuttershare.databinding.ActivityMainBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference

    private lateinit var cameraController: LifecycleCameraController
    private lateinit var preview: Preview
    private lateinit var imageCapture: ImageCapture

    private var activeEventsList = arrayOf("Cancun 2023", "Andy's Wedding", "Nationals 2023")
    private lateinit var autoCompleteTextView: AutoCompleteTextView

    private var sharedPrefs: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        database = FirebaseDatabase.getInstance().getReference("Test")
        storage = FirebaseStorage.getInstance().getReference("TestEvent")

        setContentView(viewBinding.root)

        // Creating/setting up the SharedPreference file
        sharedPrefs = getSharedPreferences("ShutterShareData", MODE_PRIVATE)

        // BOTTOM NAVIGATION SETUP
        val bottomNavigationView: BottomNavigationView = findViewById(R.id.bottomNavigationView)
        bottomNavigationView.selectedItemId = R.id.bottom_home
        bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
               R.id.bottom_home -> true
               R.id.bottom_album -> {
                   startActivity(Intent(this@MainActivity, AlbumActivity::class.java))
                   overridePendingTransition(0,0)
                   finish() // Not really sure what this does...
                   true
               }
               else -> false
            }
        }


        // CAMERA PERMISSIONS CHECK
        // Checking if permissions were granted in a previous session
        if(!hasPermissions(baseContext)) {
            // request camera-related permissions
            activityResultLauncher.launch(REQUIRED_PERMISSIONS)
        } else {
            lifecycleScope.launch {
                startCamera()
            }
        }


        // ACTIVE EVENTS LIST
        autoCompleteTextView = viewBinding.autoCompleteTextview
        var adapterItems = ArrayAdapter<String>(this, R.layout.list_item, activeEventsList)
        autoCompleteTextView.setAdapter(adapterItems)

//        autoCompleteTextView.setOnClickListener {
//            var item =
//        }


        viewBinding.imageCaptureButton.setOnClickListener { takePhoto() }

//        viewBinding.loginScreenButton.setOnClickListener {
//            val intent = Intent(this, LoginScreen::class.java)
//            startActivity(intent)
//        }

        //Firebase Test code
        //Toast.makeText(this, "Firebase Connection Successful", Toast.LENGTH_SHORT).show()

//        val firstName = "John"
//        val lastName = "Smith"
//        val age = "33"
//        val userName = "jsmith"


//        val test = Test(firstName, lastName, age, userName)
//        database.child(userName).setValue(test).addOnSuccessListener {
//            Toast.makeText(this, "Successfully Saved", Toast.LENGTH_SHORT).show()
//        }.addOnFailureListener {
//            Toast.makeText(this, "Failed to Save", Toast.LENGTH_SHORT).show()
//        }

    }

    private suspend fun startCamera() {
        val previewView: PreviewView = viewBinding.viewFinder

        val cameraProvider = ProcessCameraProvider.getInstance(this).await()

        preview = Preview.Builder().setTargetAspectRatio(AspectRatio.RATIO_16_9).build()
        preview.setSurfaceProvider(viewBinding.viewFinder.surfaceProvider)

        imageCapture = ImageCapture.Builder().setTargetAspectRatio(AspectRatio.RATIO_16_9).build()

        val aspectRatio = Rational(previewView.width, previewView.height)

        val viewPort =
            ViewPort.Builder(aspectRatio, preview.targetRotation).setScaleType(ViewPort.FIT).build()

        val useCaseGroup = UseCaseGroup.Builder()
            .setViewPort(viewPort)
            .addUseCase(preview)
            .addUseCase(imageCapture)
            .build()

        val cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

        try {
            cameraProvider.unbindAll()
            // Note: we can implement zoom, focus, and flash, but as of right now it doesn't do that
            var camera = cameraProvider.bindToLifecycle(this, cameraSelector, useCaseGroup)//preview, imageCapture)

        } catch (e: Exception) {
            Log.e(TAG, "UseCase binding failed", e)
        }


//        cameraController = LifecycleCameraController(baseContext)
//        cameraController.bindToLifecycle(this)
//        cameraController.cameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA // sets the selfie camera the default
//        previewView.controller = cameraController
    }

    private fun takePhoto() {
        // Create time stamped name and MediaStore entry
//        val name = SimpleDateFormat(FILENAME_FORMAT, Locale.CANADA)
//            .format(System.currentTimeMillis())

        // Setting up the content values for MediaStore to save the photos to the device
        // MAY WANT TO CHANGE THIS LATER ON TO THE CLOUD STUFF...?
//        val contentValues = ContentValues().apply {
//            put(MediaStore.MediaColumns.DISPLAY_NAME, name)
//            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
//            if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
//                put(MediaStore.MediaColumns.RELATIVE_PATH, "Pictures/ShutterShare-Image")
//            }
//        }

        // Create output options object which contains file + metadata
//        val outputOptions = ImageCapture.OutputFileOptions
//            .Builder(contentResolver,
//                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
//                contentValues)
//            .build()

        // Set up image capture listener, which is triggered after photo has been taken
        // right now when a picture is taken, it uploads to firebase cloud
        imageCapture.takePicture(
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun  onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)

                    // grabbing the image from memory and converting it to Byte data
//                    var bitmap: Bitmap = image.convertImageProxyToBitmap()
//                    val baos = ByteArrayOutputStream()
//                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
//                    val data = baos.toByteArray()

                    var intent = Intent(this@MainActivity, PicturePreview::class.java)

                    val extras = ExtendedDataHolder.instance
                    extras.putExtra("PicturePreviewImage", image)
                    extras.putExtra("ImageViewRotation", image.imageInfo.rotationDegrees.toFloat())
                    //intent.putExtra("PicturePreviewData", data)
                    //intent.putExtra("ImageViewRotation", image.imageInfo.rotationDegrees.toFloat())
                    //intent.putExtra("EventSelected", selectedEvent)
                    // putExtra for picture metadata???
                    Log.e("AppDebug", "Before start activity")
                    startActivity(intent)
                    overridePendingTransition(0,0)

                    //image.close()

                    // START --------(can probably be moved to PicturePreview Activity)------------------------------------------
                    // setting the folder location in Firebase
//                    val imageRef = storage.child("test.jpg")
//
//                    var toastText = ""
//
//                    // upload the image (Byte data) to Firebase cloud
//                    var uploadTask = imageRef.putBytes(data)
//                    uploadTask.addOnFailureListener {
//                        toastText = "Failure to Upload"
//                    }.addOnSuccessListener { taskSnapshot ->
//                        // taskSnapshot.metadata contains file metadata such as size, content-type, etc.
//                        toastText = "Successful Upload"
//                    }.addOnCanceledListener {
//                        toastText = "Canceled Upload"
//                    }.addOnCompleteListener {
//                        toastText = "Upload Complete"
//                    }
//
//                    Toast.makeText(this@MainActivity, toastText, Toast.LENGTH_SHORT).show()
                    // END -------------------------------------------------------


                }
            }
        )
    }

//    fun ImageProxy.convertImageProxyToBitmap(): Bitmap {
//        val buffer = planes[0].buffer
//        buffer.rewind()
//        val bytes = ByteArray(buffer.capacity())
//        buffer.get(bytes)
//        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
//    }

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions())
        { permissions ->
            // Handle Permission granted/rejected
            var permissionGranted = true

            // check if all required permissions were granted
            permissions.entries.forEach {
                if (it.key in REQUIRED_PERMISSIONS && it.value == false)
                    permissionGranted = false
            }
            if(!permissionGranted) {
                Toast.makeText(this, "Permission request denied", Toast.LENGTH_LONG).show()
            } else {
                lifecycleScope.launch {
                    startCamera()
                }
            }
        }

    companion object {
        private const val TAG = "ShutterShare"
        private const val FILENAME_FORMAT = "yyyy-MM-dd-HH-mm-ss-SSS" // may change in the future...
        private val REQUIRED_PERMISSIONS =
            mutableListOf (
                android.Manifest.permission.CAMERA
            ).apply {
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.P) {
                    add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
                }
            }.toTypedArray()
        fun hasPermissions(context: Context) = REQUIRED_PERMISSIONS.all {
            ContextCompat.checkSelfPermission(context, it) == PackageManager.PERMISSION_GRANTED
        }
    }
}