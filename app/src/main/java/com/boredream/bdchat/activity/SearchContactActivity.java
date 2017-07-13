package com.boredream.bdchat.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.boredream.bdchat.R;
import com.boredream.bdchat.adapter.ContactAdapter;
import com.boredream.bdchat.base.BaseActivity;
import com.boredream.bdchat.presenter.SearchContactContract;
import com.boredream.bdchat.presenter.SearchContactPresenter;
import com.boredream.bdcodehelper.adapter.LoadMoreAdapter;
import com.boredream.bdcodehelper.entity.User;
import com.boredream.bdcodehelper.view.SearchBar;

import java.util.ArrayList;
import java.util.List;

public class SearchContactActivity extends BaseActivity implements SearchContactContract.View {

    private SearchContactPresenter presenter;

    private SearchBar searchbar;
    private RecyclerView rv;
    private LoadMoreAdapter adapter;
    private List<User> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_contact);

        initView();
    }

    private void initView() {
        presenter = new SearchContactPresenter(this);

        searchbar = (SearchBar) findViewById(R.id.searchbar);
        rv = (RecyclerView) findViewById(R.id.rv);
        rv.setLayoutManager(new LinearLayoutManager(this));
        adapter = new LoadMoreAdapter(rv, new ContactAdapter(this, users), new LoadMoreAdapter.OnLoadMoreListener() {
            @Override
            public void onLoadMore() {
                presenter.search(null, true);
            }
        });
        rv.setAdapter(adapter);

        // TODO: 2017/7/13 pull to refresh

        searchbar.initSearchBar("请输入要搜索用户的手机号", new SearchBar.OnSearchListener() {
            @Override
            public void onSearch(@NonNull String searchKey) {
                presenter.search(searchKey, false);
            }
        });
    }

    @Override
    public void searchSuccess(ArrayList<User> users, boolean isLoadMore, boolean haveMore) {
        adapter.setStatus(haveMore ? LoadMoreAdapter.STATUS_HAVE_MORE : LoadMoreAdapter.STATUS_LOADED_ALL);
        if(!isLoadMore) {
            this.users.clear();
        }
        this.users.addAll(users);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void searchError() {
        // FIXME: 2017/7/13 是否需要这个？
    }
}
