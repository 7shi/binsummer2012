// public domain

import java.io.File;
import java.io.FileInputStream;

public class Myjint {

    private static BinaryReader br;

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
            br = new BinaryReader(data);
            read();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    private static void read() {
    }
}
