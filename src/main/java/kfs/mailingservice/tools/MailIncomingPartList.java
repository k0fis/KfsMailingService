package kfs.mailingservice.tools;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.VerticalLayout;
import java.lang.reflect.Field;
import java.util.List;
import kfs.kfsvaalib.comps.KfsButton;
import kfs.kfsvaalib.kfsTable.KfsTable;
import kfs.kfsvaalib.kfsTable.Pos;
import kfs.kfsvaalib.utils.KfsI18n;
import kfs.kfsvaalib.utils.KfsRefresh;
import kfs.mailingservice.domain.MailIncomingPart;

/**
 *
 * @author pavedrim
 */
public class MailIncomingPartList extends VerticalLayout implements
        KfsTable.TableGeneratorFactory, Table.ColumnGenerator, Button.ClickListener {

    private final String showLabel;
    private final MailIncomingPartDlg editDlg;
    private final BeanItemContainer<MailIncomingPart> cont;

    public MailIncomingPartList(KfsI18n i18n, MailIncomingPartDlg edit) {
        this(i18n, edit, "MailIncomingPartList", "MailIncomingPartList");
    }

    public MailIncomingPartList(KfsI18n i18n, MailIncomingPartDlg edit,
            String tableName, String i18nPrefix) {
        this.editDlg = edit;
        this.showLabel = i18n.getMsg(i18nPrefix + ".show");

        this.cont = new BeanItemContainer<>(MailIncomingPart.class);
        KfsTable<MailIncomingPart> table;
        addComponent(
                (table = new KfsTable<>(tableName, i18n, MailIncomingPart.class,
                        i18n.getMsg(i18nPrefix + ".title"), cont, null, this)));
        table.setWidth("100%");
        table.setHeight("500px");
        setSpacing(true);
        setMargin(true);
    }

    public void show(List<MailIncomingPart> mal, KfsRefresh kfsRefresh) {
        cont.removeAllItems();
        cont.addAll(mal);
    }

    @Override
    public Table.ColumnGenerator getColumnGenerator(Class type, Field field, Pos pos) {
        if ((type == MailIncomingPart.class) && (field.getName().equals("id"))) {
            return this;
        }
        return null;
    }

    @Override
    public Object generateCell(Table source, Object itemId, Object columnId) {
        MailIncomingPart ku = (MailIncomingPart) itemId;
        KfsButton<MailIncomingPart> edit = new KfsButton<>(showLabel, ku, this);
        edit.addStyleName("small");
        HorizontalLayout hl = new HorizontalLayout(edit);
        return hl;
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (editDlg != null) {
            editDlg.show(((KfsButton<MailIncomingPart>) event.getButton()).getButtonData(), null);
        }
    }
}
