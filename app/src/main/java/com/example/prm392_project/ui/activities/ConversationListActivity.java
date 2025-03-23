package com.example.prm392_project.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.prm392_project.R;
import com.example.prm392_project.data.external.interfaces.ApiCallback;
import com.example.prm392_project.data.external.response.BaseResp;
import com.example.prm392_project.data.external.response.ConversationListResp;
import com.example.prm392_project.data.external.services.ChatSvc;
import com.example.prm392_project.data.models.Conversation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;
import java.util.List;

public class ConversationListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ConversationAdapter adapter;
    private List<Conversation> conversationList;
    private ChatSvc chatSvc;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);
        chatSvc = new ChatSvc(this);

        recyclerView = findViewById(R.id.conversation_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        bottomNavigationView = findViewById(R.id.bottom_navigation);
        bottomNavigationView.setSelectedItemId(R.id.nav_chat);

        conversationList = new ArrayList<>();
        adapter = new ConversationAdapter(conversationList);
        recyclerView.setAdapter(adapter);

        loadConversations();
        setupBottomNavigation();
    }

    private class ConversationAdapter extends RecyclerView.Adapter<ConversationViewHolder> {
        private List<Conversation> conversations;

        public ConversationAdapter(List<Conversation> conversations) {
            this.conversations = conversations;
        }

        @Override
        public ConversationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_conversation, parent, false);
            return new ConversationViewHolder(view);
        }

        @Override
        public void onBindViewHolder(ConversationViewHolder holder, int position) {
            Conversation conversation = conversations.get(position);
            holder.bind(conversation);
        }

        @Override
        public int getItemCount() {
            return conversations.size();
        }
    }

    private class ConversationViewHolder extends RecyclerView.ViewHolder {
        private TextView nameTextView;
        private TextView lastMessageTextView;
        private TextView updatedAtTextView;

        public ConversationViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.conversation_name);
            lastMessageTextView = itemView.findViewById(R.id.conversation_last_message);
            updatedAtTextView = itemView.findViewById(R.id.conversation_updated_at);
        }

        public void bind(Conversation conversation) {
            nameTextView.setText(conversation.getCreatedUser().getFullName());
            if (conversation.getLastMessage() != null) {
                lastMessageTextView.setText(conversation.getLastMessage().getContent());
            } else {
                lastMessageTextView.setText("No messages yet");
            }
            updatedAtTextView.setText(conversation.getUpdatedAt());

            itemView.setOnClickListener(v -> {
                Intent intent = new Intent(ConversationListActivity.this, ChatAdminActivity.class);
                intent.putExtra("CONVERSATION_ID", conversation.getId());
                startActivity(intent);
                finish();
            });
        }
    }

    private void loadConversations() {
        chatSvc.getAdminRescueConversation(new ApiCallback<BaseResp<ConversationListResp>>() {
            @Override
            public void onSuccess(BaseResp<ConversationListResp> response) {
                conversationList.clear();
                conversationList.addAll(response.getData().getConversations());
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onError(String message) {
                Toast.makeText(ConversationListActivity.this, message, Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupBottomNavigation() {
        bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                int id = item.getItemId();

                if (id == R.id.nav_home) {
                    startActivity(new Intent(ConversationListActivity.this, AdminDashboardActivity.class));
                    return true;
                } else if (id == R.id.nav_request) {
                    startActivity(new Intent(ConversationListActivity.this, RequestListActivity.class));
                    return true;
                } else if (id == R.id.nav_users) {
                    startActivity(new Intent(ConversationListActivity.this, RescueStaffActivity.class));
                    return true;
                } else if (id == R.id.nav_chat) {
                    Toast.makeText(ConversationListActivity.this, "Conversation", Toast.LENGTH_SHORT).show();
                    return true;
                }
                return false;
            }
        });
    }
}