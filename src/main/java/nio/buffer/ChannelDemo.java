package nio.buffer;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.*;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static jdk.nashorn.internal.objects.NativeArray.map;

/**
 * @ClassName ChannelDemo
 * @Author laixiaoxing
 * @Date 2019/8/1 下午12:48
 * @Description 通道基础
 * @Version 1.0
 */
public class ChannelDemo {

    //    通道(Channel)是 java.nio 的第二个主要创新。它们既不是一个扩展也不是一项增强，而 是全新、极好的 Java I/O 示例，提供与 I/O 服务的直接连接。Channel 用于在字节缓冲区和位于通 道另一侧的实体(通常是一个文件或套接字)之间有效地传输数据

    //    多数情况下，通道与操作系统的文件描述符(File Descriptor)和文件句柄(File Handle)有着 一对一的关系。虽然通道比文件描述符更广义，但您将经常使用到的多数通道都是连接到开放的文 件描述符的。Channel 类提供维持平台独立性所需的抽象过程，不过仍然会模拟现代操作系统本身 的 I/O 性能。

    //I/O 可以分为广义的两大类别: File I/O 和 Stream I/O。那么相应地有两种类型的通道也就不足为怪了，它们是文件(file)通道和 套接字(socket)通道

    //    Socket 通道有可以直接创建新 socket 通道的工厂方法。但是一个 FileChannel 对象却只能通过在一个打开的 RandomAccessFile、FileInputStream 或 FileOutputStream 对象上调用 getChannel( )方法来获取。您不能直接创建一个 FileChannel 对象


    //打开客户端Socket通道
    //    SocketChannel sc = SocketChannel.open( );
    //绑定地址
    //sc.connect (new InetSocketAddress ("somehost", someport));

    //打开服务端通道
    //    ServerSocketChannel ssc = ServerSocketChannel.open( );
    //绑定地址
    //ssc.socket( ).bind (new InetSocketAddress (somelocalport));

    //    DatagramChannel dc = DatagramChannel.open( );
    //打开文件获取
    //    RandomAccessFile raf = new RandomAccessFile ("somefile", "r");
    //    FileChannel fc = raf.getChannel( );


    //读
    //    public interface ReadableByteChannel
    //            extends Channel
    //    {
    //        public int read (ByteBuffer dst) throws IOException;
    //
    //    }

    //写
    //    public interface WritableByteChannel
    //            extends Channel
    //    {
    //        public int write (ByteBuffer src) throws IOException;
    //    }

    //继承读和写的字节通道
    //    public interface ByteChannel
    //            extends ReadableByteChannel, WritableByteChannel
    //    { }


    //    通道可以是单向(unidirectional)或者双向的(bidirectional)。一个 channel 类可能实现定义 read( )方法的 ReadableByteChannel 接口，而另一个 channel 类也许实现 WritableByteChannel 接口以 提供 write( )方法。实现这两种接口其中之一的类都是单向的，只能在一个方向上传输数据。如果 一个类同时实现这两个接口，那么它是双向的，可以双向传输数据。


    //    Channel 接口本身并不定义新的 API 方法，它是一种用来聚集 它自己以一个新名称继承的多个接口的便捷接口。根据定义，实现 ByteChannel 接口的通道会同时 实现 ReadableByteChannel 和 WritableByteChannel 两个接口，所以此类通道是双向的。这是简化类 定义的语法糖(syntactic sugar)，它使得用操作器(operator)实例来测试通道对象变得更加简 单。
    //    这是一种好的类设计技巧，如果您在写您自己的 Channel 实现的话，您可以适当地实现这些接 口。不过对于使用 java.nio.channels 包中标准通道类的程序员来说，这些接口并没有太大的 吸引力。假如您快速回顾一下图 3-2 或者向前跳跃到关于 file 和 socket 通道的章节，您将发现每一 个 file 或 socket 通道都实现全部三个接口。从类定义的角度而言，这意味着全部 file 和 socket 通道
    //            对象都是双向的


    //    我们知道，一个文件可以在不同的时候以不同的权限打开。从 FileInputStream 对象的 getChannel( )方法获取的 FileChannel 对象是只读的，不过从接口声明的角度来看却是双向的，因为 FileChannel 实现 ByteChannel 接口。在这样一个通道上调用 write( )方法将抛出未经检查的 NonWritableChannelException 异常，因为 FileInputStream 对象总是以 read-only 的权限打开文件。
    //    通道会连接一个特定 I/O 服务且通道实例(channel instance)的性能受它所连接的 I/O 服务的 特征限制，记住这很重要。一个连接到只读文件的 Channel 实例不能进行写操作，即使该实例所属 的类可能有 write( )方法。基于此，程序员需要知道通道是如何打开的，避免试图尝试一个底层 I/O 服务不允许的操作。


