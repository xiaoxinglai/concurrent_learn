
import com.alibaba.excel.ExcelReader;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.read.context.AnalysisContext;
import com.alibaba.excel.read.event.AnalysisEventListener;
import com.alibaba.excel.support.ExcelTypeEnum;
import extend.FileUtil;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @ClassName TestListImportExcel
 * @Author laixiaoxing
 * @Date 2019/3/29 下午9:54
 * @Description TODO
 * @Version 1.0
 */
public class TestMutilExcelV101 {


    public static void main(String[] args) throws IOException {

        // InputStream inputStream = extend.FileUtil.getResourcesFileInputStream("201903月融租台.xlsx");
        InputStream in = FileUtil.getResourcesFileInputStream("溢出例子.xlsx");
       // InputStream inputStream = extend.FileUtil.getResourcesFileInputStream("2007.xlsx");
        AnalysisEventListener excelListener = MutitleEventListener();
        ExcelReader reader=new ExcelReader(in,ExcelTypeEnum.XLSX,null,excelListener);
        List<Sheet> sheets = reader.getSheets();
        for (Sheet sheet:sheets ) {
            reader.read(sheet);
        }
        reader.read();

    }


    public static AnalysisEventListener MutitleEventListener() {


        return new AnalysisEventListener<List<String>>() {



            @Override
            public void invoke(List<String> list, AnalysisContext context) {
                System.out.println(list.toString());
                if (context.getCurrentRowNum() == 0) {
                    System.out.println("第一行，开始定位表头");
                    System.out.println("当前sheet名称" + context.getCurrentSheet().getSheetName());
                    //初始化表头
                    for (int i = 0; i < list.size(); i++) {
                        final Integer index = i;
                        Optional.ofNullable(tableHeader.get(list.get(index)))
                                .map(x -> tableHeader.put(list.get(index), index));
                    }
                    System.out.println("初始化表头结束");
                    for (Map.Entry<String, Integer> stringIntegerEntry : tableHeader.entrySet()) {
                        System.out.println("表头：" + stringIntegerEntry.getKey() + "位置：" + stringIntegerEntry.getValue());
                    }
                    return;
                }


                System.out.println("第" + context.getCurrentRowNum() + "行");
                //遍历已有的表头取位置
                for (Map.Entry<String, Integer> stringIntegerEntry : tableHeader.entrySet()) {
                    if (stringIntegerEntry.getValue() != -1) {
                        String Header = stringIntegerEntry.getKey();
                        String cell = list.get(stringIntegerEntry.getValue());
                        System.out.print(Header+" 的值为："+cell+"  ");
                        if (Header.equals("日期")){
                            //todo 解析日期
                        }
                    }
                }

                System.out.println();
            }

            @Override
            public void doAfterAllAnalysed(AnalysisContext context) {
                System.out.println("sheet页面");
                System.out.println("AfterAllAnalysed"+context.getCurrentSheet().getSheetName());

            }

            HashMap<String, Integer> tableHeader = new HashMap();

            {
                tableHeader.put("表头1", -1);
                tableHeader.put("表头2", -1);
                tableHeader.put("表头7", -1);
                tableHeader.put("表头8", -1);

            }


        };


    }
}
