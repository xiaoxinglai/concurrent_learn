import demo5.demo5;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.*;


class demo5Test {

    @BeforeEach
    @DisplayName("每条用例开始时执行")
    void start(){
        System.out.println("每条用例开始时执行");
    }

    @AfterEach
    @DisplayName("每条用例结束时执行")
    void end(){
        System.out.println("每条用结束时执行");
    }

    @Test
    void myFirstTest() {
        assertEquals(2, 1 + 1);
    }

    @Test
    @DisplayName("描述测试用例╯°□°）╯")
    void testWithDisplayName() {

    }

    @Test
    @Disabled("这条用例暂时跑不过，忽略!")
    void myFailTest(){
        assertEquals(1,2);
    }

    @Test
    @DisplayName("运行一组断言")
    public void assertAllCase() {
        assertAll("groupAssert",
                () -> assertEquals(2, 1 + 1),
                () -> assertTrue(1 > 0)
        );
    }

    @Test
    @DisplayName("依赖注入1")
    public void testInfo(final TestInfo testInfo) {
        System.out.println(testInfo.getDisplayName());
    }

    @Test
    @DisplayName("依赖注入2")
    public void testReporter(final TestReporter testReporter) {
        testReporter.publishEntry("name", "Alex");
    }


    @ParameterizedTest
    @CsvSource({"0,1,2","5,5,5"})
    @DisplayName("测试")
    void testdemotest(int a,int b,int c){
       demo5 demo5Test=new demo5();
        demo5Test.testdemo(a,b,c);
    }
}