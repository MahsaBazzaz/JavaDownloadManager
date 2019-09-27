
import javax.swing.*;


/**
 * @author Mahsa Bazzaz 9631405
 * @version 0.3
 */
public class DownloadList extends List{
    public DownloadList(){
        super();
    }

    @Override
    public void setModel(ListModel<Download> model) {
        super.setModel(model);
    }

    @Override
    public ListModel<Download> getModel() {
        return super.getModel();
    }
}
