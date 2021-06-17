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
/**
 * SQL 片段相关类
 *
 * - 实现了各种ISqlSegment
 * - 各种ISqlSegment之间使用了组合模式进行整合？
 * - MergeSegments整合了 GroupBySegmentList、HavingSegmentList、NormalSegmentList、OrderBySegmentList
 * -
 */
package com.baomidou.mybatisplus.core.conditions.segments;
