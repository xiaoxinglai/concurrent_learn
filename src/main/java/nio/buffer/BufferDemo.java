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
        String s="lai";
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
        buffer.put(s,0,1);
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


    }
}
