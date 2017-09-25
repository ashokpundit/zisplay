package vun.zisplay.models;

/**
 * Created by ashok on 2/23/15.
 */
public class ChatItem {

    String id;
    String[] users;
    String usersIds;
    String lastMessage;
    String lastMessageType;
    String[] userNames;

    public String[] getUserNames() {
        return userNames;
    }

    public void setUserNames(String[] userNames) {
        this.userNames = userNames;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }



    public String[] getUsers() {
        return users;
    }

    public void setUsers(String[] users) {
        this.users = users;
    }

    public String getUsersIds() {
        return usersIds;
    }

    public void setUsersIds(String usersIds) {
        this.usersIds = usersIds;
    }

    public String getLastMessage() {
        return lastMessage;
    }

    public void setLastMessage(String lastMessage) {
        this.lastMessage = lastMessage;
    }

    public String getLastMessageType() {
        return lastMessageType;
    }

    public void setLastMessageType(String lastMessageType) {
        this.lastMessageType = lastMessageType;
    }

    public String getLastMessageTime() {
        return lastMessageTime;
    }

    public void setLastMessageTime(String lastMessageTime) {
        this.lastMessageTime = lastMessageTime;
    }

    String lastMessageTime;

}
