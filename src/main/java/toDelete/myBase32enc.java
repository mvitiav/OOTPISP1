//
//import org.apache.commons.codec.binary.Base32;
//import java.io.*;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.attribute.UserDefinedFileAttributeView;
//
//import static java.nio.file.Files.getFileStore;
//
//public class myBase32enc implements PlugTest {
//    @Override
//    public boolean checkFile(File src) {
//       return FileChecker.checkFileForV1(src,this);
//    }
//
//
//    @Override
//    public String getName() {
//        return "Base32";
//    }
//
//    @Override
//    public String getExt() {
//        return "b32";
//    }
//
//    @Override
//    public int encode(File dst, File src, String[] args) throws IOException {
//        byte[] fileContent = Files.readAllBytes(src.toPath());
//        String encodedString = new Base32().encodeAsString(fileContent);
//        Writer writer = new OutputStreamWriter(new FileOutputStream(String.valueOf(dst.toPath())), StandardCharsets.US_ASCII);
//        try {
//            if (FileChecker.addAttrs(dst,this)) {
//            }else {
//                writer.write(getExt());
//            }
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
//            buf.read(new char[getExt().length()]);}
//        String line = buf.readLine();
//        StringBuilder sb = new StringBuilder();
//        while(line != null)
//        {
//            sb.append(line);
//            line = buf.readLine();
//        }
//        String fileAsString = sb.toString();
////        byte[] decodedBytes = Base64.getDecoder().decode(fileAsString);
//        byte[] decodedBytes = new Base32().decode(fileAsString);
//
//        try (FileOutputStream fos = new FileOutputStream(dst)) {
//            fos.write(decodedBytes);
//        }
//        //String decodedString = new String(decodedBytes);
//        return 0;
//    }
//
//}
