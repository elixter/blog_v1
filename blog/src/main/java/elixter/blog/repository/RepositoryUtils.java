package elixter.blog.repository;

import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class RepositoryUtils {

    public static String getOrderBy(Pageable pageable) {
        Sort.Order order = pageable.getSort().stream().findFirst().orElse(Sort.Order.by("createAt"));
        Matcher m = Pattern.compile("(?<=[a-z])[A-Z]").matcher(order.getProperty());
        String underScore = m.replaceAll(match -> "_" + match.group().toLowerCase());
        String orderBy = "order by " + underScore + " " + order.getDirection();
        return orderBy;
    }
}
