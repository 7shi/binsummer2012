// public domain

import java.io.File;
import java.io.FileInputStream;

public class Myjint {

    public static void main(String[] args) {
        try {
            File f = new File("build/classes/Hello.class");
            long length = f.length();
            if (length > Integer.MAX_VALUE) {
                throw new Exception("file is too long");
            }
            byte[] data;
            try (FileInputStream s = new FileInputStream(f.getPath())) {
                data = new byte[(int) length];
                s.read(data, 0, (int) length);
            }
            BinaryReader br = new BinaryReader(data);
            ClassFile cf = new ClassFile(br);
            cf.run();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
}
