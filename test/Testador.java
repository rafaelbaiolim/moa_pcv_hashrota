
import java.io.IOException;
import java.io.InputStream;

public class Testador {

    public static void main(String[] args) throws IOException {
        Process proc = Runtime.getRuntime().exec("java -jar Moa.jar");
        InputStream in = proc.getInputStream();
        InputStream err = proc.getErrorStream();
       
    }

}
