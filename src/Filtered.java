import java.io.Serializable;
import java.util.ArrayList;

public class Filtered implements Serializable{
    private static final long serialVersionUID = -1928166548857926140L;
    public ArrayList<String> filteredURL;
    public Filtered(ArrayList f){

        filteredURL = f;
    }
    public int getSize(){
        return filteredURL.size();
    }
    public String getByIndex(int index){
        return filteredURL.get(index);
    }
}
