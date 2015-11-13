package com.jsbn.mgr.net.entity;

/**
 * Created by 13510 on 2015/9/12.
 */
public class LoginEntity extends BaseEntity{
    private int groupId;
    private String groupName;
    private int groupLeader;

    public int getGroupId() {
        return groupId;
    }

    public void setGroupId(int groupId) {
        this.groupId = groupId;
    }

    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    public int getGroupLeader() {
        return groupLeader;
    }

    public void setGroupLeader(int groupLeader) {
        this.groupLeader = groupLeader;
    }

    public LoginEntity(int groupId, String groupName, int groupLeader) {

        this.groupId = groupId;
        this.groupName = groupName;
        this.groupLeader = groupLeader;
    }
}
