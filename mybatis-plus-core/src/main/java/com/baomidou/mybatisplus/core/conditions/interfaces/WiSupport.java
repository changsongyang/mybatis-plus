package com.baomidou.mybatisplus.core.conditions.interfaces;

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.core.toolkit.StringPool;

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
public interface WiSupport<Mut> {

    String convMut2ColInSel(Mut mutable);

    default ISqlSegment convMut2ColSegment(Mut mutable) {
        return () -> convMut2Col(mutable);
    }

    String convMut2Col(Mut mutable);

    default Supplier<String> mappingSupplier(boolean mapping, Mut mutable) {
        return mapping ? () -> convMut2ColMapping(mutable) : null;
    }

    String convMut2ColMapping(Mut mutable);

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

    default ISqlSegment mutPeek(Mut column, Collection<Mut> columns) {
        return () -> {
            List<String> cs = new ArrayList<>();
            cs.add(convMut2Col(column));
            if (CollectionUtils.isNotEmpty(columns)) {
                cs.addAll(columns.stream().filter(Objects::nonNull).map(this::convMut2Col)
                    .collect(Collectors.toList()));
            }
            return String.join(StringPool.COMMA, cs);
        };
    }
}
