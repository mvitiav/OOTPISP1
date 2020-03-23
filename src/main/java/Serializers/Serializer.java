package Serializers;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface Serializer {
     String getName();
     FileNameExtensionFilter getFilter();
     boolean serialize(Object object, String pathname) throws IOException;
     Object deSerialize(String pathname) throws IOException, ClassNotFoundException;

}
