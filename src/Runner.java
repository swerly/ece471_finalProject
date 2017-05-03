import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Runner {
    public static String currentFileExtension;
    public static String currentFileName;
    public static int byteLength;

    public void run() {
        //setup
        try {
            String inputFileName = "imgTest.jpg";
            Path inputFilePath = Paths.get(".", "testFiles", inputFileName);
            byte[] inputByteArray = Files.readAllBytes(inputFilePath);
            byteLength = inputByteArray.length;
            String[] split = inputFileName.split(".");
            currentFileExtension = split[1];
            currentFileName = split[0];

            String curCipher;

            for (int i = 0; i < 3; i++) {
                curCipher = i == 0 ? Encryptor.DES : (i == 1 ? Encryptor.DES3 : Encryptor.AES);

                System.out.println(curCipher);
                for (int j = 0; j<5; j++){
                    long startTime = System.currentTimeMillis();
                    runMode(curCipher, inputByteArray, j);
                    long endTime = System.currentTimeMillis();
                    long totalTime = endTime-startTime;
                    System.out.print(totalTime + "\n");
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    private void runMode(String curCipher, byte[] input, int modeInt){
        String currentMode = "";
        switch (modeInt){
            case 0:
                currentMode = Encryptor.ECB;
                break;
            case 1:
                currentMode = Encryptor.CBC;
                break;
            case 2:
                currentMode = Encryptor.CFB;
                break;
            case 3:
                currentMode = Encryptor.OFB;
                break;
            case 4:
                currentMode = Encryptor.CTR;
                break;
        }
        Encryptor enc = new Encryptor(curCipher, currentMode);
        System.out.print("    " + currentMode + ": ");
        enc.runTest(input);
    }

    private void printTime(long millis){

    }
}
