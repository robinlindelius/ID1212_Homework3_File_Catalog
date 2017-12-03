package se.kth.id1212.server.model;

import se.kth.id1212.common.UserDTO;

import javax.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@NamedQueries({
        @NamedQuery(
                name = "findUserByUsername",
                query = "SELECT user FROM User user WHERE user.username LIKE :username"
        ),

        @NamedQuery(
                name = "deleteUserByUsername",
                query = "DELETE FROM User user WHERE user.username LIKE :username"
        )
})

@Entity(name = "User")
public class User implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long userID;

    @Column(name = "name", nullable = false)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "owner")
    private Collection<CatalogFile> files = new ArrayList<>();

    @Version
    @Column(name = "OPTLOCK")
    private int versionNum;

    public User() {
        this.username = "";
        this.password = "";
    }

    public User (UserDTO userDTO) {
        this.username = userDTO.getUsername();
        this.password = userDTO.getPassword();
    }

    public long getUserID() {
        return userID;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