    //   根据底层文件句柄的访问模式，通道实例可能不允许使用 read()或 write()方 法。

    public static void main(String[] args) throws Exception {

        //通过chanel去读文件
        FileInputStream input = new FileInputStream(
                "/Users/laixiaoxing/Documents/concurrent_learn/src/main/resources/data/text");
        FileChannel channel = input.getChannel();
        //ByteBuffer read=ByteBuffer.allocate(1);
        //与io交互使用直接缓冲区，避免性能损失，因为普通缓冲区也会创建临时直接缓冲区然后拷贝一份数据过去 再和io交互
        ByteBuffer read = ByteBuffer.allocateDirect(100);
        channel.read(read, 4);
        read.flip();
        while (read.hasRemaining()) {
            System.out.println((char) read.get());
        }
        //写入缓冲区，但是这样没有写入权限，因为FileInputStream打开的时候就是以只读的形式
        //  从 FileInputStream 对象的 getChannel( )方法获取的 FileChannel 对象是只读的

        //        ByteBuffer writer=ByteBuffer.allocateDirect(100);
        //        writer.putChar('x');
        //        channel.write(writer,channel.position());

        //        与缓冲区不同，通道不能被重复使用。一个打开的通道即代表与一个特定 I/O 服务的特定连接
        //        并封装该连接的状态。当通道关闭时，那个连接会丢失，然后通道将不再连接任何东西。


        //通道之间复制数据 就是在往文件里面写数据. 将FileInputStream换成FileOutputStream 输出流
        //获取到chanel即可
        ReadableByteChannel source = Channels.newChannel(System.in);
        FileOutputStream fileInputStream = new FileOutputStream(
                "/Users/laixiaoxing/Documents/concurrent_learn/src/main/resources/data/text");
        WritableByteChannel dest = fileInputStream.getChannel();
        //channelCopy1(source, dest);
        // alternatively, call
      //  channelCopy2(source, dest);
        source.close();
        dest.close();



//        通道可以以阻塞(blocking)或非阻塞(nonblocking)模式运行。非阻塞模式的通道永远不会 让调用的线程休眠。请求的操作要么立即完成，要么返回一个结果表明未进行任何操作。只有面向
//        流的(stream-oriented)的通道，如 sockets 和 pipes 才能使用非阻塞模式

//        Scatter/Gather
       // 通道提供了一种被称为 Scatter/Gather 的重要新功能(有时也被称为矢量 I/O)

//        它是指在多个缓冲区上实现一个简单的 I/O 操作。对 于一个 write 操作而言，数据是从几个缓冲区按顺序抽取(称为 gather)并沿着通道发送的。缓冲 区本身并不需要具备这种 gather 的能力(通常它们也没有此能力)。该 gather 过程的效果就好比全 部缓冲区的内容被连结起来，并在发送数据前存放到一个大的缓冲区中。对于 read 操作而言，从 通道读取的数据会按顺序被散布(称为 scatter)到多个缓冲区，将每个缓冲区填满直至通道中的数
//
//        一开始的 NIO 版本 JDK1.4.0 有几个与中断的通道操作以及异步可关闭性相 关的严重缺陷。这些问题期待在 1.4.1 版本中的到解决。在 1.4.0 版本中，当 休眠在一个通道 I/O 操作上的线程被阻塞或者该通道被另一个线程关闭时， 这些线程也许不能确保都能被唤醒。因此在使用依靠该行为的操作时请务必 小心。
//        据或者缓冲区的最大空间被消耗完

//        大多数现代操作系统都支持本地矢量 I/O(native vectored I/O)。
        // 当您在一个通道上请求一个 Scatter/Gather 操作时，该请求会被翻译为适当的本地调用来直接填充或抽取缓冲区。这是一个很大 的进步，因为减少或避免了缓冲区拷贝和系统调用。Scatter/Gather 应该使用直接的 ByteBuffers 以从 本地 I/O 获取最大性能优势。




        //通过chanel去读文件
        FileInputStream inputStream = new FileInputStream(
                "/Users/laixiaoxing/Documents/concurrent_learn/src/main/resources/data/headerAndbody");
        ByteBuffer header = ByteBuffer.allocateDirect (6);
        ByteBuffer body = ByteBuffer.allocateDirect (4);
        ByteBuffer [] buffers = { header, body };
        FileChannel  Gather= inputStream.getChannel();
        int bytesRead = (int) Gather.read (buffers);
        System.out.println("使用Gather的形式去读");
        header.flip();
        body.flip();
        while(header.hasRemaining()){
            System.out.print((char)header.get());
            //输出header
        }
        System.out.println();
        while(body.hasRemaining()){
            System.out.print((char)body.get());
            //输出body
        }
        System.out.println();
        System.out.println("使用Scatter的形式去写");

        FileOutputStream outputStream = new FileOutputStream(
                "/Users/laixiaoxing/Documents/concurrent_learn/src/main/resources/data/scatter");
        FileChannel  scatter= outputStream.getChannel();
        header.clear();
        body.clear();
        byte[] Byte_header={'a','a','a','a','a','a'};
        header.put(Byte_header);
        byte[] Byte_body={'b','b','b','b'};
        body.put(Byte_body);

        //重置位置
        header.flip();
        body.flip();
        //写入了 aaaaaabbbb 按数组buffers{header,body}的顺序写入了
        scatter.write(buffers);

        //假设我们有一个五元素的 fiveBuffers 阵列，它已经被 初始化并引用了五个缓冲区，下面的代码将会写第二个、第三个和第四个缓冲区的内容:
       // int bytesRead = channel.write (fiveBuffers, 1, 3);


//
//        使用得当的话，Scatter/Gather 会是一个极其强大的工具。它允许您委托操作系统来完成辛苦 活:将读取到的数据分开存放到多个存储桶(bucket)或者将不同的数据区块合并成一个整体。这 是一个巨大的成就，因为操作系统已经被高度优化来完成此类工作了。它节省了您来回移动数据的
//        工作，也就避免了缓冲区拷贝和减少了您需要编写、调试的代码数量。既然您基本上通过提供数据 容器引用来组合数据，那么按照不同的组合构建多个缓冲区阵列引用，各种数据区块就可以以不同 的方式来组合了


        //复杂的buff用scatter一起写
        int reps = 10;
        FileOutputStream fos = new FileOutputStream ("/Users/laixiaoxing/Documents/concurrent_learn/src/main/resources/data/FUZAscatter");
        GatheringByteChannel gatherChannel = fos.getChannel( );

        ByteBuffer [] bs = utterBS (reps);

        while (gatherChannel.write (bs) > 0) {
            // Empty body
            // Loop until write( ) returns zero
        }
        System.out.println ("Mindshare paradigms synergized to scatter");
        fos.close( );
    }

