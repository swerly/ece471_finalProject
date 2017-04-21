import javax.crypto.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by Seth on 4/21/2017.
 */
public class CipherTest {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        byte[] text = "This is some sample text that I want to encrypt.".getBytes();

        Cipher desCipher = Cipher.getInstance("DES/CBC/NoPadding");
        Cipher desCipher2 = Cipher.getInstance("DES/CBC/NoPadding");

        byte[] firstEight = new byte[8];
        byte[] secondEight = new byte[8];
        for (int i = 0; i<8; i++){
            firstEight[i] = text[i];
            secondEight[i] = text[i+8];
        }

        KeyGenerator keygen = KeyGenerator.getInstance("DES");
        SecretKey myDesKey = keygen.generateKey();
        SecretKey myDesKey2 = myDesKey;

        desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
        desCipher2.init(Cipher.ENCRYPT_MODE, myDesKey2);

        byte[] allTextEncrypted = desCipher.doFinal(text);
        byte[] firstBlockEncrypted = desCipher2.doFinal(firstEight);

        System.out.println(Arrays.toString(allTextEncrypted));
        System.out.println(Arrays.toString(firstBlockEncrypted));
    }
}
