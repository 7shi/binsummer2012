// public domain

public class AttributeCode extends AttributeInfo {

    public int max_stack;
    public int max_locals;
    public int code_length;
    public byte[] code;
    public int exception_table_length;
    public int attributes_count;
    public AttributeInfo[] attributes;

    @Override
    protected void parse() throws Exception {
        BinaryReader br = new BinaryReader(info);
        max_stack = br.readU2();
        max_locals = br.readU2();

        long code_length = br.readU4();
        if (code_length > Integer.MAX_VALUE) {
            throw new Exception("ABORT: code_length is too long");
        }
        this.code_length = (int) code_length;
        code = br.readBytes((int) code_length);

        exception_table_length = br.readU2();
        if (exception_table_length > 0) {
            throw new Exception("ABORT: exception table not supported");
        }

        attributes_count = br.readU2();
        attributes = new AttributeInfo[attributes_count];
        for (int i = 0; i < attributes_count; i++) {
            attributes[i] = read(br, constant_pool);
        }
    }
}
