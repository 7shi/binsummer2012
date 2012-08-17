# public domain

import sys, binary

indent = 0

def main():
    with open("build/classes/Hello.class", "rb") as f:
        data = f.read()
    binary.dumpHexAscii(data)
    print
    read(data)

def read(data):
    global br, indent, strings
    br = binary.reader(data)
    magic = br.readU4()
    minor_version = br.readU2()
    major_version = br.readU2()
    println("magic: 0x%04x" % magic)
    println("minor_version: %d" % minor_version)
    println("major_version: %d" % major_version)

    constant_pool_count = br.readU2()
    strings = [""] * constant_pool_count
    println("constant_pool_count: %d" % constant_pool_count)
    for i in range(1, constant_pool_count):
        println("#%d" % i)
        indent += 1
        readConstant(i)
        indent -= 1

    access_flags = br.readU2()
    this_class = br.readU2()
    super_class = br.readU2()
    println("access_flags: 0x%04x" % access_flags)
    println("this_class: %d" % this_class)
    println("super_class: %d" % super_class)

    interfaces_count = br.readU2()
    println("interfaces_count: %d" % interfaces_count)
    if interfaces_count > 0:
        raise Exception("ABORT: interface not supported")

    fields_count = br.readU2()
    println("fields_count: %d" % fields_count)
    if fields_count > 0:
        raise Exception("ABORT: field not supported")

    methods_count = br.readU2()
    println("methods_count: %d" % methods_count)
    for i in range(methods_count):
        println("methods[%d]:" % i)
        indent += 1
        readMethod()
        indent -= 1

    attributes_count = br.readU2()
    println("attributes_count: %d" % attributes_count)
    for i in range(attributes_count):
        println("attributes[%d]:" % i)
        indent += 1
        readAttribute()
        indent -= 1

def readConstant(no):
    tag = br.readU1()
    sys.stdout.write(getIndent())
    sys.stdout.write("tag: %d = " % tag)
    if tag == 1:
        print "Utf8"
        length = br.readU2()
        strings[no] = br.readString(length)
        println("length: %d" % length)
        println("bytes: %s" % strings[no])
    elif tag == 7:
        print "Class"
        name_index = br.readU2()
        println("name_index: %d" % name_index)
    elif tag == 8:
        print "String"
        string_index = br.readU2()
        println("string_index: %d" % string_index)
    elif tag == 9:
        print "Fieldref"
        class_index = br.readU2()
        name_and_type_index = br.readU2()
        println("class_index: %d" % class_index)
        println("name_and_type_index: %d" % name_and_type_index)
    elif tag == 10:
        print "Methodref"
        class_index = br.readU2()
        name_and_type_index = br.readU2()
        println("class_index: %d" % class_index)
        println("name_and_type_index: %d" % name_and_type_index)
    elif tag == 12:
        print "NameAndType"
        name_index = br.readU2()
        descriptor_index = br.readU2()
        println("name_index: %d" % name_index)
        println("descriptor_index: %d" % descriptor_index)
    else:
        print "???"
        raise Exception("ABORT: unknown tag")

def readMethod():
    global indent
    access_flags = br.readU2()
    name_index = br.readU2()
    descriptor_index = br.readU2()
    attributes_count = br.readU2()
    println("access_flags: 0x%04x" % access_flags)
    println("name_index: %d \"%s\"" % (
            name_index, strings[name_index]))
    println("descriptor_index: %d \"%s\"" % (
            descriptor_index, strings[descriptor_index]))
    println("attributes_count: %d" % attributes_count)
    for j in range(attributes_count):
        println("attributes[%d]:" % j)
        indent += 1
        readAttribute();
        indent -= 1

def readAttribute():
    global indent
    attribute_name_index = br.readU2()
    attribute_name = strings[attribute_name_index]
    attribute_length = br.readU4()
    println("attribute_name_index: %d \"%s\"" % (
            attribute_name_index, attribute_name))
    println("attribute_length: %d" % attribute_length)
    
    if attribute_name == "Code":
        max_stack = br.readU2()
        max_locals = br.readU2()
        code_length = br.readU4()
        println("max_stack: %d" % max_stack)
        println("max_locals: %d" % max_locals)
        println("code_length: %d" % code_length)

        println("code:")
        indent += 1
        readCode(code_length)
        indent -= 1

        exception_table_length = br.readU2()
        println("exception_table_length: %d" % exception_table_length)
        if exception_table_length > 0:
            raise Exception("ABORT: exception table not supported")

        attributes_count = br.readU2()
        println("attributes_count: %d" % attributes_count)
        for i in range(attributes_count):
            println("attributes[%d]:" % i)
            indent += 1
            readAttribute()
            indent -= 1
    
    elif attribute_name == "LineNumberTable":
        line_number_table_length = br.readU2()
        println("line_number_table_length: %d" %
                line_number_table_length)
        for i in range(line_number_table_length):
            start_pc = br.readU2()
            line_number = br.readU2()
            println("%04x: %d" % (start_pc, line_number))
    
    else:
        info = br.readBytes(attribute_length)
        println("info:")
        indent += 1
        binary.dump(info, getIndent())
        indent -= 1

def readCode(code_length):
    i = 0
    while i < code_length:
        len = 1
        mne = "?"
        op = br.readU1()
        if op == 0x02:
            mne = "iconst_m1"
        elif 0x03 <= op <= 0x08:
            mne = "iconst_%d" % (op - 0x03)
        elif 0x1a <= op <= 0x1d:
            mne = "iload_%d" % (op - 0x1a)
        elif 0x2a <= op <= 0x2d:
            mne = "aload_%d" % (op - 0x2a)
        elif 0x3b <= op <= 0x3e:
            mne = "istore_%d" % (op - 0x3b)
        elif op == 0x60:
            mne = "iadd"
        elif op == 0xb1:
            mne = "return"
        elif op == 0xb2:
            len = 3
            mne = "getstatic #%d" % br.readU2()
        elif op == 0xb6:
            len = 3
            mne = "invokevirtual #%d" % br.readU2()
        elif op == 0xb7:
            len = 3
            mne = "invokespecial #%d" % br.readU2()
        br.seek(-len)
        println("%-10s%s" % (
            " ".join("%02x" % ord(b) for b in br.readBytes(len)),
            mne))
        i += len

def getIndent():
    return "  " * indent

def println(str):
    sys.stdout.write(getIndent())
    print str

main()
