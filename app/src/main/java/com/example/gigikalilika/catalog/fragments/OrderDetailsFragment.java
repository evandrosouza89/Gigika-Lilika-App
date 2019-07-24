package com.example.gigikalilika.catalog.fragments;

import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.gigikalilika.catalog.R;
import com.example.gigikalilika.catalog.adapters.OrderListRecyclerViewAdapter;
import com.example.gigikalilika.catalog.entities.order.Order;
import com.example.gigikalilika.catalog.entities.order.OrderItem;
import com.example.gigikalilika.catalog.utils.Utils;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import lombok.Getter;

import static com.example.gigikalilika.catalog.constants.Constants.ORDER_EMAIL;
import static com.example.gigikalilika.catalog.constants.Constants.ORDER_EMAIL_SUBJECT;
import static com.example.gigikalilika.catalog.constants.Constants.ORDER_MINIMUM_QUANTITY;
import static com.example.gigikalilika.catalog.constants.Constants.ORDER_WHATSAPP_URL;

public class OrderDetailsFragment extends Fragment implements OrderListRecyclerViewAdapter.OrderListUpdateListener, View.OnClickListener {

    private View view;

    private LinearLayout linearLayoutOrderDetails;

    private TextView textViewEmptyOrder;

    private TextView textViewOrderValue;

    private TextView textViewOrderValueOnRequest;

    private OrderListRecyclerViewAdapter orderListRecyclerViewAdapter;

    private Button buttonPlaceWhatsAppOrder;

    private Button buttonPlaceEmailOrder;

    @Getter
    private Order order;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        if (savedInstanceState == null) {
            view = inflater.inflate(R.layout.layout_order_details, container, false);
        } else {
            orderListRecyclerViewAdapter.notifyDataSetChanged();
            onOrderUpdate();
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        this.view = view;
        if (savedInstanceState == null) {
            setup();
            initComponents();
            if (order != null && order.getOrderItemList() != null && !order.getOrderItemList().isEmpty()) {
                orderListRecyclerViewAdapter.notifyDataSetChanged();
                onOrderUpdate();
            }
        }
    }

    private void setup() {
        if (order == null) {
            order = new Order();
        }

        linearLayoutOrderDetails = view.findViewById(R.id.linearLayoutOrderDetails);

        textViewEmptyOrder = view.findViewById(R.id.textViewEmptyOrder);

        textViewOrderValue = view.findViewById(R.id.textViewOrderValue);

        textViewOrderValueOnRequest = view.findViewById(R.id.textViewOrderValueOnRequest);

        buttonPlaceWhatsAppOrder = view.findViewById(R.id.buttonPlaceWhatsAppOrder);

        buttonPlaceEmailOrder = view.findViewById(R.id.buttonPlaceEmailOrder);
    }

    private void initComponents() {
        if (order.getOrderItemList() == null) {
            order.setOrderItemList(new ArrayList<>());
        }

        orderListRecyclerViewAdapter = new OrderListRecyclerViewAdapter(getContext(), order);
        orderListRecyclerViewAdapter.setListener(this);

        initRecyclerView(orderListRecyclerViewAdapter);

        buttonPlaceWhatsAppOrder.setOnClickListener(this);

        buttonPlaceEmailOrder.setOnClickListener(this);
    }

    private void initRecyclerView(OrderListRecyclerViewAdapter orderListRecyclerViewAdapter) {
        RecyclerView recyclerView = view.findViewById(R.id.recyclerViewOrderList);

        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        recyclerView.setAdapter(orderListRecyclerViewAdapter);
    }

