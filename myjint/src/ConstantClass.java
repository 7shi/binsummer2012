// public domain

public class ConstantClass extends Constant {

    private int name_index;

    public ConstantClass(BinaryReader br) {
        tag = 7;
        name_index = br.readU2();
    }

    @Override
    public String toString() {
        return constant_pool[name_index].toString();
    }
}
