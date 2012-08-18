// public domain

public class ConstantNameAndType extends Constant {

    public int name_index;
    public int descriptor_index;

    public ConstantNameAndType(BinaryReader br) {
        tag = 12;
        name_index = br.readU2();
        descriptor_index = br.readU2();
    }
}
