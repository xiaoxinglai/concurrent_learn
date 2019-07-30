package nio;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @ClassName NioTest
 * @Author laixiaoxing
 * @Date 2019/7/30 上午10:13
 * @Description TODO
 * @Version 1.0
 */
public class NioTest {


    static String inPath = "/Users/laixiaoxing/Documents/concurrent_learn/src/main/resources/data/text";
    static String outPath = "/Users/laixiaoxing/Documents/concurrent_learn/src/main/resources/data/textOut";

    //    Java NIO中的Buffer用于和NIO通道进行交互。如你所知，数据是从通道读入缓冲区，从缓冲区写入到通道中的。
    //
    //    缓冲区本质上是一块可以写入数据，然后可以从中读取数据的内存。这块内存被包装成NIO Buffer对象，并提供了一组方法，用来方便的访问该块内存。
    public static void main(String[] args) throws IOException {

        //        写入数据到Buffer
        //        调用flip()方法
        //                从Buffer中读取数据
        //        调用clear()方法或者compact()方法
        //        当向buffer写入数据时，buffer会记录下写了多少数据。一旦要读取数据，需要通过flip()方法将Buffer从写模式切换到读模式。在读模式下，可以读取之前写入到buffer的所有数据。
        //
        //        一旦读完了所有的数据，就需要清空缓冲区，让它可以再次被写入。有两种方式能清空缓冲区：调用clear()或compact()方法。clear()方法会清空整个缓冲区。compact()方法只会清除已经读过的数据。任何未读的数据都被移到缓冲区的起始处，新写入的数据将放到缓冲区未读数据的后面。

        RandomAccessFile aFile = new RandomAccessFile(inPath, "rw");
        FileChannel inChannel = aFile.getChannel();
//        inChannel.map();
        //create buffer with capacity of 48 bytes
        ByteBuffer buf = ByteBuffer.allocate(48);

        int bytesRead = inChannel.read(buf); //read into buffer.
        while (bytesRead != -1) {

            buf.flip();  //make buffer ready for read

            while (buf.hasRemaining()) {
                System.out.print((char) buf.get()); // read 1 byte at a time
            }

            buf.clear(); //make buffer ready for writing



            buf.put((byte) 'b');
            inChannel.write(buf);
        }
        aFile.close();


    }
}
