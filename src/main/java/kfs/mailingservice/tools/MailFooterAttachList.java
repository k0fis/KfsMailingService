package kfs.mailingservice.tools;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.lang.reflect.Field;
import java.util.List;
import kfs.kfsvaalib.comps.KfsButton;
import kfs.kfsvaalib.comps.KfsButtonConfirm;
import kfs.kfsvaalib.kfsTable.KfsTable;
import kfs.kfsvaalib.kfsTable.Pos;
import kfs.kfsvaalib.listener.KfsButtonClickListener;
import kfs.kfsvaalib.listener.KfsButtonConfirmCallback;
import kfs.kfsvaalib.utils.KfsI18n;
import kfs.kfsvaalib.utils.KfsRefresh;
import kfs.mailingservice.domain.MailAttach;
import kfs.mailingservice.domain.MailFooterAttach;

/**
 *
 * @author pavedrim
 */
public class MailFooterAttachList extends VerticalLayout implements KfsRefresh,
        KfsTable.TableGeneratorFactory, Table.ColumnGenerator {

    private final KfsTable<MailFooterAttach> table;
    private final MailFooterAttachDlg editDlg;
    private final BeanItemContainer<MailFooterAttach> cont;
    private final String editLabel;
    private final String delLabel;
    private final String delTitleLabel;
    private final String delQuestionLabel;
    private final String delYesLabel;
    private final String delNoLabel;
    private final UI ui;

    public MailFooterAttachList(UI ui, KfsI18n i18n) {
        this(ui, i18n, new MailFooterAttachDlg(ui, i18n), "MailAttachList", "MailAttachList");
    }

    public MailFooterAttachList(UI ui, KfsI18n i18n, MailFooterAttachDlg edit,
            String tableName, String i18nPrefix) {
        this.ui = ui;
        this.editDlg = edit;
        this.editLabel = i18n.getMsg(i18nPrefix + ".edit");
        this.delLabel = i18n.getMsg(i18nPrefix + ".del");
        this.delTitleLabel = i18n.getMsg(i18nPrefix + ".delTitle");
        this.delQuestionLabel = i18n.getMsg(i18nPrefix + ".delQuestion");
        this.delYesLabel = i18n.getMsg(i18nPrefix + ".delYes");
        this.delNoLabel = i18n.getMsg(i18nPrefix + ".delNo");
        this.cont = new BeanItemContainer<>(MailFooterAttach.class);
        addComponents(
                new Button(i18n.getMsg(i18nPrefix + ".add"), new KfsButtonClickListener(this, "newClick")),
                (table = new KfsTable<>(tableName, i18n, MailFooterAttach.class,
                        i18n.getMsg(i18nPrefix + ".title"), cont, null, this))
        );
        table.setWidth("100%");
        table.setHeight("250px");
        setSpacing(true);
        setMargin(true);
    }

    public void show(List<MailFooterAttach> lst) {
        cont.removeAllItems();
        cont.addAll(lst);
    }

    @Override
    public Table.ColumnGenerator getColumnGenerator(Class type, Field field, Pos pos) {
        if ((type == MailFooterAttach.class) && ((field.getName().equals("id"))
                || (field.getName().equals("attach")))) {
            return this;
        }
        return null;
    }

    @Override
    public Object generateCell(Table source, Object itemId, Object columnId) {
        MailFooterAttach ku = (MailFooterAttach) itemId;
        if ("name".equals(columnId)) {
            return new Label(ku.getAttach().getAttachName() + " " + ku.getAttach().getContentId());
        } else if ("id0".equals(columnId)) {
            Button edit = new KfsButton<>(editLabel, ku, new KfsButtonClickListener(this, "editClick"));
            edit.addStyleName("small");
            Button del = new KfsButtonConfirm(delLabel, ui, delTitleLabel, delQuestionLabel,
                    delYesLabel, delNoLabel, new KfsButtonConfirmCallback(this, "delClick"),
                    null, null, null, ku);
            del.addStyleName("small");
            HorizontalLayout hl = new HorizontalLayout(edit, del);
            return hl;
        }
        return null;
    }

    public void newClick(Button.ClickEvent event) {
        if (editDlg != null) {
            MailFooterAttach ma = new MailFooterAttach();
            ma.setAttach(new MailAttach());
            cont.addItem(ma);
            editDlg.show(ma, this);
        }
    }

    public List<MailFooterAttach> getMailFooterAttach() {
        return cont.getItemIds();
    }

    public void editClick(Button.ClickEvent event) {
        if (editDlg != null) {
            editDlg.show(((KfsButton<MailFooterAttach>) event.getButton()).getButtonData(), this);
        }
    }

    public void delClick(MailFooterAttach ma) {
        if (ma != null) {
            cont.removeItem(ma);
        }
    }

    @Override
    public void kfsRefresh() {
        table.refreshRowCache();
    }

}
