package proxy.dao;

public class IndexTimeDao extends IndexDao
{
    @Override
    public void query() {
        System.out.println("====time======");
        super.query();
    }
}
