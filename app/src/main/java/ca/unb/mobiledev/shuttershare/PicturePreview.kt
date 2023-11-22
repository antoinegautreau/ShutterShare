package ca.unb.mobiledev.shuttershare

import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Matrix
import android.media.Image
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.ImageProxy
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.ByteArrayOutputStream


class PicturePreview : AppCompatActivity() {
    private lateinit var mImageView: ImageView
    private lateinit var mCloseButton: ImageButton
    private lateinit var mSendButton: ImageButton

    private lateinit var sharePrefs: SharedPreferences

    private lateinit var database: DatabaseReference
    private lateinit var storage: StorageReference

    @androidx.annotation.OptIn(androidx.camera.core.ExperimentalGetImage::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_picture_preview)

        sharePrefs = getSharedPreferences("ShutterShareData", MODE_PRIVATE)
        // May want to use sharedPrefs to fill in the DB and Storage values?? Or will these always be the same?
        database = FirebaseDatabase.getInstance().getReference("Test")
        storage = FirebaseStorage.getInstance().getReference("TestEvent")

        mImageView = findViewById(R.id.picture_preview)
        mCloseButton = findViewById(R.id.close_picture_button)
        mSendButton = findViewById(R.id.send_picture_button)


        // Set picture
        val extras = ExtendedDataHolder.instance
        var bitmap: Bitmap? = null
        if(extras.hasExtra("PicturePreviewImage")) {
            val image: ImageProxy = extras.getExtra("PicturePreviewImage") as ImageProxy
            bitmap = image.image!!.convertImageProxyToBitmap()


            //var croppedImage = cropImage(bitmap, mImageView, mImageView)
            //val imageBitmap = BitmapFactory.decodeByteArray(croppedImage, 0, croppedImage!!.size)



            //val baos = ByteArrayOutputStream()
            //bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            //val data = baos.toByteArray()
            //val imageBitmap = BitmapFactory.decodeByteArray(croppedImage, 0, croppedImage!!.size)

            //val imageViewRotation: Float = extras.getExtra("ImageViewRotation") as Float
            image.close()

            mImageView.rotation = 360 - image.imageInfo.rotationDegrees.toFloat() // not sure if doing 360 - rotation is the best thing, but it works for now
            mImageView.setImageBitmap(flip(bitmap))

            //mImageView.set
        }
        else {
            finish()
        }

        // Close picture and go back to Camera view (MainActivity)
        mCloseButton.setOnClickListener {
            finish()
        }

        mSendButton.setOnClickListener {
            // setting the folder location in Firebase???
            val imageRef = storage.child("test.jpg")

            // Converting the image bitmap to byte array
            val baos = ByteArrayOutputStream()
            bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, baos)
            val data = baos.toByteArray()

            // upload the image (Byte data) to Firebase cloud
            imageRef.putBytes(data)
//            var uploadTask = imageRef.putBytes(data)

            finish()

        }



//        val extras = intent.extras
//        if (extras != null) {
////            val pictureByteData = extras.getByteArray("PicturePreviewData")
////            val imageBitmap = BitmapFactory.decodeByteArray(pictureByteData, 0, pictureByteData!!.size)
//
//            val imageViewRotation = extras.getFloat("ImageViewRotation")
//            mImageView.rotation = imageViewRotation
////
////            mImageView = findViewById(R.id.picture_preview)
////            mImageView.setImageBitmap(imageBitmap) //BitmapFactory.decodeFile("pathToImageFile")
//        }
//        else { // error occured in the extras transfer/picture taking process, go back to MainActivity
//            startActivity(Intent(this@PicturePreview, MainActivity::class.java))
//        }



    }

    fun Image.convertImageProxyToBitmap(): Bitmap {
        val buffer = planes[0].buffer
        buffer.rewind()
        val bytes = ByteArray(buffer.capacity())
        buffer.get(bytes)
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
    }

    fun flip(src: Bitmap): Bitmap? {
        // create new matrix for transformation
        val matrix = Matrix()
        matrix.preScale(-1.0f, 1.0f)

        // return transformed image
        return Bitmap.createBitmap(src, 0, 0, src.width, src.height, matrix, true)
    }

    private fun cropImage(bitmap: Bitmap, frame: View, reference: View): ByteArray {
        val heightOriginal = frame.height
        val widthOriginal = frame.width
        val heightFrame = reference.height
        val widthFrame = reference.width
        val leftFrame = reference.left
        val topFrame = reference.top
        val heightReal = bitmap.height
        val widthReal = bitmap.width
        val widthFinal = widthFrame * widthReal / widthOriginal
        val heightFinal = heightFrame * heightReal / heightOriginal
        val leftFinal = leftFrame * widthReal / widthOriginal
        val topFinal = topFrame * heightReal / heightOriginal
        val bitmapFinal = Bitmap.createBitmap(
            bitmap,
            leftFinal, topFinal, widthFinal, heightFinal
        )
        val stream = ByteArrayOutputStream()
        bitmapFinal.compress(
            Bitmap.CompressFormat.JPEG,
            100,
            stream
        ) //100 is the best quality possibe
        return stream.toByteArray()
    }
}