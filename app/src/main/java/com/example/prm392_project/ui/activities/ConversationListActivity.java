package com.example.prm392_project.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.prm392_project.R;
import com.example.prm392_project.data.external.interfaces.ApiCallback;
import com.example.prm392_project.data.external.response.BaseResp;
import com.example.prm392_project.data.external.response.ConversationListResp;
import com.example.prm392_project.data.external.services.ChatSvc;
import com.example.prm392_project.data.models.Conversation;

import java.util.ArrayList;
import java.util.List;

public class ConversationListActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ConversationAdapter adapter;
    private Button backBtn;
    private List<Conversation> conversationList;
    private ChatSvc chatSvc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation_list);
        chatSvc = new ChatSvc(this);

        recyclerView = findViewById(R.id.conversation_recycler_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        conversationList = new ArrayList<>();
        adapter = new ConversationAdapter(conversationList);
        recyclerView.setAdapter(adapter);

        backBtn = findViewById(R.id.back_button);
        backBtn.setOnClickListener(v -> {
            finish();
        });

        loadConversations();
    }

    // RecyclerView Adapter
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

    // ViewHolder for individual conversation items
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
            nameTextView.setText(conversation.getName());
            if (conversation.getLastMessage() != null) {
                lastMessageTextView.setText(conversation.getLastMessage().getContent());
            } else {
                lastMessageTextView.setText("No messages yet");
            }
            updatedAtTextView.setText(conversation.getUpdatedAt());

            // Add click listener if needed
            itemView.setOnClickListener(v -> {
                // Handle conversation click - perhaps open conversation details

                Intent intent = new Intent(ConversationListActivity.this, ChatActivity.class);

                // Pass conversation ID to ChatActivity
                intent.putExtra("CONVERSATION_ID", conversation.getId());
                startActivity(intent);
            });
        }
    }


    public void loadConversations() {
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
}