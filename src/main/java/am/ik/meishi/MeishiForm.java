package am.ik.meishi;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
public class MeishiForm implements Serializable {
    private String firstName;
    private String lastName;
    private String firstNameKanji;
    private String lastNameKanji;
    private String companyName;
    private String email;
    private MultipartFile meishiFront;
    private MultipartFile meishiBack;
}
