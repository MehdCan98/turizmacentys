package business;

import core.Helper;
import dao.UserDao;
import entity.User;

import java.util.ArrayList;

public class UserManager {

    private final UserDao userDao;


    public UserManager() {
        this.userDao = new UserDao();
    }


    public ArrayList<Object[]> getForTable(int size){
        ArrayList<Object[]> rowList = new ArrayList<>();
        for(User user : findByAll()){
            Object[] rows = new Object[size];
            int i = 0;

            rows[i++] = user.getUser_id();
            rows[i++] = user.getUser_name();
            rows[i++] = user.getUser_pass();
            rows[i++] = user.getPerm();
            rowList.add(rows);
        }
        return rowList;
    }


    public User login(String username,String password){
        return userDao.login(username,password);
    }


    public ArrayList<User> findByAll(){
        return userDao.findByAll();
    }


    public boolean delete(int id){
        return userDao.delete(id);
    }


    public boolean save(User user){
        try {
            return userDao.save(user);
        }catch (Exception e){
            return false;
        }
    }


    public boolean update(User user){
        try {
            return userDao.update(user);
        }catch (Exception e){
            return false;
        }
    }


    public User getById(int id){
        return userDao.getById(id);
    }
}
