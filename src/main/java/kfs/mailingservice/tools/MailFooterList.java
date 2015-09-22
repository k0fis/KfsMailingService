package kfs.mailingservice.tools;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Panel;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import java.lang.reflect.Field;
import java.util.List;
import kfs.kfsvaalib.comps.KfsButton;
import kfs.kfsvaalib.comps.KfsButtonConfirm;
import kfs.kfsvaalib.kfsTable.KfsTable;
import kfs.kfsvaalib.kfsTable.Pos;
import kfs.kfsvaalib.listener.KfsButtonConfirmCallback;
import kfs.kfsvaalib.utils.KfsI18n;
import kfs.kfsvaalib.utils.KfsRefresh;
import kfs.mailingservice.domain.MailFooter;
import kfs.mailingservice.service.MailingService;

/**
 *
 * @author pavedrim
 */
public class MailFooterList extends Panel implements KfsRefresh,
        KfsTable.TableGeneratorFactory, Table.ColumnGenerator, Button.ClickListener {

    private final String editLabel;
    private final String delLabel;
    private final String delTitleLabel;
    private final String delQuestionLabel;
    private final String delYesLabel;
    private final String delNoLabel;
    private final UI ui;
    private final KfsTable<MailFooter> table;
    private final MailingService maSvc;    
    private final MailFooterDlg editDlg;
    private final BeanItemContainer<MailFooter> cont;
    private KfsRefresh kfsRefresh;

    public MailFooterList(UI ui, KfsI18n i18n, MailingService maSvc) {
        this(ui, i18n, maSvc, new MailFooterDlg(ui, i18n, maSvc), "MailFooterList", "MailFooterList");
    }

    public MailFooterList(UI ui, KfsI18n i18n, MailingService maSvc, MailFooterDlg edit,
            String tableName, String i18nPrefix) {
        this.ui = ui;
        this.editDlg = edit;
        this.maSvc = maSvc;
        this.editLabel = i18n.getMsg(i18nPrefix + ".edit");
        this.delLabel = i18n.getMsg(i18nPrefix + ".del");
        this.delTitleLabel = i18n.getMsg(i18nPrefix + ".delTitle");
        this.delQuestionLabel = i18n.getMsg(i18nPrefix + ".delQuestion");
        this.delYesLabel = i18n.getMsg(i18nPrefix + ".delYes");
        this.delNoLabel = i18n.getMsg(i18nPrefix + ".delNo");

        this.cont = new BeanItemContainer<>(MailFooter.class);
        setContent(
                (table = new KfsTable<>(tableName, i18n, MailFooter.class,
                        i18n.getMsg(i18nPrefix + ".title"), cont, null, this)));
        //table.setWidth("100%");
        //table.setHeight("500px");
    }

    public void show(List<MailFooter> mal, KfsRefresh kfsRefresh) {
        this.kfsRefresh = kfsRefresh;
        cont.removeAllItems();
        cont.addAll(mal);
    }

    @Override
    public Table.ColumnGenerator getColumnGenerator(Class type, Field field, Pos pos) {
        if ((type == MailFooter.class) && (field.getName().equals("id"))) {
            return this;
        }
        return null;
    }

    @Override
    public Object generateCell(Table source, Object itemId, Object columnId) {
        MailFooter ku = (MailFooter) itemId;
        KfsButton<MailFooter> edit = new KfsButton<>(editLabel, ku, this);
        edit.addStyleName("small");
        Button del = new KfsButtonConfirm(delLabel, ui, delTitleLabel, delQuestionLabel,
                delYesLabel, delNoLabel, new KfsButtonConfirmCallback(this, "delClick"),
                null, null, null, ku);
        del.addStyleName("small");
        HorizontalLayout hl = new HorizontalLayout(edit, del);
        return hl;
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (editDlg != null) {
            editDlg.show(((KfsButton<MailFooter>) event.getButton()).getButtonData(), kfsRefresh);
        }
    }
    
    public void delClick(MailFooter mf) {
        maSvc.mailFooterDelete(mf);
        if (kfsRefresh != null) {
            kfsRefresh.kfsRefresh();
        }
    }

    @Override
    public void kfsRefresh() {
        table.refreshRowCache();
    }
}
