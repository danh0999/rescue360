package com.example.prm392_project.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.widget.NestedScrollView;

import com.example.prm392_project.R;
import com.example.prm392_project.data.external.interfaces.ApiCallback;
import com.example.prm392_project.data.external.response.BaseResp;
import com.example.prm392_project.data.external.response.MessageReq;
import com.example.prm392_project.data.external.response.MessagesResp;
import com.example.prm392_project.data.external.services.ChatSvc;
import com.example.prm392_project.data.models.Conversation;
import com.example.prm392_project.data.models.Message;
import com.example.prm392_project.ui.activities.HomeActivity;
import com.example.prm392_project.ui.activities.ProfileActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.widget.LinearLayout;
import java.util.List;
import java.util.Collections;

public class ChatActivity extends AppCompatActivity {

    private LinearLayout linearLayoutMessages;
    private EditText editTextMessage;
    private ImageButton buttonSend;
    private ChatSvc chatSvc;
    private Conversation conversation;
    private ProgressBar progressBar;
    private NestedScrollView scrollViewMessages;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roomchat);
        chatSvc = new ChatSvc(ChatActivity.this);
        progressBar = findViewById(R.id.progressBar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Tin nhắn");

        // Initialize views
        linearLayoutMessages = findViewById(R.id.linearLayoutMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);
        scrollViewMessages = findViewById(R.id.scrollViewMessages);

        // Get conversation
        progressBar.setVisibility(View.VISIBLE);

        chatSvc.getUserRescueConversation(new ApiCallback<BaseResp<Conversation>>() {
            @Override
            public void onSuccess(BaseResp<Conversation> response) {
                progressBar.setVisibility(View.GONE);
                if (response.getData() != null) {
                    conversation = response.getData();
                    getSupportActionBar().setTitle(conversation.getName());
                    handleGetMessages();
                }
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ChatActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = editTextMessage.getText().toString().trim();
                if (!message.isEmpty()) {
                    handleSendNewMessage(message);
                    editTextMessage.setText("");
                } else {
                    Toast.makeText(ChatActivity.this, "Vui lòng nhập tin nhắn", Toast.LENGTH_SHORT).show();
                }
            }
        });

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation_roomchat);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            int itemId = item.getItemId();
            if (itemId == R.id.nav_home) {
                startActivity(new Intent(ChatActivity.this, HomeActivity.class));
                overridePendingTransition(0, 0);
                return true;
            } else if (itemId == R.id.nav_message) {
                return true; // Đang ở Chat, không cần chuyển trang
            } else if (itemId == R.id.nav_profile) {
                startActivity(new Intent(ChatActivity.this, ProfileActivity.class));
                overridePendingTransition(0, 0);
                return true;
            }
            return false;
        });
        bottomNavigationView.setSelectedItemId(R.id.nav_message);
    }

    private void handleSendNewMessage(String message) {
        progressBar.setVisibility(View.VISIBLE);
        chatSvc.createMessage(new MessageReq(message, conversation.getId()), new ApiCallback<BaseResp<Message>>() {
            @Override
            public void onSuccess(BaseResp<Message> response) {
                progressBar.setVisibility(View.GONE);
                if (response.getData() != null) {
                    addMessage(response.getData().getContent(), false);
                    scrollToBottom();
                }
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ChatActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleGetMessages() {
        progressBar.setVisibility(View.VISIBLE);
        chatSvc.getMessagesByConversationId(1, 1000, conversation.getId(), new ApiCallback<BaseResp<MessagesResp>>() {
            @Override
            public void onSuccess(BaseResp<MessagesResp> response) {
                progressBar.setVisibility(View.GONE);
                if (response.getData() != null) {
                    // Clear any existing messages
                    linearLayoutMessages.removeAllViews();

                    // Get messages and reverse to show oldest first
                    List<Message> messages = response.getData().getMessages();
                    Collections.reverse(messages);

                    for (Message message : messages) {
                        boolean isSystem = message.getCreatedBy().equals(conversation.getCreatedBy());
                        addMessage(message.getContent(), isSystem);
                    }

                    // Scroll to the bottom to show latest messages
                    scrollToBottom();
                }
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ChatActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addMessage(String message, boolean isSystem) {
        View view = LayoutInflater.from(this).inflate(
                isSystem ? R.layout.item_message_system : R.layout.item_message_user,
                linearLayoutMessages,
                false);
        TextView textViewMessage = view.findViewById(R.id.textViewMessage);
        textViewMessage.setText(message);
        linearLayoutMessages.addView(view);
    }

    private void scrollToBottom() {
        scrollViewMessages.post(new Runnable() {
            @Override
            public void run() {
                scrollViewMessages.fullScroll(View.FOCUS_DOWN);
            }
        });
    }
}