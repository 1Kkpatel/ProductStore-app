package com.example.productstore
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity

class EditItemActivity : AppCompatActivity() {

    private lateinit var imgView: ImageView
    private lateinit var editText1: EditText
    private lateinit var editText2: EditText
    private lateinit var editText3: EditText
    private lateinit var btnSave: Button
    private lateinit var dbHelper: DatabaseHelper

    private var selectedImageUri: Uri? = null
    private var itemId: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_item)

        imgView = findViewById(R.id.imgView)
        editText1 = findViewById(R.id.editText1)
        editText2 = findViewById(R.id.editText2)
        editText3 = findViewById(R.id.editText3)
        btnSave = findViewById(R.id.btnSave)
        dbHelper = DatabaseHelper(this)

        itemId = intent.getLongExtra("item_id", 0)
        val item = dbHelper.getAllItems().find { it.id == itemId }

        item?.let {
            selectedImageUri = Uri.parse(it.imageUri)
            imgView.setImageURI(selectedImageUri)
            editText1.setText(it.text1)
            editText2.setText(it.text2)
            editText3.setText(it.text3)
        }

        val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            selectedImageUri = uri
            imgView.setImageURI(uri)
        }

        imgView.setOnClickListener {
            getImage.launch("image/*")
        }

        btnSave.setOnClickListener {
            updateItem()
        }
    }

    private fun updateItem() {
        val imageUri = selectedImageUri?.toString() ?: ""
        val text1 = editText1.text.toString()
        val text2 = editText2.text.toString()
        val text3 = editText3.text.toString()

        val item = Item(itemId, imageUri, text1, text2, text3)
        dbHelper.updateItem(item)
        finish()
    }
}
