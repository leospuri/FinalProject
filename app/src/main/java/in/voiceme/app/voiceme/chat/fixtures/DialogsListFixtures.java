package in.voiceme.app.voiceme.chat.fixtures;

import android.support.annotation.NonNull;
import android.util.Log;

import com.stfalcon.chatkit.commons.models.IMessage;
import com.stfalcon.chatkit.commons.models.IUser;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

import in.voiceme.app.voiceme.chat.models.DefaultDialog;
import in.voiceme.app.voiceme.chat.models.DefaultUser;

/**
 * Created by Anton Bevza on 07.09.16.
 */
public final class DialogsListFixtures extends FixturesData {
    private DialogsListFixtures() {
        throw new AssertionError();
    }

    public static ArrayList<DefaultDialog> getChatList() {
        ArrayList<DefaultDialog> chats = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            chats.add(getDialog(i));
        }
        Log.e("dialog", chats.get(0).getDialogName());
        return chats;
    }

    private static IMessage getMessage(final Date date) {
        return new IMessage() {
            @Override
            public String getId() {
                return Long.toString(UUID.randomUUID().getLeastSignificantBits());
            }

            @Override
            public String getText() {
                return messages.get(rnd.nextInt(messages.size()));
            }

            @Override
            public IUser getUser() {
                return DialogsListFixtures.getUser();
            }

            @Override
            public Date getCreatedAt() {
                return date;
            }
        };
    }

    private static DefaultDialog getDialog(int i) {
        ArrayList<IUser> users = getUsers();
        return new DefaultDialog(String.valueOf(UUID.randomUUID().getLeastSignificantBits()),
                users.size() > 1 ? groupChatTitles.get(users.size() - 2) : users.get(0).getName(),
                users.size() > 1 ? groupChatImages.get(users.size() - 2) : avatars.get(rnd.nextInt(4)),
                users,
                getMessage(Calendar.getInstance().getTime()), i < 3 ? 3 - i : 0);
    }

    private static ArrayList<IUser> getUsers() {
        ArrayList<IUser> users = new ArrayList<>();
        int usersCount = 1;
        for (int i = 0; i < usersCount; i++) {
            users.add(getUser());
        }
        return users;
    }


    @NonNull
    private static IUser getUser() {
        return new DefaultUser("2", "name", avatars.get(rnd.nextInt(4)), rnd.nextBoolean());
    }
}
