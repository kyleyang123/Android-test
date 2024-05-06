package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myapplication.ui.theme.MyApplicationTheme
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database
import com.google.firebase.database.FirebaseDatabase

class MainActivity2 : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ChatScreen()
                }
            }
        }
    }
}

@Composable
fun ChatScreen() {
    val currentUser = FirebaseAuth.getInstance().currentUser
    val database = Firebase.database
    val messagesRef = database.reference.child("messages")

    var messageText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Bottom
    )

    {
        TextField(
            value = messageText,
            onValueChange = { messageText = it },
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Send
            ),
            keyboardActions = KeyboardActions(
                onSend = {
                    if (messageText.isNotBlank()) {
                        sendMessage(currentUser?.displayName ?: "Anonymous", messageText, messagesRef)
                        messageText = ""
                    }
                }
            ),
            label = { Text("Type your message...") }
        )
        Button(
            onClick = {
                if (messageText.isNotBlank()) {
                    sendMessage(currentUser?.displayName ?: "Anonymous", messageText, messagesRef)
                    messageText = ""
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Send")
        }
    }

    val messages by remember {
        mutableStateOf(
            listOf(
                Message("John", "Hello!"),
                Message("Alice", "Hi there!"),
                Message("Bob", "Hey!")
            )
        )
    }

    Column(
        modifier = Modifier.verticalScroll(rememberScrollState())
    ) {
        messages.forEach { message ->
            MessageItem(message = message)
        }
    }
}

@Composable
fun MessageItem(message: Message) {
    Surface(
        modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp)
            .shadow(4.dp, shape = RoundedCornerShape(8.dp)), // 添加陰影
    ) {
        Text(
            text = "${message.sender}: ${message.content}",
            modifier = Modifier.padding(8.dp)
        )
    }
}

data class Message(val sender: String, val content: String)

fun sendMessage(sender: String, content: String, messagesRef: DatabaseReference) {
    val message = mapOf(
        "sender" to sender,
        "content" to content
    )
    messagesRef.push().setValue(message)
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        ChatScreen()
    }
}