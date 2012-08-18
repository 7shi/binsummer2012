// public domain

public class ConstantUtf8 extends Constant {

    public int length;
    public String bytes;

    public ConstantUtf8(BinaryReader br) {
        tag = 1;
        length = br.readU2();
        bytes = br.readString(length);
    }
}
