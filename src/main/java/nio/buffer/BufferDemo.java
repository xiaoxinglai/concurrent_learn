package nio.buffer;

import java.nio.CharBuffer;

/**
 * @ClassName BufferDemo
 * @Author laixiaoxing
 * @Date 2019/7/30 下午4:48
 * @Description TODO
 * @Version 1.0
 */
public class BufferDemo {


    //    容量(Capacity)
    //    缓冲区能够容纳的数据元素的最大数量。这一容量在缓冲区创建时被设定，并且永远不能
    //    被改变。


    //    上界(Limit)
    //    缓冲区的第一个不能被读或写的元素。或者说，缓冲区中现存元素的计数。


    //    位置(Position)
    //    下一个要被读或写的元素的索引。位置会自动由相应的 get( )和 put( )函数更新。


    //    标记(Mark)
    //    一个备忘位置。调用mark( )来设定mark=postion。调用reset( )设定position= mark。标记在设定前是未定义的(undefined)。


    public static void main(String[] args) {
        CharBuffer buffer = CharBuffer.allocate(100);
        System.out.println("添加ABC");
        buffer.put('A');
        buffer.put('B');
        buffer.put('C');
        //重置positon和limit 将模式从写改为读
        System.out.println("写改读");
        buffer.flip();

        //判断是否为空，如果不是就输出

        System.out.println("hasRemaining 读");
        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }


        //读取指定的位置
        System.out.println("读指定位置");
        System.out.println(buffer.get(1));
        System.out.println(buffer.get(0));
        System.out.println(buffer.get(2));
        //如果get没有参数，则读取当前postion的位置，因为之前已经读过了 此时postion位置在最新的位置+1 所以越界了
        //        System.out.println(buffer.get());

        //rewind的
        System.out.println("重置之后再次取");
        buffer.rewind();
        System.out.println(buffer.get());

        System.out.println("mark标记，然后取");
        buffer.mark();
        System.out.println(buffer.get());
        System.out.println("重置标记，然后取");
        buffer.reset();
        System.out.println(buffer.get());

        // compact( )  读过之后的数据进行压缩
        System.out.println("compact 压缩读过的位置");
        //如果不压缩 输出应该是A  压缩之后是C
        buffer.compact();
        buffer.rewind();
        System.out.println(buffer.get());


        System.out.println("批量获取");
        buffer.put('D');
        buffer.put('C');
        buffer.put('E');
        buffer.put('F');
        buffer.flip();
        char[] bigArray = new char[5];
        //批量传输的大小总是固定的。省略长度意味着整个 数组会被填满 不然会报错

        //获取buff的长度   调用get( )之前必须查询缓冲区中的元素数量  不然填不满会异常的
        int length = buffer.remaining();

        buffer.get(bigArray, 0, length);

        for (char c : bigArray) {
            System.out.print(c);
        }


        //放入字符串 要先用flip()将postion位置重置回写
        buffer.flip();
        String s = "lai";
        buffer.put(s);
        //原来是CDCEF 先变成了laiEF 而且输出是从EF开始输出的
        System.out.println("放入字符串");
        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }

        //读之前要flip刷新位置
        System.out.println("放入字符串之后刷新位置 切换模式");
        buffer.flip();
        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }


        System.out.println("指定放入字符串的某个片段");
        buffer.flip();
        buffer.put(s, 0, 1);
        while (buffer.hasRemaining()) {
            System.out.println(buffer.get());
        }


        //  flip()   用于切换读写模式， 比如说 我写入了1，2，3 此时position的位置就是3，然后我要读，就要flip 一下，此时的position就变成了0，limit变成了3
        CharBuffer b2 = CharBuffer.allocate(100);
        b2.put('1');
        b2.put('2');
        //此时limit 100 position  2   capacity为100
        b2.flip();
        //此时limit 2  position  0   capacity为100

        //此时如果用put 会覆盖之前的 1 2
        b2.put('3');
        b2.put('4');
        b2.flip();
        while (b2.hasRemaining()) {
            System.out.println(b2.get());
        }

        //此时limit 为2 position为2 ，如果此时直接put 会因为已经到了limit最大值而异常  b2.put('5'); java.nio.BufferOverflowException
        //如果改成flip 则又回到了一开始的读模式，从position为0的位置开始写  会覆盖之前的


        //如果要追加
        //要先修改limit 然后用put或者apend
        //flip是要求先写再读，插入在写和读之间，如果flip flip 就等于中间有一次没写就读了， 当然是就会是position为0
        System.out.println("追加5");
        b2.limit(b2.capacity());
        b2.append('5');
        b2.flip();
        while (b2.hasRemaining()) {
            System.out.println(b2.get());
        }

        // position和limit都是可以重新设定的
        //        b2.position(1);
        //        b2.limit(1);
        //        b2.capacity();


        // 新的缓冲区是由分配或包装操作创建的。分配操作创建一个缓冲区对象并分配一个私有的 空间来储存容量大小的数据元素
        //里面是一个数组
        CharBuffer charBuffer = CharBuffer.allocate(100);


        //自己的数组用做缓冲区的备份存储器
        char[] myArray = new char[100];
        CharBuffer charbuffer = CharBuffer.wrap(myArray);
        charbuffer.put('A');
        myArray[2] = 'B';
        //这段代码构造了一个新的缓冲区对象，但数据元素会存在于数组中。这意味着通过调用 put()函数造成的对缓冲区的改动会直接影响这个数组，
        // 而且对这个数组的任何改动也会对这 个缓冲区对象可见

        //        Boolean 型函数 hasArray()告诉您这个缓冲区是否有 一个可存取的备份数组。如果这个函数的返回 true，array()函数会返回这个缓冲区对象所 使用的数组存储空间的引用。
        //        如果 hasArray()函数返回 false，不要调用 array()函数或者 arrayOffset()函 数。如果您这样做了您会得到一个 UnsupportedOperationException


        //将已有的字符串变成缓冲区
        //  CharBuffer charBuffer = CharBuffer.wrap ("Hello World");



        //复制
        //Duplicate()函数创建了一个与原始缓冲区相似的新缓冲区。两个缓冲区共享数据元
        //素，拥有同样的容量，但每个缓冲区拥有各自的位置，上界和标记属性。对一个缓冲区内的数 据元素所做的改变会反映在另外一个缓冲区上。这一副本缓冲区具有与原始缓冲区同样的数据 视图。如果原始的缓冲区为只读，或者为直接缓冲区，新的缓冲区将继承这些属性
        CharBuffer b3=b2.duplicate();
        b3.position(2);
        b3.get();



        //        复制一个缓冲区会创建一个新的 Buffer 对象，但并不 复制数据。原始缓冲区和副本都会操作同样的数据元 素。


        //  slice()
        //        slice()创建一个从原始缓冲区的当前位置开始的新缓冲 区，并且其容量是原始缓冲区的剩余元素数量(limit-position)。这个新缓冲区与原始 缓冲区共享一段数据元素子序列。分割出来的缓冲区也会继承只读和直接属性。
        b2.limit(7);
        CharBuffer b4=b2.slice();
        b4.get();


    }
}
