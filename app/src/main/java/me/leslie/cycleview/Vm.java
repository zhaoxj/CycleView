package me.leslie.cycleview;

/**
 * 介绍：
 * 作者：xjzhao
 * 邮箱：mr.feeling.heart@gmail.com
 * 时间: 2017-03-11  23:18
 */
 
final class Vm {
    private BaseCycleViewModel viewModel;
    private int startPosition;

    public Vm(BaseCycleViewModel viewModel, int startPosition) {
        this.viewModel = viewModel;
        this.startPosition = startPosition;
    }

    BaseCycleViewModel getViewModel() {
        return viewModel;
    }

    Vm setViewModel(BaseCycleViewModel viewModel) {
        this.viewModel = viewModel;
        return this;
    }

    int getStartPosition() {
        return startPosition;
    }

    Vm setStartPosition(int startPosition) {
        this.startPosition = startPosition;
        return this;
    }
}
