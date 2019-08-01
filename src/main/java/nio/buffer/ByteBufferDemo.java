package nio.buffer;

import java.nio.Buffer;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.CharBuffer;

/**
 * @ClassName ByteBufferDemo
 * @Author laixiaoxing
 * @Date 2019/7/31 下午12:50
 * @Description 字节缓冲区
 * @Version 1.0
 */
public class ByteBufferDemo {
    public static void main(String[] args) {
        ByteBuffer byteBuffer = ByteBuffer.allocate(100);
        //        多字节数值被存储在内存中的方式一般被称为 endian-ness(字节顺序)。如果数字数 值的最高字节——big end(大端)，位于低位地址，
        // 那么系统就是大端字节顺序。如果最低字节最先保存在内存中，那么小端字节顺序

        //        字是两个字节，也就是16个零或一组成的，例如0000 0000 0000 0000，
        // 这十六个位置中前八个就是高位，后八个是低位。内存是按字节存放的，而后面八个零会放在物理地址x的位置上，而前面八个会放在x+1的位置上，也就是高位
        //BIG_ENDIAN  存储的时候高位在前就是大端
        //内存中放的时候从左到右是低位，高位
        //也就是123 中的1放在了低位   就叫大端顺序
        System.out.println(byteBuffer.order());

        //        字节缓冲区跟其他缓冲区类型最明显的不同在于，
        //        它们可以成为通道所执行的 I/O 的源 头和/或目标。通道只接收 ByteBuffer 作为 参数。


        //直接缓冲区被用于与通道和固有 I/O 例程交 互
        //        直接字节缓冲区通常是 I/O 操作最好的选择。在设计方面，它们支持 JVM 可用的最高效 I/O 机制。非直接字节缓冲区可以被传递给通道，但是这样可能导致性能损耗。通常非直接缓 冲不可能成为一个本地 I/O 操作的目标。如果您向一个通道中传递一个非直接 ByteBuffer 对象用于写入，通道可能会在每次调用中隐含地进行下面的操作:
        //        1.创建一个临时的直接 ByteBuffer 对象。 2.将非直接缓冲区的内容复制到临时缓冲中。 3.使用临时缓冲区执行低层次 I/O 操作。 4.临时缓冲区对象离开作用域，并最终成为被回收的无用数据。


        //        直接缓冲区时 I/O 的最佳选择，但可能比创建非直接缓冲区要花费更高的成本。
        // 直接缓冲区使用的内存是通过调用本地操作系统方面的代码分配的，绕过了标准 JVM 堆栈。建立和 销毁直接缓冲区会明显比具有堆栈的缓冲区更加破费，这取决于主操作系统以及 JVM 实现。 直接缓冲区的内存区域不受无用存储单元收集支配，因为它们位于标准 JVM 堆栈之外。
        //        直接 ByteBuffer 是通过调用具有所需容量的 ByteBuffer.allocateDirect()函数 产生的，
        // 注意用一个 wrap()函数所创建的被 包装的缓冲区总是非直接的。

        // 所有的缓冲区都提供了一个叫做 isDirect()的 boolean 函数，来测试特定缓冲区是否 为直接缓冲区。虽然 ByteBuffer 是唯一可以被直接分配的类型

        //这是堆外内存  堆外内存的大小默认和jvm内存大小一致
        ByteBuffer allocateDirect = ByteBuffer.allocateDirect(100);

        // 视图缓冲区通过已存在的缓冲区对象实例的工厂方法来创建。这种视图对象维护它自己的 属性，容量，位置，上界和标记
        // ，但是和原来的缓冲区共享数据元素。比如之前的slice()  duplicate( )


        //关键
        //        ByteBuffer 类允许创建视 图来将 byte 型缓冲区字节数据映射为其它的原始数据类型。例如，asLongBuffer()函数 创建一个将八个字节型数据当成一个 long 型数据来存取的视图缓冲区。


//        下面列出的每一个工厂方法都在原有的 ByteBuffer 对象上创建一个视图缓冲区。调用 其中的任何一个方法都会创建对应的缓冲区类型，这个缓冲区是基础缓冲区的一个切分，由基 础缓冲区的位置和上界决定。新的缓冲区的容量是字节缓冲区中存在的元素数量除以视图类型 中组成一个数据类型的字节数
//        public abstract class ByteBuffer
        //                extends Buffer implements Comparable
        //        {
        //            // This is a partial API listing
        //            public abstract CharBuffer asCharBuffer( ); public abstract ShortBuffer asShortBuffer( ); public abstract IntBuffer asIntBuffer( ); public abstract LongBuffer asLongBuffer( ); public abstract FloatBuffer asFloatBuffer( ); public abstract DoubleBuffer asDoubleBuffer( );
        //        }



        //创建一个 ByteBuffer 的字符视图

        ByteBuffer buffer =
                ByteBuffer.allocate (7).order (ByteOrder.BIG_ENDIAN);
        CharBuffer charBuffer = byteBuffer.asCharBuffer( );

        buffer.put (0, (byte)0);
        buffer.put (1, (byte)'H');
        buffer.put (2, (byte)0);
        buffer.put (3, (byte)'i');
        buffer.put (4, (byte)0);
        buffer.put (5, (byte)'!');
        buffer.put (6, (byte)0);
        println (byteBuffer);
        println (charBuffer);
        charBuffer.put('a');
        //输出为
//        pos=0, limit=100, capacity=100
//        pos=0, limit=50, capacity=50
//        一旦视图缓冲区，可以用 duplicate()，slice()和
//        asReadOnlyBuffer()函数创建进一步的子视图，





      //  数据元素视图
        //ByteBuffer 类提供了一个不太重要的机制来以多字节数据类型的形式存取 byte 数据 组。ByteBuffer 类为每一种原始数据类型提供了存取的和转化的方法:
        buffer.getChar();
        buffer.putChar('a');

        //比如说，如果 getInt()函数被调用，从当前的位置开始的四个字节会被包装 成一个 int 类型的变量然后作为函数的返回值返回

    }


    private static void println (Buffer buffer)
    {
        System.out.println ("pos=" + buffer.position( )
                + ", limit=" + buffer.limit( )
                + ", capacity=" + buffer.capacity());
    }
}
