import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

/**
 * Created by Seth on 4/21/2017.
 */
public class CipherTest {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
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
        byte[] iv = desCipher.getIV();
        desCipher2.init(Cipher.ENCRYPT_MODE, myDesKey2, new IvParameterSpec(iv));
        byte[] iv2 = desCipher2.getIV();

        byte[] allTextEncrypted = desCipher.doFinal(text);
        byte[] firstBlockEncrypted = desCipher2.doFinal(firstEight);

        desCipher2.init(Cipher.ENCRYPT_MODE, myDesKey2, new IvParameterSpec(firstBlockEncrypted));
        byte[] secondBlockEncrypted = desCipher2.doFinal(secondEight);

        System.out.println(Arrays.toString(allTextEncrypted));
        System.out.println(Arrays.toString(firstBlockEncrypted));
        System.out.println(Arrays.toString(secondBlockEncrypted));

    }
}
