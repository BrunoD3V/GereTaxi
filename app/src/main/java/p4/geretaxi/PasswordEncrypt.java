package p4.geretaxi;

import se.simbio.encryption.Encryption;

/**
 * Created by belchior on 27/06/2016.
 */
public class PasswordEncrypt {





    private Encryption encryption;




    public String getEncrypted(String pass){
        encryption = Encryption.getDefault(Constants.KEY, Constants.SALT, new byte[16]);
         return encryption.encryptOrNull(pass).trim();
    }


}
