// public domain

public class ConstantString extends Constant {

    public int string_index;

    public ConstantString(BinaryReader br) {
        tag = 8;
        string_index = br.readU2();
    }

    @Override
    public String toString() {
        return constant_pool[string_index].toString();
    }
}
