// public domain

public abstract class Constant {

    public Constant[] constant_pool;
    public int tag;

    public static Constant read(BinaryReader br) {
        switch (br.readU1()) {
            case 1:
                return new ConstantUtf8(br);
            case 7:
                return new ConstantClass(br);
            case 8:
                return new ConstantString(br);
            case 9:
                return new ConstantFieldref(br);
            case 10:
                return new ConstantMethodref(br);
            case 12:
                return new ConstantNameAndType(br);
            default:
                return null;
        }
    }
}
