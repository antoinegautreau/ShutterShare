package ca.unb.mobiledev.shuttershare

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.Rational
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.view.MenuItem
import android.widget.Button
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
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var viewBinding: ActivityMainBinding
    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference

    private lateinit var cameraController: LifecycleCameraController
    private lateinit var preview: Preview
    private lateinit var imageCapture: ImageCapture
    private var lensFacing: CameraSelector = CameraSelector.DEFAULT_FRONT_CAMERA

    private lateinit var eventsSpinner: Spinner
    private var activeEventsList = arrayOf("Cancun 2023", "Andy's Wedding", "Nationals 2023")

    private var sharedPrefs: SharedPreferences? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewBinding = ActivityMainBinding.inflate(layoutInflater)
        database = FirebaseDatabase.getInstance().getReference("Test")
        //storage = FirebaseStorage.getInstance().getReference("TestEvent")

        setContentView(viewBinding.root)

        // Creating/setting up the SharedPreference file
        sharedPrefs = getSharedPreferences("ShutterShareData", MODE_PRIVATE)

        // Events Spinner (Event list) setup
        eventsSpinner = viewBinding.eventsSpinner
        val arrayAdapter = ArrayAdapter<String>(this, R.layout.spinner_text, activeEventsList)
        arrayAdapter.setDropDownViewResource(R.layout.spinner_dropdown)
        eventsSpinner.adapter = arrayAdapter
        eventsSpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, index: Int, id: Long) {
                Toast.makeText(this@MainActivity, "Event Selected: " + activeEventsList[index], Toast.LENGTH_SHORT).show()
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

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


        
        //Create/Join event button handling
        val createEventBtn: Button = findViewById(R.id.event_create_button)
        createEventBtn.setOnClickListener {
            startActivity(Intent(this@MainActivity, JoinCreateEventActivity::class.java))
//            val joinEventFragment = JoinEventFragment()
//            val fragment: Fragment? = supportFragmentManager.findFragmentByTag(JoinEventFragment::class.java.simpleName)
//
//            if (fragment !is JoinEventFragment){
//                supportFragmentManager.beginTransaction()
//                    .add()
//            }
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



        viewBinding.imageCaptureButton.setOnClickListener { takePhoto() }

        viewBinding.flipCameraButton.setOnClickListener { flipCamera() }




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

        try {
            cameraProvider.unbindAll()
            // Note: we can implement zoom, focus, and flash, but as of right now it doesn't do that
            var camera = cameraProvider.bindToLifecycle(this, lensFacing, useCaseGroup)//preview, imageCapture)

        } catch (e: Exception) {
            Log.e(TAG, "UseCase binding failed", e)
        }
    }

    private fun flipCamera() {
        if (lensFacing === CameraSelector.DEFAULT_FRONT_CAMERA)
            lensFacing = CameraSelector.DEFAULT_BACK_CAMERA
        else if (lensFacing === CameraSelector.DEFAULT_BACK_CAMERA)
            lensFacing = CameraSelector.DEFAULT_FRONT_CAMERA

        lifecycleScope.launch {
            startCamera()
        }
    }

    private fun takePhoto() {
        imageCapture.takePicture(
            ContextCompat.getMainExecutor(this),
            object : ImageCapture.OnImageCapturedCallback() {
                override fun onError(exc: ImageCaptureException) {
                    Log.e(TAG, "Photo capture failed: ${exc.message}", exc)
                }

                override fun  onCaptureSuccess(image: ImageProxy) {
                    super.onCaptureSuccess(image)

                    var intent = Intent(this@MainActivity, PicturePreview::class.java)

                    val extras = ExtendedDataHolder.instance
                    extras.putExtra("PicturePreviewImage", image)
                    extras.putExtra("ImageViewRotation", image.imageInfo.rotationDegrees.toFloat())
                    //intent.putExtra("PicturePreviewData", data)
                    //intent.putExtra("ImageViewRotation", image.imageInfo.rotationDegrees.toFloat())
                    //intent.putExtra("EventSelected", selectedEvent)
                    // putExtra for picture metadata???
                    startActivity(intent)
                    overridePendingTransition(0,0)
                }
            }
        )
    }

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