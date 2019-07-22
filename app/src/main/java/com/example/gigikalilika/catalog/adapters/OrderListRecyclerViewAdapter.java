package com.example.gigikalilika.catalog.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.Adapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.gigikalilika.catalog.R;
import com.example.gigikalilika.catalog.customviews.CustomNumberPicker;
import com.example.gigikalilika.catalog.entities.order.Order;
import com.example.gigikalilika.catalog.entities.order.OrderItem;
import com.example.gigikalilika.catalog.entities.product.Product;
import com.example.gigikalilika.catalog.utils.Utils;

import net.steamcrafted.materialiconlib.MaterialIconView;

import lombok.Setter;

public class OrderListRecyclerViewAdapter extends Adapter<OrderListRecyclerViewAdapter.ViewHolder> {

    private final Context context;

    private final LayoutInflater layoutInflater;

    private final Order order;

    @Setter
    private OrderListUpdateListener listener;

    public OrderListRecyclerViewAdapter(Context context, Order order) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.order = order;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_order_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        OrderItem orderItem = order.getOrderItemList().get(position);

        if (orderItem != null && orderItem.getProduct() != null) {
            Product product = orderItem.getProduct();

            if (product.getProductImageList() != null && product.getProductImageList().get(0) != null) {
                holder.imageViewOrderItemProductThumbnail.setImageResource(context.getResources().getIdentifier(product.getProductImageList().get(0).getSourcePath(),
                        R.drawable.class.getSimpleName(), context.getPackageName()));
            }

            holder.textViewOrderItemProductName.setText(product.getName());

            holder.textViewOrderItemProductReferenceCode.setText("Ref. " + product.getReferenceCode());

            if (orderItem.getValue() != null && orderItem.getQuantity() != null && orderItem.getQuantity() != 0) {
                holder.textViewOrderItemProductValue.setText("Valor unitário: R$ " + Utils.formatPrice(orderItem.getValue() / orderItem.getQuantity()));
                holder.textViewOrderItemTotalCost.setText("Sub-total: R$ " + Utils.formatPrice(orderItem.getValue()));
            } else {
                holder.textViewOrderItemProductValue.setText("Valor unitário: sob consulta");
                holder.textViewOrderItemTotalCost.setText("Sub-total: sob consulta");
            }

            holder.textViewOrderItemProductSize.setText("Tamanho: " + orderItem.getSize());

            holder.customNumberPickerQuantity.setValue(orderItem.getQuantity());
        }
    }

    @Override
    public int getItemCount() {
        return order.getOrderItemList().size();
    }

    public void removeItemAt(int position) {
        order.getOrderItemList().remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, order.getOrderItemList().size());
        updateOrder();
    }

    private void updateOrder() {
        Double orderValue = null;
        for (OrderItem orderItem : order.getOrderItemList()) {
            if (orderItem.getValue() != null) {
                if (orderValue == null) {
                    orderValue = orderItem.getValue();
                } else {
                    orderValue += orderItem.getValue();
                }
            }
        }
        order.setValue(orderValue);
        listener.onOrderUpdate();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, CustomNumberPicker.CustomNumberPickerClickListener {

        final View itemView;

        ImageView imageViewOrderItemProductThumbnail;

        TextView textViewOrderItemProductName;

        TextView textViewOrderItemProductReferenceCode;

        TextView textViewOrderItemProductValue;

        TextView textViewOrderItemProductSize;

        MaterialIconView materialIconViewRemoveItem;

        CustomNumberPicker customNumberPickerQuantity;

        TextView textViewOrderItemTotalCost;

        ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;

            setup();

            initComponents();
        }

        private void setup() {
            imageViewOrderItemProductThumbnail = itemView.findViewById(R.id.imageViewOrderItemProductThumbnail);
            textViewOrderItemProductName = itemView.findViewById(R.id.textViewOrderItemProductName);
            textViewOrderItemProductReferenceCode = itemView.findViewById(R.id.textViewOrderItemProductReferenceCode);
            textViewOrderItemProductValue = itemView.findViewById(R.id.textViewOrderItemProductValue);
            textViewOrderItemProductSize = itemView.findViewById(R.id.textViewOrderItemProductSize);
            textViewOrderItemTotalCost = itemView.findViewById(R.id.textViewOrderItemTotalCost);
            customNumberPickerQuantity = itemView.findViewById(R.id.customNumberPickerQuantity);
            materialIconViewRemoveItem = itemView.findViewById(R.id.materialIconViewRemoveItem);
        }

        private void initComponents() {
            customNumberPickerQuantity.setCustomNumberPickerClickListener(this);
            materialIconViewRemoveItem.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onCustomNumberPickerClick(View view) {
            OrderItem orderItem = order.getOrderItemList().get(getAdapterPosition());

            if (orderItem.getValue() != null && orderItem.getQuantity() != null && orderItem.getQuantity() != 0) {
                Double totalValue = orderItem.getValue() / orderItem.getQuantity();
                totalValue *= customNumberPickerQuantity.getValue();
                textViewOrderItemTotalCost.setText("Sub-total: " + Utils.formatPrice(totalValue));
                orderItem.setValue(totalValue);
                updateOrder();
            }

            orderItem.setQuantity(customNumberPickerQuantity.getValue());
        }

        @Override
        public void onClick(View view) {
            if (view.equals(materialIconViewRemoveItem)) {
                removeItemAt(getAdapterPosition());
            }
        }
    }

    public interface OrderListUpdateListener {
        void onOrderUpdate();
    }
}