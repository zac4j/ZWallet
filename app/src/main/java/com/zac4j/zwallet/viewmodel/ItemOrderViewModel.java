package com.zac4j.zwallet.viewmodel;

import android.content.Context;
import android.databinding.BaseObservable;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import com.zac4j.zwallet.R;
import com.zac4j.zwallet.model.response.DealOrder;
import com.zac4j.zwallet.util.Utils;
import com.zac4j.zwallet.view.OrderDetailDialogFragment;
import java.text.NumberFormat;

/**
 * Main Recent Orders List Item ViewModel
 * Created by Zac on 2016/7/4.
 */
public class ItemOrderViewModel extends BaseObservable implements ViewModel {

  private Context mContext;
  private DealOrder mDealOrder;

  public ItemOrderViewModel(Context context, DealOrder dealOrder) {
    mContext = context;
    mDealOrder = dealOrder;
  }

  public void setDealOrder(DealOrder order) {
    mDealOrder = order;
  }

  public int getOrderAvatar() {
    if ("1".equals(mDealOrder.getType())) {
      return R.drawable.ic_order_receive;
    } else {
      return R.drawable.ic_order_sell;
    }
  }

  public String getProcessName() {
    boolean isPendingOrder = verifyIsPendingOrder(mDealOrder);
    if (isPendingOrder) {
      return "Pending";
    }
    if ("1".equals(mDealOrder.getType())) {
      return "Received";
    } else {
      return "Sold";
    }
  }

  public int getProcessColor() {
    int colorRes;
    if ("1".equals(mDealOrder.getType())) {
      colorRes = R.color.green;
    } else {
      colorRes = R.color.deep_orange;
    }
    return ContextCompat.getColor(mContext, colorRes);
  }

  public String getAmount() {
    boolean isPendingOrder = verifyIsPendingOrder(mDealOrder);
    String priceTxt = mDealOrder.getOrderPrice();
    String amountTxt =
        isPendingOrder ? mDealOrder.getOrderAmount() : mDealOrder.getProcessedAmount();
    double price = 0.0;
    if (!TextUtils.isEmpty(priceTxt)) {
      price = Double.parseDouble(priceTxt);
    }
    double amount = 0.0;
    if (!TextUtils.isEmpty(amountTxt)) {
      amount = Double.parseDouble(amountTxt);
    }

    double total = price * amount;
    NumberFormat formatter = NumberFormat.getNumberInstance();
    formatter.setMaximumFractionDigits(4);
    String totalTxt = formatter.format(total);
    if ("1".equals(mDealOrder.getType())) {
      return "- ￥" + totalTxt;
    } else {
      return "￥" + totalTxt;
    }
  }

  public String getProcessedTime() {
    boolean isPendingOrder = verifyIsPendingOrder(mDealOrder);
    String timeMillisTxt =
        isPendingOrder ? mDealOrder.getOrderTime() : mDealOrder.getLastProcessedTime();
    if (!TextUtils.isEmpty(timeMillisTxt)) {
      long time = Long.parseLong(timeMillisTxt);
      long deltaTime = System.currentTimeMillis() / 1000 - time;
      return Utils.generateTime(deltaTime);
    }
    return "";
  }

  public void onItemClick(View view) {
    boolean isPendingOrder = verifyIsPendingOrder(mDealOrder);
    OrderDetailDialogFragment dialogFragment =
        OrderDetailDialogFragment.newInstance(mDealOrder.getId(),
            isPendingOrder ? mDealOrder.getOrderTime() : mDealOrder.getLastProcessedTime());
    FragmentManager fragmentMgr = ((AppCompatActivity) mContext).getSupportFragmentManager();
    dialogFragment.show(fragmentMgr);
  }

  private boolean verifyIsPendingOrder(DealOrder order) {
    String processedTime = order.getLastProcessedTime();
    return TextUtils.isEmpty(processedTime);
  }

  @Override public void destroy() {
  }
}
