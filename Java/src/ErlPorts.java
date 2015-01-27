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
            System.out.write(buf,wrote,len-wrote);
            if (len-wrote <= 0)
                return (len-wrote);
            wrote += len-wrote;
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
       /* int fn, arg, res = -1;
        byte[] buf = new byte[100];*/
        /*while (readLine(buf) > 0) {
            fn = buf[0];
            arg = buf[1];
            System.out.print(fn);
            if (fn == 1) {
                res = foo(arg);
            } else if (fn == 2) {
                res = bar(arg);
            }
            buf[0] = ByteBuffer.allocate(4).putInt(res).array()[0];
            writeLine(buf, 1);
        }
        */
        //buf[0]=1;
        //writeLine("sss".getBytes(), 1);
        System.out.write("ssss".getBytes());
    }
}