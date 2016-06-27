package p4.geretaxi;

/**
 * Created by belchior on 26/06/2016.
 */
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PasswordValidator{

    private Pattern pattern;
    private Matcher matcher;

    private static final String PASSWORD_PATTERN =
            "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=!_?«»',.-])(?=\\S+$).{8,}$";

    public PasswordValidator(){
        pattern = Pattern.compile(PASSWORD_PATTERN);
    }

    /**
     * Validate password with regular expression
     * @param password password for validation
     * @return true valid password, false invalid password
     */
    public boolean validate(final String password){
        matcher = pattern.matcher(password);
        return matcher.matches();
    }
}
