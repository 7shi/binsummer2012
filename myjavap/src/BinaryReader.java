// public domain

public class BinaryReader {

    private int pos;
    private byte[] data;

    public BinaryReader(byte[] data) {
        this.data = data;
    }

    public int readU1() {
        int ret = data[pos] & 0xff;
        ++pos;
        return ret;
    }

    public int readU2() {
        int ret = ((data[pos] & 0xff) << 8) | (data[pos + 1] & 0xff);
        pos += 2;
        return ret;
    }

    public long readU4() {
        long ret = (((long) (data[pos] & 0xff)) << 24)
                | ((data[pos + 1] & 0xff) << 16)
                | ((data[pos + 2] & 0xff) << 8)
                | (data[pos + 3] & 0xff);
        pos += 4;
        return ret;
    }

    public byte[] readBytes(int len) {
        byte[] ret = new byte[len];
        System.arraycopy(data, pos, ret, 0, len);
        pos += len;
        return ret;
    }

    public String readString(int len) {
        String ret;
        try {
            ret = new String(data, pos, len, "UTF-8");
        } catch (Exception ex) {
            char[] buf = new char[len];
            for (int i = 0; i < len; i++) {
                buf[i] = (char) (data[pos + i] & 0xff);
            }
            ret = new String(buf);
        }
        pos += len;
        return ret;
    }

    public void seek(int offset) {
        pos += offset;
    }

    public static void dumpHexAscii(byte[] data) {
        for (int i = 0; i < data.length; i += 16) {
            System.out.printf("%08X", i);
            StringBuilder sb = new StringBuilder(" ");
            for (int j = 0; j < 16; j++) {
                System.out.print(j == 8 ? "-" : " ");
                if (i + j < data.length) {
                    byte b = data[i + j];
                    System.out.printf("%02X", b);
                    sb.append(b < 32 || b >= 127 ? '.' : (char) b);
                } else {
                    System.out.print("  ");
                }
            }
            System.out.println(sb.toString());
        }
    }

    public static void dump(byte[] data, String indent) {
        for (int i = 0; i < data.length; i += 16) {
            System.out.print(indent);
            for (int j = 0; j < 16; j++) {
                if (i + j >= data.length) {
                    break;
                }
                if (j > 0) {
                    System.out.print(" ");
                }
                System.out.printf("%02x", data[i + j]);
            }
            System.out.println();
        }
    }
}
