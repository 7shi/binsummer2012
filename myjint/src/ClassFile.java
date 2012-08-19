// public domain

public class ClassFile {

    public long magic;
    public int minor_version;
    public int major_version;
    public int constant_pool_count;
    public Constant[] constant_pool;
    public int access_flags;
    public int this_class;
    public int super_class;
    public int interfaces_count;
    public int[] interfaces;
    public int fields_count;
    //public FieldInfo[] fields;
    public int methods_count;
    //public MethodInfo[] methods;
    public int attributes_count;
    public AttributeInfo[] attributes;

    public ClassFile(BinaryReader br) throws Exception {
        magic = br.readU4();
        minor_version = br.readU2();
        major_version = br.readU2();

        constant_pool_count = br.readU2();
        constant_pool = new Constant[constant_pool_count];
        for (int i = 1; i < constant_pool_count; i++) {
            constant_pool[i] = Constant.read(br);
            constant_pool[i].constant_pool = constant_pool;
        }

        access_flags = br.readU2();
        this_class = br.readU2();
        super_class = br.readU2();

        interfaces_count = br.readU2();
        interfaces = new int[interfaces_count];
        for (int i = 0; i < interfaces_count; i++) {
            interfaces[i] = br.readU2();
        }

        fields_count = br.readU2();
        if (fields_count > 0) {
            throw new Exception("ABORT: field not supported");
        }

        methods_count = br.readU2();
        for (int i = 0; i < methods_count; i++) {
        }

        // attributes_count = br.readU2();
        // attributes = new AttributeInfo[attributes_count];
        // for (int i = 0; i < attributes_count; i++) {
        //     attributes[i] = AttributeInfo.read(br, constant_pool_count);
        // }
    }

    public void run() {
    }
}
