// public domain

public class ConstantMethodref extends Constant {

    public int class_index;
    public int name_and_type_index;

    public ConstantMethodref(BinaryReader br) {
        tag = 10;
        class_index = br.readU2();
        name_and_type_index = br.readU2();
    }

    @Override
    public String toString() {
        Constant cls = constant_pool[class_index];
        Constant name_and_type = constant_pool[name_and_type_index];
        return cls.toString() + "." + name_and_type.toString();
    }
}
