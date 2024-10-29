import com.ytrue.infra.news.FileMetadata;
import com.ytrue.infra.news.UploadFileContext;
import com.ytrue.infra.news.detect.TikaContentTypeDetect;
import com.ytrue.infra.news.factory.AliyunOssFileStorageClientFactory;
import com.ytrue.infra.news.platform.AliyunOssFileStorage;
import com.ytrue.infra.news.properties.AliyunOssStorageProperties;
import com.ytrue.infra.news.wrapper.FileWrapper;
import com.ytrue.infra.news.wrapper.adapter.FileWrapperAdapter;
import com.ytrue.infra.news.wrapper.adapter.LocalFileWrapperAdapter;
import org.apache.tika.mime.MimeType;
import org.apache.tika.mime.MimeTypeException;
import org.apache.tika.mime.MimeTypes;
import org.junit.Test;

import java.io.*;
import java.util.Arrays;

public class FileStorageTest {


    @Test
    public void testAliOssUpload() throws IOException {


        // 创建客户端工厂
        AliyunOssFileStorageClientFactory clientFactory = new AliyunOssFileStorageClientFactory(properties);

        // 创建阿里云 OSS 文件存储实例
        AliyunOssFileStorage fileStorage = new AliyunOssFileStorage(clientFactory);

        // 准备上传文件上下文

        UploadFileContext context = new UploadFileContext();
        context.setSaveFileName("test123.png");
        context.setSavePath("test");

        File file1 = new File("C:\\Users\\Administrator\\Desktop\\logo\\test.png");
        FileWrapperAdapter fileWrapperAdapter = new LocalFileWrapperAdapter(new TikaContentTypeDetect());
        FileWrapper fileWrapper1 = fileWrapperAdapter.getFileWrapper(file1, null, null, null);

        context.setFileWrapper(fileWrapper1);

        try {
            // 上传文件
            FileMetadata fileMetadata = fileStorage.save(context);
            System.out.println("文件上传成功: " + fileMetadata);

            // 检查文件是否存在
            boolean exists = fileStorage.exists(fileMetadata);
            System.out.println("文件存在: " + exists);

//            // 删除文件
//            boolean deleted = fileStorage.delete(fileMetadata);
//            System.out.println("文件删除成功: " + deleted);
//
//
//            // 检查文件是否存在
//            boolean exists1 = fileStorage.exists(fileMetadata);
//            System.out.println("文件存在: " + exists1);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Test
    public void test02() throws Exception {


//        File file = new File("C:\\Users\\Administrator\\Desktop\\版本更新\\04.杭州银行分润账户\\文档资料\\开放银行商户接入签名验签说明手册.docx");
//
//        FileWrapperAdapter fileWrapperAdapter = new LocalFileWrapperAdapter(new TikaContentTypeDetect());
//        FileWrapper fileWrapper1 = fileWrapperAdapter.getFileWrapper(file, null, null, null);
//
//
//        LocalFileStorage localFileStorage = new LocalFileStorage();
//
//
//        UploadFileContext uploadFileContext = new UploadFileContext();
//        uploadFileContext.setFileWrapper(fileWrapper1);
//
//        FileMetadata fileMetadata = localFileStorage.save(uploadFileContext);
//
//
//        System.out.println(localFileStorage.exists(fileMetadata));
//        System.out.println(localFileStorage.delete(fileMetadata));
//        System.out.println(localFileStorage.exists(fileMetadata));


    }


    @Test
    public void test01() throws IOException, MimeTypeException {

        // File file1 = new File("C:\\Users\\Administrator\\Desktop\\other\\test.png");
        //    File file2 = new File("C:\\Users\\Administrator\\Desktop\\版本更新\\04.杭州银行分润账户\\文档资料\\开放银行商户接入签名验签说明手册.docx");


        // localFile
//        FileWrapperAdapter fileWrapperAdapter = new LocalFileWrapperAdapter(new TikaContentTypeDetect());
        //  FileWrapper fileWrapper1 = fileWrapperAdapter.getFileWrapper(file1, null, null, null);
//        FileWrapper fileWrapper2 = fileWrapperAdapter.getFileWrapper(file2, null, null, null);

//        // byteFile
//        FileWrapperAdapter byteFileWrapperAdapter = new ByteFileWrapperAdapter(new TikaContentTypeDetect());
//        FileWrapper fileWrapper3 = byteFileWrapperAdapter.getFileWrapper(getBytesByFile(file1), null, null, null);
//        FileWrapper fileWrapper4 = byteFileWrapperAdapter.getFileWrapper(getBytesByFile(file2), null, null, null);
//
//        // inputStreamFile
//        FileWrapperAdapter inputStreamFileWrapperAdapter = new InputStreamFileWrapperAdapter(new TikaContentTypeDetect());
//        FileWrapper fileWrapper5 = inputStreamFileWrapperAdapter.getFileWrapper(new FileInputStream(file1), null, null, null);
//        FileWrapper fileWrapper6 = inputStreamFileWrapperAdapter.getFileWrapper(new FileInputStream(file2), null, null, null);
//
//        // netUrlFilePath
//        String imageUrlPath = "https://img-blog.csdnimg.cn/7027a07aab31462b8187ff56ba57af87.png";
//        FileWrapperAdapter uriFileWrapperAdapter = new UriFileWrapperAdapter(new TikaContentTypeDetect());
//        FileWrapper fileWrapper7 = uriFileWrapperAdapter.getFileWrapper(imageUrlPath, null, null, null);
//        FileWrapper fileWrapper8 = uriFileWrapperAdapter.getFileWrapper(imageUrlPath, null, null, null);


        // getType(fileWrapper1.getContentType());
//        System.out.println(fileWrapper2.getContentType());
//        getType(fileWrapper2.getContentType());
    }


    // 获取文件类型 后缀
    private void getType(String mimeType) throws MimeTypeException {
        // 获取MIME类型对象
        MimeTypes allTypes = MimeTypes.getDefaultMimeTypes();
        MimeType type = allTypes.forName(mimeType);
        // 获取文件后缀
        String[] extensions = type.getExtensions().toArray(new String[0]);
        System.out.println(Arrays.toString(extensions));
    }


    public byte[] getBytesByFile(File file) {
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
