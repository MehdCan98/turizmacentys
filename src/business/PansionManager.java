package business;

import dao.PansionDao;
import entity.Pansion;

import java.util.ArrayList;

public class PansionManager {
    private final PansionDao pansionDao;


    public PansionManager() {
        this.pansionDao = new PansionDao();
    }


    public ArrayList<Object[]> getForTable(int size){
        ArrayList<Object[]> rowList = new ArrayList<>();
        for(Pansion pansion : findByAll()){
            Object[] rows = new Object[size];
            int i = 0;

            rowList.add(rows);
        }
        return rowList;
    }


    public ArrayList<Pansion> findByAll(){
        return pansionDao.findByAll();
    }


    public boolean delete(int id){
        return pansionDao.delete(id);
    }


    public boolean save(Pansion pansion){
        return pansionDao.save(pansion);
    }


    public boolean update(Pansion pansion){
        return pansionDao.update(pansion);
    }


    public Pansion getById(int id){
        return pansionDao.getById(id);
    }


    public Pansion[] findByHotelId(int hotelId) {
        return pansionDao.findByHotelId(hotelId);
    }
}
