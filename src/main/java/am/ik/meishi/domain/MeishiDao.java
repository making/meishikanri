package am.ik.meishi.domain;

import org.seasar.doma.Dao;
import org.seasar.doma.Delete;
import org.seasar.doma.Insert;
import org.seasar.doma.Select;
import org.seasar.doma.boot.ConfigAutowireable;
import org.seasar.doma.boot.Pageables;
import org.seasar.doma.jdbc.SelectOptions;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Iterator;
import java.util.List;

@Dao
@ConfigAutowireable
public interface MeishiDao {
    @Select
    List<Meishi> findByLoginUser(String loginUser, SelectOptions options, String orderBy);

    default Page<Meishi> findByLoginUser(String loginUser, Pageable pageable) {
        SelectOptions options = Pageables.toSelectOptions(pageable).count();
        List<Meishi> list = findByLoginUser(loginUser, options, orderBy(pageable.getSort()));
        return new PageImpl<>(list, pageable, options.getCount());
    }

    @Select
    List<Meishi> findByCompanyIdAndLoginUser(Integer companyId, String loginUser, SelectOptions options, String orderBy);

    default Page<Meishi> findByCompanyIdAndLoginUser(Integer companyId, String loginUser, Pageable pageable) {
        SelectOptions options = Pageables.toSelectOptions(pageable).count();
        List<Meishi> list = findByCompanyIdAndLoginUser(companyId, loginUser, options, orderBy(pageable.getSort()));
        return new PageImpl<>(list, pageable, options.getCount());
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
