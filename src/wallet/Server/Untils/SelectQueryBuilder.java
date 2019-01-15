package wallet.Server.Untils;

import javafx.util.Pair;

import java.util.ArrayList;

public class SelectQueryBuilder {

    private String tableName = null;
    private String select = "*";
    private String where = null;
    private ArrayList<Pair<String, String>> whereItems = new ArrayList<>();
    private ArrayList<Pair<String, String>> selectItems = new ArrayList<>();
    private ArrayList<Pair<String, String>> orderBy = new ArrayList<>();

    public void setTable(String tableName){
        this.tableName = tableName;
    }

    public void selectAll(){
        this.select = "*";
        selectItems.clear();
    }

    public void selectRaw(String select){
        this.select = select;
        selectItems.clear();
    }

    public void addSlelect(ArrayList<String> selectItems){
        for (String item : selectItems){
            this.selectItems.add(new Pair<>(item, ""));
        }
    }

    public void addSlelectWithAlias(ArrayList<Pair<String, String>> selectItems){
        for (Pair<String, String> item : selectItems){
            this.selectItems.add(new Pair<>(item.getKey(), item.getValue()));
        }
    }

    public void addWhereRaw(String where){
        this.where = where;
    }

    public void addWhereConditions(ArrayList<Pair<String, String>> whereItems){
        this.whereItems = whereItems;
    }

    public void addWhereCondition(Pair<String, String> whereItem){
        this.whereItems.add(whereItem);
    }

    public void addOrderBy(String column, String type){
        orderBy.add(new Pair<>(column, type));
    }

    public String buildQuery(){
        StringBuilder query = new StringBuilder();

        query.append("select\n");
        if(selectItems.isEmpty()){
            query.append(select);
            query.append("\n");
        }
        else {
            int i = 1;
            for (Pair<String, String> item : selectItems){
                query.append(item.getKey());
                query.append(" ");
                query.append(item.getValue());

                if(selectItems.size() > 1 && i != selectItems.size()){
                    query.append(",\n");
                }
                else {
                    query.append("\n");
                }
                i++;
            }
        }

        query.append("from\n");
        query.append(tableName);
        query.append("\n");

        if(where != null){
            query.append(where);
            query.append("\n");
        }
        else {
            for(Pair<String, String> item : whereItems){
                query.append(item.getKey());
                query.append(" ");
                query.append(item.getValue());
                query.append("\n");
            }
        }

        if(!orderBy.isEmpty()){
            query.append("order by\n");
            int i = 1;
            for (Pair<String, String> item : orderBy){
                query.append(item.getKey());
                query.append(" ");
                query.append(item.getValue());

                if(orderBy.size() > 1 && i != orderBy.size()){
                    query.append(",\n");
                }
                else {
                    query.append("\n");
                }
                i++;
            }
        }
        query.append(";");

        return query.toString();
    }

}
