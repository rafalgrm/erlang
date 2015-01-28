import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

public class ErlPorts {
    private static int readLine(byte[] bytes) throws IOException{
            int len;
            if (readExact(bytes, 2) != 2)
                return -1;
            len = (bytes[0] << 8) | bytes[1];
            return readExact(bytes, len);
    }

    private static int writeLine(byte[] bytes, int length) throws UnsupportedEncodingException{
        char[] li;
        li = Character.toChars((length >> 8) & 0xff);
        writeExact(new String(li).getBytes("UTF-8"), 1);
        li = Character.toChars(length & 0xff);
        writeExact(new String(li).getBytes("UTF-8"), 1);
        return writeExact(bytes, length);
    }

    private static int readExact(byte[] buf, int len) throws IOException
    {
        int i, got=0;
        do {
            i = System.in.read(buf,got,len-got);
            if (i <= 0)
                return(i);
            got += i;
        } while (got<len);
        return(len);
    }

    private static int writeExact(byte[] buf, int len)
    {
        int wrote = 0;

        do {
            System.out.write(buf, wrote, len - wrote);

            wrote += 2;
        } while (wrote<len);

        return (len);
    }
    private static int foo(int arg) {
        return arg*2;
    }
    private static int bar(int arg) {
        return arg/2;
    }
    public static void main(String[] args) throws IOException {
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

            buf[0] = (byte)res;//ByteBuffer.allocate(4).putInt(res).array()[0];
            writeLine(buf, 1);
        }
    }
}