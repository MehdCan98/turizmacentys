package business;

import dao.HotelDao;
import entity.Hotel;
import entity.Reservation;
import entity.Room;
import entity.Season;

import java.util.ArrayList;

public class HotelManager {

    private final HotelDao hotelDao;
    private RoomManager roomManager;
    private SeasonManager seasonManager;
    private ReservationManager reservationManager;


    public HotelManager() {
        this.hotelDao = new HotelDao();
        reservationManager = new ReservationManager();
        roomManager = new RoomManager();
        seasonManager = new SeasonManager();
    }


    public ArrayList<Object[]> getForTable(int size){
        ArrayList<Object[]> rowList = new ArrayList<>();
        for(Hotel hotel : findByAll()){
            Object[] rows = new Object[size];
            int i = 0;
            rows[i++] = hotel.getHotel_id();
            rows[i++] = hotel.getHotel_name();
            rows[i++] = hotel.getHotel_city();
            rows[i++] = hotel.getHotel_region();
            rows[i++] = hotel.getHotel_fulladres();
            rows[i++] = hotel.getHotel_mail();
            rows[i++] = hotel.getHotel_phone();
            rows[i++] = hotel.getHotel_star();
            rows[i++] = hotel.isHotel_free_parking();
            rows[i++] = hotel.isHotel_free_wifi();
            rows[i++] = hotel.isHotel_swimming_pool();
            rows[i++] = hotel.isHotel_fitness_center();
            rows[i++] = hotel.isHotel_concierge();
            rows[i++] = hotel.isHotel_spa();
            rows[i++] = hotel.isHotel_room_services();
            rows[i++] = hotel.getHotel_pansion_type();
            rowList.add(rows);
        }
        return rowList;
    }



    public ArrayList<Hotel> findByAll(){
        return hotelDao.findByAll();
    }


    public boolean delete(int id){

        for(Room room : roomManager.getHotelId(id)){
            roomManager.delete(room.getRoom_id());
        }
        for(Season season : seasonManager.getHotelId(id)){
            seasonManager.delete(season.getSeason_id());
        }
        for(Reservation reservation : reservationManager.getHotelId(id)){
            reservationManager.delete(reservation.getReservation_id());
        }
        return hotelDao.delete(id);
    }


    public boolean save(Hotel hotel){
        return hotelDao.save(hotel);
    }


    public boolean update(Hotel hotel){
        return hotelDao.update(hotel);
    }


    public Hotel getById(int id){
        return hotelDao.getById(id);
    }


    public int getHotelIdByName(String hotelName){
        return hotelDao.getHotelIdByName(hotelName);
    }
}
