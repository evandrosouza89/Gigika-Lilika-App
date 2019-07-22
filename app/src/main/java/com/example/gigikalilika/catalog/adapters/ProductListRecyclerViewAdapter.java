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
import com.example.gigikalilika.catalog.entities.product.Product;
import com.example.gigikalilika.catalog.utils.Utils;

import net.steamcrafted.materialiconlib.MaterialIconView;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import lombok.Setter;

public class ProductListRecyclerViewAdapter extends Adapter<ProductListRecyclerViewAdapter.ViewHolder> {

    private final Context context;

    private final List<Product> productList;

    private List<Product> filteredProductList;

    private final LayoutInflater layoutInflater;

    @Setter
    private ProductItemClickListener productItemClickListener;

    public ProductListRecyclerViewAdapter(Context context, List<Product> productList) {
        this.context = context;
        layoutInflater = LayoutInflater.from(context);
        this.productList = productList;
        filteredProductList = new ArrayList(productList);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.layout_product_list, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Product product = filteredProductList.get(position);

        if (product != null) {
            if (product.getProductImageList() != null && product.getProductImageList().get(0) != null) {
                holder.imageViewProductThumbnail.setImageResource(context.getResources().getIdentifier(product.getProductImageList().get(0).getSourcePath(),
                        R.drawable.class.getSimpleName(), context.getPackageName()));
            }
            holder.textViewProductName.setText(product.getName());
            if (product.getProductPriceList() != null && product.getProductPriceList().get(0) != null) {
                holder.textViewProductPrice.setText("A partir de: " + Utils.formatPrice(product.getProductPriceList().get(0).getProductPrice()));
            }

            holder.textViewProductReference.setText("Ref. " + product.getReferenceCode());

            holder.viewProductListItemSeparator.setVisibility(View.VISIBLE);

            if (position == filteredProductList.size() - 1) {
                holder.viewProductListItemSeparator.setVisibility(View.GONE);
            }
        }
    }

    @Override
    public int getItemCount() {
        return filteredProductList.size();
    }

    public void filterByOffer(List<String> referenceList) {
        filteredProductList.clear();

        for (Product product : productList) {
            if (referenceList.stream().anyMatch(reference -> reference.equalsIgnoreCase(product.getReferenceCode()))) {
                filteredProductList.add(product);
            }
        }

        notifyDataSetChanged();
    }

    public void filterByCategory(Long categoryId) {
        filteredProductList.clear();

        for (Product product : productList) {
            if (product.getProductCategoryList().stream().anyMatch(productCategory -> productCategory.getCategoryId().equals(categoryId))) {
                filteredProductList.add(product);
            }
        }

        notifyDataSetChanged();
    }

    public void filter(String newText) {
        if (newText == null || newText.isEmpty()) {
            filteredProductList = new ArrayList(productList);
        } else {
            filteredProductList.clear();

            String finalNewText = newText.toLowerCase();
            List<Product> searchResults = productList.stream().filter(product -> product.getName().toLowerCase().contains(finalNewText) ||
                    product.getDescription().toLowerCase().contains(finalNewText)).collect(Collectors.toList());

            filteredProductList.addAll(searchResults);
        }

        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        final View itemView;

        ImageView imageViewProductThumbnail;

        TextView textViewProductName;

        TextView textViewProductReference;

        TextView textViewProductPrice;

        MaterialIconView materialIconViewAddToOrder;

        View viewProductListItemSeparator;

        ViewHolder(View itemView) {
            super(itemView);

            this.itemView = itemView;

            setup();

            initComponents();
        }

        private void setup() {
            imageViewProductThumbnail = itemView.findViewById(R.id.imageViewProductThumbnail);
            textViewProductName = itemView.findViewById(R.id.textViewProductName);
            textViewProductReference = itemView.findViewById(R.id.textViewProductReference);
            textViewProductPrice = itemView.findViewById(R.id.textViewProductPrice);
            materialIconViewAddToOrder = itemView.findViewById(R.id.materialIconViewAddToOrder);
            viewProductListItemSeparator = itemView.findViewById(R.id.viewProductListItemSeparator);
        }

        private void initComponents() {
            materialIconViewAddToOrder.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (productItemClickListener != null) {
                productItemClickListener.onProductListRecyclerViewItemClick(view, itemView, getAdapterPosition());
            }
        }
    }

    public Product getItem(int id) {
        return filteredProductList.get(id);
    }

    public interface ProductItemClickListener {
        void onProductListRecyclerViewItemClick(View view, View itemView, int position);
    }
}