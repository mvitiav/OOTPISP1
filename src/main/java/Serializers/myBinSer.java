package Serializers;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.*;

public class myBinSer implements Serializer {
    @Override
    public String getName() {
        return "Binary";
    }

    @Override
    public FileNameExtensionFilter getFilter() {
        return new FileNameExtensionFilter("binary files (*.bin)", "bin");
    }

    @Override
    public boolean serialize(Object object, String pathname) throws IOException {

            FileOutputStream fileOut = new FileOutputStream(pathname);
            ObjectOutputStream objOut = new ObjectOutputStream(fileOut);
            objOut.writeObject(object);
            fileOut.close();
            objOut.close();
            return true;
    }

    @Override
    public String toString() {
        return "myBinSer{}";
    }

    @Override
    public Object deSerialize(String pathname) throws IOException, ClassNotFoundException {
        Object retObject = null;

            FileInputStream fileIn = new FileInputStream(pathname);
            ObjectInputStream objIn = new ObjectInputStream(fileIn);
            retObject= objIn.readObject();
            fileIn.close();
            objIn.close();

        return retObject;
    }
}
