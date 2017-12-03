package se.kth.id1212.common;

import java.io.Serializable;

/**
 * Created by Robin on 2017-12-02.
 */
public class UserDTO implements Serializable {
    private final String username;
    private final String password;

    /**
     * Creates a UserDTO object with the given username and password
     * @param username
     * @param password
     */
    public UserDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }

    /**
     * Returns the username
     * @return the username
     */
    public String getUsername() {
        return username;
    }

    /**
     * Returns the password
     * @return the password
     */
    public String getPassword() {
        return password;
    }
}
