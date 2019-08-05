package nio.buffer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @ClassName MappedByteBufferDemo
 * @Author laixiaoxing
 * @Date 2019/8/2 上午12:06
 * @Description 内存映射文件的使用
 * @Version 1.0
 */
public class MappedByteBufferDemo {
    // 新的 FileChannel 类提供了一个名为 map( )的方法，该方法可以在一个打开的文件和一个特殊 类型的 ByteBuffer 之间建立一个虚拟内存映射

    //   在 FileChannel 上调用 map( )方法会创建一个由磁盘文件支持的虚拟内存 映射(virtual memory mapping)并在那块虚拟内存空间外部封装一个 MappedByteBuffer 对象
    //
    //    由 map( )方法返回的 MappedByteBuffer 对象的行为在多数方面类似一个基于内存的缓冲区，只 不过该对象的数据元素存储在磁盘上的一个文件中。调用 get()方法会从磁盘文件中获取数据，此 数据反映该文件的当前内容，即使在映射建立之后文件已经被一个外部进程做了修改。通过文件映射看到的数据同您用常规方法读取文件看到的内容是完全一样的。
    // 相似地，对映射的缓冲区实现一 个 put( )会更新磁盘上的那个文件(假设对该文件您有写的权限)，并且您做的修改对于该文件的 其他阅读者也是可见的。
    //    通过内存映射机制来访问一个文件会比使用常规方法读写高效得多，甚至比使用通道的效率都高。因为不需要做明确的系统调用，那会很消耗时间。更重要的是，操作系统的虚拟内存可以自动 缓存内存页(memory page)。这些页是用系统内存来缓存的，所以不会消耗 Java 虚拟机内存堆 (memory heap)。

    //    一旦一个内存页已经生效(从磁盘上缓存进来)，它就能以完全的硬件速度再次被访问而不需 要再次调用系统命令来获取数据。那些包含索引以及其他需频繁引用或更新的内容的巨大而结构化 文件能因内存映射机制受益非常多。如果同时结合文件锁定来保护关键区域和控制事务原子性，那 您将能了解到内存映射缓冲区如何可以被很好地利用。


    //
    //    public abstract class FileChannel
    //            extends AbstractChannel
    //            implements ByteChannel, GatheringByteChannel, ScatteringByteChannel
    //    {
    //        // This is a partial API listing
    //        public abstract MappedByteBuffer map (MapMode mode, long position,long size)
    //        public static class MapMode
    //        {
    //            public static final MapMode READ_ONLY
    //            public static final MapMode READ_WRITE
    //            public static final MapMode PRIVATE
    //        } }

    //只有一种 map( )方法来创建一个文件映射。它的参数有 mode，position 和 size。
    //    要映射 100 到 299(包 含 299)位置的字节，可以使用下面的代码:
    //    buffer = fileChannel.map (FileChannel.MapMode.READ_ONLY, 100, 200);


    //    如果要映射整个文件则使用:
    //    buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());

    //注意！
    //    与文件锁的范围机制不一样，映射文件的范围不应超过文件的实际大小。如果您请求一个超出 文件大小的映射，文件会被增大以匹配映射的大小。
    // 假如您给 size 参数传递的值是
    //    Integer.MAX_VALUE，文件大小的值会膨胀到超过 2.1GB。
    //即使您请求的是一个只读映射， map( )方法也会尝试这样做并且大多数情况下都会抛出一个 IOException 异常，因为底层的文件不 能被修改

    //    MapMode.READ_ONLY 和 MapMode.READ_WRITE 意义是很明显的，它们表示您希望获取的映射 只读还是允许修改映射的文件。请求的映射模式将受被调用 map( )方法的 FileChannel 对象的访问 权限所限制。如果通道是以只读的权限打开的而您却请求 MapMode.READ_WRITE 模式，那么 map( )方法会抛出一个 NonWritableChannelException 异常;如果您在一个没有读权限的通道上请求 MapMode.READ_ONLY 映射模式，那么将产生 NonReadableChannelException 异常。不过在以 read/write 权限打开的通道上请求一个 MapMode.READ_ONLY 映射却是允许的。MappedByteBuffer 对象的可变性可以通过对它调用 isReadOnly( )方法来检查。


    //    MapMode.PRIVATE 表示您想要一个写时拷贝(copy-on-write)的映射。这意味着 您通过 put( )方法所做的任何修改都会导致产生一个私有的数据拷贝并且该拷贝中的数据只有 MappedByteBuffer 实例可以看到。该过程不会对底层文件做任何修改，而且一旦缓冲区被施以垃圾 收集动作(garbage collected)，那些修改都会丢失。尽管写时拷贝的映射可以防止底层文件被修 改，您也必须以 read/write 权限来打开文件以建立 MapMode.PRIVATE 映射。只有这样，返回的 MappedByteBuffer 对象才能允许使用 put( )方法。


