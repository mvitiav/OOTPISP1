package Serializers;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.beans.XMLDecoder;
import java.beans.XMLEncoder;
import java.io.*;

public class myXMLser implements Serializer {
    @Override
    public String getName() {
        return "XML";
    }

    @Override
    public FileNameExtensionFilter getFilter() {
        return new FileNameExtensionFilter("xml files (*.xml)", "xml");
    }

    @Override
    public boolean serialize(Object object, String pathname) throws FileNotFoundException {
        XMLEncoder encoder = null;

            encoder=new XMLEncoder(new BufferedOutputStream(new FileOutputStream(pathname)));

        encoder.writeObject(object);
        encoder.close();

        return true;
    }

    @Override
    public String toString() {
        return "myXMLser{}";
    }

    @Override
    public Object deSerialize(String pathname) throws FileNotFoundException {
        XMLDecoder decoder=null;

            decoder=new XMLDecoder(new BufferedInputStream(new FileInputStream(pathname)));

        return decoder.readObject();
    }
}
