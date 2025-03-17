package com.example.prm392_project.data.external.api;

import android.content.Context;
import android.util.Log;

import com.example.prm392_project.data.internal.TokenManager;
import com.example.prm392_project.data.models.Message;
import com.microsoft.signalr.Action1;
import com.microsoft.signalr.HubConnection;
import com.microsoft.signalr.HubConnectionBuilder;
import com.microsoft.signalr.HubConnectionState;
import com.microsoft.signalr.TransportEnum;

import io.reactivex.rxjava3.core.Single;

public class SignalRService {
    private static SignalRService instance;
    private HubConnection hubConnection;
    private boolean isConnected = false;
    private int reconnectAttempts = 0;
    private final int maxReconnectAttempts = 5;
    private final long initialDelayMs = 2000; // 2 seconds

    private SignalRService(Context context) {
        TokenManager tokenManager = new TokenManager(context);
        String token = tokenManager.getToken();
        hubConnection = HubConnectionBuilder.create("https://rescue360-server-production.up.railway.app/chat?access_token=" + token)
                .withTransport(TransportEnum.WEBSOCKETS)
                .shouldSkipNegotiate(true)
                .build();

        setupConnectionHandlers();
    }

    public static synchronized SignalRService getInstance(Context context) {
        if (instance == null) {
            instance = new SignalRService(context.getApplicationContext());
        }
        return instance;
    }

    private void setupConnectionHandlers() {
        hubConnection.onClosed(exception -> {
            isConnected = false;
            Log.e("SignalR", "Connection closed: " + (exception != null ? exception.getMessage() : "No error"));
            attemptReconnect();
        });
    }

    public void connect(OnConnectionCallback callback) {
        if (isConnected()) {
            if (callback != null) callback.onConnected();
            return;
        }

        new Thread(() -> {
            try {
                hubConnection.start().blockingAwait();
                isConnected = true;
                reconnectAttempts = 0; // Reset attempt counter on successful connection
                Log.d("SignalR", "Connected successfully");
                if (callback != null) callback.onConnected();
            } catch (Exception e) {
                Log.e("SignalR", "Error connecting: " + e.getMessage());
                if (callback != null) callback.onError(e.getMessage());
                attemptReconnect();
            }
        }).start();
    }

    private void attemptReconnect() {
        if (reconnectAttempts >= maxReconnectAttempts) {
            Log.e("SignalR", "Max reconnect attempts reached. Stopping retries.");
            return;
        }

        reconnectAttempts++;
        long delay = initialDelayMs * (long) Math.pow(2, reconnectAttempts); // Exponential backoff
        Log.d("SignalR", "Reconnecting in " + delay / 1000 + " seconds...");

        new Thread(() -> {
            try {
                Thread.sleep(delay);
                if (!isConnected()) {
                    connect(null);
                }
            } catch (InterruptedException e) {
                Log.e("SignalR", "Reconnect attempt interrupted: " + e.getMessage());
            }
        }).start();
    }

    public void addMessageHandler(Action1<Message> onMessageReceived) {
        hubConnection.on("ReceiveMessage", message -> {
            Log.d("SignalR", "Message received: " + message);
            if (onMessageReceived != null) {
                onMessageReceived.invoke(message);
            }
        }, Message.class);
    }

    public void disconnect() {
        if (isConnected) {
            new Thread(() -> {
                try {
                    hubConnection.stop().blockingAwait();
                    isConnected = false;
                    reconnectAttempts = 0; // Reset counter on manual disconnect
                    Log.d("SignalR", "Disconnected successfully");
                } catch (Exception e) {
                    Log.e("SignalR", "Error disconnecting: " + e.getMessage());
                }
            }).start();
        }
    }

    public boolean isConnected() {
        return isConnected && hubConnection.getConnectionState() == HubConnectionState.CONNECTED;
    }

    public interface OnConnectionCallback {
        void onConnected();
        void onError(String errorMessage);
    }
}
