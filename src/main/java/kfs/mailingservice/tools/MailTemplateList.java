package kfs.mailingservice.tools;

import com.vaadin.data.util.BeanItemContainer;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Table;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import java.lang.reflect.Field;
import java.util.List;
import kfs.kfsvaalib.comps.KfsButton;
import kfs.kfsvaalib.comps.KfsButtonConfirm;
import kfs.kfsvaalib.kfsTable.KfsTable;
import kfs.kfsvaalib.kfsTable.Pos;
import kfs.kfsvaalib.listener.KfsButtonConfirmCallback;
import kfs.kfsvaalib.utils.KfsI18n;
import kfs.kfsvaalib.utils.KfsRefresh;
import kfs.mailingservice.domain.MailTemplate;
import kfs.mailingservice.service.MailingService;
import org.apache.log4j.Logger;

/**
 *
 * @author pavedrim
 */
public class MailTemplateList extends VerticalLayout implements KfsRefresh,
        KfsTable.TableGeneratorFactory, Table.ColumnGenerator, Button.ClickListener {

    private final String delLabel;
    private final String delTitleLabel;
    private final String delQuestionLabel;
    private final String delYesLabel;
    private final String delNoLabel;
    private final UI ui;
    private final MailingService maSvc;
    private final KfsTable<MailTemplate> table;
    private final MailTemplateDlg editDlg;
    private final BeanItemContainer<MailTemplate> cont;

    public MailTemplateList(UI ui, KfsI18n i18n, MailingService maSvc) {
        this(ui, i18n, maSvc, new MailTemplateDlg(ui, i18n, maSvc), "MailTemplateList", "MailTemplateList");
    }

    public MailTemplateList(UI ui, KfsI18n i18n, MailingService maSvc, MailTemplateDlg edit,
            String tableName, String i18nPrefix) {
        this.ui = ui;
        this.maSvc = maSvc;
        this.editDlg = edit;
        this.delLabel = i18n.getMsg(i18nPrefix + ".del");
        this.delTitleLabel = i18n.getMsg(i18nPrefix + ".delTitle");
        this.delQuestionLabel = i18n.getMsg(i18nPrefix + ".delQuestion");
        this.delYesLabel = i18n.getMsg(i18nPrefix + ".delYes");
        this.delNoLabel = i18n.getMsg(i18nPrefix + ".delNo");

        this.cont = new BeanItemContainer<>(MailTemplate.class);
        addComponent(
                (table = new KfsTable<>(tableName, i18n, MailTemplate.class,
                        i18n.getMsg(i18nPrefix + ".title"), cont, null, this)));
        table.setWidth("100%");
        table.setHeight("500px");
        setSpacing(true);
        setMargin(true);
    }

    public void show(List<MailTemplate> mal, KfsRefresh kfsRefresh) {
        cont.removeAllItems();
        cont.addAll(mal);
    }

    @Override
    public Table.ColumnGenerator getColumnGenerator(Class type, Field field, Pos pos) {
        if ((type == MailTemplate.class) && (field.getName().equals("id"))) {
            return this;
        }
        return null;
    }

    @Override
    public Object generateCell(Table source, Object itemId, Object columnId) {
        MailTemplate ku = (MailTemplate) itemId;
        KfsButton<MailTemplate> edit = new KfsButton<>(ku.getName(), ku, this);
        edit.addStyleName("small");
        Button del = new KfsButtonConfirm(delLabel, ui, delTitleLabel, delQuestionLabel,
                delYesLabel, delNoLabel, new KfsButtonConfirmCallback(this, "delClick"),
                null, null, null, ku);
        del.addStyleName("small");
        HorizontalLayout hl = new HorizontalLayout(edit, del);
        return hl;
    }

    public void delClick(MailTemplate mt) {
        maSvc.mailTemplateRemove(mt);
        cont.removeItem(mt);
        table.refreshRowCache();
    }

    @Override
    public void buttonClick(Button.ClickEvent event) {
        if (editDlg == null) {
            return;
        }
        MailTemplate mt = ((KfsButton<MailTemplate>) event.getButton()).getButtonData();
        if (mt.getId() != null) {
            mt = maSvc.mailTemplateReload(mt);
        }
        editDlg.show(mt, this);
    }

    @Override
    public void kfsRefresh() {
        table.refreshRowCache();
    }
}
