package kfs.mailingservice.tools;

import com.vaadin.ui.Button;
import com.vaadin.ui.FormLayout;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Notification;
import com.vaadin.ui.Panel;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import java.util.logging.Level;
import java.util.logging.Logger;
import kfs.kfsvaalib.fields.KfsEditorField;
import kfs.kfsvaalib.kfsForm.MFieldGroup;
import kfs.kfsvaalib.listener.KfsButtonClickListener;
import kfs.kfsvaalib.utils.KfsI18n;
import kfs.kfsvaalib.utils.KfsRefresh;
import kfs.mailingservice.domain.MailFooter;
import kfs.mailingservice.service.MailingService;


/**
 *
 * @author pavedrim
 */
public class MailFooterDlg extends Window implements KfsEditorField.Editor<MailFooter>{

    private final UI ui;
    private final KfsI18n i18n;
    private final MFieldGroup fg;
    private final String i18nPref;
    private final MailingService maSvc;
    private final MailFooterAttachList flist;
    private MailFooter ma;
    private KfsRefresh kfsRefresh;

    public MailFooterDlg(UI ui, KfsI18n i18n, MailingService maSvc) {
        this(ui, i18n, maSvc, new MailFooterAttachDlg(ui, i18n), "MailFooterDlg", "MailFooterDlg");
    }

    public MailFooterDlg(UI ui, KfsI18n i18n,MailingService maSvc, MailFooterAttachDlg edit,
            String i18nPrefix, String formName) {
        super(i18n.getMsg(i18nPrefix + ".title"));
        this.ui = ui;
        this.i18n = i18n;
        this.maSvc = maSvc;
        this.i18nPref = i18nPrefix;
        flist = new MailFooterAttachList(ui, i18n);
        fg = new MFieldGroup(i18n, formName, null, MailFooter.class);
        fg.setWidth("400px");
        flist.setWidth("450px");
        FormLayout form = new FormLayout(fg.getSortedComponents());
        form.addComponent(new Button(i18n.getMsg(i18nPrefix + ".save"), 
                new KfsButtonClickListener(this, "saveClick")));
        form.setMargin(true);
        Panel pl = new Panel(form);
        pl.setWidth("550px");
        pl.setHeight("650px");
        HorizontalLayout hl = new HorizontalLayout(pl);
        pl = new Panel(flist);
        pl.setWidth("550px");
        pl.setHeight("350px");
        hl.addComponent(pl);
        
        hl.setSpacing(true);
        hl.setMargin(true);
        setContent(hl);
        setModal(true);
    }

    @Override
    public void show(MailFooter ma, KfsRefresh kfsRefresh) {
        this.ma = ma;
        this.kfsRefresh = kfsRefresh;
        fg.setItems(ma);
        flist.show(maSvc.mailFooterAttachLoad(ma));
        setCaption(getKfsInfo(ma));
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
        }
        maSvc.mailFooterSave(ma, flist.getMailFooterAttach());
        Notification.show(i18n.getMsg(i18nPref + ".saved"));
        if (kfsRefresh != null) {
            kfsRefresh.kfsRefresh();
        }
        this.close();
    }

    @Override
    public String getKfsInfo(MailFooter ma) {
        if (ma == null) {
            return "";
        }
        return ma.getName();
    }

    @Override
    public MailFooter getKfsValue() {
        return ma;
    }
}
