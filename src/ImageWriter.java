import java.io.File;

/**
 * Created by sethw on 5/2/2017.
 */
public class ImageWriter {

    public void writeFile(){
        String outString = "outFiles/" + Runner.currentFileName + "." + Runner.currentFileExtension;
        File out = new File(outString);
    }
}
