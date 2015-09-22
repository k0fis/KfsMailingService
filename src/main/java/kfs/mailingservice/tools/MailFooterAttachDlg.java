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
import kfs.mailingservice.domain.MailFooterAttach;

/**
 *
 * @author pavedrim
 */
public class MailFooterAttachDlg extends Window implements KfsEditorField.Editor<MailFooterAttach> {

    private final UI ui;
    private final KfsI18n i18n;
    private final String i18nPref;
    private final MailAttachField data;
    private MailFooterAttach ma;
    private KfsRefresh kfsRefresh;

    public MailFooterAttachDlg(UI ui, KfsI18n i18n) {
        this(ui, i18n, "MailAttachDlg", "MailAttachDlg");
    }

    public MailFooterAttachDlg(UI ui, KfsI18n i18n, String i18nPrefix, String formName) {
        super(i18n.getMsg(i18nPrefix + ".title"));
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
        setWidth("700px");
    }

    @Override
    public void show(MailFooterAttach ma, KfsRefresh kfsRefresh) {
        this.ma = ma;
        this.kfsRefresh = kfsRefresh;
        data.setValue(ma.getAttach());
        setCaption(getKfsInfo(ma));
        ui.addWindow(this);
    }

    public void saveClick(Button.ClickEvent event) {
        try {
            data.commit();
        } catch (Buffered.SourceException | Validator.InvalidValueException ex) {
            Logger.getLogger(getClass().getName()).log(Level.SEVERE, ex.getMessage(), ex);
            CharSequence val = data.validate("");
            Notification.show(i18n.getMsg(i18nPref + ".cannotSave") + ":\n" + val,
                    Notification.Type.WARNING_MESSAGE);
            return;
        }
        if (kfsRefresh != null) {
            kfsRefresh.kfsRefresh();
        }
        this.close();
    }

    @Override
    public String getKfsInfo(MailFooterAttach ma) {
        if ((ma != null) && (ma.getAttach() != null)) {
            return ma.getAttach().getAttachName() + " - " + ma.getAttach().getContentId();
        }
        return "";
    }
    
    @Override
    public MailFooterAttach getKfsValue() {
        return ma;
    }
    
}
