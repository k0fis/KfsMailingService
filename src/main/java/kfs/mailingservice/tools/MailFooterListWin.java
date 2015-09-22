package kfs.mailingservice.tools;

import com.vaadin.ui.Button;
import com.vaadin.ui.Component;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.Window;
import kfs.kfsvaalib.listener.KfsButtonClickListener;
import kfs.kfsvaalib.utils.KfsI18n;
import kfs.kfsvaalib.utils.KfsRefresh;
import kfs.mailingservice.domain.MailFooter;
import kfs.mailingservice.service.MailingService;

/**
 *
 * @author pavedrim
 */
public class MailFooterListWin extends Window implements KfsRefresh {

    private final UI ui;
    private final MailFooterList l;
    private final MailingService maSvc;
    private final MailFooterDlg edit;

    public MailFooterListWin(UI ui, KfsI18n i18n, MailingService maSvc, Component... comps) {
        this(ui, i18n, maSvc, new MailFooterDlg(ui, i18n, maSvc), "MailFooterListWin", comps);
    }

    public MailFooterListWin(UI ui, KfsI18n i18n, MailingService maSvc, MailFooterDlg edit,
            String i18nPrefix, Component... comps) {
        super(i18n.getMsg(i18nPrefix + ".title"));
        this.ui = ui;
        this.edit = edit;
        this.maSvc = maSvc;
        l = new MailFooterList(ui, i18n, maSvc);
        VerticalLayout vl = new VerticalLayout();
        vl.addComponent(new Button(i18n.getMsg(i18nPrefix + ".add"), 
                new KfsButtonClickListener(this, "clickAdd")));
        vl.addComponent(l);
        vl.addComponents(comps);
        vl.setSpacing(true);
        vl.setMargin(true);
        setContent(vl);
        setSizeFull();
        setModal(true);
    }

    public void show() {
        kfsRefresh();
        ui.addWindow(this);
    }

    @Override
    public void kfsRefresh() {
        l.show(maSvc.mailFooterLoad(), this);
    }

    public void clickAdd(Button.ClickEvent event) {
        edit.show(new MailFooter(), this);
    }
}
