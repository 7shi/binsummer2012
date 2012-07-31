# public domain

import sys

class reader:
    def __init__(self, data):
        self.pos = 0
        self.data = data

    def readU1(self):
        ret = ord(self.data[self.pos])
        self.pos += 1
        return ret

    def readU2(self):
        ret = (ord(self.data[self.pos]) << 8) \
            |  ord(self.data[self.pos + 1])
        self.pos += 2
        return ret

    def readU4(self):
        ret = (ord(self.data[self.pos    ]) << 24) \
            | (ord(self.data[self.pos + 1]) << 16) \
            | (ord(self.data[self.pos + 2]) <<  8) \
            |  ord(self.data[self.pos + 3])
        self.pos += 4
        return ret

    def readBytes(self, len):
        ret = self.data[self.pos : self.pos + len]
        self.pos += len
        return ret

    def readString(self, len):
        return self.readBytes(len).decode("utf8")

def dumpHexAscii(data):
    i = 0
    while i < len(data):
        sys.stdout.write("%08X " % i)
        sb = "  "
        for j in range(16):
            if j == 8: sys.stdout.write(" ")
            if i + j < len(data):
                b = ord(data[i + j])
                sys.stdout.write(" %02X" % b)
                sb += "." if b < 32 or b >= 127 else chr(b)
            else:
                sys.stdout.write("   ")
        print sb
        i += 16

def dump(data, indent):
    i = 0
    while i < len(data):
        sys.stdout.write(indent)
        for j in range(16):
            if i + j >= len(data): break
            if j > 0: sys.stdout.write(" ")
            sys.stdout.write("%02x" % ord(data[i + j]))
        print
        i += 16
