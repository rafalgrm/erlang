
import java.io.IOException;
import java.nio.ByteBuffer;

public class ErlPorts {

    private static int readLine(byte[] bytes) {
        try {
            int len;
            if (readExact(bytes, 2) != 2)
                return -1;
            len = (bytes[0] << 8) | bytes[1];
            return readExact(bytes, len);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return -1;
    }

    private static int writeLine(byte[] bytes, int length) {
        byte[] li;
        li = ByteBuffer.allocate(4).putInt((length >> 8) & 0xff).array();
        writeExact(li, 1);
        li = ByteBuffer.allocate(4).putInt(length & 0xff).array();
        writeExact(li, 1);
        return writeExact(bytes, length);
    }

    private static int readExact(byte[] bytes, int length) throws IOException{
        int in_count = length;
        int offset = 0;
        do
        {
            int c = System.in.read(bytes, offset, in_count - offset);
            offset += c;
        }
        while (offset < in_count);
        return(in_count);
    }

    //TODO:
    private static int writeExact(byte[] bytes, int length) {
        int out_count = length;
        int offset = 0;
        do
        {
            System.out.write (bytes, 0, out_count);
            offset += 2;
        }
        while (offset < out_count);
        return(out_count);
    }

    //TODO: foo, bar(nie bardzo wiem co to funkcje maj¹ robiæ)

    private static int foo(int arg) {
        return arg*2;
    }

    private static int bar(int arg) {
        return arg/2;
    }

    public static void main(String[] args) {
        int fn, arg, res = -1;
        byte[] buf = new byte[100];

        while (readLine(buf) > 0) {
            fn = buf[0];
            arg = buf[1];

            if (fn == 1) {
                res = foo(arg);
            } else if (fn == 2) {
                res = bar(arg);
            }
            buf[0] = ByteBuffer.allocate(4).putInt(res).array()[0];
            writeLine(buf, 1);
        }
    }
}
