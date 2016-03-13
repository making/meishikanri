package am.ik.meishi.domain;

import org.seasar.doma.Dao;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Dao
@ConfigAutowireable
public interface CompanyDao {
    @Select
    Optional<Company> findOne(Integer companyId);

    @Select
    Optional<Company> findByCompanyName(String companyName);

    @Insert
    @Transactional
    int create(Company company);
}
