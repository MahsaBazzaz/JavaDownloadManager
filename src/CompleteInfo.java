import javax.swing.*;
import java.io.Serializable;
import java.util.ArrayList;

public class CompleteInfo  implements Serializable {
    private static final long serialVersionUID = 7564684232115141984L;
    public ArrayList<Download> downloadsInfo = new ArrayList<>();
    public CompleteInfo(DefaultListModel<Download> downloads){
        for(int i=0;i<downloads.getSize();i++){
            downloadsInfo.add(i,downloads.get(i));
        }
    }
}

