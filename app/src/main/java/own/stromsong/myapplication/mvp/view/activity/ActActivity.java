package own.stromsong.myapplication.mvp.view.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import own.stromsong.myapplication.R;
import own.stromsong.myapplication.mvp.base.BaseSupportActivity;

public class ActActivity extends BaseSupportActivity {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    private ActAdapt mActAdapt;
    @Override
    protected int getLayoutId() {
        return R.layout.activity_act;
    }

    @Override
    protected void initData() {
        setLeftIcon(R.drawable.icon_home);
        setTitle("节目单");
        initRecycler();
    }
    /**
     * 初始化recycler
     */
    private void initRecycler() {
        final ArrayList<String> strings = new ArrayList<>();
        strings.add("1.节目1");
        strings.add("2.节目21");
        strings.add("3.节目31");
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(linearLayoutManager);
        mActAdapt = new ActAdapt(R.layout.act_item, strings);
        mRecycler.setAdapter(mActAdapt);
        mActAdapt.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                showToastMsgShort("click"+strings.get(position));
            }
        });
    }

    class ActAdapt extends BaseQuickAdapter<String,BaseViewHolder>{

        public ActAdapt(int layoutResId, @Nullable List<String> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, String item) {
            helper.setText(R.id.name, item);
        }
    }
}