    private void updateTextViewOrderValue() {
        textViewOrderValue.setText("Valor total do pedido: ");

        if (order.getValue() != null) {
            textViewOrderValue.setText(textViewOrderValue.getText() + Utils.formatPrice(order.getValue()));
        }

        boolean hasNullValue = order.getOrderItemList().stream().anyMatch(orderItem -> orderItem.getValue() == null);

        if (hasNullValue) {
            if (order.getOrderItemList().size() == 1) {
                textViewOrderValueOnRequest.setVisibility(View.GONE);
                textViewOrderValue.setText(textViewOrderValue.getText() + " sob consulta");
            } else {
                textViewOrderValueOnRequest.setVisibility(View.VISIBLE);
            }
        } else {
            textViewOrderValueOnRequest.setVisibility(View.GONE);
        }

        if (!order.getOrderItemList().isEmpty()) {
            linearLayoutOrderDetails.setVisibility(View.VISIBLE);
            textViewEmptyOrder.setVisibility(View.GONE);
        } else {
            linearLayoutOrderDetails.setVisibility(View.GONE);
            textViewEmptyOrder.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onOrderUpdate() {
        updateTextViewOrderValue();
    }

    public void addOrderItem(OrderItem orderItem) {
        if (order == null) {
            order = new Order();
            order.setOrderItemList(new ArrayList<>());
            order.getOrderItemList().add(orderItem);
        } else {
            order.getOrderItemList().add(orderItem);
            if (orderListRecyclerViewAdapter != null) {
                orderListRecyclerViewAdapter.notifyDataSetChanged();
                onOrderUpdate();
            }
        }
    }

    private void buttonPlaceWhatsAppOrderClick() {
        if (canOrderBePlaced()) {
            placeOrderWhatsapp();
        } else {
            showDenyOrderPlacementPopUp();
        }
    }

    private void placeOrderWhatsapp() {
        try {
            ApplicationInfo applicationInfo = getActivity().getPackageManager().getApplicationInfo("com.whatsapp", 0);

            if (applicationInfo.enabled) {
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(ORDER_WHATSAPP_URL + URLEncoder.encode(buildOrderText(), StandardCharsets.UTF_8.toString())));
                startActivity(i);
            } else {
                showWhatsAppNotInstalledPopUp();
            }

        } catch (PackageManager.NameNotFoundException e) {
            showWhatsAppNotInstalledPopUp();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }

    }

    private void buttonPlaceEmailOrderClick() {
        if (canOrderBePlaced()) {
            placeOrderEmail();
        } else {
            showDenyOrderPlacementPopUp();
        }
    }

    private void placeOrderEmail() {
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("text/plain");
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ORDER_EMAIL});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, ORDER_EMAIL_SUBJECT);
        emailIntent.putExtra(Intent.EXTRA_TEXT, buildOrderText());
        getContext().startActivity(Intent.createChooser(emailIntent, "Enviar email..."));
    }

    private String buildOrderText() {
        StringBuilder orderText = new StringBuilder();
        if (order != null) {
            orderText.append(ORDER_EMAIL_SUBJECT + "\n");
            orderText.append("\n");

            orderText.append("Lista de pedidos: \n");
            orderText.append("\n");

            for (OrderItem orderItem : order.getOrderItemList()) {
                orderText.append(orderItem.toText()).append("\n").append("\n");
            }

            boolean hasNullValue = order.getOrderItemList().stream().anyMatch(orderItem -> orderItem.getValue() == null);

            if (hasNullValue) {
                if (order.getOrderItemList().size() == 1) {
                    orderText.append("Valor total: sob consulta");
                } else {
                    orderText.append("Valor total: ").append(Utils.formatPrice(order.getValue()));
                    orderText.append(" + valores sob consulta");
                }
            } else {
                orderText.append("Valor total: ").append(Utils.formatPrice(order.getValue()));
            }
            orderText.append(".");
        }
        return orderText.toString();
    }

    private void showDenyOrderPlacementPopUp() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom);
        alert.setTitle("Revise seu pedido");
        alert.setMessage("O pedido precisa conter pelo menos " + ORDER_MINIMUM_QUANTITY + " produtos!");
        alert.setPositiveButton("Ok", (dialog, whichButton) -> {
        });
        alert.show();
    }

    private void showWhatsAppNotInstalledPopUp() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getContext(), R.style.AlertDialogCustom);
        alert.setTitle("WhatsApp não instalado");
        alert.setMessage("Baixe e instale o aplicativo através do google play!");
        alert.setPositiveButton("Ok", (dialog, whichButton) -> {
        });
        alert.show();
    }

    private boolean canOrderBePlaced() {
        if (order == null) {
            return false;
        }

        if (order.getOrderItemList() == null || order.getOrderItemList().isEmpty()) {
            return false;
        } else {
            int sum = 0;
            for (OrderItem orderItem : order.getOrderItemList()) {
                sum += orderItem.getQuantity();
            }
            return sum >= ORDER_MINIMUM_QUANTITY;
        }

    }

    @Override
    public void onClick(View view) {
        if (view.equals(buttonPlaceWhatsAppOrder)) {
            buttonPlaceWhatsAppOrderClick();
        } else if (view.equals(buttonPlaceEmailOrder)) {
            buttonPlaceEmailOrderClick();
        }
    }
}