    //注意
    // 同锁不一样的是，映射缓冲区没有绑定到创建它 们的通道上。关闭相关联的 FileChannel 不会破坏映射，只有丢弃缓冲区对象本身才会破坏该映 射。NIO 设计师们之所以做这样的决定是因为当关闭通道时破坏映射会引起安全问题，而解决该安 全问题又会导致性能问题。
    //    MemoryMappedBuffer 直接反映它所关联的磁盘文件。如果映射有效时文件被在结构上修改， 就会产生奇怪的行为(当然具体的行为是取决于操作系统和文件系统的)。MemoryMappedBuffer 有固定的大小，不过它所映射的文件却是弹性的。具体来说，如果映射有效时文件大小变化了，那 么缓冲区的部分或全部内容都可能无法访问，并将返回未定义的数据或者抛出未检查的异常。关于 被内存映射的文件如何受其他线程或外部进程控制这一点，请务必小心对待。
    //
    //    所有的 MappedByteBuffer 对象都是直接的，这意味着它们占用的内存空间位于 Java 虚拟机内 存堆之外(并且可能不会算作 Java 虚拟机的内存占用，不过这取决于操作系统的虚拟内存模 型)。


    //    因为 MappedByteBuffers 也是 ByteBuffers，所以能够被传递 SocketChannel 之类通道的 read( )或 write( )以有效传输数据给被映射的文件或从被映射的文件读取数据。如能再结合 scatter/gather，那 么从内存缓冲区和被映射文件内容中组织数据就变得很容易了


    //  MappedByteBuffer 还定义了几个它独有的方法:
    //  public final MappedByteBuffer load( )
    //    public final boolean isLoaded( )
    //    public final MappedByteBuffer force( )

    //
    //    当我们为一个文件建立虚拟内存映射之后，文件数据通常不会因此被从磁盘读取到内存(这取 决于操作系统)。该过程类似打开一个文件:文件先被定位，然后一个文件句柄会被创建，当您准 备好之后就可以通过这个句柄来访问文件数据。对于映射缓冲区，虚拟内存系统将根据您的需要来
    //
    //    把文件中相应区块的数据读进来。这个页验证或防错过程需要一定的时间，因为将文件数据读取到 内存需要一次或多次的磁盘访问。某些场景下，您可能想先把所有的页都读进内存以实现最小的缓 冲区访问延迟。如果文件的所有页都是常驻内存的，那么它的访问速度就和访问一个基于内存的缓 冲区一样了。

    // load( )方法会加载整个文件以使它常驻内存

    //    在一个映射缓冲区上调用 load( )方法会是一个代价高的操作，因为它会导致大量的页调入 (page-in)，具体数量取决于文件中被映射区域的实际大小。然而，load( )方法返回并不能保证文 件就会完全常驻内存，这是由于请求页面调入(demand paging)是动态的。具体结果会因某些因素 而有所差异，这些因素包括:操作系统、文件系统，可用 Java 虚拟机内存，最大 Java 虚拟机内 存，垃圾收集器实现过程等等。请小心使用 load( )方法，它可能会导致您不希望出现的结果。该方 法的主要作用是为提前加载文件埋单，以便后续的访问速度可以尽可能的快。

    //    对于那些要求近乎实时访问(near-realtime access)的程序，解决方案就是预加载。但是请记 住，不能保证全部页都会常驻内存，不管怎样，之后可能还会有页调入发生。内存页什么时候以及 怎样消失受多个因素影响，这些因素中的许多都是不受 Java 虚拟机控制的


    //  我们可以通过调用 isLoaded( )方法来判断一个被映射的文件是否完全常驻内存了。如果该方法 返回 true 值，那么很大概率是映射缓冲区的访问延迟很少或者根本没有延迟


    //    force( )同 FileChannel 类中的同名方法相似, 该方法会强制将映射缓冲区上的更改应用到永久磁盘存储器上。当用 MappedByteBuffer 对象来更新 一个文件，您应该总是使用 MappedByteBuffer.force( )而非 FileChannel.force( )，因为通道对象可能 不清楚通过映射缓冲区做出的文件的全部更改。MappedByteBuffer 没有不更新文件元数据的选项— —元数据总是会同时被更新的。


    //以 MapMode.READ_ONLY 或 MAP_MODE.PRIVATE 模式建立的，那么调用 force( ) 方法将不起任何作用，因为永远不会有更改需要应用到磁盘上


    public static void main(String[] args) throws IOException {
        RandomAccessFile randomAccessFile = new RandomAccessFile(
                "/Users/laixiaoxing/Documents/concurrent_learn/src/main/resources/data/Mapbuffer", "rw");

        MappedByteBuffer mappedByteBuffer = randomAccessFile.getChannel()
                .map(FileChannel.MapMode.READ_WRITE, 0, 100 );

        byte[] bytes="laixiaoxing".getBytes("US-ASCII");
        int length=bytes.length;
        mappedByteBuffer.put(bytes);
      //  mappedByteBuffer.force();


        byte[] a=new byte[length];
         mappedByteBuffer.flip();
         mappedByteBuffer.get(a);
        for (byte b : a) {
            System.out.println((char)b);
        }

        //如果对mappedByteBuffer进行过读，那么追加写时候的要重置limit
        //如果没有，则不需要
        mappedByteBuffer.limit(mappedByteBuffer.capacity());
        mappedByteBuffer.put(a);

        mappedByteBuffer.force();





    }


}

