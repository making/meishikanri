package am.ik.meishi.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.seasar.doma.Entity;
import org.seasar.doma.GeneratedValue;
import org.seasar.doma.GenerationType;
import org.seasar.doma.Id;

import java.io.Serializable;

@Entity
@Data
@AllArgsConstructor
@Builder
public class Company implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer companyId;
    private String companyName;

    public Company() {
    }
}
