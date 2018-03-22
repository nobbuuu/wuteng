package own.stromsong.myapplication.mvp.view.activity;


import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.blankj.utilcode.util.ActivityUtils;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.weavey.loading.lib.LoadingLayout;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import own.stromsong.myapplication.R;
import own.stromsong.myapplication.mvp.base.MvpActivity;
import own.stromsong.myapplication.mvp.model.MenuBean;
import own.stromsong.myapplication.mvp.presenter.MenuPresenter;
import own.stromsong.myapplication.mvp.view.interfaces.IMenu;

public class ActActivity extends MvpActivity<MenuPresenter> implements IMenu {

    @BindView(R.id.recycler)
    RecyclerView mRecycler;
    private ActAdapt mActAdapt;
    private List<MenuBean.ListResultBean.ListshowBean.Material.MaterialBean> mList = new ArrayList<>();

    @Override
    protected int getLayoutId() {
        return R.layout.activity_act;
    }

    @Override
    protected void initData() {
        setLeftIcon(R.drawable.icon_home);
        setTitle("节目单");
        initRecycler();
        mvpPresenter.getList();
        getLoadLayout().setOnReloadListener(new LoadingLayout.OnReloadListener() {
            @Override
            public void onReload(View v) {
                mvpPresenter.getList();
            }
        });
    }

    /**
     * 初始化recycler
     */
    private void initRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecycler.setLayoutManager(linearLayoutManager);
        mActAdapt = new ActAdapt(R.layout.act_item, null);
        mRecycler.setAdapter(mActAdapt);
        mActAdapt.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                mList.clear();
                List<MenuBean.ListResultBean> data = mActAdapt.getData();//节目单
                if (data != null) {
                    List<MenuBean.ListResultBean.ListshowBean> menu = data.get(position).getListshow();
                    if (menu != null) {
                        for (int i1 = 0; i1 < menu.size(); i1++) {//节目
                            List<MenuBean.ListResultBean.ListshowBean.Material> material = menu.get(i1).getListMaterial();
                            if (material != null) {
                                for (int i = 0; i < material.size(); i++) {
                                    MenuBean.ListResultBean.ListshowBean.Material.MaterialBean materialBean = material.get(i).getMaterial();
                                    mList.add(materialBean);
                                }
                            }
                        }
                    }
                }
                if (mList.size() == 0) {
                    showToastMsgShort("暂无素材,播放默认素材");
                }
                VideoActivity.startVideoActivity(ActActivity.this,mList);
            }
        });
    }

    @Override
    protected MenuPresenter createPresenter() {
        return new MenuPresenter(this);
    }

    @Override
    public void getMenuList(List<MenuBean.ListResultBean> showMenu) {
        if (showMenu != null) {
            mActAdapt.replaceData(showMenu);
        } else {
            showEmpty();
        }
    }

    class ActAdapt extends BaseQuickAdapter<MenuBean.ListResultBean, BaseViewHolder> {

        public ActAdapt(int layoutResId, @Nullable List<MenuBean.ListResultBean> data) {
            super(layoutResId, data);
        }

        @Override
        protected void convert(BaseViewHolder helper, MenuBean.ListResultBean item) {
            MenuBean.ListResultBean.ShowMenuBean showMenu = item.getShowMenu();//节目单
            if (showMenu==null) return;
            helper.setText(R.id.name, "节目单1");
            helper.setText(R.id.value, "3'30''00");
        }
    }
}
