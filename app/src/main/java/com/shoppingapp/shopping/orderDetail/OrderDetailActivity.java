package com.shoppingapp.shopping.orderDetail;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.shoppingapp.common.base.BaseActivity;
import com.shoppingapp.data.model.OrderModel;
import com.shoppingapp.exts.ItemOffsetDecoration;
import com.shoppingapp.shopping.orderDetail.adapter.OrderDetailAdapter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import com.shoppingapp.R;

public class OrderDetailActivity extends BaseActivity<OrderDetailPresenter> implements OrderDetailView, View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.llError)
    LinearLayout llError;

    @BindView(R.id.rvProducts)
    RecyclerView rvProducts;

    @BindView(R.id.btnRetry)
    Button btnRetry;

    @BindView(R.id.pbLoader)
    ProgressBar pbLoader;

    private String orderId;

    @Inject
    OrderDetailAdapter orderDetailAdapter;

    public static Intent newIntent(Context context) {
        return new Intent(context, OrderDetailActivity.class);
    }

    @Override
    protected int layout() {
        return R.layout.activity_order_detail;
    }

    @Override
    protected void initView() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.order_detail);
        }
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setTitleTextColor(ContextCompat.getColor(this, R.color.colorWhite));
        setupRecyclerView();
        initListeners();
    }

    private void initListeners() {
        btnRetry.setOnClickListener(this);
    }

    private void setupRecyclerView() {
        RecyclerView.LayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        rvProducts.setLayoutManager(gridLayoutManager);
        ItemOffsetDecoration itemDecoration = new ItemOffsetDecoration(this, R.dimen._1sdp);
        rvProducts.addItemDecoration(itemDecoration);
        rvProducts.setAdapter(orderDetailAdapter);
        fetchProducts();
    }

    private void fetchProducts() {
        orderId = getIntent().getStringExtra("orderId");
        presenter.getOrderedProductById(orderId);
    }

    @Override
    protected void attachView() {
        presenter.attachView(this);
    }

    @Override
    public void showLoader() {
        pbLoader.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideLoader() {
        pbLoader.setVisibility(View.GONE);
    }

    @Override
    public void showError() {
        llError.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideError() {
        llError.setVisibility(View.GONE);
    }

    @Override
    public void retry() {
        presenter.getOrderedProductById(orderId);
    }

    @Override
    public void showProducts(List<OrderModel> productsBeans) {
        orderDetailAdapter.updateProducts(productsBeans);
        rvProducts.setVisibility(View.VISIBLE);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                navigateToPrevious();
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnRetry:
                presenter.retry();
                break;
        }
    }
}
