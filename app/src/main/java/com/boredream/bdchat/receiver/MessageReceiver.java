package com.boredream.bdchat.receiver;

import android.util.Log;

import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;
import io.rong.imlib.model.Message;

public class MessageReceiver implements RongIMClient.OnReceiveMessageListener {

    /**
     * 收到消息的处理。
     *
     * @param message 收到的消息实体。
     * @param left    剩余未拉取消息数目。
     * @return 收到消息是否处理完成，true 表示自己处理铃声和后台通知，false 走融云默认处理方式。
     */
    @Override
    public boolean onReceived(Message message, int left) {
        if (message.getConversationType() == Conversation.ConversationType.SYSTEM) {
            Log.i("DDD", "onReceived: system message");
            return true;
        }

        return false;
    }
}