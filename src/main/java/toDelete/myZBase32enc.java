//package toDelete;
//
//import org.lightningj.util.ZBase32;
//
//import java.io.*;
//import java.nio.ByteBuffer;
//import java.nio.charset.Charset;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.attribute.UserDefinedFileAttributeView;
//
//import static java.nio.file.Files.getFileStore;
//
//public class myZBase32enc implements PlugTest {
//    @Override
//    public String getName() {
//        return "ZBase32";
//    }
//
//    @Override
//    public String getExt() {
//        return "z32";
//    }
//    @Override
//    public boolean checkFile(File src) {
//        return FileChecker.checkFileForV1(src,this);
//    }
//    @Override
//    public int encode(File dst, File src, String[] args) throws IOException {
//        byte[] fileContent = Files.readAllBytes(src.toPath());
//        String encodedString = new ZBase32().encodeToString(fileContent);
//        Writer writer = new OutputStreamWriter(new FileOutputStream(String.valueOf(dst.toPath())), StandardCharsets.US_ASCII);
//        try {
//            if (FileChecker.addAttrs(dst,this)) {
//            }else {
//            writer.write(getExt());
//            }
//
//            writer.write(encodedString);
//        } finally {
//            writer.close();
//        }
//        return 0;
//    }
//
//    @Override
//    public int decode(File dst, File src, String[] args) throws IOException {
//        InputStream is = new FileInputStream(src);
//        BufferedReader buf = new BufferedReader(new InputStreamReader(is));
//        if (!getFileStore(src.toPath()).supportsFileAttributeView(UserDefinedFileAttributeView.class)){
//        buf.read(new char[getExt().length()]);}
//
//       // System.err.println(Files.getAttribute(src.toPath(),"tst"));
//        String line = buf.readLine();
//        StringBuilder sb = new StringBuilder();
//        while(line != null)
//        {
//            sb.append(line);
//            line = buf.readLine();
//        }
//        String fileAsString = sb.toString();
////        byte[] decodedBytes = Base64.getDecoder().decode(fileAsString);
//        byte[] decodedBytes = new ZBase32().decode(fileAsString);
//
//        try (FileOutputStream fos = new FileOutputStream(dst)) {
//            fos.write(decodedBytes);
//        }
//        //String decodedString = new String(decodedBytes);
//        return 0;
//    }
//
//}
