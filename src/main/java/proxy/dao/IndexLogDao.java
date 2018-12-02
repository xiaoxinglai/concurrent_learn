package proxy.dao;

public class IndexLogDao extends IndexDao
{
    @Override
    public void query() {
        System.out.println("====log======");
        super.query();
    }
}