    private static String [] col1 = {
            "Aggregate", "Enable", "Leverage",
            "Facilitate", "Synergize", "Repurpose",
            "Strategize", "Reinvent", "Harness"
    };

    private static String [] col2 = {
            "cross-platform", "best-of-breed", "frictionless",
            "ubiquitous", "extensible", "compelling",
            "mission-critical", "collaborative", "integrated"
    };


    private static String [] col3 = {
            "methodologies", "infomediaries", "platforms",
            "schemas", "mindshare", "paradigms",
            "functionalities", "web services", "infrastructures"
    };


    private static String newline = System.getProperty ("line.separator");
    // The Marcom-atic 9000
    private static ByteBuffer [] utterBS (int howMany)
            throws Exception
    {
        List list = new LinkedList( );
        for (int i = 0; i < howMany; i++) {
            list.add (pickRandom (col1, " "));
            list.add (pickRandom (col2, " "));
            list.add (pickRandom (col3, newline));
        }
        ByteBuffer [] bufs = new ByteBuffer [list.size( )];
        list.toArray (bufs);
        return (bufs);
    }

    private static Random rand = new Random( );

    private static ByteBuffer pickRandom (String [] strings, String suffix)
            throws Exception
    {
        String string = strings [rand.nextInt (strings.length)];
        int total = string.length() + suffix.length( );
        ByteBuffer buf = ByteBuffer.allocate (total);
        buf.put (string.getBytes ("US-ASCII"));
        buf.put (suffix.getBytes ("US-ASCII"));
        buf.flip( );
        return (buf);
    }


    private static void channelCopy1(ReadableByteChannel src, WritableByteChannel dest) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);
        while (src.read(buffer) != -1) {
            // Prepare the buffer to be drained
            buffer.flip();
            // Write to the channel; may block
            dest.write(buffer);
            // If partial transfer, shift remainder down
            // If buffer is empty, same as doing clear( )
            buffer.compact();
        }
        // EOF will leave buffer in fill state
        buffer.flip();
        // Make sure that the buffer is fully drained
        while (buffer.hasRemaining()) {
            dest.write(buffer);
        }
    }


    private static void channelCopy2(ReadableByteChannel src, WritableByteChannel dest) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);
        while (src.read(buffer) != -1) {
            // Prepare the buffer to be drained
            buffer.flip();
            // Make sure that the buffer was fully drained
            while (buffer.hasRemaining()) {
                dest.write(buffer);
            }
            // Make the buffer empty, ready for filling
            buffer.clear();
        }
    }
}


