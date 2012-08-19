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
        Constant obj_getstatic = null, str_ldc = null;
        for (BinaryReader br = new BinaryReader(main.code.code);;) {
            int b = br.readU1();
            switch (b) {
                case 0x12:   // ldc #u1
                    str_ldc = cf.constant_pool[br.readU1()];
                    break;
                case 0xb1:   // return
                    return;
                case 0xb2:   // getstatic #u2
                    obj_getstatic = cf.constant_pool[br.readU2()];
                    break;
                case 0xb6: { // invokevirtual #2
                    String obj = obj_getstatic.toString();
                    String method = cf.constant_pool[br.readU2()].toString();
                    switch (method) {
                        case "java/io/PrintStream.println:(Ljava/lang/String;)V":
                            if (obj.equals("java/lang/System.out:Ljava/io/PrintStream;")) {
                                System.out.println(str_ldc.toString());
                            } else {
                                throw new Exception("unknown object: " + obj);
                            }
                            break;
                        default:
                            throw new Exception("unknown method: " + method);
                    }
                    break;
                }
                default:
                    throw new Exception(String.format(
                            "unknown opcode: %02x", b));
            }
        }
    }
}
