package proxy.dao;

public class IndexDaoLogImpl implements Dao {
    private  Dao dao;
    public IndexDaoLogImpl(Dao dao) {
        this.dao=dao;
    }

    @Override
    public void query() {
        System.out.println("====log====");
        dao.query();
    }
}
