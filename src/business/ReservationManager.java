package business;

import dao.ReservationDao;
import entity.Reservation;

import java.util.ArrayList;

public class ReservationManager {

    private final ReservationDao reservationDao;


    public ReservationManager() {
        this.reservationDao = new ReservationDao();
    }


    public ArrayList<Reservation> getRoomId(int id){
        return reservationDao.getRoomId(id);
    }


    public ArrayList<Reservation> findByAll(){
        return reservationDao.findByAll();
    }


    public boolean delete(int id){
        return reservationDao.delete(id);
    }


    public boolean save(Reservation reservation){
        return reservationDao.save(reservation);
    }


    public boolean update(Reservation reservation){
        return reservationDao.update(reservation);
    }


    public Reservation getById(int id){
        return reservationDao.getById(id);
    }


    public ArrayList<Reservation> getHotelId(int id){
        return reservationDao.getHotelId(id);
    }
}
