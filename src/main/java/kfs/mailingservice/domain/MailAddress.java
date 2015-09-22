package kfs.mailingservice.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import kfs.kfsvaalib.kfsForm.KfsField;
import kfs.kfsvaalib.kfsForm.KfsMField;
import kfs.kfsvaalib.kfsTable.KfsTablePos;
import kfs.kfsvaalib.kfsTable.Pos;

/**
 *
 * @author pavedrim
 */
@Entity
public class MailAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "MailAddressId_seq")
    @SequenceGenerator(name = "MailAddressId_seq", sequenceName = "MailAddressId_seq")
    private Long id;
    
    @KfsMField({
        @KfsField(name = "MailAddressDlg", pos = 20),})
    @KfsTablePos({
        @Pos(value = 20, name = "MailAddressDlg"),
        @Pos(value = 20, name = "MailTemplateCcSelect")})
    @Pattern(regexp = "(^$|^[\\w\\-]+(\\.[\\w\\-]+)*@([A-Za-z0-9-]+\\.)+[A-Za-z]{2,6}$)")
    @Size(max = 512)
    @Column(nullable = false, length = 512)
    private String address;

    @KfsMField({
        @KfsField(name = "MailAddressDlg", pos = 10),})
    @KfsTablePos({
        @Pos(value = 10, name = "MailAddressDlg"),
        @Pos(value = 10, name = "MailTemplateCcSelect", genName = "name0"),
    })
    private String name;

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
