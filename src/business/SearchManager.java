package business;

import dao.SearchDao;
import entity.Search;

import java.util.ArrayList;

public class SearchManager {

    private final SearchDao searchDao;

    public SearchManager() {
        this.searchDao = new SearchDao();
    }


    public ArrayList<Search> filterTable(String search_hotel_city, String search_hotel_name, String selectedValueForInDateYear,
                                         String selectedValueForInDateMonth, String selectedValueForInDateDay,
                                         String selectedValueForOutDateYear,String selectedValueForOutDateMonth,
                                         String selectedValueForOutDateDay)  {
        ArrayList<Search> filteredResults = new ArrayList<>();


        StringBuilder queryBuilder = new StringBuilder("SELECT * FROM public.hotel_room_season_table WHERE ");

        String checkInDate = selectedValueForInDateYear + "-" + selectedValueForInDateMonth + "-" + selectedValueForInDateDay;
        String checkOutDate =selectedValueForOutDateYear + "-" + selectedValueForOutDateMonth + "-" + selectedValueForOutDateDay;

        if (!search_hotel_city.equals("NULL")) {
            queryBuilder.append("hotel_city = '").append(search_hotel_city).append("' AND ");
        }

        if (!search_hotel_name.equals("NULL")) {
            queryBuilder.append("hotel_name = '").append(search_hotel_name).append("' AND ");
        }

        if (!selectedValueForInDateYear.equals("0") && !selectedValueForInDateMonth.equals("0") && !selectedValueForInDateDay.equals("0")) {
            queryBuilder.append("season_start_date <= '").append(checkInDate).append("' AND ");
        }

        if (!selectedValueForOutDateYear.equals("0") && !selectedValueForOutDateMonth.equals("0") && !selectedValueForOutDateDay.equals("0")) {
            queryBuilder.append("season_end_date >= '").append(checkOutDate).append("' AND ");
        }
        if(search_hotel_city.equals("NULL") &&
                search_hotel_name.equals("NULL") &&
                (selectedValueForInDateYear.equals("0") &&
                        (selectedValueForInDateMonth.equals("0") || selectedValueForInDateMonth.equals("00")) &&
                        (selectedValueForInDateDay.equals("0") || selectedValueForInDateDay.equals("00"))&&
                        selectedValueForOutDateYear.equals("0") &&
                        ( selectedValueForOutDateMonth.equals("0") || selectedValueForOutDateMonth.equals("00") )&&
                        (selectedValueForOutDateDay.equals("0") || selectedValueForOutDateDay.equals("00")))){
            return searchDao.selectByQuery("SELECT * FROM public.hotel_room_season_table");
        }


        String query = queryBuilder.toString();
        if (query.endsWith(" AND ")) {
            query = query.substring(0, query.length() - 5);
        }

        return searchDao.selectByQuery(query);
    }


    public ArrayList<Object[]> getForTable(int size, ArrayList<Search> list){
        ArrayList<Object[]> rowList = new ArrayList<>();
        for(Search obj : list){
            Object[] rows = new Object[size];
            int i = 0;

            rowList.add(rows);
        }
        return rowList;
    }


    public ArrayList<Search> findByAll(){
        return searchDao.findByAll();
    }


    public Search getById(int id){
        return searchDao.getById(id);
    }

}
