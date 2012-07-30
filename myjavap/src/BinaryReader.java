
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
}
