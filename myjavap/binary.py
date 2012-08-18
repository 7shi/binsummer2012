# public domain

import struct

class reader:
    def __init__(self, data):
        self.pos = 0
        self.data = data

    def readU1(self):
        ret = ord(self.data[self.pos])
        self.pos += 1
        return ret

    def readU2(self):
        ret, = struct.unpack(">H", self.data[self.pos:self.pos+2])
        self.pos += 2
        return ret

    def readU4(self):
        ret, = struct.unpack(">L", self.data[self.pos:self.pos+4])
        self.pos += 4
        return ret

    def readBytes(self, len):
        ret = self.data[self.pos : self.pos + len]
        self.pos += len
        return ret

    def readString(self, len):
        return self.readBytes(len).decode("utf8")

def dumpHexAscii(data):
    for i in range(0, len(data), 16):
        buf = data[i : min(i + 16, len(data))]
        dump = " ".join("%02X" % ord(x) for x in buf).ljust(47)
        print "%08X %s-%s %s" % (i, dump[:23], dump[24:],
            "".join(x if " " <= x <= "~" else "." for x in buf))

def dump(data, indent):
    for i in range(0, len(data), 16):
        buf = data[i : min(i + 16, len(data))]
        print indent + " ".join("%02x" % ord(x) for x in buf)
