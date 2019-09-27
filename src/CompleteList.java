import javax.swing.*;
import java.awt.*;

public class CompleteList extends List {
    public CompleteList(){
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
