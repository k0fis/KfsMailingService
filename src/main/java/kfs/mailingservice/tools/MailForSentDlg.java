package kfs.mailingservice.tools;

import com.vaadin.ui.Button;
import com.vaadin.ui.Field;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.logging.Level;
import java.util.logging.Logger;
import kfs.kfsvaalib.fields.KfsEditorField;
import kfs.kfsvaalib.kfsForm.KfsField;
import kfs.kfsvaalib.kfsForm.MFieldGroup;
import kfs.kfsvaalib.listener.KfsButtonClickListener;
import kfs.kfsvaalib.utils.KfsI18n;
import kfs.kfsvaalib.utils.KfsRefresh;
import kfs.mailingservice.domain.MailAddress;
import kfs.mailingservice.domain.MailForSent;
import kfs.mailingservice.domain.MailTemplate;
import kfs.mailingservice.service.MailingService;

/**
 *
 * @author pavedrim
 */
public class MailForSentDlg extends Window implements KfsEditorField.Editor<MailForSent>, 
        MFieldGroup.MFieldFactory {

    private final UI ui;
    private final KfsI18n i18n;
    private final String i18nPref;
    private final MFieldGroup fg;
    private final KfsEditorField mailAddressEditor;
    private final KfsEditorField mailTemplateEditor;
    private final MailingService maSvc;
    private MailForSent ma;
    private KfsRefresh kfsRefresh;

    public MailForSentDlg(UI ui, KfsI18n i18n, MailingService maSvc) {
        this(ui, i18n, maSvc, "MailForSentDlg", "MailForSentDlg");
    }

    public MailForSentDlg(UI ui, KfsI18n i18n, MailingService maSvc, 
            String i18nPrefix, String formName) {
        super(i18n.getMsg(i18nPrefix + ".title"));
        this.maSvc = maSvc;
        this.ui = ui;
        this.i18n = i18n;
        this.i18nPref = i18nPrefix;
        mailAddressEditor = new KfsEditorField<>(i18n.getMsg(i18nPrefix+".editAddress"), 
                MailAddress.class, new MailAddressDlg(ui, i18n, maSvc), null);
        mailTemplateEditor = new KfsEditorField<>(i18n.getMsg(i18nPrefix+".editTemplate"), 
                MailTemplate.class, new MailTemplateDlg(ui, i18n, maSvc), null);
                
        fg = new MFieldGroup(i18n, formName, this, MailForSent.class);
        VerticalLayout vl =new VerticalLayout(new FormLayout(fg.getSortedComponents()));
        HorizontalLayout hl =new HorizontalLayout(new Button(i18n.getMsg(i18nPrefix+".save"), 
            new KfsButtonClickListener(this, "saveClick")));
        vl.addComponent(hl);
        hl.setSpacing(true);
        vl.setMargin(true);
        vl.setSpacing(true);
        setContent(vl);
        setModal(true);
    }

    @Override
    public void show(MailForSent ma, KfsRefresh kfsRefresh) {
        this.ma = ma;
        this.kfsRefresh = kfsRefresh;
        fg.setItems(ma);
        ui.addWindow(this);
    }

    public void saveClick(Button.ClickEvent event) {
        try {
            fg.commit();
        } catch (Exception ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
            CharSequence val = fg.validate("");
            Notification.show(i18n.getMsg(i18nPref + ".cannotSave") + ":\n" + val,
                    Notification.Type.WARNING_MESSAGE);
            return;
        }
        maSvc.mailForSentSave(ma);
        Notification.show(i18n.getMsg(i18nPref + ".saved"));
        if (kfsRefresh != null) {
            kfsRefresh.kfsRefresh();
        }
    }

    @Override
    public Field createField(Class objectClass, String filedName, KfsField kfsField, Class fieldClass) {
        if (fieldClass.equals(MailAddress.class)) {
            return mailAddressEditor;
        } else if (fieldClass.equals(MailTemplate.class)) {
            return mailTemplateEditor;
        }
        return null;
    }

    @Override
    public String getKfsInfo(MailForSent ma) {
        if (ma == null) {
            return "";
        }
        return ma.getToAddress().getAddress();
    }

    @Override
    public MailForSent getKfsValue() {
        return ma;
    }
}
