package kfs.mailingservice.tools;

import com.vaadin.data.Buffered;
import com.vaadin.data.Validator;
import com.vaadin.ui.Button;
import com.vaadin.ui.Notification;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import java.util.logging.Level;
import java.util.logging.Logger;
import kfs.kfsvaalib.fields.KfsEditorField;
import kfs.kfsvaalib.listener.KfsButtonClickListener;
import kfs.kfsvaalib.utils.KfsI18n;
import kfs.kfsvaalib.utils.KfsRefresh;
import kfs.mailingservice.domain.MailTemplate;
import kfs.mailingservice.domain.MailTemplateAttach;
import kfs.mailingservice.service.MailingService;

/**
 *
 * @author pavedrim
 */
public class MailTemplateAttachDlg extends Window implements KfsEditorField.Editor<MailTemplateAttach> {

    private final UI ui;
    private final KfsI18n i18n;
    private final String i18nPref;
    private final MailAttachField data;
    private final MailingService maSvc;
    private MailTemplateAttach ma;
    private KfsRefresh kfsRefresh;

    public MailTemplateAttachDlg(UI ui, KfsI18n i18n, MailingService maSvc) {
        this(ui, i18n, maSvc, "MailAttachDlg", "MailAttachDlg");
    }

    public MailTemplateAttachDlg(UI ui, KfsI18n i18n, MailingService maSvc,
            String i18nPrefix, String formName) {
        super(i18n.getMsg(i18nPrefix + ".title"));
        this.maSvc = maSvc;
        this.ui = ui;
        this.i18n = i18n;
        this.i18nPref = i18nPrefix;
        data = new MailAttachField(i18n, i18nPrefix, formName);
        VerticalLayout vl = new VerticalLayout(data);
        vl.addComponent(new Button(i18n.getMsg(i18nPrefix + ".save"),
                new KfsButtonClickListener(this, "saveClick")));
        vl.setSpacing(true);
        vl.setMargin(true);
        setContent(vl);

        setModal(true);
    }

    @Override
    public void show(MailTemplateAttach ma, KfsRefresh kfsRefresh) {
        this.ma = ma;
        this.kfsRefresh = kfsRefresh;
        data.setValue(ma.getAttach());
        ui.addWindow(this);
    }

    public void saveClick(Button.ClickEvent event) {
        CharSequence val = data.validate("");
        if (val.length() > 0) {
            Notification.show(i18n.getMsg(i18nPref + ".cannotSave") + ":\n" + val,
                    Notification.Type.WARNING_MESSAGE);
            return;
        }
        try {
            data.commit();
        } catch (Buffered.SourceException | Validator.InvalidValueException | kfs.kfsvaalib.utils.KfsVaaException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
            return;
        }
        maSvc.mailTemplateAttachSave(ma);
        Notification.show(i18n.getMsg(i18nPref + ".saved"));
        if (kfsRefresh != null) {
            kfsRefresh.kfsRefresh();
        }
    }

    @Override
    public String getKfsInfo(MailTemplateAttach ma) {
        if ((ma != null) && (ma.getAttach() != null) && (ma.getAttach().getAttachName() != null)) {
            return ma.getAttach().getAttachName();
        }
        return "";
    }

    @Override
    public MailTemplateAttach getKfsValue() {
        return ma;
    }
}
