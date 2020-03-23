import java.io.*;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.util.Scanner;

import static java.nio.file.Files.getFileStore;

public class FileChecker {
    public static boolean addAttrs(File f1,PlugTest p1) throws IOException {


            if (getFileStore(f1.toPath()).supportsFileAttributeView(UserDefinedFileAttributeView.class)) {
                UserDefinedFileAttributeView udfav = Files.getFileAttributeView(f1.toPath(),
                        UserDefinedFileAttributeView.class);
              //  System.out.println("Attrs. before deletion. its size: " + udfav.list().size());
                //for (String name : udfav.list()) {
               //     System.out.println(udfav.size(name) + " " + name);
              //  }
                int written = udfav.write("file.description", Charset.defaultCharset().
                        encode(p1.getExt()));

//                for (String name : udfav.list()) {
//                    System. out.println(udfav.size(name) + " " + name);
//                }

                return true;
            }

        return false;
    }

    public static boolean checkFileForV1(File f1,PlugTest p1)
    {
        try{
            if (getFileStore(f1.toPath()).supportsFileAttributeView(UserDefinedFileAttributeView.class))
            {


                UserDefinedFileAttributeView udfav = Files.getFileAttributeView(f1.toPath(),UserDefinedFileAttributeView.class);

                boolean found=false;

                for (String name : udfav.list()) {
                    System.out.println(udfav.size(name) + " " + name);
                if (0=="file.description".compareTo(name)){found=true;}
                }

                if(!found){return false;}

                int size = udfav.size("file.description");
                ByteBuffer bb = ByteBuffer.allocateDirect(size);
                udfav.read("file.description", bb);
                bb.flip();
                String s=Charset.defaultCharset().decode(bb).toString();
               // System.out.println(Charset.defaultCharset().decode(bb).toString());
                return (0==s.compareTo(p1.getExt()));
            }

    } catch (Exception e) {
        e.printStackTrace();
    }
        try(Scanner scanner = new Scanner(new BufferedReader(new FileReader("d:\\text.txt")))){
            StringBuilder stringBuilder = new StringBuilder();
            for (int i = 0; ((i<p1.getExt().length())&&(scanner.hasNext())); i++)
            {
                stringBuilder.append(scanner.next());
            }
            return (0==p1.getExt().compareTo(stringBuilder.toString()));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return false;
    }
}
