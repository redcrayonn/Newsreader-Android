package olaf.demol.nl.newsreader548385.net.Models;

public class User {

    private static String authtoken;
    private String username;
    //Moet natuurlijk beter dan dit ;)
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
