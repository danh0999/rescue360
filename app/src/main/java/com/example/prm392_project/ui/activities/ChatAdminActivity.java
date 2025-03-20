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

import com.example.prm392_project.R;
import com.example.prm392_project.data.external.api.SignalRService;
import com.example.prm392_project.data.external.interfaces.ApiCallback;
import com.example.prm392_project.data.external.response.BaseResp;
import com.example.prm392_project.data.external.response.MessageReq;
import com.example.prm392_project.data.external.response.MessagesResp;
import com.example.prm392_project.data.external.services.ChatSvc;
import com.example.prm392_project.data.internal.UserManager;
import com.example.prm392_project.data.models.Conversation;
import com.example.prm392_project.data.models.Message;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import android.widget.LinearLayout;

import androidx.core.widget.NestedScrollView;

import java.util.Collections;
import java.util.List;

public class ChatAdminActivity extends AppCompatActivity {

    private LinearLayout linearLayoutMessages;
    private EditText editTextMessage;
    private ImageButton buttonSend;
    private ChatSvc chatSvc;
    private Conversation conversation; // Will be passed from intent
    private ProgressBar progressBar;
    private NestedScrollView scrollViewMessages;
    private SignalRService signalRService;

    private String conversationId;
    private UserManager userManager;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roomchat); // Reusing the same layout
        chatSvc = new ChatSvc(ChatAdminActivity.this);
        userManager = new UserManager(ChatAdminActivity.this);
        progressBar = findViewById(R.id.progressBar);

        // Setup Toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true); // Add back arrow

        linearLayoutMessages = findViewById(R.id.linearLayoutMessages);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSend);
        scrollViewMessages = findViewById(R.id.scrollViewMessages);

        conversationId = getIntent().getStringExtra("CONVERSATION_ID");

        bottomNavigationView = findViewById(R.id.bottom_navigation_roomchat);
        bottomNavigationView.setVisibility(View.GONE);

        // Load messages for this conversation
        progressBar.setVisibility(View.VISIBLE);
        handleGetMessages();

        buttonSend.setOnClickListener(v -> {
            String message = editTextMessage.getText().toString().trim();
            if (!message.isEmpty()) {
                handleSendNewMessage(message);
                editTextMessage.setText("");
            } else {
                Toast.makeText(ChatAdminActivity.this, "Vui lòng nhập tin nhắn", Toast.LENGTH_SHORT).show();
            }
        });

        // SignalR setup
        signalRService = SignalRService.getInstance(this);
        signalRService.addMessageHandler(message -> {
            runOnUiThread(() -> {
                Toast.makeText(ChatAdminActivity.this, "Bạn có tin nhắn mới", Toast.LENGTH_SHORT).show();
                handleGetMessages();
            });
        });

        signalRService.connect(new SignalRService.OnConnectionCallback() {
            @Override
            public void onConnected() {
                runOnUiThread(() -> {
                    Toast.makeText(ChatAdminActivity.this, "Connected to chat server", Toast.LENGTH_SHORT).show();
                });
            }

            @Override
            public void onError(String errorMessage) {
                runOnUiThread(() -> {
                    Toast.makeText(ChatAdminActivity.this, "Connection error: " + errorMessage, Toast.LENGTH_SHORT).show();
                });
            }
        });
    }

    private void handleSendNewMessage(String message) {
        progressBar.setVisibility(View.VISIBLE);
        chatSvc.createMessage(new MessageReq(message, conversationId), new ApiCallback<BaseResp<Message>>() {
            @Override
            public void onSuccess(BaseResp<Message> response) {
                progressBar.setVisibility(View.GONE);
                if (response.getData() != null) {
                    addMessage(response.getData().getContent(), false); // Admin messages as user type
                    scrollToBottom();
                }
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ChatAdminActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void handleGetMessages() {
        progressBar.setVisibility(View.VISIBLE);
        chatSvc.getMessagesByConversationId(1, 1000, conversationId, new ApiCallback<BaseResp<MessagesResp>>() {
            @Override
            public void onSuccess(BaseResp<MessagesResp> response) {
                progressBar.setVisibility(View.GONE);
                if (response.getData() != null) {
                    linearLayoutMessages.removeAllViews();
                    List<Message> messages = response.getData().getMessages();
                    Collections.reverse(messages);
                    for (Message message : messages) {
                        // For admin view, we might want to distinguish admin vs client messages differently
                        boolean isSystem = !message.getCreatedBy().equals(userManager.getUser().getId());
                        addMessage(message.getContent(), isSystem);
                    }
                    scrollToBottom();
                }
            }

            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(ChatAdminActivity.this, message, Toast.LENGTH_SHORT).show();
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
        scrollViewMessages.post(() -> scrollViewMessages.fullScroll(View.FOCUS_DOWN));
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish(); // Go back to previous activity (likely ConversationListActivity)
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Optionally disconnect SignalR if needed
    }
}