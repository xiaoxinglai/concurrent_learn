package proxy.dao;

public class indexDaoLogTimeImpl implements Dao {
    private  Dao dao;

    public indexDaoLogTimeImpl(Dao dao) {
        this.dao=dao;
    }

    @Override
    public void query() {
        System.out.println("===time===");
        dao.query();
    }
}
