import javax.swing.*;

public class TheList extends List {
    public TheList(){
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
