package am.ik.meishi.domain;

import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;
import org.springframework.data.domain.Sort;

import java.util.Iterator;
import java.util.List;

@Dao
@ConfigAutowireable
public interface MeishiDao {
    @Select
    List<Meishi> findByLoginUser(String loginUser, String orderBy);

    default List<Meishi> findByLoginUser(String loginUser, Sort sort) {
        return findByLoginUser(loginUser, orderBy(sort));
    }

    @Select
    List<Meishi> findByCompanyIdAndLoginUser(Integer companyId, String loginUser, String orderBy);

    default List<Meishi> findByCompanyIdAndLoginUser(Integer companyId, String loginUser, Sort sort) {
        return findByCompanyIdAndLoginUser(companyId, loginUser, orderBy(sort));
    }

    @Select(ensureResult = true)
    Meishi findByMeishiIdAndLoginUser(Integer meishiId, String loginUser);

    @Insert
    int create(Meishi meishi);

    @Delete
    int delete(Meishi meishi);


    default String orderBy(Sort sort) {
        StringBuilder orderBy = new StringBuilder();
        Iterator<Sort.Order> iterator = sort.iterator();
        while (iterator.hasNext()) {
            Sort.Order order = iterator.next();
            if (orderBy.length() > 0) {
                orderBy.append(",");
            } else {
                orderBy.append("ORDER BY");
            }
            orderBy.append(" ").append(order.getProperty())
                    .append(" ").append(order.getDirection().name());
        }
        return orderBy.toString();
    }
}
