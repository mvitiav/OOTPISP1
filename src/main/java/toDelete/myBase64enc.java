//package toDelete;
//
//import java.io.*;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.Files;
//import java.nio.file.attribute.UserDefinedFileAttributeView;
//import java.util.Base64;
//
//import static java.nio.file.Files.getFileStore;
//
//public class myBase64enc implements PlugTest {
//    @Override
//    public String getName() {
//        return "Base64";
//    }
//
//    @Override
//    public String getExt() {
//        return "b64";
//    }
//
//    @Override
//    public int encode(File dst, File src, String[] args) throws IOException {
//        byte[] fileContent = Files.readAllBytes(src.toPath());
//        String encodedString = Base64.getEncoder().encodeToString(fileContent);
//       // System.out.println(encodedString);
//
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
//    public boolean checkFile(File src) {
//        return FileChecker.checkFileForV1(src,this);
//    }
//
//    @Override
//
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
//        line = buf.readLine();
//        }
//        String fileAsString = sb.toString();
//        byte[] decodedBytes = Base64.getDecoder().decode(fileAsString);
//
//        try (FileOutputStream fos = new FileOutputStream(dst)) {
//            fos.write(decodedBytes);
//        }
//        //String decodedString = new String(decodedBytes);
//        return 0;
//    }
//
//}
