package nio.FileUntil;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @ClassName MappedFile
 * @Author laixiaoxing
 * @Date 2019/8/1 下午3:00
 * @Description TODO
 * @Version 1.0
 */
public class MappedFile {
    // 文件名
    private String fileName;

    // 文件所在目录路径
    private String fileDirPath;

    // 文件对象
    private File file;

    private MappedByteBuffer mappedByteBuffer;
    private FileChannel fileChannel;
    private boolean boundSuccess = false;

    // 文件最大只能为50MB
    // private final static long MAX_FILE_SIZE = 1024 * 1024 * 50;

    private final static long MAX_FILE_SIZE = 50;

    // 最大的脏数据量512KB,系统必须触发一次强制刷
    // private long MAX_FLUSH_DATA_SIZE = 1024 * 512;

    private long MAX_FLUSH_DATA_SIZE = 1;

    // 最大的刷间隔,系统必须触发一次强制刷
    private long MAX_FLUSH_TIME_GAP = 1000;

    // 文件写入位置
    private long writePosition = 0;

    // 最后一次刷数据的时候
    private long lastFlushTime;

    // 上一次刷的文件位置
    private long lastFlushFilePosition = 0;

    public MappedFile(String fileName, String fileDirPath) {
        super();
        this.fileName = fileName;
        this.fileDirPath = fileDirPath;
        this.file = new File(fileDirPath + "/" + fileName);
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    /**
     * 内存映照文件绑定
     *
     * @return
     */
    public synchronized boolean boundChannelToByteBuffer() {
        try {
            RandomAccessFile raf = new RandomAccessFile(file, "rw");
            this.fileChannel = raf.getChannel();
        } catch (Exception e) {
            e.printStackTrace();
            this.boundSuccess = false;
            return false;
        }

        try {
            this.mappedByteBuffer = this.fileChannel.map(FileChannel.MapMode.READ_WRITE, 0, MAX_FILE_SIZE);
        } catch (IOException e) {
            e.printStackTrace();
            this.boundSuccess = false;
            return false;
        }

        this.boundSuccess = true;
        return true;
    }

    /**
     * 写数据：先将之前的文件删除然后重新
     *
     * @param data
     * @return
     */
    public synchronized boolean writeData(byte[] data) {

        return false;
    }

    /**
     * 在文件末尾追加数据
     *
     * @param data
     * @return
     * @throws Exception
     */
    public synchronized boolean appendData(ByteBuffer data) throws Exception {
        if (!boundSuccess) {
            boundChannelToByteBuffer();
        }

        writePosition = writePosition + data.limit();
        if (writePosition >= MAX_FILE_SIZE) {   // 如果写入data会超出文件大小限制，不写入
            flush();
            writePosition = writePosition - data.limit();
            System.out.println("File=" + file.toURI().toString() + " is written full.");
            System.out.println("already write data length:" + writePosition + ", max file size=" + MAX_FILE_SIZE);
            return false;
        }
        this.mappedByteBuffer.put(data);
        // 检查是否需要把内存缓冲刷到磁盘
        if ((writePosition - lastFlushFilePosition > this.MAX_FLUSH_DATA_SIZE) || (
                System.currentTimeMillis() - lastFlushTime > this.MAX_FLUSH_TIME_GAP
                        && writePosition > lastFlushFilePosition)) {
            flush();   // 刷到磁盘
        }

        return true;
    }

    public synchronized void flush() {
        this.mappedByteBuffer.force();
        this.lastFlushTime = System.currentTimeMillis();
        this.lastFlushFilePosition = writePosition;
    }

    public long getLastFlushTime() {
        return lastFlushTime;
    }

    public String getFileName() {
        return fileName;
    }

    public String getFileDirPath() {
        return fileDirPath;
    }

    public boolean isBundSuccess() {
        return boundSuccess;
    }

    public File getFile() {
        return file;
    }

    public static long getMaxFileSize() {
        return MAX_FILE_SIZE;
    }

    public long getWritePosition() {
        return writePosition;
    }

    public long getLastFlushFilePosition() {
        return lastFlushFilePosition;
    }

    public long getMAX_FLUSH_DATA_SIZE() {
        return MAX_FLUSH_DATA_SIZE;
    }

    public long getMAX_FLUSH_TIME_GAP() {
        return MAX_FLUSH_TIME_GAP;
    }

    public MappedByteBuffer getMappedByteBuffer() {
        return mappedByteBuffer;
    }

    public static void main(String[] args) throws Exception {
        MappedFile mappedFile = new MappedFile("text",
                "/Users/laixiaoxing/Documents/concurrent_learn/src/main/resources/data");
        mappedFile.boundChannelToByteBuffer();
        String s = "赖晓星";
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect(100);
        int l2=s.getBytes("UTF-8").length;
        byteBuffer.put(s.getBytes("UTF-8"));
        byteBuffer.flip();
        mappedFile.appendData(byteBuffer);
        mappedFile.flush();

        byteBuffer.clear();
        String ss = "好好学习 天天向上";
        int length=ss.getBytes("UTF-8").length;
        byteBuffer.put(ss.getBytes("UTF-8"));
        byteBuffer.flip();
        mappedFile.appendData(byteBuffer);
        mappedFile.flush();


        MappedByteBuffer mappedByteBuffer = mappedFile.getMappedByteBuffer();
        mappedByteBuffer.flip();
        byte[] c=new  byte[l2];
        System.out.println(mappedByteBuffer.get(c));
        String world=new String(c);
        System.out.println("字符串："+world);


        byte[] cc=new  byte[length];
        System.out.println(mappedByteBuffer.get(cc));
        String world2=new String(cc);
        System.out.println("字符串："+world2);


    }


}
