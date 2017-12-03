package se.kth.id1212.client.view;

import se.kth.id1212.common.FileDTO;

/**
 * Created by Robin on 2017-12-03.
 */
public class ClientFile implements FileDTO{
    private String name;
    private long size;
    private boolean privateAccess;
    private boolean writePermission;
    private boolean notify;

    public ClientFile(String name, long size, boolean privateAccess, boolean writePermission, boolean notify) {
        this.name = name;
        this.size = size;
        this.privateAccess = privateAccess;
        this.writePermission = writePermission;
        this.notify = notify;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public boolean isPrivateAccess() {
        return privateAccess;
    }

    @Override
    public boolean isWritePermission() {
        return writePermission;
    }

    @Override
    public boolean isNotify() {
        return notify;
    }

    @Override
    public String getInfo() {
        return "File: [NAME:" + name + ", SIZE: " + size + ", " +
                ((privateAccess) ? "PRIVATE" : "PUBLIC")+ ", " +
                ((writePermission) ? "WRITE" : "READ") +
                        ((notify) ? ", NOTIFY" : "") + "]";
    }
}
