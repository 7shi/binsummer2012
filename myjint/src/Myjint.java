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
            run(new ClassFile(new BinaryReader(data)));
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }
    
    public static void run(ClassFile cf) throws Exception {
        MethodInfo main = null;
        for (MethodInfo mi : cf.methods) {
            if (mi.toString().equals("main")) {
                main = mi;
            }
        }
        if (main == null) {
            throw new Exception("can not find main");
        }
        for (byte b : main.code.code) {
            System.out.printf("%02x\n", b);
        }
    }
}
