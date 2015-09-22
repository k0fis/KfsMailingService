package kfs.mailingservice.tools;

import com.vaadin.ui.Button;
import com.vaadin.ui.UI;
import com.vaadin.ui.Window;
import java.util.List;
import kfs.kfsvaalib.fields.KfsEditorField;
import kfs.kfsvaalib.listener.KfsButtonClickListener;
import kfs.kfsvaalib.utils.KfsI18n;
import kfs.kfsvaalib.utils.KfsRefresh;

/**
 *
 * @author pavedrim
 */
public class MailAddressListDlg extends Window implements KfsEditorField.Editor<List> {

    private final UI ui;
    private final MailAddressList mal;
    private KfsRefresh kfsRefresh;

    public MailAddressListDlg(UI ui, KfsI18n i18n, MailAddressDlg edit, String i18nPrefix) {
        this.ui = ui;
        mal = new MailAddressList(ui, i18n, edit, i18nPrefix, "MailAddressList");
        mal.addComponent(new Button(i18n.getMsg(i18nPrefix + ".save"),
                new KfsButtonClickListener(this, "saveClick")));
        setContent(mal);
        setModal(true);
        setWidth("500px");
    }

    @Override
    public void show(List data, KfsRefresh kfsRefresh) {
        this.kfsRefresh = kfsRefresh;
        mal.show(data, kfsRefresh);
        ui.addWindow(this);
    }

    @Override
    public List getKfsValue() {
        return mal.getKfsValue();
    }

    @Override
    public String getKfsInfo(List data) {
        if (data != null) {
            return Integer.toString(data.size());
        }
        return "";
    }

    public void saveClick(Button.ClickEvent ce) {
        if (kfsRefresh != null) {
            kfsRefresh.kfsRefresh();
        }
        close();
    }

}
