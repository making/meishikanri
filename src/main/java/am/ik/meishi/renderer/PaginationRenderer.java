package am.ik.meishi.renderer;


import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import org.springframework.web.util.UriUtils;

import java.io.UnsupportedEncodingException;

@Component
public class PaginationRenderer<T> {

    public String render(Page<T> page) throws UnsupportedEncodingException {
        return new Pager<>(page).render();
    }

    @Data
    static class Pager<T> {
        private final Page<T> page;
        private final int maxDisplayCount = 5;

        String render() throws UnsupportedEncodingException {
            StringBuilder sb = new StringBuilder();
            long current = page.getNumber();
            BeginAndEnd beginAndEnd = calcBeginAndEnd();
            UriComponentsBuilder builder = ServletUriComponentsBuilder.fromCurrentRequest();

            sb.append("<li");
            sb.append(">");
            if (!page.isFirst()) {
                builder.replaceQueryParam("page", 0);
                sb.append("<a href=\"");
                sb.append(UriUtils.decode(builder.build().toString(), "UTF-8"));
                sb.append("\">");
            } else {
                sb.append("<a>");
            }
            sb.append("&lt;&lt;");
            sb.append("</a>");

            sb.append("</li>");
            for (long p = beginAndEnd.begin; p <= beginAndEnd.end; p++) {
                boolean active = (p == current);
                builder.replaceQueryParam("page", p);
                sb.append("<li");
                if (active) {
                    sb.append(" class=\"is-active\"");
                }
                sb.append(">");
                if (active) {
                    sb.append("<a>");
                } else {
                    sb.append("<a href=\"");
                    sb.append(UriUtils.decode(builder.build().toString(), "UTF-8"));
                    sb.append("\">");
                }
                sb.append(p + 1);
                sb.append("</a>");
                sb.append("</li>");
            }
            sb.append("<li");
            sb.append(">");
            if (!page.isLast()) {
                builder.replaceQueryParam("page", page.getTotalPages() - 1);
                sb.append("<a href=\"");
                sb.append(UriUtils.decode(builder.build().toString(), "UTF-8"));
                sb.append("\">");
            } else {
                sb.append("<a>");
            }
            sb.append("&gt;&gt;");
            sb.append("</a>");
            sb.append("</li>");
            return sb.toString();
        }

        BeginAndEnd calcBeginAndEnd() {
            long begin = Math.max(0, this.page.getNumber() - (int) Math.floor(this.maxDisplayCount * 0.5));
            long end = begin + this.maxDisplayCount - 1;
            long last = this.page.getTotalPages() - 1;
            if (end > last) {
                end = last;
                begin = Math.max(0, end - (this.maxDisplayCount - 1));
            }
            return new BeginAndEnd(begin, end);
        }
    }

    @Data
    static class BeginAndEnd {
        final long begin;
        final long end;
    }
}
