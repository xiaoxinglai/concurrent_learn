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







    }
}
