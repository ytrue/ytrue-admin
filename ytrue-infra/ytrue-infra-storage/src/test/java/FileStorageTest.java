import com.ytrue.infra.news.detect.TikaContentTypeDetect;
import com.ytrue.infra.news.wrapper.FileWrapper;
import com.ytrue.infra.news.wrapper.adapter.*;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class FileStorageTest {


    @Test
    public void  test01() throws IOException {

        File file1 = new File("C:\\Users\\Administrator\\Desktop\\other\\test.png");
        File file2 = new File("C:\\Users\\Administrator\\Desktop\\other\\工具类收集.txt");


        // localFile
        FileWrapperAdapter fileWrapperAdapter = new LocalFileWrapperAdapter(new TikaContentTypeDetect());
        FileWrapper fileWrapper1 = fileWrapperAdapter.getFileWrapper(file1, null, null, null);
        FileWrapper fileWrapper2 = fileWrapperAdapter.getFileWrapper(file2, null, null, null);

        // byteFile
        FileWrapperAdapter byteFileWrapperAdapter = new ByteFileWrapperAdapter(new TikaContentTypeDetect());
        FileWrapper fileWrapper3 = byteFileWrapperAdapter.getFileWrapper(getBytesByFile(file1), null, null, null);
        FileWrapper fileWrapper4 = byteFileWrapperAdapter.getFileWrapper(getBytesByFile(file2), null, null, null);

        // inputStreamFile
        FileWrapperAdapter inputStreamFileWrapperAdapter = new InputStreamFileWrapperAdapter(new TikaContentTypeDetect());
        FileWrapper fileWrapper5 = inputStreamFileWrapperAdapter.getFileWrapper(new FileInputStream(file1), null, null, null);
        FileWrapper fileWrapper6 = inputStreamFileWrapperAdapter.getFileWrapper(new FileInputStream(file2), null, null, null);

        // netUrlFilePath
        String imageUrlPath = "https://img-blog.csdnimg.cn/7027a07aab31462b8187ff56ba57af87.png";
        FileWrapperAdapter uriFileWrapperAdapter = new UriFileWrapperAdapter(new TikaContentTypeDetect());
        FileWrapper fileWrapper7 = uriFileWrapperAdapter.getFileWrapper(imageUrlPath, null, null, null);
        FileWrapper fileWrapper8 = uriFileWrapperAdapter.getFileWrapper(imageUrlPath, null, null, null);

    }


    public   byte[] getBytesByFile(File file) {
        try {
            //获取输入流
            FileInputStream fis = new FileInputStream(file);

            //新的 byte 数组输出流，缓冲区容量1024byte
            ByteArrayOutputStream bos = new ByteArrayOutputStream(1024);
            //缓存
            byte[] b = new byte[1024];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }
            fis.close();
            //改变为byte[]
            byte[] data = bos.toByteArray();
            //
            bos.close();
            return data;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}