package in.voiceme.app.voiceme.chat;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import in.voiceme.app.voiceme.R;
import in.voiceme.app.voiceme.infrastructure.BaseActivity;
import in.voiceme.app.voiceme.infrastructure.MainNavDrawer;

public class ChatDialogActivity extends BaseActivity {
    private List<ChatDialogPojo> chatDialogs;
    private RecyclerView rv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_dialog);
        getSupportActionBar().setTitle("Chat Messages");
        setNavDrawer(new MainNavDrawer(this));

        rv=(RecyclerView)findViewById(R.id.chat_dialog_recyclerview);
        rv.addItemDecoration(new DividerItemDecoration(getApplicationContext()));
        LinearLayoutManager llm = new LinearLayoutManager(this);
        rv.setLayoutManager(llm);
        rv.setHasFixedSize(true);

        initializeData();
        initializeAdapter();

    }

    private void initializeData(){
        chatDialogs = new ArrayList<>();
        chatDialogs.add(new ChatDialogPojo(R.mipmap.ic_launcher, R.mipmap.ic_launcher, "id", "username", "last message", "time", "2"));
        chatDialogs.add(new ChatDialogPojo(R.mipmap.ic_launcher, R.mipmap.ic_launcher, "id", "username2", "last message 2", "time", "2"));
        chatDialogs.add(new ChatDialogPojo(R.mipmap.ic_launcher, R.mipmap.ic_launcher, "id", "username3", "last message 3", "time", "2"));
    }

    private void initializeAdapter(){
        ChatDialogAdapter adapter = new ChatDialogAdapter(chatDialogs);
        rv.setAdapter(adapter);
    }
}
