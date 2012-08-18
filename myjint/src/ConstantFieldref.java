// public domain

public class ConstantFieldref extends Constant {

    public int class_index;
    public int name_and_type_index;

    public ConstantFieldref(BinaryReader br) {
        tag = 9;
        class_index = br.readU2();
        name_and_type_index = br.readU2();
    }
}
