// public domain

import java.io.File;
import java.io.FileInputStream;

public class Myjavap {

    private static int indent = 0;
    private static BinaryReader br;
    private static String[] strings;

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
            BinaryReader.dumpHexAscii(data);
            System.out.println();
            br = new BinaryReader(data);
            read();
        } catch (Exception ex) {
            System.out.println(ex);
        }
    }

    public static void read() throws Exception {
        long magic = br.readU4();
        int minor_version = br.readU2();
        int major_version = br.readU2();
        printfln("magic: 0x%04x", magic);
        printfln("minor_version: %d", minor_version);
        printfln("major_version: %d", major_version);

        int constant_pool_count = br.readU2();
        strings = new String[constant_pool_count];
        printfln("constant_pool_count: %d", constant_pool_count);
        for (int i = 1; i < constant_pool_count; i++) {
            printfln("#%d", i);
            ++indent;
            readConstant(i);
            --indent;
        }

        int access_flags = br.readU2();
        int this_class = br.readU2();
        int super_class = br.readU2();
        printfln("access_flags: 0x%04x", access_flags);
        printfln("this_class: %d", this_class);
        printfln("super_class: %d", super_class);

        int interfaces_count = br.readU2();
        printfln("interfaces_count: %d", interfaces_count);
        if (interfaces_count > 0) {
            throw new Exception("ABORT: interface not supported");
        }

        int fields_count = br.readU2();
        printfln("fields_count: %d", fields_count);
        if (fields_count > 0) {
            throw new Exception("ABORT: field not supported");
        }

        int methods_count = br.readU2();
        printfln("methods_count: %d", methods_count);
        for (int i = 0; i < methods_count; i++) {
            printfln("methods[%d]:", i);
            ++indent;
            readMethod();
            --indent;
        }

        int attributes_count = br.readU2();
        printfln("attributes_count: %d", attributes_count);
        for (int i = 0; i < attributes_count; i++) {
            printfln("attributes[%d]:", i);
            ++indent;
            readAttribute();
            --indent;
        }
    }

    public static void readConstant(int no) throws Exception {
        int tag = br.readU1();
        System.out.print(getIndent());
        System.out.printf("tag: %d = ", tag);
        switch (tag) {
            case 1: {
                System.out.println("Utf8");
                int length = br.readU2();
                strings[no] = br.readString(length);
                printfln("length: %d", length);
                printfln("bytes: %s", strings[no]);
                break;
            }
            case 7: {
                System.out.println("Class");
                int name_index = br.readU2();
                printfln("name_index: %d", name_index);
                break;
            }
            case 8: {
                System.out.println("String");
                int string_index = br.readU2();
                printfln("string_index: %d", string_index);
                break;
            }
            case 9: {
                System.out.println("Fieldref");
                int class_index = br.readU2();
                int name_and_type_index = br.readU2();
                printfln("class_index: %d", class_index);
                printfln("name_and_type_index: %d", name_and_type_index);
                break;
            }
            case 10: {
                System.out.println("Methodref");
                int class_index = br.readU2();
                int name_and_type_index = br.readU2();
                printfln("class_index: %d", class_index);
                printfln("name_and_type_index: %d", name_and_type_index);
                break;
            }
            case 12: {
                System.out.println("NameAndType");
                int name_index = br.readU2();
                int descriptor_index = br.readU2();
                printfln("name_index: %d", name_index);
                printfln("descriptor_index: %d", descriptor_index);
                break;
            }
            default: {
                System.out.println("???");
                throw new Exception("ABORT: unknown tag");
            }
        }
    }

    public static void readMethod() throws Exception {
        int access_flags = br.readU2();
        int name_index = br.readU2();
        int descriptor_index = br.readU2();
        int attributes_count = br.readU2();
        printfln("access_flags: 0x%04x", access_flags);
        printfln("name_index: %d \"%s\"",
                name_index, strings[name_index]);
        printfln("descriptor_index: %d \"%s\"",
                descriptor_index, strings[descriptor_index]);
        printfln("attributes_count: %d", attributes_count);
        for (int j = 0; j < attributes_count; j++) {
            printfln("attributes[%d]:", j);
            ++indent;
            readAttribute();
            --indent;
        }
    }

    public static void readAttribute() throws Exception {
        int attribute_name_index = br.readU2();
        String attribute_name = strings[attribute_name_index];
        long attribute_length = br.readU4();
        printfln("attribute_name_index: %d \"%s\"",
                attribute_name_index, attribute_name);
        printfln("attribute_length: %d", attribute_length);
        if (attribute_length > Integer.MAX_VALUE) {
            throw new Exception("ABORT: attribute_length is too long");
        }
        switch (attribute_name) {
            case "Code": {
                int max_stack = br.readU2();
                int max_locals = br.readU2();
                long code_length = br.readU4();
                if (code_length > Integer.MAX_VALUE) {
                    throw new Exception("ABORT: code_length is too long");
                }
                printfln("max_stack: %d", max_stack);
                printfln("max_locals: %d", max_locals);
                printfln("code_length: %d", code_length);

                printfln("code:");
                ++indent;
                readCode((int) code_length);
                --indent;

                int exception_table_length = br.readU2();
                printfln("exception_table_length: %d", exception_table_length);
                if (exception_table_length > 0) {
                    throw new Exception("ABORT: exception table not supported");
                }

                int attributes_count = br.readU2();
                printfln("attributes_count: %d", attributes_count);
                for (int i = 0; i < attributes_count; i++) {
                    printfln("attributes[%d]:", i);
                    ++indent;
                    readAttribute();
                    --indent;
                }
                break;
            }
            case "LineNumberTable": {
                int line_number_table_length = br.readU2();
                printfln("line_number_table_length: %d",
                        line_number_table_length);
                for (int i = 0; i < line_number_table_length; i++) {
                    int start_pc = br.readU2();
                    int line_number = br.readU2();
                    printfln("%04x: %d", start_pc, line_number);
                }
                break;
            }
            default: {
                byte[] info = br.readBytes((int) attribute_length);
                printfln("info:");
                ++indent;
                BinaryReader.dump(info, getIndent());
                --indent;
                break;
            }
        }
    }

    public static void readCode(int code_length) {
        for (int i = 0; i < code_length;) {
            int len = 1;
            String mne = "?";
            int op = br.readU1();
            switch (op) {
                case 0x02:
                    mne = "iconst_m1";
                    break;
                case 0x03:
                case 0x04:
                case 0x05:
                case 0x06:
                case 0x07:
                case 0x08:
                    mne = "iconst_" + (op - 0x03);
                    break;
                case 0x1a:
                case 0x1b:
                case 0x1c:
                case 0x1d:
                    mne = "iload_" + (op - 0x1a);
                    break;
                case 0x2a:
                case 0x2b:
                case 0x2c:
                case 0x2d:
                    mne = "aload_" + (op - 0x2a);
                    break;
                case 0x3b:
                case 0x3c:
                case 0x3d:
                case 0x3e:
                    mne = "istore_" + (op - 0x3b);
                    break;
                case 0x60:
                    mne = "iadd";
                    break;
                case 0xb1:
                    mne = "return";
                    break;
                case 0xb2:
                    len = 3;
                    mne = "getstatic #" + br.readU2();
                    break;
                case 0xb6:
                    len = 3;
                    mne = "invokevirtual #" + br.readU2();
                    break;
                case 0xb7:
                    len = 3;
                    mne = "invokespecial #" + br.readU2();
                    break;
            }
            StringBuilder sb = new StringBuilder();
            br.seek(-len);
            for (byte b : br.readBytes(len)) {
                sb.append(String.format("%02x ", b));
            }
            printfln("%-10s%s", sb, mne);
            i += len;
        }
    }

    public static String getIndent() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            sb.append("  ");
        }
        return sb.toString();
    }

    public static void printfln(String format, Object... args) {
        System.out.print(getIndent());
        System.out.printf(format, args);
        System.out.println();
    }
}
