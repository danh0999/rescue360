package com.example.prm392_project;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.example.prm392_project.R;

public class ChatActivity extends AppCompatActivity {

    private LinearLayout linearLayoutMessages;
    private EditText editTextMessage;
    private ImageButton buttonSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roomchat);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tin nhắn");

        linearLayoutMessages = findViewById(R.id.linearLayoutMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editTextMessage.getText().toString().trim();
                if (!message.isEmpty()) {
                    addMessage(message, true);
                    editTextMessage.setText("");
                } else {
                    Toast.makeText(ChatActivity.this, "Vui lòng nhập tin nhắn", Toast.LENGTH_SHORT).show();
                }
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                Toast.makeText(ChatActivity.this, "Trang chủ", Toast.LENGTH_SHORT).show();
                return true;
            } else if (itemId == R.id.nav_profile) {
                Toast.makeText(ChatActivity.this, "Hồ sơ", Toast.LENGTH_SHORT).show();
                return true;
            } else {
                return false;
            }
        });


    }

    private void addMessage(String message, boolean isUser) {
        View view = LayoutInflater.from(this).inflate(
                isUser ? R.layout.item_message_user : R.layout.item_message_system,
                linearLayoutMessages,
                false);
        TextView textViewMessage = view.findViewById(R.id.textViewMessage);
        textViewMessage.setText(message);
        linearLayoutMessages.addView(view);
    }
}
