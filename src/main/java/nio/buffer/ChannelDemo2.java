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



//        锁(lock)可以是共享的(shared)或独占的(exclusive)。本节中描 述的文件锁定特性在很大程度上依赖本地的操作系统实现。并非所有的操作系统和文件系统都支持 共享文件锁。对于那些不支持的，对一个共享锁的请求会被自动提升为对独占锁的请求。这可以保 证准确性却可能严重影响性能



//        并非所有平台都以同一个方式来实现基本的文件锁定。在不同的操作系统上，甚至在同 一个操作系统的不同文件系统上，文件锁定的语义都会有所差异。一些操作系统仅提供劝告锁定 (advisory locking)，一些仅提供独占锁(exclusive locks)，而有些操作系统可能两种锁都提供。 您应该总是按照劝告锁的假定来管理文件锁，因为这是最安全的


     //   有关 FileChannel 实现的文件锁定模型的一个重要注意项是:锁的对象是文件而不是通道或线 程，这意味着文件锁不适用于判优同一台 Java 虚拟机上的多个线程发起的访问。

//        如果一个线程在某个文件上获得了一个独占锁，然后第二个线程利用一个单独打开的通道来请 求该文件的独占锁，那么第二个线程的请求会被批准。但如果这两个线程运行在不同的 Java 虚拟 机上，那么第二个线程会阻塞，因为锁最终是由操作系统或文件系统来判优的并且几乎总是在进程 级而非线程级上判优

//        锁与文件关联，而不是与通道关联。我们使用锁来判优外部进程，而不是判 优同一个 Java 虚拟机上的线程。

    //    文件锁旨在在进程级别上判优文件访问，比如在主要的程序组件之间或者在集成其他供应商的 组件时。如果您需要控制多个 Java 线程的并发访问

//        带参数形式的 lock( )方法。锁是在文件内部区域上获得的。调用带参数的 Lock( ) 方法会指定文件内部锁定区域的开始 position 以及锁定区域的 size。第三个参数 shared 表 示您想获取的锁是共享的(参数值为 true)还是独占的(参数值为 false)。要获得一个共享 锁，您必须先以只读权限打开文件，而请求独占锁时则需要写权限。另外，您提供的 position 和 size 参数的值不能是负数。

//        锁定区域的范围不一定要限制在文件的 size 值以内，锁可以扩展从而超出文件尾。因此，我们 可以提前把待写入数据的区域锁定，我们也可以锁定一个不包含任何文件内容的区域，比如文件最 后一个字节以外的区域。如果之后文件增长到达那块区域，那么您的文件锁就可以保护该区域的文 件内容了。相反地，如果您锁定了文件的某一块区域，然后文件增长超出了那块区域，那么新增加 的文件内容将不会受到您的文件锁的保护。

//        不带参数的简单形式的 lock( )方法是一种在整个文件上请求独占锁的便捷方法，锁定区域等于 它能达到的最大范围。该方法等价于:
//        fileChannel.lock (0L, Long.MAX_VALUE, false);

//        如果您正请求的锁定范围是有效的，那么 lock( )方法会阻塞，它必须等待前面的锁被释放。假 如您的线程在此情形下被暂停，该线程的行为受中断语义(类似我们在 3.1.3 节中所讨论的)控 制。如果通道被另外一个线程关闭，该暂停线程将恢复并产生一个 AsynchronousCloseException 异 常。假如该暂停线程被直接中断(通过调用它的 interrupt( )方法)，它将醒来并产生一个 FileLockInterruptionException 异常。如果在调用 lock( )方法时线程的 interrupt status 已经被设置，也 会产生 FileLockInterruptionException 异常。


//        在上面的 API 列表中有两个名为 tryLock( )的方法，它们是 lock( )方法的非阻塞变体。这两个 tryLock( )和 lock( )方法起相同的作用，不过如果请求的锁不能立即获取到则会返回一个 null。


//        FileLock 类封装一个锁定的文件区域。FileLock 对象由 FileChannel 创建并且总是关联到那个特 定的通道实例。您可以通过调用 channel( )方法来查询一个 lock 对象以判断它是由哪个通道创建 的。
//        一个 FileLock 对象创建之后即有效，直到它的 release( )方法被调用或它所关联的通道被关闭或 Java 虚拟机关闭时才会失效。我们可以通过调用 isValid( )布尔方法来测试一个锁的有效性。一个锁 的有效性可能会随着时间而改变，不过它的其他属性——位置(position)、范围大小(size)和独 占性(exclusivity)——在创建时即被确定，不会随着时间而改变。
//        您可以通过调用 isShared( )方法来测试一个锁以判断它是共享的还是独占的。如果底层的操作 系统或文件系统不支持共享锁，那么该方法将总是返回 false 值，即使您申请锁时传递的参数值 是 true。假如您的程序依赖共享锁定行为，请测试返回的锁以确保您得到了您申请的锁类型。 FileLock 对象是线程安全的，多个线程可以并发访问一个锁对象。
//        最后，您可以通过调用 overlaps( )方法来查询一个 FileLock 对象是否与一个指定的文件区域重 叠。这将使您可以迅速判断您拥有的锁是否与一个感兴趣的区域(region of interest)有交叉。不过 即使返回值是 false 也不能保证您就一定能在期望的区域上获得一个锁，因为 Java 虚拟机上的其 他地方或者外部进程可能已经在该期望区域上有一个或多个锁了。您最好使用 tryLock( )方法确认 一下。
//
//        尽管一个 FileLock 对象是与某个特定的 FileChannel 实例关联的，它所代表的锁却是与一个底 层文件关联的，而不是与通道关联。因此，如果您在使用完一个锁后而不释放它的话，可能会导致 冲突或者死锁。请小心管理文件锁以避免出现此问题。一旦您成功地获取了一个文件锁，如果随后 在通道上出现错误的话，请务必释放这个锁。


    }


}
