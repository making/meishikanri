package am.ik.meishi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.seasar.doma.*;

import java.io.Serializable;

@Data
@Entity
@Builder
@AllArgsConstructor
public class Meishi implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer meishiId;
    private String firstName;
    private String lastName;
    private String firstNameKanji;
    private String lastNameKanji;
    private Integer companyId;
    @Column(insertable = false)
    private String companyName;
    private String email;
    private String meishiFront;
    private String meishiBack;
    private String loginUser;

    public Meishi() {
    }
}
