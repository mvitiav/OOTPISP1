import java.io.*;
import java.nio.file.attribute.UserDefinedFileAttributeView;

import static java.nio.file.Files.getFileStore;

public class myDefaultenc implements PlugTest {
    @Override
    public boolean checkFile(File src) {
        return FileChecker.checkFileForV1(src,this);
    }


    @Override
    public String getName() {
        return "no encoder";
    }

    @Override
    public String getExt() {
        return "non";
    }

    @Override
    public String toString() {
        return getName();
    }

    @Override
    public int encode(File dst, File src, String[] args) throws IOException {
        InputStream is = new FileInputStream(src);
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();
        dst.createNewFile();
        FileOutputStream fileOutputStream = new FileOutputStream(String.valueOf(dst.toPath()));
        if (FileChecker.addAttrs(dst,this)) {
        }else {
            sb.append(getExt());
        }

        System.out.println(checkFile(dst));

        String line = buf.readLine();
        while(line != null)
        {
            sb.append(line).append("\n");
            line = buf.readLine();
        }
        System.out.println(51+ ""+checkFile(dst));


        try (Writer fos = new OutputStreamWriter(fileOutputStream)) {

            fos.write(sb.toString());
        }

        return 0;
    }

    @Override
    public int decode(File dst, File src, String[] args) throws IOException {
        InputStream is = new FileInputStream(src);
        BufferedReader buf = new BufferedReader(new InputStreamReader(is));
        if (!getFileStore(src.toPath()).supportsFileAttributeView(UserDefinedFileAttributeView.class)){
            buf.read(new char[getExt().length()]);}
        String line = buf.readLine();
        StringBuilder sb = new StringBuilder();
        while(line != null)
        {
            sb.append(line).append("\n");
            line = buf.readLine();
        }
        try (FileOutputStream fos = new FileOutputStream(dst)) {

            fos.write(sb.toString().getBytes());
        }
        return 0;
    }

}
