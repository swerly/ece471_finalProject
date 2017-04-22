import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.lang.reflect.Array;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

/**
 * Created by Seth on 4/21/2017.
 */
public class Encryptor {
    public final static String AES = "AES";
    public final static String DES3 = "DESede";
    public final static String DES = "DES";

    public final static String ECB = "ECB";
    public final static String CBC = "CBC";
    public final static String CFB = "CFB";
    public final static String OFB = "OFB";
    public final static String CTR = "CTR";

    private Cipher currentCipher;
    private SecretKey key;
    private byte[] inputByteArray, initVal;
    private int blockSize;
    private ArrayList<byte[]> blocks, encrypted;
    private String type, mode;

    public Encryptor(String type, String mode) throws NoSuchPaddingException, NoSuchAlgorithmException {
        this.type = type;
        this.mode = mode;
        String instanceString = type + "/" + mode + "/NoPadding";
        currentCipher = Cipher.getInstance(instanceString);

        KeyGenerator keygen = KeyGenerator.getInstance(type);
        key = keygen.generateKey();

        if (type == AES){
            blockSize = 16;
        } else {
            blockSize = 8;
        }
    }


    public ArrayList<byte[]> runTest(String inputString){
        inputByteArray = inputString.getBytes();
        blocks = Utilities.getBlocksFromString(inputString, blockSize);

        switch (mode){
            case ECB:
                runECB();
                break;
            case CBC:
                runCBC();
                break;
            case CFB:
                break;
            case OFB:
                break;
            case CTR:
                break;
            default:
                System.out.println("This shouldn't happen...");
                break;
        }

        return encrypted;
    }

    private void runECB(){
        try {
            encrypted = new ArrayList<>();
            currentCipher.init(Cipher.ENCRYPT_MODE, key);

            for (byte[] block : blocks) {
                encrypted.add(currentCipher.doFinal(block));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void runCBC(){
        try{
            encrypted = new ArrayList<>();
            if (initVal != null) {
                currentCipher.init(Cipher.ENCRYPT_MODE, key);
            } else {

            }

            for (int i = 0; i < blocks.size(); i++){
                if (i != 0){
                    currentCipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(encrypted.get(i-1)));
                } else if (initVal != null){
                    currentCipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(initVal));
                }
                encrypted.add(currentCipher.doFinal(blocks.get(i)));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public void setKey(SecretKey key){
        this.key = key;
    }

    public void setInitVal(byte[] init){
        this.initVal = init;
    }
}
