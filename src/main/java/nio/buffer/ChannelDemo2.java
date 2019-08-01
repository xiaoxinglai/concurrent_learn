package nio.buffer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @ClassName ChannelDemo2
 * @Author laixiaoxing
 * @Date 2019/8/1 下午11:08
 * @Description 文件通道
 * @Version 1.0
 */
public class ChannelDemo2 {
    //   文件通道总是阻塞式的，因此不能被置于非阻塞模式。现代操作系统都有复杂的缓存和预取机 制，使得本地磁盘 I/O 操作延迟很少
    //    FileChannel对象不能直接创建。一个FileChannel实例只能通过在一个 打开的file对象(RandomAccessFile、FileInputStream或 FileOutputStream)上调用getChannel( )方法 获取 调用getChannel( )方法会返回一个连接到相同文件的FileChannel对象且该FileChannel对象 具有与file对象相同的访问权限
    //
    //    FileChannel 对象是线程安全(thread-safe)的。多个进程可以在同一个实例上并发调用方法而 不会引起任何问题，不过并非所有的操作都是多线程的(multithreaded)。影响通道位置或者影响 文件大小的操作都是单线程的(single-threaded)


//    同大多数 I/O 相关的类一样，FileChannel 是一个反映 Java 虚拟机外部一个具体对象的抽象。 FileChannel 类保证同一个 Java 虚拟机上的所有实例看到的某个文件的视图均是一致的，但是 Java 虚拟机却不能对超出它控制范围的因素提供担保。通过一个 FileChannel 实例看到的某个文件的视 图同通过一个外部的非 Java 进程看到的该文件的视图可能一致，也可能不一致。多个进程发起的 并发文件访问的语义高度取决于底层的操作系统和(或)文件系统。一般而言，由运行在不同 Java 虚拟机上的 FileChannel 对象发起的对某个文件的并发访问和由非 Java 进程发起的对该文件的并发 访问是一致的。

    public static void main(String[] args) throws IOException {
        RandomAccessFile file = new RandomAccessFile(
                "/Users/laixiaoxing/Documents/concurrent_learn/src/main/resources/data/RandomAccessFile", "rw");
        FileChannel fileChannel = file.getChannel();
        ByteBuffer byteBuffer=ByteBuffer.allocateDirect(100);
        byteBuffer.put("laixiaoxingiii".getBytes("US-ASCII"));
        byteBuffer.flip();
        fileChannel.write(byteBuffer);

        byteBuffer.flip();
        byteBuffer.put("wangruipeng".getBytes("US-ASCII"));
        byteBuffer.flip();
        fileChannel.write(byteBuffer);




      //  每个 FileChannel 对象都同一个文件描述符(file descriptor)有一对一的关系

     //   在通道出现之前，底层的文 件操作都是通过 RandomAccessFile 类的方法来实现的。FileChannel 模拟同样的 I/O 服务，因此它 的 API 自然也是很相似的。

//        同底层的文件描述符一样，每个 FileChannel 都有一个叫“file position”的概念。这个 position 值 决定文件中哪一处的数据接下来将被读或者写。从这个方面看，FileChannel 类同缓冲区很类似， 并且 MappedByteBuffer 类使得我们可以通过 ByteBuffer API 来访问文件数据


        System.out.println("fileChannel的postion:"+fileChannel.position());
//        有两种形式的position( )方法。第一种，不带参数的，返回当 前文件的position值。返回值是一个长整型(long)，表示文件中的当前字节位置 22。

//        第二种形式的 position( )方法带一个 long(长整型)参数并将通道的 position 设置为指定值。 如果尝试将通道 position 设置为一个负值会导致 java.lang.IllegalArgumentException 异常，
        // 不过可以 把 position 设置到超出文件尾，这样做会把 position 设置为指定值而不改变文件大小。假如在将 position 设置为超出当前文件大小时实现了一个 read( )方法，那么会返回一个文件尾(end-of-file) 条件;
        // 倘若此时实现的是一个 write( )方法则会引起文件增长以容纳写入的字节



// read()和put
//        类似于缓冲区的 get( ) 和 put( )方法，当字节被 read( )或 write( )方法传输时，文件 position 会 自动更新。如果 position 值达到了文件大小的值(文件大小的值可以通过 size( )方法返回)，read( ) 方法会返回一个文件尾条件值(-1)。可是，不同于缓冲区的是，如果实现 write( )方法时 position 前进到超过文件大小的值，该文件会扩展以容纳新写入的字节。


//        当需要减少一个文件的 size 时，truncate( )方法会砍掉您所指定的新 size 值之外的所有数据。 如果当前 size 大于新 size，超出新 size 的所有字节都会被悄悄地丢弃。如果提供的新 size 值大于或 等于当前的文件 size 值，该文件不会被修改。这两种情况下，truncate( )都会产生副作用:文件的 position 会被设置为所提供的新 size 值。


//        force( )。该方法告诉通道强制将全部待定的修改都应用到磁盘的 文件上。所有的现代文件系统都会缓存数据和延迟磁盘文件更新以提高性能。调用 force( )方法要 求文件的所有待定修改立即同步到磁盘。

//        如果文件位于一个本地文件系统，那么一旦 force( )方法返回，即可保证从通道被创建(或上 次调用 force( ))时起的对文件所做的全部修改已经被写入到磁盘。对于关键操作如事务 (transaction)处理来说，这一点是非常重要的，可以保证数据完整性和可靠的恢复。然而，如果 文件位于一个远程的文件系统，如 NFS 上，那么不能保证待定修改一定能同步到永久存储器


//        force( )方法的布尔型参数表示在方法返回值前文件的元数据(metadata)是否也要被同步更新 到磁盘。元数据指文件所有者、访问权限、最后一次修改时间等信息。大多数情形下，该信息对数 据恢复而言是不重要的。给 force( )方法传递 false 值表示在方法返回前只需要同步文件数据的更 改。大多数情形下，同步元数据要求操作系统进行至少一次额外的底层 I/O 操作。一些大数量事务 处理程序可能通过在每次调用 force( )方法时不要求元数据更新来获取较高的性能提升，同时也不 会牺牲数据完整性。

    }

}
