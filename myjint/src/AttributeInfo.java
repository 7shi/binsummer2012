// public domain

public class AttributeInfo {
    
    public Constant[] constant_pool;
    public int attribute_name_index;
    public int attribute_length;
    public byte[] info;
    
    protected void parse() throws Exception {
    }
    
    @Override
    public String toString() {
        return constant_pool[attribute_name_index].toString();
    }
    
    public static AttributeInfo read(BinaryReader br, Constant[] constant_pool)
            throws Exception {
        AttributeInfo ret;
        int attribute_name_index = br.readU2();
        long attribute_length = br.readU4();
        if (attribute_length > Integer.MAX_VALUE) {
            throw new Exception("ABORT: attribute_length is too long");
        }
        switch (constant_pool[attribute_name_index].toString()) {
            case "Code":
                ret = new AttributeCode();
                break;
            default:
                ret = new AttributeInfo();
                break;
        }
        ret.constant_pool = constant_pool;
        ret.attribute_name_index = attribute_name_index;
        ret.attribute_length = (int) attribute_length;
        ret.info = br.readBytes((int) attribute_length);
        ret.parse();
        return ret;
    }
}
