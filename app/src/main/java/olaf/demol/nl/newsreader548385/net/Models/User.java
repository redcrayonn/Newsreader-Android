package olaf.demol.nl.newsreader548385.net.Models;

/**
 * Created by olaf on 15-10-17.
 */

public class User {

    private static String authtoken;
    private String username;
    private String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public static String getAuthtoken() {
        return authtoken;
    }

    public static void setAuthtoken(String token) {
        authtoken = token;
    }

    public static boolean isLoggedIn() {
        return getAuthtoken() != null && !getAuthtoken().isEmpty();
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
