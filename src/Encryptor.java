import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import java.lang.reflect.Array;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;

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

    public Encryptor(String type, String mode) {
        this.type = type;
        this.mode = mode;
        try {
            String instanceString = type + "/" + mode + "/NoPadding";
            currentCipher = Cipher.getInstance(instanceString);

            KeyGenerator keygen = KeyGenerator.getInstance(type);
            key = keygen.generateKey();

            if (type == AES) {
                blockSize = 16;
            } else {
                blockSize = 8;
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }


    public ArrayList<byte[]> runTest(byte[] input){
        inputByteArray = input;
        blocks = Utilities.getBlocksFromArray(inputByteArray, blockSize);

        switch (mode){
            case ECB:
                runECB();
                break;
            case CBC:
                runCBC();
                break;
            case CFB:
                runCFB();
                break;
            case OFB:
                runOFB();
                break;
            case CTR:
                runCTR();
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

            for (int i = 0; i < blocks.size(); i++){
                if (i != 0){
                    currentCipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(encrypted.get(i-1)));
                } else if (initVal != null){
                    currentCipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(initVal));
                } else {
                    currentCipher.init(Cipher.ENCRYPT_MODE, key);
                }
                encrypted.add(currentCipher.doFinal(blocks.get(i)));
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void runCFB(){
        try {
            encrypted = new ArrayList<>();
            byte[] thisIV = initVal == null ? currentCipher.getIV() : initVal;
            byte[] previousOut = null;
            for (int i = 0; i < blocks.size(); i++) {
                byte[] encIV;
                if (i != 0) {
                    currentCipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(previousOut));
                    encIV = currentCipher.doFinal(previousOut);
                } else if (initVal != null) {
                    currentCipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(thisIV));
                    encIV = currentCipher.doFinal(thisIV);
                }else {
                    currentCipher.init(Cipher.ENCRYPT_MODE, key);
                    encIV = currentCipher.doFinal();
                }

                //xor encIV with current block
                byte[] xorResult = new byte[blockSize];
                byte[] curBlock = blocks.get(i);
                for (int j = 0; j<encIV.length; j++){
                    xorResult[j] = (byte) (((int) curBlock[j]) ^ ((int) encIV[j]));
                }
                previousOut = xorResult;
                encrypted.add(xorResult);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void runOFB(){
        try {
            encrypted = new ArrayList<>();

            byte[] currentIV = initVal == null ? currentCipher.getIV() : initVal;

            for (int i = 0; i < blocks.size(); i++) {
                if (currentIV != null) {
                    //setup the current cipher to use the key and currentIV
                    currentCipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(currentIV));
                } else {
                    currentCipher.init(Cipher.ENCRYPT_MODE, key);
                    currentIV = currentCipher.getIV();
                }

                //encrypt the current IV, set the next IV to be the enc version of this iv
                currentIV = currentCipher.doFinal(currentIV);

                byte[] xorOut = new byte[blockSize];
                byte[] curBlock = blocks.get(i);
                for (int j = 0; j < blockSize; j++){
                    int xorVal = ((int) curBlock[j]) ^ ((int) currentIV[j]);
                    xorOut[j] = (byte) (0xff & xorVal);
                }
                encrypted.add(xorOut);
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void runCTR(){
        try{
            encrypted = new ArrayList<>();
            byte[] currentIV = initVal == null ? currentCipher.getIV() : initVal;

            for (int i = 0; i < blocks.size(); i++){
                if (i != 0){
                    incrementAtIndex(currentIV, 0);
                    currentCipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(currentIV));
                } else if (initVal != null){
                    currentCipher.init(Cipher.ENCRYPT_MODE, key, new IvParameterSpec(currentIV));
                } else {
                    currentCipher.init(Cipher.ENCRYPT_MODE, key);
                    currentIV = currentCipher.getIV();
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

    private void incrementAtIndex(byte[] array, int index) {
        if (array[index] == Byte.MAX_VALUE) {
            array[index] = 0;
            if(index > 0)
                incrementAtIndex(array, index - 1);
        }
        else {
            array[index]++;
        }
    }
}
