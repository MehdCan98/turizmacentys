package business;

import dao.SeasonDao;
import entity.Room;
import entity.Season;

import java.util.ArrayList;

public class SeasonManager {

    private final SeasonDao seasonDao;
    private final RoomManager roomManager;


    public SeasonManager() {
        this.seasonDao = new SeasonDao();
        this.roomManager = new RoomManager();
    }


    public ArrayList<Object[]> getForTable(int size, ArrayList<Season> list){
        ArrayList<Object[]> rowList = new ArrayList<>();
        for(Season obj : list){
            Object[] rows = new Object[size];
            int i = 0;

            rows[i++] = obj.getSeason_id();
            rows[i++] = obj.getSeason_hotel_id();
            rows[i++] = obj.getSeason_start_date();
            rows[i++] = obj.getSeason_end_date();
            rows[i++] = obj.getSeasonName();
            rowList.add(rows);
        }
        return rowList;
    }


    public ArrayList<Season> findByAll(){
        return seasonDao.findByAll();
    }


    public boolean delete(int id){

        for (Room room : roomManager.getSeasonId(id)){
            roomManager.delete(room.getRoom_id());
        }
        return seasonDao.delete(id);
    }


    public boolean save(Season season){
        return seasonDao.save(season);
    }


    public boolean update(Season season){
        return seasonDao.update(season);
    }


    public Season getById(int id){
        return seasonDao.getById(id);
    }


    public int getSeasonIdBySeasonName(String seasonName){
        return seasonDao.getSeasonIdBySeasonName(seasonName);
    }


    public ArrayList<Season> getHotelId(int id){
        return seasonDao.getHotelId(id);
    }
}
