package com.baomidou.mybatisplus.core.conditions.interfaces;

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;
import com.baomidou.mybatisplus.core.toolkit.support.SFunction;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

/**
 * @author miemie
 * @since 2025/9/25
 */
public interface SfSupport<T> {

    String convSf2ColInSel(SFunction<T, ?> function);

    default ISqlSegment convSf2ColSegment(SFunction<T, ?> function) {
        return () -> convSf2Col(function);
    }

    String convSf2Col(SFunction<T, ?> mutable);

    default Supplier<String> mappingSupplier(boolean mapping, SFunction<T, ?> function) {
        return mapping ? () -> convSf2ColMapping(function) : null;
    }

    String convSf2ColMapping(SFunction<T, ?> function);

    default ISqlSegment strCol2Segment(String column) {
        return () -> checkStrCol(column);
    }

    String checkStrCol(String column);

    default ISqlSegment strPeek(String column, Collection<String> columns) {
        return () -> {
            List<String> cs = new ArrayList<>();
            cs.add(checkStrCol(column));
            if (CollectionUtils.isNotEmpty(columns)) {
                cs.addAll(columns.stream().filter(Objects::nonNull).peek(this::checkStrCol).collect(Collectors.toList()));
            }
            return String.join(StringPool.COMMA, cs);
        };
    }

    default ISqlSegment mutPeek(SFunction<T, ?> column, Collection<SFunction<T, ?>> columns) {
        return () -> {
            List<String> cs = new ArrayList<>();
            cs.add(convSf2Col(column));
            if (CollectionUtils.isNotEmpty(columns)) {
                cs.addAll(columns.stream().filter(Objects::nonNull).map(this::convSf2Col).collect(Collectors.toList()));
            }
            return String.join(StringPool.COMMA, cs);
        };
    }
}
