package proxy.dao;

public class IndexTimeLogDao extends IndexLogDao {
    @Override
    public void query() {
        System.out.println("====Time===");
        super.query();
    }
}
