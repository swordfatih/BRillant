import java.net.URL;
import java.net.URLClassLoader;

public class Application {

    public static void main(String[] args) throws Exception {
        String fileDirURL = "ftp://localhost:2121/serveurBRi.jar";
        URLClassLoader urlcl = new URLClassLoader(new URL[]{new URL(fileDirURL)});

        Class<?> classeChargee = urlcl.loadClass("bri.ServiceServeur");
    }

}
