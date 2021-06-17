/*
 * Copyright (c) 2011-2021, baomidou (jobob@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.baomidou.mybatisplus.core.conditions.segments;

import com.baomidou.mybatisplus.core.conditions.ISqlSegment;

import java.util.List;

import static com.baomidou.mybatisplus.core.enums.SqlKeyword.ORDER_BY;
import static java.util.stream.Collectors.joining;

/**
 * Order By SQL 片段
 *
 * @author miemie
 * @since 2018-06-27
 */
@SuppressWarnings("serial")
public class OrderBySegmentList extends AbstractISegmentList {


    @Override
    protected boolean transformList(List<ISqlSegment> list, ISqlSegment firstSegment, ISqlSegment lastSegment) {
        /* 保存ISqlSegment列表构成的order子句， 但是去掉了order*/
        list.remove(0);
        /* 保存的过程中，已经解析了各个ISqlSegment， 每个Seg都重新封装为() -> sql，大概是为了效率*/
        final String sql = list.stream().map(ISqlSegment::getSqlSegment).collect(joining(SPACE));
        list.clear();
        list.add(() -> sql);
        return true;
    }

    /* 取出ISqlSegment列表，在头部添加了ORDER Seg*/
    @Override
    protected String childrenSqlSegment() {
        if (isEmpty()) {
            return EMPTY;
        }
        return this.stream().map(ISqlSegment::getSqlSegment).collect(joining(COMMA, SPACE + ORDER_BY.getSqlSegment() + SPACE, EMPTY));
    }
}
