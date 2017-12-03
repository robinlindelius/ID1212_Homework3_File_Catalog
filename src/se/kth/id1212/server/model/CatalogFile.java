package se.kth.id1212.server.model;

import se.kth.id1212.common.FileDTO;

import javax.persistence.*;

@NamedQueries({
        @NamedQuery(
                name = "deleteFileByName",
                query = "DELETE FROM File file WHERE file.name LIKE :fileName"
        )
        ,
        @NamedQuery(
                name = "findFileByName",
                query = "SELECT file FROM File file WHERE file.name LIKE :fileName",
                lockMode = LockModeType.OPTIMISTIC
        )
        ,
        @NamedQuery(
                name = "findAllFiles",
                query = "SELECT file FROM File file",
                lockMode = LockModeType.OPTIMISTIC
        )
})

@Entity(name = "File")
public class CatalogFile implements FileDTO {

    @Id
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "size", nullable = false)
    private long size;

    @JoinColumn(name = "owner", nullable = false)
    private User owner;

    @Column(name = "privateAccess", nullable = false)
    private boolean privateAccess;

    @Column(name = "writePermission", nullable = false)
    private boolean writePermission;

    @Column(name = "notify", nullable = false)
    private boolean notify;

    @Version
    @Column(name = "OPTLOCK")
    private int versionNum;

    public CatalogFile() {
        this(null, 0, null, false, false, false);
    }

    public CatalogFile(String name, long size, User user, boolean privateAccess, boolean writePermission, boolean notify) {
        this.name = name;
        this.size = size;
        this.owner = user;
        this.privateAccess = privateAccess;
        this.writePermission = writePermission;
        this.notify = notify;
    }

    public CatalogFile (FileDTO fileDTO, User user) {
        this.name = fileDTO.getName();
        this.size = fileDTO.getSize();
        this.owner = user;
        this.privateAccess = fileDTO.isPrivateAccess();
        this.writePermission = fileDTO.isWritePermission();
        this.notify = fileDTO.isNotify();
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setPrivateAccess(boolean privateAccess) {
        this.privateAccess = privateAccess;
    }

    public void setWritePermission(boolean writePermission) {
        this.writePermission = writePermission;
    }

    public void setNotify(boolean notify) {
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

    public User getOwner() {
        return owner;
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
        return "" + this;
    }

    @Override
    public String toString() {
        return "File: [NAME:" + name + ", SIZE: " + size + ", OWNER: " +
                owner.getUsername() + ", " +
                ((privateAccess) ? "PRIVATE" : "PUBLIC")+ ", " +
                ((writePermission) ? "WRITE" : "READ") +
                        ((notify) ? ", NOTIFY" : "") + "]";
    }
}
