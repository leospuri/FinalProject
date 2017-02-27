package in.voiceme.app.voiceme.chat;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import in.voiceme.app.voiceme.R;

/**
 * Created by harish on 2/27/2017.
 */

public class ChatDialogAdapter extends RecyclerView.Adapter<ChatDialogAdapter.ChatDialogViewHolder> {

    public static class ChatDialogViewHolder extends RecyclerView.ViewHolder {

        CardView cv;
        protected TextView dialogName;
        protected TextView dialogDate;
        protected ImageView dialogAvatar;
        protected ImageView dialogLastMessageUserAvatar;
        protected TextView dialogLastMessage;
        protected TextView unReadBubble;

        ChatDialogViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.cv);
            dialogName = (TextView) itemView.findViewById(R.id.dialogName);
            dialogDate = (TextView) itemView.findViewById(R.id.dialogDate);
            dialogLastMessage = (TextView) itemView.findViewById(R.id.dialogLastMessage);
            unReadBubble = (TextView) itemView.findViewById(R.id.dialogUnreadBubble);
            dialogLastMessageUserAvatar = (ImageView) itemView.findViewById(R.id.dialogLastMessageUserAvatar);
            dialogAvatar = (ImageView) itemView.findViewById(R.id.dialogAvatar);
        }
    }

    List<ChatDialogPojo> chatDialogPojo;

    ChatDialogAdapter(List<ChatDialogPojo> chatDialogPojo){
        this.chatDialogPojo = chatDialogPojo;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public ChatDialogViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.chat_dialog, viewGroup, false);
        ChatDialogViewHolder pvh = new ChatDialogViewHolder(v);
        return pvh;
    }

    @Override
    public void onBindViewHolder(ChatDialogViewHolder holder, int position) {
        holder.dialogName.setText(chatDialogPojo.get(position).username);
        holder.dialogDate.setText(chatDialogPojo.get(position).timeStamp);
        holder.dialogLastMessage.setText(chatDialogPojo.get(position).lastMessage);
        holder.unReadBubble.setText(chatDialogPojo.get(position).unRead);
        holder.dialogAvatar.setImageResource(chatDialogPojo.get(position).lastDialogAvatarUrl);
        holder.dialogLastMessageUserAvatar.setImageResource(chatDialogPojo.get(position).avatarUrl);

    }

    @Override
    public int getItemCount() {
        return chatDialogPojo.size();
    }
}

