import java.io.File;
import java.io.IOException;

public interface PlugTest {
boolean checkFile(File src);
String getName();
String getExt();
int encode(File dst,File src, String[] args) throws IOException;
int decode(File dst,File src, String[] args) throws IOException;
}
