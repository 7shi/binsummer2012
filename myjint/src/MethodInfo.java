// public domain

public class MethodInfo {

    public Constant[] constant_pool;
    public int access_flags;
    public int name_index;
    public int descriptor_index;
    public int attributes_count;
    public AttributeInfo[] attributes;
    public AttributeCode code;

    public MethodInfo(BinaryReader br, Constant[] constant_pool)
            throws Exception {
        this.constant_pool = constant_pool;
        access_flags = br.readU2();
        name_index = br.readU2();
        descriptor_index = br.readU2();
        attributes_count = br.readU2();
        attributes = new AttributeInfo[attributes_count];
        for (int i = 0; i < attributes_count; i++) {
            attributes[i] = AttributeInfo.read(br, constant_pool);
            if (attributes[i] instanceof AttributeCode) {
                code = (AttributeCode) attributes[i];
            }
        }
    }

    @Override
    public String toString() {
        return constant_pool[name_index].toString();
    }
}
