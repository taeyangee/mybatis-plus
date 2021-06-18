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
package com.baomidou.mybatisplus.core.conditions;


import java.io.Serializable;

/**
 * SQL 片段接口
 *
 * 思路：一个sql可以拆解为若干SQL片段，是整个conditions包的核心抽象
 * - 最终输出的完整查询条件， 是ISqlSegment
 * - 查询条件各种拼接、嵌套、生成，都是在管理对应的ISqlSegment
 * - 围绕ISqlSegment接口， AbstractWrapper/AbstractISegmentList采用组合模型、解析器模型完成了复杂条件的组装和sql语句输出
 * -
 * @author hubin miemie HCL
 * @since 2018-05-28
 */
@FunctionalInterface
public interface ISqlSegment extends Serializable {

    /**
     * SQL 片段
     */
    String getSqlSegment();
}
