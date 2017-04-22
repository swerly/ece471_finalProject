import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

/**
 * Created by Seth on 4/21/2017.
 */
public class CipherTest {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        byte[] text = "This is some sample text that I want to encrypt. Hi.".getBytes();
        String txt = "This is some sample text that I want to encrypt. Hi.";

        int ptLength = 0;
        if (text.length%8 !=0){
            ptLength = text.length + (8 - ((text.length%8)));
        }
        byte[] plaintext = new byte[ptLength];
        System.arraycopy(text, 0, plaintext, 0, text.length);
        //TEST ECB
        Cipher desCipher = Cipher.getInstance("DESede/CBC/NoPadding");

        KeyGenerator keygen = KeyGenerator.getInstance("DESede");
        SecretKey myDesKey = keygen.generateKey();

        desCipher.init(Cipher.ENCRYPT_MODE, myDesKey);
        byte[] initVal = desCipher.getIV();

        byte[] allTextEncrypted = desCipher.doFinal(plaintext);

        System.out.println(Arrays.toString(allTextEncrypted));
        //END TEST ECB


        Encryptor ecpEnc = new Encryptor(Encryptor.DES3, Encryptor.CBC);
        ecpEnc.setKey(myDesKey);
        ecpEnc.setInitVal(initVal);
        ArrayList<byte[]> encrypted = ecpEnc.runTest(txt);
        System.out.println();
        for (byte[] block : encrypted){
            System.out.print(Arrays.toString(block));
        }
    }
}
