// public domain

public class ConstantNameAndType extends Constant {

    public int name_index;
    public int descriptor_index;

    public ConstantNameAndType(BinaryReader br) {
        tag = 12;
        name_index = br.readU2();
        descriptor_index = br.readU2();
    }

    @Override
    public String toString() {
        Constant name = constant_pool[name_index];
        Constant descriptor = constant_pool[descriptor_index];
        return name.toString() + ":" + descriptor.toString();
    }
}
