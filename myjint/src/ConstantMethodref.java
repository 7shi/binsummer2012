// public domain

public class ConstantMethodref extends Constant {

    public int class_index;
    public int name_and_type_index;

    public ConstantMethodref(BinaryReader br) {
        tag = 10;
        class_index = br.readU2();
        name_and_type_index = br.readU2();
    }
}
