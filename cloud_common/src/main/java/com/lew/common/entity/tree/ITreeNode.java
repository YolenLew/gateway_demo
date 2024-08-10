/*
 * Copyright (c) 2024. This is my custom copyright information.2024-2024. All rights reserved.
 */

package com.lew.common.entity.tree;

import java.util.List;

/**
 * 作者：light0x00
 * 链接：<a href="https://www.zhihu.com/question/535295797/answer/3006181813">处理树结构</a>
 * 来源：知乎
 * 著作权归作者所有。商业转载请联系作者获得授权，非商业转载请注明出处。
 *
 * @param <T>
 */
public interface ITreeNode<T extends ITreeNode<T>> {
    T parent();

    void setChildren(List<T> children);

    List<T> getChildren();
}