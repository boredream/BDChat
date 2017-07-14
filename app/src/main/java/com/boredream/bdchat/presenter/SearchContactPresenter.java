package com.boredream.bdchat.presenter;

import com.boredream.bdcodehelper.entity.ListResponse;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.net.DefaultDisposableObserver;
import com.boredream.bdcodehelper.net.HttpRequest;
import com.boredream.bdcodehelper.net.RxComposer;
import com.boredream.bdcodehelper.utils.StringUtils;

import java.util.ArrayList;

public class SearchContactPresenter implements SearchContactContract.Presenter {

    private static final int PAGE_COUNT = 20;

    // TODO: 2017/7/13 封装多页加载
    private int curPage;
    private String searchKey;

    private final SearchContactContract.View view;

    public SearchContactPresenter(SearchContactContract.View view) {
        this.view = view;
    }

    @Override
    public void search(String phone, final boolean isLoadMore) {
        final int page;
        if (isLoadMore) {
            page = curPage + 1;
        } else {
            if(StringUtils.isEmpty(phone)) {
                view.showTip("搜索内容不能为空");
                return;
            }

            searchKey = phone;
            page = curPage = 1;
        }

        HttpRequest.getSingleton()
                .getUsersByUsername(searchKey, page, PAGE_COUNT)
                .compose(RxComposer.<ListResponse<User>>schedulers())
                .compose(RxComposer.<User>handleListResponse())
                .subscribe(new DefaultDisposableObserver<ArrayList<User>>(view) {

                    @Override
                    public void onStart() {
                        if(!isLoadMore) view.showProgress();
                    }

                    @Override
                    public void onNext(ArrayList<User> users) {
                        super.onNext(users);

                        if (isLoadMore) {
                            curPage = page;
                        }

                        boolean haveMore = users.size() >= PAGE_COUNT;
                        view.searchSuccess(users, isLoadMore, haveMore);
                    }

                    @Override
                    public void onError(Throwable e) {
                        super.onError(e);

                        view.searchError();
                    }
                });
    }

}
