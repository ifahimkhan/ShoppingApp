package com.shoppingapp.shopping.orderListing;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.shoppingapp.R;
import com.shoppingapp.common.base.BaseActivity;
import com.shoppingapp.shopping.orderDetail.OrderDetailActivity;
import com.shoppingapp.shopping.orderListing.adapter.OrderListingAdapter;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;

public class OrderListingActivity extends BaseActivity<OrderListingPresenter>
        implements OrderListingView, View.OnClickListener {

    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @BindView(R.id.llError)
    LinearLayout llError;

    @BindView(R.id.rvOrders)
    RecyclerView rvOrders;

    @BindView(R.id.btnRetry)
    Button btnRetry;

    @BindView(R.id.pbLoader)
    ProgressBar pbLoader;

    @BindView(R.id.tvNoOrders)
    TextView tvNoOrders;

    @Inject
    OrderListingAdapter orderListingAdapter;

    public static Intent newIntent(Context context) {
        return new Intent(context, OrderListingActivity.class);
    }

    @Override
    protected int layout() {
        return R.layout.activity_order;
    }

    @Override
    protected void initView() {
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setTitle(R.string.order);
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
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        rvOrders.setLayoutManager(layoutManager);
        rvOrders.setAdapter(orderListingAdapter);
        fetchOrders();
    }

    private void fetchOrders() {
        presenter.getAllOrders();
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
        presenter.getAllOrders();
    }


    @Override
    public void showOrders(List<String> ordersArray) {
        HashSet<String> hashSet = new LinkedHashSet<>(ordersArray);
        ordersArray.clear();
        ordersArray.addAll(hashSet);
        Collections.reverse(ordersArray);
        orderListingAdapter.updateOrders(ordersArray);

        if (ordersArray.isEmpty()) {
            tvNoOrders.setVisibility(View.VISIBLE);
        } else {
            rvOrders.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void viewOrderDetail(String orderId) {
        startActivity(OrderDetailActivity.newIntent(OrderListingActivity.this)
                .putExtra("orderId", orderId));
        navigateToNext();
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
