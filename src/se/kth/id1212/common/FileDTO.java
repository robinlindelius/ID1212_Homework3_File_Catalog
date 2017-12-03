package se.kth.id1212.common;

import java.io.Serializable;

/**
 * Created by Robin on 2017-12-02.
 */
public interface FileDTO extends Serializable {

    public String getName();

    public long getSize();

    public boolean isPrivateAccess() ;

    public boolean isWritePermission();

    public boolean isNotify();

    public String getInfo();
}
