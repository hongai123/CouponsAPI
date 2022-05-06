package tryingCoupons.tryingCoupon.beans;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;

@Builder
/**
 * UserName and Password of the user
 * @param username - username
 * @param password - password
 */
public class UserProp {
    private String username;
    private String password;
    @JsonIgnore
    private Roles role;

    /**
     *
     * @param username - username
     * @param password - password
     */
    public UserProp(String username , String password, Roles roles){
        this.username=username;
        this.password=password;
        this.role = roles;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "UserProp{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    public Roles getRole() {
        return role;
    }

    public void setRole(Roles role) {
        this.role = role;
    }
}
