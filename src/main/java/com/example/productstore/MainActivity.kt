package com.example.productstore
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {

    private lateinit var imgView: ImageView
    private lateinit var editText1: EditText
    private lateinit var editText2: EditText
    private lateinit var editText3: EditText
    private lateinit var btnSave: Button
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ItemAdapter
    private lateinit var dbHelper: DatabaseHelper

    private var selectedImageUri: Uri? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        imgView = findViewById(R.id.imgView)
        editText1 = findViewById(R.id.editText1)
        editText2 = findViewById(R.id.editText2)
        editText3 = findViewById(R.id.editText3)
        btnSave = findViewById(R.id.btnSave)
        recyclerView = findViewById(R.id.recyclerView)
        dbHelper = DatabaseHelper(this)

        val getImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            selectedImageUri = uri
            imgView.setImageURI(uri)
        }

        imgView.setOnClickListener {
            getImage.launch("image/*")
        }

        adapter = ItemAdapter { item -> editItem(item) }
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        loadItems()

        btnSave.setOnClickListener {
            saveItem()
        }
    }

    private fun loadItems() {
        val items = dbHelper.getAllItems()
        adapter.submitList(items)
    }

    private fun saveItem() {
        val imageUri = selectedImageUri?.toString() ?: ""
        val text1 = editText1.text.toString()
        val text2 = editText2.text.toString()
        val text3 = editText3.text.toString()

        val item = Item(0, imageUri, text1, text2, text3)
        dbHelper.insertItem(item)
        loadItems()

        editText1.text.clear()
        editText2.text.clear()
        editText3.text.clear()
        imgView.setImageURI(null)
        selectedImageUri = null
    }

    private fun editItem(item: Item) {
        val intent = Intent(this, EditItemActivity::class.java)
        intent.putExtra("item_id", item.id)
        startActivity(intent)
    }
}
