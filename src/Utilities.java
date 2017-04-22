import java.util.ArrayList;

/**
 * Created by Seth on 4/21/2017.
 */
public class Utilities {
    //get an arraylist with each block
    public static ArrayList<byte[]> getBlocksFromString(String str, int blockLength){
        ArrayList<byte[]> toReturn = new ArrayList<>();
        byte[] allBytes = str.getBytes();
        int bytesToPad = blockLength - (allBytes.length%blockLength);
        int totalBlocks = (int)(Math.ceil((float)allBytes.length/blockLength));

        for (int i = 0; i<totalBlocks; i++){
            byte[] curBlock = new byte[blockLength];
            for (int j = 0; j<blockLength; j++){
                if ((i*blockLength)+j < allBytes.length) {
                    curBlock[j] = allBytes[(i * blockLength) + j];
                } else {
                    padCurrentBlock(curBlock, bytesToPad);
                }
            }
            toReturn.add(curBlock);
        }

        return toReturn;
    }

    //pad the current block
    private static byte[] padCurrentBlock(byte[] curBlock, int toPad){
        for (int i = toPad; i > 0; i--){
            curBlock[curBlock.length - i] = 0;
        }
        return curBlock;
    }
}
