import java.util.ArrayList;
import java.util.List;


public class Main {
    static Form form;

    private static String pluginsDir;
    static List<PlugTest> plugins;


    public static void main(String[] args) {

        pluginsDir="plugins";
        //System.out.println( new File(System.getProperty("user.dir")+File.separator+pluginsDir));
        plugins=new ArrayList<>();
        plugins.addAll( PluginClassLoader.getPlugins(pluginsDir));

        form = new Form(800,600);
    }

}
