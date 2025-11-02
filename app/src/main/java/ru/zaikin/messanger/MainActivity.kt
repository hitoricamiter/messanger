package ru.zaikin.messanger

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ListView
import android.widget.ProgressBar
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.*

class MainActivity : AppCompatActivity() {

    private lateinit var messageListView: ListView
    private lateinit var adapter: AwesomeMessageAdapter
    private lateinit var progressBar: ProgressBar
    private lateinit var sendImageButton: ImageButton
    private lateinit var sendMessageButton: Button
    private lateinit var messageEditText: EditText
    private lateinit var userName: String
    private lateinit var database: FirebaseDatabase
    private lateinit var myRef: DatabaseReference
    private lateinit var messagesChildEventListener: ChildEventListener

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Инициализация Firebase
        database = FirebaseDatabase.getInstance()
        myRef = database.getReference("message")

        // UI элементы
        progressBar = findViewById(R.id.progressBar)
        sendImageButton = findViewById(R.id.sendPhotoButton)
        sendMessageButton = findViewById(R.id.sendMessageButton)
        messageEditText = findViewById(R.id.messageEditText)
        messageListView = findViewById(R.id.messageListView)

        // Настройка имени пользователя
        userName = "Martin"

        // Настройка адаптера
        val list: MutableList<AwesomeMessage> = ArrayList()
        adapter = AwesomeMessageAdapter(this, R.layout.message_item, list)
        messageListView.adapter = adapter

        progressBar.visibility = ProgressBar.INVISIBLE

        // Включение/отключение кнопки отправки по тексту
        messageEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                sendMessageButton.isEnabled = s.toString().trim().isNotEmpty()
            }
            override fun afterTextChanged(s: Editable?) {}
        })

        // Отправка текста
        sendMessageButton.setOnClickListener {
            val message = AwesomeMessage(messageEditText.text.toString(), userName, null)
            myRef.push().setValue(message)
            messageEditText.setText("")
        }

        // Заглушка для отправки фото
        sendImageButton.setOnClickListener {
            // TODO: Реализовать отправку изображений
        }

        // Слушатель изменений в базе
        messagesChildEventListener = object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val message = snapshot.getValue(AwesomeMessage::class.java)
                if (message != null) {
                    adapter.add(message)
                    messageListView.smoothScrollToPosition(adapter.count - 1)
                }
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onChildRemoved(snapshot: DataSnapshot) {}
            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
            override fun onCancelled(error: DatabaseError) {}
        }

        myRef.addChildEventListener(messagesChildEventListener)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Убираем слушатель при закрытии Activity
        myRef.removeEventListener(messagesChildEventListener)
    }
}
