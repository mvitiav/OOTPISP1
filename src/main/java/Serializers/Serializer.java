package Serializers;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface Serializer {
    public String getName();
    public FileNameExtensionFilter getFilter();
    public boolean serialize(Object object, String pathname) throws IOException;
    public Object deSerialize(String pathname) throws IOException, ClassNotFoundException;
}
